package wynd.tetris;

import java.util.ArrayList;

public class TSRenderPlayers implements TSRenderable{
	
	private TSRenderable parent;
	private int x, y, w;
	private ArrayList<TSPlayer> players;
	private ArrayList<TSRenderBoard> list;
	
	public TSRenderPlayers(int x, int y, int w, TSRenderable parent){
		this.parent = parent;
		setBounds(x,y,w);
	}
	
	public void setBounds(int xx, int yy, int ww){
		x = xx; y = yy;
		w = ww;
	}
	
	private static final double GAP = 0.03;
	
	public void update(int delta){
		int sides = (int)Math.ceil(Math.sqrt(getPlayers().size()));
		double gap = GAP * w;
		
		//side of square
		double s = (w - gap * sides) / sides;
		
		double offsetx = s / 4;
		double sizex = s / 2;

		list = new ArrayList<TSRenderBoard>();
		
		for(int i = 0; i<getPlayers().size(); i++){
			TSPlayer p = getPlayers().get(i);
			
			int r = i/sides;
			int c = i%sides;
			
			double x = c * (gap + s) + gap + offsetx + this.x;
			double y = this.y + w - (r + 1) * (gap + s);
			if( p!= null &&  p.getData() != null &&
					p.getData().getBoard() != null){
				TSRenderBoard rb = new TSRenderBoard(p.getData().getBoard(), 
						(int)x, (int)y, (int)sizex, (int)s);
				rb.setHold(p.getData().getHold());
				rb.setForesight(p.getData().getForesight());
				rb.setGarbage(p.getData().getGarbage());
				list.add(rb);
			}
			
		}
		
	}

	public void display(){
		for(TSRenderBoard rb : list){
			rb.render();
			//rb.renderHold();
			rb.renderGarbage();
			rb.renderForesight();
		}
	}
	
	public void init(){}
	public void pressedKey(int k){}
	public void releasedKey(int k){}
	public int getWidth(){
		return parent.getWidth();
	}
	public int getHeight(){
		return parent.getHeight();
	}

	public ArrayList<TSPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<TSPlayer> players) {
		this.players = players;
	}
}
