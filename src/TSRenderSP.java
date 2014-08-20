package wynd.tetris;
import java.util.*;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class TSRenderSP implements TSRenderable{
    
    private TSTetris t;
    private TSRenderBoard rb;
    private TSRenderable parent;
    
    public TSRenderSP(TSRenderable parent){
    	this.parent = parent;
    }
    
    public int getWidth(){
    	return parent.getWidth();
    }
    
    public int getHeight(){
    	return parent.getHeight();
    }
    
    public void init(){
        
        //setFullscreen(true);
        
        t = new TSTetris();
        t.newGame();
        
        rb = new TSRenderBoard(null, 0, 0, getHeight()/2, getHeight());
        
        glEnable(GL_BLEND);
        glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        project();
    }
    
    private void project(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getWidth(), 0, getHeight(), 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }
    
    public static final int[] KMATCH = new int[70000];
    static{
        Arrays.fill(KMATCH, TSTetris.KLIM-1);
        KMATCH[KEY_LEFT] = TSTetris.KLEFT;
        KMATCH[KEY_RIGHT] = TSTetris.KRIGHT;
        KMATCH[KEY_DOWN] = TSTetris.KDOWN;
        KMATCH[KEY_UP] = TSTetris.KROTATECW;
        KMATCH[KEY_Z] = TSTetris.KROTATECCW;
        KMATCH[KEY_X] = TSTetris.KROTATECW;
        KMATCH[KEY_SPACE] = TSTetris.KDROP;
        KMATCH[KEY_LSHIFT] = TSTetris.KHOLD;
        KMATCH[KEY_RSHIFT] = TSTetris.KHOLD;
        KMATCH[KEY_S] = TSTetris.KSAVE;
        KMATCH[KEY_L] = TSTetris.KLOAD;
        
    }
    
    public void update(int delta){
        t.tick(delta);
    }

	public void pressedKey(int k) {
        t.pressed(KMATCH[getEventKey()]);
        if( KMATCH[getEventKey()] == TSTetris.KSAVE)
        	save();
        else if( KMATCH[getEventKey()] == TSTetris.KLOAD)
        	load();
	}

	public void releasedKey(int k) {
		t.released(KMATCH[getEventKey()]);
	}
    
    public boolean save(){
    	return TSSerialData.writeToFile("tetris.txt", t);
    }
    
    public void load(){
    	t = (TSTetris)TSSerialData.readFromFile("tetris.txt");
    }
    
    private static final int GAP = 30;
    
    public void display(){
        project();
        
        rb.setBoard(t.getBoard());
        
        //center board
        int bh = getHeight() - GAP * 2;
        int bw = bh / 2;
        rb.setSize((getWidth() - GAP * 2 - bw ) / 2 + GAP, GAP, bw, bh);
        
        rb.render();
        rb.renderForesight(t.getForesight());
        rb.renderHold(t.getHold());
    }
}
