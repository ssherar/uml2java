package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.Attribute;
import uk.ac.aber.dcs.cs124group.model.ClassModel;
import uk.ac.aber.dcs.cs124group.model.IVisibility;

public class FlagEdit extends AbstractUndoableEdit {
	
	private Object editedObject;
	private boolean formerFlag;
	private boolean latterFlag;
	
	private String whatChanged;
	
	private IVisibility formerVisibility;
	private IVisibility latterVisibility;
	
	public FlagEdit(Object o, String w, boolean f, boolean l) {
		this.editedObject = o;
		this.whatChanged = w;
		this.latterFlag = l;
		this.formerFlag = f;
	}
	
	public FlagEdit(Object o, String s,IVisibility former, IVisibility latter) {
		this.editedObject = o;
		this.formerVisibility = former;
		this.latterVisibility = latter;
	}
	
	public void undo() {
		if(this.whatChanged != "visibility") {
			this.setFlag(formerFlag);
		}
		else if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setVisibility(formerVisibility, false);
		}
	}
	
	public void redo() {
		if(this.whatChanged != "visibility") {
			this.setFlag(latterFlag);
		}
		else if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setVisibility(latterVisibility, false);
		}
	}
	
	private void setFlag(boolean flag) {
		if(editedObject instanceof ClassModel) {
			ClassModel c = (ClassModel) editedObject;
			
			if(whatChanged.equals("isStatic")) {
				c.setStatic(flag, false);
			}
			else if(whatChanged.equals("isAbstract")) {
				c.setAbstract(flag, false);
			}
			else if(whatChanged.equals("isFinal")) {
				c.setFinal(flag, false);
			}
		}
		else if(editedObject instanceof Attribute) {
			Attribute a = (Attribute) editedObject;
			
			if(whatChanged.equals("flagStatic")) {
				a.setStatic(flag, false);
			} else if(whatChanged.equals("flagAbstract")) {
				a.setAbstract(flag, false);
			} else if(whatChanged.equals("flagFinal")) {
				a.setFinal(flag, false);
			} else if(whatChanged.equals("flagTransient")) {
				a.setTransient(flag, false);
			}
		}
	}
}
