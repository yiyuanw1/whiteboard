package phase2.server;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread{
	public Socket socket;
	public DataInputStream input ;
	public DataOutputStream output;
	//public ObjectOutputStream objectout;
	public Graphics g;
	
	public ServerThread(Socket socket) {
		this.socket=socket;
	}
 
	public void run() {
		try {

			
			  input=new DataInputStream(socket.getInputStream()); 
			  output= new DataOutputStream(socket.getOutputStream()); 
			  //objectout = new ObjectOutputStream(socket.getOutputStream());
			 
			
			while(true){
				int value=input.read();

				for (int i = 0; i <SocketServer.list.size(); i++) {
					ServerThread clients =SocketServer.list.get(i);
					if(clients!=this){
						clients.output.write(value);
						clients.output.flush();
					}
					
				}
			}
		}catch (SocketException e) {
			try {
				SocketServer.list.remove(this);
				this.socket.close();
				System.out.println("client quit......");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
