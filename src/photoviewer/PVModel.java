package photoviewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	private ArrayList<LineAnnotation> lineAnnotations = new ArrayList<>();
	private ArrayList<TextAnnotation> textAnnotations = new ArrayList<>();
	private ImageIcon image;
	private TextAnnotation selectedText;
	private LineAnnotation selectedLine;

	private boolean faceUp = true;

	private Color annotationColor = Color.RED;
	private int penSize = 1;

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public PVModel getModel() {
		return this;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(){
		faceUp = !faceUp;
		fireChangeListener();
	}

	public void showPhoto(){
		faceUp = true;
		fireChangeListener();
	}

	private void fireChangeListener() {
		for (ChangeListener l : changeListeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	public void setImage(ImageIcon img) {
		this.image = img;
	}

	public void addLineAnnotation(LineAnnotation la) {
		lineAnnotations.add(la);
	}

	public ArrayList<LineAnnotation> getLineAnnotations() {
		return this.lineAnnotations;
	}

	public void addTextAnnotation(TextAnnotation ta) {
		textAnnotations.add(ta);
	}

	public ArrayList<TextAnnotation> getTextAnnotations() {
		return this.textAnnotations;
	}

	public void clearAnnotations() {
		this.lineAnnotations = new ArrayList<>();
		this.textAnnotations = new ArrayList<>();
	}

	public void setColor(Color c) {
		this.annotationColor = c;
		if (this.isTextSelected()) {
			for (TextAnnotation t : this.textAnnotations) {
				if (t.isSelected) {
					t.color = c;
				}
			}
		}
		if (this.isLineSelected()) {
			for (LineAnnotation l : this.lineAnnotations) {
				if (l.isSelected) {
					l.color = c;
				}
			}
		}
	}

	public void setSize(int i) {
		this.penSize = i;
		if (this.isLineSelected()) {
			for (LineAnnotation l : this.lineAnnotations) {
				if (l.isSelected) {
					l.size = i;
				}
			}
		}
	}

	public Color getColor() {
		return this.annotationColor;
	}

	public int getPenSize() {
		return this.penSize;
	}

	public ImageIcon getImage() {
		return this.image;
	}

	public boolean selectAnnotations(Point point) {
		if (this.selectText(point) || this.selectLine(point)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean selectLine(Point point) {
		boolean onLine = false;
		for (LineAnnotation l : this.lineAnnotations) {
			if (l.mouseInside(point)) {
				onLine = l.mouseInside(point);
				this.selectedLine = l;
				break;
			}
		}
		return onLine;
	}

	private boolean selectText(Point point) {
		boolean onText = false;
		for (TextAnnotation t : this.textAnnotations) {
			if (t.mouseInside(point)) {
				onText = t.mouseInside(point);
				this.selectedText = t;
				break;
			}
		}
		return onText;
	}

	public boolean isAnnotationSelected() {
		if (this.isTextSelected() || this.isLineSelected()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isLineSelected() {
		boolean selected = false;
		for (LineAnnotation l : this.lineAnnotations) {
			if (l.isSelected) {
				selected = true;
				break;
			}
		}
		return selected;
	}

	private boolean isTextSelected() {
		boolean selected = false;
		for (TextAnnotation t : this.textAnnotations) {
			if (t.isSelected) {
				selected = true;
				break;
			}
		}
		return selected;
	}

	public void getSelectedAnnotation(Point selectionStart, Point currentPoint, Graphics g) {
		// get selected annotation and move it based on the translation from selectionStart and currentPoint
		int deltaX = currentPoint.x - selectionStart.x;
		int deltaY = currentPoint.y - selectionStart.y;

		if (isTextSelected()) {
			for (TextAnnotation t : this.textAnnotations) {
				if (t.isSelected) {
					t.location.x += deltaX;
					t.location.y += deltaY;
					t.wrapSimple(g, image);
				}
			}
		}
		if (isLineSelected()) {
			for (LineAnnotation l : this.lineAnnotations) {
				if (l.isSelected) {
					for (Point p : l.points) {
						p.x += deltaX;
						p.y += deltaY;
					}
				}
			}
		}
	}

	// clear all selections
	public void deselectAnnotations() {
		this.selectedLine = null;
		this.selectedText = null;
		for (LineAnnotation l : this.lineAnnotations) {
			l.isSelected = false;
		}
		for (TextAnnotation t : this.textAnnotations) {
			t.isSelected = false;
		}
	}
}
