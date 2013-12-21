package wynd.tetris;
import java.awt.Color;
import java.io.*;
import java.util.*;
public class TSTetris implements Serializable{
    
	private static final long serialVersionUID = 7526472295622776147L;
	
	private static final boolean AUTOROBOT = false;
	private static final boolean SHOWGUIDE = false;
    
    private TSBoard<TSBlock> board;
    private TSPiece piece;
    private TSPiece hold;
    private boolean lastHold;
    private TSScore score;
    private int lines;
    private ArrayList<TSPiece> que;
    private TSRobot robot;
    private TSPiece[] nextMove;
    
    private ArrayList<Integer> garbage;
    
    public static final int NFORESIGHT = 5;
    private static final int NPIECES = 7;
    
    public TSTetris(){
        klasttime[KAUTODROP] = getTime();
    }
    
    public void newGame(){
        board = new TSBoard<TSBlock>();
        que = new ArrayList<TSPiece>();
        garbage = new ArrayList<Integer>();
        robot = new TSRobot();
        score = new TSScore();
        hold = null;
        lastHold = false;
        lines = 0;
        pop();
        updateRobot();
    }
    
    private void didDrop(){
    	//System.out.println(lines);
        TSBoard<TSBlock> tmp = clearLines();
        if( tmp != null ) setBoard(tmp);
        
        if( lines != 0 && !garbage.isEmpty()){
        	int g = getGarbage();
        	if( lines >= g){
        		garbage.clear();
        		lines -= g;
        	}else{
        		for(int i = 0; i<garbage.size() && g>0; i++)
        			if( g >= garbage.get(i))
        				g -= garbage.remove(i--);
        			else{
        				garbage.set(i, garbage.get(i)-g);
        				g = 0;
        			}
        	}
        }
        
        processGarbage();
        
        updateRobot();
        
    }
    
    private void updateRobot(){
        nextMove = new TSPiece[1];
        //new TSRobotThread(robot, nextMove, que, piece, board).start();
        //System.out.println(robot.canyons(board));
        //System.out.println(robot.getStDv(board));
    }
    
    //gets next piece
    private void pop(){
    	
        lastHold = false;
        if( que.size() <= 5 )
            que.addAll(getRandom());
        piece = que.remove(0);
        //updateRobot();
        if (!piece.place(board.copy()))
            endGame();
    }
    
    public TSPiece getPiece(){
        return piece;
    }
    
    //get a random list of all the pieces
    private ArrayList<TSPiece> getRandom(){
        ArrayList<TSPiece> ar = new ArrayList<TSPiece>();
        
        ar.add(new TSPieceBlue(board));
        ar.add(new TSPieceGreen(board));
        ar.add(new TSPieceLine(board));
        ar.add(new TSPieceOrange(board));
        ar.add(new TSPiecePurple(board));
        ar.add(new TSPieceRed(board));
        ar.add(new TSPieceYellow(board));
        
        double rand = Math.random() * NPIECES;
        
        if( rand < 1 )
            ar.add(new TSPieceBlue(board));
        else if( rand < 2 )
            ar.add(new TSPieceGreen(board));
        else if( rand < 3 )
            ar.add(new TSPieceLine(board));
        else if( rand < 4 )
            ar.add(new TSPieceOrange(board));
        else if( rand < 5 )
            ar.add(new TSPiecePurple(board));
        else if( rand < 6 )
            ar.add(new TSPieceRed(board));
        else if( rand < 7 )
            ar.add(new TSPieceYellow(board));
        
        Collections.shuffle(ar);
        
        return ar;
        
    }
    
    public ArrayList<TSPiece> getForesight(){
        ArrayList<TSPiece> tmp = new ArrayList<TSPiece>(5);
        for(int i =0; i<5; i++)
            tmp.add(que.get(i));
        return tmp;
    }
    
    public TSBoard<TSBlock> getBoard(){
        TSBoard<TSBlock> t = board.copy();
        piece.place(t);
        if( !AUTOROBOT && SHOWGUIDE){
        	if( nextMove != null && nextMove[0] != null)
        		nextMove[0].getGuide().place(t);
        }
        piece.getGhost().place(t);
        return t;
    }
    
    private void setBoard(TSBoard<TSBlock> b){
        board = b;
        if( piece != null )
            piece.setBoard(board);
        if( que != null)
        	for(TSPiece p : que)
        		p.setBoard(board);
        if( hold != null )
            hold.setBoard(board);
    }
    
    public void endGame(){
        newGame();
    }
    
    public static TSBoard<TSBlock> clearBoard(TSBoard<TSBlock> board, int[] score){

        TSBoard<TSBlock> tmp = new TSBoard<TSBlock>(board.getRow(), board.getCol());
        int ii = board.getRow()-1;
        int count = 0;
        
        for(int i = board.getRow()-1; i>=0; i--){
            boolean full = true;
            for(int j = 0; j<board.getCol(); j++)
                if( board.get(i,j) == null){
                    full = false;
                    break;
                }
            if( !full ){
                for(int j = 0; j<board.getCol(); j++)
                    tmp.set(ii,j,board.get(i,j));
                ii--;
            }else{
                count ++;
            }
        }
        score[0] = count;
        return tmp;
    }
    
    public TSBoard<TSBlock> clearLines(){
        
    	int scores[]= new int[1];
    	TSBoard<TSBlock> tmp = clearBoard(board, scores);
        int send = score.process(scores[0]);
        
        lines += send;
        
        if( scores[0] != 0 )
            return tmp;
        return null;
    }
    
