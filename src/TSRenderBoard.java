package wynd.tetris;
import java.util.*;
import static org.lwjgl.opengl.GL11.*;

public class TSRenderBoard{
    
    private TSBoard<TSBlock> b;
    
    private ArrayList<TSPiece> foresight;
    private TSPiece hold;
    private int x, y, w, h;
    private int garbage;
    
    public TSRenderBoard(TSBoard<TSBlock> b, int x, int y, int w, int h){
        setBoard(b);
        setSize(x,y,w,h);
    }
    
    public void setSize(int xx, int yy, int ww, int hh){
        x = xx; y = yy; w = ww; h = hh;
    }
    
    public void setBoard(TSBoard<TSBlock> b){
        this.b = b;
    }
    
    private static final double RATIO = 8;
    private static final double BOARDER = 0.3;
    
    public static final double MULT1 = .8;
    public static final double MULT2 = 1-MULT1;
    
    public static final double MULT3 = .5;
    public static final double MULT4 = 1-MULT3;
    public static final double ALPHA = 1;
    
    public static final double BT = 2; //boarder thickness
    
    
    private int row, col;
    private double gap, sizer, sizec, width, height;
    
    //useful vars
    private void recalc(){
        row = b.getRow();
        col = b.getCol();
        gap = h / ( row * (RATIO+1) + 1 );
        sizer = gap * RATIO;
        sizec = (w - gap * (col + 1) ) / col;
        width = (sizec + gap ) *4 + gap;
        height = gap + (gap + sizer) * 4;
    }
    
    public void render(){
        recalc();
        glBegin(GL_QUADS);
        
        lineColor();
        frame(0,0,w,h, BT);
        
        for(int i = 0; i<row; i++)
            for(int j = 0; j<col; j++)
                renderBlock(b.get(i,j),
                            gap + (gap + sizec) * j,
                            gap + (gap + sizer) * i,
                            sizer, sizec);
        
        glEnd();
        
    }
    
    private void lineColor(){
        glColor4d(1,1,1,.7);
    }
    
    private void renderBlock(TSBlock bl, double x, double y, double sizer, double sizec){
        
        double bd = sizer * BOARDER;
        
        
        if( bl == null ){
            glColor4d(1,1,1,.1);
            square(x,y,
                   x + sizec, y + sizer);
            
        }else{
            
            lineColor();
            square(x,y,
                   x + sizec, y + sizer);
            
            glColor4d(bl.getColor().getRed()/255.*MULT1 + MULT2,
                      bl.getColor().getGreen()/255.*MULT1 + MULT2,
                      bl.getColor().getBlue()/255.*MULT1 + MULT2,
                      bl.getColor().getAlpha()/255*ALPHA);
            
            final double bd2 = Math.round(bd/3);
            
            square(x + bd2, y + bd2,
                   x + sizec - bd2, y + sizer - bd2);
            
            //lineColor();
            
            glColor4d(bl.getColor().getRed()/255.*MULT3 + MULT4,
                      bl.getColor().getGreen()/255.*MULT3 + MULT4,
                      bl.getColor().getBlue()/255.*MULT3 + MULT4,
                      bl.getColor().getAlpha()/255*ALPHA);
                      
            
            square(x + bd, y + bd,
                   x + sizec - bd, y + sizer - bd);
        }
        
    }
    
    private void line(double x1, double y1,
                      double x2, double y2,
                      double thick){
        thick /= 2;
        square(x1-thick, y1-thick,
               x2+thick, y2+thick);
    }
    
    private static final double SHRINK = 1.25;
    public void renderForesight(){
    	renderForesight(getForesight());
    }
    
    public void renderGarbage(){
    	renderGarbage(getGarbage());
    }
    
    public void renderGarbage(int x){
        if( x  == 0) return;
        
        recalc();
        glColor4d(1,.3,.3,.9);
        
        double left = w + sizec;
        double top = h - x * (sizec+gap);
        
        glBegin(GL_QUADS);
        square(left, top, left + gap * 3, h);
        glEnd();
    }
    
    public void renderForesight(ArrayList<TSPiece> list){
        recalc();
        
        glBegin(GL_QUADS);
        lineColor();
        
        double left = w + sizec * 2 + gap * 3;
        

        //shrink
        sizer /= SHRINK;
        sizec /= SHRINK;
        gap /= SHRINK;
        width = gap + (gap + sizec) * 5;
        height = gap + (gap + sizer) * 5;
        
        frame(left, 0, left+width, h, BT);
        
        for( int i = 0; i<5; i++){
            TSPiece pp = list.get(i);
            TSBlock[][] p = pp.p;
            double offsetx = (width - gap - (gap+sizec) * p.length) / 2 + gap + left;
            double offsety = (height - gap - (gap+sizer) * p.length) / 2 + gap + height*i;
            renderPiece(pp, offsetx, offsety);
        }
        glEnd();
    }
    
    private void renderPiece(TSPiece pp, double offsetx, double offsety){
        TSBlock[][] p = pp.p;
        
        for(int j = 0; j<p.length; j++)
            for(int k = 0; k<p[j].length; k++)
                if( p[j][k] != null)
                    renderBlock(p[j][k],
                                offsetx + (gap + sizec) * k,
                                offsety + (gap + sizer) * j,
                                sizer, sizec);
    }
    
    public void renderHold(){
    	renderHold(getHold());
    }
    
    public void renderHold(TSPiece hold){
        recalc();
        
        glBegin(GL_QUADS);
        lineColor();
        double left = -sizec - gap * 3 - width;
        frame(left, 0, left+width, height, BT);
        
        glEnd();
        
        if( hold == null ) return;
        
        glBegin(GL_QUADS);
        
        TSBlock[][] p = hold.p;
        double offsetx = (width - gap - (gap+sizec) * p.length) / 2 + gap + left;
        double offsety = (height - gap - (gap+sizer) * p.length) / 2 + gap;
        renderPiece(hold, offsetx, offsety);
        
        glEnd();
    }
    
    private void frame(double x1, double y1,
                       double x2, double y2,
                       double T){
        /*//horizontal
        line(x1-T, -T, x2+T, -T, T);
        line(x1-T, y2+T, x2+T, y2+T, T);
        
        //vertical
        line(x2+T, y1, x2+T, y2, T);
        line(x1-T, y1, x1-T, y2, T);*/
        
        final double G = T*2;
        
        //horizontal
        line(x1+G, -G, x2-G, -G, T);
        line(x1+G, y2+G, x2-G, y2+G, T);
        
        //vertical
        line(x2+G, y1+G, x2+G, y2-G, T);
        line(x1-G, y1+G, x1-G, y2-G, T);
    }
    
    private void square(double x1, double y1,
                        double x2, double y2){
        vertex(x1,y1);
        vertex(x1,y2);
        vertex(x2,y2);
        vertex(x2,y1);
    }
    
    private void vertex(double x, double y){
        glVertex2d(x + this.x, h-y + this.y);
    }

	public ArrayList<TSPiece> getForesight() {
		return foresight;
	}

	public void setForesight(ArrayList<TSPiece> foresight) {
		this.foresight = foresight;
	}

	public TSPiece getHold() {
		return hold;
	}

	public void setHold(TSPiece hold) {
		this.hold = hold;
	}

	public int getGarbage() {
		return garbage;
	}

	public void setGarbage(int garbage) {
		this.garbage = garbage;
	}
    
}

