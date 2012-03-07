package uk.ac.aber.dcs.cs124group.undo;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.*;


/**
 * An edit which manages the locations (past and present) of a single
 * DocumentElement adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class LocationEdit extends AbstractUndoableEdit {
	
	private Moveable editedObject;
	private Point formerLocation;
	private Point latterLocation;
	
	public LocationEdit(Moveable o, Point f, Point l) {
		this.editedObject = o;
		this.formerLocation = f;
		this.latterLocation = l;
	}
	
	public void undo() {
		editedObject.setLocation(formerLocation, false);

	}
	
	public void redo() {
		editedObject.setLocation(latterLocation, false);
	}

}
