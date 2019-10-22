package phase1.files;

import phase1.Canvas;
import phase1.tools.AbstractTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class ImageService {
    private FileChooser fileChooser = new FileChooser();

    public Dimension getScreenSize() {
        Toolkit dt = Toolkit.getDefaultToolkit();
        return dt.getScreenSize();
    }


    public void initPaintingSpace(Canvas frame) {
        Graphics g = frame.getBufferedImage().getGraphics();
        Dimension d = frame.getPaintingSpace().getPreferredSize();
        int drawWidth = (int) d.getWidth();
        int drawHeight = (int) d.getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, drawWidth, drawHeight);
    }


    public void repaint(Graphics g, BufferedImage bufferedImage) {
        int drawWidth = bufferedImage.getWidth();
        int drawHeight = bufferedImage.getHeight();
        Dimension screenSize = getScreenSize();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, (int) screenSize.getWidth() * 10, (int) screenSize
                .getHeight() * 10);
        g.setColor(Color.BLUE);
        g.fillRect(drawWidth, drawHeight, 4, 4);
        g.fillRect(drawWidth, (int) drawHeight / 2 - 2, 4, 4);
        g.fillRect((int) drawWidth / 2 - 2, drawHeight, 4, 4);
        g.drawImage(bufferedImage, 0, 0, drawWidth, drawHeight, null);
    }


    public static Cursor createCursor(String path) {
        Image cursorImage = Toolkit.getDefaultToolkit().createImage(path);
        return Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(10, 10), "myCursor");
    }


    public static void setViewport(JScrollPane scroll, JPanel panel, int width,
                                   int height) {
        JViewport viewport = new JViewport();
        panel.setPreferredSize(new Dimension(width, height));
        viewport.setView(panel);
        scroll.setViewport(viewport);
    }


    public void save(boolean bool, Canvas frame) {
        if (bool) {
            if (fileChooser.showSaveDialog(frame) == FileChooser.APPROVE_OPTION) {
                File currentDirectory = fileChooser.getCurrentDirectory();
                String fileName = fileChooser.getSelectedFile().getName();
                String suffix = fileChooser.getSuffix();
                System.out.println("fileName "+fileName);
                System.out.println(suffix);

                String savePath = currentDirectory + "/" + fileName + "." + suffix;
                System.out.println(savePath);
                try {
                    ImageIO.write(frame.getBufferedImage(), suffix, new File(
                            savePath));
                } catch (java.io.IOException ie) {
                    ie.printStackTrace();
                }

                frame.getBufferedImage().setIsSaved(true);
            }
        } else if (!frame.getBufferedImage().isSaved()) {
            JOptionPane option = new JOptionPane();
            int checked = option.showConfirmDialog(frame, "Save changes?", "Confirmation",
                    option.YES_NO_OPTION, option.WARNING_MESSAGE);
            if (checked == option.YES_OPTION) {
                save(true, frame);
            }
        }
    }


    public void open(Canvas frame) {
        save(false, frame);
        if (fileChooser.showOpenDialog(frame) == FileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileChooser.setCurrentDirectory(file);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            AbstractTool.width = width;
            AbstractTool.height = height;
            ImageFile myImage = new ImageFile(width, height,
                    BufferedImage.TYPE_INT_RGB);
            myImage.getGraphics().drawImage(image, 0, 0, width, height, null);
            frame.setBufferedImage(myImage);
            // repaint
            frame.getPaintingSpace().repaint();
            // Reset viewport
            ImageService.setViewport(frame.getScroll(), frame.getPaintingSpace(),
                    width, height);
            myImage.setIsSaved(false);
        }
    }


    public void createCanvas(Canvas frame) {
        save(false, frame);

        //int width = (int) getScreenSize().getWidth() / 2;
        int height = (int) getScreenSize().getHeight() / 2;
        int width = height;
        AbstractTool.width = width;
        AbstractTool.height = height;

        ImageFile imageFile = new ImageFile(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = imageFile.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        frame.setBufferedImage(imageFile);
        // repaint
        frame.getPaintingSpace().repaint();
        // Reset viewport
        ImageService.setViewport(frame.getScroll(), frame.getPaintingSpace(),
                width, height);

    }


    public void exit(Canvas frame) {
        save(false, frame);
        System.exit(0);
    }



    public void menuFunctions(Canvas frame, String cmd) {

        if (cmd.equals("New")) {
            createCanvas(frame);
        }

        if (cmd.equals("Open")) {
            open(frame);
        }

        if (cmd.equals("Save As")) {
            save(true, frame);
        }

        if (cmd.equals("Exit")) {
            exit(frame);
        }

    }

}