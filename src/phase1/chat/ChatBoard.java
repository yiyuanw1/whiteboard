package phase1.chat;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatBoard extends JPanel{

    private static final long serialVersionUID = 1L;

    // components for chat
    private JTextArea chatWindow;
    private JTextArea outMessage;
    private JButton btnSend;

    private Dimension size;

    private String text;

    public ChatBoard(int width, int height) {
        size = new Dimension(width, height);
        setLayout(new BorderLayout(0, 0));
        initialize();
    }

    private void initialize() {
        chatWindow = new JTextArea();
        chatWindow.setPreferredSize(new Dimension(size.width, size.height-50));
        chatWindow.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 6), new LineBorder(new Color(192, 192, 192), 6)));
        chatWindow.setLineWrap(true);
        chatWindow.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatWindow);
        this.add(chatScroll, BorderLayout.CENTER);

        JPanel sendingMessage = new JPanel();
        this.add(sendingMessage, BorderLayout.SOUTH);
        sendingMessage.setLayout(new BorderLayout(0,0));

        outMessage = new JTextArea();
        outMessage.setLineWrap(true);
        outMessage.setPreferredSize(new Dimension(size.width-20, 40));
        outMessage.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
        JScrollPane outMessageScroll = new JScrollPane(outMessage);
        sendingMessage.add(outMessageScroll, BorderLayout.CENTER);

        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                text = new String(outMessage.getText());
                chatWindow.append(text+"\n");
                outMessage.setText("");
            }

        });
        sendingMessage.add(btnSend, BorderLayout.EAST);
    }

}

