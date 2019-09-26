package photoviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVView {

	private PicViewer controller;
	private JScrollPane photoContainer;
	private String label;
	
	public PVView(String label, PicViewer picViewer) {
		this.controller = picViewer;
		this.label = label;
		
		setupListeners();
	}
	
	private void setupListeners() {
		controller.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
			}			
		});
	}

	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(200,52);
	}

	public void paint(Graphics g, PicViewer picViewer, Image image) {
		// TODO Auto-generated method stub
		System.out.println("in paint (PVView)");
		PVModel model = this.controller.getModel();
		
		if (image == null) {
			g.drawString("import a photo from the file menu", 10, 50);
		}
		
		if (model.isFaceUp()) {
			// paint image
			g.drawImage(image, 0, 0, null);
		} else {
			// paint back for annotations
			g.drawImage(image, 0, 0, null);
		}
	}

	public void paintSIMPLE(Graphics g, PicViewer picViewer) {
		System.out.println("in paint (PVView)");
		g.fillRect(0, 0, 30, 30);
	}
}
