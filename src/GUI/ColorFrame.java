package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SwingConstants;

public class ColorFrame extends JFrame{
	
	public static int MAX_COLOR = 255;
	public static String[] colors = {"   Red : "," Green : ","  Blue : ","Transparency:"};
	
	Color color;
	JPanel colorBoard;
	public int sRGB[] = new int[4];

	public ColorFrame() {
		this.setTitle("color panel");
		this.color = AbstractTool.color;
		init();
	}
	
	public void init() {
		JPanel pane = new JPanel(new BorderLayout(0,0));
		
		JPanel mainColorPanel = new JPanel(new BorderLayout(0,0));
		this.createColorSliders(mainColorPanel);
		pane.add(mainColorPanel,BorderLayout.NORTH);
		
		colorBoard = new JPanel();
		colorBoard.setLayout(null);
		this.createColorBoard();
		pane.add(colorBoard,BorderLayout.CENTER);
		
		getContentPane().add(pane);
		this.pack();
	}

	private void createColorBoard() {
		
	}

	private void createColorSliders(JPanel parent) {
		JPanel colorPanels = new JPanel(new BorderLayout(0,0));
		colorPanels.add(createColorPanel(0),BorderLayout.NORTH);
		colorPanels.add(createColorPanel(1),BorderLayout.CENTER);
		colorPanels.add(createColorPanel(2),BorderLayout.SOUTH);
		parent.add(colorPanels,BorderLayout.NORTH);
		parent.add(createColorPanel(3),BorderLayout.CENTER);
	}
	
	private JPanel createColorPanel(int index) {
		JPanel panel = new JPanel(new BorderLayout(0,0));
		JLabel label = new JLabel(ColorFrame.colors[index]);
		JTextField value = new JTextField();
		value.setHorizontalAlignment(SwingConstants.TRAILING);
		value.setText(" 255 ");
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, MAX_COLOR, 255);
		slider.setPaintTrack(false);
		slider.setBackground(this.getOriginColor(index, slider.getValue()));
		slider.addChangeListener(new ColorStateListener(index,value));
		panel.add(label,BorderLayout.WEST);
		panel.add(value,BorderLayout.CENTER);
		panel.add(slider,BorderLayout.EAST);
		return panel;
	}
	
	private Color getOriginColor(int index, int value) {
		switch(index) {
		case 0:
			return new Color(value,0,0);
		case 1:
			return new Color(0,value,0);
		case 2:
			return new Color(0,0,value);
		default:
			return color;
		}
	}
	
	class ColorStateListener implements ChangeListener{

		int index;
		JTextField textField;
		
		ColorStateListener(int index,JTextField textField){
			this.index = index;
			this.textField = textField;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			
		}
		
	}
}

