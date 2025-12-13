package deliveryservice.test;

import deliveryservice.domain.OrderVO;
import deliveryservice.repository.OrderRepository;
import java.util.ArrayList;

public class ArrayListTest {
    public static void main(String[] args) {
        OrderRepository repo = new OrderRepository();
        System.out.println("DB 연결 테스트 시작...");

        ArrayList<OrderVO> list = repo.select("", 0);
        if(list.isEmpty()) {
            System.out.println("데이터가 없습니다. (GUI로 등록해보세요)");
        } else {
            for(OrderVO vo : list) {
                System.out.println("주문번호: " + vo.getOrderId() + " | 고객: " + vo.getUserId());
            }
        }
    }
}