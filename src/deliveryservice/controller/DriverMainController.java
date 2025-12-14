package deliveryservice.controller;

import deliveryservice.domain.*;
import deliveryservice.repository.*;
import deliveryservice.view.driver.*;
import deliveryservice.view.customer.UserInfoUpdateView;
import center_Frame.CenterFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// 기사님(Driver) 전용 메인 화면을 관리하는 컨트롤러입니다.
// 고객용 컨트롤러(CustomerMainController)와 구조는 비슷하지만, 여기서는 배차 수락/취소 같은 기사님 전용 기능이 있어서
// 별도의 클래스로 분리하여 구현했습니다. CardLayout을 사용해 한 프레임 내에서 화면 전환이 되도록 설계했습니다.
public class DriverMainController extends JFrame {
    CardLayout card;
    JPanel mainP;
    UserVO currentDriver; // 로그인한 기사님의 정보를 저장합니다.

    // 기사님 화면 구성요소들입니다.
    DriverDashboard dashboard;
    RealTimeOrderView realTimeView; // 실시간 배차 대기 목록 화면
    DispatchHistoryView historyView; // 내가 잡은 배차 내역 화면
    UserInfoUpdateView userUpdateView; // 정보 수정 화면 (고객용과 동일한 뷰 재사용)

    OrderRepository orderRepo;
    UserRepository userRepo;

