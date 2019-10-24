package phase1.tools;

import phase1.Canvas;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.Socket;


public class Free extends AbstractTool {
    private static Tool tool = null;
    private static String shape = "Free";
    
    private Free(Canvas frame, Socket s) {
        super(frame, "resource/freecursor.gif",s);
        this.setShape(shape);
    }

    public static Tool getInstance(Canvas frame, Socket s) {
        if (tool == null) {
            tool = new Free(frame, s);
        }
        return tool;
    }


    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            g.drawLine(getX(), getY(), e.getX(), e.getY());
            sendData(getX(), getY(), e.getX(), e.getY(),shape,color,"");
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
        }
    }
}