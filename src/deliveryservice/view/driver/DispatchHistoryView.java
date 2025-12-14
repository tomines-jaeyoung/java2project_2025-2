package deliveryservice.view.driver;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 기사님이 본인이 수락한 배차 내역(Dispatch History)을 조회하고 관리하는 화면 패널입니다.
// 일반 고객의 '주문 조회' 화면과 비슷하지만, 아래쪽에 '배차 취소', '건의' 같은 기사 전용 기능이 있습니다.
public class DispatchHistoryView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> historyList; // DB에서 가져온 주문 목록을 담는 리스트

    // 상단 검색 컴포넌트
    JPanel panN;
    JComboBox<String> combo;
    JTextField tfSearch;
    JButton btnSearch, btnHome;
    // 테이블 헤더와 검색 조건의 문자열 배열입니다.
    String[] header = {"주문번호", "출발지", "도착지", "화물정보", "운임", "상차일시", "상태"};
    String[] comboStr = {"주문번호", "출발지", "도착지"};

    // 하단 배차 관리 패널 (고객 화면과 동일한 구조로 재활용했습니다.)
    JPanel panS;
    JTextField[] tf; // 상세 정보 표시용: 테이블에서 선택된 행의 정보를 여기에 뿌려줍니다.
    JButton btnSuggest, btnCancel; // 배차 건의, 배차 취소 버튼

    public DispatchHistoryView() {
        // 전체 레이아웃은 BorderLayout으로 상단(검색), 중앙(테이블), 하단(상세 폼)을 나눴습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 검색 패널
        panN = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panN.setBackground(Color.WHITE);

        combo = new JComboBox<>(comboStr);
        combo.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);

        tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        btnSearch = new JButton("검색");
        styleButton(btnSearch, new Color(255, 140, 0)); // 기사 화면의 메인 색상인 주황 계열로 버튼 스타일을 통일했습니다.

        btnHome = new JButton("🏠 홈으로");
        styleButton(btnHome, new Color(119, 136, 153)); // 회색으로 뒤로가기 버튼을 만들었습니다.

        panN.add(new JLabel("검색 조건: "));
        panN.add(combo);
        panN.add(tfSearch);
        panN.add(btnSearch);
        panN.add(Box.createHorizontalStrut(20)); // 컴포넌트 사이에 수평 공간을 강제로 추가했습니다.
        panN.add(btnHome);

        add(panN, BorderLayout.NORTH);

        // 하단 배차 관리 폼 (GridBagLayout)
        // 복잡한 컴포넌트를 깔끔하게 배치하기 위해 GridBagLayout을 사용했습니다.
        panS = new JPanel(new GridBagLayout());
        panS.setBackground(Color.WHITE);
        panS.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] labels = {"주문번호", "출발지", "도착지", "화물정보", "운임", "상차일시"};
        tf = new JTextField[labels.length];

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        // 라벨과 텍스트 필드를 반복문을 이용해 두 줄로 배치했습니다.
        for(int i = 0; i < labels.length; i++){
            c.gridx = (i % 2) * 2; c.gridy = i / 2; c.weightx = 0.1; // 라벨
            JLabel l = new JLabel(labels[i], SwingConstants.RIGHT);
            l.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            panS.add(l, c);

            tf[i] = new JTextField(15);
            tf[i].setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            tf[i].setEditable(false); // 기사는 정보 수정 권한이 없으므로 입력 불가능하도록 막았습니다.
            tf[i].setBackground(Color.WHITE);
            c.gridx = (i % 2) * 2 + 1; c.weightx = 0.4; // 텍스트 필드
            panS.add(tf[i], c);
        }

        // 버튼 패널
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtn.setBackground(Color.WHITE);

        btnCancel = new JButton("배차 취소");
        styleButton(btnCancel, new Color(220, 20, 60)); // 위험한 작업이므로 빨간색으로 강조

        btnSuggest = new JButton("배차 건의");
        styleButton(btnSuggest, new Color(255, 140, 0)); // 주요 작업이므로 주황색으로 설정

        pBtn.add(btnCancel);
        pBtn.add(btnSuggest);

        // 버튼 패널을 GridBagLayout 오른쪽 하단에 배치하도록 설정했습니다.
        c.gridx = 2; c.gridy = 3; c.gridwidth = 2; c.weightx = 0.0; c.anchor = GridBagConstraints.EAST;
        panS.add(pBtn, c);

        add(panS, BorderLayout.SOUTH);
    }

    // 버튼 공통 스타일링 메소드
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true); // 배경색이 제대로 보이도록 설정
    }

    // 테이블 초기화 및 스크롤 패널 추가
    public void initView() {
        // 테이블 데이터를 수정하지 못하게 DefaultTableModel을 상속받아 isCellEditable을 false로 오버라이드했습니다.
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        // 컬럼 폭을 조절해서 데이터가 잘리지 않게 했습니다.
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane scroll = new JScrollPane(table);
        updateTable();
        add(scroll, BorderLayout.CENTER);
    }

    // 리스트의 내용을 테이블에 반영 (테이블 새로고침)
    public void updateTable() {
        model.setRowCount(0); // 기존 데이터 싹 지우기
        if (historyList != null) {
            for(OrderVO vo : historyList) {
                model.addRow(new Object[] {
                        vo.getOrderId(), vo.getOrigin(), vo.getDest(),
                        vo.getCargoInfo(), vo.getPrice(), vo.getPickupTime(), vo.getStatus()
                });
            }
        }
    }

    // 선택된 행의 데이터를 하단 필드에 채우기
    public void setTextField(int rowIndex) {
        if(rowIndex < 0) return; // 선택된 행이 없을 경우 처리
        // 테이블 모델에서 직접 데이터를 가져와서 텍스트 필드 배열에 넣어줍니다.
        tf[0].setText(model.getValueAt(rowIndex, 0).toString());
        tf[1].setText(model.getValueAt(rowIndex, 1).toString());
        tf[2].setText(model.getValueAt(rowIndex, 2).toString());
        tf[3].setText(model.getValueAt(rowIndex, 3).toString());
        tf[4].setText(model.getValueAt(rowIndex, 4).toString());
        tf[5].setText(model.getValueAt(rowIndex, 5).toString());
    }

    // 하단 필드에 채워진 주문번호만 VO 객체로 반환합니다. (취소, 건의 시 주문번호가 필요합니다.)
    public OrderVO getSelectedOrder() {
        OrderVO vo = new OrderVO();
        vo.setOrderId(tf[0].getText());
        return vo;
    }

    // 컨트롤러에서 데이터를 받아오는 Setter와 이벤트 처리를 위한 Getter입니다.
    public void setHistoryList(ArrayList<OrderVO> list) { this.historyList = list; }
    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnSuggest() { return btnSuggest; }
    public JButton getBtnCancel() { return btnCancel; }
    public JTable getTable() { return table; }
    public String getSearchWord() { return tfSearch.getText(); }
    public int getComboIndex() { return combo.getSelectedIndex(); }
}