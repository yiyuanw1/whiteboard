package whiteBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import drawingStrategy.DrawingStrategy;
import drawingStrategy.ShapeClass;

public class WhiteBoard extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private static WhiteBoard instance = null;
	
	private DrawingStrategy draw;
	
	private ArrayList<ShapeClass> shapes;
	private Color penColor = Color.BLACK;
	private Color background = Color.WHITE;

	private WhiteBoard() {
		super();
		draw = null;
		shapes = new ArrayList<>();
	}
	
	public void setDraw(DrawingStrategy draw) {
		if( draw != null ) {
			this.cleanMouseListener();
		}
		this.draw = draw;
		// if( draw instanceof Free ) Free.changeCursor();
		this.addMouseListener(draw);
		this.addMouseMotionListener(draw);
		this.addMouseWheelListener(draw);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for( ShapeClass shape : shapes ) {
			shape.draw(g);
		}
		if( draw != null && draw.temp != null ) {
			draw.temp.draw(g);
		}
	}
	
	public static WhiteBoard getInstance() {
		if( instance == null ) instance = new WhiteBoard();
		return instance;
	}
	
	public void setPenColor(Color color) {
		this.penColor = color;
	}
	
	public Color getPenColor() {
		return penColor;
	}
	
	public Color getBackground() {
		return this.background;
	}
	
	public void addShape(ShapeClass temp) {
		shapes.add(temp);
	}
	
	private void cleanMouseListener() {
		for( MouseListener l : this.getMouseListeners() ) {
			this.removeMouseListener(l);
		}
		for( MouseMotionListener l : this.getMouseMotionListeners() ) {
			this.removeMouseMotionListener(l);
		}
		for( MouseWheelListener l : this.getMouseWheelListeners() ) {
			this.removeMouseWheelListener(l);
		}
	}
	
	
}

