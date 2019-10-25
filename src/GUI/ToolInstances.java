package GUI;

import java.util.HashMap;
import java.util.Map;

public class ToolInstances {

    private static Map<String, Tool> tools = null;


    public static Tool getToolInstance(Canvas frame, String type) {
        if (tools == null) {
            tools = new HashMap<>();
            tools.put(Tool.FREE_TOOL, Free.getInstance(frame));
            tools.put(Tool.ERASER_TOOL, Eraser.getInstance(frame));
            tools.put(Tool.LINE_TOOL, Line.getInstance(frame));
            tools.put(Tool.RECT_TOOL, Rect.getInstance(frame));
            tools.put(Tool.CIRCLE_TOOL, Circle.getInstance(frame));
            tools.put(Tool.OVAL_TOOL, Oval.getInstance(frame));
            tools.put(Tool.TEXT_TOOL, Text.getInstance(frame));
        }
        return tools.get(type);
    }
}