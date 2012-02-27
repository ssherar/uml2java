package uk.ac.aber.dcs.cs124group.model;

import java.util.Observable;

public abstract class DocumentElementModel extends Observable implements java.io.Serializable {

	private static final long serialVersionUID = -8960995955260463413L;
	
	private boolean exists = true;
	
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	
	protected DocumentElementModel() {
		
	}
	
	
	public void setPaintState(ElementPaintState paintState) {
		this.paintState = paintState;
		setChanged();
		notifyObservers("paintStateChanged");
	}
	
	public ElementPaintState getPaintState() {
		return paintState;
	}
	

	public boolean exists() {
		return exists;
	}
	
	public void remove() {
		exists = false;
		setChanged();
		notifyObservers("wasRemoved");
	}


}
