package w1112;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioButtonTest extends JFrame {


    JRadioButton rbLine = new JRadioButton("선");
    JRadioButton rbRect = new JRadioButton("사각형");
    JRadioButton rbOval = new JRadioButton("타원", true); // 타원을 기본 선택
    JButton drawButton = new JButton("그리기 실행");


    private DrawingPanel drawingPanel = new DrawingPanel();


    public RadioButtonTest() {
        setTitle("라디오 버튼 활용 간단 그림판 (실제 그리기)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel topPanel = new JPanel();

        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(rbLine);
        shapeGroup.add(rbRect);
        shapeGroup.add(rbOval);

        topPanel.add(new JLabel("도형 선택: "));
        topPanel.add(rbLine);
        topPanel.add(rbRect);
        topPanel.add(rbOval);
        topPanel.add(drawButton);


        add(topPanel, BorderLayout.NORTH);

        add(drawingPanel, BorderLayout.CENTER);


        ButtonListener listener = new ButtonListener();


        rbLine.addActionListener(listener);
        rbRect.addActionListener(listener);
        rbOval.addActionListener(listener);
        drawButton.addActionListener(listener);


        setSize(500, 400);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - 500) / 2;
        int y = (dim.height - 400) / 2;
        setLocation(x, y);

        setVisible(true);
    }


    public class DrawingPanel extends JPanel {

        private String currentShape = "타원";


        private boolean isShapeDrawn = false;

        public DrawingPanel() {

            setBackground(Color.YELLOW);
        }


        public void setSelectedShape(String shape) {
            this.currentShape = shape;
            this.isShapeDrawn = false;
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);


            if (isShapeDrawn) {
                return;
            }


            int width = getWidth();
            int height = getHeight();
            int drawSize = 150;
            int x = (width - drawSize) / 2;
            int y = (height - drawSize) / 2;


            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));


            switch (currentShape) {
                case "선":
                    // 중앙을 가로지르는 선
                    g2.drawLine(20, height / 2, width - 20, height / 2);
                    break;
                case "사각형":

                    g2.drawRect(x, y, drawSize, drawSize);
                    break;
                case "타원":

                    g2.drawOval(x, y, drawSize, drawSize + 50); // 세로로 긴 타원
                    break;
                default:

                    break;
            }

            this.isShapeDrawn = true;
        }
    }



    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


            if (e.getSource() instanceof JRadioButton) {
                JRadioButton selected = (JRadioButton) e.getSource();

                drawingPanel.setSelectedShape(selected.getText());

            }


            else if (e.getSource() == drawButton) {

                String selectedShape = "";
                if (rbLine.isSelected()) {
                    selectedShape = "선";
                } else if (rbRect.isSelected()) {
                    selectedShape = "사각형";
                } else if (rbOval.isSelected()) {
                    selectedShape = "타원";
                }


                drawingPanel.setSelectedShape(selectedShape);


                drawingPanel.repaint();


                JOptionPane.showMessageDialog(
                        null,
                        "[" + selectedShape + "]이(가) 중앙에 그려졌습니다.",
                        "그리기 완료",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }


    public static void main(String[] args) {
        new RadioButtonTest();
    }
}