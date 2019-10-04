package phase1.files;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class ImageFileFilter extends FileFilter {
    String suffix;

    String description;

    public ImageFileFilter() {
        super();
    }


    public ImageFileFilter(String suffix, String description) {
        super();
        this.suffix = suffix;
        this.description = description;
    }

    public boolean accept(File file) {

        if (file.getName().toUpperCase().endsWith(suffix)) {
            return true;
        }

        return file.isDirectory();
    }


    public String getDescription() {
        return this.description;
    }
}
