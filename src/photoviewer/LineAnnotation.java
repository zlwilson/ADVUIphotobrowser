package photoviewer;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class LineAnnotation {
	
	public boolean isActive;
	public ArrayList<Point> points = new ArrayList<>();
	public Color color;
	public int size;
	
	public LineAnnotation(Color c, int s) {
		this.isActive = true;
		this.color = c;
		this.size = s;
	}
	
	public LineAnnotation(Color c, int s, Boolean active) {
		this.isActive = active;
		this.color = c;
		this.size = s;
	}
}
