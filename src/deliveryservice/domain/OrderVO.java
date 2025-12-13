package deliveryservice.domain;

public class OrderVO {
    private String orderId;
    private String userId;
    private String origin;
    private String dest;
    private String cargoInfo;
    private int price;

    // ★ 수정됨: 날짜+시간을 완벽하게 표현하기 위해 String으로 변경
    private String pickupTime;

    private String status;

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

    // ★ 날짜 타입 변경 (Date -> String)
    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}