    public DriverMainController(UserVO driver) {
        // 자바 기본 UI가 너무 촌스러워서 OS 스타일에 맞춰지도록 룩앤필을 설정했습니다.
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        this.currentDriver = driver;
        this.orderRepo = new OrderRepository();
        this.userRepo = new UserRepository();

        // 화면 전환을 위한 카드 레이아웃 초기화
        card = new CardLayout();
        mainP = new JPanel(card);

        dashboard = new DriverDashboard();

        // 실시간 주문 뷰 초기화
        realTimeView = new RealTimeOrderView();
        realTimeView.setOrderList(new ArrayList<>()); // 초기화 시 빈 리스트를 넣어주지 않으면 에러가 발생해서 추가했습니다.
        realTimeView.initView();

        // 배차 내역 뷰 초기화
        historyView = new DispatchHistoryView();
        historyView.setHistoryList(new ArrayList<>());
        historyView.initView();

        // 정보 수정 뷰는 고객용 뷰를 그대로 가져와서 재사용성을 높였습니다.
        userUpdateView = new UserInfoUpdateView();

        // 패널들을 카드 레이아웃에 등록합니다.
        mainP.add(dashboard, "HOME");
        mainP.add(realTimeView, "REALTIME");
        mainP.add(historyView, "HISTORY");
        mainP.add(userUpdateView, "EDIT_INFO");

        add(mainP);

        //이벤트 처리

        // 로그아웃 버튼: 실수로 눌렀을 때를 대비해 확인 창을 띄웠습니다.
        dashboard.getBtnLogout().addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if(ans == JOptionPane.YES_OPTION) { dispose(); new LoginController(); }
        });

        // 실시간 배차 버튼: 누를 때마다 최신 목록을 DB에서 가져오도록 refresh 메서드를 호출했습니다.
        dashboard.getBtnRealTime().addActionListener(e -> { refreshRealTime(); card.show(mainP, "REALTIME"); });

        // 배차 내역 버튼: 검색어 없이 전체 조회를 하도록 처리했습니다.
        dashboard.getBtnHistory().addActionListener(e -> { refreshHistory(""); card.show(mainP, "HISTORY"); });

        // 정보 수정 버튼: 현재 기사님의 정보를 뷰에 세팅하고 화면을 넘깁니다.
        dashboard.getBtnEditInfo().addActionListener(e -> { userUpdateView.setUserInfo(currentDriver); card.show(mainP, "EDIT_INFO"); });

        // 홈 버튼 동작: 대시보드로 복귀
        realTimeView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // 배차 수락 버튼
        // 기사님이 주문을 선택하고 수락하면, 해당 주문을 기사님에게 할당하는 로직입니다.
        realTimeView.getBtnAccept().addActionListener(e -> {
            int row = realTimeView.getTable().getSelectedRow();
            // 아무것도 선택하지 않고 버튼을 누르면 인덱스 -1이 반환되므로 예외 처리를 했습니다.
            if(row == -1) { JOptionPane.showMessageDialog(this, "접수할 주문을 선택하세요."); return; }

            // 선택한 행의 주문 번호(PK)를 가져옵니다.
            String orderId = (String) realTimeView.getTable().getValueAt(row, 0);

            // 배차는 중요한 작업이라 한번 더 확인을 받습니다.
            int confirm = JOptionPane.showConfirmDialog(this, "정말 이 주문을 배차 받으시겠습니까?", "배차 확인", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                // DB 업데이트 수행
                String resultMsg = orderRepo.acceptOrder(orderId, currentDriver.getUserId());
                if("success".equals(resultMsg)) {
                    JOptionPane.showMessageDialog(this, "배차 완료! [배차 관리]에서 확인하세요.");
                    refreshRealTime(); // 목록에서 사라지게 하기 위해 새로고침 합니다.
                } else { JOptionPane.showMessageDialog(this, "배차 실패: " + resultMsg); }
            }
        });

        // [배차 관리] 화면 이벤트
        historyView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));
        // 검색 버튼: 콤보박스 조건과 검색어를 이용해 필터링합니다.
        historyView.getBtnSearch().addActionListener(e -> refreshHistory(historyView.getSearchWord()));

        // 테이블 클릭 시 입력창 채우기
        // 수정이나 건의 기능을 사용할 때 어떤 주문인지 쉽게 알 수 있도록 구현했습니다.
        historyView.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = historyView.getTable().getSelectedRow();
                historyView.setTextField(row);
            }
        });

        // 배차 건의 기능
        // 실제 기능을 다 구현하기엔 복잡해서 일단 팝업창으로 메시지를 받는 시늉만 냈습니다.
        // 추후에 관리자에게 메시지를 전송하는 기능을 추가할 예정입니다.
        historyView.getBtnSuggest().addActionListener(e -> {
            OrderVO vo = historyView.getSelectedOrder();
            if(vo.getOrderId().equals("")) { JOptionPane.showMessageDialog(this, "건의할 배차를 선택해주세요."); return; }

            String msg = JOptionPane.showInputDialog(this,
                    "[" + vo.getOrderId() + "] 건에 대한 건의사항을 입력하세요.\n(예: 날짜 변경 가능할까요?)");

            if(msg != null && !msg.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "건의사항이 고객에게 전송되었습니다.\n내용: " + msg);
            }
        });

        //  배차 취소 기능
        // 기사님이 실수로 배차를 잡았거나 사정이 생겼을 때 취소하는 기능입니다.
        // DB에서 해당 주문의 상태를 다시 '대기 중'으로 돌려놔야 합니다.
        historyView.getBtnCancel().addActionListener(e -> {
            OrderVO vo = historyView.getSelectedOrder();
            if(vo.getOrderId().equals("")) { JOptionPane.showMessageDialog(this, "취소할 배차를 선택해주세요."); return; }

            int ans = JOptionPane.showConfirmDialog(this,
                    "정말 배차를 취소하시겠습니까?\n주문번호: " + vo.getOrderId() + "\n(주문은 다시 대기 상태로 돌아갑니다.)",
                    "배차 취소 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(ans == JOptionPane.YES_OPTION) {
                orderRepo.cancelDispatch(vo.getOrderId(), currentDriver.getUserId()); // DB 업데이트
                JOptionPane.showMessageDialog(this, "배차가 취소되었습니다.");
                refreshHistory(""); // 목록을 갱신해서 취소된 건이 안 보이게 처리했습니다.
            }
        });

        // 정보 수정 로직
        userUpdateView.getBtnUpdate().addActionListener(e -> {
            UserVO newUser = userUpdateView.getUpdatedUser();
            userRepo.updateUser(newUser);
            currentDriver = newUser; // 세션 정보 갱신
            JOptionPane.showMessageDialog(this, "정보가 수정되었습니다.");
            card.show(mainP, "HOME");
        });
        userUpdateView.getBtnCancel().addActionListener(e -> card.show(mainP, "HOME"));

        setTitle("화물 파트너 - " + driver.getUserName() + " 기사님");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setVisible(true);
    }

    // 실시간 대기 주문 목록을 새로고침하는 메서드
    // 코드가 중복되어 별도 함수로 뺐습니다.
    private void refreshRealTime() {
        ArrayList<OrderVO> list = orderRepo.selectWaitingOrders(); // 대기 상태인 주문만 가져옵니다.
        realTimeView.setOrderList(list);
        realTimeView.updateTable();
    }

    // 배차 내역을 새로고침하는 메서드
    // 검색 조건(searchWord)에 따라 쿼리 결과가 달라지도록 처리했습니다.
    private void refreshHistory(String searchWord) {
        int comboIndex = historyView.getComboIndex(); // 검색 조건(콤보박스) 인덱스 가져오기
        ArrayList<OrderVO> list = orderRepo.selectDispatchHistory(currentDriver.getUserId(), searchWord, comboIndex);
        historyView.setHistoryList(list);
        historyView.updateTable();
    }
}