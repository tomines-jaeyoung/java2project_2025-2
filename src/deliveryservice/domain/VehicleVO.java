package deliveryservice.domain;

// 기사님들의 차량 정보를 별도로 관리하기 위해 만든 Value Object 클래스입니다.
// 처음에는 UserVO에 몽땅 다 넣으려고 했는데, 데이터베이스 설계를 하다 보니
// 차량 정보는 별도의 테이블로 정규화하는 게 맞다고 판단되어 클래스도 분리했습니다.
public class VehicleVO {
    private String carNum;      // 차량번호는 중복될 수 없는 고유한 값이므로 기본키(PK)로 설정했습니다.
    private String carType;     // 차종 (카고, 윙바디 등 문자열로 저장합니다)
    private int maxWeight;      // 최대 적재량입니다. 톤 단위 소수점으로 할까 하다가 계산 오류를 줄이기 위해 kg 단위 정수로 통일했습니다.
    private String driverId;    // 이 차량이 누구의 소유인지 연결하기 위해 기사님의 ID를 외래키(FK)로 가집니다.

    // 생성자
    // 기본 생성자는 자바 빈(Java Bean) 규약이나 나중에 라이브러리 쓸 때 필요할 것 같아서 습관적으로 만들어 두었습니다.
    public VehicleVO() {}

    // 객체 생성과 동시에 데이터를 초기화할 때 편의성을 위해 만든 생성자입니다.
    public VehicleVO(String carNum, String carType, int maxWeight, String driverId) {
        this.carNum = carNum;
        this.carType = carType;
        this.maxWeight = maxWeight;
        this.driverId = driverId;
    }

    // Getter & Setter
    // 정보은닉(Encapsulation) 원칙을 지키기 위해 필드는 private으로 막고 메소드로만 접근하도록 구현했습니다.
    public String getCarNum() { return carNum; }
    public void setCarNum(String carNum) { this.carNum = carNum; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public int getMaxWeight() { return maxWeight; }
    public void setMaxWeight(int maxWeight) { this.maxWeight = maxWeight; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
}