package wynd.tetris;

import java.io.*;
import java.net.*;
import java.util.*;

public class TSUser implements Runnable {
	//private String name;
	private Socket client;
	private Thread t;
	private TSServer server;
	private TSPlayer player;
	
	private ArrayList<String> msgs;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public TSUser(Socket c, TSServer s){
		client = c;
		server = s;
		msgs = new ArrayList<String>();
	}
	
	public void start(){
		t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		if( out != null){
			try{
				out.close();
				out = null;
			}catch(Exception e){}
		}
		if( in != null){
			try{
				in.close();
				in = null;
			}catch(Exception e){}
		}
		if( t != null)
		t.interrupt();
	}
	
	public void run(){
		try{
			out = new ObjectOutputStream(client.getOutputStream()); 
			in = new ObjectInputStream(client.getInputStream()); 
		} catch (IOException e) {
			System.out.println("in or out failed");
			//System.exit(-1);
			server.remove(this);
			return;
		}
		
		try{
		
			while(true){
				TSBiggerData d = (TSBiggerData) in.readObject();
				if( d != null){
					setPlayer(d.getPlayers().get(0));
					if( !d.getMessages().isEmpty())
						for(String msg : d.getMessages())
							for(TSUser u : server.getUsers())
								if( u != this )
									u.addMessage(msg);
				}
				//System.out.println(getData());
				TSBiggerData d2 = new TSBiggerData();
				for(TSUser u : server.getUsers())
					if( u != this && u.getPlayer() != null)
						d2.addPlayer(u.getPlayer());
				synchronized(this){
					for(String s : msgs)
						d2.addMessage(s);
					msgs = new ArrayList<String>();
				}
				out.writeObject(d2);
			}
		
		}catch(Exception e){
			//e.printStackTrace();
			server.remove(this);
		}
	}
	
	public synchronized void addMessage(String s){
		msgs.add(s);
	}

	public TSPlayer getPlayer() {
		return player;
	}

	public void setPlayer(TSPlayer player) {
		this.player = player;
	}
	
}
