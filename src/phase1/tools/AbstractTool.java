package phase1.tools;

import phase1.Canvas;
import phase1.files.ImageService;

import java.awt.*;
import java.awt.event.MouseEvent;

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


}