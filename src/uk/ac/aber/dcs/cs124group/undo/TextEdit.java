package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.TextLabelModel;

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
