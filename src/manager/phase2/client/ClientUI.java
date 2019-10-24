package phase2.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.Socket;

import javax.swing.JFrame;

public class ClientUI extends JFrame{

	public Socket socket;
	public Graphics g;
	public ControlClient cc;

	public ClientUI(Socket s) {

		try {
			//socket = new Socket("localhost",9090);
			socket = s;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	public void initFrame() {
		//���ô�������
		this.setTitle("WhiteBoard");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(3);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//�õ�����Ļ���
		g = (Graphics) this.getGraphics();
		g.setColor(Color.RED);
		
		//��������ʼ��
		cc = new ControlClient(g,socket);
		//���������
		Listener listener = new Listener(cc);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		//�������������ݲ���������
		cc.receiveData();
	}

}
