package drawingStrategy;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import whiteBoard.WhiteBoard;

public class Free extends DrawingStrategy {
	
	@Override
	public FreeShape draw(Point start, Point end) {
		return null;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		temp = null;
		start = e.getPoint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		WhiteBoard.getInstance().addShape(temp);
		WhiteBoard.getInstance().repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		if( temp == null ) temp = new FreeShape(start, radius);
		((FreeShape) temp).addPoint(p);
		WhiteBoard.getInstance().repaint();
	}
	
	public static void changeCursor() { 
        BufferedImage bi = new BufferedImage(radius,radius,BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = bi.getGraphics();
        g.setColor(WhiteBoard.getInstance().getPenColor());
        g.drawOval(0,0,radius,radius);
        WhiteBoard.getInstance().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(bi, new Point(radius,radius), WhiteBoard.getInstance().getPenColor() + " Circle"));
	}
}
