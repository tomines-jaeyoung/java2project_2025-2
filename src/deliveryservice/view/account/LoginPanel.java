package deliveryservice.view.account;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// 사용자 인증을 위한 로그인 화면 패널입니다.
// GridBagLayout을 사용하여 화면 중앙에 깔끔하게 정렬되도록 디자인했습니다.
public class LoginPanel extends JPanel {
    JTextField tfId;
    JPasswordField pfPw; // 보안을 위해 JTextField 대신 JPasswordField를 사용했습니다.
    JButton btnLogin;
    JButton btnBack;

    public LoginPanel() {
        setLayout(new GridBagLayout()); // 중앙 정렬
        setBackground(Color.WHITE);

        // 아이디/비밀번호/버튼을 담을 내부 패널입니다.
        // 라벨과 입력 필드를 포함하여 총 6개의 행이 필요합니다.
        JPanel p = new JPanel(new GridLayout(6, 1, 5, 5));
        p.setBackground(Color.WHITE);

        // 폼을 눈에 띄게 하기 위해 테두리와 안쪽 여백을 설정했습니다.
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // 타이틀 설정
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        lblTitle.setForeground(new Color(65, 105, 225)); // 산뜻한 파란색 계열로 포인트를 줬습니다.
        p.add(lblTitle);

        // 아이디 라벨 & 입력
        JLabel lblId = new JLabel("아이디");
        lblId.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        p.add(lblId);

        tfId = new JTextField();
        styleTextField(tfId); // 텍스트 필드 스타일링은 별도 메소드로 분리해서 재사용성을 높였습니다.
        p.add(tfId);

        // 비번 라벨 & 입력
        JLabel lblPw = new JLabel("비밀번호");
        lblPw.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        p.add(lblPw);

        pfPw = new JPasswordField();
        styleTextField(pfPw); // JPasswordField도 JTextField를 상속하므로 동일한 스타일링 메소드를 사용할 수 있습니다.
        p.add(pfPw);

        // 버튼 패널
        // 버튼 두 개를 가로로 나란히 배치하기 위해 GridLayout을 사용했습니다.
        JPanel pBtn = new JPanel(new GridLayout(1, 2, 10, 0));
        pBtn.setBackground(Color.WHITE);

        // 뒤로가기 버튼은 회색 계열로, 주요 버튼인 로그인 버튼은 파란색 계열로 디자인했습니다.
        btnBack = new JButton("뒤로");
        styleButton(btnBack, new Color(169, 169, 169));

        btnLogin = new JButton("로그인");
        styleButton(btnLogin, new Color(65, 105, 225));

        pBtn.add(btnBack);
        pBtn.add(btnLogin);
        p.add(pBtn);

        add(p);
    }

    // 텍스트 필드의 공통 스타일을 설정하는 보조 메소드입니다.
    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        tf.setPreferredSize(new Dimension(280, 40));
        // 안쪽 여백(EmptyBorder)을 줘서 텍스트가 테두리에 붙지 않게 했습니다.
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    // 버튼의 공통 스타일을 설정하는 보조 메소드입니다.
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE); // 글자색은 흰색으로
        btn.setFocusPainted(false); // 버튼 클릭 시 생기는 포커스 테두리 제거
        btn.setBorderPainted(false); // 버튼 테두리 제거
    }

    // 컨트롤러에서 입력값을 가져가거나 이벤트 리스너를 붙일 수 있도록 Getter를 제공합니다.
    public String getId() { return tfId.getText(); }

    // 비밀번호는 getPassword()로 char 배열을 받고, 안전하게 사용하기 위해 String으로 변환해서 반환했습니다.
    public String getPw() { return new String(pfPw.getPassword()); }

    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnBack() { return btnBack; }

    // LoginController에서 포커스를 설정할 때 사용할 아이디 필드 Getter입니다.
    public JTextField getTfId() { return tfId; }
}