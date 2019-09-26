package photoviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVView {

	private PicViewer controller;
	private String label;
	private ImageIcon img;
	private int width;
	private int height;
	
	public LineAnnotation localAnnotation = new LineAnnotation();
	
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
		        }
		    }

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO draw strokes
				if (!controller.getModel().isFaceUp()) {
					localAnnotation.active = true;
					localAnnotation.points.add(e.getPoint());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					localAnnotation.points.add(e.getPoint());
//					controller.addLineAnnotation(localAnnotation);
					controller.getModel().addLineAnnotation(localAnnotation);
					
					System.out.println("PVView - localA[].size: "+ localAnnotation.points.size());
					
					controller.repaint();
					
					System.out.println("PVView - localA[1]: "+ localAnnotation.points.get(1));
					
					localAnnotation.active = false;
					localAnnotation.points.clear();
					
					System.out.println("PVView - la[] size: "+ controller.getModel().lineAnnotations.size());
					System.out.println("PVView - la[1].points[] size: "+ controller.getModel().lineAnnotations.get(0).points.size());
					System.out.println("PVView - la[1].points[0]: "+ controller.getModel().lineAnnotations.get(0).points.get(0));
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
		});
		
		controller.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					localAnnotation.points.add(e.getPoint());
					controller.repaint();
				}
			}
		});
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
				drawLines(g, controller.getModel().lineAnnotations);
				
				// draw active annotation
				if (localAnnotation.active) {
					drawCurve(g, localAnnotation);
				}
			}
		}
	}

	public void setImage(ImageIcon image) {
		this.img = image;
	}
	
	// draw all lines in line annotation list (from the model)
	private void drawLines(Graphics g, ArrayList<LineAnnotation> lineAnnotations) {
		for (int i = 0; i < lineAnnotations.size(); i++) {
			drawCurve(g, lineAnnotations.get(i));
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
