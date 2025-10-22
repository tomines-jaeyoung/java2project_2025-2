package w1022;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;

public class ListAndComboTest extends JFrame {
    JList list;
    JComboBox combo;
    String [] items= {"사과", "토마토", "포도", "딸기", "오렌지", "바나나"};

    public ListAndComboTest() {
        setLayout(new FlowLayout());
        list = new JList(items);
        combo = new JComboBox(items);
        add(combo);
        add(list);

        setTitle("항목 선택 가능 컴포넌트 JList and JComboBox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CenterFrame은 외부 클래스이므로 존재하는 경우에만 작동합니다.
        CenterFrame cf = new CenterFrame(300, 300);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());

        setVisible(true);

    }

    public static void main(String[] args) {

        new ListAndComboTest();
    }

}