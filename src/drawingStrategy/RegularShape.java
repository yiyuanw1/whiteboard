package drawingStrategy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

public class RegularShape extends ShapeClass{

	public Shape shape;
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.draw(shape);
	}
}
