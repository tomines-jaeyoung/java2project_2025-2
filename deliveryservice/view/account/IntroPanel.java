package deliveryservice.view.account;

import javax.swing.*;
import java.awt.*;

// 프로그램 시작 시 사용자에게 가장 먼저 보여주는 인트로 화면 패널입니다.
// 복잡한 기능 없이 로그인 또는 회원가입으로 이동하는 두 개의 버튼만 배치했습니다.
public class IntroPanel extends JPanel {
    JButton btnGoLogin;
    JButton btnGoRegister;

    public IntroPanel() {
        // 레이아웃 관리: 화면 중앙에 컴포넌트들을 모으기 위해 GridBagLayout을 사용했습니다.
        // GridBagLayout은 복잡하지만 중앙 정렬이 깔끔하게 돼서 선호하는 레이아웃입니다.
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // 실제 버튼과 제목을 담을 중앙 패널입니다.
        // 세로로 3줄, 간격 10, 20으로 설정하여 컴포넌트들이 겹치지 않게 했습니다.
        JPanel centerP = new JPanel(new GridLayout(3, 1, 10, 20));
        centerP.setBackground(Color.WHITE);

        // 제목 레이블 설정
        JLabel title = new JLabel("Pick & Go", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24)); // 제목이니까 굵고 크게 설정했습니다.

        // 로그인 버튼
        btnGoLogin = new JButton("로그인 하러가기");
        // 버튼 크기를 고정시켜서 화면 크기가 변해도 버튼 크기가 유지되게 했습니다.
        btnGoLogin.setPreferredSize(new Dimension(200, 50));

        // 회원가입 버튼
        btnGoRegister = new JButton("회원가입 하러가기");
        btnGoRegister.setPreferredSize(new Dimension(200, 50));

        // 중앙 패널에 컴포넌트들을 순서대로 추가합니다.
        centerP.add(title);
        centerP.add(btnGoLogin);
        centerP.add(btnGoRegister);

        // 최종적으로 메인 패널에 중앙 패널을 추가하여 화면 중앙에 배치되도록 합니다.
        add(centerP);
    }

    // 외부 컨트롤러(LoginController)에서 버튼에 리스너를 달 수 있도록 Getter를 제공합니다.
    public JButton getBtnGoLogin() { return btnGoLogin; }
    public JButton getBtnGoRegister() { return btnGoRegister; }
}