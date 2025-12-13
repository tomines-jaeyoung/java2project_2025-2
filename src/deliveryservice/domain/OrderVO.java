package deliveryservice.domain;

import java.sql.Date;

public class OrderVO {
    private String orderId;     // 주문번호 (PK)
    private String userId;      // 고객ID (FK)
    private String origin;      // 출발지
    private String dest;        // 도착지
    private String cargoInfo;   // 화물정보
    private int price;          // 운임
    private Date pickupTime;    // 희망상차시간
    private String status;      // 상태 (대기/배차/완료)

    // Getter & Setter
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

    public Date getPickupTime() { return pickupTime; }
    public void setPickupTime(Date pickupTime) { this.pickupTime = pickupTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}