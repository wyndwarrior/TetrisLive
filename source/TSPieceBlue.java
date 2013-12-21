package wynd.tetris;
import java.awt.*;
public class TSPieceBlue extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock _ = null;
    private static final TSBlock B = new TSBlock(Color.BLUE);
    private static final TSBlock[][] M = {
    
    {B,_,_},
    {B,B,B},
    {_,_,_}
    
    };
    
    public TSPieceBlue (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'B';
    }
    
}


