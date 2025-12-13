package deliveryservice.controller;

import deliveryservice.domain.*;
import deliveryservice.repository.*;
import deliveryservice.view.driver.*;
import deliveryservice.view.customer.UserInfoUpdateView;
import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DriverMainController extends JFrame {
    CardLayout card;
    JPanel mainP;
    UserVO currentDriver;

    // 뷰들
    DriverDashboard dashboard;
    RealTimeOrderView realTimeView;
    DispatchHistoryView historyView;
    UserInfoUpdateView userUpdateView;

    // 리포지토리
    OrderRepository orderRepo;
    UserRepository userRepo;

    public DriverMainController(UserVO driver) {
        this.currentDriver = driver;
        this.orderRepo = new OrderRepository();
        this.userRepo = new UserRepository();

        card = new CardLayout();
        mainP = new JPanel(card);

        // 1. 뷰 생성 및 초기화
        dashboard = new DriverDashboard();

        realTimeView = new RealTimeOrderView();
        realTimeView.setOrderList(new ArrayList<>());
        realTimeView.initView();

        historyView = new DispatchHistoryView();
        historyView.setHistoryList(new ArrayList<>());
        historyView.initView();

        userUpdateView = new UserInfoUpdateView();

        // 2. 패널 추가
        mainP.add(dashboard, "HOME");
        mainP.add(realTimeView, "REALTIME");
        mainP.add(historyView, "HISTORY");
        mainP.add(userUpdateView, "EDIT_INFO");

        add(mainP);

        // --- [이벤트 핸들러] ---

        // 1. [대시보드 -> 실시간 주문]
        dashboard.getBtnRealTime().addActionListener(e -> {
            refreshRealTime();
            card.show(mainP, "REALTIME");
        });

        // 2. [대시보드 -> 배차 내역]
        dashboard.getBtnHistory().addActionListener(e -> {
            refreshHistory(""); // 처음에 검색어 없이 전체 조회
            card.show(mainP, "HISTORY");
        });

        // 3. [대시보드 -> 내 정보 수정]
        dashboard.getBtnEditInfo().addActionListener(e -> {
            userUpdateView.setUserInfo(currentDriver);
            card.show(mainP, "EDIT_INFO");
        });

        // 4. [실시간 주문] 홈으로
        realTimeView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // 5. [실시간 주문] 주문 수락 (핵심 기능)
        realTimeView.getBtnAccept().addActionListener(e -> {
            int row = realTimeView.getTable().getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this, "접수할 주문을 선택하세요.");
                return;
            }

            // 테이블 0번째 컬럼이 OrderID라고 가정
            String orderId = (String) realTimeView.getTable().getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "정말 이 주문을 배차 받으시겠습니까?", "배차 확인", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION) {
                // DB 트랜잭션 실행
                String resultMsg = orderRepo.acceptOrder(orderId, currentDriver.getUserId());

                if("success".equals(resultMsg)) {
                    JOptionPane.showMessageDialog(this, "배차 완료! [배차 내역]에서 확인하세요.");
                    refreshRealTime(); // 목록 갱신 (수락한 건 사라짐)
                } else {
                    JOptionPane.showMessageDialog(this, "배차 실패: " + resultMsg);
                }
            }
        });

        // 6. [배차 내역] 홈으로
        historyView.getBtnHome().addActionListener(e -> card.show(mainP, "HOME"));

        // 7. [배차 내역] 검색 버튼 (★ 추가된 기능)
        historyView.getBtnSearch().addActionListener(e -> {
            String searchWord = historyView.getSearchWord();
            refreshHistory(searchWord);
        });

        // 8. [정보 수정] 기능
        userUpdateView.getBtnUpdate().addActionListener(e -> {
            UserVO newUser = userUpdateView.getUpdatedUser();
            userRepo.updateUser(newUser);
            currentDriver = newUser;
            JOptionPane.showMessageDialog(this, "정보가 수정되었습니다.");
            card.show(mainP, "HOME");
        });
        userUpdateView.getBtnCancel().addActionListener(e -> card.show(mainP, "HOME"));

        // 프레임 설정
        setTitle("화물 파트너 - " + driver.getUserName() + " 기사님");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            CenterFrame cf = new CenterFrame(800, 600);
            cf.centerXY();
            setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        } catch(Exception e) { setSize(800, 600); setLocationRelativeTo(null); }
        setVisible(true);
    }

    // 실시간 대기 주문 갱신
    private void refreshRealTime() {
        ArrayList<OrderVO> list = orderRepo.selectWaitingOrders();
        realTimeView.setOrderList(list);
        realTimeView.updateTable();
    }

    // 내 배차 내역 갱신 (검색어 포함)
    private void refreshHistory(String searchWord) {
        int comboIndex = historyView.getComboIndex();
        ArrayList<OrderVO> list = orderRepo.selectDispatchHistory(currentDriver.getUserId(), searchWord, comboIndex);
        historyView.setHistoryList(list);
        historyView.updateTable();
    }
}