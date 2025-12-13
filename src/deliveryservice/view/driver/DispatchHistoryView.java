package deliveryservice.view.driver;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DispatchHistoryView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> historyList;

    // 상단 검색 패널 컴포넌트
    JPanel panN;
    JComboBox<String> combo;
    JTextField tfSearch;
    JButton btnSearch;
    JButton btnHome;

    String[] header = {"주문번호", "출발지", "도착지", "화물정보", "운임", "상태"};
    String[] comboStr = {"주문번호", "출발지", "도착지"};

    public DispatchHistoryView() {
        setLayout(new BorderLayout());

        // --- 상단 검색 패널 구성 ---
        panN = new JPanel();

        combo = new JComboBox<>(comboStr);
        tfSearch = new JTextField(15);
        btnSearch = new JButton("검색");

        btnHome = new JButton("🏠 홈으로");
        btnHome.setBackground(new Color(230, 230, 250));

        panN.add(new JLabel("검색조건: "));
        panN.add(combo);
        panN.add(tfSearch);
        panN.add(btnSearch);
        panN.add(Box.createHorizontalStrut(20)); // 간격
        panN.add(btnHome);

        add(panN, BorderLayout.NORTH);
    }

    public void initView() {
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);

        // 컬럼 너비 살짝 조정
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(table);
        updateTable();
        add(scroll, BorderLayout.CENTER);
    }

    public void updateTable() {
        model.setRowCount(0);
        if (historyList != null) {
            for(OrderVO vo : historyList) {
                model.addRow(new Object[] {
                        vo.getOrderId(), vo.getOrigin(), vo.getDest(),
                        vo.getCargoInfo(), vo.getPrice(), vo.getStatus()
                });
            }
        }
    }

    public void setHistoryList(ArrayList<OrderVO> list) {
        this.historyList = list;
    }

    // Controller에서 사용할 Getter들
    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnSearch() { return btnSearch; }
    public String getSearchWord() { return tfSearch.getText(); }
    public int getComboIndex() { return combo.getSelectedIndex(); }
}