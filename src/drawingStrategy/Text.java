package drawingStrategy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import whiteBoard.WhiteBoard;

public class Text extends DrawingStrategy{
	
	private String text;
	private JTextField textField;
	
	@Override
	public TextShape draw(Point start, Point end) {
		temp = new TextShape();
		((TextShape)temp).setPosition(new Rect().computeCorner(start, start));
		((TextShape)temp).setText(text);
		return null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		textField = new JTextField();
		textField.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
		textField.setLocation((this.start = e.getPoint()));
		textField.setPreferredSize(new Dimension(16,12));
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					text = textField.getText();
					WhiteBoard.getInstance().addShape(draw(start,delta));
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
		});
		WhiteBoard.getInstance().add(textField);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		this.delta = e.getPoint();
		textField.setLocation(new Rect().computeCorner(start, delta));
		textField.setPreferredSize(new Rect().computeDimension(start, delta));
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		WhiteBoard.getInstance().addShape(temp);
	}
}
