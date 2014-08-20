package wynd.tetris;
import java.awt.*;

public class TSPieceGreen extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private static final TSBlock _ = null;
    private static final TSBlock B = new TSBlock(Color.GREEN);
    private static final TSBlock[][] M = {
    
    {_,B,B},
    {B,B,_},
    {_,_,_}
    
    };
    
    public int numRot(){
    	return 2;
    }
    
    public TSPieceGreen (TSBoard<TSBlock> b){
        super(M,b);
        reposition();
    }
    
    public char getChar(){
    	return 'G';
    }
    
}


