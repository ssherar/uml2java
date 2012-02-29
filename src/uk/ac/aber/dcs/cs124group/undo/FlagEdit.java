package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.Attribute;
import uk.ac.aber.dcs.cs124group.model.ClassModel;

public class FlagEdit extends AbstractUndoableEdit {
	
	private Object editedObject;
	private boolean formerFlag;
	private boolean latterFlag;
	private String whichFlag;
	
	public FlagEdit(Object o, String w, boolean f, boolean l) {
		this.editedObject = o;
		this.whichFlag = w;
		this.latterFlag = l;
		this.formerFlag = f;
	}
	
	public void undo() {
		this.setFlag(formerFlag);
	}
	
	public void redo() {
		this.setFlag(latterFlag);
	}
	
	private void setFlag(boolean flag) {
		if(editedObject instanceof ClassModel) {
			ClassModel c = (ClassModel) editedObject;
			
			if(whichFlag.equals("isStatic")) {
				c.setStatic(flag, false);
			}
			else if(whichFlag.equals("isAbstract")) {
				c.setAbstract(flag, false);
			}
			else if(whichFlag.equals("isFinal")) {
				c.setFinal(flag, false);
			}
		}
		else if(editedObject instanceof Attribute) {
			Attribute a = (Attribute) editedObject;
		}
	}
}
