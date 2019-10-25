package photoviewer;

import java.awt.BasicStroke;
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
	private Color color;
	private int penSize;

	public LineAnnotation localLineAnnotation = new LineAnnotation(color, penSize, false);
	public TextAnnotation localTextAnnotation = new TextAnnotation(color);
	private Graphics graphicContext;

	private Point selectionStart;

	public PVView(String label, PicViewer picViewer) {
		this.controller = picViewer;
		this.label = label;
		this.color = controller.getColor();
		this.penSize = controller.getPenSize();
		setupListeners();
	}

	private void setupListeners() {
		controller.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
				color = controller.getColor();
				controller.requestFocusInWindow();

				if(e.getClickCount()==2){
					controller.doubleClick();
				} else if (!controller.getModel().isFaceUp()) {

					if (localTextAnnotation.isActive) {
						// click out of text annotation to save
						controller.addTextAnnotation(localTextAnnotation);
					}

					if (controller.selectAnnotations(e.getPoint())) {
						// click on an annotation
//						controller.selectAnnotations(e.getPoint());
						selectionStart = e.getPoint();
						controller.repaint();
					} else  {
						localTextAnnotation = new TextAnnotation(e.getPoint(), color);
						controller.repaint();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!controller.getModel().isFaceUp()) {
					if (e.getPoint().x <= img.getIconWidth() || e.getPoint().y <= img.getIconHeight()) {
						color = controller.getColor();
						localLineAnnotation = new LineAnnotation(color, penSize);
						localLineAnnotation.isActive = true;
					} else {
						localLineAnnotation.isActive = false;
					}

					// save text when switching from editing text straight to drawing line
					if (localTextAnnotation.isActive) {
						controller.addTextAnnotation(localTextAnnotation);
						localTextAnnotation = new TextAnnotation(color);
					}
					
					if (!controller.selectAnnotations(e.getPoint())) {
						// deselect all annotations if press on empty canvas
						controller.deselectAnnotations();
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
					if (controller.isAnnotationSelected()) {
						// drag annotation if there is one selected
						controller.translate(selectionStart, e.getPoint());
						selectionStart = e.getPoint();
					} else if (e.getPoint().x >= img.getIconWidth() || e.getPoint().y >= img.getIconHeight()) {
						// mouse beyond image edges so stop and save line if one is active
						if (localLineAnnotation.isActive) {
							saveLineAnnotation();
						}
					} else {
						// drawing a line
						localLineAnnotation.isActive = true;
						localLineAnnotation.points.add(e.getPoint());
					}
					controller.repaint();
				}
			}
		});

		controller.addKeyListener(new KeyListener(){
			@Override
			public void keyReleased(KeyEvent e) {
				color = controller.getColor();
				penSize = controller.getPenSize();

				if (localTextAnnotation.isActive) {
					localTextAnnotation.wrap(e, graphicContext, img);
				}
				controller.repaint();
			}

			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
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

		this.color = controller.getColor();
		this.penSize = controller.getPenSize();

		if (img == null) {
			g.drawString("import a photo from the file menu", 10, 50);
		} else {
			// black background for photos
			g.setColor(Color.BLACK);
			g.fillRect(0,0, controller.getSize().width, controller.getSize().height);
			
			g.drawImage(img.getImage(), 0, 0, null);

			if (!model.isFaceUp()) {
				g.setColor(color);

				// draw past annotations
				drawLineAnnotations(g, controller.getModel().getLineAnnotations());
				drawTextAnnotations(g, controller.getModel().getTextAnnotations());

				// draw active annotations
				if (localLineAnnotation.isActive) {
					localLineAnnotation.draw(g);
				}

				if (localTextAnnotation.isActive) {
					localTextAnnotation.draw(g);
				}
			}
		}
	}

	// TODO use change listeners instead?

	private void drawTextAnnotations(Graphics g, ArrayList<TextAnnotation> textAnnotations) {
		for (TextAnnotation a : textAnnotations) {
			a.draw(g);
		}
	}

	private void drawLineAnnotations(Graphics g, ArrayList<LineAnnotation> lineAnnotations) {
		for (LineAnnotation a : lineAnnotations) {
			a.draw(g);
		}
	}
}
