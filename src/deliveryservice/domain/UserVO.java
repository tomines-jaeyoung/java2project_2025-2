package deliveryservice.domain;

public class UserVO {
    private String userId;
    private String password;
    private String userName;
    private String phone;
    private String userType; // '고객' or '기사'

    // ★ 추가된 부분: 기사일 경우 차량 정보를 담음
    private VehicleVO vehicle;

    public UserVO() {}
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

    // ★ 차량 Getter/Setter 추가
    public VehicleVO getVehicle() { return vehicle; }
    public void setVehicle(VehicleVO vehicle) { this.vehicle = vehicle; }
}