package uk.ac.aber.dcs.cs124group.view;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.Relationship;

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
