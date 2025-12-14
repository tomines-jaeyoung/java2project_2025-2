package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// 고객이 새로운 배송 주문을 신청할 때 사용하는 화면 패널입니다.
// 복잡한 입력 항목들을 GridBagLayout을 이용해 깔끔한 폼 형태로 배치했습니다.
public class OrderInsertView extends JPanel {
    // 입력 필드들
    JTextField tfOrigin, tfDest, tfCargoName, tfLoad, tfDate, tfCost;
    JSpinner timeSpinner; // 시간 입력은 스피너를 사용했습니다.
    JLabel lblUserId; // 아이디는 수정 불가하므로 레이블로 처리했습니다.
    JButton btnSubmit, btnHome, btnDate; // 기능 버튼들

    public OrderInsertView() {
        // 전체 구조는 BorderLayout으로 제목(NORTH), 폼(CENTER), 버튼(SOUTH)을 분리했습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 타이틀 설정
        JLabel title = new JLabel("배송 신청", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        title.setForeground(new Color(65, 105, 225));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 입력 폼 패널 (GridBagLayout 사용)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL; // 가로로 꽉 채우기

        // 입력 필드들 배치 (addInput 보조 메소드 활용)
        addInput(formPanel, "고객 ID", lblUserId = new JLabel("Loading..."), 0, c);
        lblUserId.setForeground(new Color(100, 149, 237));
        lblUserId.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        addInput(formPanel, "출발지", tfOrigin = createField(), 1, c);
        addInput(formPanel, "도착지", tfDest = createField(), 2, c);
        addInput(formPanel, "화물 이름", tfCargoName = createField(), 3, c);
        addInput(formPanel, "적재량(kg)", tfLoad = createField(), 4, c);

        // 배송 희망 일시 입력부: 날짜와 시간을 분리하여 배치했습니다.
        JPanel pDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pDate.setBackground(Color.WHITE);

        tfDate = new JTextField(10); // 날짜 표시 필드
        tfDate.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        tfDate.setEditable(false); // 직접 입력은 막고, 버튼을 통해서만 입력받습니다.
        tfDate.setBackground(Color.WHITE);
        tfDate.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tfDate.setPreferredSize(new Dimension(100, 30));

        btnDate = new JButton("📅"); // 날짜 선택 팝업을 띄울 버튼
        btnDate.setBackground(Color.WHITE);
        btnDate.setFocusPainted(false);

        // 시간 입력 컴포넌트 (JSpinner 사용): 시간 포맷만 보이도록 설정했습니다.
        // JSpinner를 사용하면 사용자가 시간을 직접 타이핑하거나 위/아래 버튼으로 쉽게 변경할 수 있습니다.
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(editor);
        timeSpinner.setPreferredSize(new Dimension(80, 30));
        timeSpinner.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        pDate.add(tfDate); pDate.add(btnDate); pDate.add(new JLabel("  시간: ")); pDate.add(timeSpinner);
        addInput(formPanel, "배송 희망일시", pDate, 5, c);
        addInput(formPanel, "운임(원)", tfCost = createField(), 6, c);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setBackground(Color.WHITE);

        btnHome = createButton("취소 / 홈", new Color(169, 169, 169));
        // 버튼 파란색 변경: 주문 접수가 핵심 기능이므로 강조했습니다.
        btnSubmit = createButton("주문 접수", new Color(0, 102, 204));

        btnPanel.add(btnHome);
        btnPanel.add(btnSubmit);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // 입력 라벨과 컴포넌트를 GridBagLayout에 추가하는 보조 메소드입니다.
    private void addInput(JPanel p, String labelText, Component comp, int y, GridBagConstraints c) {
        c.gridx = 0; c.gridy = y; c.weightx = 0.2; // 라벨
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        p.add(l, c);
        c.gridx = 1; c.weightx = 0.8; // 입력 컴포넌트 (가로 공간을 넓게 줌)
        p.add(comp, c);
    }

    // 텍스트 필드 공통 스타일을 지정하는 보조 메소드입니다.
    private JTextField createField() {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(200, 35));
        // 안쪽 여백을 줘서 텍스트가 테두리에 붙지 않게 했습니다.
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return tf;
    }

    // 버튼 공통 스타일을 지정하는 보조 메소드입니다.
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true); // 배경색이 투명해지는 문제를 방지
        return btn;
    }

    // 외부 컨트롤러에서 아이디를 받아와서 레이블에 표시합니다.
    public void setUserId(String id) { lblUserId.setText(id); }
    // DatePicker에서 선택된 날짜를 받아와서 필드에 표시합니다.
    public void setDate(String date) { tfDate.setText(date); }

    // 버튼 Getter
    public JButton getBtnSubmit() { return btnSubmit; }
    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnDate() { return btnDate; }

    // 입력된 모든 데이터를 읽어서 OrderVO 객체로 통합하여 반환하는 핵심 메소드입니다.
    public OrderVO getOrderData() {
        OrderVO vo = new OrderVO();
        vo.setUserId(lblUserId.getText());
        vo.setOrigin(tfOrigin.getText());
        vo.setDest(tfDest.getText());

        // 화물 이름과 적재량을 하나의 필드(cargoInfo)로 묶어서 DB에 저장되도록 처리했습니다.
        // 예: "가구 (100kg)" 이런 형식으로 저장됩니다. DB에서 파싱하기 쉽도록 괄호와 "kg"을 붙여서 포맷을 통일했습니다.
        vo.setCargoInfo(tfCargoName.getText() + " (" + tfLoad.getText() + "kg)");

        // 날짜와 시간을 합치는 로직입니다.
        Date timeVal = (Date) timeSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // DB에 저장될 때 Date 형식으로 변환하기 편하도록 "날짜 시간" 형태의 문자열로 만듭니다.
        vo.setPickupTime(tfDate.getText() + " " + sdf.format(timeVal));

        // 운임(Cost)은 숫자(int)로 변환해야 하므로, 입력 오류로 인해 프로그램이 죽지 않도록
        // try-catch로 감싸서 예외 발생 시 0으로 처리하는 방어 코드를 넣었습니다.
        try { vo.setPrice(Integer.parseInt(tfCost.getText())); } catch(Exception e) { vo.setPrice(0); }
        return vo;
    }

    // 주문 접수 후, 다음 사용자를 위해 입력 필드들을 초기화하는 메소드입니다.
    public void clear() {
        tfOrigin.setText(""); tfDest.setText(""); tfCargoName.setText("");
        tfLoad.setText(""); tfDate.setText(""); tfCost.setText("");
        timeSpinner.setValue(new Date()); // 시간 스피너도 현재 시간으로 재설정
    }
}