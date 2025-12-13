package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderInsertView extends JPanel {
    JTextField tfOrigin, tfDest, tfCargoName, tfLoad, tfDate, tfCost;
    JLabel lblUserId;
    JButton btnSubmit, btnHome;
    JButton btnDate; // ★ 날짜 선택 버튼 추가

    public OrderInsertView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("화물 배송 신청서", SwingConstants.CENTER);
        title.setFont(new Font("궁서", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), " 배송 정보 입력 ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("맑은 고딕", Font.BOLD, 14)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // 1. 고객 ID
        addLabel(formPanel, "고객 ID:", 0, 0, c);
        lblUserId = new JLabel("Loading...");
        lblUserId.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUserId.setForeground(Color.BLUE);
        c.gridx = 1; c.gridy = 0;
        formPanel.add(lblUserId, c);

        // 2. 출발지
        addLabel(formPanel, "출발지:", 0, 1, c);
        tfOrigin = new JTextField(20);
        c.gridx = 1; c.gridy = 1; formPanel.add(tfOrigin, c);

        // 3. 도착지
        addLabel(formPanel, "도착지:", 0, 2, c);
        tfDest = new JTextField(20);
        c.gridx = 1; c.gridy = 2; formPanel.add(tfDest, c);

        // 4. 화물 정보
        addLabel(formPanel, "화물 이름:", 0, 3, c);
        tfCargoName = new JTextField(20);
        c.gridx = 1; c.gridy = 3; formPanel.add(tfCargoName, c);

        addLabel(formPanel, "화물 적재량(kg):", 0, 4, c);
        tfLoad = new JTextField(20);
        c.gridx = 1; c.gridy = 4; formPanel.add(tfLoad, c);

        // 5. 배송 일시 (★ 수정됨: 텍스트필드 + 버튼)
        addLabel(formPanel, "배송 희망일시:", 0, 5, c);

        JPanel pDate = new JPanel(new BorderLayout(5, 0)); // 내부 패널
        pDate.setBackground(new Color(245, 245, 245)); // 배경색 일치

        tfDate = new JTextField();
        tfDate.setEditable(false); // 직접 입력 금지 (달력으로만 선택)

        btnDate = new JButton("📅"); // 달력 아이콘 버튼
        btnDate.setPreferredSize(new Dimension(50, 25));

        pDate.add(tfDate, BorderLayout.CENTER);
        pDate.add(btnDate, BorderLayout.EAST);

        c.gridx = 1; c.gridy = 5;
        formPanel.add(pDate, c);

        // 6. 운임
        addLabel(formPanel, "운임(원):", 0, 6, c);
        tfCost = new JTextField(20);
        c.gridx = 1; c.gridy = 6; formPanel.add(tfCost, c);

        add(formPanel, BorderLayout.CENTER);

        // --- 하단 버튼 ---
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnSubmit = new JButton("주문 등록하기");
        btnSubmit.setPreferredSize(new Dimension(150, 45));
        btnSubmit.setBackground(new Color(34, 139, 34));
        btnSubmit.setForeground(Color.WHITE);

        btnHome = new JButton("취소 / 홈으로");
        btnHome.setPreferredSize(new Dimension(120, 45));

        btnPanel.add(btnHome);
        btnPanel.add(btnSubmit);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void addLabel(JPanel p, String text, int x, int y, GridBagConstraints c) {
        c.gridx = x; c.gridy = y;
        JLabel l = new JLabel(text, SwingConstants.RIGHT);
        l.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        p.add(l, c);
    }

    public void setUserId(String id) { lblUserId.setText(id); }
    public void setDate(String date) { tfDate.setText(date); } // 날짜 세팅용

    public JButton getBtnSubmit() { return btnSubmit; }
    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnDate() { return btnDate; } // 달력 버튼 getter

    public OrderVO getOrderData() {
        OrderVO vo = new OrderVO();
        vo.setUserId(lblUserId.getText());
        vo.setOrigin(tfOrigin.getText());
        vo.setDest(tfDest.getText());
        vo.setCargoInfo(tfCargoName.getText() + " (" + tfLoad.getText() + "kg)");
        // 날짜가 비어있으면 오늘 날짜로 대체하는 등 처리 가능
        try { vo.setPrice(Integer.parseInt(tfCost.getText())); }
        catch(Exception e) { vo.setPrice(0); }
        return vo;
    }

    public void clear() {
        tfOrigin.setText(""); tfDest.setText(""); tfCargoName.setText("");
        tfLoad.setText(""); tfDate.setText(""); tfCost.setText("");
    }
}