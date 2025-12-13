package deliveryservice.view.customer;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import javax.swing.*;
import java.awt.*;

public class UserInfoUpdateView extends JPanel {
    JLabel lblId;
    JTextField tfName, tfPhone;
    JPasswordField pfPw;

    JPanel pVehicle;
    JTextField tfCarNum, tfMaxWeight;
    JComboBox<String> comboCarType;
    String[] carTypes = {"카고", "윙바디", "탑차", "냉동탑차", "다마스", "라보", "기타"};

    JButton btnUpdate, btnCancel;
    UserVO currentUser;

    public UserInfoUpdateView() {
        setLayout(new BorderLayout());

        JPanel pCenter = new JPanel(new GridBagLayout());
        pCenter.setBorder(BorderFactory.createTitledBorder("내 정보 수정"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // 1. 기본 정보
        c.gridx=0; c.gridy=0; pCenter.add(new JLabel("아이디:"), c);
        lblId = new JLabel(); c.gridx=1; pCenter.add(lblId, c);

        c.gridx=0; c.gridy=1; pCenter.add(new JLabel("비밀번호:"), c);
        pfPw = new JPasswordField(15); c.gridx=1; pCenter.add(pfPw, c);

        c.gridx=0; c.gridy=2; pCenter.add(new JLabel("이름:"), c);
        tfName = new JTextField(15); c.gridx=1; pCenter.add(tfName, c);

        c.gridx=0; c.gridy=3; pCenter.add(new JLabel("전화번호:"), c);
        tfPhone = new JTextField(15); c.gridx=1; pCenter.add(tfPhone, c);

        // 2. 차량 정보 패널 (기사 전용)
        pVehicle = new JPanel(new GridBagLayout());
        pVehicle.setBorder(BorderFactory.createTitledBorder("차량 정보 수정 (기사 전용)"));
        pVehicle.setVisible(false);

        GridBagConstraints vc = new GridBagConstraints();
        vc.insets = new Insets(5, 5, 5, 5);
        vc.fill = GridBagConstraints.HORIZONTAL;

        // ★★★ [수정된 부분] 차량번호 수정 가능하도록 변경 ★★★
        vc.gridx=0; vc.gridy=0; pVehicle.add(new JLabel("차량번호:"), vc);
        tfCarNum = new JTextField(15);
        // tfCarNum.setEditable(false);  <-- 이 줄을 삭제하거나 주석 처리함
        // tfCarNum.setBackground(Color.LIGHT_GRAY); <-- 배경색 설정도 삭제
        vc.gridx=1; pVehicle.add(tfCarNum, vc);

        vc.gridx=0; vc.gridy=1; pVehicle.add(new JLabel("차종:"), vc);
        comboCarType = new JComboBox<>(carTypes);
        vc.gridx=1; pVehicle.add(comboCarType, vc);

        vc.gridx=0; vc.gridy=2; pVehicle.add(new JLabel("최대적재량(kg):"), vc);
        tfMaxWeight = new JTextField(15);
        vc.gridx=1; pVehicle.add(tfMaxWeight, vc);

        c.gridx=0; c.gridy=4; c.gridwidth=2;
        pCenter.add(pVehicle, c);

        add(pCenter, BorderLayout.CENTER);

        JPanel pBtn = new JPanel();
        btnUpdate = new JButton("수정완료");
        btnCancel = new JButton("취소");
        pBtn.add(btnCancel); pBtn.add(btnUpdate);
        add(pBtn, BorderLayout.SOUTH);
    }

    public void setUserInfo(UserVO vo) {
        this.currentUser = vo;
        lblId.setText(vo.getUserId());
        pfPw.setText(vo.getPassword());
        tfName.setText(vo.getUserName());
        tfPhone.setText(vo.getPhone());

        if("기사".equals(vo.getUserType())) {
            pVehicle.setVisible(true);
            if(vo.getVehicle() != null) {
                tfCarNum.setText(vo.getVehicle().getCarNum());
                comboCarType.setSelectedItem(vo.getVehicle().getCarType());
                tfMaxWeight.setText(String.valueOf(vo.getVehicle().getMaxWeight()));
            } else {
                tfCarNum.setText("");
                tfMaxWeight.setText("0");
            }
        } else {
            pVehicle.setVisible(false);
        }
    }

    public UserVO getUpdatedUser() {
        UserVO vo = new UserVO();
        vo.setUserId(lblId.getText());
        vo.setPassword(new String(pfPw.getPassword()));
        vo.setUserName(tfName.getText());
        vo.setPhone(tfPhone.getText());
        vo.setUserType(currentUser.getUserType());

        if("기사".equals(currentUser.getUserType())) {
            VehicleVO v = new VehicleVO();
            v.setCarNum(tfCarNum.getText()); // 수정된 차량번호 가져옴
            v.setCarType((String)comboCarType.getSelectedItem());
            try {
                v.setMaxWeight(Integer.parseInt(tfMaxWeight.getText()));
            } catch(Exception e) {
                v.setMaxWeight(0);
            }
            v.setDriverId(vo.getUserId());
            vo.setVehicle(v);
        }
        return vo;
    }

    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnCancel() { return btnCancel; }
}