package photoviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public class PicViewer extends JComponent {
	private PVModel model;
	private PVView view;
	
	public PicViewer(String label){
		setModel(new PVModel());
		setView(new PVView(label,this));
	}

	private void setView(PVView v) {
		this.view = v;
	}

	private void setModel(PVModel m) {
		this.model = m;
		model.addChangeListener(event -> repaint());
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
		this.view.setImage(image);
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
		System.out.println("PicViewer - add line annotation, size: " + la.points.size());
		this.getModel().addLineAnnotation(la);;
	}
}
