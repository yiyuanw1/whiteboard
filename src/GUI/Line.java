package GUI;

import java.awt.*;


public class Line extends AbstractTool {
    private static Tool tool = null;

    private Line(Canvas frame) {
        super(frame);

    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) tool = new Line(frame);
        return tool;
    }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }
}