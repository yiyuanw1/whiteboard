package drawingStrategy;

import java.awt.Dimension;
import java.awt.Point;
public class Circle extends Oval{

	@Override
	protected Point computeCorner(Point start, Point end) {
		Dimension dim = this.computeDimension(start, end);
		Point corner = new Point();
		corner.x = (start.x > end.x) ? start.x-dim.width : start.x;
		corner.y = (start.y > end.y) ? start.y-dim.height : start.y;
		return corner;
	}
	
	@Override
	protected Dimension computeDimension(Point start, Point end) {
		int radius = Math.min(Math.abs(start.x-end.x),Math.abs(start.y-end.y));
		return new Dimension(radius, radius);
	}
}
