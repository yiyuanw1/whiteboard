package GUI;

import javax.swing.*;

import org.json.JSONObject;

import ThreadPool.SocketThreadPipeStructure;
import whiteBoardServer.ServerListeningSocketThread;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Text extends AbstractTool {
    private static Tool tool = null;

    private Text(Canvas frame) {
        super(frame);
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) tool = new Text(frame);
        return tool;
    }

    public void mousePressed(MouseEvent e) {
        super.mouseClicked(e);
        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            String text = JOptionPane.showInputDialog("Enter Text:");
            g.drawString(text, getX(), getY());
            
            //插入到此
            JSONObject circleObj = new JSONObject();
            circleObj.put("Action", "draw");
            circleObj.put("text", text);
            circleObj.put("x0", getX());
            circleObj.put("y0", getY());
            circleObj.put("shape", "Text");
            circleObj.put("color", g.getColor().hashCode());
            String circleS = circleObj.toString();
            
            
            //原来
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
            
            
            
            
            

            
    		//发给别人
    		//迭代每个socket
    		for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
    			SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
    			Socket iteraSocket = stps.getSocket();
    		
    			
    			try {
    				BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
    				messagetoClientWt.write(circleS + "\n");
    				messagetoClientWt.flush();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			
    			System.out.println("Response, " + circleS + ", has been sent!");
            
    		}
            
            
            
            
            
        }
    }
}