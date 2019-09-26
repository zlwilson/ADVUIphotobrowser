package photoviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public class PicViewer extends JComponent {
	private PVModel model;
	private PVView view;
	private Image img;
	
	public PicViewer(String label){
		setModel(new PVModel());
		setView(new PVView(label,this));
	}

	private void setView(PVView v) {
		this.view = v;
	}

	private void setModel(PVModel m) {
		this.model = m;
//		model.addChangeListener(event -> repaint());
	}
	
	public void addActionListener(ActionListener listener) {
		model.addActionListener(listener);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return view.getPreferredSize();
	}
	
	@Override
	public void paintComponent(Graphics g){
		System.out.println("trying to paint (PicViewer)");
		view.paint(g, this, this.img);
	}

	public void setImage(Image image) {
		this.img = image;
		System.out.println("image set (PicViewer)");
		this.repaint();
	}

	public void addChangeListener(ChangeListener changeListener) {
		model.addChangeListener(changeListener);
	}

	public PVModel getModel() {
		return this.model;
	}
}
