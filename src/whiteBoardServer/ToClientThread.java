/**
 * 
 */
package whiteBoardServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ThreadPool.SocketThreadPipeStructure;

/**
 * @author sxy
 *
 */
public class ToClientThread extends Thread {

	private Socket serverSocket;
	private PipedWriter lowerPw;
	
	public ToClientThread(Socket ss, PipedWriter pw) {
		serverSocket = ss;
		lowerPw = pw;
	}
	
	public void run(){
		try{
			BufferedReader messageFromClientRd = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), "UTF-8"));
			
			//读信息
			String messageFromClient;
			while ((messageFromClient = messageFromClientRd.readLine()) != null){
				try {
					JSONObject mobj = new JSONObject(messageFromClient);
					if(mobj.get("Action").equals("draw"))
						mainServer.graphArrayList.add(new JSONObject(messageFromClient));
					
					
					messageFromClient += '\n';
					//String 转 char[]
					char[] messageFromClientChar = messageFromClient.toCharArray();
					
					//发送给上一级master
					lowerPw.write(messageFromClientChar, 0, messageFromClient.length()); 
					lowerPw.flush();
					
					synchronized(WhiteBoardServer.Pipelock) {
						WhiteBoardServer.Pipelock.notify();
				}
					
				}
				catch(JSONException e){
				}
			}
			//先删是因为报错顺序
			/*把带有该socket和thread的从ThreadPool里删除（thread好像不用删除，这个就是当前thread，执行完自动退出）*/
			//迭代每个socket
			for(int i = 0; i < ServerListeningSocketThread.threadMaster.size(); i++) {
				SocketThreadPipeStructure stps = ServerListeningSocketThread.threadMaster.get(i);
				if(serverSocket == stps.getSocket()) {
					
					String userName = stps.getUserName();
					String IP = stps.getIP();
					ServerListeningSocketThread.threadMaster.remove(i);
					
					if(stps.getUserName()== null) break;
					
					
					SimpleDateFormat sdf = new SimpleDateFormat();
	                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
	                Date date = new Date();
	                String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: The socket[" + serverSocket.toString() + "] cannot connect to " + userName + "("+ IP +"), so it is deleted from the pool.\n";
					mainServer.f.getChatBoard().getChatWindow().append(SYSinfo);
					
					//通知已退出
					String SYSinfoDistribute = "[" + sdf.format(date).toString() + "] SYSTEM: The " + userName + "("+ IP +") is disconnected.\n";
					JSONObject SYSinfoObj = new JSONObject();
					SYSinfoObj.put("Action", "system");
					SYSinfoObj.put("message", SYSinfoDistribute);
					String SYSinfoS = SYSinfoObj.toString();
					
					//迭代每个socket
	        		for(int j = 0; j < ServerListeningSocketThread.threadMaster.size(); j++) {
	        			SocketThreadPipeStructure stpsToCLient = ServerListeningSocketThread.threadMaster.get(j);
	        			Socket iteraSocket = stpsToCLient.getSocket();
	        		
	        			try {
	        				BufferedWriter messagetoClientWt = new BufferedWriter(new OutputStreamWriter(iteraSocket.getOutputStream(), "UTF-8"));
	        				messagetoClientWt.write(SYSinfoS + "\n");
	        				messagetoClientWt.flush();
	        			} catch (IOException e1) {
	        				mainServer.f.getChatBoard().getChatWindow().append("[" + sdf.format(date).toString() + "] SYSTEM: " + e1.getMessage() + "\n");
	        			}
	        		}   
	                
					break;
				}
			}
			//退出循环（对方关闭），则关闭socket
			SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
            Date date = new Date();
            String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: The socket[" + serverSocket.toString() + "] is closed.\n";
            mainServer.f.getChatBoard().getChatWindow().append(SYSinfo);
            SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: The socket thread is also interrupted.\n";
            mainServer.f.getChatBoard().getChatWindow().append(SYSinfo);
            
            serverSocket.shutdownInput();
            serverSocket.shutdownOutput();
			serverSocket.close();
			
		}catch (IOException e){
			/*SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); 
            Date date = new Date();
			String SYSinfo = "[" + sdf.format(date).toString() + "] SYSTEM: " + e.getMessage() + "\n";
			mainServer.f.getChatBoard().getChatWindow().append(SYSinfo);
			*/
		}
		
		
	}

}
