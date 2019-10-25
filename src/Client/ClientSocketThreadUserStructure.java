/**
 * 
 */
package Client;

import java.net.Socket;

/**
 * @author sxy
 *
 */
public class ClientSocketThreadUserStructure {

	private Socket csocket;
	private Thread clientThread;
	private String userName;
	
	public ClientSocketThreadUserStructure(Socket cs, Thread th, String un) {
		
		//连接成功，客户端启动socket，thread
		csocket = cs;
		clientThread = th;
		userName = un;
		}
		
		public Socket getSocket(){
			return csocket;
		}
		
		public Thread getThread() {
			return clientThread;
		}
		
		public String getUserName() {
			return userName;
		}

}
