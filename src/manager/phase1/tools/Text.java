package phase1.tools;

import phase1.Canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.Socket;

public class Text extends AbstractTool {
    private static Tool tool = null;
    private String shape = "Text";
    private Text(Canvas frame,Socket s) {
        super(frame,s);
        this.setShape(shape);
    }

    public static Tool getInstance(Canvas frame,Socket s) {
        if (tool == null) tool = new Text(frame,s);
        return tool;
    }


    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            String text = JOptionPane.showInputDialog("Enter Text:");
            g.drawString(text, getX(), getY());
            sendData(getX(), getY(), e.getX(), e.getY(),shape,color,text);
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
        }
    }
}