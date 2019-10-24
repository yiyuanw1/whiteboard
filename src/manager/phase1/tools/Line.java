package phase1.tools;

import phase1.Canvas;

import java.awt.*;
import java.net.Socket;


public class Line extends AbstractTool {
    private static Tool tool = null;
    private String shape = "Line";
    private Line(Canvas frame, Socket s) {
        super(frame,s);
        this.setShape(shape);

    }

    public static Tool getInstance(Canvas frame,Socket s) {
        if (tool == null) tool = new Line(frame,s);
        return tool;
    }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);

    }
}