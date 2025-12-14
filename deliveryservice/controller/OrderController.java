package deliveryservice.controller;

import deliveryservice.domain.OrderVO;
import deliveryservice.repository.OrderRepository;
import deliveryservice.view.order.OrderInsertView;
import deliveryservice.view.order.OrderSearchView;
import deliveryservice.view.order.OrderUpdateView;
import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

// 관리자용 주문 통합 관리 컨트롤러입니다.
// 처음에는 기능별로 창을 다 따로 띄웠는데, 관리하기 너무 복잡해서 JTabbedPane을 도입했습니다.
// 하나의 창에서 탭만 누르면 검색, 등록, 수정/삭제를 다 할 수 있게 구조를 개선해 보았습니다.
public class OrderController extends JFrame {
    OrderSearchView searchPan;
    OrderInsertView insertPan;
    OrderUpdateView updatePan;

    OrderRepository orderRepository;
    ArrayList<OrderVO> orderVOList;
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP); // 상단에 탭이 위치하도록 설정

    public OrderController() {
        orderRepository = new OrderRepository();

        // 1. [화물검색] 탭 구성
        // 프로그램 실행되자마자 데이터가 보여야 해서 refreshData()를 먼저 호출했습니다.
        searchPan = new OrderSearchView();
        refreshData(); // DB에서 데이터 가져오기
        searchPan.initView(); // ★ 초기화 필수 (안 하면 화면이 깨지거나 빈 화면이 뜸)
        searchPan.getBtnSearch().addActionListener(btnSearchL);
        tab.add("화물검색", searchPan);

        // 2. [주문등록] 탭 구성 (수정됨: 에러 원인 해결)
        insertPan = new OrderInsertView();
        //  예전 코드에서 여기서 자꾸 NullPointer 에러가 나서 확인해보니,
        // 등록 화면은 리스트를 보여줄 필요가 없어서 setOrderVOList 관련 코드를 삭제했습니다.
        insertPan.setUserId("admin"); // 관리자 모드이므로 기본 아이디는 admin으로 고정했습니다.
        insertPan.getBtnSubmit().addActionListener(btnInsertL);
        tab.add("주문등록", insertPan);

        // 3. [수정 및 삭제] 탭 구성
        updatePan = new OrderUpdateView();
        updatePan.setOrderVOList(orderVOList); // 검색 탭과 동일한 데이터를 공유합니다.
        updatePan.initView(); // ★ 초기화 필수
        updatePan.getBtnUpdate().addActionListener(btnUpdateL);
        updatePan.getTable().addMouseListener(tableUpdateL); // 테이블 클릭 이벤트 연결
        tab.add("수정 및 삭제", updatePan);

        // 탭 전환시 데이터 갱신
        // 이 부분이 제일 중요합니다!! 등록 탭에서 주문을 넣고 검색 탭으로 왔는데 데이터가 안 떠서 당황했었습니다.
        // 탭을 클릭할 때마다 DB를 새로 조회(refreshData)하도록 해서 동기화 문제를 해결했습니다.
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { refreshData(); }
        });

        add(tab);
        setTitle("화물 배차 관리 시스템 (통합 관리자용)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 창 위치 잡기
        // CenterFrame이라는 유틸 클래스를 써서 중앙 정렬을 시도하고, 만약 실패하면 수동으로 잡도록 예외처리했습니다.
        try {
            CenterFrame cf = new CenterFrame(800, 600);
            cf.centerXY();
            setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        } catch(Exception e) {
            setSize(800, 600);
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    // 데이터를 새로고침하는 메소드입니다.
    // 검색 탭과 수정 탭 두 군데 모두 데이터를 뿌려줘야 해서 메소드로 묶었습니다.
    private void refreshData() {
        orderVOList = orderRepository.select("", 0); // 전체 조회

        // 검색 탭 갱신
        searchPan.setOrderVOList(orderVOList);
        searchPan.pubSearchResult();

        // 수정 탭 갱신
        updatePan.setOrderVOList(orderVOList);
        updatePan.putSearchResult();
    }

    // --- 이벤트 리스너 ---
    // 코드가 너무 길어져서 익명 내부 클래스 대신 람다식으로 멤버 변수에 할당하는 방식을 써봤습니다.

    // 검색 버튼 리스너
    ActionListener btnSearchL = e -> {
        // 콤보박스 인덱스랑 검색어를 가져와서 필터링된 데이터를 조회합니다.
        orderVOList = orderRepository.select(searchPan.getSearchWord(), searchPan.getCombo().getSelectedIndex());
        searchPan.setOrderVOList(orderVOList);
        searchPan.pubSearchResult();
    };

    // 등록 버튼 리스너
    ActionListener btnInsertL = e -> {
        try {
            OrderVO vo = insertPan.getOrderData(); // 메소드 이름 변경됨 (needed -> get) : 직관적으로 바꿨습니다.

            // 유효성 검사: 아이디가 없으면 안 되니까 막았습니다.
            if(vo.getUserId().isEmpty()) {
                JOptionPane.showMessageDialog(null, "고객 ID가 필요합니다.");
                return;
            }

            orderRepository.insert(vo); // DB 저장

            JOptionPane.showMessageDialog(null, "주문 등록 완료");
            refreshData(); // 등록 후 리스트 갱신
            insertPan.clear(); // 입력창 초기화 (init -> clear 로 이름 변경)
            tab.setSelectedIndex(0); // 등록 끝나면 바로 확인할 수 있게 검색 탭으로 이동시킵니다. (UX 고려)

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "입력 오류: " + ex.getMessage());
        }
    };

    // 수정 버튼 리스너
    ActionListener btnUpdateL = e -> {
        try {
            OrderVO vo = updatePan.neededUpdateData(); // 수정할 데이터 가져오기
            orderRepository.update(vo);
            refreshData(); // 수정된 내용 바로 반영
            JOptionPane.showMessageDialog(null, "수정 완료");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "수정 실패: " + ex.getMessage());
        }
    };

    // 테이블 마우스 클릭 이벤트 (수정/삭제 탭)
    MouseAdapter tableUpdateL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = updatePan.getTable().getSelectedRow();
            // 클릭 횟수에 따라 기능을 나눴습니다.
            // 한 번 클릭하면 -> 데이터를 텍스트 필드로 가져와서 수정 준비
            if (e.getClickCount() == 1) {
                updatePan.setTextField(row);
            }
            // 더블 클릭하면 -> 삭제 기능 실행 (실수 방지를 위해 더블클릭으로 함)
            else if (e.getClickCount() == 2) {
                int ans = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    OrderVO vo = updatePan.neededUpdateData();
                    orderRepository.delete(vo);
                    refreshData(); // 삭제 후 목록 갱신
                }
            }
        }
    };

    public static void main(String[] args) {
        new OrderController();
    }
}