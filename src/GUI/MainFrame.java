package GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame {
    public static void main(String[] args) {
        Canvas f = new Canvas();

        f.pack();
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                f.getService().saveAs(false,f);
                e.getWindow().dispose();
            }
        });

    }
}
