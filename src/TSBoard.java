package wynd.tetris;
import java.util.*;
import java.io.*;

public class TSBoard<E> implements Serializable{
    
	private static final long serialVersionUID = 7526472295622776147L;
	
    private static final int DEFAULTROW = 20;
    private static final int DEFAULTCOL = 10;
    private ArrayList<ArrayList<E> > mat;
    private int row, col;
    
    public TSBoard(){
        this(DEFAULTROW,DEFAULTCOL);
    }
    
    public TSBoard(int r, int c){
        if( r <= 0 || c <= 0 )
            throw new RuntimeException("Invalid row or col: " + r + " " + c);
            
        row = r;
        col = c;
        
        mat = new ArrayList<ArrayList<E> >(row);
        for(int i = 0; i<row; i++){
            mat.add(new ArrayList<E>(col));
            for(int j = 0; j<col; j++)
                mat.get(i).add(null);
        }
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public boolean inBounds(int r, int c){
        return !( r < 0 || c < 0 || r >= getRow() || c >= getCol() );
    }
    
    public E get(int r, int c){
        if( !inBounds(r,c) )
            throw new RuntimeException("Board index out of bounds: " + r + " " + c);
        return mat.get(r).get(c);
    }
    
    public E set(int r, int c, E b){
        if( !inBounds(r,c) )
            throw new RuntimeException("Board index out of bounds: " + r + " " + c);
        return mat.get(r).set(c,b);
    }
    
    @Override @SuppressWarnings({"unchecked"})
    public boolean equals(Object o){
        if( !(o instanceof TSBoard))
            return false;
        if( o == this ) return true;
        
        TSBoard<E> b2 = (TSBoard<E>) o;
        if( b2.getRow() != getRow() || b2.getCol() != getCol() )
            return false;
        
        for(int i = 0; i<getRow(); i++)
            for(int j = 0; j<getCol(); j++){
                E a = get(i,j), b = b2.get(i,j);
                if( a == null || b == null ){
                    if( a != b) return false;
                }else{
                    if( a != b && !a.equals(b) )
                        return false;
                }
            }
        return true;
    }
    
    public static final int NULLHASH = 52489;
    
    @Override
    public int hashCode(){
        int h = getRow() * 37 + getCol();
        for(int i = 0; i<getRow(); i++)
            for(int j = 0; j<getCol(); j++){
                E t = get(i,j);
                int hash = t == null ? NULLHASH : t.hashCode();
                h = h*23 + hash;
            }
        return h;
    }
    
    @Override
    public String toString(){
        return "[TSBoard: " + getRow() + "x" + getCol() +"]";
    }
    
    public String fullToString(){
        StringBuffer sb = new StringBuffer();
        sb.append(toString());
        sb.append("\n");
        for(int i = 0; i<getRow(); i++)
            sb.append(mat.get(i)+"\n");
        return sb.toString();
    }
    
    //display null vs not-null
    public String smallToString(){
        StringBuffer sb = new StringBuffer();
        sb.append(toString());
        sb.append("\n");
        for(int i = 0; i<getRow(); i++){
            for(int j = 0; j<getCol(); j++)
                sb.append(get(i,j) == null ? '.' : 'o' );
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //shallow copy
    public TSBoard<E> copy(){
        TSBoard<E> c = new TSBoard<E>(getRow(), getCol());
        for(int i = 0; i<getRow(); i++)
            for(int j = 0; j<getCol(); j++)
                c.set(i,j,get(i,j));
        return c;
    }
    
}

