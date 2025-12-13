package deliveryservice.controller;

import deliveryservice.domain.*;
import deliveryservice.repository.*;
import deliveryservice.view.*; // DatePicker 때문에 필요
import deliveryservice.view.customer.*;
import deliveryservice.view.order.*;
import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomerMainController extends JFrame {
    CardLayout card;
    JPanel mainP;
    UserVO currentUser;

    CustomerDashboard dashboard;
    OrderInsertView insertView;
    OrderSearchView searchView;
    OrderUpdateView updateView;
    UserInfoUpdateView userUpdateView;
    EditSelectPanel editSelectPanel;

    OrderRepository orderRepo;
    UserRepository userRepo;

    public CustomerMainController(UserVO user) {
        this.currentUser = user;
        this.orderRepo = new OrderRepository();
        this.userRepo = new UserRepository();

        card = new CardLayout();
        mainP = new JPanel(card);

        // 뷰 생성 및 초기화
        dashboard = new CustomerDashboard();

        insertView = new OrderInsertView();

        searchView = new OrderSearchView();
        searchView.setOrderVOList(new ArrayList<>());
        searchView.initView();

        editSelectPanel = new EditSelectPanel();
        userUpdateView = new UserInfoUpdateView();

        updateView = new OrderUpdateView();
        updateView.setOrderVOList(new ArrayList<>());
        updateView.initView();

        // 패널 추가
        mainP.add(dashboard, "HOME");
        mainP.add(insertView, "INSERT");
        mainP.add(searchView, "SEARCH");
        mainP.add(editSelectPanel, "EDIT_SELECT");
        mainP.add(userUpdateView, "USER_EDIT");
        mainP.add(updateView, "ORDER_EDIT");

        add(mainP);

        // --- 이벤트 핸들러 ---

        // [메인] 배송하기
        dashboard.getBtnShip().addActionListener(e -> {
            insertView.setUserId(currentUser.getUserId());
            insertView.clear();
            card.show(mainP, "INSERT");
        });

        // [메인] 내 주문 내역
        dashboard.getBtnHistory().addActionListener(e -> {
            loadMyOrders();
            card.show(mainP, "SEARCH");
        });

        // [메인] 내 정보 수정
        dashboard.getBtnEditInfo().addActionListener(e -> card.show(mainP, "EDIT_SELECT"));

        // [주문 등록] 날짜 선택 버튼 (★ 추가된 부분)
        insertView.getBtnDate().addActionListener(e -> {
            // 달력 팝업 띄우기
            DatePicker dp = new DatePicker(this);
            // 선택된 날짜 가져오기
            String selectedDate = dp.setPickedDate();
            // 텍스트 필드에 넣기
            insertView.setDate(selectedDate);
        });

        // [주문 등록] 제출 -> DB저장 -> 목록화면으로
        insertView.getBtnSubmit().addActionListener(e -> {
            OrderVO vo = insertView.getOrderData();
            orderRepo.insert(vo);
            JOptionPane.showMessageDialog(this, "배송 신청이 완료되었습니다!");
            loadMyOrders();
            card.show(mainP, "SEARCH");
        });

        // [주문 등록] 취소/홈으로
        insertView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // [주문 검색] 홈으로 버튼 (★ 추가된 부분)
        searchView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // [수정 선택]
        editSelectPanel.getBtnEditProfile().addActionListener(e -> {
            userUpdateView.setUserInfo(currentUser);
            card.show(mainP, "USER_EDIT");
        });

        editSelectPanel.getBtnEditOrder().addActionListener(e -> {
            refreshUpdateView();
            card.show(mainP, "ORDER_EDIT");
        });

        // [개인정보 수정]
        userUpdateView.getBtnUpdate().addActionListener(e -> {
            UserVO newUser = userUpdateView.getUpdatedUser();
            userRepo.updateUser(newUser);
            currentUser = newUser;
            JOptionPane.showMessageDialog(this, "회원정보가 수정되었습니다.");
            card.show(mainP, "HOME");
        });
        userUpdateView.getBtnCancel().addActionListener(e -> card.show(mainP, "HOME"));

        // 프레임 설정
        setTitle("화물 서비스 - " + user.getUserName() + "님");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            CenterFrame cf = new CenterFrame(800, 600);
            cf.centerXY();
            setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        } catch(Exception e) { setSize(800, 600); }
        setVisible(true);
    }

    private void loadMyOrders() {
        ArrayList<OrderVO> list = orderRepo.select("", 0);
        searchView.setOrderVOList(list);
        searchView.pubSearchResult();
    }

    private void refreshUpdateView() {
        ArrayList<OrderVO> list = orderRepo.select("", 0);
        updateView.setOrderVOList(list);
        updateView.putSearchResult();
    }
}