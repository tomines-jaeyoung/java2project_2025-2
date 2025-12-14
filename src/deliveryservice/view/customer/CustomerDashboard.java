package deliveryservice.view.customer;

import javax.swing.*;
import java.awt.*;

// 고객이 로그인 후 가장 먼저 보게 되는 메인 화면(대시보드) 패널입니다.
// 고객이 주로 이용하는 핵심 기능 세 가지(배송 신청, 내역 조회, 정보 수정)로 바로 갈 수 있도록 버튼을 배치했습니다.
public class CustomerDashboard extends JPanel {
    JButton btnShip;      // 배송 신청 기능으로 가는 버튼입니다.
    JButton btnHistory;   // 내 주문 내역을 조회하는 버튼입니다.
    JButton btnEditInfo;  // 내 정보(회원 정보)를 수정하는 버튼입니다.
    JButton btnLogout;    // 시스템에서 나가는 로그아웃 버튼입니다.

    public CustomerDashboard() {
        // 컴포넌트 위치를 정확히 지정하기 위해 Absolute Layout (null)을 사용했습니다.
        setLayout(null);
        setBackground(new Color(240, 242, 245)); // 연한 회색 배경으로 편안한 시각 환경을 제공하려 했습니다.

        // 상단 헤더 영역 구성
        JPanel header = new JPanel();
        header.setBounds(0, 0, 800, 80);
        header.setBackground(new Color(65, 105, 225)); // 시원한 로얄 블루 색상으로 헤더 영역을 강조했습니다.
        header.setLayout(null); // 헤더 내부의 컴포넌트도 절대 좌표로 배치합니다.

        // 타이틀 레이블
        JLabel lblTitle = new JLabel("Customer Service", SwingConstants.LEFT);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(30, 20, 300, 40);
        header.add(lblTitle);

        // 로그아웃 버튼 (헤더 우측)
        // 헤더 디자인에 맞게 반투명 처리하고 테두리를 제거했습니다.
        // 초기에는 마우스 호버 시 배경색이 변하는 문제를 해결하기 위해 paintComponent를 오버라이드할까 고민했지만,
        // 일단 기본 버튼 기능만 살리는 것으로 간결하게 구현했습니다.
        btnLogout = new JButton("로그아웃");
        btnLogout.setBounds(680, 25, 90, 30);
        btnLogout.setBackground(new Color(255, 255, 255, 50)); // 반투명 흰색을 적용했습니다.
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false); // 버튼의 기본 테두리 제거
        btnLogout.setFocusPainted(false); // 포커스 효과 제거
        header.add(btnLogout);

        add(header);

        // 메뉴 버튼 생성 (카드 스타일)
        // 세 개의 버튼을 나란히 배치하여 사용자가 주요 기능을 쉽게 찾을 수 있도록 했습니다. 각 기능별로 다른 색상을 부여했습니다.
        btnShip = createMenuButton("배송 신청", "🚚", 100, 150, new Color(100, 149, 237)); // 파란색 계열
        btnHistory = createMenuButton("주문 내역", "📋", 310, 150, new Color(60, 179, 113)); // 초록색 계열
        btnEditInfo = createMenuButton("정보 수정", "⚙️", 520, 150, new Color(119, 136, 153)); // 회색 계열

        add(btnShip);
        add(btnHistory);
        add(btnEditInfo);
    }

    // 대시보드에서 사용하는 정사각형 메뉴 버튼을 생성하는 보조 메소드입니다.
    // HTML을 사용하여 아이콘과 텍스트를 위아래로 깔끔하게 배치하는 디자인 패턴을 적용했습니다.
    private JButton createMenuButton(String text, String icon, int x, int y, Color color) {
        // HTML 태그를 사용해 이모지(아이콘)를 크게 표시하고 줄 바꿈(<br>)을 넣어 텍스트를 그 아래에 중앙 정렬했습니다.
        JButton btn = new JButton("<html><center><font size='6'>" + icon + "</font><br><br>" + text + "</center></html>");
        btn.setBounds(x, y, 180, 180); // 버튼 크기 고정
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 50));
        // 하단에만 굵은 컬러 바(Bar)를 넣어 입체감과 기능성을 시각적으로 강조했습니다.
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, color));
        btn.setFocusPainted(false); // 포커스 제거
        return btn;
    }

    // 외부 컨트롤러(CustomerMainController)에서 이벤트 리스너를 붙일 수 있도록 Getter 메소드를 제공합니다.
    public JButton getBtnShip() { return btnShip; }
    public JButton getBtnHistory() { return btnHistory; }
    public JButton getBtnEditInfo() { return btnEditInfo; }
    public JButton getBtnLogout() { return btnLogout; }
}