import java.awt.Graphics;
public class DText extends DShape {
	
	public DText() {
		dshapemodel=new DTextModel();
		
	
	}
	public void draw(Graphics g) {
		g.setFont(g.getFont());
		g.setColor(dshapemodel.getColor());
		g.drawString(dshapemodel.text,dshapemodel.getX(),dshapemodel.getY());
	}
}