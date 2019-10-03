package drawingStrategy;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import whiteBoard.WhiteBoard;

public class Oval extends Rect{

	@Override
	public RegularShape draw(Point start, Point end) {
		RegularShape shape = new RegularShape();
		Point corner = this.computeCorner(start, end);
		Dimension dim = this.computeDimension(start, end);
		shape.shape = new Ellipse2D.Double(corner.getX(),corner.getY(),dim.getWidth(),dim.getHeight());
		shape.color = WhiteBoard.getInstance().getPenColor();
		return shape;
	}

}
