package phase1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import phase2.client.ClientUI;

public class TestMain {
	public static void main(String[] args)  
	{
		try {
			String username = "Mark"; //arg[2]
			String ip ="localhost"; //arg[0]
			int port = 9090; //arg[1]
			Socket socket = new Socket(ip,port);
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
			        Canvas f = new Canvas(socket, username);
			        f.pack();
			        f.setVisible(true);

			        f.addWindowListener(new WindowAdapter() {
			            @Override
			            public void windowClosing(WindowEvent e) {

			                f.getService().save(false,f);
			                e.getWindow().dispose();
			                JSONObject JO = new JSONObject();
			                JO.put("Action", "Quit");
			                JO.put("Username", username);
			                try {
								output.writeUTF(JO.toString());
								output.flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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

