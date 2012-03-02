package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.Dimension;

public class SizeEdit extends AbstractUndoableEdit {
	
	private Resizeable editedObject;
	private Dimension formerSize;
	private Dimension latterSize;
	
	public SizeEdit(Resizeable o, Dimension f, Dimension l) {
		this.editedObject = o;
		this.formerSize = f;
		this.latterSize = l;
	}
	
	public void undo() {
		editedObject.setSize(formerSize, false);
	}
	
	public void redo() {
		editedObject.setSize(latterSize, false);
		
	}
}
