package photoviewer;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

public class AnnotationMouseListener implements MouseListener {
	
	private PicViewer controller;
	private String label;
	private ImageIcon img;
	private int width;
	private int height;
	
	public LineAnnotation localLineAnnotation = new LineAnnotation(Color.RED, 1, false);
	public TextAnnotation localTextAnnotation = new TextAnnotation(Color.RED);

	public AnnotationMouseListener(PicViewer controller, String label, ImageIcon img, int width, int height) {
		// TODO Auto-generated constructor stub
		this.controller = controller;
		this.label = label;
		this.img = img;
		this.width = width;
		this.height = height;
	}

	@Override
    public void mouseClicked(MouseEvent e){
        if(e.getClickCount()==2){
        	controller.doubleClick();
        }
    }

	@Override
	public void mousePressed(MouseEvent e) {
		if (!controller.getModel().isFaceUp()) {
			if (e.getPoint().x <= img.getIconWidth() || e.getPoint().y <= img.getIconHeight()) {
				// TODO check if text or line annotation
				// make a text and a line annotation
				// if clicked again (after keys)
				localLineAnnotation.isActive = true;
				localLineAnnotation.points.add(e.getPoint());
			} else {
				localLineAnnotation.isActive = false;
				localLineAnnotation.points.clear();
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
	
	private void saveLineAnnotation() {
		controller.addLineAnnotation(localLineAnnotation);
		controller.repaint();
		localLineAnnotation.isActive = false;
		localLineAnnotation.points.clear();
	}

}