    private long getTime() {
        return System.nanoTime() / 1000000;
    }
    
    public final static int KLIM = 256;
    private transient long[] ktime = new long[KLIM]; //delta time
    private transient long[] klasttime = new long[KLIM]; 
    private transient boolean[] kpressed = new boolean[KLIM]; // is pressed
    private transient boolean[] kstarted = new boolean[KLIM]; // has started past initial delay
    
    private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
    	is.defaultReadObject();
    	
    	ktime = new long[KLIM];
        klasttime = new long[KLIM]; 
        kpressed = new boolean[KLIM];
        kstarted = new boolean[KLIM];
    }
    
    public static final int KLEFT = 0;
    public static final int KRIGHT = 1;
    public static final int KDOWN = 2;
    public static final int KROTATECW = 3;
    public static final int KROTATECCW = 4;
    public static final int KDROP = 5;
    public static final int KAUTODROP = 6;
    public static final int KHOLD = 7;
    public static final int KSAVE = 8;
    public static final int KLOAD = 9;
    
    private static final int KSTART = 110; //inital delay
    private static final int KDELAY = 10; //subsequent delay
    private static final int KDROPDELAY = KSTART/3; //delay for soft-drop
    private static final int KAUTODROPTIME = AUTOROBOT?500:1000;
    
    public void pressed(int key){
        kpressed[key] = true;
        kstarted[key] = false;
        klasttime[key] = getTime();
        ktime[key] = 0;
        
        //handle key events
        if( key == KROTATECW )
            piece.rotateCW();
        else if( key == KROTATECCW )
            piece.rotateCCW();
        else if( key == KLEFT )
            piece.moveLeft();
        else if( key == KRIGHT )
            piece.moveRight();
        else if( key == KDOWN )
            moveDown();
        else if( key == KDROP ){
            piece.drop();
            pop();
            didDrop();
        }else if( key == KHOLD )
            hold();
        
    }
    
    public void released(int key){
        kpressed[key] = false;
    }
    
    private void moveDown(){
        if( !piece.moveDown() ){
            piece.drop();
            pop();
            didDrop();
        }
    }
    
    public void hold(){
        
        if( lastHold ) return;
        
        TSPiece tmp = piece;
        if( hold == null ){
            pop();
            hold = piece;
        }
        piece = hold;
        hold = tmp;
        hold.reset();
        lastHold = true;
        updateRobot();
    }
    
    public TSPiece getHold(){
        return hold;
    }
    
    public void tick(int delta){
        processDelay(KLEFT);
        processDelay(KRIGHT);
        processDelay(KDOWN);
        
        updateDelta(KAUTODROP);
        if( ktime[KAUTODROP] >= KAUTODROPTIME ){
        	if( ktime[KAUTODROP] > 0 )
        		ktime[KAUTODROP] %= KAUTODROPTIME;
        	if( AUTOROBOT){
                if( nextMove != null && nextMove[0] != null){
                	nextMove[0].place(board);
                	nextMove[0] = null;
                	pop();
                	didDrop();
                }
        	}else{
        		moveDown();
        	}
        }
        
    }
    
    private void updateDelta(int key){
        long time = getTime();
        long delta = time-klasttime[key];
        klasttime[key] = time;
        ktime[key] += delta;
    }
    
    private void processDelay(int key){
        
        if( kpressed[key] ){
            updateDelta(key);
            
            if( key == KDOWN ){
                
                if (ktime[key] >= KDROPDELAY){
                    ktime[key] %= KDROPDELAY;
                    if( piece.isReal())
                        moveDown();
                    else if( ktime[KAUTODROP] >= 0)
                    	ktime[KAUTODROP] = -1000;
                }
                
            }else if( ktime[key] >= KSTART && !kstarted[key] ){
                kstarted[key] = true;
                ktime[key] %= KSTART;
            }else if(ktime[key] >= KDELAY && kstarted[key]){
                
                ktime[key] %= KDELAY;
                if( key == KLEFT )
                    piece.moveLeft();
                else if( key == KRIGHT )
                    piece.moveRight();
                
            }
            
        }
    }
    
    public String toString(){
        return getBoard().smallToString();
    }
    
    public int linesSent(){
    	return lines;
    }
    
    public void resetLines(){
    	lines = 0;
    }
    
    public void addGarbage(int x){
    	garbage.add(x);
    }
    
    public int getGarbage(){
    	int g  = 0;
    	for(int i : garbage)
    		g += i;
    	return g;
    }
    
    public void processGarbage(){
    	
    	if( garbage.isEmpty() ) return;
    	
    	int g = getGarbage();
    	
        TSBoard<TSBlock> tb = new TSBoard<TSBlock>(board.getRow(), board.getCol());
        for(int i = board.getRow()-1; i>=g; i--){
        	for(int j = 0; j<board.getCol(); j++)
        		tb.set(i-g, j, board.get(i, j));
        }
        
        int ii = board.getRow()-1;
        for(int i : garbage){
        	int gap = (int)(Math.random() * board.getCol());
        	for(int k = ii; k > ii-i; k--)
        	for(int j = 0; j<board.getCol(); j++){
        		if( j != gap)
        			tb.set(k, j, new TSBlock(Color.BLACK));
        	}
        	ii-=i;
        }
        
        setBoard(tb);
        garbage.clear();
    	
    }
    
}

