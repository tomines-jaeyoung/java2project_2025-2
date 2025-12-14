package deliveryservice.view.customer;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// 고객과 기사 모두의 회원 정보를 수정할 때 사용하는 범용 뷰 패널입니다.
// 기존 정보를 텍스트 필드에 미리 채워주고, 기사일 경우에만 차량 정보 수정 필드를 추가로 보여주도록 설계했습니다.
public class UserInfoUpdateView extends JPanel {
    // 기본 사용자 정보 필드
    JLabel lblId;
    JTextField tfName, tfPhone;
    JPasswordField pfPw;

    // 차량 정보 관련 컴포넌트
    JPanel pVehicle;
    JTextField tfCarNum, tfMaxWeight;
    JComboBox<String> comboCarType;
    String[] carTypes = {"카고", "윙바디", "탑차", "냉동탑차", "다마스", "라보", "기타"};

    JButton btnUpdate, btnCancel;
    UserVO currentUser; // 현재 로그인된 사용자 정보를 임시로 저장해두는 변수입니다.

    public UserInfoUpdateView() {
        // 큰 틀은 BorderLayout으로 잡고, 중앙에 GridBagLayout을 사용하여 입력 폼을 배치했습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 타이틀 설정
        JLabel title = new JLabel("내 정보 수정", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        title.setForeground(new Color(65, 105, 225));
        title.setBorder(new EmptyBorder(20,0,20,0));
        add(title, BorderLayout.NORTH);

        // 중앙 입력 폼 패널
        JPanel pCenter = new JPanel(new GridBagLayout());
        pCenter.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // 아이디는 수정 불가능하도록 JLabel로 처리했습니다.
        addInput(pCenter, "아이디", lblId = new JLabel(), 0, c);
        lblId.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 나머지 정보는 수정 가능하도록 텍스트 필드를 사용했습니다.
        addInput(pCenter, "비밀번호", pfPw = new JPasswordField(15), 1, c);
        styleField(pfPw);

        addInput(pCenter, "이름", tfName = new JTextField(15), 2, c);
        styleField(tfName);

        addInput(pCenter, "전화번호", tfPhone = new JTextField(15), 3, c);
        styleField(tfPhone);

        // 차량 정보 패널
        // 기사일 경우에만 동적으로 보여주기 위해 별도 패널로 분리했습니다.
        pVehicle = new JPanel(new GridBagLayout());
        pVehicle.setBackground(new Color(245, 245, 250)); // 연한 배경으로 구분
        pVehicle.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237)), " 차량 정보 수정 (기사) "
        ));
        pVehicle.setVisible(false); // 초기 상태는 숨김

        GridBagConstraints vc = new GridBagConstraints();
        vc.insets = new Insets(5, 5, 5, 5);
        vc.fill = GridBagConstraints.HORIZONTAL;

        // 차량 정보 입력 필드 배치
        vc.gridx=0; vc.gridy=0;
        JLabel vl = new JLabel("차량번호"); vl.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        pVehicle.add(vl, vc);

        tfCarNum = new JTextField(15); styleField(tfCarNum);
        vc.gridx=1; pVehicle.add(tfCarNum, vc);

        vc.gridx=0; vc.gridy=1;
        pVehicle.add(new JLabel("차종"), vc);

        comboCarType = new JComboBox<>(carTypes);
        comboCarType.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        comboCarType.setBackground(Color.WHITE);
        vc.gridx=1; pVehicle.add(comboCarType, vc);

        vc.gridx=0; vc.gridy=2;
        pVehicle.add(new JLabel("적재량(kg)"), vc);

        tfMaxWeight = new JTextField(15); styleField(tfMaxWeight);
        vc.gridx=1; pVehicle.add(tfMaxWeight, vc);

        c.gridx=0; c.gridy=4; c.gridwidth=2; // 중앙 패널에 차량 패널 추가
        pCenter.add(pVehicle, c);

        add(pCenter, BorderLayout.CENTER);

        // 하단 버튼 구성
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pBtn.setBackground(Color.WHITE);

        btnCancel = createButton("취소", new Color(169, 169, 169)); // 취소 버튼은 회색
        btnUpdate = createButton("수정완료", new Color(65, 105, 225)); // 메인 버튼은 파란색

        pBtn.add(btnCancel); pBtn.add(btnUpdate);
        add(pBtn, BorderLayout.SOUTH);
    }

    // 입력 필드를 GridBagLayout에 쉽게 추가하는 보조 메소드입니다.
    private void addInput(JPanel p, String label, Component comp, int y, GridBagConstraints c) {
        c.gridx=0; c.gridy=y; c.gridwidth=1;
        JLabel l = new JLabel(label);
        l.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        p.add(l, c);
        c.gridx=1; p.add(comp, c);
    }

    // 텍스트 필드의 공통 스타일을 설정하는 보조 메소드입니다.
    private void styleField(JTextField tf) {
        tf.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(200, 35));
    }

    // 버튼의 공통 스타일을 설정하는 보조 메소드입니다.
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

    // 외부에서 UserVO 객체를 받아와서 화면에 데이터를 뿌려주는 핵심 메소드입니다.
    public void setUserInfo(UserVO vo) {
        this.currentUser = vo; // 현재 사용자 타입을 기억하기 위해 저장해둡니다.
        lblId.setText(vo.getUserId());
        pfPw.setText(vo.getPassword());
        tfName.setText(vo.getUserName());
        tfPhone.setText(vo.getPhone());

        // 기사일 경우에만 차량 정보 패널을 보이게 처리했습니다.
        if("기사".equals(vo.getUserType())) {
            pVehicle.setVisible(true);
            // 차량 정보가 DB에 등록되어 있으면 필드에 채워주고, 없으면 빈 값으로 둡니다.
            if(vo.getVehicle() != null) {
                tfCarNum.setText(vo.getVehicle().getCarNum());
                comboCarType.setSelectedItem(vo.getVehicle().getCarType());
                tfMaxWeight.setText(String.valueOf(vo.getVehicle().getMaxWeight()));
            } else {
                tfCarNum.setText("");
                tfMaxWeight.setText("0"); // 값이 없으면 0으로 표시하여 사용자에게 입력 유도
            }
        } else {
            pVehicle.setVisible(false); // 고객이면 무조건 숨김
        }
    }

    // 사용자가 입력한 수정된 데이터를 VO 객체로 만들어서 컨트롤러에 전달하는 핵심 메소드입니다.
    public UserVO getUpdatedUser() {
        UserVO vo = new UserVO();
        vo.setUserId(lblId.getText());
        // 비밀번호 필드는 JPasswordField에서 Char 배열로 가져와서 String으로 변환했습니다.
        vo.setPassword(new String(pfPw.getPassword()));
        vo.setUserName(tfName.getText());
        vo.setPhone(tfPhone.getText());
        vo.setUserType(currentUser.getUserType()); // 사용자 타입은 수정 불가하므로 기존 값을 그대로 사용합니다.

        // 기사일 경우, VehicleVO 객체를 생성하여 UserVO에 연결합니다.
        if("기사".equals(currentUser.getUserType())) {
            VehicleVO v = new VehicleVO();
            v.setCarNum(tfCarNum.getText());
            v.setCarType((String)comboCarType.getSelectedItem());

            // 적재량은 숫자(int)여야 하므로, 입력된 문자열을 정수로 변환하며 예외 처리를 했습니다.
            // 만약 숫자가 아니면 0으로 처리하여 프로그램이 죽지 않게 방어했습니다.
            try { v.setMaxWeight(Integer.parseInt(tfMaxWeight.getText())); } catch(Exception e) { v.setMaxWeight(0); }

            v.setDriverId(vo.getUserId());
            vo.setVehicle(v);
        }
        return vo;
    }

    // 버튼 Getter (컨트롤러에서 이벤트 연결용)
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnCancel() { return btnCancel; }
}