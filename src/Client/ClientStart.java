/**
 * 
 */
package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import Exception.ClientException;

/**
 * @author sxy
 *
 */
public class ClientStart {
	
	private InetAddress serverIP;
	private int serverPort;
	private String userName;
	private Socket clientSocket;
	public ClientSocketThread clientSocketThread;
	


	public ClientStart(InetAddress IP, int port, String name) throws ClientException, IOException {
		
		serverIP = IP;
		serverPort = port;
		userName = name;
		//！！！需要更多的判断！！！
		if ( serverPort == 0)  throw new ClientException("The port could not be 0, please enter another number.");
		
		//生成新的socket
		clientSocket = new Socket(serverIP, serverPort);
		
		//分配socket一个thread
		clientSocketThread = new ClientSocketThread(clientSocket, userName);
		clientSocketThread.start();
		
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
	public ClientSocketThread getClientSocketThread() {
		return clientSocketThread;
	}

}
