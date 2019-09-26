package photoviewer;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;

public class PVModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	private ArrayList<ActionListener> actionListeners = new ArrayList();
	
	private boolean faceUp = true;

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public void addActionListener(ActionListener listener) {
		this.actionListeners.add(listener);
	}

	public PVModel getModel() {
		return this;
	}
	
	public boolean isFaceUp() {
		return faceUp;
	}

}
