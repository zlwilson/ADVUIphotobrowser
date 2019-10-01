package photoviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private Graphics graphicContext;
	
	
	public PVView(String label, PicViewer picViewer) {
		this.controller = picViewer;
		this.label = label;
		
		setupListeners();
	}
	
	private void setupListeners() {
		controller.addMouseListener(new MouseListener(){
			// I know this should be its own declared class and I really tried! but it kept breaking :(
		    @Override
		    public void mouseClicked(MouseEvent e){
		        if(e.getClickCount()==2){
		        	controller.doubleClick();
		        } else if (!controller.getModel().isFaceUp()) {
		        	if (localTextAnnotation.isActive) {
		        		// save annotation
		        		controller.addTextAnnotation(localTextAnnotation);
		        		localTextAnnotation = new TextAnnotation();
		        	} else {
		        		localTextAnnotation.location = e.getPoint();
		        		localTextAnnotation.isActive = true;
		        		localTextAnnotation.addText("");
		        		controller.repaint();
		        	}
		        }
		    }

			@Override
			public void mousePressed(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					if (e.getPoint().x <= img.getIconWidth() || e.getPoint().y <= img.getIconHeight()) {
						localLineAnnotation = new LineAnnotation();
						localLineAnnotation.isActive = true;
					} else {
						localLineAnnotation.isActive = false;
					}
					
					// save text when switching from editing text straight to drawing line
					if (localTextAnnotation.isActive) {
						controller.addTextAnnotation(localTextAnnotation);
		        		localTextAnnotation = new TextAnnotation();
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					if (localLineAnnotation.isActive) {
						saveLineAnnotation();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) {
				if (localLineAnnotation.isActive) {
					saveLineAnnotation();
				}
			}
		});
		
		controller.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					// its not a text annotation, so clear text annotation
					localTextAnnotation = new TextAnnotation();
					controller.repaint();
					
					if (e.getPoint().x >= img.getIconWidth() || e.getPoint().y >= img.getIconHeight()) {
						if (localLineAnnotation.isActive) {
							saveLineAnnotation();
						}
					} else {
						localLineAnnotation.isActive = true;
						localLineAnnotation.points.add(e.getPoint());
					}
				}
			}
		});
		
		controller.addKeyListener(new KeyListener(){
			
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (localTextAnnotation.isActive) {
					FontMetrics fm = graphicContext.getFontMetrics();
					String currentLine = localTextAnnotation.getLine();
					int currentLength = fm.stringWidth(currentLine);
					int lineHeight = fm.getHeight();
					
					// check if annotation reached the bottom of the photo
					// if at the bottom save and exit text annotation
					int textHeight = lineHeight * localTextAnnotation.lines.size();
					if (localTextAnnotation.location.y+textHeight >= img.getIconHeight()) {
						controller.addTextAnnotation(localTextAnnotation);
		        		localTextAnnotation = new TextAnnotation();
					} else if (currentLength+localTextAnnotation.location.x >= img.getIconWidth()-2) {
						// check length of line to see if should wrap
						for (int i = currentLine.length()-1; i >= 0; i--) {
							
							// wrap on most recent space character if possible
							if (currentLine.charAt(i) == ' ') {
								String remaining = currentLine.substring(i+1);
								localTextAnnotation.lines.remove(localTextAnnotation.currentLine);
								localTextAnnotation.lines.add(localTextAnnotation.currentLine, currentLine.substring(0, i-1));
								remaining += Character.toString(e.getKeyChar());
								localTextAnnotation.newLine(remaining);
								break;
							}
							
							// no space, therefore wrap on last character
							if (i == 0) {
								localTextAnnotation.newLine(Character.toString(e.getKeyChar()));
							}
						}
					} else if (e.getKeyCode() == 10) {
						// return key creates new empty line
						localTextAnnotation.newLine("");
					} else if (e.getKeyCode() == 8) {
						// backspace deletes character
						localTextAnnotation.delete();
					} else {
						localTextAnnotation.addText(Character.toString(e.getKeyChar()));
					}
				}
				controller.repaint();
			}
		});
	}
	
	private void saveLineAnnotation() {
		controller.addLineAnnotation(localLineAnnotation);
		controller.repaint();
		localLineAnnotation.isActive = false;
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
		this.graphicContext = g;
		PVModel model = this.controller.getModel();
		this.img = this.controller.getModel().getImage();
		
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
				drawLineAnnotations(g, controller.getModel().getLineAnnotations());
				drawTextAnnotations(g, controller.getModel().getTextAnnotations());
				
				// draw active annotations
				if (localLineAnnotation.isActive) {
					drawCurve(g, localLineAnnotation);
				}

				if (localTextAnnotation.isActive) {
					drawString(g, localTextAnnotation);
				}
			}
		}
	}
	
	// draw all lines in line annotation list (from the model)
	private void drawTextAnnotations(Graphics g, ArrayList<TextAnnotation> textAnnotations) {
		for (TextAnnotation a : textAnnotations) {
			drawString(g, a);
		}
	}
	
	private void drawString(Graphics g, TextAnnotation a) {
		Graphics2D g2 = (Graphics2D) g;
		int lineHeight = g.getFontMetrics().getHeight();
		int y = a.location.y;
		
		for (String line : a.lines) {
			if (line != null) {
				g2.drawString(line, a.location.x, y);
				y += lineHeight;
			}
		}
	}

	// draw all lines in line annotation list (from the model)
	private void drawLineAnnotations(Graphics g, ArrayList<LineAnnotation> lineAnnotations) {
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
		Graphics2D g2 = (Graphics2D) g;
		g2.drawLine(start.x, start.y, end.x, end.y);
	}
}
