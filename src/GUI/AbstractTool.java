package GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONObject;

import Client.mainClient;
import ThreadPool.SocketThreadPipeStructure;
import whiteBoardServer.ServerListeningSocketThread;
import whiteBoardServer.mainServer;

/**
 * Tool Abstract Class
 */
public abstract class AbstractTool implements Tool {

    private Canvas frame = null;

    public static int width = 0;

    public static int height = 0;

    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    private int pressX = -1;
    private int pressY = -1;

    public static Color color = Color.BLACK;
    
    public static int thick = 1;


    public AbstractTool(Canvas frame) {
        this.frame = frame;
        AbstractTool.width = frame.getBufferedImage().getWidth();
        AbstractTool.height = frame.getBufferedImage().getHeight();
    }

    public AbstractTool(Canvas frame, String icon) {
        this(frame);
        this.defaultCursor = ImageService.createCursor(icon);
    }


    public Canvas getFrame() {
        return this.frame;
    }




    public void setX(int x) {
        this.pressX = x;
    }

    public void setY(int y) {
        this.pressY = y;
    }

    public int getX() {
        return this.pressX;
    }

    public int getY() {
        return this.pressY;
    }


    public void mouseDragged(MouseEvent e) {

        Graphics g = getFrame().getPaintingSpace().getGraphics();
    	((Graphics2D) g).setStroke(new BasicStroke(thick));
        createShape(e, g);

    }


    public void mouseMoved(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Cursor cursor = this.defaultCursor;

        getFrame().getPaintingSpace().setCursor(cursor);
    }


    public void mouseReleased(MouseEvent e) {
        Graphics g = getFrame().getBufferedImage().getGraphics();
    	((Graphics2D) g).setStroke(new BasicStroke(thick));
        createShape(e, g);
        
        
        /*鍙戦��*/
        String fileName = this.toString();
        String shape = new String();
        for(int i = 0; i < fileName.length(); i++) {
        	if(fileName.charAt(i) == '.') {
        		char temp;
        		for(i++;(temp = fileName.charAt(i)) != '@'; i++) {
        			shape += temp;
        		}
        	}
        }

       
        
        JSONObject releaseObj = new JSONObject();
        releaseObj.put("Action", "draw");
        releaseObj.put("shape", shape);
        releaseObj.put("x0", getX());
        releaseObj.put("y0", getY());
        releaseObj.put("x1", e.getX());
        releaseObj.put("y1", e.getY());
        releaseObj.put("color", g.getColor().hashCode());
        String circleS = releaseObj.toString();
        
        if(!shape.equals("Eraser"))
			mainServer.graphArrayList.add(releaseObj);
        
		
		//鍙戠粰鍒汉
		//杩唬姣忎釜socket
        if( ServerListeningSocketThread.threadMaster == null ) {
        	Socket clientSocket = mainClient.clientStart.getClientSocket();
        	try {
        		BufferedWriter messagetoServerWt = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
        		messagetoServerWt.write(circleS + '\n');
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
					messagetoClientWt.write(circleS + "\n");
					messagetoClientWt.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println("Response, " + circleS + ", has been sent!");
	        
	
			}
		}
        
        
        setX(-1);
        setY(-1);

        getFrame().getPaintingSpace().repaint();
    }


    private void createShape(MouseEvent e, Graphics g) {
        // Position by the cursor is within the canvas
        if (getX() > 0 && getY() > 0
                && e.getX() > 0
                && e.getX() < AbstractTool.width
                && e.getY() > 0
                && e.getY() < AbstractTool.height) {

            g.drawImage(getFrame().getBufferedImage(), 0, 0,
                    AbstractTool.width, AbstractTool.height, null);

            g.setColor(AbstractTool.color);
            getFrame().getBufferedImage().setIsSaved(false);

            draw(g, getX(), getY(), e.getX(), e.getY());
        }
    }


    public void mousePressed(MouseEvent e) {
        if (e.getX() > 0 && e.getX() < AbstractTool.width && e.getY() > 0 && e.getY() < AbstractTool.height) {
            setX(e.getX());
            setY(e.getY());
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getX() > 0 && e.getX() < AbstractTool.width && e.getY() > 0 && e.getY() < AbstractTool.height) {
            setX(e.getX());
            setY(e.getY());
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    	int rotation = e.getWheelRotation();
		if( rotation <= 1 ) {
			thick += 1;
		}
		else if( rotation > 1 ){
			thick -= 1;
		}
		else {
		}

    }

    public void draw(Graphics g, int x1, int y1, int x2, int y2) { }


}