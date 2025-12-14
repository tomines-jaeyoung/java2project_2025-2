package deliveryservice.test;

import deliveryservice.domain.OrderVO;
import deliveryservice.repository.OrderRepository;
import java.util.ArrayList;

// GUI 화면을 만들기 전에, 데이터베이스 연결과 SQL 쿼리가 제대로 동작하는지 검증하기 위해 만든 테스트 클래스입니다.
// 화면 개발 단계에서 에러가 나면 UI 문제인지 DB 문제인지 파악하기가 어려워서,
// 이렇게 콘솔에서 먼저 핵심 로직(Repository)을 단위 테스트하는 과정을 거쳤습니다.
public class ArrayListTest {
    public static void main(String[] args) {
        // 테스트할 리포지토리 객체를 직접 생성합니다.
        OrderRepository repo = new OrderRepository();
        System.out.println("DB 연결 테스트 시작...");

        // 검색 조건 없이("") 전체 데이터를 조회하여 ArrayList에 담습니다.
        // 리포지토리의 select 메소드가 정상적으로 리스트를 반환하는지 확인하는 단계입니다.
        ArrayList<OrderVO> list = repo.select("", 0);

        // 데이터가 아예 없는 경우와 있는 경우를 나누어 출력하도록 했습니다.
        // 이렇게 하면 연결은 성공했으나 데이터가 없는 상태인지 명확히 알 수 있습니다.
        if(list.isEmpty()) {
            System.out.println("데이터가 없습니다. (GUI로 등록해보세요)");
        } else {
            // 향상된 for문을 사용하여 리스트 내부의 데이터를 순회합니다.
            // OrderVO 객체에 DB 데이터가 올바르게 매핑되었는지 주문번호와 ID를 찍어서 확인했습니다.
            for(OrderVO vo : list) {
                System.out.println("주문번호: " + vo.getOrderId() + " | 고객: " + vo.getUserId());
            }
        }
    }
}