package wynd.tetris;
import static org.lwjgl.opengl.GL11.*;

public class TSRenderPB implements TSRenderable{
    
    private TSPhaseBeam pb;
    private TSRenderable parent;
    
    public TSRenderPB(TSRenderable parent){
    	this.parent = parent;
    }
    
    public int getWidth(){
    	return parent.getWidth();
    }
    
    public int getHeight(){
    	return parent.getHeight();
    }
    
    public void init(){
        
        pb = new TSPhaseBeam(getWidth(), getHeight());
        
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
    
    public void update(int delta){
        pb.tick(delta);
    }

	public void pressedKey(int k) {}

	public void releasedKey(int k) {}
    
    public void display(){
        project();
        
        pb.setDim(getWidth(), getHeight());
        pb.render();
        
    }
    
}
