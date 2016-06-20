import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

public class DShapeModel implements Comparable  {
	Color color;
	int x,y,width,height;
	Point[] knobArray;
	String text;
	int[] bounds;
	public DShapeModel() {
		color=Color.GRAY;
		x=0;
		y= 0;
		width=0;
		height=0;
		createBounds();
		
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Color getColor() {
		return color;
	}
	public void setX(int a) {
		x=a;
	}
	public void setY(int a) {
		y=a;
	}
	public void setWidth(int a) {
		width=a;
	}
	public void setHeight(int a) {
		height=a;
	}
	public void setColor(Color a) {
		color=a;
	}
	public int[] getBounds() {
		return bounds;
		
		
	}
	
	public void createBounds() {
		 bounds = new int[4];
		 bounds[0]= x; //low x
			bounds[1]=x+width; // high x
			bounds[2]=y; //low y
			bounds[3]=y+height;//high y
			
	}
	public void moveBy(int dx, int dy) { 
		 x += dx;
		 y += dy;
		 
	}
	public void moveKnob(int selectedknob,int dx, int dy) {
		int xLoc = (int) knobArray[selectedknob].getX();
		int yLoc = (int) knobArray[selectedknob].getY();
		knobArray[selectedknob].setLocation(xLoc+dx, yLoc+dy);
	}


	
	public int compareTo(Object other) {
		// TODO Auto-generated method stub
	if(Arrays.equals(((DShapeModel)other).getBounds(), this.getBounds())) {
		return 0;
	}
		return 1;
	}
	
	public void createKnobs() {
		knobArray=new Point[4];
		int a = x+width;
		int b =y+height;
		knobArray[0]=new Point(x-5,y-5); // top left corner
		knobArray[1]=new Point(a,y-5); //top right corner
		knobArray[2]=new Point(a,b); // bottom right corner
		knobArray[3]=new Point(x-5,b); //bottom left corner
	}
	
	public Point[] getKnobs() {
		return knobArray;
	}
	public void update() {
		createBounds();
		createKnobs();
	}
	
	
	
	public void resizeShape(int selectedknob,int opp, int dx, int dy) {
		Point p = knobArray[selectedknob];
		Point op=knobArray[opp];
		switch(selectedknob) {
		case 0: 
			knobArray[1]=new Point((int)op.getX(),(int)p.getY());
			knobArray[3]=new Point((int)p.getX(),(int)op.getY());
			x=(int)p.getX()+5;
			y=(int)p.getY()+5;
			width=(int)op.getX()-x;
			height=(int)op.getY()-y;
			
			break;
		case 1:
			knobArray[0]=new Point((int)op.getX(),(int)p.getY());
			knobArray[2]=new Point((int)p.getX(),(int)op.getY());
			x=(int)op.getX();
			y=(int)p.getY();
			width=(int)p.getX()-x; 
			height=(int)op.getY()-y+5;
			break;
		case 2:knobArray[1]=new Point((int)op.getX(),(int)p.getY());
				knobArray[3]=new Point((int)p.getX(),(int)op.getY());
				x=(int)p.getX();
				y=(int)op.getY();
				width=(int)op.getX()-x+5;
				height=(int)p.getY()-y;
				break;
		case 3:knobArray[0]=new Point((int)op.getX(),(int)p.getY());
				knobArray[2]=new Point((int)p.getX(),(int)op.getY()); 
				x=(int)op.getX()+5;
				y=(int)op.getY()+5;
				width=(int)p.getX()-x+5;
				height=(int)p.getY()-y+5;
				break;
		}
	}


}