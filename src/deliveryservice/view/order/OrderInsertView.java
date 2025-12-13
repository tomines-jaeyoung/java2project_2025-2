package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrderInsertView extends JPanel {
    JTextField tfOrigin, tfDest, tfCargoName, tfLoad, tfDate, tfCost;

    // ★ 수정됨: 콤보박스 대신 JSpinner 사용 (시간:분 조절용)
    JSpinner timeSpinner;

    JLabel lblUserId;
    JButton btnSubmit, btnHome;
    JButton btnDate;

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

        // 5. 배송 일시 (★ 수정됨: 날짜 + 분 단위 시간 조절)
        addLabel(formPanel, "배송 희망일시:", 0, 5, c);

        JPanel pDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pDate.setBackground(new Color(245, 245, 245));

        tfDate = new JTextField(10);
        tfDate.setEditable(false);
        btnDate = new JButton("📅");
        btnDate.setPreferredSize(new Dimension(50, 25));

        // ★ JSpinner 설정 (시간:분, 1분 단위)
        // 현재 시간, 최소/최대 제한 없음, 1분 단위 증감
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(model);

        // 포맷을 "HH:mm" (예: 14:30)으로 설정
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(editor);

        // 스피너 크기 조정
        timeSpinner.setPreferredSize(new Dimension(80, 25));

        pDate.add(tfDate);
        pDate.add(btnDate);
        pDate.add(new JLabel(" 시간: "));
        pDate.add(timeSpinner); // 콤보박스 대신 스피너 추가

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
        btnSubmit.setBackground(new Color(34, 139, 34)); // Forest Green
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
    public void setDate(String date) { tfDate.setText(date); }

    public JButton getBtnSubmit() { return btnSubmit; }
    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnDate() { return btnDate; }

    public OrderVO getOrderData() {
        OrderVO vo = new OrderVO();
        vo.setUserId(lblUserId.getText());
        vo.setOrigin(tfOrigin.getText());
        vo.setDest(tfDest.getText());
        vo.setCargoInfo(tfCargoName.getText() + " (" + tfLoad.getText() + "kg)");

        // ★ 중요: 스피너에서 시간 값을 가져와서 "HH:mm" 문자열로 변환
        Date timeVal = (Date) timeSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeStr = sdf.format(timeVal);

        // 날짜 + 시간 문자열 합치기 (예: 2025-12-14 14:05)
        String fullDate = tfDate.getText() + " " + timeStr;
        vo.setPickupTime(fullDate);

        try { vo.setPrice(Integer.parseInt(tfCost.getText())); }
        catch(Exception e) { vo.setPrice(0); }
        return vo;
    }

    public void clear() {
        tfOrigin.setText(""); tfDest.setText(""); tfCargoName.setText("");
        tfLoad.setText(""); tfDate.setText(""); tfCost.setText("");
        // 시간 스피너를 현재 시간으로 초기화
        timeSpinner.setValue(new Date());
    }
}