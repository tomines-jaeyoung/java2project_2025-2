package deliveryservice.repository;

import deliveryservice.domain.OrderVO;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderRepository {
    ArrayList<OrderVO> orderVOList;

    // 1. 주문 조회 (SELECT)
    public ArrayList<OrderVO> select(String searchWord, int selectedIndex) {
        Connection con = JDBCConnector.getConnection();
        orderVOList = new ArrayList<OrderVO>();
        ResultSet rs = null;
        PreparedStatement psmt = null;

        String[] columnName = {"order_id", "origin", "dest"};

        // ★ TO_CHAR를 사용하여 '년-월-일 시:분' 형태로 문자열 변환해서 가져옴
        String sql = "SELECT order_id, user_id, origin, dest, cargo_info, price, status, " +
                "TO_CHAR(pickup_time, 'YYYY-MM-DD HH24:MI') as time_str " +
                "FROM ORDER_SHEET WHERE " + columnName[selectedIndex] + " LIKE ? ORDER BY order_id DESC";

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
                // ★ 문자열로 된 시간 저장
                vo.setPickupTime(rs.getString("time_str"));
                vo.setStatus(rs.getString("status"));
                orderVOList.add(vo);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return orderVOList;
    }

    // 2. 주문 등록 (INSERT)
    public void insert(OrderVO vo) {
        Connection con = null;
        PreparedStatement psmtSeq = null, psmtUpdateSeq = null, psmtInsert = null;
        ResultSet rs = null;

        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false);

            // A. 시퀀스 처리
            String sqlSeq = "SELECT LAST_DATE, CURR_SEQ FROM SEQ_MANAGER WHERE TABLE_NAME = 'ORDER_SHEET' FOR UPDATE";
            psmtSeq = con.prepareStatement(sqlSeq);
            rs = psmtSeq.executeQuery();
            String today = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            int nextSeq = 1;
            if (rs.next()) {
                if (today.equals(rs.getString("LAST_DATE"))) nextSeq = rs.getInt("CURR_SEQ") + 1;
            }
            String newId = today + "-" + String.format("%04d", nextSeq);
            vo.setOrderId(newId);

            String sqlUpdateSeq = "UPDATE SEQ_MANAGER SET LAST_DATE = ?, CURR_SEQ = ? WHERE TABLE_NAME = 'ORDER_SHEET'";
            psmtUpdateSeq = con.prepareStatement(sqlUpdateSeq);
            psmtUpdateSeq.setString(1, today);
            psmtUpdateSeq.setInt(2, nextSeq);
            psmtUpdateSeq.executeUpdate();

            // B. 데이터 INSERT (★ TO_DATE 사용)
            // 고객이 입력한 "2025-12-14 14:00" 문자열을 DB 날짜형으로 변환해 저장
            String sqlInsert = "INSERT INTO ORDER_SHEET (order_id, user_id, origin, dest, cargo_info, price, pickup_time, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI'), ?)";

            psmtInsert = con.prepareStatement(sqlInsert);
            psmtInsert.setString(1, vo.getOrderId());
            psmtInsert.setString(2, vo.getUserId());
            psmtInsert.setString(3, vo.getOrigin());
            psmtInsert.setString(4, vo.getDest());
            psmtInsert.setString(5, vo.getCargoInfo());
            psmtInsert.setInt(6, vo.getPrice());
            // ★ VO에 저장된 시간 문자열을 넣음 (SYSDATE 아님!)
            psmtInsert.setString(7, vo.getPickupTime());
            psmtInsert.setString(8, "대기");

            psmtInsert.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try { if(con != null) con.rollback(); } catch(Exception ex) {}
        } finally {
            close(null, psmtInsert, null); close(null, psmtUpdateSeq, null); close(con, psmtSeq, rs);
        }
    }

    // 5. 실시간 대기 주문 조회 (SELECT)
    public ArrayList<OrderVO> selectWaitingOrders() {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();
        // ★ TO_CHAR 추가
        String sql = "SELECT order_id, user_id, origin, dest, cargo_info, price, status, " +
                "TO_CHAR(pickup_time, 'YYYY-MM-DD HH24:MI') as time_str " +
                "FROM ORDER_SHEET WHERE status = '대기' ORDER BY order_id DESC";
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
                // ★ 시간 문자열 세팅
                vo.setPickupTime(rs.getString("time_str"));
                vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return list;
    }

    // 6. 배차 내역 조회 (SELECT)
    public ArrayList<OrderVO> selectDispatchHistory(String driverId, String searchWord, int searchIndex) {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();
        String[] dbCols = {"o.order_id", "o.origin", "o.dest"};
        String targetCol = dbCols[searchIndex];

        // ★ TO_CHAR 추가
        String sql = "SELECT o.order_id, o.user_id, o.origin, o.dest, o.cargo_info, o.price, o.status, " +
                "TO_CHAR(o.pickup_time, 'YYYY-MM-DD HH24:MI') as time_str " +
                "FROM ORDER_SHEET o JOIN DISPATCH d ON o.order_id = d.order_id " +
                "WHERE d.driver_id = ? AND " + targetCol + " LIKE ? ORDER BY d.matched_at DESC";

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
                // ★ 시간 문자열 세팅
                vo.setPickupTime(rs.getString("time_str"));
                vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return list;
    }

    // [중요] 나머지 acceptOrder, update, delete는 기존 코드를 그대로 유지하세요.
    // acceptOrder는 아까 드린 로직(무게 체크 포함) 그대로 쓰시면 됩니다.
    // 단, 이 파일에 acceptOrder 메서드도 반드시 포함되어 있어야 합니다!

    // (편의를 위해 acceptOrder만 다시 붙여드립니다. 기존 것과 동일함)
    public String acceptOrder(String orderId, String driverId) {
        Connection con = null;
        PreparedStatement psmtCheck = null, psmtOrderInfo = null;
        PreparedStatement psmtSeq = null, psmtUpdateSeq = null, psmtDispatch = null, psmtOrder = null;
        ResultSet rs = null;
        String msg = "success";
        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false);

            String carNum = null; int maxWeight = 0;
            String sqlCheck = "SELECT car_num, max_weight FROM VEHICLE WHERE driver_id = ?";
            psmtCheck = con.prepareStatement(sqlCheck); psmtCheck.setString(1, driverId);
            rs = psmtCheck.executeQuery();
            if(rs.next()) { carNum = rs.getString("car_num"); maxWeight = rs.getInt("max_weight"); }
            else return "등록된 차량이 없습니다.";
            rs.close(); psmtCheck.close();

            String cargoInfo = "";
            String sqlOrderInfo = "SELECT cargo_info FROM ORDER_SHEET WHERE order_id = ?";
            psmtOrderInfo = con.prepareStatement(sqlOrderInfo); psmtOrderInfo.setString(1, orderId);
            rs = psmtOrderInfo.executeQuery();
            if(rs.next()) cargoInfo = rs.getString("cargo_info");
            rs.close(); psmtOrderInfo.close();

            int cargoWeight = 0;
            try {
                Pattern p = Pattern.compile("\\((\\d+)kg\\)"); Matcher m = p.matcher(cargoInfo);
                if(m.find()) cargoWeight = Integer.parseInt(m.group(1));
            } catch(Exception e) {}

            if(cargoWeight > maxWeight) return "차량 적재 용량 부족! (" + cargoWeight + "kg > " + maxWeight + "kg)";

            String sqlSeq = "SELECT LAST_DATE, CURR_SEQ FROM SEQ_MANAGER WHERE TABLE_NAME = 'DISPATCH' FOR UPDATE";
            psmtSeq = con.prepareStatement(sqlSeq); rs = psmtSeq.executeQuery();
            String today = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            int nextSeq = 1;
            if(rs.next() && today.equals(rs.getString("LAST_DATE"))) nextSeq = rs.getInt("CURR_SEQ") + 1;
            String dispatchId = today + "-" + String.format("%04d", nextSeq);

            String sqlUpdateSeq = "UPDATE SEQ_MANAGER SET LAST_DATE = ?, CURR_SEQ = ? WHERE TABLE_NAME = 'DISPATCH'";
            psmtUpdateSeq = con.prepareStatement(sqlUpdateSeq); psmtUpdateSeq.setString(1, today); psmtUpdateSeq.setInt(2, nextSeq); psmtUpdateSeq.executeUpdate();

            String sqlDispatch = "INSERT INTO DISPATCH (dispatch_id, order_id, driver_id, car_num, matched_at) VALUES (?, ?, ?, ?, SYSDATE)";
            psmtDispatch = con.prepareStatement(sqlDispatch);
            psmtDispatch.setString(1, dispatchId); psmtDispatch.setString(2, orderId); psmtDispatch.setString(3, driverId); psmtDispatch.setString(4, carNum); psmtDispatch.executeUpdate();

            String sqlOrder = "UPDATE ORDER_SHEET SET status = '배차' WHERE order_id = ?";
            psmtOrder = con.prepareStatement(sqlOrder); psmtOrder.setString(1, orderId); psmtOrder.executeUpdate();

            con.commit();
        } catch(Exception e) {
            e.printStackTrace(); try{if(con!=null)con.rollback();}catch(Exception ex){} msg = e.getMessage();
        } finally {
            close(null, psmtOrder, null); close(null, psmtDispatch, null); close(null, psmtUpdateSeq, null); close(null, psmtSeq, null); close(con, psmtCheck, rs);
        }
        return msg;
    }

    // update, delete 메서드도 기존과 동일하게 유지 (VO 타입이 String으로 바뀌었으니 setString 사용)
    public void update(OrderVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "UPDATE ORDER_SHEET SET origin=?, dest=?, cargo_info=?, price=?, status=? WHERE order_id=?";
        PreparedStatement psmt = null;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, vo.getOrigin()); psmt.setString(2, vo.getDest()); psmt.setString(3, vo.getCargoInfo());
            psmt.setInt(4, vo.getPrice()); psmt.setString(5, vo.getStatus()); psmt.setString(6, vo.getOrderId());
            psmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(con, psmt, null); }
    }
    public void delete(OrderVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "DELETE FROM ORDER_SHEET WHERE order_id=?";
        PreparedStatement psmt = null;
        try {
            psmt = con.prepareStatement(sql); psmt.setString(1, vo.getOrderId()); psmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(con, psmt, null); }
    }
    private void close(Connection con, PreparedStatement psmt, ResultSet rs) {
        try { if(rs!=null)rs.close(); if(psmt!=null)psmt.close(); if(con!=null)con.close(); } catch(Exception e){}
    }
}