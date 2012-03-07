package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.Dimension;

/**
 * An edit which manages the size of a single
 * DocumentElement adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
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
