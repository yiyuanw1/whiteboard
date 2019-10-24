package phase1.tools;

import java.awt.event.MouseEvent;

/**
 * Interface for all the tools

 */
public interface Tool {

    public static final String FREE_TOOL = "FreeTool";
    public static final String ERASER_TOOL = "EraserTool";
    public static final String LINE_TOOL = "LineTool";
    public static final String RECT_TOOL = "RectTool";
    public static final String CIRCLE_TOOL = "CircleTool";
    public static final String OVAL_TOOL ="OvalTool" ;
    public static final String TEXT_TOOL ="TextTool" ;


    public void mouseDragged(MouseEvent e);


    public void mouseMoved(MouseEvent e);


    public void mouseReleased(MouseEvent e);


    public void mousePressed(MouseEvent e);


    public void mouseClicked(MouseEvent e);
    
    public void receiveData();
}