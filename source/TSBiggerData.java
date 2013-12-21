package wynd.tetris;

import java.io.Serializable;
import java.util.ArrayList;

public class TSBiggerData implements Serializable {

	private static final long serialVersionUID = 6393199177767020791L;
	
	private ArrayList<String> messages;
	private ArrayList<TSPlayer> players;
	
	public TSBiggerData(){
		messages = new ArrayList<String>();
		players = new ArrayList<TSPlayer>();
	}
	
	public void addMessage(String m){
		getMessages().add(m);
	}
	
	public void addPlayer(TSPlayer p){
		getPlayers().add(p);
	}
	
	public ArrayList<String> getMessages() {
		return messages;
	}

	public ArrayList<TSPlayer> getPlayers() {
		return players;
	}
	
	
}
