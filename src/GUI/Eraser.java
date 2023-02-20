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


public class Eraser extends AbstractTool {
    private static Tool tool = null;

    private Eraser(Canvas frame) {
        super(frame, "resource/erasercursor.gif");
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) {
            tool = new Eraser(frame);
        }
        return tool;
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        Graphics g = getFrame().getBufferedImage().getGraphics();
        ((Graphics2D) g).setStroke(new BasicStroke(thick));
        int x = 0;
        int y = 0;

        int size = 6;
        if (e.getX() > 0 && e.getY() > 0) {
            g.setColor(Color.WHITE);
            g.drawLine(getX(), getY(), e.getX(), e.getY());
            
            
            //插入到此
            JSONObject EraserObj = new JSONObject();
            EraserObj.put("Action", "draw");
            EraserObj.put("x", x);
            EraserObj.put("y", y);
            EraserObj.put("x0", getX());
            EraserObj.put("y0", getY());
            EraserObj.put("x1", e.getX());
            EraserObj.put("y1", e.getY());
            EraserObj.put("size", size);
            EraserObj.put("shape", "Eraser");
            EraserObj.put("color", g.getColor().hashCode());
            EraserObj.put("thick", thick);
            String EraserS = EraserObj.toString();
            
            
            setX(e.getX());
            setY(e.getY());
            getFrame().getPaintingSpace().repaint();
            
            mainServer.graphArrayList.add(EraserObj);
            
            //发给别人
    		//迭代每个socket
            if( ServerListeningSocketThread.threadMaster == null ) {
            	Socket clientSocket = mainClient.clientStart.getClientSocket();
            	try {
            		BufferedWriter messagetoServerWt = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            		messagetoServerWt.write(EraserS + '\n');
            		messagetoServerWt.flush();

            	} catch (IOException e1) {
            		System.out.println(e1.getMessage());
            	}
            }
            else{
            	for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
            		SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
            		Socket iteraSocket = stps.getSocket();

            		try {
            			BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
            			messagetoClientWt.write(EraserS + "\n");
            			messagetoClientWt.flush();
            		} catch (IOException e1) {
            			// TODO Auto-generated catch block
            			e1.printStackTrace();
            		}

            		System.out.println("Response, " + EraserS + ", has been sent!");

            	}
            }
            
        }
    }

}