/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import Exception.ClientException;
import GUI.Canvas;

/**
 * @author sxy
 *
 */
public class mainClient {
	
	public static Canvas f = null;
	public static ClientStart clientStart;
	public static Object lock = new Object();
	public static String userName;
	public static InetAddress serverIP;
	public static int serverPort;
	
	public static void main(String[] args) {
        
		try {
			userName = "Jack";
			serverIP = InetAddress.getByName("127.0.0.1");
			serverPort = 9000;
			clientStart = new ClientStart(serverIP, serverPort, userName);
			
			
			int msgAnswer = JOptionPane.showConfirmDialog(null, "Do you want to join to the WhiteBoard?","Join to the WhiteBoard",JOptionPane.YES_NO_CANCEL_OPTION);
			if(msgAnswer != 0) {
				try {
					JOptionPane.showConfirmDialog(null, "See you next time!","Join to the WhiteBoard",JOptionPane.DEFAULT_OPTION);
					clientStart.getClientSocket().shutdownInput();
					clientStart.getClientSocket().shutdownOutput();
					clientStart.getClientSocket().close();
					clientStart.getClientSocketThread().interrupt();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
			}
			else {
				
					JSONObject requireJS = new JSONObject();
					requireJS.put("Action", "join");
					requireJS.put("userName", userName);
					requireJS.put("IP", InetAddress.getLocalHost().toString());
					String requireS = requireJS.toString();
					
					
					BufferedWriter messageToServerWt = new BufferedWriter(new OutputStreamWriter(clientStart.getClientSocket().getOutputStream(), "UTF-8"));
					messageToServerWt.write(requireS + "\n");
					messageToServerWt.flush();
					
					synchronized(lock) {
						while(clientStart.getClientSocketThread().getJoinState() == 100) {
							lock.wait();
						}
					}

					if(clientStart.getClientSocketThread().getJoinState() == 1) {
			        	f = new Canvas();
			        	f.initCanvas();
			            f.pack();
				        f.setVisible(true);
				        
				        //输出加入信息
				        SimpleDateFormat sdf = new SimpleDateFormat();
		                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
		                Date date = new Date();
						String welcome = "[" + sdf.format(date).toString() + "]" + " SYSTEM: You are permitted to join, enjoy!\n";
						mainClient.f.getChatBoard().getChatWindow().append(welcome);
						
						

				        f.addWindowListener(new WindowAdapter() {
				            @Override
				            public void windowClosing(WindowEvent e) {

				                f.getService().saveAs(false,f);
				                e.getWindow().dispose();
				                try {
									clientStart.getClientSocket().shutdownInput();
									clientStart.getClientSocket().shutdownOutput();
									clientStart.getClientSocket().close();
									clientStart.getClientSocketThread().interrupt();
								} catch (IOException e1) {
									System.out.println(e1.getMessage());
								}
								
				                
				            } });
			        	}
					else {
						JOptionPane.showConfirmDialog(null, "You are not permitted!","Join to the WhiteBoard",JOptionPane.DEFAULT_OPTION);
						clientStart.getClientSocket().shutdownInput();
			        	clientStart.getClientSocket().shutdownOutput();
			        	clientStart.getClientSocket().close();
			        	clientStart.getClientSocketThread().interrupt();
			        	System.exit(0);
					}
			}
			
			
		} catch (ClientException | IOException | InterruptedException e) {
		}
		
		

    }
	
	
}
