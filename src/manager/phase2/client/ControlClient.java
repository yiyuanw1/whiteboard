package phase2.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.json.*;

import phase1.tools.Tool;

import javax.swing.JOptionPane;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
public class ControlClient {

	private Socket socket;
	public int x1,y1,x2,y2,color;
	public String action;
	public byte type,strock;
	public Tool tool;
 
	public Graphics g;
	
	public ControlClient(Graphics g,Socket socket) {
		this.g=g;
		this.socket=socket;
		this.tool = tool;
	}
 
	//���Ͻ��ܷ��������͹�������Ϣ
	public void receiveData(){
		new Thread(){
			public void run() {
				try {
				while(true){

					DataInputStream dis=new DataInputStream(socket.getInputStream());
					DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
						
					String inputStr = dis.readUTF();
						
					JSONObject JO = new JSONObject(inputStr);
					//JSONParser parser = new JSONParser();
					//JO = (JSONObject) parser.parse(inputStr);
					action = JO.getString("Action");
					
					switch(action) {
					case "Join":{
						String joiner = JO.getString("Username");
						int approve = JOptionPane.showConfirmDialog(null, joiner+" want to join.", "New request",JOptionPane.YES_NO_OPTION);
						JO.put("Action", "Reply");
						if(approve==0) { //ͬ�����							
							JO.put("Reply","Approve");
							dos.writeUTF(JO.toString());								
						} else {
							JO.put("Reply","Refuse to join.");
							dos.writeUTF(JO.toString());
						}
						dos.flush();
						break;
					}
					case "Draw":{
						int t = JO.getInt("Type");
						type = (byte) (t&0xff);
						x1 =   JO.getInt("X1");
						y1 =  JO.getInt("Y1");
						x2 =  JO.getInt("X2");
						y2 =  JO.getInt("Y1");
						int s = JO.getInt("Strock");
						strock  = (byte) (s&0xff);
						color =  JO.getInt("Color");
						
						drawGra();
						break;
					}
					case "Quit":{
						String name = JO.getString("Username");
						JOptionPane.showMessageDialog(null, name+" quit." , "Message",JOptionPane.PLAIN_MESSAGE); 
						break;
					}
					
					default:
						break;
					}
						
	/*					//��ͼ������
						type=dis.readByte();
						
						//������
						x1=dis.readInt();
						y1=dis.readInt();
						x2=dis.readInt();
						y2=dis.readInt();
						//����ϸ
						strock=dis.readByte();
						//����ɫ
						color=dis.readInt();
						//��������֮��ͼ��
						drawGra();  */
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	//����ͼ�����ݸ�������
	public void sendData(int type,int x1,int y1,int x2,int y2,int strock){
		try {

			DataInputStream	dis=new DataInputStream(socket.getInputStream());
			DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
			//дͼ������
			
			JSONObject JO = new JSONObject();
			JO.put("Action","Draw");
			JO.put("Type", type);
			JO.put("X1", x1);
			JO.put("X2", x2);
			JO.put("Y1", y1);
			JO.put("Y2", y2);
			JO.put("Strock", strock);
			JO.put("Color", g.getColor().getRGB());
			
			dos.writeUTF(JO.toString());
			/*
			dos.writeByte(type);
			//д����
			dos.writeInt(x1);
			dos.writeInt(y1);
			dos.writeInt(x2);
			dos.writeInt(y2);
			//д��ϸ
			dos.writeByte(strock);
			//д��ɫ
			dos.writeInt(g.getColor().getRGB());
			*/
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//����ͼ��
	public void drawGra() {
		g.setColor(new Color(color));
		((Graphics2D) g).setStroke(new BasicStroke(strock));
		if(type==1){
			g.drawLine(x1, y1, x2, y2);
//			System.out.println("hutu ����������");
		}
	}
 

}
