package w1029;
import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class RandomGridDice extends JFrame {
    public RandomGridDice() {
        Random rand = new Random();
        int rows = rand.nextInt(10) + 1;
        int cols = rand.nextInt(10) + 1;
        setLayout(new GridLayout(rows, cols));

        int diceNum = rand.nextInt(5);
        Color[] colors = {Color.BLUE, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow };

        for (int i = 0; i < rows*cols; i++) {

            JButton btn = new JButton(diceNum + "");
            btn.setBackground(colors[diceNum-1]);
            add(btn);
        }
        setTitle("RandomGridDice");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(500, 500);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    public static void main(String[] args) {
        new RandomGridDice();
    }
}

