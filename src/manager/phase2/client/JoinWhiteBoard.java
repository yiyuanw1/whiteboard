package phase2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class JoinWhiteBoard {
	public static void main(String[] args)  
	{
		try {
			String username = "John";
			Socket socket = new Socket("localhost",9090);
			DataInputStream input=new DataInputStream(socket.getInputStream()); 
			DataOutputStream output= new DataOutputStream(socket.getOutputStream()); 
			if(socket!=null) {
				output.writeUTF(username);
				output.flush();
				output.writeUTF("join");
				output.flush();
				String message = input.readUTF();
				JOptionPane.showMessageDialog(null, message , "Message",JOptionPane.PLAIN_MESSAGE); 
				if(message.equals("Approve")) { 
			        ClientUI UI = new ClientUI(socket);
			        UI.initFrame();
				}
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
