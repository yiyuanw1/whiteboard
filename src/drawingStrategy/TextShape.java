package drawingStrategy;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class TextShape extends ShapeClass {

	private Font font;
	private String text;
	private Point p;
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		g2.drawString(text, p.x, p.y);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setPosition(Point p) {
		this.p = p;
	}
}
