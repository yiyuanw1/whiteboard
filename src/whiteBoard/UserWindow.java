package whiteBoard;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

import chatSystem.ChatBoard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import drawingStrategy.*;

public class UserWindow implements ActionListener{

	protected JFrame frame;
	
	private DrawingStrategy draw;
	// components for drawing
	private JToolBar operations;
	
	public Point CENTER_POINT = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	protected Point beginPoint;
	
	protected int whiteBoardWidth = 640;
	protected int whiteBoardHeight = 480;
	
	protected String title = "User WhiteBoard";
	
	/**
	 * Create the application.
	 */
	public UserWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	protected void initialize() {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationByPlatform(true);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		createWindow();
		frame.pack();
	}
	
	protected void createWindow() {

		operations = new JToolBar();
		frame.getContentPane().add(operations, BorderLayout.WEST);
		operations.setOrientation(JToolBar.VERTICAL);
		initializeOperations();
		
		WhiteBoard.getInstance().setBackground(WhiteBoard.getInstance().getBackground());
		WhiteBoard.getInstance().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JScrollPane scrollPane = new JScrollPane(WhiteBoard.getInstance());
		
		scrollPane.setPreferredSize(new Dimension(whiteBoardWidth, whiteBoardHeight));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		ChatBoard chatBoard = new ChatBoard(whiteBoardWidth/2, whiteBoardHeight);
		frame.getContentPane().add(chatBoard, BorderLayout.EAST);
	}
	
	protected void initializeOperations() {
		for( String s : DrawingStrategy.OP ) {
			this.createButton(s);
		}
		this.operations.addSeparator();
		
		this.createButton("Color");
	}
	
	protected void createButton(String text) {
		JButton button = new JButton(text);
		button.addActionListener(this);
		button.setActionCommand(text);
		operations.add(button);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		if( DrawingStrategy.validOperations(cmd) ) {
			String className = "drawingStrategy."+cmd;
			try {
				Class<?> drawClass = Class.forName(className);
				Constructor<?> classConstructor = drawClass.getConstructor();
				draw = (DrawingStrategy) classConstructor.newInstance(new Object[] {});
				WhiteBoard.getInstance().setDraw(draw);
			}catch ( NoSuchMethodException | SecurityException 
					| InstantiationException | IllegalAccessException 
					| IllegalArgumentException | InvocationTargetException | ClassNotFoundException e1) {
				System.out.println("operation not found.");
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserWindow window = new UserWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
