package GUI;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.json.JSONObject;

import ThreadPool.SocketThreadPipeStructure;
import whiteBoardServer.ServerListeningSocketThread;

public class Circle extends AbstractTool {
    private static Tool tool = null;

    private Circle(Canvas frame) {
        super(frame);
    }

    public static Tool getInstance(Canvas frame) {
        if (tool == null) {
            tool = new Circle(frame);
        }
        return tool;
    }


    public void draw(Graphics g, int x1, int y1, int x2, int y2) {

        int radius = Math.min(Math.abs(x1-x2), Math.abs(y1-y2));
        int x = (x1 > x2) ? x1-radius: x1;
        int y = (y1 > y2) ? y1-radius: y1;
        g.drawOval(x, y, radius, radius);
        
    }
}