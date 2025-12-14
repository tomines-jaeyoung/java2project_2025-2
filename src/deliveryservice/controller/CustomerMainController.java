package deliveryservice.controller;

import deliveryservice.domain.*;
import deliveryservice.repository.*;
import deliveryservice.view.*;
import deliveryservice.view.customer.*;
import deliveryservice.view.order.*;
import center_Frame.CenterFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// 고객 화면 전체를 관리하는 메인 컨트롤러 클래스입니다.
// 처음에는 화면마다 프레임을 새로 띄울까 고민했는데, 너무 지저분해 보여서
// CardLayout을 사용해 하나의 창 안에서 화면만 바뀌도록 구현해 보았습니다.
public class CustomerMainController extends JFrame {
    CardLayout card;
    JPanel mainP;
    UserVO currentUser; // 로그인할 때 받아온 사용자 정보를 유지하기 위해 선언했습니다.

    // 관리해야 할 뷰(화면)들이 많아서 변수가 좀 많아졌습니다.
    CustomerDashboard dashboard;
    OrderInsertView insertView;
    OrderSearchView searchView;
    OrderUpdateView updateView;
    UserInfoUpdateView userUpdateView;
    EditSelectPanel editSelectPanel;

    // 데이터베이스와 통신하는 리포지토리 객체들입니다.
    OrderRepository orderRepo;
    UserRepository userRepo;

