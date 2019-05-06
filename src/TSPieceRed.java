package wynd.tetris;
import java.awt.*;

public class TSPieceRed extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock N = null;
    private static final TSBlock B = new TSBlock(Color.RED);
    private static final TSBlock[][] M = {
    
    {B,B,N},
    {N,B,B},
    {N,N,N}
    
    };
    
    public int numRot(){
    	return 2;
    }
    
    public TSPieceRed (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'R';
    }
}


