package phase1.files;
import java.awt.image.BufferedImage;

public class ImageFile extends BufferedImage {
    private boolean isSaved = true;

    public ImageFile(int width, int height, int type) {
        super(width, height, type);
        this.getGraphics().fillRect(0, 0, width, height);
    }

    public void setIsSaved(boolean b) {
        this.isSaved = b;
    }

    public boolean isSaved() {
        return this.isSaved;
    }
}