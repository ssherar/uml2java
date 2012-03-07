package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Dimension;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.undo.*;
import uk.ac.aber.dcs.cs124group.view.ClassRectangle;
/**
 * The backbone dataclass behind {@link ClassRectangle}. This handles
 * all data and graphical manipulation, while holding all {@link Relationship relationships},
 * {@link Attributes data fields} and {@link Attributes methods}.
 * <p>
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version v1.0.0
 *
 */
public class ClassModel extends DocumentElementModel implements Moveable, Resizeable {
	
	/**
	 * A unique SerialVersion identifier.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A label for the name of the class. The name acts just like an {@link Attribute}.
	 */
	private TextLabelModel nameLabel;
	/**
	 * A list of all the relationships associated to the class.
	 */
	private ArrayList<Relationship> relationships = new ArrayList<Relationship> ();
	/**
	 * A list of all the data fields.
	 */
	private ArrayList<Attribute> dataFields = new ArrayList<Attribute>();
	/**
	 * A list of all the methods.
	 */
	private ArrayList<Attribute> methods = new ArrayList<Attribute>();
	/**
	 * The visibility of the class. By default it is set to 
	 * {@link IVisibility#PUBLIC}.
	 */
	private IVisibility visibility = IVisibility.PUBLIC;
	/**
	 * A flag for the abstract modifier of the class. By default
	 * it is set to <code>false</code>.
	 */
	private boolean isAbstract = false;
	/**
	 * A flag for the final modifier of the class. By default
	 * it is set to <code>false</code>.
	 */
	private boolean isFinal = false;
	/**
	 * A flag for the static modifier of the class. By default
	 * it is set to <code>false</code>.
	 */
	private boolean isStatic = false;
	/**
	 * The location of the ClassRectangle when drawn
	 */
	private Point location;
	/**
	 * The size of the ClassRectangle. By default it is
	 * set to 220px by 150px
	 */
	private Dimension size = new Dimension(220,150);
	/**
	 * The ability to package certain edits into one big undo.
	 * This is used for both resizing and moving the ClassModel
	 * around the screen with no bugs
	 * @see LocationEdit
	 * @see SizeEdit
	 */
	private CompoundEdit compoundEdit = new CompoundEdit();
	
	/**
	 * Constructor: Set the location according to the parameters
	 * @param p		The top-left hand point of where
	 * 				we want to place a ClassRectangle
	 */
	public ClassModel(Point p) {
		this.location = p;
	}
	
	/**
	 * Creates a new version of the {@link ClassRectangle} according to
	 * the data in the model.
	 * @see DocumentElementModel#getView()
	 * @returns			A new ClassRectangle
	 */
	@Override
	public ClassRectangle getView() {
		return new ClassRectangle(this, false);
	}
	
	/**
	 * Returns the name of the Class
	 * @return		A text value of the name label.
	 */
	public String getClassName() {
		return this.nameLabel.getText();
	}
	
	/**
	 * Sets the new text value of the name label.
	 * @param n		A Text value for the nameLabel.
	 */
	public void setNameLabel(TextLabelModel n) {
		this.nameLabel = n;
	}
	
	/**
	 * Returns the label in it's native format
	 * @return		The nameLabel's model
	 */
	public TextLabelModel getNameLabel() {
		return nameLabel;
	}
	
