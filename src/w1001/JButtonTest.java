package w1001;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonTest extends JFrame {
public JButtonTest() {
    setLayout(new FlowLayout(FlowLayout.CENTER));
    setTitle("JButtonTest");
    setDefaultCloseOperation(EXIT_ON_CLOSE);


  String[] lblTexts ={"폴리텍", "서울정수", "인공지능", "소프트웨어과", "1학년"};
  JButton[] btns = new JButton[lblTexts.length];
  for(int i = 0; i < lblTexts.length; i++) {
      btns[i] = new JButton(lblTexts[i]);
      btns[i].addActionListener(btnListener);
      add(btns[i]);
  }

setBounds(200, 200, 300, 200);
setVisible(true);



}
ActionListener btnListener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
JOptionPane.showMessageDialog(null, e.getActionCommand()+ "버튼을 선택했습니다.");
    }
};


public static void main(String[] args) {
    new JButtonTest();
}






}
