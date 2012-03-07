package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.Dimension;

/**
 * An edit which manages the size of a single
 * DocumentElement adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class SizeEdit extends AbstractUndoableEdit {
	/**
	 * A resizable DocumentElementModel
	 */
	private Resizeable editedObject;
	/**
	 * The previous size of the Element
	 */
	private Dimension formerSize;
	/**
	 * The current size of the Element
	 */
	private Dimension latterSize;
	
	/**
	 * Constructor:	Assigns the parameters to global
	 * variables	
	 * @param o		The resizeable object
	 * @param f		The private size
	 * @param l		The current size
	 */
	public SizeEdit(Resizeable o, Dimension f, Dimension l) {
		this.editedObject = o;
		this.formerSize = f;
		this.latterSize = l;
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Changes the size to the previous size of the Element
	 */
	public void undo() {
		editedObject.setSize(formerSize, false);
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Changes the size to the current size of the Element
	 */
	public void redo() {
		editedObject.setSize(latterSize, false);
		
	}
}
