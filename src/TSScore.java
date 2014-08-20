package wynd.tetris;

public class TSScore {
	private int combo;
	private boolean b2b;
	private static final int[] scores = {
		0,1,2,4
	};
	private static final int[] combos = {
		0,1,1,2,2,3,3,4,4
	};
	
	public TSScore(){
		
	}
	
	public int process(int x){
		int score = 0;
		if( x == 0){
			combo = 0;
			return score;
		}
		if( x != 4) b2b = false;
		else {
			if( b2b )
				score ++;
			b2b = true;
		}
		score += scores[x-1];
		combo ++;
		if( combo > combos.length) combo = combos.length;
		score += combos[combo-1];
		
		return score;
		
	}
	
}
