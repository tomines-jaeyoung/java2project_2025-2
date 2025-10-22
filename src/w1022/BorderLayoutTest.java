package w1022;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutTest extends JFrame {
    public void FlowLayoutTest() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
       String []locs= {BorderLayout.EAST, BorderLayout.WEST, BorderLayout.NORTH, BorderLayout.CENTER};
        JButton[] btns = new JButton[5];
        for(int i = 0; i < btns.length; i++) {
            btns[i] = new JButton("버튼" + (i + 1));
            add(btns[i], locs[i]);
        }

        setTitle("Flowlayout 연습");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //없으면 종료 버튼 눌러도 프로그램은 종료 안 됨
        CenterFrame cf = new CenterFrame(200, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true); //컴포넌트가 프레임에 추가된 다음에 작성해야 함(화면에 보이도록)
    }

    public static void main(String[] args) {
        new BorderLayoutTest();
    }
}
