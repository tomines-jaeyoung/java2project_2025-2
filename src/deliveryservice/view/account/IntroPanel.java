package deliveryservice.view.account;

import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {
    JButton btnGoLogin;
    JButton btnGoRegister;

    public IntroPanel() {
        setLayout(new GridBagLayout()); // 중앙 정렬을 위해
        setBackground(Color.WHITE);

        JPanel centerP = new JPanel(new GridLayout(3, 1, 10, 20));
        centerP.setBackground(Color.WHITE);

        JLabel title = new JLabel("Pick & Go", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        btnGoLogin = new JButton("로그인 하러가기");
        btnGoLogin.setPreferredSize(new Dimension(200, 50));

        btnGoRegister = new JButton("회원가입 하러가기");
        btnGoRegister.setPreferredSize(new Dimension(200, 50));

        centerP.add(title);
        centerP.add(btnGoLogin);
        centerP.add(btnGoRegister);

        add(centerP);
    }

    public JButton getBtnGoLogin() { return btnGoLogin; }
    public JButton getBtnGoRegister() { return btnGoRegister; }
}