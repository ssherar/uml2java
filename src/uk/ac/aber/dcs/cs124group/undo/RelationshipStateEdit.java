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
	
	private Relationship previousState;
	private Relationship currentState;
	
	public RelationshipStateEdit(Relationship p, Relationship c) {
		this.previousState = p;
		this.currentState = c;
	}
	
	public void undo() {
		Relationship tmp = this.previousState;
		this.previousState = this.currentState.clone();
		this.currentState.restoreFromPrevious(tmp);
	}
	
	public void redo() {
		this.undo();
	}
}
