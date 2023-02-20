package GUI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import ThreadPool.SocketThreadPipeStructure;
import management.ManagerListener;
import management.UserPool;
import whiteBoardServer.ServerListeningSocketThread;

import javax.swing.ListSelectionModel;

public class UserListFrame extends JFrame implements ManagerListener{
	
	protected JList<String> list;
	protected DefaultListModel<String> listModel; 
	
	public UserListFrame() {
		UserPool.addListener(this);
		init();
	}
	
	public void init() {
		this.setLocationByPlatform(true);
		listModel = new DefaultListModel<String>();
		showList();
		
		list = new JList<String>(listModel);
		list.setValueIsAdjusting(true);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(-1);
		
		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(80,360));
		
		this.add(scroll);
		
		this.pack();
	}
	
	@Override
	public void newUser(String name) {
		listModel.addElement(name);
	}

	@Override
	public void removeUser(String name) {
		listModel.removeElement(name);
	}
	
	public void showList() {
		ArrayList<String> users = UserPool.readUser();
		for( String name: users ) {
			listModel.addElement(name);
		}
	}

}
