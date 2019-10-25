package GUI;

import java.awt.*;


public class Oval extends AbstractTool {
    private static Tool tool = null;

    private Oval(Canvas frame) {
        super(frame);
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) tool = new Oval(frame);
        return tool;
    }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) {

        int x = Math.min(x2, x1);
        int y = Math.min(y2, y1);

        g.drawOval(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}