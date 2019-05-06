package wynd.tetris;
import java.awt.*;

public class TSPiecePurple extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock N = null;
    private static final TSBlock B = new TSBlock(Color.MAGENTA);
    private static final TSBlock[][] M = {
    
    {N,B,N},
    {B,B,B},
    {N,N,N}
    
    };
    
    public TSPiecePurple (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'P';
    }
    
}


