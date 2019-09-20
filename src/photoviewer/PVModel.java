package photoviewer;

import java.util.ArrayList;

import javax.swing.event.ChangeListener;

public class PVModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}

}
