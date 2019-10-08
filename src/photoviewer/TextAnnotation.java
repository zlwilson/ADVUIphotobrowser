package photoviewer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

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
	
	public TextAnnotation() {
		this.isActive = false;
		this.currentLine = 0;
		this.lines.add(currentLine, "");
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
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.color);
		int lineHeight = g.getFontMetrics().getHeight();
		int y = this.location.y;
		
		for (String line : this.lines) {
			if (line != null) {
				g2.drawString(line, this.location.x, y);
				y += lineHeight;
			}
		}
	}

	public void wrap(KeyEvent e, Graphics g, ImageIcon img) {
		FontMetrics fm = g.getFontMetrics();
		String currentLine = this.getLine();
		int currentLength = fm.stringWidth(currentLine);
		int lineHeight = fm.getHeight();
		
		// check if annotation reached the bottom of the photo, if at the bottom save and exit text annotation
		int textHeight = lineHeight * this.lines.size();
		if (this.location.y+textHeight >= img.getIconHeight()) {
			// end annotation at bottom of image
		} else if (currentLength+this.location.x >= img.getIconWidth()-2) {
			// check length of line to see if should wrap
			for (int i = currentLine.length()-1; i >= 0; i--) {
				
				// wrap on most recent space character if possible
				if (currentLine.charAt(i) == ' ') {
					String remaining = currentLine.substring(i+1);
					this.lines.remove(this.currentLine);
					this.lines.add(this.currentLine, currentLine.substring(0, i-1));
					remaining += Character.toString(e.getKeyChar());
					this.newLine(remaining);
					break;
				}
				
				// no space, therefore wrap on last character
				if (i == 0) {
					this.newLine(Character.toString(e.getKeyChar()));
				}
			}
		} else if (e.getKeyCode() == 10) {
			// return key creates new empty line
			this.newLine("");
		} else if (e.getKeyCode() == 8) {
			// backspace deletes character
			this.delete();
		} else {
			this.addText(Character.toString(e.getKeyChar()));
		}
	}
}