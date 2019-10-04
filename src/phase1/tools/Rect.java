package phase1.tools;

import phase1.Canvas;

import java.awt.*;

public class Rect extends AbstractTool {
    private static Tool tool = null;

    private Rect(Canvas frame) {
        super(frame);
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) {
            tool = new Rect(frame);
        }
        return tool;
    }

    public void draw(Graphics g, int x1, int y1, int x2, int y2) {

        int x = Math.min(x2, x1);
        int y = Math.min(y2, y1);
        g.drawRect(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}