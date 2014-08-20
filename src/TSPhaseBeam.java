package wynd.tetris;
import java.util.*;
import java.awt.*;
import java.io.Serializable;

import static org.lwjgl.opengl.GL11.*;

public class TSPhaseBeam implements Serializable{
    
	private static final long serialVersionUID = 7526472295622776147L;
    
    private int w,h;
    private ArrayList<PBCircle> left;
    private ArrayList<PBCircle> right;
    
    public TSPhaseBeam(int w, int h){
        setDim(w,h);
        genCircles();
    }
    
    //initially create circles
    public void genCircles(){
        left = new ArrayList<PBCircle>();
        right = new ArrayList<PBCircle>();
        
        int N1 = (int)(Math.random() * 5) + 7;
        int N2 = (int)(Math.random() * 5) + 7;
        
        for(int i = 0; i<N1; i++){
            PBCircle c = getBlueCircle();
            c.y = Math.random() * h;
            left.add(c);
        }
        for(int i = 0; i<N2; i++){
            PBCircle c = getPurpleCircle();
            c.y = Math.random() * h;
            right.add(c);
        }
    }
    
    public void setDim(int w, int h){
        this.w = w;
        this.h = w;
    }
    
    //http://www.java-gaming.org/index.php/topic,4140.
    private void circle(Color innerColor, Color outerColor, int slices, double radius, double x, double y)
    {
        double incr = 2 * Math.PI / slices;
        
        glBegin(GL_TRIANGLE_FAN);
        
        setColor(innerColor);
        glVertex2d(x,y);
        setColor(outerColor);
        
        for(int i = 0; i < slices; i++){
            double angle = incr * i;
            
            double xx = Math.cos(angle) * radius;
            double yy = Math.sin(angle) * radius;
            
            glVertex2d(x+xx, y+yy);
        }
        
        glVertex2d(radius + x , y);
        
        glEnd();
    }
    
    
    //diag gradient color
    private static final Color color1 = new Color(31,13,121,255);
    private static final Color color2 = new Color(0,7,31,255);
    
    private void drawDiagGradient(){
        
        //slope
        double m1 = -h/(double)w;
        double m2 = -1/m1; //perpendicular slope
        
        //y intercept
        double b1 = h - m1 * w;
        double b2 = h;
        
        //top left
        double x1 = (b2 - b1) / (m1 - m2);
        double y1 = x1 * m1 + b1;
        
        //bottom left
        double x2 = -x1;
        double y2 = x2 * m2 + b2;
        
        //bottom right
        double x3 = w - x1;
        double y3 = x3 * m1;
        
        //top right
        double x4 = w + x1;
        double y4 = x4 * m1 + b1;
        
        
        glBegin(GL_QUADS);
            setColor(color1);
            glVertex2d(x1,y1);
            glVertex2d(x2,y2);
            setColor(color2);
            glVertex2d(x3,y3);
            glVertex2d(x4,y4);
        glEnd();
        
    }
    
    
    //circle colors
    //big blue gradient color
    private static final Color color3 = new Color(6,132,208,0);
    private static final Color color4 = new Color(6,132,208,255);
    
    //big purple gradient
    private static final Color color5 = new Color(132,28,175,0);
    private static final Color color6 = new Color(132,28,175,255);
    
    //small purple circle
    private static final Color color7 = new Color(132,28,175,7);
    private static final Color color8 = new Color(132,28,175,9);
    
    //small blue circle
    private static final Color color9 = new Color(65,176,255,7);
    private static final Color color10 = new Color(65,176,255,9);
    
    private void drawCircleGradients(){
        circle(color6, color5, 100 , w * 0.8 , w * 0.1 , h * 0.7);
        circle(color4, color3, 100 , w * 0.8 , w * 0.1 , h * 0.9);
    }
    
    private PBCircle getBlueCircle(){
        double r = Math.random()* w / 12. + 15;
        return new PBCircle( Math.random() * w / 2  , -r*1.5 ,0.001+Math.random()*0.004,Math.random()*0.02+0.005,r , (int)(Math.random()*7+5), color9, color10);
    }
    private PBCircle getPurpleCircle(){
        double r = Math.random()* w / 12. + 15;
        return new PBCircle( Math.random() * w / 2  + w / 2. , -r*1.5 ,0.001+Math.random()*0.004,Math.random()*0.008+0.003,r , (int)(Math.random()*7+5), color7, color8);
    }
    
    private static final int NSLICE = 50;
    private static final double BLURDIST = 0.3;
    
    //max number of circles
    private static final int MAXCIRCLE = 35;
    
    //moving blurred circle
    private class PBCircle implements Serializable{
        
    	private static final long serialVersionUID = 7526472295622776147L;
        double x,y,vx,vy,r;
        double BLUR,BLURINC,BLURSTART,BLUREND;
        Color c1, c2;
        
        PBCircle(double x, double y, double vx, double vy, double r, int blur, Color c1, Color c2){
            this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.r = r;
            
            BLUR = blur;
            BLURINC = BLURDIST/BLUR;
            BLURSTART = 1 + BLURINC;
            BLUREND = 1 + BLURDIST;
            
            this.c1 = c1; this.c2 = c2;
        }
        
        public void tick(int delta){
            x += vx * delta;
            y += vy * delta;
        }
        
        public boolean shouldUnrender(){
            return x + r < 0 || /*y + r < 0 ||*/ x - r >= w || y - r >= h;
        }
        
        public void render(){
            
            for(double i = BLURSTART; i<= BLUREND; i+=BLURINC)
                circle(c1, c2, NSLICE , r*i , x , y);
        }
        
        public String toString(){
            return String.format("(%.2f,%.2f), r:%.2f", x,y,r);
        }
        
    }
    
    private void setColor(Color c){
        glColor4d(c.getRed()/255.,c.getGreen()/255.,c.getBlue()/255.,c.getAlpha()/255.);
    }
    
    private int time = 0;
    
    public void tick(int delta){
        process(left, delta);
        process(right, delta);
        
        time += delta;
        
        //generate circle every second with higher chance if less circles
        if( time >= 1000){
            time %= 1000;
            if( Math.random() <= (MAXCIRCLE - (left.size() + right.size()))/(double)MAXCIRCLE ){
                if( Math.random() < .5)
                    left.add(getBlueCircle());
                else
                    right.add(getPurpleCircle());
            }
        }
    }
    
    //tick each circle, remove if necessary
    private void process(ArrayList<PBCircle> list, int delta){
        for(int i = 0; i<list.size(); i++){
            PBCircle c = list.get(i);
            if( c.shouldUnrender() )
                list.remove(i--);
            else
                c.tick(delta);
        }
    }
    
    private void renderList(ArrayList<PBCircle> list){
        for(PBCircle c : list)
            c.render();
    }
    
    public void render(){
        drawDiagGradient();
        drawCircleGradients();
        
        renderList(left);
        renderList(right);
        
    }
    
}
