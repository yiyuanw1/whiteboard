package phase1.tools;

import phase1.Canvas;

import java.awt.*;
import java.net.Socket;


public class Oval extends AbstractTool {
    private static Tool tool = null;
    private String shape = "Oval";
    private Oval(Canvas frame,Socket s) {
        super(frame,s);
        this.setShape(shape);
    }

    public static Tool getInstance(Canvas frame,Socket s) {
        if (tool == null) tool = new Oval(frame,s);
        return tool;
    }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) {

        int x = Math.min(x2, x1);
        int y = Math.min(y2, y1);

        g.drawOval(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));

    }
}