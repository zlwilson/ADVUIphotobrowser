package photoviewer;

import java.awt.Component;

public class PicViewer extends Component {
	private PVModel model;
	private PVView view;
	
	public PicViewer(String label){
		setModel(model);
		setView(view);
	}

	private void setView(PVView v) {
		this.view = v;
	}

	private void setModel(PVModel m) {
		this.model = m;
		model.addChangeListener(event -> repaint());
	}
}
