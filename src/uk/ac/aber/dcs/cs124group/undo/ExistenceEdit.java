package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.DocumentElementModel;

public class ExistenceEdit extends AbstractUndoableEdit {
	
	private DocumentElementModel toBeOrNotToBe;
	private boolean isAnAdd;
	
	public ExistenceEdit(DocumentElementModel m, boolean isAnAdd) {
		this.toBeOrNotToBe = m;
		this.isAnAdd = isAnAdd;
	}
	
	public void undo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.remove();
		}
		else toBeOrNotToBe.resurrect();
	}
	
	public void redo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.resurrect();
		}
		else toBeOrNotToBe.remove();
		
	}

}
