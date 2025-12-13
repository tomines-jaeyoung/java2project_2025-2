package deliveryservice.view.account;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    JTextField tfId;
    JPasswordField pfPw;
    JButton btnLogin;
    JButton btnBack;

    public LoginPanel() {
        setLayout(new GridBagLayout());

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.setBorder(BorderFactory.createTitledBorder("로그인"));

        p.add(new JLabel("아이디:"));
        tfId = new JTextField(15);
        p.add(tfId);

        p.add(new JLabel("비밀번호:"));
        pfPw = new JPasswordField(15);
        p.add(pfPw);

        btnLogin = new JButton("로그인");
        btnBack = new JButton("뒤로가기");

        p.add(btnBack);
        p.add(btnLogin);

        add(p);
    }

    public String getId() { return tfId.getText(); }
    public String getPw() { return new String(pfPw.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnBack() { return btnBack; }
}