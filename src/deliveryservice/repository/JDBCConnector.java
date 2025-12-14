package deliveryservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 데이터베이스 연결을 전담하는 클래스입니다.
// DAO(Repository)마다 연결 코드를 매번 쓰는 게 너무 비효율적이고 코드 중복이 심해서,
// 이렇게 별도의 클래스로 분리하고 static 메소드로 만들어 어디서든 쉽게 호출할 수 있게 구현했습니다.
public class JDBCConnector {
    // 오라클 드라이버 클래스의 풀 네임입니다. 오타가 나면 드라이버를 못 찾으니 주의해야 합니다.
    private static final String DRIVER_PATH = "oracle.jdbc.driver.OracleDriver";
    // 로컬 개발 환경(localhost)에 맞춰진 접속 URL입니다.
    // 집에서 할 때랑 학교에서 할 때 포트나 SID(xe)가 달라서 연결이 안 될 때가 있어 변수로 뺐습니다.
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER_NAME = "c##dpem";
    private static final String PASSWORD = "1234";

    private static Connection con;

    // 실제로 DB와 연결된 Connection 객체를 반환해주는 핵심 메소드입니다.
    public static Connection getConnection() {
        try {
            // 메모리에 드라이버 클래스를 동적으로 로딩합니다.
            // ojdbc 라이브러리를 프로젝트에 추가하지 않으면 여기서 에러가 나서 많이 헤맸습니다.
            Class.forName(DRIVER_PATH);
            // 드라이버 매니저를 통해 실제 DB 인증을 거쳐 연결 객체를 얻어옵니다.
            con = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            // 드라이버 파일(jar)이 없거나 경로가 틀렸을 때 예외를 처리합니다.
            System.out.println("Driver not found");
        } catch (SQLException e) {
            // 아이디/비밀번호가 틀렸거나 DB 서버가 꺼져있을 때 에러 로그를 출력하도록 했습니다.
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return con;
    }
}