package GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONObject;

import Client.mainClient;
import ThreadPool.SocketThreadPipeStructure;
import whiteBoardServer.ServerListeningSocketThread;
import whiteBoardServer.mainServer;


public class Free extends AbstractTool {
    private static Tool tool = null;

    private Free(Canvas frame) {
        super(frame, "resource/freecursor.gif");
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) {
            tool = new Free(frame);
        }
        return tool;
    }


    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        Graphics g = getFrame().getBufferedImage().getGraphics();
        if (getX() > 0 && getY() > 0) {
            g.setColor(color);
            g.drawLine(getX(), getY(), e.getX(), e.getY());
            
            
            
            //插入到此
            JSONObject freeObj = new JSONObject();
            freeObj.put("Action", "draw");
            freeObj.put("x0", getX());
            freeObj.put("y0", getY());
            freeObj.put("x1", e.getX());
            freeObj.put("y1", e.getY());
            freeObj.put("shape", "Free");
            freeObj.put("color", g.getColor().hashCode());
            String freeS = freeObj.toString();
            
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
            
            mainServer.graphArrayList.add(freeObj);
          //发给别人
    		//迭代每个socket
            if( ServerListeningSocketThread.threadMaster == null ) {
            	Socket clientSocket = mainClient.clientStart.getClientSocket();
            	try {
            		BufferedWriter messagetoServerWt = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            		messagetoServerWt.write(freeS + '\n');
            		messagetoServerWt.flush();

            	} catch (IOException e1) {
            		System.out.println(e1.getMessage());
            	}
            }
            else {
            	for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
            		SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
            		Socket iteraSocket = stps.getSocket();

            		try {
            			BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
            			messagetoClientWt.write(freeS + "\n");
            			messagetoClientWt.flush();
            		} catch (IOException e1) {
            			// TODO Auto-generated catch block
            			e1.printStackTrace();
            		}

            		System.out.println("Response, " + freeS + ", has been sent!");

            	}

            }
            
        }
    }
}