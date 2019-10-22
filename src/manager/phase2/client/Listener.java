package phase2.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Listener extends MouseAdapter {


	public int x1,y1,x2,y2,x4,y4;
	public ControlClient control;
	
	//���캯�����ܿ���������
	public Listener(ControlClient control) {
		this.control=control;
	}
 
	//������갴���¼�
	public void mousePressed(MouseEvent e) {
		//��ȡ��갴�µ�����ֵ
		x1=e.getX();
		y1=e.getY();
		x4=x1;
		y4=y1;
	}
	
	//��ȡ����ͷŵ�����ֵ
//	public void mouseReleased(MouseEvent e) {
//		x2=e.getX();
//		y2=e.getY();
//		//������ͷŵ�ʱ���ڸû����ϻ���һ��ֱ��
//		control.g.drawLine(x4, y4, x2, y2);
//		//��һ��1����ͼ�����ͣ��ڶ���1������ ��ϸ
//		//��ֱ����Ϣͨ�����������͸���������
//		control.sendData(1,x4, y4, x2, y2,1);
//	}
	
	//��������϶��¼���Ǧ�ʹ��ܣ�
	public void mouseDragged(MouseEvent e) {
		int x3=e.getX();
		int y3=e.getY();
		control.g.drawLine(x1, y1, x3, y3);
		control.sendData(1,x1, y1, x3, y3,1);
		x1=x3;
		y1=y3;
	}
}
