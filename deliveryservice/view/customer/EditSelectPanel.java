package deliveryservice.view.customer;

import javax.swing.*;
import java.awt.*;

// 메인 화면에서 '정보 수정' 버튼을 눌렀을 때 나타나는 중간 선택 패널입니다.
// 회원 정보 수정과 주문 정보 수정/취소라는 두 가지 주요 기능을 명확히 분리하여,
// 사용자가 다음 단계를 실수 없이 선택할 수 있도록 유도하기 위해 만들었습니다.
public class EditSelectPanel extends JPanel {
    JButton btnEditProfile; // 개인 회원 정보를 수정하는 화면으로 이동
    JButton btnEditOrder;   // 주문 내역을 수정하거나 취소하는 화면으로 이동
    JButton btnBack;        // (주석 처리됨) 뒤로가기 버튼을 여기에 넣을까 하다가 컨트롤러에서 처리하기로 했습니다.

    public EditSelectPanel() {
        // 두 개의 버튼을 가로로 나란히 배치하기 위해 GridLayout을 사용했고, 버튼 사이에 20픽셀 간격을 두었습니다.
        setLayout(new GridLayout(1, 2, 20, 20));
        // 패널 외곽에 넓은 여백을 설정하여 버튼이 중앙에 배치된 것처럼 보이도록 했습니다.
        setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        // 1. 개인 정보 수정 버튼
        btnEditProfile = new JButton("개인정보 수정");
        btnEditProfile.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        // 2. 주문 수정/취소 버튼
        btnEditOrder = new JButton("주문 수정/취소");
        btnEditOrder.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        // 패널에 버튼들을 추가합니다.
        add(btnEditProfile);
        add(btnEditOrder);
    }

    // 이 패널을 담을 부모 프레임에 뒤로가기 버튼 등을 추가로 달아도 됩니다.

    // 외부 컨트롤러(CustomerMainController)에서 이벤트 리스너를 붙일 수 있도록 Getter를 제공합니다.
    public JButton getBtnEditProfile() { return btnEditProfile; }
    public JButton getBtnEditOrder() { return btnEditOrder; }
}