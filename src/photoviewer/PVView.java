package photoviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PVView {

	private PicViewer controller;
	private String label;
	private ImageIcon img;
	private int width;
	private int height;
	
	public LineAnnotation localLineAnnotation = new LineAnnotation(false);
	public TextAnnotation localTextAnnotation = new TextAnnotation();
	
	
	public PVView(String label, PicViewer picViewer) {
		this.controller = picViewer;
		this.label = label;
		
		setupListeners();
	}
	
	private void setupListeners() {
		controller.addMouseListener(new MouseListener(){
		    @Override
		    public void mouseClicked(MouseEvent e){
		        if(e.getClickCount()==2){
		        	controller.doubleClick();
		        } else if (!controller.getModel().isFaceUp()) {
		        	// TODO text annotation
		        	// if existing text annotation -> save, else create new
		        	if (localTextAnnotation.isActive) {
		        		// save annotation
		        	} else {
		        		localTextAnnotation.location = e.getPoint();
		        		localTextAnnotation.isActive = true;
		        		controller.repaint();
		        	}
		        }
		    }

			@Override
			public void mousePressed(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					if (e.getPoint().x <= img.getIconWidth() || e.getPoint().y <= img.getIconHeight()) {
						localLineAnnotation = new LineAnnotation();
						localLineAnnotation.active = true;
					} else {
						localLineAnnotation.active = false;
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					if (localLineAnnotation.active) {
						saveLineAnnotation();
					}
					System.out.println("PVView - MouseReleased: " + controller.getModel().lineAnnotations.size());
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) {
				if (localLineAnnotation.active) {
					saveLineAnnotation();
				}
			}
		});
		
		controller.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					// its not a text annotation
					localTextAnnotation = new TextAnnotation();
					controller.repaint();
					
					if (e.getPoint().x >= img.getIconWidth() || e.getPoint().y >= img.getIconHeight()) {
						if (localLineAnnotation.active) {
							saveLineAnnotation();
						}
					} else {
						localLineAnnotation.active = true;
						localLineAnnotation.points.add(e.getPoint());
					}
				}
			}
		});
	}
	
	private void saveLineAnnotation() {
		controller.addLineAnnotation(localLineAnnotation);
		controller.repaint();
		localLineAnnotation.active = false;
	}

	public Dimension getPreferredSize() {
		if (img != null) {
			return new Dimension(img.getIconWidth(),img.getIconHeight());
		} else {
			width = 300;
			height = 200;
		}
		return new Dimension(width, height);
	}
	
	public Dimension setMinimumSize() {
		return new Dimension(300, 200);
	}

	public void paint(Graphics g, PicViewer picViewer) {
		PVModel model = this.controller.getModel();
		
		if (img == null) {
			g.drawString("import a photo from the file menu", 10, 50);
		} else {
			// black background for photos
			g.setColor(Color.BLACK);
			g.fillRect(0,0, controller.getSize().width, controller.getSize().height);
			
			if (model.isFaceUp()) {
				// paint image
				g.drawImage(img.getImage(), 0, 0, null);
			} else {
				// paint white background for annotations
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, img.getIconWidth(), img.getIconHeight());
				
				g.setColor(Color.RED);
				
				// draw past annotations
				drawAnnotations(g, controller.getModel().getLineAnnotations());
				
				// draw active annotation
				if (localLineAnnotation.active) {
					drawCurve(g, localLineAnnotation);
				}
			}
		}
	}

	public void setImage(ImageIcon image) {
		this.img = image;
	}
	
	// draw all lines in line annotation list (from the model)
	private void drawAnnotations(Graphics g, ArrayList<LineAnnotation> lineAnnotations) {
		for (LineAnnotation a : lineAnnotations) {
			drawCurve(g, a);
		}
	}
	
	// draw a curve based on a line annotation object
	private void drawCurve(Graphics g, LineAnnotation annotation) {		
		for (int i = 0; i < annotation.points.size()-1; i++) {
			drawLine(g, annotation.points.get(i), annotation.points.get(i+1));
		}
	}
	
	// draw a line based on two points
	private void drawLine(Graphics g, Point start, Point end) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}
}
