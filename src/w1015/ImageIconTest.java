package w1015;

import center_Frame.CenterFrame;

import javax.swing.*;

public class ImageIconTest extends JFrame {
    public ImageIconTest() {
        ImageIcon icon1 = new ImageIcon("D:/java2projects/java2project/src/w1015/imgs/blackpink.jpg");
        ImageIcon icon2 = new ImageIcon("D:/java2projects/java2project/src/w1015/imgs/ive1.jpg");
        ImageIcon icon3 = new ImageIcon("D:/java2projects/java2project/src/w1015/imgs/newjeans");
        JLabel label = new JLabel();
        JButton button = new JButton();
        add(label, "North");
        add(button, "Center");

        setTitle("ImageIconTest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JFrame이 모니터 화면 정중앙에 오도록 한다
        CenterFrame cf = new CenterFrame(500, 500);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true); //화면 출력
    }

    public static void main(String[] args) {
        new ImageIconTest();
    }
}
