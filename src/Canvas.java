import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class Canvas extends JPanel{
	ArrayList<DShape> shapes;
	Graphics g; 
	Point start; 
	Point end;
	DShapeModel lastShape;
	int[] lastBounds;
	int lastX,lastY;
	ArrayList<DShapeModel> shapesmodel;
	int knobNumber;
	DShape temp;
	boolean shapeselected;
	public Canvas(){
		lastBounds=new int[4];
		shapesmodel=new ArrayList<>();
		shapeselected=false;
		setBackground(Color.WHITE);
		MouseHandler mouse = new MouseHandler();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setSize(400,400);
		shapes=new ArrayList<>();
		  addMouseListener( new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	            	
	            	
	            	
	            	
	            	if(lastShape!=null && knobSelected(e.getX(),e.getY()))  {
	            		shapeselected=true;
	            	
	            		
	            	} else {

		            	DShapeModel currentShape=findModel(e.getX(),e.getY());
		            	lastShape=currentShape;
	           
	            	if(lastShape!=null) {
	            	
	            		lastBounds=currentShape.getBounds();
	            	lastX=lastBounds[0];
	            	lastY=lastBounds[2];
	            	for(int i = 0 ; i < shapesmodel.size();i++) {           //compares list of dshapemodels
	            		if(lastShape.compareTo(shapesmodel.get(i))==0) {	//sees if any dshapemodel is the same
	            			shapes.get(i).drawKnobs(getGraphics());			// draws knobs
	            		}
	            	}
	            	repaint();
	            	}
	            	shapeselected=false;
	            	}
	            	
	            }
	            
	        });
		  
		  
		  addMouseListener(new MouseAdapter() {
			  public void mouseReleased(MouseEvent e) {
				  shapeselected=false;
			
				  
			  }
		  });
		  
		  addMouseMotionListener( new MouseMotionAdapter() {
	            public void mouseDragged(MouseEvent e) {
	                
	            	if(lastShape!=null && shapeselected) {	
	            		 	int dx = e.getX()-lastX;
		                    int dy = e.getY()-lastY;
		                    lastX=e.getX();
		                    lastY=e.getY();
	            		int opposite=(knobNumber+2)%4;
	            		knobMove(lastShape,knobNumber,opposite,dx,dy);
	       
	            		repaint();
	            	
	            	} else {          	
	            	if (lastShape != null) {
	                	
	                    int dx = e.getX()-lastX;
	                    int dy = e.getY()-lastY;
	                    lastX=e.getX();
	                    lastY=e.getY();
	                    doMove(lastShape, dx, dy);
	                	lastShape.update();
	                    repaint();
	                }
	            	}
	            }
	        });
		  
		  
		  
		  
	}
	public void knobMove(DShapeModel model,int knobnum,int opp, int dx, int dy) {
		model.moveKnob(knobnum, dx, dy);
		model.resizeShape(knobnum,opp ,dx, dy);
	}
	
	
	public boolean knobSelected(int x, int y) {

		
	int size=lastShape.getKnobs().length;
		for(int i = 0 ; i < size;i++) {
			Rectangle boundary=new Rectangle((int)lastShape.getKnobs()[i].getX(),(int)lastShape.getKnobs()[i].getY(),10,10);
		
			if(boundary.contains(x, y)) {
				knobNumber=i;
				return true;
			}
		}
		return false;
	}
	
	
	
	
	public void doMove(DShapeModel model, int dx, int dy) {
		model.moveBy(dx, dy);
	}
	
	public void addShape(DShapeModel a) {
		shapesmodel.add(a);
		System.out.println(shapeselected);
		if(a instanceof DRectModel) {
			DRect rect=new DRect();
			rect.dshapemodel=a;
			addShape(rect);
			rect.draw(getGraphics());
			
		}
			if(a instanceof DOvalModel) {
			DOval oval = new DOval();
			oval.dshapemodel=a;
			addShape(oval);
			oval.draw(getGraphics());
		}
			if(a instanceof DLineModel) {
				DLine line=new DLine();
				line.dshapemodel=a;
				addShape(line);
				line.draw(getGraphics());
			}
			if(a instanceof DTextModel) {
				
				DText text= new DText();
				text.dshapemodel=a;
				addShape(text);
				text.draw(getGraphics());
			}
	}
	
	
	public void addShape(DShape a) {
		shapes.add(a);
		a.draw(getGraphics());
	}	
	
	
	/*
	 * Finds the Dshapemodel given a point
	 * 
	 */
	public DShapeModel findModel(int x, int y) {
		int size= shapesmodel.size();
		for(int i = 0 ; i < size; i++) {
			DShapeModel myShapeModel=shapesmodel.get(i);
			myShapeModel.createBounds();
			int[] bounds = myShapeModel.getBounds();
			if(bounds[0]<x && bounds[1]>x) {
				if(bounds[2]<y && bounds[3]>y) {
					return myShapeModel;
				}
			}
		}
		return null; 
	}
	
	
	
	private class MouseHandler extends MouseAdapter implements MouseMotionListener{
		public void mousePressed(MouseEvent event){
			start = event.getPoint();
		}
		public void mouseReleased(MouseEvent event){
			end = event.getPoint();
		}
		
	}
	
	
	public void paintComponent(Graphics g){	
		super.paintComponent(g);
		for(DShapeModel currentModel:shapesmodel) {
			if(currentModel instanceof DRectModel) {
				DRect x = new DRect();
				x.dshapemodel=currentModel;
			
				x.draw(g);
				if(lastShape!=null) {
				if(currentModel.compareTo(lastShape)==0) {
					x.drawKnobs(g);
				}
				}
			}
			if(currentModel instanceof DOvalModel) {
				DOval x=new DOval();
				x.dshapemodel=currentModel;
				x.draw(g);
				if(lastShape!=null) {
				if(currentModel.compareTo(lastShape)==0) {
					x.drawKnobs(g);
				}
				}
		
			}
			if(currentModel instanceof DLineModel) {
				DLine x=new DLine();
				x.dshapemodel=currentModel;
			
				x.draw(g);
				if(lastShape!=null) {
				if(currentModel.compareTo(lastShape)==0) {
					x.drawKnobs(g);
					
				}
				}
			}
		}
	}
	public void update() {
		repaint();
	}
	
	
}