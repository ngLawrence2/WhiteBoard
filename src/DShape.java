import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class DShape {
	DShapeModel dshapemodel;
	public void draw(Graphics g) {
		
	}
	public Point[] getKnobs() {
	return dshapemodel.getKnobs();
	}
	public int[] getBounds() {
		return dshapemodel.getBounds();
		
	}
	public void drawKnobs(Graphics g) {
		g.setColor(Color.BLACK);
		for(int i = 0 ; i < getKnobs().length;i++) {
			Point p = getKnobs()[i];
			g.fillRect((int)p.getX(),(int)p.getY(), 10, 10);
		
		}
		
	}
	

}