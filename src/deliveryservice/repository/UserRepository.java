package deliveryservice.repository;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import java.sql.*;

public class UserRepository {

    // 1. 로그인 (차량 정보 조회 포함)
    public UserVO getUser(String id, String pw) {
        Connection con = JDBCConnector.getConnection();
        // USERS 테이블과 VEHICLE 테이블을 조인하여 정보 가져오기
        String sql = "SELECT u.*, v.car_num, v.car_type, v.max_weight " +
                "FROM USERS u LEFT JOIN VEHICLE v ON u.user_id = v.driver_id " +
                "WHERE u.user_id = ? AND u.password = ?";
        PreparedStatement psmt = null;
        ResultSet rs = null;
        UserVO user = null;

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, id);
            psmt.setString(2, pw);
            rs = psmt.executeQuery();

            if (rs.next()) {
                user = new UserVO();
                user.setUserId(rs.getString("user_id"));
                user.setPassword(rs.getString("password"));
                user.setUserName(rs.getString("user_name"));
                user.setPhone(rs.getString("phone"));
                user.setUserType(rs.getString("user_type"));

                // 차량 정보가 존재하면 VO에 담기
                String carNum = rs.getString("car_num");
                if(carNum != null) {
                    VehicleVO v = new VehicleVO();
                    v.setCarNum(carNum);
                    v.setCarType(rs.getString("car_type"));
                    v.setMaxWeight(rs.getInt("max_weight"));
                    v.setDriverId(user.getUserId());
                    user.setVehicle(v);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { close(con, psmt, rs); }
        return user;
    }

    // 2. 회원가입 (트랜잭션)
    public int registerUser(UserVO vo) {
        Connection con = JDBCConnector.getConnection();
        PreparedStatement psmtUser = null;
        PreparedStatement psmtVehicle = null;
        int result = 0;

        try {
            con.setAutoCommit(false); // 트랜잭션 시작

            // A. 사용자 등록
            String sqlUser = "INSERT INTO USERS (user_id, password, user_name, phone, user_type) VALUES (?, ?, ?, ?, ?)";
            psmtUser = con.prepareStatement(sqlUser);
            psmtUser.setString(1, vo.getUserId());
            psmtUser.setString(2, vo.getPassword());
            psmtUser.setString(3, vo.getUserName());
            psmtUser.setString(4, vo.getPhone());
            psmtUser.setString(5, vo.getUserType());
            result = psmtUser.executeUpdate();

            // B. 차량 등록 (기사이고 차량정보가 있을 경우)
            if("기사".equals(vo.getUserType()) && vo.getVehicle() != null) {
                String sqlVehicle = "INSERT INTO VEHICLE (car_num, car_type, max_weight, driver_id) VALUES (?, ?, ?, ?)";
                psmtVehicle = con.prepareStatement(sqlVehicle);
                psmtVehicle.setString(1, vo.getVehicle().getCarNum());
                psmtVehicle.setString(2, vo.getVehicle().getCarType());
                psmtVehicle.setInt(3, vo.getVehicle().getMaxWeight());
                psmtVehicle.setString(4, vo.getUserId());
                psmtVehicle.executeUpdate();
            }

            con.commit(); // 커밋
        } catch (SQLException e) {
            System.out.println("가입 실패: " + e.getMessage());
            try { if(con!=null) con.rollback(); } catch(Exception ex){}
            result = 0;
        } finally {
            close(null, psmtVehicle, null);
            close(con, psmtUser, null);
        }
        return result;
    }

    // 3. 정보 수정 (★ 여기가 수정되었습니다: 없으면 INSERT, 있으면 UPDATE)
    public int updateUser(UserVO vo) {
        Connection con = JDBCConnector.getConnection();
        PreparedStatement psmtUser = null;
        PreparedStatement psmtVehicle = null;
        int result = 0;

        try {
            con.setAutoCommit(false);

            // A. 사용자 정보 수정
            String sqlUser = "UPDATE USERS SET password=?, user_name=?, phone=? WHERE user_id=?";
            psmtUser = con.prepareStatement(sqlUser);
            psmtUser.setString(1, vo.getPassword());
            psmtUser.setString(2, vo.getUserName());
            psmtUser.setString(3, vo.getPhone());
            psmtUser.setString(4, vo.getUserId());
            result = psmtUser.executeUpdate();

            // B. 차량 정보 처리 (기사일 경우)
            if("기사".equals(vo.getUserType()) && vo.getVehicle() != null) {

                // 1단계: 먼저 UPDATE 시도 (기존 차량 정보가 있는지 확인)
                String sqlUpdate = "UPDATE VEHICLE SET car_num=?, car_type=?, max_weight=? WHERE driver_id=?";
                psmtVehicle = con.prepareStatement(sqlUpdate);
                psmtVehicle.setString(1, vo.getVehicle().getCarNum());
                psmtVehicle.setString(2, vo.getVehicle().getCarType());
                psmtVehicle.setInt(3, vo.getVehicle().getMaxWeight());
                psmtVehicle.setString(4, vo.getUserId());

                int updateCount = psmtVehicle.executeUpdate();

                // 2단계: 수정된 줄이 0개라면? (차량이 없었다는 뜻) -> INSERT 실행
                if(updateCount == 0) {
                    // 이전 PreparedStatement 닫기
                    if(psmtVehicle != null) psmtVehicle.close();

                    String sqlInsert = "INSERT INTO VEHICLE (car_num, car_type, max_weight, driver_id) VALUES (?, ?, ?, ?)";
                    psmtVehicle = con.prepareStatement(sqlInsert);
                    psmtVehicle.setString(1, vo.getVehicle().getCarNum());
                    psmtVehicle.setString(2, vo.getVehicle().getCarType());
                    psmtVehicle.setInt(3, vo.getVehicle().getMaxWeight());
                    psmtVehicle.setString(4, vo.getUserId());
                    psmtVehicle.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { if(con!=null) con.rollback(); } catch(Exception ex){}
        } finally {
            close(null, psmtVehicle, null);
            close(con, psmtUser, null);
        }
        return result;
    }

    private void close(Connection con, PreparedStatement psmt, ResultSet rs) {
        try {
            if(rs!=null) rs.close(); if(psmt!=null) psmt.close(); if(con!=null) con.close();
        } catch(Exception e){}
    }
}