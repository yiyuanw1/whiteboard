package drawingStrategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class FreeShape extends ShapeClass {

	public ArrayList<Point> points;
	
	public FreeShape() {
		super();
		points = new ArrayList<>();
	}
	
	public FreeShape(Point p) {
		super();
		points = new ArrayList<>();
		points.add(p);
	}
	
	public FreeShape(Point start, int thickness) {
		super();
		points = new ArrayList<>();
		points.add(start);
		this.thickness = thickness;
	}
	
	public FreeShape(Color background, Point start, int radius) {
		super(background);
		points = new ArrayList<>();
		points.add(start);
		this.thickness = radius;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		Graphics2D g2 = (Graphics2D) g;
		Point start = points.get(0);
		g2.fillOval(start.x-thickness/2, start.y-thickness/2, thickness, thickness);
		for( Point p : points.subList(1, points.size()-1) ) {
			g2.fillOval(p.x-thickness/2, p.y-thickness/2, thickness, thickness);
			g2.drawLine(start.x, start.y, p.x, p.y);
			start = p;
		}
	}
	
	public void addPoint(Point p) {
		points.add(p);
	}
	
	public void setRad(int rad) {
		this.thickness = rad;
	}
}
