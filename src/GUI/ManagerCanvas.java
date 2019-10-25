package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ManagerCanvas extends Canvas implements ActionListener{

	@Override
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = super.createMenuBar();
		JMenu menu = new JMenu("Management");
		JMenuItem item = new JMenuItem("Show Clients");
		item.addActionListener(this);
		item.setActionCommand("show client");
		menu.add(item);
		menuBar.add(menu);
		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand().equals("show client")) {
			(new ManagerFrame()).setVisible(true);
		}
	}
}
