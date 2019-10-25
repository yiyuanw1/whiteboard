package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageService {
	
	//绂佹鎵�鏈夊彲閫夋枃浠讹紝骞舵坊鍔犲彲閫夋枃浠剁殑鎵╁睍鍚�
    private FileChooser fileChooser = new FileChooser();

    //鑾峰彇灞忓箷灏哄(Dimension)
    public Dimension getScreenSize() {
        Toolkit dt = Toolkit.getDefaultToolkit();
        return dt.getScreenSize();
    }

    //
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
        g.fillRect(0, 0, (int) screenSize.getWidth() * 10, (int) screenSize.getHeight() * 10);
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


    public void saveAs(boolean bool, Canvas canvas) {
        if (bool) {
            if (fileChooser.showSaveDialog(canvas) == FileChooser.APPROVE_OPTION) {
                File currentDirectory = fileChooser.getCurrentDirectory();
                String fileName = fileChooser.getSelectedFile().getName();
                String suffix = fileChooser.getSuffix();
                String savedPath = currentDirectory + "/" + fileName + "." + suffix;

                File tmpFile = new File(savedPath);
                if (tmpFile.exists()){
                    int response = JOptionPane.showConfirmDialog(null,
                            "Do you want to replace the existing file?",
                            "Confirm", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (response != JOptionPane.YES_OPTION) {
                        saveAs(true,canvas);
                    }else{
                        try {
                            ImageIO.write(canvas.getBufferedImage(), suffix, new File(savedPath));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        canvas.getBufferedImage().setIsSaved(true);
                        canvas.getBufferedImage().setSavedFileName(savedPath);
                    }

                }else{
                    try {
                        ImageIO.write(canvas.getBufferedImage(), suffix, new File(savedPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    canvas.getBufferedImage().setIsSaved(true);
                    canvas.getBufferedImage().setSavedFileName(savedPath);

                }

            }
        } else if (!canvas.getBufferedImage().isSaved()) {
            JOptionPane option = new JOptionPane();
            int checked = option.showConfirmDialog(canvas, "Save changes?", "Confirmation",
                    option.YES_NO_OPTION, option.WARNING_MESSAGE);
            if (checked == option.YES_OPTION) {
                saveAs(true, canvas);
            }
        }
    }

    public void save(Canvas canvas) {
        String fileName = canvas.getBufferedImage().getSavedFileName();
        System.out.println("fn: "+fileName);
        int point = fileName.indexOf('.');
        String suffix = fileName.substring(point+1,fileName.length());
        System.out.println("suffix:"+suffix);
        try {
            ImageIO.write(canvas.getBufferedImage(), suffix, new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void open(Canvas frame) {
        saveAs(false, frame);
        if (fileChooser.showOpenDialog(frame) == FileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileChooser.setCurrentDirectory(file);
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(file);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return;
            }
            int w = bufferedImage.getWidth();
            int h = bufferedImage.getHeight();
            AbstractTool.width = w;
            AbstractTool.height = h;
            ImageFile myImage = new ImageFile(w, h, BufferedImage.TYPE_INT_RGB);
            myImage.getGraphics().drawImage(bufferedImage, 0, 0, w, h, null);
            frame.setBufferedImage(myImage);
            // repaint
            frame.getPaintingSpace().repaint();
            // Reset viewport
            ImageService.setViewport(frame.getScroll(), frame.getPaintingSpace(), w, h);
            myImage.setIsSaved(false);
            
            frame.getBufferedImage().setSavedFileName(file.getAbsolutePath());
            System.out.print(file.getAbsolutePath()+"\n");
        }
    }




    public void createCanvas(Canvas frame) {
        saveAs(false, frame);

        int width = (int) getScreenSize().getWidth() / 3;
        int height = (int) getScreenSize().getHeight() / 2;
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
        ImageService.setViewport(frame.getScroll(), frame.getPaintingSpace(), width, height);

    }


    public void exit(Canvas frame) {
        saveAs(false, frame);
        System.exit(0);
    }



    public void menuFunctions(Canvas frame, String cmd) {

        if (cmd.equals("New")) {
            createCanvas(frame);
        }

        if (cmd.equals("Open")) {
            open(frame);
        }

        if (cmd.equals("Save")) {
            if(!frame.getBufferedImage().getSavedFileName().equals("")){
                save(frame);
            }else{
                saveAs(true,frame);
            }
        }

        if (cmd.equals("Save As")) {
            saveAs(true, frame);
        }

        if (cmd.equals("Exit")) {
            exit(frame);
        }

    }


}