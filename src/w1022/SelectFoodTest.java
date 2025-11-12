package w1022;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// *주의: java.awt.*와 java.awt.event.*는 이미지에 보이지 않지만,
//       JFrame과 ActionListener를 사용하기 위해 필요하다고 가정합니다.

public class SelectFoodTest extends JFrame { // new *
    JComboBox combo;
    JLabel imgLbl;
    String[] items = {"연어초밥", "새우초밥", "장어초밥", "유부초밥", "계란초밥"}; // 1 usage


    public SelectFoodTest() { // 1 usage new *

        JPanel northPan = new JPanel();
        JLabel lbl1 = new JLabel("Food: "); // 'text:'는 IntelliJ IDEA의 힌트일 가능성이 높습니다.
        combo = new JComboBox(items);
        JButton btn = new JButton("");
        northPan.add(lbl1);northPan.add(combo);northPan.add(btn);

        add(northPan, "North");
        imgLbl = new JLabel(new ImageIcon());
        add(imgLbl, "Center");


        setTitle("음식선택");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(400, 500);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());

        setVisible(true);

    }
ActionListener btnListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = combo.getSelectedIndex();
            ImageIcon imgIcon = new ImageIcon();
            imgLbl.setIcon(imgIcon);
        }
};


public static void main(String[] args) {
        new SelectFoodTest();
}

}