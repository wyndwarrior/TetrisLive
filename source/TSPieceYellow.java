package wynd.tetris;
import java.awt.*;

public class TSPieceYellow extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock B = new TSBlock(Color.YELLOW);
    private static final TSBlock[][] M = {
    
    {B,B},
    {B,B}
    
    };

    public int numRot(){
    	return 1;
    }
    
    public TSPieceYellow (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'Y';
    }
}


