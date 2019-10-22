package phase1.tools;

import phase1.Canvas;

import java.awt.*;
import java.awt.event.MouseEvent;


public class Free extends AbstractTool {
    private static Tool tool = null;

    private Free(Canvas frame) {
        super(frame, "resource/freecursor.gif");
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) {
            tool = new Free(frame);
        }
        return tool;
    }


    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            g.drawLine(getX(), getY(), e.getX(), e.getY());
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
        }
    }
}