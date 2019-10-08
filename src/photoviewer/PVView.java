package photoviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.transitions.LeaveOnShape;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;

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

					// TODO set selected
					// make a new selection tool

					if (localTextAnnotation.isActive) {
						// save annotation
						controller.addTextAnnotation(localTextAnnotation);
						localTextAnnotation = new TextAnnotation(color);
					} else {
						localTextAnnotation.location = e.getPoint();
						localTextAnnotation.color = color;
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
					localTextAnnotation = new TextAnnotation(color);
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

			if (model.isFaceUp()) {
				// paint image
				g.drawImage(img.getImage(), 0, 0, null);

			} else {
				// paint white background for annotations
//				g.setColor(Color.WHITE);
//				g.fillRect(0, 0, img.getIconWidth(), img.getIconHeight());
				
				g.drawImage(img.getImage(), 0, 0, null);

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
	
	CExtensionalTag selected = new CExtensionalTag() {
	    public void added(CShape s) { 
	        s.setOutlined(true).setStroke(new BasicStroke(2));
	    }
	    public void removed(CShape s) {
	        s.setStroke(new BasicStroke(0));
	    }
	};

	CStateMachine sm = new CStateMachine() {
		Paint initColor;
		public State start = new State() {
			Transition pressOnShape = new PressOnShape(BUTTON1) {
				public void action() {
					getShape().addTag(selected);
				}
			};
			Transition pressOnBackground = new Press(BUTTON1) {
				public void action() {
//					controller.newRectangle(getPoint().getX(), getPoint().getY(), 30, 20);
				}
			};
		};
		public State in = new State() {
			Transition leaveBox = new LeaveOnShape(">> out") {
				public void action() {
					getShape().setFillPaint(initColor);
				}
			};
		};
	};
}
