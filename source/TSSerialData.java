package wynd.tetris;

import java.io.*;
import java.util.*;

public class TSSerialData{
	
	public static boolean writeToFile(String name, Object o){
		try{
    		OutputStream file = new FileOutputStream( name );
    		OutputStream buffer = new BufferedOutputStream( file );
    		ObjectOutputStream output = new ObjectOutputStream( buffer );
    		try{
    			output.writeObject(o);
    			output.flush();
    		}finally{
    			output.close();
    		}
    	}catch(Exception e){
    		return false;
    	}
    	return true;
	}
	
	public static Object readFromFile(String name){
    	try{
    		InputStream file = new FileInputStream ( name );
    		InputStream  buffer = new BufferedInputStream ( file );
    		ObjectInputStream  input = new ObjectInputStream ( buffer );
    		try{
    			return input.readObject();
    		}finally{
    			input.close();
    		}
    	}catch(Exception e){
    		return null;
    	}
	}
	
	public static String serialPiece( TSPiece p){
		if( p == null)
			return "\0";
		return p.getChar()+"";
	}
	
	/*public static boolean writeBoard(String name, TSBoard<TSBlock> b){
		
		int[][] ar = new int[b.getRow()][b.getCol()];
		for(int i = 0; i<b.getRow(); i++)
			for(int j = 0; j<b.getCol(); j++){
				TSBlock bl = b.get(i,j);
				if( bl == null)
					ar[i][j] = 0;
				else ar[i][j] = bl.getColor().getRGB();
			}
		return writeToFile(name, ar);
	}*/
	
	public static String serialForesight(ArrayList<TSPiece> list){
		StringBuffer sb = new StringBuffer();
		for(TSPiece p : list)
			sb.append(p.getChar());
		return sb.toString();
	}
	
	public static ArrayList<TSPiece> readForesight(String s){
		ArrayList<TSPiece> list = new ArrayList<TSPiece>();
		for(char c : s.toCharArray())
			list.add(TSPiece.getPiece(c));
		return list;
	}
	
	public static TSPiece readPiece(String s){
		char c = s.charAt(0);
		return TSPiece.getPiece(c);
	}
	
}
