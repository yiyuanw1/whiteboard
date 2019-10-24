package phase1.tools;

import phase1.Canvas;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ToolInstances {

    private static Map<String, Tool> tools = null;


    public static Tool getToolInstance(Canvas frame, String type, Socket s) {
        if (tools == null) {
            tools = new HashMap<>();
            tools.put(Tool.FREE_TOOL, Free.getInstance(frame,s));
            tools.put(Tool.ERASER_TOOL, Eraser.getInstance(frame,s));
            tools.put(Tool.LINE_TOOL, Line.getInstance(frame,s));
            tools.put(Tool.RECT_TOOL, Rect.getInstance(frame,s));
            tools.put(Tool.CIRCLE_TOOL, Circle.getInstance(frame,s));
            tools.put(Tool.OVAL_TOOL, Oval.getInstance(frame,s));
            tools.put(Tool.TEXT_TOOL, Text.getInstance(frame,s));
        }
        return tools.get(type);
    }
}