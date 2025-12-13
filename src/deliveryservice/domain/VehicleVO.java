package deliveryservice.domain;

public class VehicleVO {
    private String carNum;      // 차량번호 (PK)
    private String carType;     // 차종 (카고, 윙바디 등)
    private int maxWeight;      // 최대 적재량 (kg)
    private String driverId;    // 기사 ID (FK)

    // 생성자
    public VehicleVO() {}
    public VehicleVO(String carNum, String carType, int maxWeight, String driverId) {
        this.carNum = carNum;
        this.carType = carType;
        this.maxWeight = maxWeight;
        this.driverId = driverId;
    }

    // Getter & Setter
    public String getCarNum() { return carNum; }
    public void setCarNum(String carNum) { this.carNum = carNum; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public int getMaxWeight() { return maxWeight; }
    public void setMaxWeight(int maxWeight) { this.maxWeight = maxWeight; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
}