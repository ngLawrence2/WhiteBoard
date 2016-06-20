import java.awt.Graphics;
public class DOval extends DShape {
	
	public DOval() {
		dshapemodel=new DOvalModel();
	}
	public void draw(Graphics g) {
		g.setColor(dshapemodel.getColor());
		g.fillOval(dshapemodel.getX(), dshapemodel.getY(), dshapemodel.getWidth(), dshapemodel.getHeight());
	}
}