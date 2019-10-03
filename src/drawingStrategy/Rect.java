package drawingStrategy;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import whiteBoard.WhiteBoard;

public class Rect extends DrawingStrategy{

	@Override
	public RegularShape draw(Point start, Point end) {
		RegularShape shape = new RegularShape();
		shape.shape = new Rectangle(this.computeCorner(start, end), this.computeDimension(start, end));
		shape.color = WhiteBoard.getInstance().getPenColor();
		return shape;
	}

	protected Point computeCorner(Point start, Point end) {
		Point corner = new Point();
		corner.x = (start.x > end.x) ? end.x : start.x;
		corner.y = (start.y > end.y) ? end.y : start.y;
		return corner;
	}
	
	protected Dimension computeDimension(Point start, Point end) {
		return new Dimension(Math.abs(start.x-end.x),Math.abs(start.y-end.y));
	}
}