	/**
	 * Returns an Arraylist of all the attributes. 
	 * @return
	 * 			If either {@link ClassModel#dataFields} or {@link ClassModel#model} are empty,
	 * 			it only returns the non-empty variable. If both have some values in, then
	 * 			merge and return. If both are empty then return null.
	 */
	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		if(dataFields == null && methods == null)
			return null;
		else if(dataFields == null)
			return methods;
		else if(methods == null)
			return dataFields;
		else {
			attributes.addAll(dataFields);
			attributes.addAll(methods);
			return attributes;
		}

	}
	
	/**
	 * Returns an ArrayList of the dataFields variable.
	 * @return		the {@link ClassModel#dataFields dataFields} ArrayList
	 */
	public ArrayList<Attribute> getDataFields() {
		return dataFields;
	}
	
	/**
	 * Returns an ArrayList of the methods variable
	 * @return		the {@link ClassModel#methods methods} ArrayList.
	 */
	public ArrayList<Attribute> getMethods() {
		return methods;
	}
	
	/**
	 * Adds an Undolistener to the Attribute and creates a new ExistanceEdit
	 * before it adds the attribute to the correct ArrayList.
	 * @param a		the new attribute to be added
	 */
	public void addAttribute(Attribute a)  {
		a.addUndoableEditListener(this.getUndoableEditListener());
		
		ExistenceEdit edit = new ExistenceEdit(a, true, "New attribute added to " + this.getClassName());
		this.fireUndoableEvent(edit);
		
		if(a.getType() == AttributeType.METHOD)
			this.methods.add(a);
		else this.dataFields.add(a);
		
	}
	
	/**
	 * Notifies the observer to add a new Data Field to the ClassModel.
	 * @see ClassRectangle#update(object, argument)
	 */
	public void requestNewDataField() {
		this.setChanged();
		this.notifyObservers("addDataFieldRequested");
	}
	
	/**
	 * Notifies the observer to add a new Method to the ClassModel.
	 * @see ClassRectangle#update(object, argument)
	 */
	public void requestNewMethod() {
		this.setChanged();
		this.notifyObservers("addMethodRequested");
	}
	
	/**
	 * Returns an ArrayList with all known relationships associated
	 * with this ClassModel.
	 * @see Relationship
	 * @return		The Array of Relationships
	 */
	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}
	
	/**
	 * Add a relationship to the ClassModel.
	 * @param r		The relationship to add
	 */
	public void addRelationship(Relationship r) {
		relationships.add(r);
	}
	
	/**
	 * Remove a relationship to the ClassModel.
	 * @param r		The relationship to remove
	 */
	public void removeRelationship(Relationship r) {
		relationships.remove(r);
	}
	
	/**
	 * Returns the class modifier flag "Abstract"
	 * @return		<code>True</code> if modifier is set, <code>false</code> otherwise.
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * Set the abstract flag in the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param isAbstract		To set the flag directly
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise
	 */
	public void setAbstract(boolean isAbstract, boolean undoable) {
		if(undoable) {
			FlagEdit edit = new FlagEdit(this, "isAbstract", this.isAbstract, isAbstract);
			this.fireUndoableEvent(edit);
		}
		
		this.isAbstract = isAbstract;
		if(isAbstract) {
			this.isStatic = false;
			this.isFinal = false;
		}
		this.setChanged();
		notifyObservers("flagChanged");
	}

	/**
	 * Returns the class modifier flag "Final"
	 * @return		<code>True</code> if modifier is set, <code>false</code> otherwise.
	 */
	public boolean isFinal() {
		return isFinal;
	}
	
	/**
	 * Set the final flag in the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param isFinal			To set the flag directly
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise
	 */
	public void setFinal(boolean isFinal, boolean undoable) {
		if(undoable) {
			FlagEdit edit = new FlagEdit(this, "isFinal", this.isFinal, isFinal);
			this.fireUndoableEvent(edit);
		}
		this.isFinal = isFinal;
		if(isFinal) {
			this.isStatic = false;
			this.isAbstract = false;
		}
		this.setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Returns the class modifier flag "Static"
	 * @return		<code>True</code> if modifier is set, <code>false</code> otherwise.
	 */
	public boolean isStatic(){
		return isStatic;
	}

	/**
	 * Set the Static flag in the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param isStatic			To set the flag directly
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise
	 */
	public void setStatic(boolean isStatic, boolean undoable){
		if(undoable) {
			FlagEdit edit = new FlagEdit(this, "isStatic", this.isStatic, isStatic);
			this.fireUndoableEvent(edit);
		}

		this.isStatic = isStatic;
		if(isStatic) {
			this.isFinal = false;
			this.isAbstract = false;
		}
		
		this.setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Remove all the modifying flags from the class
	 * @see #setAbstract(boolean, boolean)
	 * @see #setStatic(boolean, boolean)
	 * @see #setFinal(boolean, boolean)
	 */
	public void removeFlags() {
		if(isAbstract) {
			this.setAbstract(false, true);
		}
		if(isStatic) {
			this.setStatic(false, true);
		}
		if(isFinal) {
			this.setFinal(false, true);
		}
	}
	
	/**
	 * @return		The size of the ClassModel
	 */
	public Dimension getSize() {
		return this.size;
	}
	
	/**
	 * Set the size of the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param s					The new size of the ClassModel.
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise.
	 */
	public void setSize(Dimension s, boolean undoable) {
		if(undoable) {
			this.compoundEdit.addEdit(new SizeEdit(this, this.size, s));
		}
		
		this.size = s;
		this.setChanged();
		notifyObservers("sizeChanged");
	}
	
	/**
	 * Set the visibility of the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param visibility		The new visibility of the ClassModel
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise
	 */
	public void setVisibility(IVisibility visibility, boolean undoable) {
		if(undoable) {
			FlagEdit edit = new FlagEdit(this, "visibility",this.visibility, visibility);
			this.fireUndoableEvent(edit);
		}
		this.visibility = visibility;
	}
	
	/**
	 * @return		The visibility of the ClassModel.
	 */
	public IVisibility getVisibility() {
		return visibility;
	}
	
	/**
	 * @return		The location of the ClassModel.
	 */
	@Override
	public Point getLocation() {
		return location;
	}

	/**
	 * Set the location of the ClassModel, then tells the observer to act upon this new information.
	 * @see ClassRectangle#update(java.util.Observable, Object)
	 * 
	 * @param l					The new location of the ClassModel
	 * @param undoable			<code>true</code> if this is an user action which can be undone, <code>false</code> otherwise
	 */
	@Override
	public void setLocation(Point l, boolean undoable) {
		if(undoable) {
			compoundEdit.addEdit(new LocationEdit(this, this.location, l));
		}
		
		this.location = l;
		this.setChanged();
		notifyObservers("locationChanged");
	}
	
	/**
	 * Called from ClassController to notify the model that mouse dragging is over and that a 
	 * CompoundEdit should be sent to the UndoableEditListener
	 * 
	 * @see uk.ac.aber.dcs.cs124group.controller.ClassController#mouseReleased(MouseEvent) LabelController.mouseReleased()
	 * @see #compoundEdit
	 */
	public void stopMoving() {
		this.compoundEdit.end();
		this.fireUndoableEvent(this.compoundEdit);
		this.compoundEdit = new CompoundEdit();
	}
	
	/**
	 * Loops through the ArrayLists for relationships, datafields and
	 * methods to check if there they exist or not.
	 */
	public void cleanUp() {
		if(dataFields != null && methods != null) {
			for(int i = 0; i < dataFields.size(); i++) {
				if(!dataFields.get(i).exists()) {
					dataFields.remove(i);
					i--;
				}
				
			}
			for(int i = 0; i < methods.size(); i++) {
				if(!methods.get(i).exists()) {
					methods.remove(i);
					i--;
				}
			}
		}
		for(int i = 0; i < relationships.size(); i++) {
			if(!relationships.get(i).exists()) {
				relationships.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Sends a message to the observer for a new outgoing relationship
	 */
	public void requestOutgoingRelationship() {
		this.setChanged();
		this.notifyObservers("relationshipRequested");
	}
	
	/**
	 * Adds an undoableEditListener to the superclass as well as the
	 * name label
	 * 
	 * @param l		The undoableListener
	 */
	@Override
	public void addUndoableEditListener(UndoableEditListener l) {
		super.addUndoableEditListener(l);
		nameLabel.addUndoableEditListener(l);
	}
	
	/**
	 * Resurrect this and all the dead relationships when called. Used for undos and redos
	 * @see RelationshipStateEdit
	 */
	@Override
	public void resurrect() {
		super.resurrect();
		for(int i = 0; i < this.relationships.size(); i++) {
			this.relationships.get(i).resurrect();
		}
	}


}