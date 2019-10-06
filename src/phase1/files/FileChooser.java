package phase1.files;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class FileChooser extends JFileChooser {

    public FileChooser() {
        super();
        setAcceptAllFileFilterUsed(false);
        chooseFormat();
    }


    public FileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
        setAcceptAllFileFilterUsed(false);
        chooseFormat();
    }


    public String getSuffix() {

        FileFilter fileFilter = this.getFileFilter();
        String desc = fileFilter.getDescription();
        String[] sufarr = desc.split(" ");
        String suf = sufarr[0];
        return suf.toLowerCase();
    }


    private void chooseFormat() {
        this.addChoosableFileFilter(new ImageFileFilter(new String(".PNG" ),
                "PNG (*.PNG)"));
        this.addChoosableFileFilter(new ImageFileFilter(new String(".JPG" ),
                "JPG (*.JPG)"));
        this.addChoosableFileFilter(new ImageFileFilter(new String(".JPEG" ),
                "JPEG (*.JPEG)"));
        this.addChoosableFileFilter(new ImageFileFilter(new String(".BMP" ),
                "BMP (*.BMP)"));
        this.addChoosableFileFilter(new ImageFileFilter(new String(".TIF" ),
                "TIF (*.TIF)"));
        this.addChoosableFileFilter(new ImageFileFilter(new String(".TIFF" ),
                "TIFF (*.TIFF)"));
    }



}