package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.Relationship;

/**
 * An edit which manages the different states of a single
 * Relationship (past and present) adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class RelationshipStateEdit extends AbstractUndoableEdit {
	/**
	 * The previous state of the relationship
	 */
	private Relationship previousState;
	/**
	 * The current state of the relationship
	 */
	private Relationship currentState;
	
	/**
	 * Constructor: Assign the parameters to the global
	 * variables
	 * @param p
	 * @param c
	 */
	public RelationshipStateEdit(Relationship p, Relationship c) {
		this.previousState = p;
		this.currentState = c;
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Restores the current state with the previous state and swaps
	 * the global variables around.
	 * @see Relationship#restoreFromPrevious(previous)
	 */
	public void undo() {
		Relationship tmp = this.previousState;
		this.previousState = this.currentState.clone();
		this.currentState.restoreFromPrevious(tmp);
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's redo method.<p>
	 * Calls the {RelationshipStateEdit#undo()} method.
	 */
	public void redo() {
		this.undo();
	}
}
