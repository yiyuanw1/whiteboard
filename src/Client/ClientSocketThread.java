/**
 * 
 */
package Client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ClientException;

/**
 * @author sxy
 *
 */


//鐩戝惉socket
public class ClientSocketThread extends Thread {
	
	private Socket clientSocket;
	private BufferedReader messageFromServerRd; //input
	private int joinState;

	public ClientSocketThread(Socket csocket, String un) throws ClientException {
		clientSocket = csocket;
		joinState = 100;
		try {
			messageFromServerRd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		} catch (IOException e) {
			throw new ClientException(e.getMessage());
		}
	}
	
	public void run() {

			try {
				//鏀跺埌娑堟伅, draw, chat, successful, unsuccessful, kickOut
				String messageFromServer;
				while ((messageFromServer = messageFromServerRd.readLine()) != null){
	
				try {
					JSONObject messageFromServerObj = new JSONObject(messageFromServer);
					String tag = messageFromServerObj.getString("Action");
					switch(tag)
					{	
						case "system":{
							String message = messageFromServerObj.getString("message");
							mainClient.f.getChatBoard().getChatWindow().append(message);
						}
						case "welcome":{
							String userName = messageFromServerObj.getString("userName");
							if(!userName.equals(mainClient.userName)) {
								String IP = messageFromServerObj.getString("IP");
								String time = messageFromServerObj.getString("time");
								String welcome = "[" + time + "] SYSTEM: The user named " + userName + "(" + IP +")" +" is joined.\n";
								mainClient.f.getChatBoard().getChatWindow().append(welcome);
								}
							else {
								break;
							}
							break;
						}
						case "successful":{
								joinState = 1;
								synchronized(mainClient.lock) {
									mainClient.lock.notify();
									}
								break;
						}
						case "unsuccessful":{
							joinState = 0;
							synchronized(mainClient.lock) {
								mainClient.lock.notify();
								}
							break;
						}
						case "draw" :{
							if(mainClient.f == null) break;
							String shape = messageFromServerObj.getString("shape");
							Graphics g = mainClient.f.getBufferedImage().getGraphics();
							int thick = messageFromServerObj.getInt("thick");
							((Graphics2D) g).setStroke(new BasicStroke(thick));
							switch(shape) {
								case "Circle":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									int x1 = messageFromServerObj.getInt("x1");
									int y1 = messageFromServerObj.getInt("y1");
									int colorHashcode = messageFromServerObj.getInt("color");
									
									g.setColor(new Color(colorHashcode));

							        int radius = Math.min(Math.abs(x0-x1), Math.abs(y0-y1));
							        int x = (x0 > x1) ? x0-radius: x0;
							        int y = (y0 > y1) ? y0-radius: y0;
							        g.drawOval(x, y, radius, radius);
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								
								case "Rect":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									int x1 = messageFromServerObj.getInt("x1");
									int y1 = messageFromServerObj.getInt("y1");
									int colorHashcode = messageFromServerObj.getInt("color");

									g.setColor(new Color(colorHashcode));
									
									int x = Math.min(x1, x0);
							        int y = Math.min(y1, y0);
							        
							        g.drawRect(x, y, Math.abs(x0 - x1), Math.abs(y0 - y1));
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								
								case "Text":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									String text = messageFromServerObj.getString("text");
									int colorHashcode = messageFromServerObj.getInt("color");
									
									g.setColor(new Color(colorHashcode));
									
									g.drawString(text, x0, y0);
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								
								case "Oval":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									int x1 = messageFromServerObj.getInt("x1");
									int y1 = messageFromServerObj.getInt("y1");
									int colorHashcode = messageFromServerObj.getInt("color");
									
									g.setColor(new Color(colorHashcode));
									
									int x = Math.min(x1, x0);
								    int y = Math.min(y1, y0);

								    g.drawOval(x, y, Math.abs(x0 - x1), Math.abs(y0 - y1));
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								
								case "Line":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									int x1 = messageFromServerObj.getInt("x1");
									int y1 = messageFromServerObj.getInt("y1");
									int colorHashcode = messageFromServerObj.getInt("color");
									
									g.setColor(new Color(colorHashcode));
									
									g.drawLine(x0, y0, x1, y1);
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								
								case "Free":{
									int x0 = messageFromServerObj.getInt("x0");
									int y0 = messageFromServerObj.getInt("y0");
									int x1 = messageFromServerObj.getInt("x1");
									int y1 = messageFromServerObj.getInt("y1");
									int colorHashcode = messageFromServerObj.getInt("color");
									
									g.setColor(new Color(colorHashcode));
									
									g.drawLine(x0, y0, x1, y1);
							        mainClient.f.getPaintingSpace().repaint();
							        
									break;
								}
								case "Eraser":{
									if(messageFromServerObj.has("x")) {
										int x = messageFromServerObj.getInt("x");
										int y = messageFromServerObj.getInt("y");
										int x0 = messageFromServerObj.getInt("x0");
										int y0 = messageFromServerObj.getInt("y0");
										int x1 = messageFromServerObj.getInt("x1");
										int y1 = messageFromServerObj.getInt("y1");
										int size = messageFromServerObj.getInt("size");
										int colorHashcode = messageFromServerObj.getInt("color");

										g.setColor(new Color(colorHashcode));

							            g.drawLine(x0, y0, x1,y1);
							            
							            mainClient.f.getPaintingSpace().repaint();
									}else {
										break;
									}
									break;
								}
							}
							
							break;
						}
						case "chat" :{
							if(mainClient.f == null) break;
							
							String userName = messageFromServerObj.getString("userName");
							if(!userName.equals(mainClient.userName)) {
								String IP = messageFromServerObj.getString("IP");
								String message = messageFromServerObj.getString("message");
								String time = messageFromServerObj.getString("time");
								String summary = "[" + time + "] " + userName + "(" + IP + ") says: " + message + "\n";
								mainClient.f.getChatBoard().getChatWindow().append(summary);
								}
							else {
								break;
							}
							break;
						}

						case "kickOut":{
							if(mainClient.f == null) break;
							
							//*******寮瑰嚭瀵硅瘽妗嗏�滀綘琚玨ickOut鈥�/鎴栬�呰繛鎺ョ姸鎬佹樉绀�*********
							clientSocket.getInputStream().close();//鎹涓嶆姤閿�
							clientSocket.close();
							break;
						}
					}
				}catch (JSONException e) {}
				
			}
		}catch (IOException e){
			SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
            Date date = new Date();
			String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: " + e.getMessage() + "\n";
			mainClient.f.getChatBoard().getChatWindow().append(SYSinfo);
		}
			
			try {
				if(mainClient.f == null) {
					clientSocket.getInputStream().close();
					clientSocket.getOutputStream().close();
					clientSocket.close();//鎹涓嶆姤閿�
				}
				else {
					SimpleDateFormat sdf = new SimpleDateFormat();
		            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
		            Date date = new Date();
		            String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: The master is closed.\n";
		            mainClient.f.getChatBoard().getChatWindow().append(SYSinfo);
		            SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: The socket thread is interrupted.\n";
		            mainClient.f.getChatBoard().getChatWindow().append(SYSinfo);
					
					clientSocket.getInputStream().close();
					clientSocket.getOutputStream().close();
					clientSocket.close();//鎹涓嶆姤閿�
				}
			} catch (IOException e) {
				if(mainClient.f != null) {
					SimpleDateFormat sdf = new SimpleDateFormat();
					sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
					Date date = new Date();
					String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: " + e.getMessage() + "\n";
					mainClient.f.getChatBoard().getChatWindow().append(SYSinfo);
				}
			}
		}
	
	public int getJoinState() {
		return joinState;
	}
	
	
	
}

