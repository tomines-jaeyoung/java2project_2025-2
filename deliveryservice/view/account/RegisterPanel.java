package deliveryservice.view.account;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// 회원가입 화면 패널입니다. 고객과 기사 정보를 통합해서 받지만,
// 기사일 경우에만 추가로 차량 정보를 입력받도록 동적으로 화면을 처리하는 데 주력했습니다.
public class RegisterPanel extends JPanel {
    // 기본 사용자 정보 입력 필드
    JTextField tfId, tfName, tfPhone;
    JPasswordField pfPw;
    JComboBox<String> comboType; // 고객/기사 선택 콤보박스

    // 차량 관련 컴포넌트
    JPanel vehiclePanel; // 기사 선택 시에만 보이게 할 패널
    JTextField tfCarNum, tfMaxWeight;
    JComboBox<String> comboCarType;
    String[] carTypes = {"카고", "윙바디", "탑차", "냉동탑차", "다마스", "라보", "기타"};

    JButton btnRegister, btnBack;

    public RegisterPanel() {
        // 전체 구조를 BorderLayout으로 나누고 각 영역에 컴포넌트를 배치했습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 메인 입력 폼을 담는 패널입니다. 컴포넌트 간의 정렬과 배치를 위해 GridBagLayout을 사용했습니다.
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 5, 8, 5); // 컴포넌트 주변 여백 설정
        c.fill = GridBagConstraints.HORIZONTAL; // 가로로 최대한 늘어나게 설정

