package photoviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class LineAnnotation {
	
	public boolean isActive;
	public ArrayList<Point> points = new ArrayList<>();
	public Color color;
	public int size;
	public boolean isSelected;
	
	public LineAnnotation(Color c, int s) {
		this.isActive = true;
		this.color = c;
		this.size = s;
		this.isSelected = false;
	}
	
	public LineAnnotation(Color c, int s, Boolean active) {
		this.isActive = active;
		this.color = c;
		this.size = s;
		this.isSelected = false;
	}
	
	// TODO fire change listeners instead of drawing?
	public void draw(Graphics g) {
		int stroke;
		if (isSelected) {
			stroke = size+2;
		} else {
			stroke = size;
		}
		for (int i = 0; i < this.points.size()-1; i++) {
			drawLine(g, this.points.get(i), this.points.get(i+1), stroke, this.color);
		}
	}
	
	private void drawLine(Graphics g, Point start, Point end, int size, Color c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(c);
		g2.drawLine(start.x, start.y, end.x, end.y);
		g2.setStroke(new BasicStroke(size));
	}

	public boolean mouseInside(Point point) {
		boolean hit = false;
		for (Point p : this.points) {
			if (point.x >= (p.x - 2) && point.x <= (p.x + 2) && point.y >= (p.y - 2) && point.y >= (p.y + 2)) {
				hit = true;
				this.isSelected = true;
				break;
			} else {
				this.isSelected = false;
			}
		}
		return hit;
	}
}
