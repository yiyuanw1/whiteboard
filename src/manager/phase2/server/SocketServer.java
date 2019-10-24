package phase2.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.net.ServerSocketFactory;
import org.json.*;
import phase1.Canvas;

public class SocketServer {

	public ServerSocket server;
	public DataInputStream input;
	public DataOutputStream output;
	public Socket managerSocket;
	public String managerName;
	public static Map<Socket,String> map = new HashMap<Socket,String>();
	public static ArrayList<ServerThread> list = new ArrayList<ServerThread>();
	
	//public static Map<String,ArrayList<ServerThread>> project = new HashMap<String,ArrayList<ServerThread>>();
	
	public void startServer() {
		
		try {
			server = new ServerSocket(9090);
			System.out.println("Server is ready");
			while(true){
				Socket socket =server.accept();				
				
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				
				JSONObject JO = new JSONObject();

				String username = input.readUTF();
				String creator = input.readUTF();
				//creator or joiner
				if(creator.equals("create")) {
					if(!list.isEmpty()) {
						// 项目已存在
						output.writeUTF("Fail. Whiteboard has been created!");
						socket.close();
					} else {
						//创建成功
						System.out.println(username + " created white board....");
						output.writeUTF("Successfully");
						managerSocket = socket;
						managerName = username;
						ServerThread t = new ServerThread(socket);
						t.start();
						map.put(socket, username);
						list.add(t);
					}
				} else { //如果是申请加入“join"					
					if(!list.isEmpty()) { // 申请加入项目
						ServerThread t = new ServerThread(socket);
						t.start();
						list.add(t);
						map.put(socket, username);
						
						JO.put("Action", "Join");
						JO.put("Username", username);
						
						DataOutputStream toManager = new DataOutputStream(managerSocket.getOutputStream());
						toManager.writeUTF(JO.toString());
						toManager.flush();
							
		/*				if(reply.equals("Approve")) {		// 若同意加入 =======
							System.out.println(username + " join the white board... ");
							output.writeUTF("Approve");
							ServerThread t = new ServerThread(socket);
							t.start();
							list.add(t);
							map.put(username, socket);					
						} else { // 若不同意
							output.writeUTF("Refuse to join.");
							socket.close();
						}*/
					} else {// 不存在项目
						output.writeUTF("Not found");
						socket.close();
					}										
				} 
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
  
	}
	
}