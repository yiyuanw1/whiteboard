/**
 * 
 */
package whiteBoardServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import Exception.ClientException;
import Exception.ServerException;
import GUI.Canvas;
import GUI.ManagerCanvas;
import GUI.ServerLoginPage;
import ThreadPool.SocketThreadPipeStructure;

/**
 * @author sxy
 *
 */
public class mainServer {

	public static ManagerCanvas f;
	public static WhiteBoardServer wbs;
	public static String userName;
	public static InetAddress IP;
	public static ArrayList<JSONObject> graphArrayList = new ArrayList<JSONObject>();
	public static ServerLoginPage login;
	public static int port;
	public static Object loginLock = new Object();
	
	private static String IPS;
	private static String portS;

public static void main(String[] args) {
	
	login = new ServerLoginPage();
	login.pack();

	synchronized(loginLock) {
		try {
			while(login.getLoginIp() == null &&  login.getLoginPort() == null && login.getLoginName() == null) {
				loginLock.wait();
				IPS = login.getLoginIp();
				portS = login.getLoginPort();
				userName = login.getLoginName();
			}
		} catch (InterruptedException e1) {
			System.out.println(e1.getMessage());
		}
	}

	f = new ManagerCanvas();
	f.initCanvas();
    f.pack();
    f.setVisible(true);
    
	try {
		IP = InetAddress.getByName(IPS);
		port = Integer.parseInt(portS);
		wbs = new WhiteBoardServer(userName, IP, port, 10);
	} catch (ServerException | IOException | InterruptedException e2) {
		System.out.println(e2.getMessage());
	}
		
    
   

    f.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {

            f.getService().saveAs(false,f);
            e.getWindow().dispose();
            
            int socketNum = ServerListeningSocketThread.threadMaster.size();
            for(int i = 0; i < socketNum; i++) {
            	Socket skt = ServerListeningSocketThread.threadMaster.get(i).getSocket();
            	Thread thd = ServerListeningSocketThread.threadMaster.get(i).getThread();
            	try {
					skt.shutdownInput();
					skt.shutdownOutput();
					skt.close();
					thd.interrupt();
				} catch (IOException e1) {
				}
            	
            	try {
					wbs.getLiseningSocket().close();
					wbs.getServerListeningSocketThread().interrupt();
				} catch (IOException e1) {
				}
            	
            }
            
            
        }
    });

}


}
