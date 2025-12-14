package deliveryservice.repository;

import deliveryservice.domain.OrderVO;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 주문 및 배차와 관련된 모든 데이터베이스 작업을 전담하는 클래스입니다.
// SQL 쿼리가 컨트롤러에 섞이면 코드가 너무 지저분해져서 DAO 패턴을 적용해 이곳으로 모았습니다.
// 단순히 넣고 빼는 것 외에도, 배차 수락 시 트랜잭션 처리 같은 복잡한 로직도 여기서 처리하도록 설계했습니다.
public class OrderRepository {
    ArrayList<OrderVO> orderVOList;

    // 1. 주문 조회
    // 사용자가 선택한 검색 조건(인덱스)에 따라 동적으로 쿼리를 구성해야 해서 고민을 좀 했습니다.
    // if-else문을 많이 쓰는 것보다 배열을 활용하여 컬럼명을 매핑하는 방식이 훨씬 깔끔해서 이렇게 구현했습니다.
    public ArrayList<OrderVO> select(String searchWord, int selectedIndex) {
        Connection con = JDBCConnector.getConnection();
        orderVOList = new ArrayList<OrderVO>();
        ResultSet rs = null;
        PreparedStatement psmt = null;
        String[] columnName = {"order_id", "origin", "dest"};
        // 날짜 포맷을 자바에서 변환하기 번거로워 오라클 함수인 TO_CHAR를 사용해 쿼리단에서 문자열로 가져왔습니다.
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
                vo.setPickupTime(rs.getString("time_str"));
                vo.setStatus(rs.getString("status"));
                orderVOList.add(vo);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return orderVOList;
    }

    // 2. 주문 등록
    // 단순히 DB 시퀀스를 쓰면 편하겠지만, 교수님께서 요구하신 '날짜-일련번호(YYYYMMDD-0001)' 형식을 맞추기 위해
    // 직접 로직을 짰습니다. 오늘 날짜로 된 가장 마지막 번호를 조회해서 +1 하는 방식입니다.
    public String insert(OrderVO vo) {
        Connection con = null;
        PreparedStatement psmtMax = null, psmtInsert = null;
        ResultSet rs = null;
        String resultMsg = "success";

        try {
            con = JDBCConnector.getConnection();
            String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String newId = today + "-0001"; // 오늘 첫 주문일 경우 기본값

            // 현재 날짜로 시작하는 ID 중 가장 큰 값을 찾아옵니다.
            String sqlMax = "SELECT MAX(order_id) FROM ORDER_SHEET WHERE order_id LIKE ?";
            psmtMax = con.prepareStatement(sqlMax);
            psmtMax.setString(1, today + "%");
            rs = psmtMax.executeQuery();

            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    // '-' 기준으로 뒤의 숫자만 잘라내서 1을 더한 뒤 다시 4자리 포맷으로 맞췄습니다.
                    String[] parts = maxId.split("-");
                    int nextSeq = Integer.parseInt(parts[1]) + 1;
                    newId = today + "-" + String.format("%04d", nextSeq);
                }
            }
            vo.setOrderId(newId);
            rs.close(); psmtMax.close();

