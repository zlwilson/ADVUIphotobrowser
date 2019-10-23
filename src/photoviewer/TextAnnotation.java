package photoviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class TextAnnotation {
	public ArrayList<String> lines = new ArrayList<>();
	public int currentLine;
	public Point location;
	public Boolean isActive;
	public Color color;
	public Boolean isSelected;
	public int width = 0;
	public int height = 0;
	
	public TextAnnotation(Color c) {
		this.isActive = false;
		this.currentLine = 0;
		this.lines.add(currentLine, "");
		this.color = c;
		this.isSelected = false;
		this.setBoundingBox();
	}

	private void setBoundingBox() {
		this.width = 0;
		this.height = 0;
	}

	public TextAnnotation(Point p, Color c) {
		this.location = p;
		this.isActive = true;
		this.color = c;
		this.lines.add(currentLine, "");
		this.isSelected = false;
	}
	
	public TextAnnotation() {
		this.isActive = false;
		this.currentLine = 0;
		this.lines.add(currentLine, "");
		this.isSelected = false;
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
	
	// implement delete key
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
	
	// set max x-value for bounding box for text annotation
	public void setWidth(Graphics2D g2d) {
		int maxLineLength = 0;
		
		for (String line : this.lines) {
			FontMetrics fm = g2d.getFontMetrics();
			int currentLength = fm.stringWidth(line);
			if (currentLength > maxLineLength) {
				maxLineLength = currentLength;
			}
		}
		
		this.width = this.location.x + maxLineLength;
	}
	
	// set max y-value for bounding box for text annotation
	public void setHeight(int lineHeight) {
		this.height = this.location.y + lineHeight * this.lines.size();
	}
	
	// check if point is inside text annotation, select annotation if it is
	public boolean mouseInside(Point p) {
		if (p.x <= this.width && p.x >= this.location.x && p.y <= this.height && p.y >= this.location.y - 16) {
			this.isSelected = true;
			return true;
		} else {
			this.isSelected = false;
			return false;
		}
	}
	
	// draw a text annotation
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.color);
		int lineHeight = g.getFontMetrics().getHeight();
		int y = this.location.y;
		this.setHeight(lineHeight);
		this.setWidth(g2);
				
		for (String line : this.lines) {
			if (line != null) {
				g2.drawString(line, this.location.x, y);
				y += lineHeight;
			}
		}
		
		if (isSelected) {
			g2.setColor(Color.BLUE);
			Stroke old = g2.getStroke();
			g2.setStroke(new BasicStroke(1));
			g2.drawRect(this.location.x, this.location.y-lineHeight, this.width-this.location.x, this.height-this.location.y);
			g2.setStroke(old);
		}
	}
	
	// simple wrap for text annotations
	public void wrapSimple(Graphics g, ImageIcon img) {
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
					this.newLine(remaining);
					break;
				}
			}
		}
	}

	// wrapping for text annotation with keyboard events
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