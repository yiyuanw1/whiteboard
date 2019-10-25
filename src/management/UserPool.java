package management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserPool {
	
	private static final String filePath = "./resource/users";

	public static ArrayList<ManagerListener> listeners = new ArrayList<ManagerListener>();
	
	public UserPool() {
		new File(filePath).delete();
	}
	
	public static void newUser(String name) {
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			String buffer = new String();
			File file = new File(filePath);
			if( file.exists() ) {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while( (line = br.readLine()) != null ) {
					buffer+=line;
					buffer+="\n";
				}
			}
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(buffer+name);
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				if( br != null) br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeUsers(ArrayList<String> users) {
		for( String name : users ) {
			newUser(name);
		}
	}
	
	public static ArrayList<String> readUser(){
		ArrayList<String> users = new ArrayList<>();
		String name;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(filePath)));
			
			while( (name = br.readLine()) != null ) {
				users.add(name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}
	
	public static void deleteUser(String name) {
		ArrayList<String> users = readUser();
		users.remove(name);
		File file = new File(filePath);
		file.delete();
		writeUsers(users);
	}
	
	public static void addListener(ManagerListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(ManagerListener listener) {
		listeners.remove(listener);
	}
	
	public static void notifyAdd(String name) {
		for( ManagerListener listener : listeners ) {
			listener.newUser(name);
		}
	}
	
	public static void notifyQuit(String name) {
		for( ManagerListener listener : listeners ) {
			listener.removeUser(name);
		}
	}
	
}
