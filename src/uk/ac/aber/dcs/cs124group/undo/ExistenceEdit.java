package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.DocumentElementModel;

/**
 * An edit which manages the existence of a DocumentElement
 * adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class ExistenceEdit extends AbstractUndoableEdit {
	
	/**
	 * The Element which we are questioning it's existence
	 */
	private DocumentElementModel toBeOrNotToBe;
	
	/**
	 * <code> True </code> if it's a new instance of an element
	 */
	private boolean isAnAdd;
	
	/**
	 * A human-friendly name for the edit for the statusbar
	 */
	private String presentationName;
	
	/**
	 * Constructor: Overloads {@link #ExistenceEdit(DocumentElementModel, boolean, String)} with
	 * an empty string for the presentationName
	 * @param m			The element which we want to question it's existence
	 * @param isAnAdd	<code> True </code> if it's a new instance of an element
	 */
	public ExistenceEdit(DocumentElementModel m, boolean isAnAdd) {
		this(m, isAnAdd, "");
	}
	
	/**
	 * Constructor: Adds the parameters to the global variables
	 * @param m					The element which we want to question it's existence
	 * @param isAnAdd			<code> True </code> if it's a new instance of an element
	 * @param presentationName	The human friendly name for the edit
	 */
	public ExistenceEdit(DocumentElementModel m, boolean isAnAdd, String presentationName) {
		this.toBeOrNotToBe = m;
		this.isAnAdd = isAnAdd;
		this.presentationName = presentationName;
	}
	
	/**
	 * If it's a new instance, it will remove it, if not
	 * Resurrect the old element
	 */
	@Override
	public void undo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.remove();
		}
		else toBeOrNotToBe.resurrect();
	}
	
	/**
	 * If it's a new instance, it will Resurrect the deleted element,
	 * if not, it will remove it.
	 */
	@Override
	public void redo() {
		if(this.isAnAdd) {
			toBeOrNotToBe.resurrect();
		}
		else toBeOrNotToBe.remove();
		
	}
	
	/**
	 * Returns the presentation name of the edit
	 */
	@Override
	public String getPresentationName() {
		return this.presentationName;
	}

}
