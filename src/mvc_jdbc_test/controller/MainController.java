package mvc_jdbc_test.controller;

import jdbc_test.JDBCConnector;
import mvc_jdbc_test.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    public static void main(String[] args) {
        Connection con = JDBCConnector.getConnection();
        ArrayList<Customer> customerList = new ArrayList<Customer>();

        try {
            String sql = "select 고객이름,고객아이디,배송지,수량, 주문일자, 제품명 from 고객, 주문, 제품 where 주문.주문고객 = 고객.고객아이디 and 주문.주문제품 = 제품.제품번호";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                String name = rs.getString("고객이름");
                String id = rs.getString("고객아이디");
                String address = rs.getString("배송지");
                int quantity = rs.getInt("수량");
                String orderDate = rs.getString("주문일자");
                String productName = rs.getString("제품명");

                System.out.println("고객이름: " + name + ", 고객아이디: " + id + ", 배송지: " + address
                        + ", 수량: " + quantity + ", 주문일자: " + orderDate + ", 제품명: " + productName);

            }

        } catch (SQLException e) {
            System.out.println("Statement or SQL Error");
        }
    }
}
