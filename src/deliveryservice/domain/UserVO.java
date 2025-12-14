package deliveryservice.domain;

// 회원 정보를 담는 VO(Value Object) 클래스입니다.
// 처음에는 고객(Customer)과 기사(Driver) 클래스를 상속으로 분리할까 고민했었는데,
// 로그인 처리나 공통 정보를 다룰 때 하나의 객체로 관리하는 게 훨씬 효율적일 것 같아 통합했습니다.
public class UserVO {
    private String userId;
    private String password;
    private String userName;
    private String phone;
    private String userType; // '고객'인지 '기사'인지 구분하는 필드입니다.

    // 기사님일 경우에만 필요한 차량 정보를 저장하기 위해 VehicleVO 객체를 필드로 포함시켰습니다.
    // 1:1 관계를 객체 내의 컴포지션(Composition)으로 표현하여 구조를 단순화했습니다.
    private VehicleVO vehicle;

    // 기본 생성자: MyBatis나 프레임워크에서 객체를 생성할 때 필요하다고 배워서 명시해 두었습니다.
    public UserVO() {}

    // 필수 정보를 한 번에 세팅하기 편하도록 만든 생성자입니다.
    public UserVO(String userId, String password, String userName, String phone, String userType) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.userType = userType;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    // 차량 정보에 접근하기 위한 Getter/Setter 메소드입니다.
    // 일반 고객일 경우 이 값은 null이 될 수 있으므로 사용할 때 null 체크에 유의해야 합니다.
    public VehicleVO getVehicle() { return vehicle; }
    public void setVehicle(VehicleVO vehicle) { this.vehicle = vehicle; }
}