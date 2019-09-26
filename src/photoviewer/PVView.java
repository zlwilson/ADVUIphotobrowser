package photoviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVView {

	private PicViewer controller;
	private String label;
	private ImageIcon img;
	
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
		if (img != null) {
			int imgWidth = img.getIconWidth();
			int imgHeight = img.getIconHeight();
			System.out.println("Preferred size = image");
			return new Dimension(imgWidth,imgHeight);
		}
		System.out.println("Preferred size != image");
		return new Dimension(200,52);
	}

	public void paint(Graphics g, PicViewer picViewer) {
		// TODO Auto-generated method stub
		System.out.println("in paint (PVView)");
		PVModel model = this.controller.getModel();
		
		if (img == null) {
			g.drawString("import a photo from the file menu", 10, 50);
		} else {
			if (model.isFaceUp()) {
				// paint image
				g.drawImage(img.getImage(), 0, 0, null);
			} else {
				// paint back for annotations
				g.drawImage(img.getImage(), 0, 0, null);
			}
		}
	}

	public void setImage(ImageIcon image) {
		this.img = image;
	}
}
