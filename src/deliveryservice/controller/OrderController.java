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

public class OrderController extends JFrame {
    OrderSearchView searchPan;
    OrderInsertView insertPan;
    OrderUpdateView updatePan;

    OrderRepository orderRepository;
    ArrayList<OrderVO> orderVOList;
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

    public OrderController() {
        orderRepository = new OrderRepository();

        // 1. [화물검색] 탭 구성
        searchPan = new OrderSearchView();
        refreshData(); // 데이터 불러오기
        searchPan.initView(); // ★ 초기화 필수
        searchPan.getBtnSearch().addActionListener(btnSearchL);
        tab.add("화물검색", searchPan);

        // 2. [주문등록] 탭 구성 (수정됨: 에러 원인 해결)
        insertPan = new OrderInsertView();
        // ★ 에러 나던 setOrderVOList, initView 삭제함 (이제 필요 없음)
        insertPan.setUserId("admin"); // 관리자 모드용 기본값
        insertPan.getBtnSubmit().addActionListener(btnInsertL);
        tab.add("주문등록", insertPan);

        // 3. [수정 및 삭제] 탭 구성
        updatePan = new OrderUpdateView();
        updatePan.setOrderVOList(orderVOList);
        updatePan.initView(); // ★ 초기화 필수
        updatePan.getBtnUpdate().addActionListener(btnUpdateL);
        updatePan.getTable().addMouseListener(tableUpdateL);
        tab.add("수정 및 삭제", updatePan);

        // 탭 전환시 데이터 갱신
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { refreshData(); }
        });

        add(tab);
        setTitle("화물 배차 관리 시스템 (통합 관리자용)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    private void refreshData() {
        orderVOList = orderRepository.select("", 0);

        // 검색 탭 갱신
        searchPan.setOrderVOList(orderVOList);
        searchPan.pubSearchResult();

        // 수정 탭 갱신
        updatePan.setOrderVOList(orderVOList);
        updatePan.putSearchResult();
    }

    // --- 이벤트 리스너 ---

    ActionListener btnSearchL = e -> {
        orderVOList = orderRepository.select(searchPan.getSearchWord(), searchPan.getCombo().getSelectedIndex());
        searchPan.setOrderVOList(orderVOList);
        searchPan.pubSearchResult();
    };

    ActionListener btnInsertL = e -> {
        try {
            OrderVO vo = insertPan.getOrderData(); // 메소드 이름 변경됨 (needed -> get)

            if(vo.getUserId().isEmpty()) {
                JOptionPane.showMessageDialog(null, "고객 ID가 필요합니다.");
                return;
            }

            orderRepository.insert(vo);

            JOptionPane.showMessageDialog(null, "주문 등록 완료");
            refreshData();
            insertPan.clear(); // 메소드 이름 변경됨 (init -> clear)
            tab.setSelectedIndex(0); // 검색 탭으로 이동

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "입력 오류: " + ex.getMessage());
        }
    };

    ActionListener btnUpdateL = e -> {
        try {
            OrderVO vo = updatePan.neededUpdateData();
            orderRepository.update(vo);
            refreshData();
            JOptionPane.showMessageDialog(null, "수정 완료");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "수정 실패: " + ex.getMessage());
        }
    };

    MouseAdapter tableUpdateL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = updatePan.getTable().getSelectedRow();
            if (e.getClickCount() == 1) {
                updatePan.setTextField(row);
            } else if (e.getClickCount() == 2) {
                int ans = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    OrderVO vo = updatePan.neededUpdateData();
                    orderRepository.delete(vo);
                    refreshData();
                }
            }
        }
    };

    public static void main(String[] args) {
        new OrderController();
    }
}