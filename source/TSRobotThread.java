package wynd.tetris;

import java.util.*;

public class TSRobotThread extends Thread{
	private TSRobot robot;
	private TSPiece[] pointer;
	private ArrayList<TSPiece> fs;
	private TSPiece p;
	private TSBoard<TSBlock> b;
	
	public TSRobotThread(TSRobot r, TSPiece[] point,
				ArrayList<TSPiece> fs, TSPiece p, TSBoard<TSBlock> b){
		robot = r;
		pointer = point;
		this.p = p;
		this.fs = fs;
		this.b = b;
	}
	public void run(){
		pointer[0] = robot.nextMove(fs, p, b);
	}
}
