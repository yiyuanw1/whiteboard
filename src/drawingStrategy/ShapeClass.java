package drawingStrategy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import whiteBoard.WhiteBoard;

public abstract class ShapeClass {

	protected Color color;
	protected int thickness = 1;
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.setStroke(new BasicStroke(thickness));
	}
	
	public ShapeClass() {
		this.color = WhiteBoard.getInstance().getPenColor();
	}
	
	public ShapeClass(Color color) {
		this.color = color;
	}
	
	public void setThick(int thickness) {
		this.thickness = thickness;
	}
}
