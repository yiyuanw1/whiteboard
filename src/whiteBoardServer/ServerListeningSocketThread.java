/**
 * 
 */
package whiteBoardServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import ThreadPool.SocketThreadPipeStructure;
import ThreadPool.ThreadPool;

/**
 * @author sxy
 *
 */
public class ServerListeningSocketThread extends Thread {

	private ServerSocket serverListeningSocket;
	private Socket serverSocket;
	public static ThreadPool threadMaster;
	
	private ToClientThread toClientThread;

	
	public ServerListeningSocketThread(ServerSocket ss)
	{
		serverListeningSocket = ss;
		threadMaster = new ThreadPool();
	}
	
	
	public void run(){
		
		while(true) {
			try {	
				serverSocket = serverListeningSocket.accept();
				//为新thread，新建PipedWriter和PipedReader
				PipedWriter pw = new PipedWriter();
				PipedReader pr = new PipedReader();
				pw.connect(pr);
			
				//为新socket，新建thread
				toClientThread = new ToClientThread(serverSocket, pw);
			
				//构造socket thread PipedWriter PipedReader数据结构
				SocketThreadPipeStructure socketThreadPipeStructure = new SocketThreadPipeStructure(serverSocket, toClientThread, pw, pr);
			
				//结构加入pool
				threadMaster.add(socketThreadPipeStructure);

				//启动
				toClientThread.start();
			}
			 catch (IOException e) {
					SimpleDateFormat sdf = new SimpleDateFormat();
		            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
		            Date date = new Date();
					String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: " + e.getMessage() + "\n";
					mainServer.f.getChatBoard().getChatWindow().append(SYSinfo);
					
			}
		
		
		}
	}

}
