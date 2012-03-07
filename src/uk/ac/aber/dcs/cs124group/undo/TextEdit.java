package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.TextLabelModel;

/**
 * An edit which manages the different text entered into
 * a edited TextLabel adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class TextEdit extends AbstractUndoableEdit {
	/**
	 * An editable TextLabelElement
	 */
	private TextLabelModel editedObject;
	/**
	 * The previous text
	 */
	private String formerText;
	/**
	 * The current text
	 */
	private String latterText;
	
	/**
	 * Constructor: Assigning parameters to global variables
	 * @param o
	 * @param f
	 * @param l
	 */
	public TextEdit(TextLabelModel o, String f, String l) {
		this.editedObject = o;
		this.formerText = f;
		this.latterText = l;
		
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Sets the editedObjects text back to the previous value.
	 */
	public void undo() {
		this.editedObject.setText(formerText, false);
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's redo method.<p>
	 * Sets the editedObjects text back to the current value.
	 */
	public void redo() {
		this.editedObject.setText(latterText, false);
	}

}
