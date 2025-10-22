package w1022;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextFieldTest extends JFrame { // 클래스 정의 시작
    JTextField tf;
    JTextArea ta;
    JPasswordField pf;

    public TextFieldTest() {

        tf = new JTextField(10);
        ta = new JTextArea(5, 10);
        pf = new JPasswordField(10);

        JButton btn = new JButton("ShowMessage");
        btn.addActionListener(btnListener);

        setLayout(new FlowLayout());

        add(tf);
        JScrollPane sp = new JScrollPane(ta);
        add(sp);
        add(pf);
        add(btn); // [수정] 버튼 추가!

        setTitle("텍스트 입력 가능 컴포넌트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CenterFrame은 외부 클래스이므로 존재하는 경우에만 작동합니다.
        CenterFrame cf = new CenterFrame(200, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());

        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tfText = tf.getText();
            String taText = ta.getText();
            String pwText = pf.getText();
            JOptionPane.showMessageDialog(null, tfText + "\n" + taText + "\n" + pwText);
        }
    }; // btnListener 정의 끝

    public static void main(String[] args) { // [수정] 클래스 내부에 main 메서드 정의
        new TextFieldTest();
    }
} // 클래스 정의 끝