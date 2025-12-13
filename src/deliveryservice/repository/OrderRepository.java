package deliveryservice.repository;

import deliveryservice.domain.OrderVO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderRepository {
    ArrayList<OrderVO> orderVOList;

    // ==========================================
    //  [고객 기능] 조회, 등록, 수정, 삭제
    // ==========================================

    // 1. 주문 조회 (고객용 - 검색 포함)
    public ArrayList<OrderVO> select(String searchWord, int selectedIndex) {
        Connection con = JDBCConnector.getConnection();
        orderVOList = new ArrayList<OrderVO>();
        ResultSet rs = null;
        PreparedStatement psmt = null;

        // 검색 조건: 0:주문번호, 1:출발지, 2:도착지
        String[] columnName = {"order_id", "origin", "dest"};
        String sql = "SELECT * FROM ORDER_SHEET WHERE " + columnName[selectedIndex] + " LIKE ? ORDER BY order_id DESC";

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, "%" + searchWord + "%");
            rs = psmt.executeQuery();
            while (rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderId(rs.getString("order_id"));
                vo.setUserId(rs.getString("user_id"));
                vo.setOrigin(rs.getString("origin"));
                vo.setDest(rs.getString("dest"));
                vo.setCargoInfo(rs.getString("cargo_info"));
                vo.setPrice(rs.getInt("price"));
                vo.setPickupTime(rs.getDate("pickup_time"));
                vo.setStatus(rs.getString("status"));
                orderVOList.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con, psmt, rs);
        }
        return orderVOList;
    }

    // 2. 주문 등록 (트랜잭션 + ID생성) - ★이 부분이 없어서 에러가 났던 것입니다.
    public void insert(OrderVO vo) {
        Connection con = null;
        PreparedStatement psmtSeq = null;
        PreparedStatement psmtUpdateSeq = null;
        PreparedStatement psmtInsert = null;
        ResultSet rs = null;

        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // A. 시퀀스 테이블 Lock (FOR UPDATE)
            String sqlSeq = "SELECT LAST_DATE, CURR_SEQ FROM SEQ_MANAGER WHERE TABLE_NAME = 'ORDER_SHEET' FOR UPDATE";
            psmtSeq = con.prepareStatement(sqlSeq);
            rs = psmtSeq.executeQuery();

            String today = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            int nextSeq = 1;

            if (rs.next()) {
                String lastDate = rs.getString("LAST_DATE");
                int currSeq = rs.getInt("CURR_SEQ");
                if (today.equals(lastDate)) {
                    nextSeq = currSeq + 1;
                }
            }

            // B. ID 생성 (YYYYMMDD-0001)
            String newId = today + "-" + String.format("%04d", nextSeq);
            vo.setOrderId(newId);

            // C. 시퀀스 테이블 업데이트
            String sqlUpdateSeq = "UPDATE SEQ_MANAGER SET LAST_DATE = ?, CURR_SEQ = ? WHERE TABLE_NAME = 'ORDER_SHEET'";
            psmtUpdateSeq = con.prepareStatement(sqlUpdateSeq);
            psmtUpdateSeq.setString(1, today);
            psmtUpdateSeq.setInt(2, nextSeq);
            psmtUpdateSeq.executeUpdate();

            // D. 주문 데이터 INSERT
            String sqlInsert = "INSERT INTO ORDER_SHEET (order_id, user_id, origin, dest, cargo_info, price, pickup_time, status) VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?)";
            psmtInsert = con.prepareStatement(sqlInsert);
            psmtInsert.setString(1, vo.getOrderId());
            psmtInsert.setString(2, vo.getUserId());
            psmtInsert.setString(3, vo.getOrigin());
            psmtInsert.setString(4, vo.getDest());
            psmtInsert.setString(5, vo.getCargoInfo());
            psmtInsert.setInt(6, vo.getPrice());
            psmtInsert.setString(7, "대기");

            psmtInsert.executeUpdate();

            con.commit(); // 커밋

        } catch (SQLException e) {
            e.printStackTrace();
            try { if(con != null) con.rollback(); } catch(Exception ex) {}
        } finally {
            close(null, psmtInsert, null);
            close(null, psmtUpdateSeq, null);
            close(con, psmtSeq, rs);
        }
    }

    // 3. 주문 수정
    public void update(OrderVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "UPDATE ORDER_SHEET SET origin=?, dest=?, cargo_info=?, price=?, status=? WHERE order_id=?";
        PreparedStatement psmt = null;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, vo.getOrigin());
            psmt.setString(2, vo.getDest());
            psmt.setString(3, vo.getCargoInfo());
            psmt.setInt(4, vo.getPrice());
            psmt.setString(5, vo.getStatus());
            psmt.setString(6, vo.getOrderId());
            psmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        finally { close(con, psmt, null); }
    }

    // 4. 주문 삭제
    public void delete(OrderVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "DELETE FROM ORDER_SHEET WHERE order_id=?";
        PreparedStatement psmt = null;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, vo.getOrderId());
            psmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        finally { close(con, psmt, null); }
    }

    // ==========================================
    //  [기사 기능] 대기 목록, 배차 내역, 배차 수락
    // ==========================================

    // 5. 실시간 대기 주문 조회 (상태가 '대기'인 것만)
    public ArrayList<OrderVO> selectWaitingOrders() {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();
        String sql = "SELECT * FROM ORDER_SHEET WHERE status = '대기' ORDER BY order_id DESC";
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();
            while(rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderId(rs.getString("order_id"));
                vo.setUserId(rs.getString("user_id"));
                vo.setOrigin(rs.getString("origin"));
                vo.setDest(rs.getString("dest"));
                vo.setCargoInfo(rs.getString("cargo_info"));
                vo.setPrice(rs.getInt("price"));
                vo.setPickupTime(rs.getDate("pickup_time"));
                vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return list;
    }

    // 6. 내 배차 내역 조회 (JOIN + 검색 기능)
    public ArrayList<OrderVO> selectDispatchHistory(String driverId, String searchWord, int searchIndex) {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();

        // 검색 컬럼 매핑
        String[] dbCols = {"o.order_id", "o.origin", "o.dest"};
        String targetCol = dbCols[searchIndex];

        // JOIN 쿼리
        String sql = "SELECT o.* FROM ORDER_SHEET o " +
                "JOIN DISPATCH d ON o.order_id = d.order_id " +
                "WHERE d.driver_id = ? AND " + targetCol + " LIKE ? " +
                "ORDER BY d.matched_at DESC";

        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, driverId);
            psmt.setString(2, "%" + searchWord + "%");

            rs = psmt.executeQuery();
            while(rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderId(rs.getString("order_id"));
                vo.setUserId(rs.getString("user_id"));
                vo.setOrigin(rs.getString("origin"));
                vo.setDest(rs.getString("dest"));
                vo.setCargoInfo(rs.getString("cargo_info"));
                vo.setPrice(rs.getInt("price"));
                vo.setPickupTime(rs.getDate("pickup_time"));
                vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return list;
    }

    // 7. 주문 수락 (배차 처리 트랜잭션 + 적재량 체크)
    public String acceptOrder(String orderId, String driverId) {
        Connection con = null;
        PreparedStatement psmtCheck = null;
        PreparedStatement psmtOrderInfo = null;
        PreparedStatement psmtSeq = null, psmtUpdateSeq = null;
        PreparedStatement psmtDispatch = null, psmtOrder = null;
        ResultSet rs = null;
        String msg = "success";

        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // 1. 기사의 차량 정보 조회
            String carNum = null;
            int maxWeight = 0;
            String sqlCheck = "SELECT car_num, max_weight FROM VEHICLE WHERE driver_id = ?";
            psmtCheck = con.prepareStatement(sqlCheck);
            psmtCheck.setString(1, driverId);
            rs = psmtCheck.executeQuery();
            if(rs.next()) {
                carNum = rs.getString("car_num");
                maxWeight = rs.getInt("max_weight");
            } else {
                return "등록된 차량이 없습니다. 차량을 먼저 등록해주세요.";
            }
            rs.close(); psmtCheck.close();

            // 2. 주문 화물 정보 조회 (무게 비교를 위해)
            String cargoInfo = "";
            String sqlOrderInfo = "SELECT cargo_info FROM ORDER_SHEET WHERE order_id = ?";
            psmtOrderInfo = con.prepareStatement(sqlOrderInfo);
            psmtOrderInfo.setString(1, orderId);
            rs = psmtOrderInfo.executeQuery();
            if(rs.next()) {
                cargoInfo = rs.getString("cargo_info");
            }
            rs.close(); psmtOrderInfo.close();

            // 3. 무게 비교 로직
            int cargoWeight = 0;
            try {
                // "화물명 (500kg)" 형태에서 숫자만 추출
                Pattern p = Pattern.compile("\\((\\d+)kg\\)");
                Matcher m = p.matcher(cargoInfo);
                if(m.find()) {
                    cargoWeight = Integer.parseInt(m.group(1));
                }
            } catch(Exception e) {
                System.out.println("무게 파싱 실패, 0으로 처리");
            }

            if(cargoWeight > maxWeight) {
                return "차량 적재 용량 부족! (화물: " + cargoWeight + "kg / 내차: " + maxWeight + "kg)";
            }

            // 4. 배차 ID 생성
            String sqlSeq = "SELECT LAST_DATE, CURR_SEQ FROM SEQ_MANAGER WHERE TABLE_NAME = 'DISPATCH' FOR UPDATE";
            psmtSeq = con.prepareStatement(sqlSeq);
            rs = psmtSeq.executeQuery();
            String today = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            int nextSeq = 1;
            if(rs.next()) {
                if(today.equals(rs.getString("LAST_DATE"))) nextSeq = rs.getInt("CURR_SEQ") + 1;
            }
            String dispatchId = today + "-" + String.format("%04d", nextSeq);

            // 5. 시퀀스 업데이트
            String sqlUpdateSeq = "UPDATE SEQ_MANAGER SET LAST_DATE = ?, CURR_SEQ = ? WHERE TABLE_NAME = 'DISPATCH'";
            psmtUpdateSeq = con.prepareStatement(sqlUpdateSeq);
            psmtUpdateSeq.setString(1, today);
            psmtUpdateSeq.setInt(2, nextSeq);
            psmtUpdateSeq.executeUpdate();

            // 6. DISPATCH 테이블 Insert
            String sqlDispatch = "INSERT INTO DISPATCH (dispatch_id, order_id, driver_id, car_num, matched_at) VALUES (?, ?, ?, ?, SYSDATE)";
            psmtDispatch = con.prepareStatement(sqlDispatch);
            psmtDispatch.setString(1, dispatchId);
            psmtDispatch.setString(2, orderId);
            psmtDispatch.setString(3, driverId);
            psmtDispatch.setString(4, carNum);
            psmtDispatch.executeUpdate();

            // 7. ORDER_SHEET 상태 변경
            String sqlOrder = "UPDATE ORDER_SHEET SET status = '배차' WHERE order_id = ?";
            psmtOrder = con.prepareStatement(sqlOrder);
            psmtOrder.setString(1, orderId);
            psmtOrder.executeUpdate();

            con.commit(); // 커밋

        } catch(Exception e) {
            e.printStackTrace();
            try { if(con!=null) con.rollback(); } catch(Exception ex){}
            msg = "배차 처리 중 오류: " + e.getMessage();
        } finally {
            close(null, psmtOrder, null); close(null, psmtDispatch, null);
            close(null, psmtUpdateSeq, null); close(null, psmtSeq, null);
            close(con, psmtCheck, rs);
        }
        return msg;
    }

    private void close(Connection con, PreparedStatement psmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}