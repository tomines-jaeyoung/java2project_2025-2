package w1015;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class RadioButtonTest extends JFrame {
    JRadioButton[] rbArr = new JRadioButton[3];
    String[] rbStrArr = {"아이브", "뉴진스", "블랙핑크"};
    String[] imgNameArr = {"blackpink.jpg", "ive1.jpg", "newjeans.jpg"};
JLabel lbl = new JLabel("", JLabel.CENTER);
    public RadioButtonTest() {
        JPanel panel = new JPanel();
        ButtonGroup bg = new ButtonGroup();
       
     int i =0;
        for(String str : rbStrArr) {
        rbArr[i] =new JRadioButton(str);
        rbArr[i].addActionListener(radioBtnListener);
        bg.add(rbArr[i]);
        add(rbArr[i]);
//        i++;

        }
        lbl.setOpaque(true);
        lbl.setBackground(Color.PINK);
        add(panel,"North");
        add(lbl, "Center");

        setTitle("RadioButtonTest");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CenterFrame cf = new CenterFrame(500,300);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFh(), cf.getFw());
        setVisible(true);



    }
    ActionListener radioBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i =0;
            ImageIcon ImageIcon = new ImageIcon("D:/java2projects/java2project/src/w1015/imgs/blackpink.jpg"+imgNameArr[i]+".jpg");
            for(JRadioButton rb : rbArr) {
                if(rb.isSelected()) {

                    lbl.setIcon(ImageIcon);
                    JOptionPane.showMessageDialog(null, rb.getText()+"가 선택되었습니다.");
                }
                i++;
            }
        }
    };
    public static void main(String[] args) {
        new RadioButtonTest();
    }

}
