package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.DocumentElementModel;

/**
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class ExistenceEdit extends AbstractUndoableEdit {
	
	private DocumentElementModel toBeOrNotToBe;
	private boolean isAnAdd;
	private String presentationName;
	
	public ExistenceEdit(DocumentElementModel m, boolean isAnAdd) {
		this(m, isAnAdd, "");
	}
	
	public ExistenceEdit(DocumentElementModel m, boolean isAnAdd, String presentationName) {
		this.toBeOrNotToBe = m;
		this.isAnAdd = isAnAdd;
		this.presentationName = presentationName;
	}
	
	@Override
	public void undo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.remove();
		}
		else toBeOrNotToBe.resurrect();
	}
	
	@Override
	public void redo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.resurrect();
		}
		else toBeOrNotToBe.remove();
		
	}
	
	@Override
	public String getPresentationName() {
		return this.presentationName;
	}

}
