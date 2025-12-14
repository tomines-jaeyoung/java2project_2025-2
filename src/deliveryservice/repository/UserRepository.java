package deliveryservice.repository;

import deliveryservice.domain.UserVO;
import deliveryservice.domain.VehicleVO;
import java.sql.*;

// 회원 로그인, 가입, 정보 수정을 담당하는 리포지토리 클래스입니다.
// 처음에는 사용자 테이블과 차량 테이블이 나뉘어 있어서 리포지토리를 따로 만들까 했는데,
// 로그인할 때 두 정보를 한 번에 가져와야 해서 여기서 통합 관리하도록 설계했습니다.
public class UserRepository {

    // 1. 로그인 (차량 정보 조회 포함)
    // 일반 회원과 기사 회원의 로그인 처리를 어떻게 통합할지 고민하다가,
    // LEFT JOIN을 사용하면 기사가 아닌 사람은 차량 정보만 NULL로 나오고 에러는 안 나기 때문에 이 방식을 채택했습니다.
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
                // 조인 결과에서 차량 관련 컬럼이 있으면 객체를 생성해서 넣어줍니다.
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
    // 회원 정보는 들어갔는데 차량 정보 입력에서 에러가 나면 데이터가 꼬이는 문제가 있었습니다.
    // 그래서 AutoCommit을 끄고 두 개의 INSERT문이 모두 성공했을 때만 커밋되도록 트랜잭션을 적용했습니다.
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
            // 일반 고객은 차량 정보가 null이므로 이 부분은 건너뛰게 됩니다.
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
            try { if(con!=null) con.rollback(); } catch(Exception ex){} // 에러 발생 시 롤백
            result = 0;
        } finally {
            close(null, psmtVehicle, null);
            close(con, psmtUser, null);
        }
        return result;
    }

    // 3. 정보 수정 (수정됨: 없으면 INSERT, 있으면 UPDATE)
    // 정보 수정 로직을 짤 때 가장 애먹었던 부분입니다.
    // 기존에 차량이 없던 기사님이 나중에 차량을 등록할 수도 있고, 기존 차량 정보를 수정할 수도 있습니다.
    // 그래서 무조건 UPDATE만 하면 안 되고, 상황에 따라 INSERT를 해야 해서 로직을 분기했습니다.
    public int updateUser(UserVO vo) {
        Connection con = JDBCConnector.getConnection();
        PreparedStatement psmtUser = null;
        PreparedStatement psmtVehicle = null;
        int result = 0;

        try {
            con.setAutoCommit(false);

            // A. 사용자 정보 수정 (비밀번호, 이름, 전화번호 변경)
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
                // MERGE INTO 구문을 쓰면 한 방에 된다고 들었는데, 아직 오라클 문법이 익숙지 않아서 자바 로직으로 처리했습니다.
                String sqlUpdate = "UPDATE VEHICLE SET car_num=?, car_type=?, max_weight=? WHERE driver_id=?";
                psmtVehicle = con.prepareStatement(sqlUpdate);
                psmtVehicle.setString(1, vo.getVehicle().getCarNum());
                psmtVehicle.setString(2, vo.getVehicle().getCarType());
                psmtVehicle.setInt(3, vo.getVehicle().getMaxWeight());
                psmtVehicle.setString(4, vo.getUserId());

                int updateCount = psmtVehicle.executeUpdate();

                // 2단계: 수정된 줄이 0개라면? (차량이 없었다는 뜻) -> INSERT 실행
                // executeUpdate의 리턴값이 '변경된 행의 개수'라는 점을 이용해서 분기 처리했습니다.
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