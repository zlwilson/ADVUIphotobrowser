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
	
	public LineAnnotation localAnnotation = new LineAnnotation(null, null);
	
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
					localAnnotation.start = e.getPoint();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					localAnnotation.end = e.getPoint();
					controller.addLineAnnotation(localAnnotation);
					localAnnotation = new LineAnnotation(null,null);
					controller.repaint();
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
					localAnnotation.end = e.getPoint();
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
				// paint back for annotations
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, img.getIconWidth(), img.getIconHeight());
				
				g.setColor(Color.RED);
				drawLines(g, controller.getModel().lineAnnotations);
				if (localAnnotation.start != null && localAnnotation.end != null) {
					drawLine(g, localAnnotation);
				}
			}
		}
	}

	public void setImage(ImageIcon image) {
		this.img = image;
	}
	
	// draw all lines in line annotation list (from the model)
	private void drawLines(Graphics g, ArrayList<LineAnnotation> lineAnnotations) {
		Graphics2D g2d = (Graphics2D) g;
		
		for (int i = 0; i < lineAnnotations.size(); i++) {
			drawLine(g, lineAnnotations.get(i));
		}
		
	}
	
	// draw a line based on a line annotation object
	private void drawLine(Graphics g, LineAnnotation la) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(la.start.x, la.start.y, la.end.x, la.end.y);
	}
}
