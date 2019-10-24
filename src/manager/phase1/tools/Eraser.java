package phase1.tools;

import phase1.Canvas;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.Socket;


public class Eraser extends AbstractTool {
    private static Tool tool = null;
    private String shape = "Eraser";
    private Eraser(Canvas frame,Socket s) {
        super(frame, "resource/erasercursor.gif",s);
        this.setShape(shape);
    }

    public static Tool getInstance(Canvas frame, Socket s) {
        if (tool == null) {
            tool = new Eraser(frame, s);
        }
        return tool;
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        Graphics g = getFrame().getBufferedImage().getGraphics();
        int x = 0;
        int y = 0;

        int size = 6;
        if (getX() > 0 && getY() > 0) {
            g.setColor(Color.WHITE);
            x = ((e.getX() - getX()) > 0) ? getX() : e.getX();
            y = ((e.getY() - getY()) > 0) ? getY() : e.getY();
            g.fillRect(x, y, Math.abs(e.getX() - getX())
                    + size, Math.abs(e.getY() - getY()) + size);
            sendData(getX(), getY(), e.getX(), e.getY(),shape,color,"");
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
        }
    }

}