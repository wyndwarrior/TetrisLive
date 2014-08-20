package wynd.tetris;

public interface TSRenderable {
	public void update(int delta);
	public void display();
	public void init();
	public void pressedKey(int k);
	public void releasedKey(int k);
	public int getWidth();
	public int getHeight();
}
