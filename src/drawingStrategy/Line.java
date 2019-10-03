package drawingStrategy;

import java.awt.Point;
import java.awt.geom.Line2D;

import whiteBoard.WhiteBoard;

public class Line extends DrawingStrategy{
	
	@Override
	public RegularShape draw(Point start, Point end) {
		RegularShape shape = new RegularShape();
		shape.shape = new Line2D.Double(start,end);
		shape.color = WhiteBoard.getInstance().getPenColor();
		return shape;
	}

}
