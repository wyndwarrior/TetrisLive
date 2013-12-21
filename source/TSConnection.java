package wynd.tetris;

import java.util.*;
import java.net.*;
import java.io.*;

public class TSConnection implements Runnable{
	
	public static final int PORT = 1337;
	
	private String host;
	private Socket conn;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private TSData data;
	private TSPlayer p;
	private ArrayList<TSPlayer> players;
	private Queue<String> msgQue;
	private ArrayList<String> myMsgs;
	
	private String name;
	
	public TSConnection (String h){
		host = h;
		setName(""+Math.random());
		p = new TSPlayer(getName());
		msgQue = new LinkedList<String>();
		myMsgs = new ArrayList<String>();
	}
	
	public void run(){
		try{
			conn = new Socket(host, PORT);
			
			out = new ObjectOutputStream(conn.getOutputStream()); 
			in = new ObjectInputStream(conn.getInputStream()); 
			
			while(true){
				if( getData() != null){
					out.reset();
					
					TSBiggerData d = new TSBiggerData();
					p.setData(getData());
					d.addPlayer(p);
					while( !msgQue.isEmpty())
						d.addMessage(popMessage());
					
					out.writeObject(d);
					out.flush();
					
					TSBiggerData d2 = (TSBiggerData) in.readObject();
					if( d2 != null ){
						setPlayers(d2.getPlayers());
						for(String s : d2.getMessages()){
							System.out.println("Msg: " + s);
							myMsgs.add(s);
						}
					}
					
					Thread.sleep(30);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			//System.exit(1);
		}finally{
			stop();
		}
	}
	
	private synchronized String popMessage(){
		return msgQue.poll();
	}
	
	public synchronized void addMessage(String s){
		msgQue.add(s);
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
	}

	public TSData getData() {
		return data;
	}

	public void setData(TSData data) {
		this.data = data;
	}
	
	public TSPlayer getPlayer(){
		return p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<TSPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<TSPlayer> players) {
		this.players = players;
	}
	
	public ArrayList<String> getMessages(){
		return myMsgs;
	}
	
	public void clearMessages(){
		myMsgs = new ArrayList<String>();
	}
	
}