    public CustomerMainController(UserVO user) {
        // 자바 기본 디자인(Metal)이 너무 옛날 느낌이라, 현재 운영체제 스타일에 맞춰지도록 설정했습니다.
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        this.currentUser = user;
        this.orderRepo = new OrderRepository();
        this.userRepo = new UserRepository();

        // 화면 전환을 위한 카드 레이아웃 초기화
        card = new CardLayout();
        mainP = new JPanel(card);

        // 각 화면 패널들을 생성합니다.
        dashboard = new CustomerDashboard();
        insertView = new OrderInsertView();

        searchView = new OrderSearchView();
        searchView.setOrderVOList(new ArrayList<>()); // 이 부분을 빼먹으니 NullPointerException이 발생해서 빈 리스트로 초기화했습니다.
        searchView.initView();

        editSelectPanel = new EditSelectPanel();
        userUpdateView = new UserInfoUpdateView();

        updateView = new OrderUpdateView();
        updateView.setOrderVOList(new ArrayList<>()); // 수정 화면도 마찬가지로 리스트 초기화를 먼저 진행했습니다.
        updateView.initView();

        // 카드 레이아웃에 패널들을 추가하면서 각각 식별할 수 있는 이름을 붙였습니다.
        // 이 이름(String)을 통해 화면을 전환할 수 있습니다.
        mainP.add(dashboard, "HOME");
        mainP.add(insertView, "INSERT");
        mainP.add(searchView, "SEARCH");
        mainP.add(editSelectPanel, "EDIT_SELECT");
        mainP.add(userUpdateView, "USER_EDIT");
        mainP.add(updateView, "ORDER_EDIT");

        add(mainP);

        // 이벤트 처리
        // 버튼 클릭 시 동작을 정의합니다. 코드가 길어져서 람다식을 활용해 보았습니다.

        // 로그아웃 버튼: 실수로 눌렀을 때 바로 꺼지면 불편할 것 같아 확인 창을 띄우도록 했습니다.
        dashboard.getBtnLogout().addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if(ans == JOptionPane.YES_OPTION) { dispose(); new LoginController(); }
        });

        // 배송 신청 화면으로 이동
        dashboard.getBtnShip().addActionListener(e -> {
            insertView.setUserId(currentUser.getUserId()); // 사용자 아이디를 자동으로 채워줍니다.
            insertView.clear(); // 이전에 입력하다 만 내용이 남아있어서, 들어갈 때마다 초기화하도록 수정했습니다.
            card.show(mainP, "INSERT");
        });

        // 배송 내역 조회: 버튼을 누를 때마다 DB에서 최신 정보를 가져와야 해서 loadMyOrders 메소드를 호출했습니다.
        dashboard.getBtnHistory().addActionListener(e -> { loadMyOrders(); card.show(mainP, "SEARCH"); });

        // 정보 수정 선택 화면으로 이동
        dashboard.getBtnEditInfo().addActionListener(e -> card.show(mainP, "EDIT_SELECT"));

        // 날짜 선택 버튼: 달력 기능이 복잡해서 DatePicker 클래스를 따로 만들어 분리했습니다.
        insertView.getBtnDate().addActionListener(e -> {
            DatePicker dp = new DatePicker(this);
            insertView.setDate(dp.setPickedDate());
        });

        // 신청 완료 버튼
        insertView.getBtnSubmit().addActionListener(e -> {
            String result = orderRepo.insert(insertView.getOrderData());
            if("success".equals(result)) {
                JOptionPane.showMessageDialog(this, "배송 신청이 완료되었습니다!");
                loadMyOrders(); // 신청하자마자 목록에도 반영되도록 리로드했습니다.
                card.show(mainP, "SEARCH");
            } else {
                // 실패했을 경우 어떤 에러인지 알 수 있게 메시지를 띄우도록 처리했습니다.
                JOptionPane.showMessageDialog(this, "주문 등록 실패!\n원인: " + result, "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 홈으로 돌아가기 버튼들
        insertView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));
        searchView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // 회원 정보 수정 화면 진입 시 현재 정보를 미리 보여주도록 설정했습니다.
        editSelectPanel.getBtnEditProfile().addActionListener(e -> {
            userUpdateView.setUserInfo(currentUser);
            card.show(mainP, "USER_EDIT");
        });

        // 주문 수정 화면 진입
        editSelectPanel.getBtnEditOrder().addActionListener(e -> {
            refreshUpdateView(); // 데이터 동기화를 위해 진입 시점에 DB 조회를 합니다.
            card.show(mainP, "ORDER_EDIT");
        });

        // 회원 정보 실제 업데이트 로직
        userUpdateView.getBtnUpdate().addActionListener(e -> {
            UserVO newUser = userUpdateView.getUpdatedUser();
            userRepo.updateUser(newUser);
            currentUser = newUser; // DB만 바꾸고 여기 변수를 안 바꿨더니 옛날 정보가 떠서, 갱신해 주었습니다.
            JOptionPane.showMessageDialog(this, "회원정보가 수정되었습니다.");
            card.show(mainP, "HOME");
        });
        userUpdateView.getBtnCancel().addActionListener(e -> card.show(mainP, "HOME"));

        updateView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // 테이블의 행을 클릭했을 때 데이터를 입력창으로 가져오는 기능입니다.
        // 마우스 이벤트를 사용해 선택된 행(row)의 인덱스를 찾아내는 방식으로 구현했습니다.
        updateView.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = updateView.getTable().getSelectedRow();
                updateView.setTextField(row);
            }
        });

        // 주문 수정 버튼 로직
        updateView.getBtnUpdate().addActionListener(e -> {
            OrderVO vo = updateView.neededUpdateData();
            int result = orderRepo.update(vo);
            if(result > 0) {
                JOptionPane.showMessageDialog(this, "주문이 수정되었습니다.");
                refreshUpdateView(); // 수정된 내용이 테이블에 바로 반영되도록 새로고침 했습니다.
            } else {
                JOptionPane.showMessageDialog(this, "수정 실패!");
            }
        });

        // 주문 취소(삭제) 버튼 로직
        updateView.getBtnDelete().addActionListener(e -> {
            // 삭제할 데이터를 가져옵니다.
            OrderVO vo = updateView.neededUpdateData();

            // 목록에서 선택하지 않고 버튼을 누르면 에러가 날 수 있어서 예외 처리를 했습니다.
            if(vo.getOrderId().equals("")) {
                JOptionPane.showMessageDialog(this, "취소할 주문을 목록에서 선택해주세요.");
                return;
            }

            // 삭제는 중요한 작업이라 실수 방지를 위해 다시 한번 물어보게 구현했습니다.
            int ans = JOptionPane.showConfirmDialog(this,
                    "정말 이 주문을 취소(삭제)하시겠습니까?\n주문번호: " + vo.getOrderId(),
                    "주문 취소 확인",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(ans == JOptionPane.YES_OPTION) {
                orderRepo.delete(vo); // DB에서 삭제
                JOptionPane.showMessageDialog(this, "주문이 취소되었습니다.");
                refreshUpdateView(); // 삭제 후 목록을 다시 불러와야 삭제된 게 눈에 보입니다.

                // 입력창 비우기 (선택적)
                // updateView.clearFields(); // 필요시 구현
            }
        });

        setTitle("화물 서비스 - " + user.getUserName() + "님");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 창이 모니터 정중앙에 뜨게 설정했습니다.
        setVisible(true);
    }

    // 주문 목록을 불러오는 코드가 중복되어 별도 메소드로 분리했습니다.
    // 코드 재사용성을 높이기 위해 만들었습니다.
    private void loadMyOrders() {
        ArrayList<OrderVO> list = orderRepo.select("", 0);
        searchView.setOrderVOList(list);
        searchView.pubSearchResult();
    }

    // 수정 화면의 테이블을 새로고침하는 메소드입니다.
    private void refreshUpdateView() {
        ArrayList<OrderVO> list = orderRepo.select("", 0);
        updateView.setOrderVOList(list);
        updateView.putSearchResult();
    }
}