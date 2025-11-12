package mvc_jdbc_test.view;

import mvc_jdbc_test.entity.Customer;
import java.util.Scanner; // import는 그대로 둡니다.

public class InputCustomerInfoView {


    public Customer inputCustomerInfo(Scanner s) {



        Customer customer = new Customer();

        System.out.println("--- 고객 정보 입력을 시작합니다 ---\n");


        System.out.print("고객 ID 입력: ");
        String customerId = s.nextLine();


        System.out.print("고객 이름 입력: ");
        String customerName = s.nextLine();


        System.out.print("고객 나이 입력: ");
        int customerAge = s.nextInt();
        s.nextLine();


        System.out.print("고객 등급 입력: ");
        String customerLevel = s.nextLine();


        System.out.print("고객 직업 입력: ");
        String customerJob = s.nextLine();


        System.out.print("고객 적립금 입력: ");
        int customerReward = s.nextInt();
        s.nextLine();

        customer.setCustomerid(customerId);
        customer.setCustomername(customerName);
        customer.setAge(customerAge);
        customer.setLevel(customerLevel);
        customer.setJob(customerJob);
        customer.setReward(customerReward);



        return customer;
    }
}