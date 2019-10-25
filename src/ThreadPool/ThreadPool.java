/**
 * 
 */
package ThreadPool;

import java.util.ArrayList;

import GUI.ManagerListener;

/**
 * @author sxy
 *
 */
public class ThreadPool {

	private ArrayList<SocketThreadPipeStructure> socketThreadPipeStruArrayList;
	
	private ArrayList<ManagerListener> listeners = new ArrayList<ManagerListener>();
	
	public ThreadPool() {
		socketThreadPipeStruArrayList = new ArrayList<SocketThreadPipeStructure>();
		
	}

	public void add(SocketThreadPipeStructure e)
	{
		socketThreadPipeStruArrayList.add(e);
		
	}
	
	public int size()
	{
		return socketThreadPipeStruArrayList.size();
		
	}
	
	public SocketThreadPipeStructure get(int index) {
		return socketThreadPipeStruArrayList.get(index);
	}
	
	public void remove(int index) {
		socketThreadPipeStruArrayList.remove(index);
	}
	
	public void addListener(ManagerListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ManagerListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyAdd(String name) {
		for( ManagerListener listener : listeners ) {
			listener.newUser(name);
		}
	}
	
	public void notifyQuit(String name) {
		for( ManagerListener listener : listeners ) {
			listener.removeUser(name);
		}
	}
	
	public ArrayList<String> getAllUser(){
		ArrayList<String> names = new ArrayList<>();
		for( SocketThreadPipeStructure s : this.socketThreadPipeStruArrayList ) {
			names.add(s.getUserName());
		}
		return names;
	}
}
