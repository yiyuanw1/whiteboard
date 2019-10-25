package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

import static java.awt.Color.*;


public class Canvas extends JFrame {

    private ImageService service = new ImageService();

    private Dimension screenSize = service.getScreenSize();

    private JPanel paintingSpace = createPaintingSpace();

    private ImageFile bufferedImage = new ImageFile(
            (int) screenSize.getWidth() / 3, (int) screenSize.getHeight() / 2,
            BufferedImage.TYPE_INT_RGB);

    private Tool tool = null;

    Graphics g = bufferedImage.getGraphics();

    private JPanel currentColorPanel = null;
    
    //added
    public int msgAnswer;

    ActionListener menuListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            service.menuFunctions(Canvas.this, e.getActionCommand());
        }
    };

    private JScrollPane scroll = null;

    JPanel toolsPanel = createToolsPanel();

    JPanel colorsPanel = createColorsPanel();

    ChatBoard chatBoard = new ChatBoard((int) screenSize.getHeight() / 4, (int) screenSize.getHeight() / 2);


    public Canvas() {
        super();
    }

    public ImageService getService() {
        return this.service;
    }


    public void initCanvas() {
        this.setTitle("Shared Whiteboard - HONE");
        service.initPaintingSpace(this);
        tool = ToolInstances.getToolInstance(this, Tool.FREE_TOOL);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

            public void mousePressed(MouseEvent e) { tool.mousePressed(e); }

            public void mouseClicked(MouseEvent e) {
                tool.mouseClicked(e);
            }
            
        };
        
        MouseWheelListener mouseWheelListener = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				tool.mouseWheelMoved(e);
			}
        	
        };

        paintingSpace.addMouseMotionListener(mouseMotionListener);
        paintingSpace.addMouseListener(mouseListener);
        paintingSpace.addMouseWheelListener(mouseWheelListener);

        scroll = new JScrollPane(paintingSpace);
        ImageService.setViewport(scroll, paintingSpace, bufferedImage.getWidth(),
                bufferedImage.getHeight());

        this.add(scroll, BorderLayout.CENTER);
        this.add(toolsPanel, BorderLayout.WEST);
        this.add(colorsPanel, BorderLayout.SOUTH);
        this.add(chatBoard,BorderLayout.EAST);
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

    public JPanel createPaintingSpace() {
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
        String[] toolsarray = { Tool.FREE_TOOL, Tool.ERASER_TOOL, Tool.LINE_TOOL,
        		Tool.RECT_TOOL, Tool.CIRCLE_TOOL, Tool.OVAL_TOOL, Tool.TEXT_TOOL };
        for (int i = 0; i < toolsarray.length; i++) {
            JButton button = new JButton(new ToolAndColorAction(new ImageIcon("resource/"+ toolsarray[i] + ".jpg"), toolsarray[i], this));
            toolBar.add(button);

        }
        
        /*JButton button = new JButton(new ImageIcon("resoruce/color.jpg"));
        button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				(new ColorFrame()).setVisible(true);
			}
        	
        });
        toolBar.add(button);*/
        panel.add(toolBar);
        return panel;
    }


    public class DrawSpace extends JPanel {

        public void paint(Graphics g) {
            service.repaint(g, bufferedImage);
        }
    }


	public ChatBoard getChatBoard() {
		
		return chatBoard;
	}

}