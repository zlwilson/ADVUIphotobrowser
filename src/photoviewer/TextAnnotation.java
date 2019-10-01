package photoviewer;

import java.awt.Point;
import java.util.ArrayList;

public class TextAnnotation {
	public ArrayList<String> lines = new ArrayList<>();
	public int currentLine;
	public Point location;
	public Boolean isActive;
	
	public TextAnnotation() {
		this.isActive = false;
		this.currentLine = 0;
		this.lines.add(currentLine, "");
	}

	public TextAnnotation(Point p) {
		this.location = p;
		this.isActive = true;
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
	
	public void delete() {
		if (this.lines.size() == 0) {
			// empty lines, do nothing
			
		} else if (this.lines.get(currentLine).length() == 0) {
			this.lines.remove(currentLine);
			this.currentLine = currentLine-1;
		} else {
			String s = this.lines.get(currentLine).substring(0, this.lines.get(currentLine).length()-1);
			this.lines.remove(currentLine);
			this.lines.add(currentLine, s);
		}
	}
}