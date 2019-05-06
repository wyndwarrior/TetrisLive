package wynd.tetris;
import java.awt.*;
public class TSPieceBlue extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock N = null;
    private static final TSBlock B = new TSBlock(Color.BLUE);
    private static final TSBlock[][] M = {
    
    {B,N,N},
    {B,B,B},
    {N,N,N}
    
    };
    
    public TSPieceBlue (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'B';
    }
    
}


