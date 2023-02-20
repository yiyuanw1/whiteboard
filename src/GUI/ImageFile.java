package GUI;
import java.awt.image.BufferedImage;

public class ImageFile extends BufferedImage {
    private boolean isSaved = true;
    private String filename = "";

    //鎻愬彇鍥惧儚锛岀浜屼釜鍑芥暟锛�
    public ImageFile(int width, int height, int type) {
        super(width, height, type);
        this.getGraphics().fillRect(0, 0, width, height);
    }
    //锛佽瀹氾紒瀛樺偍鍥惧儚鐨勬枃浠跺悕瀛�
    public void setSavedFileName(String fileName){
        this.filename = fileName;
    }
    //锛佽繑鍥烇紒瀛樺偍鍥惧儚鐨勬枃浠跺悕瀛�
    public String getSavedFileName() {
        return this.filename;
    }
    //锛佽缃紒鏄惁瀛樺偍锛岄粯璁や负true
    public void setIsSaved(boolean b) {
        this.isSaved = b;
    }
    //锛佽繑鍥烇紒鏄惁瀛樺偍
    public boolean isSaved() {
        return this.isSaved;
    }
}