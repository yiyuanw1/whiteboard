/**
 * 
 */
package whiteBoardServer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import Exception.ServerException;
import ThreadPool.SocketThreadPipeStructure;

/**
 * @author sxy
 *
 */
public class WhiteBoardServer {

		private InetAddress serverIP;
		private int serverPort;
		private String serverName;
		private int backlog;
		private ServerSocket serverListeningSocket;
		private ServerListeningSocketThread serverSocketThread;
		private int msgAnswer= 100;
		
		public static Object Pipelock = new Object();
		
		
//		private ThreadPool threadMaster;
		
		
		
		//浠庢枃鏈鑾峰彇
		//鎶妔ystemOut鏀瑰埌textField
		public WhiteBoardServer(String name, InetAddress IP, int port, int bl) throws ServerException, IOException, InterruptedException {
			
			this.serverIP = IP;
			this.serverPort = port;
			this.serverName = name;
			this.backlog = bl;
			
			if ( port == 0)  throw new ServerException("The port could not be 0, please enter another number.");
			//鐢熸垚listeningServerSocket
			serverListeningSocket = new ServerSocket(serverPort, backlog, serverIP);
			
			
			//鍒嗛厤listening涓�涓猼hread
			serverSocketThread = new ServerListeningSocketThread(serverListeningSocket);
			serverSocketThread.start();
			
			//绛夊緟PipedReader鏄惁ready
			while(true)
			{
				//System.out.println("The size is " + ServerListeningSocketThread.threadMaster.size());
/*
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	*/			
				
				synchronized(Pipelock) {
						 Pipelock.wait();
				}

				
				for(int index = 0; index < ServerListeningSocketThread.threadMaster.size(); index++)
				{
					PipedReader pr = ServerListeningSocketThread.threadMaster.get(index).getHigherReader();
					
					//濡傛灉鏈夊鎴风鏁版嵁浼犱笂鏉�
					while((index < ServerListeningSocketThread.threadMaster.size()) && pr.ready()) {
						
						//璇诲彇鎵�鏈変紶涓婃潵鐨勬暟鎹�, 骞惰浆鎴怞SONObject
						char readChar;
						String cbufS = new String();
						while((readChar = (char) pr.read())!= '\n')
							cbufS += readChar;
						
						JSONObject cbufJSON = new JSONObject(cbufS);
						
						//System.out.println(" The Receive is " + cbufS);
						
						//鑾峰彇淇℃伅鏍囧織(chat, draw, kickOut)
						String messageFlag = cbufJSON.getString("Action");
						
						switch(messageFlag) {
						
						case "join":{
							
							msgAnswer = JOptionPane.showConfirmDialog(null, "Want to join to the WhiteBoard, yes or no?","Join to the WhiteBoard",JOptionPane.YES_NO_CANCEL_OPTION);
							Socket skt = ServerListeningSocketThread.threadMaster.get(index).getSocket();
							BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream(), "UTF-8"));
							
							if(msgAnswer == 0) {
								
								String userName = cbufJSON.getString("userName");
								String IPClient = cbufJSON.getString("IP");
								ServerListeningSocketThread.threadMaster.get(index).setUserName(userName);
								ServerListeningSocketThread.threadMaster.get(index).setIP(IPClient);
								ServerListeningSocketThread.threadMaster.notifyAdd(userName);
								
								JSONObject successfulJobj = new JSONObject();
								successfulJobj.put("Action", "successful");
								String successfulS = successfulJobj.toString();
								
								messagetoClientWt.write(successfulS + '\n');
								messagetoClientWt.flush();
								
								//缁欒嚜宸辨樉绀篣ser鍔犲叆
								SimpleDateFormat sdf = new SimpleDateFormat();
				                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
				                Date date = new Date();
				                String welcome = "[" + sdf.format(date).toString() + "] SYSTEM: The user named " + userName + "(" + IPClient +")" +" is joined.\n";
				                mainServer.f.getChatBoard().getChatWindow().append(welcome);
								
				                //鍛婅瘔鍒汉User鍔犲叆
				                JSONObject welcomeObj = new JSONObject();
				                welcomeObj.put("Action", "welcome");
				                welcomeObj.put("userName", userName);
				                welcomeObj.put("IP", IPClient);
				                welcomeObj.put("time", sdf.format(date).toString());
				                String welcomeS = welcomeObj.toString();
				                
								int socketNum = ServerListeningSocketThread.threadMaster.size();//socketNumber
								//杩唬姣忎釜socket
								for(int i = 0; i < socketNum; i++) {
									SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
									Socket iteraSocket = stps.getSocket();
								
									messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
									messagetoClientWt.write(welcomeS + "\n");
									messagetoClientWt.flush();
								}
				  
				                
								Thread.sleep(200);
								for(int i = 0; i < mainServer.graphArrayList.size(); i++) {
									System.out.println(mainServer.graphArrayList.get(i).toString());
									JSONObject historyDraw = mainServer.graphArrayList.get(i);
									messagetoClientWt.write(historyDraw.toString() + '\n');
									messagetoClientWt.flush();
								}
								
							}
							else {
								JSONObject unsuccessfulJobj = new JSONObject();
								unsuccessfulJobj.put("Action", "unsuccessful");
								String unsuccessfulS = unsuccessfulJobj.toString();
								
								messagetoClientWt.write(unsuccessfulS + '\n');
								messagetoClientWt.flush();
								
								//鍏抽棴鏈湴socket鐨刼utStream(鎹鍐嶅叧socket涓嶆姤閿�)
								skt.getOutputStream().close();
								//鍏抽棴socket
								skt.close();
								//shutdown Thread
								try {
									Thread thrd = ServerListeningSocketThread.threadMaster.get(index).getThread();
									thrd.interrupt();
									ServerListeningSocketThread.threadMaster.remove(index);
								}
								catch(SecurityException e) {
									//鍒楄〃鍒犻櫎
									ServerListeningSocketThread.threadMaster.remove(index);
								}
								
								
							}
							break;
						}
						
						/*case "newUser":{
	
							String userName = cbufJSON.getString("userName");
	
							int socketNum = ServerListeningSocketThread.threadMaster.size();//socketNumber
							SocketThreadPipeStructure lastStru = ServerListeningSocketThread.threadMaster.get(socketNum - 1);
							lastStru.setUserName(userName);
							break;
						}
						*/
						case "kickOut":{

							String userName = cbufJSON.getString("userName");

							int socketNum = ServerListeningSocketThread.threadMaster.size();//socketNumber
							//杩唬姣忎釜socket
							for(int i = 0; i < socketNum; i++) {
								SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
								if(userName == stps.getUserName()) {
									
									//鍏堝彂娑堟伅鍛婅瘔client琚玨icked
									Socket skt = stps.getSocket();
									BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream(), "UTF-8"));
									messagetoClientWt.write("You are kicked out!!!" + "\n");
									messagetoClientWt.flush();
									
									//鍏抽棴鏈湴socket鐨刼utStream(鎹鍐嶅叧socket涓嶆姤閿�)
									skt.getOutputStream().close();
									
									//鍏抽棴socket
									skt.close();
									ServerListeningSocketThread.threadMaster.notifyQuit(userName);
									//shutdown Thread
									try {
										stps.getThread().interrupt();
									}
									catch(SecurityException e) {
										//鍒楄〃鍒犻櫎
										ServerListeningSocketThread.threadMaster.remove(i);
									}
								}
							}
							break;
						}
						
						
						case "draw":{
							String shapeS = cbufJSON.getString("shape");
							int thick = cbufJSON.getInt("thick");
							Graphics g = mainServer.f.getBufferedImage().getGraphics();
							((Graphics2D) g).setStroke(new BasicStroke(thick));
							switch(shapeS) {
								case "Circle":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									int x1 = cbufJSON.getInt("x1");
									int y1 = cbufJSON.getInt("y1");
									int colorHashcode = cbufJSON.getInt("color");

									g.setColor(new Color(colorHashcode));

							        int radius = Math.min(Math.abs(x0-x1), Math.abs(y0-y1));
							        int x = (x0 > x1) ? x0-radius: x0;
							        int y = (y0 > y1) ? y0-radius: y0;
							        g.drawOval(x, y, radius, radius);
							        mainServer.f.getPaintingSpace().repaint();
							        
							        
							        //鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
									break;
									}
								case "Text":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									String text = cbufJSON.getString("text");
									int colorHashcode = cbufJSON.getInt("color");
									
									g.setColor(new Color(colorHashcode));
									
									g.drawString(text, x0, y0);
									mainServer.f.getPaintingSpace().repaint();
									
									//鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
									
									break;
								}
								
								case "Rect":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									int x1 = cbufJSON.getInt("x1");
									int y1 = cbufJSON.getInt("y1");
									int colorHashcode = cbufJSON.getInt("color");

									g.setColor(new Color(colorHashcode));
									
									int x = Math.min(x1, x0);
								    int y = Math.min(y1, y0);
								    
								    g.drawRect(x, y, Math.abs(x0 - x1), Math.abs(y0 - y1));
							        mainServer.f.getPaintingSpace().repaint();
							        
							        
							      //鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
							        
							        
									break;
								}
								
