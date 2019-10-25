/**
 * 
 */
package ThreadPool;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sxy
 *
 */
public class SocketThreadPipeStructure {
	
		private Socket ssocket;
		private Thread serverThread;
		private String userName;
		private String IP;
		private PipedWriter lowerWriter;
		private PipedReader higherReader;
		
		//只有服务器端listening socket调用
/*		public SocketThreadArrayList(Socket sc, Thread th, String un )
		{
			socket = sc;
			thread = th;
			userName = un;
		}
*/
		//连接成功，服务器启动新的socket，PipedWriter， PipedReader
		public SocketThreadPipeStructure(Socket ss, Thread th, PipedWriter pw, PipedReader pd)
		{
			ssocket = ss;
			serverThread = th;
			lowerWriter = pw;
			higherReader = pd;
			userName = null;
			IP = null;
		}
		
		public PipedReader getHigherReader(){
			return higherReader;
		}
		
		public Socket getSocket(){
			return ssocket;
		}
		
		public Thread getThread() {
			return serverThread;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public String getIP() {
			return IP;
		}
		
		public void setIP(String str) {
			IP = str;
		}
		
		public void setUserName(String str) {
			userName = str;
		}

}
