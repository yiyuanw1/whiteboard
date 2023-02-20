/**
 * 
 */
package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
		//锛侊紒锛侀渶瑕佹洿澶氱殑鍒ゆ柇锛侊紒锛�
		if ( serverPort == 0)  throw new ClientException("The port could not be 0, please enter another number.");
		
		//鐢熸垚鏂扮殑socket
		(new Socket()).connect(new InetSocketAddress(IP, port), 2000);
		clientSocket = new Socket(serverIP, serverPort);
		
		//鍒嗛厤socket涓�涓猼hread
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
