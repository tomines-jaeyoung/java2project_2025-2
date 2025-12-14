package deliveryservice.domain;

// 데이터베이스의 주문(Order) 테이블과 1:1로 매핑되는 Value Object(VO) 클래스입니다.
// 처음에는 데이터를 그냥 변수로 주고받을까 했는데, 객체지향적으로 관리하고
// 데이터의 무결성을 지키려면 이렇게 VO를 만들어서 캡슐화하는 게 정석이라고 해서 작성했습니다.
public class OrderVO {
    // DB 컬럼명과 변수명을 통일시켜서 나중에 매핑할 때 헷갈리지 않도록 설계했습니다.
    private String orderId;
    private String userId;
    private String origin;
    private String dest;
    private String cargoInfo;
    private int price;

    // 원래는 Date 타입을 썼었는데, 포맷팅 과정에서 자꾸 에러가 나고 시간 처리가 복잡해서
    // 직관적으로 날짜와 시간을 다루기 위해 과감하게 String 타입으로 변경하여 해결했습니다.
    private String pickupTime;

    private String status;

    // Getter & Setter 메소드
    // 외부에서 private 변수에 직접 접근하지 못하게 하고, 메소드를 통해서만 값을 수정/조회하도록 구현했습니다.
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDest() { return dest; }
    public void setDest(String dest) { this.dest = dest; }

    public String getCargoInfo() { return cargoInfo; }
    public void setCargoInfo(String cargoInfo) { this.cargoInfo = cargoInfo; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    // 앞서 고민했던 날짜 필드입니다.
    // String으로 변경하니까 DB에 넣거나 화면에 출력할 때 형변환 걱정이 없어서 훨씬 효율적이었습니다.
    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}