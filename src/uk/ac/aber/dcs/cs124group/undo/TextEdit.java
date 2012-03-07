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
@SuppressWarnings("serial")
public class TextEdit extends AbstractUndoableEdit {
	
	private TextLabelModel editedObject;
	private String formerText;
	private String latterText;
	
	public TextEdit(TextLabelModel o, String f, String l) {
		this.editedObject = o;
		this.formerText = f;
		this.latterText = l;
		
	}
	
	public void undo() {
		this.editedObject.setText(formerText, false);
	}
	
	public void redo() {
		this.editedObject.setText(latterText, false);
	}

}
