package wynd.tetris;

import java.util.*;
import java.io.*;

public class TSData implements Serializable{
	
	private static final long serialVersionUID = 9011312216258461241L;
	
	private TSBoard<TSBlock> board;
	private String hold;
	private String foresight;
	private int garbage;
	
	public TSData(TSBoard<TSBlock> board,
			TSPiece hold,
			ArrayList<TSPiece> foresight,
			int g){
		setBoard(board);
		setHold(hold);
		setForesight(foresight);
		setGarbage(g);
	}
	
	public TSBoard<TSBlock> getBoard() {
		return board;
	}
	public void setBoard(TSBoard<TSBlock> board) {
		this.board = board;
	}
	public TSPiece getHold() {
		if( hold == null )return null;
		return TSSerialData.readPiece(hold);
	}
	public void setHold(TSPiece hold) {
		if( hold == null) return;
		this.hold = TSSerialData.serialPiece(hold);
	}
	public ArrayList<TSPiece> getForesight() {
		if( foresight == null)return null;
		return TSSerialData.readForesight(foresight);
	}
	public void setForesight(ArrayList<TSPiece> foresight) {
		if( foresight == null )return;
		this.foresight = TSSerialData.serialForesight(foresight);
	}

	public int getGarbage() {
		return garbage;
	}

	public void setGarbage(int garbage) {
		this.garbage = garbage;
	}
	
	
}
