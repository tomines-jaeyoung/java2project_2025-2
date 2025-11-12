package w1105;

import center_Frame.CenterFrame;

import javax.swing.*;
import java.awt.*;

public class DrawPolygonTest extends JFrame {
    public DrawPolygonTest() {

        DrawPanel dp = new DrawPanel();
                add(dp);
        setTitle("MouseEvent find location");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(500, 400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }


    public static void main(String[] args) {
        new DrawPolygonTest();
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.green);
            g.drawOval(20, 30, 150, 100);
            g.setColor(Color.blue);
            g.fillOval(200, 30, 150, 100);
            g.setColor(Color.orange);
            g.fillOval(370, 30, 100, 100);
            g.setColor(Color.pink);
            g.drawRect(20, 160, 200, 150);
            g.setColor(Color.magenta);
            g.fillRect(250, 160, 200, 200);
            g.setColor(Color.black);
            g.fillRoundRect(280, 190, 140, 140, 20, 20);
            g.setColor(Color.yellow);
            int[] xPoints = {70, 20, 100};
            int[] yPoints = {180, 215, 215};
            g.drawPolygon(xPoints, yPoints, xPoints.length);
        }
    }
}