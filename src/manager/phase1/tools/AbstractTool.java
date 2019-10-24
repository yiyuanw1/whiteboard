package phase1.tools;

import phase1.Canvas;
import phase1.files.ImageService;
import phase2.client.ControlClient;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.json.JSONObject;

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
    
    public Socket socket;
    private String action;
    private String shape;


    public AbstractTool(Canvas frame,Socket s) {
        this.frame = frame;
        AbstractTool.width = frame.getBufferedImage().getWidth();
        AbstractTool.height = frame.getBufferedImage().getHeight();
        this.socket = s;
    }

    public AbstractTool(Canvas frame, String icon, Socket s) {
        this(frame,s);
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
        createShape(e, g);

        sendData(getX(), getY(), e.getX(), e.getY(), shape,color, "null");
        
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
        if (e.getX() > 0 && e.getX() < AbstractTool.width && e.getY() > 0
                && e.getY() < AbstractTool.height) {
            setX(e.getX());
            setY(e.getY());
        }
    }

    public void mouseClicked(MouseEvent e) { }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) { }

	public void receiveData(){
		new Thread(){
			public void run() {
				try {
				while(true){

					DataInputStream dis=new DataInputStream(socket.getInputStream());
					DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
						
					String inputStr = dis.readUTF();
					System.out.print("receive data");	
					JSONObject JO = new JSONObject(inputStr);

					action = JO.getString("Action");
					
					switch(action) {
					case "Join":{
						String joiner = JO.getString("Username");
						int approve = JOptionPane.showConfirmDialog(null, joiner+" want to join.", "New request",JOptionPane.YES_NO_OPTION);
						JO.put("Action", "Reply");
						if(approve==0) { //同意加入							
							JO.put("Reply","Approve");
							dos.writeUTF(JO.toString());								
						} else {
							JO.put("Reply","Refuse to join.");
							dos.writeUTF(JO.toString());
						}
						dos.flush();
						break;
					}
					case "Draw":{
						int x1,x2,y1,y2,colorhash;
						String shape;
						Graphics g = getFrame().getBufferedImage().getGraphics();
						x1 =   JO.getInt("X1");
						y1 =  JO.getInt("Y1");
						x2 =  JO.getInt("X2");
						y2 =  JO.getInt("Y2");
						colorhash = JO.getInt("Color");
						shape = JO.getString("Shape");
						
						System.out.printf("%d,%d,%d,%d\n",x1,y1,x2,y2);
						
						switch(shape) {
							case "Free":{
					            g.setColor(new Color(colorhash));
								g.drawLine(x1, y1, x2, y2);								
								break;
							}
							case "Line":{
					            g.setColor(new Color(colorhash));
								g.drawLine(x1, y1, x2, y2);
								break;
							}
							case "Circle":{
								g.setColor(new Color(colorhash));
						        int x = Math.min(x2, x1);
						        int y = Math.min(y2, y1);
						        if (Math.abs(x1-x2)>Math.abs(y1-y2)){
						            g.drawOval(x, y, Math.abs(x1 - x2), Math.abs(x1 - x2));
						        }else{
						            g.drawOval(x, y, Math.abs(y1 - y2), Math.abs(y1 - y2));
						        }
								break;
							}
							case "Oval":{
								g.setColor(new Color(colorhash));
						        int x = Math.min(x2, x1);
						        int y = Math.min(y2, y1);

						        g.drawOval(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));
							    break;
							}
							case "Rect":{
								g.setColor(new Color(colorhash));
						        int x = Math.min(x2, x1);
						        int y = Math.min(y2, y1);
						        g.drawRect(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));
							    break;
							}
							case "Eraser":{
						        int x = 0;
						        int y = 0;

						        int size = 6;
						        if (x1 > 0 && y1 > 0) {
						            g.setColor(Color.WHITE);
						            x = ((x2 - x1) > 0) ? x1 : x2;
						            y = ((y2 - y1) > 0) ? y1 : y2;
						            g.fillRect(x, y, Math.abs(x2 - x1)
						                    + size, Math.abs(y2 - y1) + size);
						        }
						        break;
							}
							case "Text":{
								g.setColor(new Color(colorhash));
								String text = JO.getString("Text");
								if(!text.equals("")) {
									g.drawString(text, x1, y1);
								}					            
							}
							
							default:
								break;
						}
						getFrame().getPaintingSpace().repaint();
						break;
					}
					case "Quit":{
						String name = JO.getString("Username");
						frame.chatBoard.chatWindow.append(name+" exited the project\n");
						if(name.equals(Canvas.username)) {
							socket.close();
						}
						break;
					}
					case "Chat":{
						String name = JO.getString("Username");
						String text = JO.getString("Message");
						frame.chatBoard.chatWindow.append(name+": "+text+"\n");
						System.out.print(inputStr);
						break;
					}
					
					default:
						break;
					}
					
				}
				} catch (Exception e) {							
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public void sendData(int x1,int y1,int x2,int y2, String shape, Color color,String text){
		try {

			DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
			//写图形类型
			
			JSONObject JO = new JSONObject();
			JO.put("Action","Draw");
			//JO.put("Type", type);
			JO.put("X1", x1);
			JO.put("X2", x2);
			JO.put("Y1", y1);
			JO.put("Y2", y2);
			JO.put("Shape", shape);
			JO.put("Color",color.hashCode());
			JO.put("Text", text);
			//JO.put("Color", g.getColor().getRGB());
			
			dos.writeUTF(JO.toString());
			dos.flush();
	//		oos.writeObject(g);
	//		oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setShape(String s) {
		this.shape = s;
	}

}