package photoviewer;

import java.awt.Color;
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
		this.selectedText.color = c;
//		this.selectedLine.color = c;
	}

	public void setSize(int i) {
		this.penSize = i;
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
		// TODO Auto-generated method stub
		boolean insideAnnotation;
		insideAnnotation = this.selectText(point) || this.selectLine(point);
		return insideAnnotation;
	}

	private boolean selectLine(Point point) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean selectText(Point point) {
		// TODO Auto-generated method stub
		System.out.println("Model: selectText @ " + point);
		boolean onText = false;
		for (TextAnnotation t : this.textAnnotations) {
			if (t.mouseInside(point)) {
				onText = t.mouseInside(point);
				this.selectedText = t;
			}
		}
		return onText;
	}
}
