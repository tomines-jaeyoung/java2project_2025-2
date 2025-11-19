package w1112.simple_paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener; // 드래그를 위해 추가!

// MouseListener와 MouseMotionListener를 모두 구현합니다.
public class PaintingPanel extends JPanel implements MouseListener, MouseMotionListener {
    public static String polygon;
    public int x1, y1, x2, y2;
    private boolean isDrawing = false; // 현재 드래그 중인지 확인하는 플래그

    public PaintingPanel(String polygon) {
        this.polygon = polygon;
        setBackground(Color.YELLOW);
        addMouseListener(this);
        addMouseMotionListener(this); // 드래그 이벤트를 받기 위해 추가
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // x1, y1, x2, y2가 모두 0이라면 아무것도 그리지 않습니다. (지우기 후 상태)
        if (x1 == 0 && y1 == 0 && x2 == 0 && y2 == 0) {
            return;
        }

        // 가상의 사각형 영역의 시작점(x, y)과 너비/높이(w, h)를 계산합니다.
        // 드래그 방향(왼쪽 위 -> 오른쪽 아래, 오른쪽 아래 -> 왼쪽 위 등)에 관계없이 정확한 좌표를 얻습니다.
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);

        // Graphics2D로 변환하여 선 굵기를 설정합니다.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // 도형 그리기
        switch (polygon){
            case "선":
                g2.setColor(Color.RED);
                // 선은 사각형 영역이 아닌, 처음 누른 좌표와 마지막 뗀/드래그 좌표를 직접 연결합니다.
                g2.drawLine(x1, y1, x2, y2);
                break;
            case "사각형":
                g2.setColor(Color.MAGENTA);
                // 계산된 x, y, w, h를 사용합니다.
                g2.drawRect(x, y, w, h); // fillRect 대신 drawRect로 변경 (내부 채우기 대신 테두리만)
                break;
            case "타원":
                g2.setColor(Color.BLUE);
                // 계산된 x, y, w, h를 사용합니다.
                g2.drawOval(x, y, w, h); // fillOval 대신 drawOval로 변경 (내부 채우기 대신 테두리만)
                break;
        }

        // **주의:** 마우스가 떼지지 않았는데도 x1=...0으로 초기화하면 그림이 사라지므로,
        // 오직 mouseReleased 시에만 좌표를 초기화하도록 합니다.
    }

    // --- MouseListener 구현 ---

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            // 좌클릭: 그리기 시작점 기록
            x1 = e.getX();
            y1 = e.getY();
            isDrawing = true;
            // x2, y2를 x1, y1과 동일하게 설정하여 그림이 점(dot)으로 시작하도록 합니다.
            x2 = x1;
            y2 = y1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // **우클릭: 화면 지우기**
            // 모든 좌표를 0으로 초기화
            x1 = x2 = y1 = y2 = 0;
            isDrawing = false;
            repaint(); // repaint를 호출하여 빈 화면(배경색)을 다시 그립니다.
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;

        // 마우스 뗀 좌표 기록
        x2 = e.getX();
        y2 = e.getY();
        isDrawing = false;

        repaint(); // 최종적으로 완성된 도형을 그립니다.

        // **주의:** 마지막에 그린 도형이 사라지지 않도록 x1, y1 등을 초기화하지 않습니다.
        // 다음 그리기(mousePressed) 때 새로운 좌표가 덮어쓰여집니다.
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    // --- MouseMotionListener 구현 ---

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isDrawing) return;

        // 드래그 중인 좌표 기록
        x2 = e.getX();
        y2 = e.getY();

        // repaint를 호출하여 실시간으로 도형 미리보기를 그립니다.
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}