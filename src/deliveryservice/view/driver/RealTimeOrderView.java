package deliveryservice.view.driver;

import deliveryservice.domain.OrderVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 기사님이 실시간으로 대기 중인 배차 요청(콜) 목록을 보고 수락하는 화면 패널입니다.
// 이 화면은 DB에서 상태가 '대기'인 주문만 조회해서 보여줍니다.
public class RealTimeOrderView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<OrderVO> orderList; // DB에서 조회된 실시간 주문 데이터를 저장하는 리스트
    JButton btnAccept, btnHome; // 주문 수락 버튼과 홈으로 돌아가는 버튼

    // 테이블 헤더 (사용자에게 보여줄 컬럼명)
    String[] header = {"주문번호", "출발지", "도착지", "화물정보", "운임", "상차일시", "고객ID"};

    public RealTimeOrderView() {
        // BorderLayout을 사용하여 상단(정보), 중앙(테이블), 하단(버튼)을 명확히 분리했습니다.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // 전체 배경 흰색

        // 상단 패널: 정보 메시지와 홈 버튼을 우측에 배치
        JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pNorth.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("현재 대기 중인 콜 목록입니다.   ");
        lblInfo.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        btnHome = new JButton("🏠 홈으로");
        btnHome.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        btnHome.setBackground(new Color(230, 230, 250)); // 연한 보라색으로 포인트를 주었습니다.
        btnHome.setFocusPainted(false); // 버튼 클릭 시 생기는 포커스 테두리 제거

        pNorth.add(lblInfo);
        pNorth.add(btnHome);
        add(pNorth, BorderLayout.NORTH);

        // 하단 버튼 패널
        JPanel pSouth = new JPanel();
        pSouth.setBackground(Color.WHITE);
        pSouth.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // 배차 수락 버튼: 이 화면에서 가장 중요한 기능이므로 크기와 색상을 강조했습니다.
        btnAccept = new JButton("선택한 주문 받기 (배차)");
        btnAccept.setPreferredSize(new Dimension(300, 60)); // 크기도 큼직하게 설정했습니다.
        btnAccept.setBackground(new Color(0, 102, 204)); // 진한 파란색 (눈에 잘 띄게)
        btnAccept.setForeground(Color.WHITE); // 글자색 흰색
        btnAccept.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // 버튼 스타일 (투명해지는 문제 해결)
        btnAccept.setFocusPainted(false);
        btnAccept.setBorderPainted(false); // 플랫 스타일
        // 스윙 버튼이 OS 룩앤필을 따라가면서 배경색이 적용되지 않고 투명해지는 문제가 있었습니다.
        // setOpaque(true)로 설정해 주어야 배경색(setBackground)이 제대로 보이게 되어 이 문제를 해결했습니다.
        btnAccept.setOpaque(true);

        pSouth.add(btnAccept);
        add(pSouth, BorderLayout.SOUTH);
    }

    public void initView() {
        // 테이블 모델 설정: 테이블의 데이터를 관리하며, 수정 불가능하도록 오버라이드했습니다.
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30); // 행 높이를 키워서 시인성을 높였습니다.
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));

        // 컬럼 너비 조정: 중요한 정보(주문번호, 화물정보, 시간)는 넓게 보이도록 설정했습니다.
        table.getColumnModel().getColumn(0).setPreferredWidth(120); // 주문번호
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // 화물정보
        table.getColumnModel().getColumn(5).setPreferredWidth(130); // 상차일시

        JScrollPane scroll = new JScrollPane(table);
        updateTable(); // 초기 데이터가 있다면 테이블을 갱신합니다.
        add(scroll, BorderLayout.CENTER);
    }

    // DB에서 가져온 리스트를 테이블에 실제 반영하는 메소드입니다. (테이블 새로고침 기능)
    public void updateTable() {
        model.setRowCount(0); // 기존 데이터를 모두 지웁니다.
        if (orderList != null) {
            for(OrderVO vo : orderList) {
                // VO 객체의 데이터를 배열로 만들어 테이블 모델에 한 행씩 추가합니다.
                model.addRow(new Object[] {
                        vo.getOrderId(), vo.getOrigin(), vo.getDest(),
                        vo.getCargoInfo(), vo.getPrice(), vo.getPickupTime(), vo.getUserId()
                });
            }
        }
    }

    // 컨트롤러에서 DB 데이터를 받아와서 리스트에 저장하는 Setter입니다.
    public void setOrderList(ArrayList<OrderVO> list) {
        this.orderList = list;
    }

    // 외부 컨트롤러에서 이벤트 리스너를 붙이거나 테이블 정보를 가져갈 수 있도록 Getter를 제공합니다.
    public JButton getBtnAccept() { return btnAccept; }
    public JButton getBtnHome() { return btnHome; }
    public JTable getTable() { return table; }
}