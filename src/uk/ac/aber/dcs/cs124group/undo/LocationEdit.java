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
	
	/**
	 * A movable DocumentElementModel
	 */
	private Moveable editedObject;
	/**
	 * The previous location of the Element
	 */
	private Point formerLocation;
	/**
	 * The current location of the Element
	 */
	private Point latterLocation;
	
	/**
	 * Constructor: Assigning the parameters to the 
	 * global variables
	 * @param o		The Movable Object in question
	 * @param f		The previous location
	 * @param l		The current location
	 */
	public LocationEdit(Moveable o, Point f, Point l) {
		this.editedObject = o;
		this.formerLocation = f;
		this.latterLocation = l;
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Undo the object to the previous location
	 */
	public void undo() {
		editedObject.setLocation(formerLocation, false);

	}
	
	/**
	 * Overrides the AbstractUndoableEdit's redo method.<p>
	 * Redos the object to the previous location
	 */
	public void redo() {
		editedObject.setLocation(latterLocation, false);
	}

}
