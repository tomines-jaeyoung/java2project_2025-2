package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderSearchView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderVOList;
    String[] header = {"주문번호", "고객ID", "출발지", "도착지", "화물정보", "운임", "상태"};

    JLabel lbl;
    JTextField textSearch;
    JButton btnSearch;
    JButton btnHome; // ★ 홈으로 버튼 추가

    JPanel panN;
    JComboBox<String> combo;
    String[] comboStr = {"주문번호", "출발지", "도착지"};

    public OrderSearchView() {
        setLayout(new BorderLayout());
        combo = new JComboBox<>(comboStr);
        lbl = new JLabel("검색어: ");
        textSearch = new JTextField(20);
        btnSearch = new JButton("검색");

        // ★ 홈 버튼 생성 및 디자인
        btnHome = new JButton("🏠 홈으로");
        btnHome.setBackground(new Color(230, 230, 250));

        panN = new JPanel();
        panN.add(combo);
        panN.add(lbl);
        panN.add(textSearch);
        panN.add(btnSearch);
        panN.add(Box.createHorizontalStrut(20)); // 간격 띄우기
        panN.add(btnHome); // 패널에 추가
    }

    public void initView() {
        model = new DefaultTableModel(header, orderVOList.size()) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        pubSearchResult();
        add(panN, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void pubSearchResult() {
        model.setRowCount(orderVOList.size());
        for (int i = 0; i < orderVOList.size(); i++) {
            OrderVO vo = orderVOList.get(i);
            model.setValueAt(vo.getOrderId(), i, 0);
            model.setValueAt(vo.getUserId(), i, 1);
            model.setValueAt(vo.getOrigin(), i, 2);
            model.setValueAt(vo.getDest(), i, 3);
            model.setValueAt(vo.getCargoInfo(), i, 4);
            model.setValueAt(vo.getPrice(), i, 5);
            model.setValueAt(vo.getStatus(), i, 6);
        }
    }

    public String getSearchWord() { return textSearch.getText(); }
    public void setOrderVOList(ArrayList<OrderVO> list) { this.orderVOList = list; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnHome() { return btnHome; } // Getter 추가
    public JComboBox<String> getCombo() { return combo; }
}