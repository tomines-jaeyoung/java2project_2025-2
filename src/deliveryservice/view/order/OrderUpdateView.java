package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderUpdateView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderVOList;
    String[] header = {"주문번호", "고객ID", "출발지", "도착지", "화물정보", "운임", "상태"};

    JPanel panS;
    JTextField[] tf;
    JComboBox<String> statusCombo;
    JButton btnUpdate;

    public OrderUpdateView() {
        setLayout(new BorderLayout());

        // --- 하단 입력 패널 디자인 개선 (GridBagLayout 사용) ---
        panS = new JPanel(new GridBagLayout());
        panS.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] labels = {"주문번호", "출발지", "도착지", "화물정보", "운임"};
        tf = new JTextField[labels.length];

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5); // 간격 조정

        // 1. 텍스트 필드 배치 루프
        for(int i = 0; i < labels.length; i++){
            // 라벨
            c.gridx = (i % 2) * 2; // 0열 혹은 2열
            c.gridy = i / 2;       // 행
            c.weightx = 0.1;
            panS.add(new JLabel(labels[i], SwingConstants.RIGHT), c);

            // 입력창
            tf[i] = new JTextField(15);
            c.gridx = (i % 2) * 2 + 1; // 1열 혹은 3열
            c.weightx = 0.4;
            panS.add(tf[i], c);
        }

        tf[0].setEditable(false); // 주문번호 수정 불가

        // 2. 상태 콤보박스 배치 (마지막 줄 왼쪽)
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.1;
        panS.add(new JLabel("상태", SwingConstants.RIGHT), c);

        statusCombo = new JComboBox<>(new String[]{"대기", "배차", "완료"});
        c.gridx = 1;
        c.weightx = 0.4;
        panS.add(statusCombo, c);

        // 3. 수정 버튼 배치 (마지막 줄 오른쪽 끝)
        btnUpdate = new JButton("주문수정");
        btnUpdate.setPreferredSize(new Dimension(100, 35));
        c.gridx = 3;
        c.gridy = 3;
        c.weightx = 0.0; // 버튼은 늘어나지 않게
        c.anchor = GridBagConstraints.EAST;
        panS.add(btnUpdate, c);
    }

    public void setTextField(int rowIndex) {
        tf[0].setText(model.getValueAt(rowIndex, 0).toString());
        tf[1].setText(model.getValueAt(rowIndex, 2).toString());
        tf[2].setText(model.getValueAt(rowIndex, 3).toString());
        tf[3].setText(model.getValueAt(rowIndex, 4).toString());
        tf[4].setText(model.getValueAt(rowIndex, 5).toString());
        statusCombo.setSelectedItem(model.getValueAt(rowIndex, 6).toString());
    }

    public void initView() {
        model = new DefaultTableModel(header, orderVOList.size()) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        putSearchResult();

        add(scrollPane, BorderLayout.CENTER);
        add(panS, BorderLayout.SOUTH);
    }

    public void putSearchResult() {
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

    public JButton getBtnUpdate() { return btnUpdate; }
    public JTable getTable() { return table; }
    public void setOrderVOList(ArrayList<OrderVO> list) { this.orderVOList = list; }

    public OrderVO neededUpdateData() {
        OrderVO vo = new OrderVO();
        vo.setOrderId(tf[0].getText());
        vo.setOrigin(tf[1].getText());
        vo.setDest(tf[2].getText());
        vo.setCargoInfo(tf[3].getText());
        vo.setPrice(Integer.parseInt(tf[4].getText()));
        vo.setStatus((String)statusCombo.getSelectedItem());
        return vo;
    }
}