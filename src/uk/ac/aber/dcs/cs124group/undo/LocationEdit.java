package uk.ac.aber.dcs.cs124group.undo;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.*;

public class LocationEdit extends AbstractUndoableEdit {
	
	private Object editedObject;
	private Point formerLocation;
	private Point latterLocation;
	
	public LocationEdit(Object o, Point f, Point l) {
		this.editedObject = o;
		this.formerLocation = f;
		this.latterLocation = l;
	}
	
	public void undo() {
		if(editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setLocation(formerLocation, false);
		}
	}
	
	public void redo() {
		if(editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setLocation(latterLocation, false);
		}
	}

}
