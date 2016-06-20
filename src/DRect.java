
import java.awt.Graphics;

public class DRect extends DShape {

	public DRect() {
		dshapemodel=new DRectModel();
	}
	 
	public void draw(Graphics g) {
		g.setColor(dshapemodel.getColor());
		g.fillRect(dshapemodel.getX(), dshapemodel.getY(), dshapemodel.getWidth(), dshapemodel.getHeight());	
		
	}
	
}