package deliveryservice.view.customer;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JPanel {
    JButton btnShip;      // 배송하기 (위)
    JButton btnHistory;   // 내주문내역 (좌하)
    JButton btnEditInfo;  // 내정보수정 (우하)

    public CustomerDashboard() {
        setLayout(null); // 삼각형 배치를 위해 null layout 사용 (좌표 지정)
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("원하시는 서비스를 선택하세요", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        lblTitle.setBounds(0, 50, 800, 50);
        add(lblTitle);

        // 1. 배송하기 (중앙 상단)
        btnShip = new JButton("배송하기");
        btnShip.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        btnShip.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        btnShip.setForeground(Color.WHITE);
        btnShip.setBounds(300, 150, 200, 100);
        add(btnShip);

        // 2. 내 주문 내역 (왼쪽 하단)
        btnHistory = new JButton("내 주문 내역");
        btnHistory.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        btnHistory.setBounds(150, 300, 180, 80);
        add(btnHistory);

        // 3. 내 정보 수정 (오른쪽 하단)
        btnEditInfo = new JButton("내 정보 수정");
        btnEditInfo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        btnEditInfo.setBounds(470, 300, 180, 80);
        add(btnEditInfo);
    }

    public JButton getBtnShip() { return btnShip; }
    public JButton getBtnHistory() { return btnHistory; }
    public JButton getBtnEditInfo() { return btnEditInfo; }
}