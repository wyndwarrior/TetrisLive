package wynd.tetris;
import java.awt.*;
import java.io.*;

public class TSBlock implements Serializable{
    
	private static final long serialVersionUID = 7526472295622776147L;
	
    private Color color;
    
    public TSBlock(Color c){
        setColor(c);
    }
    
    public void setColor(Color c){
        color = c;
    }
    
    public Color getColor(){
        return color;
    }
    
    @Override
    public boolean equals(Object o){
        if( !(o instanceof TSBlock) )
            return false;
        if( o == this) return true;
        return color.equals(((TSBlock)o).getColor());
    }
    
    @Override
    public int hashCode(){
        return color.hashCode();
    }
    
    @Override
    public String toString(){
        return "[TSBlock: " +color + "]";
    }
    
}


