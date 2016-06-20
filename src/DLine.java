
import java.awt.Graphics;

public class DLine extends DShape {

	public DLine() {
		dshapemodel=new DLineModel();
	}
	 
	public void draw(Graphics g) {
		int length=dshapemodel.getWidth();
		g.setColor(dshapemodel.getColor());
		g.drawLine(dshapemodel.getX(),dshapemodel.getY(),dshapemodel.getX()+length,dshapemodel.getY());	
		
	}
	
}