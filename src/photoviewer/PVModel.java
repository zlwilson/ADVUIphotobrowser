package photoviewer;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PVModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	
	private boolean faceUp = true;

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public PVModel getModel() {
		return this;
	}
	
	public boolean isFaceUp() {
		return faceUp;
	}
	
	public void setFaceUp(){
		faceUp = !faceUp;
		fireChangeListener();
	}
	
	private void fireChangeListener() {
		for (ChangeListener l : changeListeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

}
