package management;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import GUI.UserListFrame;

public class ManagerFrame extends UserListFrame {

	@Override
	public void init() {
		super.init();
		list.addMouseListener(new mouseListener());
	}
	
	@Override
	public void removeUser(String name) {
		listModel.removeElement(name);
		UserPool.deleteUser(name);
	}
	
	class mouseListener extends MouseAdapter{
		
		@Override
		public void mousePressed(MouseEvent e) {
			if( SwingUtilities.isRightMouseButton(e) ) {
				JPopupMenu popup = new JPopupMenu();
				JMenuItem menuItem = new JMenuItem("Kick");
				menuItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int index = list.getSelectedIndex();
						listModel.remove(index);
					}
					
				});
				popup.add(menuItem);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
