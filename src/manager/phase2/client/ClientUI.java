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
		//设置窗体属性
		this.setTitle("WhiteBoard");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(3);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//拿到窗体的画笔
		g = (Graphics) this.getGraphics();
		g.setColor(Color.RED);
		
		//控制器初始化
		cc = new ControlClient(g,socket);
		//添加鼠标监听
		Listener listener = new Listener(cc);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		//控制器接收数据并处理数据
		cc.receiveData();
	}

}
