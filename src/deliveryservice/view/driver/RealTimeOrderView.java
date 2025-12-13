package deliveryservice.view.driver;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class RealTimeOrderView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderList;
    JButton btnAccept, btnHome;

    // 테이블 헤더
    String[] header = {"주문번호", "출발지", "도착지", "화물정보", "운임", "상차일시", "고객ID"};

    public RealTimeOrderView() {
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnHome = new JButton("🏠 홈으로");
        btnHome.setBackground(Color.WHITE);
        pNorth.add(new JLabel("현재 대기 중인 콜 목록입니다.   "));
        pNorth.add(btnHome);
        add(pNorth, BorderLayout.NORTH);

        // 하단 버튼
        btnAccept = new JButton("선택한 주문 받기 (배차)");
        btnAccept.setPreferredSize(new Dimension(200, 50));
        btnAccept.setBackground(new Color(0, 100, 0)); // Dark Green
        btnAccept.setForeground(Color.WHITE);
        btnAccept.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        JPanel pSouth = new JPanel();
        pSouth.add(btnAccept);
        add(pSouth, BorderLayout.SOUTH);
    }

    public void initView() {
        // 테이블 모델 생성
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        // 컬럼 너비 조정
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(table);
        updateTable();
        add(scroll, BorderLayout.CENTER);
    }

    public void updateTable() {
        model.setRowCount(0);
        if (orderList != null) {
            for(OrderVO vo : orderList) {
                model.addRow(new Object[] {
                        vo.getOrderId(), vo.getOrigin(), vo.getDest(),
                        vo.getCargoInfo(), vo.getPrice(), vo.getPickupTime(), vo.getUserId()
                });
            }
        }
    }

    // ★ Controller에서 호출하는 메서드
    public void setOrderList(ArrayList<OrderVO> list) {
        this.orderList = list;
    }

    public JButton getBtnAccept() { return btnAccept; }
    public JButton getBtnHome() { return btnHome; }
    public JTable getTable() { return table; }
}