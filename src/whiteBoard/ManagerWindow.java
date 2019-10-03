package whiteBoard;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fileManagement.FileManageStrategy;

public class ManagerWindow extends UserWindow{

	private JMenuBar jmb;
	private JMenu fileMenu;
	private FileManageStrategy fileManaging;
	
	public ManagerWindow() {
		this.title = "Manager WhiteBoard";
		initialize();
	}
	
	@Override
	protected void initialize() {
		frame = new JFrame(title);
		this.initializeFileMenu();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationByPlatform(true);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		super.createWindow();
		frame.pack();
	}
	
	private void initializeFileMenu() {
		jmb = new JMenuBar();
		fileMenu = new JMenu("File");
		for( String op: FileManageStrategy.OP ) {
			JMenuItem item = new JMenuItem(op);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String className = "fileManagement."+op;
					try {
						Class<?> fileClass = Class.forName(className);
						Constructor<?> classConstructor = fileClass.getConstructor();
						ManagerWindow.this.fileManaging = (FileManageStrategy) classConstructor.newInstance(new Object[] {});
					} catch (ClassNotFoundException | NoSuchMethodException 
							| SecurityException | InstantiationException 
							| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			});
			fileMenu.add(new JMenuItem(op));
		}
		jmb.add(fileMenu);
		frame.setJMenuBar(jmb);
	}

	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerWindow window = new ManagerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
