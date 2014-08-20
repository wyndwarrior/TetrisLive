package wynd.tetris;

import java.io.Serializable;

public class TSPlayer implements Serializable{
	private static final long serialVersionUID = -6285775066867150899L;
	
	private String name;
	private TSData d;
	public TSPlayer(String name){
		setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TSData getData() {
		return d;
	}
	public void setData(TSData d) {
		this.d = d;
	}
}
