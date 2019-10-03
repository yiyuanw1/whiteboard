package drawingStrategy;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import whiteBoard.WhiteBoard;

public abstract class DrawingStrategy extends MouseAdapter{

	protected Point start;
	protected Point delta;

	public static int radius = 1;
	
	public ShapeClass temp = null;
	
	public final static String[] OP = {"Line","Rect","Circle","Oval","Free","Eraser","Text"};
	public abstract ShapeClass draw(Point start, Point end);

	public DrawingStrategy() {
		start = new Point();
		delta = new Point();
	}
	
	public static boolean validOperations(String op) {
		for ( String oP : OP ) {
			if ( op.equals(oP) ) return true;
		}
		return false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.start = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		WhiteBoard.getInstance().addShape(temp);
		WhiteBoard.getInstance().repaint();
		temp = null;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		delta = e.getPoint();
		temp = draw(start, delta);
		WhiteBoard.getInstance().repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		if( rotation <= 1 ) {
			radius += 1;
		}
		else if( rotation > 1 ){
			radius -= 1;
		}
		else {
		}
	}
}