								case "Oval":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									int x1 = cbufJSON.getInt("x1");
									int y1 = cbufJSON.getInt("y1");
									int colorHashcode = cbufJSON.getInt("color");

									g.setColor(new Color(colorHashcode));
									
									int x = Math.min(x1, x0);
								    int y = Math.min(y1, y0);
								    
								    g.drawOval(x, y, Math.abs(x0 - x1), Math.abs(y0 - y1));
							        mainServer.f.getPaintingSpace().repaint();
							        
							      //鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
							        
									break;
								}
								
								case "Line":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									int x1 = cbufJSON.getInt("x1");
									int y1 = cbufJSON.getInt("y1");
									int colorHashcode = cbufJSON.getInt("color");

									g.setColor(new Color(colorHashcode));
									
									g.drawLine(x0, y0, x1, y1);
							        mainServer.f.getPaintingSpace().repaint();
							        
							      //鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
							        
									break;
								}
								
								case "Free":{
									//棣栧厛鍙戠粰鑷繁
									int x0 = cbufJSON.getInt("x0");
									int y0 = cbufJSON.getInt("y0");
									int x1 = cbufJSON.getInt("x1");
									int y1 = cbufJSON.getInt("y1");
									int colorHashcode = cbufJSON.getInt("color");

									g.setColor(new Color(colorHashcode));
									
									g.drawLine(x0, y0, x1, y1);
							        mainServer.f.getPaintingSpace().repaint();
							        
							        //鍙戠粰鍒汉
									//杩唬姣忎釜socket
									for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
										SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
										Socket iteraSocket = stps.getSocket();
									
										BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
										messagetoClientWt.write(cbufS + "\n");
										messagetoClientWt.flush();
										//System.out.println("Response, " + cbufS + ", has been sent!");
										}
							        
									break;
								}
								case "Eraser":{
									if(cbufJSON.has("x")) {
										//棣栧厛鍙戠粰鑷繁
										int x = cbufJSON.getInt("x");
										int y = cbufJSON.getInt("y");
										int x0 = cbufJSON.getInt("x0");
										int y0 = cbufJSON.getInt("y0");
										int x1 = cbufJSON.getInt("x1");
										int y1 = cbufJSON.getInt("y1");
										int size = cbufJSON.getInt("size");
										int colorHashcode = cbufJSON.getInt("color");

										g.setColor(new Color(colorHashcode));

							            g.drawLine(x0, y0, x1,y1);
								        mainServer.f.getPaintingSpace().repaint();
								        
								        //鍙戠粰鍒汉
										//杩唬姣忎釜socket
										for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
											SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
											Socket iteraSocket = stps.getSocket();
										
											BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
											messagetoClientWt.write(cbufS + "\n");
											messagetoClientWt.flush();
											//System.out.println("Response, " + cbufS + ", has been sent!");
											}
									}else {
										break;
									}
									break;
								}
								
								
							}
							

							
							/*draw de break*/
							break;
						}
							    
						case "chat":{
							/*涓嬪彂缁欐瘡涓猻ocket锛堝箍鎾級*/
							//鑾峰彇杩炴帴鍒板鎴风鐨剆ocket鏁伴噺
							String userName = cbufJSON.getString("userName");
							String Ip = cbufJSON.getString("IP");
							String message = cbufJSON.getString("message");
							String time = cbufJSON.getString("time");
							String summary = "[" + time + "] " + userName + "(" + IP + ") says: " + message + "\n";
							
							mainServer.f.getChatBoard().getChatWindow().append(summary);
							
							int socketNum = ServerListeningSocketThread.threadMaster.size();//socketNumber
							//杩唬姣忎釜socket
							for(int i = 0; i < socketNum; i++) {
								SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
								Socket iteraSocket = stps.getSocket();
							
								BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
								messagetoClientWt.write(cbufS + "\n");
								messagetoClientWt.flush();
							}
							break;
						}
							
						}
						
					
					}
				}
			}
			
		}
		
		
		public InetAddress getServerIP(){
			return serverIP;
		}
		
		public String getServerName(){
			return serverName;
		}
		
		public int getServerPort(){
			return serverPort;
		}
		
		public ServerSocket getLiseningSocket() {
			return serverListeningSocket;
		}
		
		public ServerListeningSocketThread getServerListeningSocketThread() {
			return serverSocketThread;
		}
		
			
	}
		

	        
			
			



