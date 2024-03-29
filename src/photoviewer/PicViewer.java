package photoviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public class PicViewer extends JComponent {
	private PVModel model;
	private PVView view;
	
	public PicViewer(String label){
		setModel(new PVModel());
		setView(new PVView(label,this));
				
		setFocusable(true);
	}

	private void setView(PVView v) {
		this.view = v;
	}

	private void setModel(PVModel m) {
		this.model = m;
		model.addChangeListener(e -> repaint());
	}
	
	@Override
	public Dimension getPreferredSize(){
		return view.getPreferredSize();
	}
	
	@Override
	public void paintComponent(Graphics g){
		view.paint(g, this);
	}

	public void setImage(ImageIcon image) {
		this.getModel().setImage(image);
		this.revalidate();
	}

	public void addChangeListener(ChangeListener changeListener) {
		model.addChangeListener(changeListener);
	}

	public PVModel getModel() {
		return this.model;
	}
	
	public void doubleClick() {
		this.model.setFaceUp();
	}

	public void addLineAnnotation(LineAnnotation la) {
		this.getModel().addLineAnnotation(la);
	}
	
	public void addTextAnnotation(TextAnnotation ta) {
		this.getModel().addTextAnnotation(ta);
	}

	public void clearAnnotations() {
		this.getModel().clearAnnotations();
		this.revalidate();
	}
	
	public void updateColor(Color c) {
		this.getModel().setColor(c);
		repaint();
	}
	
	public void updateSize(int i) {
		this.getModel().setSize(i);
		repaint();
	}

	public Color getColor() {
		return this.getModel().getColor();
	}

	public int getPenSize() {
		return this.getModel().getPenSize();
	}

	public boolean selectAnnotations(Point point) {
		return this.getModel().selectAnnotations(point);
	}

	public boolean isAnnotationSelected() {
		return this.getModel().isAnnotationSelected();
	}

	public void translate(Point selectionStart, Point currentPoint) {
		this.getModel().getSelectedAnnotation(selectionStart, currentPoint, this.getGraphics());
	}

	public void deselectAnnotations() {
		this.getModel().deselectAnnotations();
	}
}
