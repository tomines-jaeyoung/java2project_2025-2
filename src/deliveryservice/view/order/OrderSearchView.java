package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 주문 내역을 조회하는 화면 패널입니다. 고객용 컨트롤러와 통합 관리자용 컨트롤러에서 모두 사용됩니다.
// 테이블을 이용해 DB에서 가져온 데이터를 깔끔한 목록 형태로 사용자에게 보여주는 것이 주된 목적입니다.
public class OrderSearchView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderVOList; // DB에서 가져온 데이터를 담는 리스트입니다.

    // 테이블 헤더: 컬럼명이 됩니다. "상차일시" 컬럼이 추가되어 총 8개 컬럼이 되었습니다.
    String[] header = {"주문번호", "고객ID", "출발지", "도착지", "화물정보", "운임", "상차일시", "상태"};

    JLabel lbl;
    JTextField textSearch;
    JButton btnSearch;
    JButton btnHome;

    JPanel panN; // 상단 검색 컴포넌트들을 담는 패널입니다.
    JComboBox<String> combo;
    String[] comboStr = {"주문번호", "출발지", "도착지"}; // 검색 가능한 조건 목록

    public OrderSearchView() {
        // 전체 구조는 BorderLayout으로 상단(검색), 중앙(테이블)을 분리했습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 검색 패널 디자인 개선
        // FlowLayout을 사용하고, 컴포넌트 간의 간격을 띄워서 정돈된 느낌을 주었습니다.
        panN = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panN.setBackground(Color.WHITE);

        // 검색 조건 콤보박스
        combo = new JComboBox<>(comboStr);
        combo.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);

        lbl = new JLabel("검색어: ");
        lbl.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 검색어 입력 필드
        textSearch = new JTextField(20);
        textSearch.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textSearch.setPreferredSize(new Dimension(200, 30));

        // 검색 버튼 (메인 색상인 로얄 블루로 설정)
        btnSearch = new JButton("검색");
        styleButton(btnSearch, new Color(65, 105, 225));

        // 홈 버튼 (보조 색상인 회색 계열로 설정)
        btnHome = new JButton("🏠 홈으로");
        styleButton(btnHome, new Color(119, 136, 153));

        // 상단 패널에 컴포넌트 순서대로 추가
        panN.add(combo);
        panN.add(lbl);
        panN.add(textSearch);
        panN.add(btnSearch);
        panN.add(Box.createHorizontalStrut(10)); // 검색 버튼과 홈 버튼 사이에 공간을 띄웠습니다.
        panN.add(btnHome);
    }

    // 테이블 초기화 및 화면 구성
    public void initView() {
        // 테이블 모델 설정: 수정 불가능하도록 DefaultTableModel을 상속받아 오버라이드했습니다.
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30); // 행 높이를 늘려 시인성을 높였습니다.
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));

        // 컬럼 너비 조정: 데이터가 많은 컬럼은 넓게 보이도록 설정했습니다.
        table.getColumnModel().getColumn(0).setPreferredWidth(120); // 주문번호
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // 화물정보
        table.getColumnModel().getColumn(6).setPreferredWidth(130); // 상차일시

        JScrollPane scrollPane = new JScrollPane(table); // 테이블은 스크롤 기능이 필수입니다.
        pubSearchResult(); // 초기 데이터를 테이블에 반영합니다.

        add(panN, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // DB에서 받아온 리스트를 테이블에 출력(게시)하는 메소드입니다.
    public void pubSearchResult() {
        model.setRowCount(0); // 기존 테이블 데이터를 모두 지워서 새로고침 효과를 냅니다.
        if (orderVOList != null) {
            for (OrderVO vo : orderVOList) {
                // VO 객체의 필드들을 순서대로 배열로 만들어 테이블에 한 행씩 추가합니다.
                model.addRow(new Object[]{
                        vo.getOrderId(),
                        vo.getUserId(),
                        vo.getOrigin(),
                        vo.getDest(),
                        vo.getCargoInfo(),
                        vo.getPrice(),
                        vo.getPickupTime(), // 여기가 상차일시(시간) 데이터입니다.
                        vo.getStatus()
                });
            }
        }
    }

    // 버튼 공통 스타일을 설정하는 보조 메소드입니다.
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(90, 32)); // 버튼 크기 통일
    }

    // 컨트롤러에서 필요한 데이터를 가져가거나 설정을 변경할 수 있도록 Getter와 Setter를 제공합니다.
    public String getSearchWord() { return textSearch.getText(); }
    public void setOrderVOList(ArrayList<OrderVO> list) { this.orderVOList = list; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnHome() { return btnHome; }
    public JComboBox<String> getCombo() { return combo; }
}