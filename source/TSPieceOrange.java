package wynd.tetris;
import java.awt.*;

public class TSPieceOrange extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock _ = null;
    private static final TSBlock B = new TSBlock(Color.ORANGE);
    private static final TSBlock[][] M = {
    
    
    {_,_,B},
    {B,B,B},
    {_,_,_}
    
    };
    
    public TSPieceOrange (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'O';
    }
}


