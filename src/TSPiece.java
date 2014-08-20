package wynd.tetris;

import java.io.Serializable;

public class TSPiece implements Serializable{
    
	public TSPiece copy(){
		TSPiece pp = new TSPiece();
		pp.p = p;
		pp.b = b;
		pp.depth = depth;
		pp.rotation = rotation;
		pp.row = row;
		pp.col = col;
		pp.rot = numRot();
		pp.ch = getChar();
		return pp;
	}
	
	
	private static final long serialVersionUID = 7526472295622776147L;
	
    private TSBoard<TSBlock> b;
    public TSBlock[][] p; //should always be a square
    private int depth;  //absolute location
    private int rotation, rot = 4;
    private int row, col; //relative location
    private char ch;
    
    /*
     *
     * PUBLIC methods
     *
     */
    
    private TSPiece(){}
    
    public static TSPiece getPiece(char c){
    	if( c == 'B' ) return new TSPieceBlue(null);
    	if( c == 'G' ) return new TSPieceGreen(null);
    	if( c == 'L' ) return new TSPieceLine(null);
    	if( c == 'O' ) return new TSPieceOrange(null);
    	if( c == 'P' ) return new TSPiecePurple(null);
    	if( c == 'R' ) return new TSPieceRed(null);
    	if( c == 'Y' ) return new TSPieceYellow(null);
    	return null;
    }
    
    public int numRot(){
    	return rot;
    }
    
    public char getChar(){
    	return ch;
    }
    
    // p: square matrix of TSBlocks, null if empty
    // b: corresponding board
    
    protected TSPiece(TSBlock[][] p, TSBoard<TSBlock> b){
        if( p.length != p[0].length )
            throw new RuntimeException("Size is not a square " +p.length + "x" + p[0].length );
        this.p = p;
        this.b = b;
        reset();
    }
    
    public void reset(){
    	if( b == null) return;
        //find first non empty space
    outer:for(int i = 0; i<p.length; i++)
        for(int j = 0; j<p.length; j++)
            if( p[i][j] != null){
                depth = p.length-i-1;
                setRow(depth - p.length+1);
                break outer;
            }
        while( rotation != 0)
            rotate();
        setCol((b.getCol()-p[0].length)/2);
    }
    
    public void setBoard(TSBoard<TSBlock> b){
        this.b = b;
    }
    
    // return: true if succesful
    public boolean moveLeft(){
        int oldcol = getCol();
        setCol(getCol() - 1);
        boolean collide = collides();
        if( collide ) setCol(oldcol);
        reposition();
        return collide;
    }
    
    // return: true if succesful
    public boolean moveRight(){
        int oldcol = getCol();
        setCol(getCol() + 1);
        boolean collide = collides();
        if( collide ) setCol(oldcol);
        reposition();
        return collide;
    }
    
    // return: false if hit bottom
    public boolean moveDown(){
        setDepth(getDepth() + 1);
        return reposition();
    }
    
    // return: false if cannot rotate
    public boolean rotateCW(){
        rotate();
        boolean rep = reposition();
        if( !rep ){
            for(int i = 0; i<3; i++)
                rotate();
            reposition();
        }
        return rep;
    }
    
    // return: false if cannot rotate
    public boolean rotateCCW(){
        for(int i = 0; i<3; i++)
            rotate();
        boolean rep = reposition();
        if( !rep ){
            rotate();
            reposition();
        }
        return rep;
    }
    
    public void drop(){
        setRow(getDrop());
        place(b);
    }
    
    public boolean isGood(int drop, TSBoard<TSBlock> b){
    	int lim = Math.min(b.getRow(), drop+p.length+2);
    	for(int c = 0; c<p[0].length && c+col < b.getCol(); c++){
    		if( c+col < 0)continue;
    		boolean hit = false;
    		for(int r = 0; r+drop < lim; r++){
    			if( r+drop < 0 )continue;
    			//System.out.println( r + " "  + c + ":" + (r+drop + " " + (c+col)));
    			if(  (r<p.length&&p[r][c] != null) || b.get(r+drop, c+col) != null) hit = true;
    			else if( hit) return false;
    		}
    	}
    	return true;
    }
    
