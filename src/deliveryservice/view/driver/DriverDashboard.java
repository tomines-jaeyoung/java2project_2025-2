package deliveryservice.view.driver;

import javax.swing.*;
import java.awt.*;

public class DriverDashboard extends JPanel {
    JButton btnRealTime;   // 실시간 주문
    JButton btnHistory;    // 배차 내역
    JButton btnEditInfo;   // 내 정보 수정

    public DriverDashboard() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("기사님, 오늘도 안전 운전하세요!", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBounds(0, 50, 800, 50);
        add(lblTitle);

        // 1. 실시간 주문 (상단 중앙)
        btnRealTime = new JButton("실시간 주문 접수");
        btnRealTime.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        btnRealTime.setBackground(new Color(255, 140, 0)); // Dark Orange
        btnRealTime.setForeground(Color.WHITE);
        btnRealTime.setBounds(300, 150, 200, 100);
        add(btnRealTime);

        // 2. 배차 내역 (좌하단)
        btnHistory = new JButton("배차 내역");
        btnHistory.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        btnHistory.setBounds(150, 300, 180, 80);
        add(btnHistory);

        // 3. 내 정보 수정 (우하단)
        btnEditInfo = new JButton("내 정보 수정");
        btnEditInfo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        btnEditInfo.setBounds(470, 300, 180, 80);
        add(btnEditInfo);
    }

    public JButton getBtnRealTime() { return btnRealTime; }
    public JButton getBtnHistory() { return btnHistory; }
    public JButton getBtnEditInfo() { return btnEditInfo; }
}