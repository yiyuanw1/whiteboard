package GUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import ThreadPool.SocketThreadPipeStructure;
import whiteBoardServer.ServerListeningSocketThread;
import whiteBoardServer.mainServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatBoard extends JPanel{

    private static final long serialVersionUID = 1L;

    // components for chat
    private JTextArea chatWindow;
    private JTextArea outMessage;
    private JButton btnSend;

    private Dimension size;

    private String text;

    public ChatBoard(int width, int height) {
        size = new Dimension(width, height);
        setLayout(new BorderLayout(0, 0));
        initialize();
    }

    private void initialize() {
        chatWindow = new JTextArea();
        chatWindow.setPreferredSize(new Dimension(size.width, size.height-50));
        chatWindow.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 6), new LineBorder(new Color(192, 192, 192), 6)));
        chatWindow.setLineWrap(true);
        chatWindow.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatWindow);
        this.add(chatScroll, BorderLayout.CENTER);

        JPanel sendingMessage = new JPanel();
        this.add(sendingMessage, BorderLayout.SOUTH);
        sendingMessage.setLayout(new BorderLayout(0,0));

        outMessage = new JTextArea();
        outMessage.setLineWrap(true);
        outMessage.setPreferredSize(new Dimension(size.width-20, 40));
        outMessage.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
        JScrollPane outMessageScroll = new JScrollPane(outMessage);
        sendingMessage.add(outMessageScroll, BorderLayout.CENTER);

        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                text = new String(outMessage.getText());
                outMessage.setText("");
                
              
                JSONObject chatObj = new JSONObject();
                chatObj.put("Action", "chat");
                chatObj.put("userName", mainServer.userName);
                chatObj.put("IP", mainServer.IP.toString());
                chatObj.put("message", text);
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
                Date date = new Date();
                chatObj.put("time", sdf.format(date).toString());
                String chatS = chatObj.toString();
  
                chatWindow.append("[" + sdf.format(date).toString() + "] " + mainServer.userName + "(" + mainServer.IP.toString()+ ") says: " + text + "\n");
                
                //发给别人
        		//迭代每个socket
        		for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
        			SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
        			Socket iteraSocket = stps.getSocket();
        		
        			try {
        				BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
        				messagetoClientWt.write(chatS + "\n");
        				messagetoClientWt.flush();
        			} catch (IOException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			System.out.println("Response, " + chatS + ", has been sent!");
                
        		}   
                
                
                
            }

        });
        sendingMessage.add(btnSend, BorderLayout.EAST);
    }
    
    public JTextArea getChatWindow() {
    	return chatWindow;
    }

}

