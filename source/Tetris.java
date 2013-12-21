package wynd.tetris;
import java.util.*;

import org.lwjgl.input.*;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class Tetris extends MCWindow implements TSRenderable{
    
    private ArrayList<TSRenderable> list;
    private boolean multiplayer;
    private String host;
    
    public Tetris(String h, boolean m){
        super( 900, 600 );
        //super( 400, 300 );
    	host = h;
    	multiplayer = m;
    }
    
    public void init(){
    	//setFullscreen(true);
    	list = new ArrayList<TSRenderable>();
    	list.add(new TSRenderPB(this));
    	if( multiplayer)
    		list.add(new TSRenderMP(this, host));
    	else
    		list.add(new TSRenderSP(this));
    	for(TSRenderable r : list)
    		r.init();
    }
    
    public void update(int delta){
    	for(TSRenderable r : list)
    		r.update(delta);
        while (Keyboard.next())
            if (Keyboard.getEventKeyState())
            	pressedKey(getEventKey());
            else
                releasedKey(getEventKey());
    }
    
	public void pressedKey(int k){
	    for(TSRenderable r : list)
	    	r.pressedKey(k);
    }
    
    public void releasedKey(int k){
    	for(TSRenderable r : list)
    		r.releasedKey(k);
    }
    
    
    public void display(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    	for(TSRenderable r : list)
    		r.display();
    }
    
    public static void main(String[] args){
    	Scanner file = new Scanner(System.in);
    	System.out.print("Multiplayer? (y/n): ");
    	boolean multiplayer = file.next().trim().toLowerCase().equals("y");
    	String host = null;
    	if( multiplayer ){
    		System.out.print("Server IP: ");
    		host = file.next().trim();
    	}
        Tetris t = new Tetris(host, multiplayer);
        t.start();
        System.exit(0);
    }
    
}
