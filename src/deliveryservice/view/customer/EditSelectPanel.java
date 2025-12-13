package deliveryservice.view.customer;

import javax.swing.*;
import java.awt.*;

public class EditSelectPanel extends JPanel {
    JButton btnEditProfile;
    JButton btnEditOrder;
    JButton btnBack;

    public EditSelectPanel() {
        setLayout(new GridLayout(1, 2, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        btnEditProfile = new JButton("개인정보 수정");
        btnEditProfile.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        btnEditOrder = new JButton("주문 수정/취소");
        btnEditOrder.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        add(btnEditProfile);
        add(btnEditOrder);
    }

    // 이 패널을 담을 부모 프레임에 뒤로가기 버튼 등을 추가로 달아도 됩니다.
    public JButton getBtnEditProfile() { return btnEditProfile; }
    public JButton getBtnEditOrder() { return btnEditOrder; }
}