            // 생성된 ID와 함께 데이터를 삽입합니다. 날짜는 TO_DATE 함수를 써서 DB 형식에 맞췄습니다.
            String sqlInsert = "INSERT INTO ORDER_SHEET (order_id, user_id, origin, dest, cargo_info, price, pickup_time, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI'), ?)";
            psmtInsert = con.prepareStatement(sqlInsert);
            psmtInsert.setString(1, vo.getOrderId());
            psmtInsert.setString(2, vo.getUserId());
            psmtInsert.setString(3, vo.getOrigin());
            psmtInsert.setString(4, vo.getDest());
            psmtInsert.setString(5, vo.getCargoInfo());
            psmtInsert.setInt(6, vo.getPrice());
            psmtInsert.setString(7, vo.getPickupTime());
            psmtInsert.setString(8, "대기"); // 초기 상태는 무조건 대기
            psmtInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return "DB 오류: " + e.getMessage();
        } finally { close(null, psmtInsert, null); close(con, psmtMax, rs); }
        return resultMsg;
    }

    // 3. 주문 수정
    // 기본키인 order_id를 조건으로 나머지 정보를 업데이트합니다.
    public int update(OrderVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "UPDATE ORDER_SHEET SET origin=?, dest=?, cargo_info=?, price=?, status=? WHERE order_id=?";
        PreparedStatement psmt = null;
        int result = 0;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, vo.getOrigin());
            psmt.setString(2, vo.getDest());
            psmt.setString(3, vo.getCargoInfo());
            psmt.setInt(4, vo.getPrice());
            psmt.setString(5, vo.getStatus());
            psmt.setString(6, vo.getOrderId());
            result = psmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(con, psmt, null); }
        return result;
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
        } catch (SQLException e) { e.printStackTrace(); } finally { close(con, psmt, null); }
    }

    // 5. 실시간 대기 주문 조회
    // 기사님 화면에는 이미 배차된 주문은 보이면 안 되므로, 상태가 '대기'인 것만 필터링해서 가져옵니다.
    public ArrayList<OrderVO> selectWaitingOrders() {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();
        String sql = "SELECT order_id, user_id, origin, dest, cargo_info, price, status, " +
                "TO_CHAR(pickup_time, 'YYYY-MM-DD HH24:MI') as time_str " +
                "FROM ORDER_SHEET WHERE status = '대기' ORDER BY order_id DESC";
        PreparedStatement psmt = null; ResultSet rs = null;
        try {
            psmt = con.prepareStatement(sql); rs = psmt.executeQuery();
            while(rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderId(rs.getString("order_id")); vo.setUserId(rs.getString("user_id"));
                vo.setOrigin(rs.getString("origin")); vo.setDest(rs.getString("dest"));
                vo.setCargoInfo(rs.getString("cargo_info")); vo.setPrice(rs.getInt("price"));
                vo.setPickupTime(rs.getString("time_str")); vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); } finally { close(con, psmt, rs); }
        return list;
    }

    // 6. 배차 내역 조회
    // 기사님이 자기가 수행한 내역을 봐야 하므로 ORDER_SHEET와 DISPATCH 테이블을 조인(JOIN)했습니다.
    // 쿼리가 좀 복잡해졌지만, 두 테이블의 정보를 한 번에 가져오기 위해 필수적이었습니다.
    public ArrayList<OrderVO> selectDispatchHistory(String driverId, String searchWord, int searchIndex) {
        Connection con = JDBCConnector.getConnection();
        ArrayList<OrderVO> list = new ArrayList<>();
        String[] dbCols = {"o.order_id", "o.origin", "o.dest"}; // 조인 시 테이블 별칭(Alias) 주의
        String targetCol = dbCols[searchIndex];
        String sql = "SELECT o.order_id, o.user_id, o.origin, o.dest, o.cargo_info, o.price, o.status, " +
                "TO_CHAR(o.pickup_time, 'YYYY-MM-DD HH24:MI') as time_str " +
                "FROM ORDER_SHEET o JOIN DISPATCH d ON o.order_id = d.order_id " +
                "WHERE d.driver_id = ? AND " + targetCol + " LIKE ? ORDER BY d.matched_at DESC";
        PreparedStatement psmt = null; ResultSet rs = null;
        try {
            psmt = con.prepareStatement(sql); psmt.setString(1, driverId); psmt.setString(2, "%" + searchWord + "%");
            rs = psmt.executeQuery();
            while(rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderId(rs.getString("order_id")); vo.setUserId(rs.getString("user_id"));
                vo.setOrigin(rs.getString("origin")); vo.setDest(rs.getString("dest"));
                vo.setCargoInfo(rs.getString("cargo_info")); vo.setPrice(rs.getInt("price"));
                vo.setPickupTime(rs.getString("time_str")); vo.setStatus(rs.getString("status"));
                list.add(vo);
            }
        } catch(Exception e) { e.printStackTrace(); } finally { close(con, psmt, rs); }
        return list;
    }

    // 7. 주문 수락
    // 이 프로젝트에서 가장 핵심적이면서 어려웠던 부분입니다.
    // 차량 적재량 비교, 배차 ID 생성, 배차 테이블 입력, 주문 상태 변경이 '모두 성공'하거나 '모두 실패'해야 합니다.
    // 그래서 AutoCommit을 끄고 트랜잭션(Transaction) 처리를 적용해 보았습니다.
    public String acceptOrder(String orderId, String driverId) {
        Connection con = null; PreparedStatement psmtCheck=null, psmtOrderInfo=null, psmtMax=null, psmtDispatch=null, psmtOrder=null;
        ResultSet rs = null; String msg = "success";
        try {
            con = JDBCConnector.getConnection(); con.setAutoCommit(false); // 트랜잭션 시작

            // 1. 기사님의 차량 정보를 먼저 확인합니다.
            String carNum = null; int maxWeight = 0;
            String sqlCheck = "SELECT car_num, max_weight FROM VEHICLE WHERE driver_id = ?";
            psmtCheck = con.prepareStatement(sqlCheck); psmtCheck.setString(1, driverId); rs = psmtCheck.executeQuery();
            if(rs.next()) { carNum = rs.getString("car_num"); maxWeight = rs.getInt("max_weight"); }
            else return "등록된 차량이 없습니다.";
            rs.close(); psmtCheck.close();

            // 2. 화물 정보를 가져와서 무게를 파악해야 하는데, 텍스트(예: "박스(500kg)")로 되어 있어서 난감했습니다.
            String cargoInfo = "";
            String sqlOrderInfo = "SELECT cargo_info FROM ORDER_SHEET WHERE order_id = ?";
            psmtOrderInfo = con.prepareStatement(sqlOrderInfo); psmtOrderInfo.setString(1, orderId); rs = psmtOrderInfo.executeQuery();
            if(rs.next()) cargoInfo = rs.getString("cargo_info");
            rs.close(); psmtOrderInfo.close();

            // 3. 정규표현식(Regex)을 공부해서 숫자만 추출해 적재량과 비교하는 로직을 구현했습니다.
            int cargoWeight = 0;
            try { Pattern p = Pattern.compile("\\((\\d+)kg\\)"); Matcher m = p.matcher(cargoInfo); if(m.find()) cargoWeight = Integer.parseInt(m.group(1)); } catch(Exception e) {}
            if(cargoWeight > maxWeight) return "차량 적재 용량 부족!";

            // 4. 배차 ID 생성 (주문 ID 생성 로직과 비슷하게 처리)
            String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String dispatchId = today + "-0001";
            String sqlMax = "SELECT MAX(dispatch_id) FROM DISPATCH WHERE dispatch_id LIKE ?";
            psmtMax = con.prepareStatement(sqlMax); psmtMax.setString(1, today + "%"); rs = psmtMax.executeQuery();
            if(rs.next() && rs.getString(1) != null) {
                String[] parts = rs.getString(1).split("-");
                dispatchId = today + "-" + String.format("%04d", Integer.parseInt(parts[1]) + 1);
            }
            rs.close(); psmtMax.close();

            // 5. 실제 데이터 삽입 및 상태 변경
            String sqlDispatch = "INSERT INTO DISPATCH (dispatch_id, order_id, driver_id, car_num, matched_at) VALUES (?, ?, ?, ?, SYSDATE)";
            psmtDispatch = con.prepareStatement(sqlDispatch); psmtDispatch.setString(1, dispatchId); psmtDispatch.setString(2, orderId); psmtDispatch.setString(3, driverId); psmtDispatch.setString(4, carNum); psmtDispatch.executeUpdate();

            String sqlOrder = "UPDATE ORDER_SHEET SET status = '배차' WHERE order_id = ?";
            psmtOrder = con.prepareStatement(sqlOrder); psmtOrder.setString(1, orderId); psmtOrder.executeUpdate();

            con.commit(); // 모든 과정이 문제 없으면 최종 저장
        } catch(Exception e) { e.printStackTrace(); try{if(con!=null)con.rollback();}catch(Exception ex){} msg = e.getMessage(); }
        finally { close(null, psmtOrder, null); close(null, psmtDispatch, null); close(con, psmtMax, rs); }
        return msg;
    }

    // 8. 배차 취소 (기사님용)
    // 배차를 취소하면 단순히 기록만 지우는 게 아니라, 주문 상태를 다시 '대기'로 돌려놔야
    // 다른 기사님이 해당 주문을 잡을 수 있습니다. 여기서도 데이터 일관성을 위해 트랜잭션을 사용했습니다.
    public void cancelDispatch(String orderId, String driverId) {
        Connection con = null;
        PreparedStatement psmtDelete = null, psmtUpdate = null;
        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // 1. 배차 내역 삭제
            String sqlDelete = "DELETE FROM DISPATCH WHERE order_id = ? AND driver_id = ?";
            psmtDelete = con.prepareStatement(sqlDelete);
            psmtDelete.setString(1, orderId);
            psmtDelete.setString(2, driverId);
            psmtDelete.executeUpdate();

            // 2. 주문 상태 복구 ('배차' -> '대기')
            String sqlUpdate = "UPDATE ORDER_SHEET SET status = '대기' WHERE order_id = ?";
            psmtUpdate = con.prepareStatement(sqlUpdate);
            psmtUpdate.setString(1, orderId);
            psmtUpdate.executeUpdate();

            con.commit(); // 둘 다 성공해야 커밋
        } catch(Exception e) {
            e.printStackTrace();
            try{if(con!=null)con.rollback();}catch(Exception ex){} // 에러 나면 롤백
        } finally {
            close(null, psmtUpdate, null);
            close(con, psmtDelete, null);
        }
    }

    private void close(Connection con, PreparedStatement psmt, ResultSet rs) { try { if(rs!=null)rs.close(); if(psmt!=null)psmt.close(); if(con!=null)con.close(); } catch(Exception e){} }
}