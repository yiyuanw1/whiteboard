/**
 * 
 */
package ThreadPool;

import java.util.ArrayList;

import management.ManagerListener;

/**
 * @author sxy
 *
 */
public class ThreadPool {

	private ArrayList<SocketThreadPipeStructure> socketThreadPipeStruArrayList;
	
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
	
	public ArrayList<String> getAllUser(){
		ArrayList<String> names = new ArrayList<>();
		for( SocketThreadPipeStructure s : this.socketThreadPipeStruArrayList ) {
			names.add(s.getUserName());
		}
		return names;
	}
}
