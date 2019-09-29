package photoviewer;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class LineAnnotation {
	
	public boolean isActive;
	public ArrayList<Point> points = new ArrayList<>();
	
	public LineAnnotation() {
		this.isActive = true;
	}
	
	public LineAnnotation(Boolean active) {
		this.isActive = active;
	}
}