package uk.ac.aber.dcs.cs124group.undo;

import javax.swing.undo.AbstractUndoableEdit;

import uk.ac.aber.dcs.cs124group.model.Attribute;
import uk.ac.aber.dcs.cs124group.model.ClassModel;
import uk.ac.aber.dcs.cs124group.model.IVisibility;


/**
 * An edit which manages a single instance of the attribute/class
 * flag and its current state adhering to the {@link AbsractUndoableEdit}
 * constraints
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */
public class FlagEdit extends AbstractUndoableEdit {
	
	/**
	 * The editedObject in question: Can only be {@link ClassModel} or {@link Attribute} types
	 */
	private Object editedObject;
	/**
	 * The flag before it changed
	 */
	private boolean formerFlag;
	/**
	 * What the flag is now
	 */
	private boolean latterFlag;
	/**
	 * What flag changed (e.g. flagStatic, flagAbstract)
	 */
	private String whatChanged;
	/**
	 * The visibility of the ClassModel before the edit
	 */
	private IVisibility formerVisibility;
	/**
	 * The visibility of the ClassModel after the edit
	 */
	private IVisibility latterVisibility;
	
	/**
	 * Constructor: This sets the object and the previous and current
	 * visibility. This is only called from the ClassModel
	 * 
	 * @param o			The Class in question
	 * @param s			What has been edited
	 * @param former	The previous state of the visibility
	 * @param latter	The current state of the visibility
	 */
	public FlagEdit(Object o, String s,IVisibility former, IVisibility latter) {
		this.editedObject = o;
		this.formerVisibility = former;
		this.latterVisibility = latter;
		this.whatChanged = s;
	}
	
	/**
	 * Constructor: This sets the object and global variables about what
	 * flags have been changed
	 * @param o			The Class in question
	 * @param w			What had been edited (e.g. flagStatic, flagAbstract etc)
	 * @param f			The previous state of the flag
	 * @param l			The current state of the flag
	 */
	public FlagEdit(Object o, String w, boolean f, boolean l) {
		this.editedObject = o;
		this.whatChanged = w;
		this.latterFlag = l;
		this.formerFlag = f;
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's undo method.<p>
	 * Checks what has changed: if it is visibility, change the visibility to the previous state. If an attribute/classmodel
	 * change the flag to the previous state
	 */
	public void undo() {
		if(this.whatChanged != "visibility") {
			this.setFlag(formerFlag);
		}
		else if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setVisibility(formerVisibility, false);
		}
	}
	
	/**
	 * Overrides the AbstractUndoableEdit's redo method.<p>
	 * Checks what has been changed: if it's visibility then change it back to the current state. If
	 * an Attribute/ClassModel, then change the flag to the current state
	 */
	public void redo() {
		if(this.whatChanged != "visibility") {
			this.setFlag(latterFlag);
		}
		else if(this.editedObject instanceof ClassModel) {
			((ClassModel)editedObject).setVisibility(latterVisibility, false);
		}
	}
	
	/**
	 * Sets the flag in the {@link Attribute} or the {@link ClassModel} classes
	 * according to the param and {@link whatChanged}
	 * @param flag			Boolean of the state of the flag.
	 */
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
