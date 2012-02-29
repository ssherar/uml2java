package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.Dimension;

public class SizeEdit extends AbstractUndoableEdit {
	
	private Object editedObject;
	private Dimension formerSize;
	private Dimension latterSize;
	
	public SizeEdit(Object o, Dimension f, Dimension l) {
		this.editedObject = o;
		this.formerSize = f;
		this.latterSize = l;
	}
	
	public void undo() {
		if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setSize(formerSize, false);
		}
	}
	
	public void redo() {
		if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setSize(latterSize, false);
		}
		
	}
}
