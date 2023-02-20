package GUI;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class ImageFileFilter extends FileFilter {
    String suffix;

    String description;

    public ImageFileFilter() {
        super();
    }

//构造函数：设定可接受的“suffix”和“描述”
    public ImageFileFilter(String suffix, String description) {
        super();
        this.suffix = suffix;
        this.description = description;
    }

    public boolean accept(File file) {
//判断是否和对象的suffix相等
        if (file.getName().toUpperCase().endsWith(suffix)) {
            return true;
        }
//目录也返回true？
        return file.isDirectory();
    }

//获得可接受图像的描述
    public String getDescription() {
        return this.description;
    }
}
