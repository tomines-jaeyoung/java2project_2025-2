package center_Frame;

import java.awt.*;

public class CenterFrame {
    private int x,y,fw,fh;

    public CenterFrame(int fw, int fh) {
        this.fw = fw;
        this.fh = fh;
    }

    public void centerXY(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int x = screenWidth/2 - fw/2;
        int y = screenHeight/2 - fh/2;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFw() {
        return fw;
    }

    public void setFw(int fw) {
        this.fw = fw;
    }

    public int getFh() {
        return fh;
    }

    public void setFh(int fh) {
        this.fh = fh;
    }


}
