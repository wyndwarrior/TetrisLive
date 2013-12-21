package wynd.tetris;
import java.util.*;

import org.lwjgl.input.Keyboard;
//import org.lwjgl.util.Renderable;

import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.opengl.GL11.*;

public class TSServerView extends MCWindow implements TSRenderable{
    
    private TSRenderPlayers rp;
    private TSServer server;
    
    private ArrayList<TSRenderable> rlist;
    
    public TSServerView(){
        super( 800, 600 );
        //super( 400, 300 );
    }
    
    public void init(){
        
        //setFullscreen(true);
    	rlist = new ArrayList<TSRenderable>();

    	rlist.add(new TSRenderPB(this));
    	rlist.add(rp = new TSRenderPlayers(0,0,0,this));
    	
    	for(TSRenderable r : rlist)
    		r.init();
        
        server = new TSServer();
        new Thread(server).start();
    }
    public void update(int delta){
        
        ArrayList<TSUser> list = server.getUsers();
        ArrayList<TSPlayer> players = new ArrayList<TSPlayer>();
        if( list != null)
            for(TSUser u : list){
	        	if( u == null || u.getPlayer() == null )continue;
	        	
	        	TSData d = u.getPlayer().getData();
	        	if( d != null)
	        		players.add(u.getPlayer());
	        }
        rp.setBounds(GAP, GAP, getHeight()-GAP);
        rp.setPlayers(players);
        
    	for(TSRenderable r : rlist)
    		r.update(delta);
        while (Keyboard.next())
            if (Keyboard.getEventKeyState())
            	pressedKey(getEventKey());
            else
                releasedKey(getEventKey());
    }

	public void pressedKey(int k) {
	    for(TSRenderable r : rlist)
	    	r.pressedKey(k);
	}

	public void releasedKey(int k) {
	    for(TSRenderable r : rlist)
	    	r.releasedKey(k);
	}
    
    private static final int GAP = 30;
    
    public void display(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    	for(TSRenderable r : rlist)
    		r.display();
    }
    
    public static void main(String[] args){
        TSServerView t = new TSServerView();
        t.start();
        System.exit(0);
    }
    
}
