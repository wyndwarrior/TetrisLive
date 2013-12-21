package wynd.tetris;

import java.io.Serializable;

public class TSMessage implements Serializable {
	private static final long serialVersionUID = 7212060963746758932L;
	private String message;
	
	public TSMessage(String m){
		setMessage(m);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
