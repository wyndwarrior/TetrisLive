package wynd.tetris;
import java.util.*;
import java.io.*;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class TSRenderMP implements TSRenderable{
    
    private TSTetris t;
    private TSRenderBoard rb;
    private TSRenderPlayers rp;
    private TSConnection conn;
    private TSRenderable parent;
    private String host;
    
    public TSRenderMP(TSRenderable parent, String host){
    	this.parent = parent;
    	this.host = host;
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
        rp = new TSRenderPlayers(0,0,0, this);
        rp.init();

		/*String ip = "localhost";
		
		try{
			ip = new Scanner(new File("ip.txt")).nextLine();
		}catch(Exception e){}*/
		//System.out.print("Server IP: ");
		//String ip = new Scanner(System.in).nextLine();
		
        conn = new TSConnection(host);
        //conn = new TSConnection("localhost");
        new Thread(conn).start();
        
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
        KMATCH[KEY_SPACE] = TSTetris.KDROP;
        KMATCH[KEY_LSHIFT] = TSTetris.KHOLD;
        KMATCH[KEY_RSHIFT] = TSTetris.KHOLD;
        KMATCH[KEY_R] = TSTetris.KROBOT;
    }

	public void pressedKey(int k) {
        t.pressed(KMATCH[getEventKey()]);
	}

	public void releasedKey(int k) {
		t.released(KMATCH[getEventKey()]);
	}
    
    private static final int GAP = 30;
    private static final double SCALE = 0.8;
    
    public void update(int delta){
        t.tick(delta);
        
        rb.setBoard(t.getBoard());
        int bh = getHeight() - GAP * 2;
        bh *= SCALE;
        rb.setSize( GAP + bh/4, (getHeight()-bh)/2, bh/2, bh);
        bh /= SCALE;
        
        rp.setBounds(GAP*2 + bh*3/4, (getHeight()-bh*3/4-GAP)/2 + GAP, bh*3/4-GAP/2);

        rp.setPlayers(conn.getPlayers());
        if( conn.getPlayers() == null )
        	rp.setPlayers(new ArrayList<TSPlayer>());
        rp.update(delta);
        
        
        if( t.linesSent() != 0){
        	conn.addMessage("send " + t.linesSent());
        	t.resetLines();
        }
        conn.setData(new TSData(t.getBoard(), t.getHold(), t.getForesight(), t.getGarbage()));
        
        if( !conn.getMessages().isEmpty()){
        	ArrayList<String> msg = conn.getMessages();
        	conn.clearMessages();
        	
        	for(String s : msg){
        		if( s.matches("send \\d+")){
        			String[] sr = s.split(" ");
        			t.addGarbage(Integer.parseInt(sr[1]));
        		}
        	}
        }
        
    }
    
    public void display(){
        project();
        
        rb.render();
        rb.renderForesight(t.getForesight());
        rb.renderHold(t.getHold());
        rb.renderGarbage(t.getGarbage());
        
        rp.display();
        
    }
    
}
