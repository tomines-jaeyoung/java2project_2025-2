package w1112;

import center_Frame.CenterFrame;
import javax.swing.*;

public class UserDialogTest extends JFrame {

    public UserDialogTest() {
        setTitle("사용자정의 대화상자 연습");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 프레임 중앙 배치 로직 (이전 코드와 동일)
        CenterFrame cf = new CenterFrame(500, 400); // 이미지 기반 추정
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);

        // --- JOptionPane을 사용한 대화 상자 추가 부분 ---

        // 1. 선택지 설정 및 질문 대화 상자 표시
        String[] strOptions = {"뉴진스", "블랙핑크", "아이브", "르세라핌"};
        int option = JOptionPane.showOptionDialog(
                null,
                "가장 좋아하는 아이돌 그룹을 선택해 주세요.",
                "2025Idol",
                JOptionPane.OK_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                strOptions,
                strOptions[0]
        );

        // 2. 결과 표시 대화 상자
        JOptionPane.showMessageDialog(
                null,
                strOptions[option] + "이 선택되었습니다.",
                "좋아하는 아이돌",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        new UserDialogTest();
    }
}