        // 타이틀 설정
        JLabel title = new JLabel("회원가입", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setForeground(new Color(65, 105, 225));
        title.setBorder(new EmptyBorder(20,0,20,0));
        add(title, BorderLayout.NORTH);

        // 1. 기본 정보 입력 (addInput이라는 보조 메소드를 활용해 반복 작업을 줄였습니다.)
        addInput(contentPanel, "아이디", tfId = new JTextField(15), 0, c);
        addInput(contentPanel, "비밀번호", pfPw = new JPasswordField(15), 1, c);
        addInput(contentPanel, "이름", tfName = new JTextField(15), 2, c);
        addInput(contentPanel, "전화번호", tfPhone = new JTextField(15), 3, c);

        // 구분 콤보박스
        c.gridx=0; c.gridy=4;
        JLabel l = new JLabel("가입 구분");
        l.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        contentPanel.add(l, c);

        comboType = new JComboBox<>(new String[]{"고객", "기사"});
        comboType.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        comboType.setBackground(Color.WHITE);
        c.gridx=1; contentPanel.add(comboType, c);

        // 2. 차량 정보 패널 (핵심 동적 영역)
        vehiclePanel = new JPanel(new GridBagLayout());
        vehiclePanel.setBackground(new Color(245, 245, 255)); // 배경색을 옅게 줘서 일반 정보와 시각적으로 분리했습니다.
        vehiclePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237)), " 차량 정보 (기사 필수) "
        ));
        vehiclePanel.setVisible(false); // 초기에는 고객이 선택되어 있을 수 있으니 숨겨둡니다.

        GridBagConstraints vc = new GridBagConstraints();
        vc.insets = new Insets(5, 5, 5, 5);
        vc.fill = GridBagConstraints.HORIZONTAL;

        addVehicleInput(vehiclePanel, "차량번호", tfCarNum = new JTextField(12), 0, vc);

        vc.gridx=0; vc.gridy=1;
        JLabel vl = new JLabel("차종"); vl.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        vehiclePanel.add(vl, vc);

        comboCarType = new JComboBox<>(carTypes);
        comboCarType.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        comboCarType.setBackground(Color.WHITE);
        vc.gridx=1; vehiclePanel.add(comboCarType, vc);

        addVehicleInput(vehiclePanel, "최대적재량(kg)", tfMaxWeight = new JTextField(12), 2, vc);

        c.gridx=0; c.gridy=5; c.gridwidth=2; // contentPanel 내에서 차량 패널이 2칸을 병합하도록 했습니다.
        contentPanel.add(vehiclePanel, c);

        add(contentPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnP.setBackground(Color.WHITE);

        btnBack = createButton("취소", new Color(169, 169, 169));
        btnRegister = createButton("가입완료", new Color(65, 105, 225));

        btnP.add(btnBack);
        btnP.add(btnRegister);
        add(btnP, BorderLayout.SOUTH);

        // 이벤트: 콤보박스를 클릭했을 때 발생하는 동적 처리 로직입니다.
        comboType.addActionListener(e -> {
            // "기사"를 선택했을 때만 vehiclePanel이 보이도록 했습니다.
            vehiclePanel.setVisible("기사".equals(comboType.getSelectedItem()));

            // 여기서 revalidate()와 repaint()를 호출해 주지 않으면,
            // 숨겨진 패널이 차지하던 공간이 제대로 줄어들지 않고 화면이 깨지는 듯한 버그가 발생해서 추가했습니다.
            revalidate();
            repaint();
        });
    }

    // 기본 입력 필드를 GridBagLayout에 쉽게 추가하기 위한 보조 메소드입니다.
    private void addInput(JPanel p, String label, JComponent comp, int y, GridBagConstraints c) {
        c.gridx=0; c.gridy=y; c.gridwidth=1;
        JLabel l = new JLabel(label);
        l.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        p.add(l, c);

        c.gridx=1;
        comp.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        comp.setPreferredSize(new Dimension(180, 30));
        p.add(comp, c);
    }

    // 차량 정보 입력 필드 추가를 위한 보조 메소드입니다. (차량 패널 전용)
    private void addVehicleInput(JPanel p, String label, JTextField tf, int y, GridBagConstraints c) {
        c.gridx=0; c.gridy=y;
        JLabel l = new JLabel(label);
        l.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        p.add(l, c);
        c.gridx=1;
        tf.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        p.add(tf, c);
    }

    // 버튼 공통 스타일링 메소드입니다.
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    // 입력된 모든 데이터를 UserVO 객체로 만들어서 반환합니다. 이 부분이 DB에 전달되는 핵심 데이터입니다.
    public UserVO getUserVO() {
        // 기본 UserVO 객체 생성
        UserVO user = new UserVO(tfId.getText(), new String(pfPw.getPassword()), tfName.getText(), tfPhone.getText(), (String)comboType.getSelectedItem());

        // 기사일 경우에만 차량 정보를 처리합니다.
        if("기사".equals(user.getUserType())) {
            VehicleVO v = new VehicleVO();
            v.setCarNum(tfCarNum.getText());
            v.setCarType((String)comboCarType.getSelectedItem());

            // 최대 적재량(kg)은 정수(int)여야 하는데, 사용자가 문자를 입력하면 프로그램이 죽을 수 있습니다.
            // 데이터 무결성과 프로그램 안정성을 위해 try-catch로 감싸서 예외 발생 시 0으로 처리하도록 방어 코드를 넣었습니다.
            try { v.setMaxWeight(Integer.parseInt(tfMaxWeight.getText())); } catch(Exception e) { v.setMaxWeight(0); }

            v.setDriverId(user.getUserId());
            user.setVehicle(v);
        }
        return user;
    }

    // 가입 완료 후, 다음 사용자를 위해 모든 입력 필드를 비우고 초기 상태로 되돌리는 메소드입니다.
    public void clearFields() {
        tfId.setText(""); pfPw.setText(""); tfName.setText(""); tfPhone.setText("");
        comboType.setSelectedIndex(0); tfCarNum.setText(""); tfMaxWeight.setText("");
        comboCarType.setSelectedIndex(0); vehiclePanel.setVisible(false); // 차량 패널도 다시 숨겨줍니다.
    }

    // 컨트롤러에서 이벤트 리스너를 붙일 수 있도록 버튼 Getter를 제공합니다.
    public JButton getBtnRegister() { return btnRegister; }
    public JButton getBtnBack() { return btnBack; }
}