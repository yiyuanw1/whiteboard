package phase2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class CreateWhiteBoard {
	public static void main(String[] args)  
	{
		try {
			String username = "Mark";
			Socket socket = new Socket("localhost",9090);
			DataInputStream input=new DataInputStream(socket.getInputStream()); 
			DataOutputStream output= new DataOutputStream(socket.getOutputStream()); 
			if(socket!=null) {
				output.writeUTF(username);
				output.flush();
				output.writeUTF("create");
				output.flush();
				String message = input.readUTF();
				JOptionPane.showMessageDialog(null, message , "Message",JOptionPane.PLAIN_MESSAGE); 
				if(message.equals("Successfully")) { 
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
