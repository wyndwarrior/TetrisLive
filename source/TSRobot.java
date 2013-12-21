package wynd.tetris;

import java.util.*;

public class TSRobot {
	public TSRobot(){
	}
	
	public TSPiece nextMove(ArrayList<TSPiece> fs, TSPiece p, TSBoard<TSBlock> b){
		TSPiece[] ar = new TSPiece[fs.size()+1];
		TSPiece[] tp = new TSPiece[fs.size()+1];
		tp[0] = p.copy();
		for(int i = 0; i<fs.size(); i++)
			tp[i+1] = fs.get(i).copy();
		findMove(tp, b.copy(), 0, ar);
		//System.out.println(ar[0]);
		return ar[0];
	}
	
	int canyons(TSBoard<TSBlock> b){
		
		int tot = 0;
		
    	for(int c = 1; c<b.getCol(); c++)
    		for(int r = b.getRow()-1; r>=3; r--)
    			if(b.get(r, c) == null){
    				int length = 0;
    				for(int rr = r; rr>=0; rr--, length++){
    					if( c != 1 && b.get(rr, c-1)== null)
    						break;
    					if( c != b.getCol()-1 && b.get(rr, c+1)==null)
    						break;
    				}
					if( length >=2) tot += length;
    				break;
    			}
    	return tot;
	}
	
	public boolean canTetris(TSBoard<TSBlock> b){
		for(int i = 1; i<b.getCol(); i++)
			for(int j = b.getRow()-4; j<b.getRow(); j++){
				if( b.get(j,i) == null)
					return false;
			}
		return true;
	}
	
	public double getStDv(TSBoard<TSBlock> b){
		
		int bottom;
		outer:for(bottom = b.getRow()-1; bottom>=0; bottom--){
			for(int i = 1; i<b.getCol(); i++)
				if( b.get(bottom, i) == null)
					break outer;
		}
		
		double mean = 0;
		int tot = 0;
		for(int r = bottom; r>=0; r--)
			for(int c = 1; c<b.getCol(); c++)
				if( b.get(r, c) != null){
					tot ++;
					mean += bottom-r;
				}
		if( tot == 0) return 0;
		mean /= tot;
		
		//System.out.println(mean);
		
		double stdv = 0;
		int num = 0;
		
		for(int r = bottom; r>=0; r--)
			for(int c = 1; c<b.getCol(); c++)
				if( b.get(r, c) == null && r < mean || 
					b.get(r, c) != null && r > mean){
					num ++;
					stdv += Math.pow(mean-(bottom-r), 2);
				}
		if( num == 0 )return 0;
		return Math.sqrt(stdv/num);
	}
	
	double Pheight = 5;
	double Pcanyon = 30;
	double Pstdv = 30;
	double Pcleared = 25;
	
	public int findMove(TSPiece[] fs, TSBoard<TSBlock> b, int cur, TSPiece[] ar){
		if( cur >= 6){
			b = TSTetris.clearBoard(b, new int[1]);
			int height = b.getRow();
			outer: for(int r = 0; r<b.getRow(); r++)
				for(int c = 0; c<b.getCol(); c++)
					if( b.get(r,c) != null){
						height = r;
						break outer;
					}
			int canyon = canyons(b);
			//System.out.println(canyon);
			//System.out.println(canyon);
			return (int)(height*Pheight - canyon*Pcanyon - getStDv(b)*Pstdv);
		}
		int max = Integer.MIN_VALUE;
		TSPiece p = fs[cur];
		p.setBoard(b);
		
		
		if( p.getChar() == 'L' && canTetris(b)){
			
			
			p.rotateCW();
			
			int prevCol = p.getCol();
			do{
				prevCol = p.getCol();
				p.moveLeft();
			}while( prevCol != p.getCol() );
			
			p.setRow(p.getDrop());
			
			TSBoard<TSBlock> tb = b.copy();
			p.place(tb);
			int[] scores = new int[1];
			tb = TSTetris.clearBoard(tb,  scores);
			int cleared = scores[0]*2;
			int score = findMove(fs, tb, cur+1, ar) + (int)(Pcleared*cleared);
			if( score > max){
				TSPiece pp = p.copy();
				ar[cur] = pp;
				max = score;
			}
			
			p.reset();
		}
		
		//System.out.println(p.numRot());
		for(int i = 0; i<p.numRot(); i++){
			//System.out.println("c"+p.getCol());
			int prevCol = p.getCol();
			do{
				prevCol = p.getCol();
				p.moveLeft();
			}while( prevCol != p.getCol() );

			p.moveRight();
			do{
				prevCol = p.getCol();
				//System.out.println("cc"+p.getCol());
				int d = p.getDrop();
				if( p.isGood(d,b)){
					int oldRow = p.getRow();
					TSBoard<TSBlock> tb = b.copy();
					p.setRow(d);
					p.place(tb);
					int[] scores = new int[1];
					tb = TSTetris.clearBoard(tb,  scores);
					int cleared = scores[0];
					int score = findMove(fs, tb, cur+1, ar) + (int)(Pcleared*cleared);
					if( score > max){
						TSPiece pp = p.copy();
						ar[cur] = pp;
						max = score;
					}
					p.setRow(oldRow);
				}
				p.moveRight();
			}while(prevCol != p.getCol());
			//System.out.println(max);
			p.rotateCW();
			p.reposition();
			p.setCol(b.getCol()/2);
		
		}
		
		p.reset();
		return max;
	}
}
