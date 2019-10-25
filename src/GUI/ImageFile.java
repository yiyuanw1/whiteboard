package GUI;
import java.awt.image.BufferedImage;

public class ImageFile extends BufferedImage {
    private boolean isSaved = true;
    private String filename = null;

    //提取图像，第二个函数？
    public ImageFile(int width, int height, int type) {
        super(width, height, type);
        this.getGraphics().fillRect(0, 0, width, height);
    }
    //！设定！存储图像的文件名字
    public void setSavedFileName(String fileName){
        this.filename = fileName;
    }
    //！返回！存储图像的文件名字
    public String getSavedFileName() {
        return this.filename;
    }
    //！设置！是否存储，默认为true
    public void setIsSaved(boolean b) {
        this.isSaved = b;
    }
    //！返回！是否存储
    public boolean isSaved() {
        return this.isSaved;
    }
}