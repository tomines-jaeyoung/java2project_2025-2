package deliveryservice.view.account;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    JTextField tfId, tfName, tfPhone;
    JPasswordField pfPw;
    JComboBox<String> comboType;

    // ★ 차량 관련 컴포넌트
    JPanel vehiclePanel;
    JTextField tfCarNum, tfMaxWeight;
    JComboBox<String> comboCarType;
    // SQL에 정의된 차종 목록
    String[] carTypes = {"카고", "윙바디", "탑차", "냉동탑차", "다마스", "라보", "기타"};

    JButton btnRegister, btnBack;

    public RegisterPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("회원가입"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        // 1. 기본 정보
        c.gridx=0; c.gridy=0; formPanel.add(new JLabel("아이디:"), c);
        tfId = new JTextField(15); c.gridx=1; formPanel.add(tfId, c);

        c.gridx=0; c.gridy=1; formPanel.add(new JLabel("비밀번호:"), c);
        pfPw = new JPasswordField(15); c.gridx=1; formPanel.add(pfPw, c);

        c.gridx=0; c.gridy=2; formPanel.add(new JLabel("이름:"), c);
        tfName = new JTextField(15); c.gridx=1; formPanel.add(tfName, c);

        c.gridx=0; c.gridy=3; formPanel.add(new JLabel("전화번호:"), c);
        tfPhone = new JTextField(15); c.gridx=1; formPanel.add(tfPhone, c);

        c.gridx=0; c.gridy=4; formPanel.add(new JLabel("구분:"), c);
        comboType = new JComboBox<>(new String[]{"고객", "기사"});
        c.gridx=1; formPanel.add(comboType, c);

        // 2. 차량 정보 패널 (기본 숨김)
        vehiclePanel = new JPanel(new GridBagLayout());
        vehiclePanel.setBorder(BorderFactory.createTitledBorder("차량 정보 (기사 필수)"));
        vehiclePanel.setVisible(false); // 처음엔 안보임

        GridBagConstraints vc = new GridBagConstraints();
        vc.insets = new Insets(5, 5, 5, 5);
        vc.fill = GridBagConstraints.HORIZONTAL;

        vc.gridx=0; vc.gridy=0; vehiclePanel.add(new JLabel("차량번호:"), vc);
        tfCarNum = new JTextField(10); vc.gridx=1; vehiclePanel.add(tfCarNum, vc);

        vc.gridx=0; vc.gridy=1; vehiclePanel.add(new JLabel("차종:"), vc);
        comboCarType = new JComboBox<>(carTypes); vc.gridx=1; vehiclePanel.add(comboCarType, vc);

        vc.gridx=0; vc.gridy=2; vehiclePanel.add(new JLabel("최대적재량(kg):"), vc);
        tfMaxWeight = new JTextField(10); vc.gridx=1; vehiclePanel.add(tfMaxWeight, vc);

        // 메인 폼에 차량 패널 추가
        c.gridx=0; c.gridy=5; c.gridwidth=2;
        formPanel.add(vehiclePanel, c);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnP = new JPanel();
        btnBack = new JButton("취소");
        btnRegister = new JButton("가입완료");
        btnP.add(btnBack);
        btnP.add(btnRegister);
        add(btnP, BorderLayout.SOUTH);

        // ★ 이벤트: '기사' 선택 시 차량 패널 보이기
        comboType.addActionListener(e -> {
            String selected = (String)comboType.getSelectedItem();
            if("기사".equals(selected)) {
                vehiclePanel.setVisible(true);
            } else {
                vehiclePanel.setVisible(false);
            }
            revalidate(); // 레이아웃 새로고침
            repaint();
        });
    }

    public UserVO getUserVO() {
        UserVO user = new UserVO(
                tfId.getText(), new String(pfPw.getPassword()),
                tfName.getText(), tfPhone.getText(),
                (String)comboType.getSelectedItem()
        );

        // 기사면 차량 정보도 담기
        if("기사".equals(user.getUserType())) {
            VehicleVO v = new VehicleVO();
            v.setCarNum(tfCarNum.getText());
            v.setCarType((String)comboCarType.getSelectedItem());
            try {
                v.setMaxWeight(Integer.parseInt(tfMaxWeight.getText()));
            } catch(NumberFormatException e) {
                v.setMaxWeight(0); // 오류 방지
            }
            v.setDriverId(user.getUserId());
            user.setVehicle(v);
        }
        return user;
    }

    public void clearFields() {
        tfId.setText(""); pfPw.setText("");
        tfName.setText(""); tfPhone.setText("");
        comboType.setSelectedIndex(0);
        tfCarNum.setText(""); tfMaxWeight.setText("");
        comboCarType.setSelectedIndex(0);
        vehiclePanel.setVisible(false);
    }

    public JButton getBtnRegister() { return btnRegister; }
    public JButton getBtnBack() { return btnBack; }
}