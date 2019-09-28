package photoviewer;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class TextAnnotation {
	public ArrayList<String> lines = new ArrayList<>();
	public int currentLine;
	public Point location;
	public Boolean isActive;
	public Color color;
	
	public TextAnnotation(Color c) {
		this.isActive = false;
		this.currentLine = 0;
		this.lines.add(currentLine, "");
		this.color = c;
	}

	public TextAnnotation(Point p, Color c) {
		this.location = p;
		this.isActive = true;
		this.color = c;
	}
		
	public void addText(String str) {
		String s = this.lines.get(currentLine) + str;
		this.lines.remove(currentLine);
		this.lines.add(currentLine, s);
	}
	
	public void setActive(Point p) {
		this.location = p;
		this.isActive = true;
	}
	
	public void newLine(String s) {
		this.currentLine++;
		this.lines.add(currentLine, s);
	}

	public String getLine() {
		return this.lines.get(currentLine);
	}
}
