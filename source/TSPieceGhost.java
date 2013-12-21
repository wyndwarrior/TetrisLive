package wynd.tetris;
import java.awt.*;

public class TSPieceGhost extends TSPiece{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    public static final TSBlock B = new TSBlock(new Color(0x55ACACAC));
    
    public TSPieceGhost (TSPiece pp, TSBoard<TSBlock> b){
        super(new TSBlock[pp.p.length][pp.p[0].length],b);
        
        for(int i = 0; i<p.length; i++)
            for(int j = 0; j<p[0].length; j++)
                if( pp.p[i][j] != null)
                    p[i][j] = B;
        
        setRow(pp.getDrop());
        setCol(pp.getCol());
        setDepth(pp.getDepth());
        
    }
    
}


