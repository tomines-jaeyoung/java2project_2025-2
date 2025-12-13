package deliveryservice.controller;

import deliveryservice.domain.UserVO;
import deliveryservice.repository.UserRepository;
import deliveryservice.view.account.*;
import center_Frame.CenterFrame;
import javax.swing.*;
import java.awt.*;

public class LoginController extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    IntroPanel introPan;
    LoginPanel loginPan;
    RegisterPanel registerPan;
    UserRepository userRepo;

    public LoginController() {
        userRepo = new UserRepository();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        introPan = new IntroPanel();
        loginPan = new LoginPanel();
        registerPan = new RegisterPanel();

        mainPanel.add(introPan, "INTRO");
        mainPanel.add(loginPan, "LOGIN");
        mainPanel.add(registerPan, "REGISTER");
        add(mainPanel);

        // 이벤트 연결
        introPan.getBtnGoLogin().addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        introPan.getBtnGoRegister().addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        loginPan.getBtnBack().addActionListener(e -> cardLayout.show(mainPanel, "INTRO"));
        loginPan.getBtnLogin().addActionListener(e -> {
            String id = loginPan.getId();
            String pw = loginPan.getPw();
            UserVO user = userRepo.getUser(id, pw); // 수정된 메서드 호출

            if(user != null) {
                JOptionPane.showMessageDialog(this, user.getUserName() + "님 환영합니다!");
                dispose();

                // 고객/기사 분기 처리
                if("고객".equals(user.getUserType())) {
                    new CustomerMainController(user); // 고객 메인 실행
                } else {
                    // ★ 수정된 부분: 기사 로그인 시 DriverMainController 실행
                    new DriverMainController(user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀립니다.");
            }
        });

        registerPan.getBtnBack().addActionListener(e -> cardLayout.show(mainPanel, "INTRO"));
        registerPan.getBtnRegister().addActionListener(e -> {
            UserVO vo = registerPan.getUserVO();
            if(userRepo.registerUser(vo) > 0) {
                JOptionPane.showMessageDialog(this, "가입 완료! 로그인하세요.");
                registerPan.clearFields();
                cardLayout.show(mainPanel, "LOGIN");
            }
        });

        setTitle("화물 시스템 - 접속");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) { new LoginController(); }
}