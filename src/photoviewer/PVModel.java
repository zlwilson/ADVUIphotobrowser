package photoviewer;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	public ArrayList<LineAnnotation> lineAnnotations = new ArrayList<>();
	public ArrayList<TextAnnotation> textAnnotations = new ArrayList<>();
	
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
}