    public boolean place(TSBoard<TSBlock> b){
        boolean good = true;
        for(int r = 0; r<p.length; r++)
            for(int c = 0; c<p[r].length; c++)
                if( p[r][c] != null && b.inBounds(r+row, c+col) ){
                	if(b.get(r+row, c+col) == null)
                		b.set(r+row, c+col, p[r][c]);
                	else {
                    	good = false;
                		b.set(r+row, c+col, new TSBlock(b.get(r+row, c+col).getColor().darker().darker()));
                	}
    			}else if( p[r][c] != null )
                    good = false;
        return good;
    }
    
    public TSPiece getGhost(){
        return new TSPieceGhost(this, b);
    }
    public TSPiece getGuide(){
        return new TSPieceGuide(this, b);
    }
    
    
    /*
     *
     * OTHER methods
     *
     */
    
    public void setRow(int r){
        row = r;
    }
    
    protected void setCol(int c){
        col = c;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public int getDepth(){
        return depth;
    }
    
    public void setDepth(int d){
        depth = d;
    }
    
    public int getRotation(){
        return rotation;
    }
    
    private static final int OUTLEFT = 1;
    private static final int OUTRIGHT = 1<<1;
    private static final int OUTBOTTOM = 1<<2;
    
    private int inBounds(){
        int bounds = 0;
        for(int r = 0; r<p.length; r++)
            for(int c = 0; c<p[r].length; c++)
                if( p[r][c] != null ){
                    int tr = row + r;
                    int tc = col + c;
                    if( tc < 0) bounds |= OUTLEFT;
                    if( tc >= b.getCol() ) bounds |= OUTRIGHT;
                    if( tr >= b.getRow() ) bounds |= OUTBOTTOM;
                }
        return bounds;
    }
    
    private boolean collides(){
        for(int r = 0; r<p.length; r++)
            for(int c = 0; c<p[r].length; c++)
                if( p[r][c] != null && b.inBounds(r+row, c+col) && b.get(r+row, c+col) != null )
                    return true;
        return false;
    }
    
    private static final int MAXSHIFT = 2;
    
    //returns whether repositioned within MAXSHIFT shifts up
    protected boolean reposition(){
    	
    	if( b == null)return true;
    	
        int bounds;
        
        //fit in bounds
        setRow(depth - p.length+1);
        while( (bounds = inBounds()) != 0 ){
            if( (bounds & OUTRIGHT) != 0)
                setCol(getCol()-1);
            if( (bounds & OUTLEFT) != 0)
                setCol(getCol()+1);
            if( (bounds & OUTBOTTOM) != 0)
                setRow(getRow()-1);
        }
        
        while(collides())
            setRow(getRow()-1);
        
        if( depth - p.length + 1 - getRow() >= MAXSHIFT )
            return false;
        
        return true;
        
    }
    
    public boolean isReal(){
        setRow(depth - p.length+1);
        boolean bad = (inBounds() != 0 || collides());
        reposition();
        return !bad;
    }
    
    private void rotate(){
        rotation = (rotation+1)%4;
        TSBlock[][] tmp = new TSBlock[p.length][p.length];
        for(int r = 0; r<p.length; r++)
            for(int c = 0; c<p.length; c++)
                tmp[r][c] = p[p.length-c-1][r];
        p = tmp;
    }
    
    protected int getDrop(){
        
        int oldrow = getRow();
        
        while(!collides() && (inBounds() & OUTBOTTOM) == 0)
            setRow(getRow()+1);
        
        int ret = getRow()-1;
        setRow(oldrow);
        
        return ret;
    }

    public String toString(){
    	return "TSPiece:"+getChar();
    }
    
}

