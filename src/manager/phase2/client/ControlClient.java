package phase2.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ControlClient {

	private Socket socket;
	public int x1,y1,x2,y2,color;
	public byte type,strock;
 
	public Graphics g;
	
	public ControlClient(Graphics g,Socket socket) {
		this.g=g;
		this.socket=socket;
	}
 
	//不断接受服务器发送过来的信息
	public void receiveData(){
		new Thread(){
			public void run() {
				try {
				while(true){

						DataInputStream dis=new DataInputStream(socket.getInputStream());
						DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
						//读图形类型
						type=dis.readByte();
						
						//读坐标
						x1=dis.readInt();
						y1=dis.readInt();
						x2=dis.readInt();
						y2=dis.readInt();
						//读粗细
						strock=dis.readByte();
						//读颜色
						color=dis.readInt();
						//读完数据之后画图形
						drawGra();
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	//发送图形数据给服务器
	public void sendData(int type,int x1,int y1,int x2,int y2,int strock){
		try {

			DataInputStream	dis=new DataInputStream(socket.getInputStream());
			DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
			//写图形类型
			dos.writeByte(type);
			//写坐标
			dos.writeInt(x1);
			dos.writeInt(y1);
			dos.writeInt(x2);
			dos.writeInt(y2);
			//写粗细
			dos.writeByte(strock);
			//写颜色
			dos.writeInt(g.getColor().getRGB());
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//绘制图形
	public void drawGra() {
		g.setColor(new Color(color));
		((Graphics2D) g).setStroke(new BasicStroke(strock));
		if(type==1){
			g.drawLine(x1, y1, x2, y2);
//			System.out.println("hutu 。。。。。");
		}
	}
 

}
