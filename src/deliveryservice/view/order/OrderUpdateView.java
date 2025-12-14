package deliveryservice.view.order;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 주문 내역을 보여주고, 사용자가 선택한 주문을 수정하거나 삭제(취소)할 수 있게 하는 화면 패널입니다.
// 테이블 클릭 이벤트를 통해 아래쪽 폼에 데이터를 채우는 '테이블-폼 연동' 구조가 핵심입니다.
public class OrderUpdateView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderVOList; // DB에서 가져온 모든 주문 리스트

    // 테이블 헤더: 컬럼명이 됩니다.
    String[] header = {"주문번호", "고객ID", "출발지", "도착지", "화물정보", "운임", "상태"};

    JPanel panS, panN; // 하단(폼) 패널과 상단(버튼) 패널
    JTextField[] tf; // 상세 정보를 보여줄 텍스트 필드 배열
    JComboBox<String> statusCombo; // 주문 상태를 변경할 때 사용하는 콤보박스

    // 주문취소 버튼이 새로 추가되었습니다.
    JButton btnUpdate, btnDelete, btnHome;

    public OrderUpdateView() {
        // 전체 구조는 BorderLayout으로 상단(버튼), 중앙(테이블), 하단(폼)을 나눴습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단: 홈 버튼 배치
        panN = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panN.setBackground(Color.WHITE);
        btnHome = new JButton("🏠 홈으로");
        styleButton(btnHome, new Color(119, 136, 153)); // 회색 계열
        panN.add(btnHome);
        add(panN, BorderLayout.NORTH);

        // 하단 입력폼 (GridBagLayout 사용)
        panS = new JPanel(new GridBagLayout());
        panS.setBackground(Color.WHITE);
        panS.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] labels = {"주문번호", "출발지", "도착지", "화물정보", "운임"};
        tf = new JTextField[labels.length];

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        // 폼 필드들을 반복문으로 배치합니다.
        for(int i = 0; i < labels.length; i++){
            c.gridx = (i % 2) * 2; c.gridy = i / 2; c.weightx = 0.1; // 라벨 배치
            JLabel l = new JLabel(labels[i], SwingConstants.RIGHT);
            l.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            panS.add(l, c);

            tf[i] = new JTextField(15);
            tf[i].setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            c.gridx = (i % 2) * 2 + 1; c.weightx = 0.4; // 텍스트 필드 배치
            panS.add(tf[i], c);
        }

        // 주문번호는 기본키(PK)이므로 수정 불가능하게 막았습니다.
        tf[0].setEditable(false);

        // 주문 상태 콤보박스 배치
        c.gridx = 0; c.gridy = 3; c.weightx = 0.1;
        panS.add(new JLabel("상태", SwingConstants.RIGHT), c);

        statusCombo = new JComboBox<>(new String[]{"대기", "배차", "완료"});
        statusCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        c.gridx = 1; c.weightx = 0.4;
        panS.add(statusCombo, c);

        // 버튼 패널 (수정 / 취소)
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtn.setBackground(Color.WHITE);

        // 주문 취소 버튼: 삭제 기능은 민감하므로 진한 빨간색으로 시각적 경고를 주었습니다.
        btnDelete = new JButton("주문 취소");
        styleButton(btnDelete, new Color(220, 20, 60));

        btnUpdate = new JButton("주문 수정");
        styleButton(btnUpdate, new Color(0, 102, 204)); // 수정 버튼은 메인 색상인 파랑

        pBtn.add(btnDelete);
        pBtn.add(btnUpdate);

        // 버튼 패널을 GridBagLayout 오른쪽 아래에 배치합니다.
        c.gridx = 2; c.gridy = 3; c.gridwidth = 2; c.weightx = 0.0; c.anchor = GridBagConstraints.EAST;
        panS.add(pBtn, c);
    }

    // 버튼 스타일링 보조 메소드
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true); // 배경색이 투명해지는 문제를 방지
    }

    // 테이블에서 행을 클릭했을 때 하단 폼에 데이터를 채우는 메소드입니다.
    public void setTextField(int rowIndex) {
        // 테이블 모델에서 직접 데이터를 읽어와서 텍스트 필드와 콤보박스에 세팅합니다.
        tf[0].setText(model.getValueAt(rowIndex, 0).toString());
        tf[1].setText(model.getValueAt(rowIndex, 2).toString());
        tf[2].setText(model.getValueAt(rowIndex, 3).toString());
        tf[3].setText(model.getValueAt(rowIndex, 4).toString());
        tf[4].setText(model.getValueAt(rowIndex, 5).toString());
        statusCombo.setSelectedItem(model.getValueAt(rowIndex, 6).toString());
    }

    // 테이블 초기화 및 화면 배치
    public void initView() {
        // 테이블 모델 설정: 수정 불가능하도록 오버라이드
        model = new DefaultTableModel(header, orderVOList.size()) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        putSearchResult(); // 데이터 테이블에 반영
        add(scrollPane, BorderLayout.CENTER); // 테이블을 중앙에 배치
        add(panS, BorderLayout.SOUTH); // 폼을 하단에 배치
    }

    // DB 리스트의 내용을 테이블 모델에 반영하는 메소드입니다. (새로고침 기능)
    public void putSearchResult() {
        // 리스트 크기에 맞게 테이블 행 개수를 설정합니다.
        model.setRowCount(orderVOList.size());
        for (int i = 0; i < orderVOList.size(); i++) {
            OrderVO vo = orderVOList.get(i);
            // VO 객체에서 데이터를 추출하여 테이블 모델의 각 셀에 할당합니다.
            model.setValueAt(vo.getOrderId(), i, 0);
            model.setValueAt(vo.getUserId(), i, 1);
            model.setValueAt(vo.getOrigin(), i, 2);
            model.setValueAt(vo.getDest(), i, 3);
            model.setValueAt(vo.getCargoInfo(), i, 4);
            model.setValueAt(vo.getPrice(), i, 5);
            model.setValueAt(vo.getStatus(), i, 6);
        }
    }

    // 버튼 Getter
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; } // 주문 취소 버튼 Getter
    public JButton getBtnHome() { return btnHome; }
    public JTable getTable() { return table; }
    public void setOrderVOList(ArrayList<OrderVO> list) { this.orderVOList = list; }

    // 하단 폼에 입력된(혹은 테이블에서 가져온) 데이터를 VO 객체로 묶어 반환합니다. (수정/삭제 시 사용)
    public OrderVO neededUpdateData() {
        OrderVO vo = new OrderVO();
        vo.setOrderId(tf[0].getText()); // PK
        vo.setOrigin(tf[1].getText());
        vo.setDest(tf[2].getText());
        vo.setCargoInfo(tf[3].getText());
        // 운임은 숫자로 변환해야 하므로, 예외 발생 시 0으로 처리하여 방어했습니다.
        try { vo.setPrice(Integer.parseInt(tf[4].getText())); } catch(Exception e){}
        vo.setStatus((String)statusCombo.getSelectedItem());
        return vo;
    }
}