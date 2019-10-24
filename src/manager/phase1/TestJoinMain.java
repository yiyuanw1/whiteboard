package phase1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import phase2.client.ClientUI;

public class TestJoinMain {
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
			        Canvas f = new Canvas(socket);
			        f.pack();
			        f.setVisible(true);

			        f.addWindowListener(new WindowAdapter() {
			            @Override
			            public void windowClosing(WindowEvent e) {

			                f.getService().save(false,f);
			                e.getWindow().dispose();
			            }
			        });
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