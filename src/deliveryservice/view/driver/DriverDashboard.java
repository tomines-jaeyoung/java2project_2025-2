package deliveryservice.view.driver;

import javax.swing.*;
import java.awt.*;

// 기사님 전용 메인 화면(대시보드) 패널입니다.
// 고객용 대시보드와 구조는 동일하지만, '실시간 콜' 수락과 '배차 관리' 기능에 초점을 맞춰 버튼을 배치했습니다.
public class DriverDashboard extends JPanel {
    JButton btnRealTime;   // 실시간 대기 중인 주문을 조회하는 버튼입니다.
    JButton btnHistory;    // 본인이 수락한 배차 내역을 관리하는 버튼입니다.
    JButton btnEditInfo;   // 내 정보(회원/차량 정보)를 수정하는 버튼입니다.
    JButton btnLogout;     // 시스템 로그아웃 버튼입니다.

    public DriverDashboard() {
        // 절대 좌표(null) 레이아웃을 사용해 컴포넌트들을 원하는 위치에 정확히 배치했습니다.
        setLayout(null);
        setBackground(new Color(240, 242, 245)); // 배경은 밝은 회색 계열로 설정했습니다.

        // 상단 헤더 영역 구성
        JPanel header = new JPanel();
        header.setBounds(0, 0, 800, 80);
        header.setBackground(new Color(255, 140, 0)); // 기사 테마 색상으로 다크 오렌지 계열을 사용했습니다.
        header.setLayout(null);

        // 타이틀 레이블
        JLabel lblTitle = new JLabel("Driver Partner", SwingConstants.LEFT);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(30, 20, 300, 40);
        header.add(lblTitle);

        // 로그아웃 버튼 (특수 스타일링 적용)
        // 헤더 색상과 조화롭게 보이기 위해 배경이 투명하게 처리되도록 paintComponent를 오버라이드했습니다.
        btnLogout = new JButton("로그아웃") {
            @Override
            protected void paintComponent(Graphics g) {
                // 배경색을 직접 그려서 Swing 기본 배경 그리기(호버/클릭 효과)를 무시합니다.
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g); // 텍스트를 그립니다.
            }
        };
        btnLogout.setBounds(680, 25, 90, 30);
        btnLogout.setBackground(new Color(255, 255, 255, 50)); // 반투명 흰색
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false); // 오버라이딩된 paintComponent가 작동하게 하는 중요한 설정입니다.
        header.add(btnLogout);
        add(header);

        // 메뉴 버튼 생성
        // '실시간 콜'은 가장 중요한 기능이므로 눈에 띄는 빨간색 계열을 사용했습니다.
        btnRealTime = createMenuButton("실시간 콜", "📣", 100, 150, new Color(255, 69, 0));

        // 이름 변경: 기존 '배차 내역'에서 '배차 관리'로 이름을 바꾸어 기능의 포괄성을 높였습니다.
        // 배차 관리는 운송 업무의 핵심이므로 초록색 계열을 사용했습니다.
        btnHistory = createMenuButton("배차 관리", "🚛", 310, 150, new Color(34, 139, 34));

        // 정보 수정은 보조 기능이므로 무채색 계열을 사용했습니다.
        btnEditInfo = createMenuButton("정보 수정", "🔧", 520, 150, new Color(105, 105, 105));

        add(btnRealTime);
        add(btnHistory);
        add(btnEditInfo);
    }

    // 대시보드 메뉴 버튼 스타일링을 위한 보조 메소드입니다.
    // HTML을 사용하여 이모지 아이콘과 텍스트를 중앙에 분리하여 배치했습니다.
    private JButton createMenuButton(String text, String icon, int x, int y, Color color) {
        // HTML을 사용해서 아이콘(<font size='6'>)을 크게 표시하고 텍스트를 줄 바꿈으로 아래에 정렬했습니다.
        JButton btn = new JButton("<html><center><font size='6'>" + icon + "</font><br><br>" + text + "</center></html>");
        btn.setBounds(x, y, 180, 180);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 50));
        // 버튼 하단에만 색상 테두리를 넣어 기능을 강조하고 디자인을 깔끔하게 마무리했습니다.
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, color));
        btn.setFocusPainted(false);
        return btn;
    }

    // 외부 컨트롤러(DriverMainController)에서 이벤트 리스너를 연결할 수 있도록 Getter를 제공합니다.
    public JButton getBtnRealTime() { return btnRealTime; }
    public JButton getBtnHistory() { return btnHistory; }
    public JButton getBtnEditInfo() { return btnEditInfo; }
    public JButton getBtnLogout() { return btnLogout; }
}