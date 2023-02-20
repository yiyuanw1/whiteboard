package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import whiteBoardServer.WhiteBoardServer;
import whiteBoardServer.mainServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class ServerLoginPage extends JFrame {
    JLabel labelTitle, labelName, labelIP, labelPort;
    JTextField tfName, tfIP, tfPort;

    JButton btnCreate;

    String loginName;
    String loginIp;
    String loginPort;

    public ServerLoginPage() {
        JFrame frame = new JFrame("Login Page");
        labelTitle = new JLabel("Welcome to the collaborative whiteboard!");
        labelTitle.setForeground(Color.blue);
        labelTitle.setFont(new Font("Serif", Font.BOLD, 20));

        labelName = new JLabel("Your Username");
        labelIP = new JLabel("IP address");
        labelPort = new JLabel("Port");
        tfName = new JTextField();
        tfIP = new JTextField();
        tfPort = new JTextField();
        btnCreate = new JButton("Create");

        labelTitle.setBounds(20, 30, 400, 30);
        labelName.setBounds(30, 70, 100, 30);
        labelIP.setBounds(30, 110, 100, 30);
        labelPort.setBounds(30,150,100,30);
        tfName.setBounds(150, 70, 200, 30);
        tfIP.setBounds(150, 110, 200, 30);
        tfPort.setBounds(150, 150, 200, 30);
        btnCreate.setBounds(150, 200, 100, 30);

        frame.add(labelTitle);
        frame.add(labelName);
        frame.add(tfName);
        frame.add(labelIP);
        frame.add(tfIP);
        frame.add(labelPort);
        frame.add(tfPort);
        frame.add(btnCreate);


        final int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        final int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation(width / 2 - 125, height / 2 - 125);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                frame.dispose();
            }
        });

        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setVisible(true);


        btnCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Pattern p_ip = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Pattern p_number = Pattern.compile("^-?[0-9]+$");
                Matcher m_ip = p_ip.matcher(tfIP.getText());
                Matcher m_number = p_number.matcher(tfPort.getText());
                if (m_ip.find() && m_number.find() && Integer.parseInt(tfPort.getText()) < 65536 && Integer.parseInt(tfPort.getText()) > 1024 && !tfName.equals("")) {
                    loginIp = tfIP.getText();
                    loginPort = tfPort.getText();
                    loginName = tfName.getText();
                    setLoginIp(loginIp);
                    setLoginPort(loginPort);
                    setLoginName(loginName);
                    
                    
                    synchronized(mainServer.loginLock) {
                    	mainServer.loginLock.notify();
                    }
                    

                } else if (tfName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty");

                } else {
                    JOptionPane.showMessageDialog(null, "IP Address or Port Number in wrong format ");
                    tfPort.setText("");
                    tfIP.setText("");
                }
                ServerLoginPage.this.setVisible(false);
            }
        });


    }


    private void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private void setLoginPort(String loginPort) {
        this.loginPort = loginPort;
    }

    private void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginName(){
        return loginName;
    }

    public String getLoginIp(){
        return loginIp;
    }

    public String getLoginPort(){
        return loginPort;
    }




}
