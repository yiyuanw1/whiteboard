package drawingStrategy;

import java.awt.Point;
import java.awt.event.MouseEvent;

import whiteBoard.WhiteBoard;

public class Eraser extends Free{

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		if( temp == null ) temp = new FreeShape(WhiteBoard.getInstance().getBackground(), start, radius);
		((FreeShape) temp).addPoint(p);
		WhiteBoard.getInstance().repaint();
	}

}
