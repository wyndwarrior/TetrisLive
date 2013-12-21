package wynd.tetris;

import java.net.*;
import java.io.*;
import java.util.*;

public class TSServer implements Runnable{
	
	public static final int PORT = TSConnection.PORT;
	private ServerSocket server;
	
	private ArrayList<TSUser> list;
	
	public TSServer(){
	}
	
	public void run(){
		list = new ArrayList<TSUser>();
		try{
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + PORT);
			System.exit(-1);
		}

		System.out.println("Listening on port " + PORT);
		
		while(true){
			TSUser w = null;
			try{
				w = new TSUser(server.accept(), this);
				System.out.println ("New person joined");
				list.add(w);
				w.start();
			} catch (Exception e) {
				System.out.println("Accept failed");
				if( w != null )
					remove(w);
				//System.exit(-1);
			}
		}
	}
	
	public ArrayList<TSUser> getUsers(){
		return list;
	}

	public void remove(TSUser user){
		user.stop();
		list.remove(user);
		System.out.println ("Person left");
	}
	
	public void stop(){
		while( !list.isEmpty())
			remove(list.get(list.size()-1));
	}

	public static void main (String[] args) {
		//TSServer test = new TSServer();
		//test.start();
	}

}
