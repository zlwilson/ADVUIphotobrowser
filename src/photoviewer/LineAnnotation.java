package photoviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CShape;

public class LineAnnotation extends CPolyLine {
	
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
	
	// TODO fire change listeners instead of drawing?
	public void draw(Graphics g) {	
		for (int i = 0; i < this.points.size()-1; i++) {
			drawLine(g, this.points.get(i), this.points.get(i+1), this.size, this.color);
		}
	}
	
	private void drawLine(Graphics g, Point start, Point end, int size, Color c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(size));
		g2.setColor(c);
		g2.drawLine(start.x, start.y, end.x, end.y);
	}
}
