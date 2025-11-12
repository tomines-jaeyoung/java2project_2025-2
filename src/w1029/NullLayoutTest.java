package w1029;
import center_Frame.CenterFrame;

import javax.swing.*;

public class NullLayoutTest extends JFrame {
    public NullLayoutTest() {
        setLayout(null); //배치관리자 없음
        int[] xy = {50, 70, 90, 110};

        for (int i = 0; i < xy.length; i++) {
            JButton btn = new JButton("버튼" + (i+1));
            btn.setBounds(xy[i], xy[i], 70, 50);
            add(btn);
        }

        setTitle("LayoutManager Null");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(250, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    public static void main(String[] args) {
        new NullLayoutTest();
    }

}