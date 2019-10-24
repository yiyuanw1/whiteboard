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
import java.util.ArrayList;

import org.json.JSONObject;

public class ServerThread extends Thread{
	public Socket socket;
	public DataInputStream input ;
	public DataOutputStream output;
	public static ArrayList<String> previousgraph = new ArrayList<String>();
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
			  
			  JSONObject JO;
			  String action ="";
			  
			 
			
			  while(true){
				  //int value=input.read();

				  String data = input.readUTF();
				  JO = new JSONObject(data);
				  action = JO.getString("Action");
				  switch(action) {
				  case "Reply":{
					  String reply = JO.getString("Reply");
					  ServerThread client = SocketServer.list.get(SocketServer.list.size()-1);
					  client.output.writeUTF(reply); 
					  client.output.flush();
					  if(!reply.equals("Approve")) {		//close socket				  					  
						  SocketServer.list.remove(client);
						  client.socket.close();
					  }else {  //send previous graph
						  for(int i = 0; i < previousgraph.size();i++) {
							  client.output.writeUTF(previousgraph.get(i));
							  client.output.flush();
						  }
					  }

					  break;
				  }
				  case "Draw":{
					  previousgraph.add(data);
					  for (int i = 0; i <SocketServer.list.size(); i++) {
						  ServerThread clients =SocketServer.list.get(i);
						  if(clients!=this){
							  clients.output.writeUTF(data);
							  clients.output.flush();
						  }						
					  }
					  break;
				  }
				  case "Chat":{
					  for (int i = 0; i <SocketServer.list.size(); i++) {
						  ServerThread clients =SocketServer.list.get(i);
						  if(clients!=this){
							  clients.output.writeUTF(data);
							  clients.output.flush();
						  }
					  }
					  break;
				  }
				  case "Quit":{
					  for (int i = 0; i <SocketServer.list.size(); i++) {
						  ServerThread clients =SocketServer.list.get(i);
						  clients.output.writeUTF(data);
						  clients.output.flush();						  
					  }
					  System.out.println("client:" +SocketServer.map.get(socket)+" quit......");
					  SocketServer.list.remove(this);
					  SocketServer.map.remove(socket);
					  this.socket.close();
					  
					  
					  break;
				  }
				  default:
					  break;
				  }
			  }
		}catch (SocketException e) {
			try {
				SocketServer.list.remove(this);
				SocketServer.map.remove(socket);
				this.socket.close();				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
