package phase1;

import phase1.chat.ChatBoard;
import phase1.files.ImageFile;
import phase1.files.ImageService;
import phase1.tools.Tool;
import phase1.tools.ToolInstances;
import phase2.client.ControlClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.Socket;

import static java.awt.Color.*;
import static phase1.tools.Tool.*;


public class Canvas extends JFrame {

	public static Socket clientsocket;
	public static String username;
	
	private ImageService service = new ImageService();

    private Dimension screenSize = service.getScreenSize();

    private JPanel paintingSpace = createDrawSpace();

    private ImageFile bufferedImage = new ImageFile(
            (int) screenSize.getWidth() / 3, (int) screenSize.getHeight() / 2,
            BufferedImage.TYPE_INT_RGB);

    private Tool tool = null;

    Graphics g = bufferedImage.getGraphics();

    private JPanel currentColorPanel = null;

    ActionListener menuListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            service.menuFunctions(Canvas.this, e.getActionCommand());
        }
    };

    private JScrollPane scroll = null;

    JPanel toolsPanel = createToolsPanel();

    JPanel colorsPanel = createColorsPanel();    

    public ChatBoard chatBoard;
    
    public Canvas(Socket s, String username) {
        super();
        Canvas.clientsocket = s;
        this.username = username;
        chatBoard = new ChatBoard((int) screenSize.getHeight() / 4, (int) screenSize.getHeight() / 2,username);
        initCanvas();
    }

    public ImageService getService() {
        return this.service;
    }


    public void initCanvas() {
        this.setTitle("Shared Whiteboard - HONE");
        service.initPaintingSpace(this);
        tool = ToolInstances.getToolInstance(this, FREE_TOOL, clientsocket);
        chatBoard.setSocket(clientsocket);

        MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                tool.mouseDragged(e);
            }

            public void mouseMoved(MouseEvent e) {
                tool.mouseMoved(e);
            }
        };
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                tool.mouseReleased(e);
            }

            public void mousePressed(MouseEvent e) {
                tool.mousePressed(e);
            }

            public void mouseClicked(MouseEvent e) {
                tool.mouseClicked(e);
            }
        };

        paintingSpace.addMouseMotionListener(mouseMotionListener);
        paintingSpace.addMouseListener(mouseListener);

        createMenuBar();
        scroll = new JScrollPane(paintingSpace);
        ImageService.setViewport(scroll, paintingSpace, bufferedImage.getWidth(),
                bufferedImage.getHeight());

        this.add(scroll, BorderLayout.CENTER);
        this.add(toolsPanel, BorderLayout.WEST);
        this.add(colorsPanel, BorderLayout.SOUTH);
        this.add(chatBoard,BorderLayout.EAST);
        tool.receiveData();
    }


    public JPanel getPaintingSpace() {
        return this.paintingSpace;
    }


    public JScrollPane getScroll() {
        return this.scroll;
    }


    public ImageFile getBufferedImage() {
        return this.bufferedImage;
    }


    public void setBufferedImage(ImageFile bufferedImage) {
        this.bufferedImage = bufferedImage;
    }


    public void setTool(Tool tool) {
        this.tool = tool;
    }


    public Tool getTool() {
        return this.tool;
    }



    public JPanel createColorsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JToolBar toolBar = new JToolBar("Color");
        toolBar.setFloatable(false);
        toolBar.setMargin(new Insets(2, 2, 2, 2));
        toolBar.setLayout(new GridLayout(2, 3, 2, 2));
        Color[] colorsArray = { BLACK, BLUE, GREEN, RED, YELLOW ,PINK};
        String[] colorNames = { "BLACK", "BLUE", "GREEN", "RED", "YELLOW","PINK" };
        JButton[] panelArray = new JButton[colorsArray.length];

        currentColorPanel = new JPanel();
        currentColorPanel.setBackground(Color.BLACK);
        currentColorPanel.setPreferredSize(new Dimension(20, 20));
        // Create color buttons
        for (int i = 0; i < panelArray.length; i++) {
            panelArray[i] = new JButton(new ToolAndColorAction(colorsArray[i],
                    currentColorPanel));
            panelArray[i].setBackground(colorsArray[i]);
            panelArray[i].setText(colorNames[i]);

            toolBar.add(panelArray[i]);
        }
        panel.add(currentColorPanel);
        panel.add(toolBar);
        return panel;
    }


    public void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        String[] menuArr = { "File","Management"  };
        String[][] menuItemArr = { { "New", "Open", "Save", "Save As", "Exit" },{"Remove Member","Show Members List"}};
        for (int i = 0; i < menuArr.length; i++) {
            JMenu menu = new JMenu(menuArr[i]);
            for (int j = 0; j < menuItemArr[i].length; j++) {
                JMenuItem menuItem = new JMenuItem(menuItemArr[i][j]);
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }
            menuBar.add(menu);
        }
        this.setJMenuBar(menuBar);
    }


    public JPanel createDrawSpace() {
        JPanel drawSpace = new DrawSpace();
        drawSpace.setPreferredSize(new Dimension((int) screenSize.getWidth(),
                (int) screenSize.getHeight() - 150));
        return drawSpace;
    }


    public JPanel createToolsPanel() {
        JPanel panel = new JPanel();
        JToolBar toolBar = new JToolBar("Tool");
        toolBar.setOrientation(toolBar.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setMargin(new Insets(2, 2, 2, 2));
        toolBar.setLayout(new GridLayout(6, 1, 2, 2));
        String[] toolsarray = { FREE_TOOL, ERASER_TOOL, LINE_TOOL,
                RECT_TOOL, CIRCLE_TOOL, OVAL_TOOL,TEXT_TOOL };
        for (int i = 0; i < toolsarray.length; i++) {
            JButton button = new JButton(new ToolAndColorAction(new ImageIcon("resource/"+ toolsarray[i] + ".jpg"), toolsarray[i], this));
            toolBar.add(button);

        }
        panel.add(toolBar);
        return panel;
    }


    public class DrawSpace extends JPanel {

        public void paint(Graphics g) {
            service.repaint(g, bufferedImage);
        }
    }

}