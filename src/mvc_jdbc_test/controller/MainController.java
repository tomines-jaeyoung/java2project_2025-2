package mvc_jdbc_test.controller;

import jdbc_test.JDBCConnector;
import mvc_jdbc_test.entity.Customer;
import mvc_jdbc_test.view.InputCustomerInfoView;
import mvc_jdbc_test.view.CustomerView; // ★ 1. CustomerView 임포트

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class MainController {

    // Controller가 View들을 관리
    private InputCustomerInfoView inputView;
    private CustomerView customerView; // ★ 2. CustomerView 변수 선언
    private Scanner scanner;
    private Connection con;

    // 생성자: 필요한 객체들을 초기화
    public MainController() {
        this.inputView = new InputCustomerInfoView();
        this.customerView = new CustomerView(); // ★ 3. CustomerView 객체 생성
        this.scanner = new Scanner(System.in);
        this.con = JDBCConnector.getConnection();
    }

    /**
     * 프로그램의 메인 로직을 실행하는 메소드
     */
    public void run() {

        while (true) {
            // 1. 사용자 입력 (View 호출)
            Customer customer = inputView.inputCustomerInfo(scanner);

            // 2. 입력된 내용 출력 (CustomerView 사용) // ★ 4. 이 부분이 변경됨
            System.out.println("\n--- [입력 확인] ---");
            customerView.printHead();
            customerView.printCustomer(customer); // 입력받은 customer 객체를 전달
            customerView.printFooter();
            System.out.println("--------------------");


            // 3. DB Insert
            String sql = "INSERT INTO 고객 (고객아이디, 고객이름, 나이, 등급, 직업, 적립금) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, customer.getCustomerid());
                ps.setString(2, customer.getCustomername());
                ps.setInt(3, customer.getAge());
                ps.setString(4, customer.getLevel());
                ps.setString(5, customer.getJob());
                ps.setInt(6, customer.getReward());

                int affectedRows = ps.executeUpdate();

                // 4. 추가 완료 (결과 피드백)
                if (affectedRows > 0) {
                    System.out.println("[성공] " + customer.getCustomername() + " 님의 정보가 DB에 저장되었습니다.");
                } else {
                    System.out.println("[실패] DB 저장에 실패했습니다.");
                }

            } catch (SQLException e) {
                System.out.println("[오류] 데이터베이스 저장 중 오류가 발생했습니다.");
                e.printStackTrace();
            }

            // 5. 추가 여부 (c=계속, e=종료)
            System.out.print("\n계속 추가하시겠습니까? (c: 계속, e: 종료): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("e")) {
                System.out.println("프로그램을 종료합니다.");
                break; // while 루프 탈출
            }
        }

        // --- 루프 종료 후 자원 정리 ---
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    /**
     * 프로그램 시작점
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.run();
    }
}



















































//package mvc_jdbc_test.controller;
//
//import jdbc_test.JDBCConnector;
//import mvc_jdbc_test.entity.Customer;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class MainController {
//    public static void main(String[] args) {
//        Connection con = JDBCConnector.getConnection();
//        ArrayList<Customer> customerList = new ArrayList<Customer>();
//
//        try {
//            String sql = "select 고객이름,고객아이디,배송지,수량, 주문일자, 제품명 from 고객, 주문, 제품 where 주문.주문고객 = 고객.고객아이디 and 주문.주문제품 = 제품.제품번호";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//
//            while (rs.next()) {
//                String name = rs.getString("고객이름");
//                String id = rs.getString("고객아이디");
//                String address = rs.getString("배송지");
//                int quantity = rs.getInt("수량");
//                String orderDate = rs.getString("주문일자");
//                String productName = rs.getString("제품명");
//
//                System.out.println("고객이름: " + name + ", 고객아이디: " + id + ", 배송지: " + address
//                        + ", 수량: " + quantity + ", 주문일자: " + orderDate + ", 제품명: " + productName);
//
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Statement or SQL Error");
//        }
//    }
//}
