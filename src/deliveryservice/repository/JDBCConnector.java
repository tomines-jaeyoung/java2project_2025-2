package deliveryservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {
    private static final String DRIVER_PATH = "oracle.jdbc.driver.OracleDriver";
    // ★★★ 본인 오라클 설정에 맞게 수정 필수 ★★★
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER_NAME = "c##dpem";
    private static final String PASSWORD = "1234";

    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER_PATH);
            con = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        } catch (SQLException e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
        return con;
    }
}