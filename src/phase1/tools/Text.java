package phase1.tools;

import phase1.Canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Text extends AbstractTool {
    private static Tool tool = null;

    private Text(Canvas frame) {
        super(frame);
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) tool = new Text(frame);
        return tool;
    }


    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            String text = JOptionPane.showInputDialog("Enter Text:");
            g.drawString(text, getX(), getY());
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
        }
    }
}