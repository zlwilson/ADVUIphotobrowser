package photoviewer;

public class Main {

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ImageRotator");	
		
		PhotoViewer viewer = new PhotoViewer();
		viewer.setVisible(true);
	}
}
