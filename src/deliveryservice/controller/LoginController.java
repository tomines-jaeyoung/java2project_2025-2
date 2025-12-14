package deliveryservice.controller;

import deliveryservice.domain.UserVO;
import deliveryservice.repository.UserRepository;
import deliveryservice.view.account.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// 프로그램의 시작점이자 로그인, 회원가입 화면을 관리하는 컨트롤러입니다.
// 처음 설계할 때 화면 전환을 어떻게 할지 고민을 많이 했는데,
// 구글링을 통해 CardLayout을 알게 되어 한 프레임 내에서 패널만 교체하는 방식으로 구현했습니다.
public class LoginController extends JFrame {
    CardLayout cardLayout; // 패널을 카드처럼 겹쳐놓고 하나씩 보여주기 위해 사용했습니다.
    JPanel mainPanel;
    IntroPanel introPan;
    LoginPanel loginPan;
    RegisterPanel registerPan;
    UserRepository userRepo; // DB 연결을 담당하는 객체

    public LoginController() {
        // 시스템 UI 적용
        // 자바 스윙 기본 디자인(Metal)이 너무 옛날 느낌이라서, 운영체제(OS) 스타일에 맞춰지도록 설정했습니다.
        // 이걸 적용하니까 버튼이나 입력창이 훨씬 현대적으로 보입니다.
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        userRepo = new UserRepository();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면 패널들을 생성합니다.
        introPan = new IntroPanel();
        loginPan = new LoginPanel();
        registerPan = new RegisterPanel();

        // 카드 레이아웃에 패널들을 추가하면서, 나중에 불러올 이름(String)을 지정했습니다.
        mainPanel.add(introPan, "INTRO");
        mainPanel.add(loginPan, "LOGIN");
        mainPanel.add(registerPan, "REGISTER");
        add(mainPanel);

        // 이벤트 연결
        introPan.getBtnGoLogin().addActionListener(e -> {
            cardLayout.show(mainPanel, "LOGIN");
            // 로그인 화면 가면 아이디 입력창에 커서 포커스 & 엔터키 설정
            // 화면이 바뀌었을 때 마우스로 입력창을 클릭하지 않아도 바로 타이핑할 수 있게 requestFocus를 넣었습니다.
            loginPan.getTfId().requestFocus();
            // 아이디/비번 치고 엔터를 누르면 로그인 버튼이 눌린 것과 같은 효과를 줍니다. (UX 개선)
            this.getRootPane().setDefaultButton(loginPan.getBtnLogin());
        });

        introPan.getBtnGoRegister().addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        // 뒤로가기 버튼: 다시 인트로 화면으로 돌아옵니다.
        loginPan.getBtnBack().addActionListener(e -> cardLayout.show(mainPanel, "INTRO"));

        // 로그인 버튼 로직
        loginPan.getBtnLogin().addActionListener(e -> {
            String id = loginPan.getId();
            String pw = loginPan.getPw();
            // DB에서 해당 아이디와 비밀번호를 가진 유저가 있는지 확인합니다.
            UserVO user = userRepo.getUser(id, pw);

            if(user != null) {
                // JOptionPane.showMessageDialog(this, user.getUserName() + "님 환영합니다!"); // 팝업 제거 (바로 넘어가게)
                // 로그인에 성공하면 현재 로그인 창은 닫습니다.
                dispose(); // 창 닫기

                // 사용자 타입(고객/기사)에 따라 서로 다른 메인 화면을 띄워줍니다.
                // 처음에는 하나의 메인 컨트롤러에서 다 처리하려고 했는데, 기능 차이가 커서 분리하는 게 낫다고 판단했습니다.
                if("고객".equals(user.getUserType())) {
                    new CustomerMainController(user);
                } else {
                    new DriverMainController(user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀립니다.");
            }
        });

        registerPan.getBtnBack().addActionListener(e -> cardLayout.show(mainPanel, "INTRO"));

        // 회원가입 버튼 로직
        registerPan.getBtnRegister().addActionListener(e -> {
            UserVO vo = registerPan.getUserVO(); // 입력된 정보를 VO 객체로 가져옵니다.
            // insert 쿼리 실행 후 성공 여부(1 이상)를 확인합니다.
            if(userRepo.registerUser(vo) > 0) {
                JOptionPane.showMessageDialog(this, "가입 완료! 로그인하세요.");
                registerPan.clearFields(); // 가입 후 입력창을 비워주는 센스
                cardLayout.show(mainPanel, "LOGIN"); // 바로 로그인 화면으로 이동시킵니다.
            }
        });

        setTitle("화물 시스템 - 접속");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 이걸 안 넣으면 창을 닫아도 프로그램이 백그라운드에서 계속 돌아서 꼭 넣어야 합니다.
        setSize(400, 500);
        setLocationRelativeTo(null); // 화면 정중앙에 실행되도록 설정
        setVisible(true);
    }
    // 프로그램 진입점(Entry Point)
    public static void main(String[] args) { new LoginController(); }
}