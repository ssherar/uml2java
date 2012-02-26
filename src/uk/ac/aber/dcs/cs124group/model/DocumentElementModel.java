package uk.ac.aber.dcs.cs124group.model;

import java.util.Observable;

public abstract class DocumentElementModel extends Observable implements java.io.Serializable {

	private static final long serialVersionUID = -8960995955260463413L;
	
	private boolean exists = true;
	
	protected DocumentElementModel() {
		
	}

	public boolean exists() {
		return exists;
	}
	
	public void remove() {
		exists = false;
	}


}
