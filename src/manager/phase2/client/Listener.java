package phase2.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Listener extends MouseAdapter {


	public int x1,y1,x2,y2,x4,y4;
	public ControlClient control;
	
	//构造函数接受控制器对象
	public Listener(ControlClient control) {
		this.control=control;
	}
 
	//监听鼠标按下事件
	public void mousePressed(MouseEvent e) {
		//获取鼠标按下的坐标值
		x1=e.getX();
		y1=e.getY();
		x4=x1;
		y4=y1;
	}
	
	//获取鼠标释放的坐标值
//	public void mouseReleased(MouseEvent e) {
//		x2=e.getX();
//		y2=e.getY();
//		//当鼠标释放的时候在该画板上绘制一条直线
//		control.g.drawLine(x4, y4, x2, y2);
//		//第一个1代表图形类型，第二个1代表画笔 粗细
//		//将直线信息通过控制器发送给服务器端
//		control.sendData(1,x4, y4, x2, y2,1);
//	}
	
	//监听鼠标拖动事件（铅笔功能）
	public void mouseDragged(MouseEvent e) {
		int x3=e.getX();
		int y3=e.getY();
		control.g.drawLine(x1, y1, x3, y3);
		control.sendData(1,x1, y1, x3, y3,1);
		x1=x3;
		y1=y3;
	}
}
