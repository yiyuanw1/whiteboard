package phase1;

import phase1.tools.AbstractTool;
import phase1.tools.Tool;
import phase1.tools.ToolInstances;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ToolAndColorAction extends AbstractAction {
    private String name = "";
    private Canvas canvas = null;
    private Color color = null;
    private Tool tool = null;
    private JPanel colorPanel = null;

    public ToolAndColorAction(Color color, JPanel colorsPanel) {
        super();
        this.color = color;
        this.colorPanel = colorsPanel;
    }


    public ToolAndColorAction(ImageIcon icon, String name, Canvas canvas) {
        super("", icon);
        this.name = name;
        this.canvas = canvas;
    }


    public void actionPerformed(ActionEvent e) {
        tool = (name != "") ? ToolInstances.getToolInstance(canvas, name, Canvas.clientsocket) : tool;
        if (tool != null) {
            canvas.setTool(tool);
        }
        if (color != null) {
            AbstractTool.color = color;
            colorPanel.setBackground(color);
        }
    }
}
