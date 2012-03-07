package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import javax.swing.JTextField;
import javax.swing.undo.CompoundEdit;

import uk.ac.aber.dcs.cs124group.undo.LocationEdit;
import uk.ac.aber.dcs.cs124group.undo.TextEdit;
import uk.ac.aber.dcs.cs124group.view.LabelView;


/**
 * A container model for any text displayed on the class diagram. 
 * This includes free text labels on the canvas, relationship labels,
 * cardinalities, attributes and class names.
 * 
 * @see Attribute
 * @see Cardinality
 * @see RelationshipLabel
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class TextLabelModel extends DocumentElementModel implements Moveable {
	
	/** The text contained in this label. */
	private String text = "New Label";
	
	/** The location of this label on the diagram. */
	private Point location;
	
	/** The size of the text container. */
	private Dimension size;
	
	/** The editing state of this text label. 
	 *  @see LabelView#enableEdit()
	 *  @see LabelView#exitEdit()
	 */
	private boolean editing = false;
	
	/** 
	 * The alignment of this text label in the parent component. 
	 * JTextField.LEFT denotes a user-specified position.
	 */
	private int alignmentInParent = JTextField.LEFT;
	
	/**
	 * True if this text label is a class name label, false otherwise.
	 */
	private boolean isClassName = false;
	
	/**
	 * The ability to package certain edits into one big undo.
	 * This is used moving the label around the canvas with no bugs.
	 * 
	 * @see LocationEdit
	 */
	private CompoundEdit compoundEdit = new CompoundEdit();
	
	/**
	 * Constructs a new TextLabelModel at the specified point.
	 * 
	 * @param p The location of the new TextLabelModel.
	 */
	public TextLabelModel(Point p) {
		this.location = p;
	}
	
	/**
	 * Constructs a new TextLabelModel at the specified point
	 * with the specified text.
	 * 
	 * @param p The location of the new TextLabelModel.
	 * @param text The text of the new TextLabelModel.
	 */
	public TextLabelModel(Point p, String text) {
		this.location = p;
		this.text = text;
	}
	
	/**
	 * Constructs a new TextLabelModel with the specified attributes
	 * 
	 * @param p The location of the new TextLabelModel.
	 * @param text The text of the new TextLabelModel.
	 * @param isClassName True if this label is a class name label, false otherwise.
	 */
	public TextLabelModel(Point p, String text, boolean isClassName) {
		this.location = p;
		this.text = text;
		this.isClassName = isClassName;
	}
	
	
	@Override
	/* See superclass */
	public LabelView getView() {
		return new LabelView(this);
	}

	/**
	 * @return The String contained within this label.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text within this label. If the second argument is true,
	 * creates a new UndoableEdit for this user action.
	 * 
	 * @param text The new String to be set into this label.
	 * @param undoable True if this is a user action that can be undone.
	 * 
	 * @see TextEdit
	 */
	public void setText(String text, boolean undoable) {
		if(undoable && !this.text.equals(text)) {
			TextEdit edit = new TextEdit(this, this.text, text);
			this.fireUndoableEvent(edit);
		}
		
		
		this.text = text;
		this.setChanged();
		this.notifyObservers("textChanged");
	}

	/**
	 * @see Moveable#getLocation()
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @see Moveable#setLocation(Point, boolean)
	 */
	public void setLocation(Point location, boolean undoable) {
		if(undoable) {
			compoundEdit.addEdit(new LocationEdit(this, this.location, location));
		}
		
		this.location = location;
		this.setChanged();
		this.notifyObservers("locationChanged");
	}

	/**
	 * @return The current size of this label.
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * Sets a new size on this label.
	 * NOTE: TextLabelModel does not implement Resizeable, therefore
	 * this can never be a user action and as such cannot be undone.
	 * 
	 * @param size The size to be set
	 */
	public void setSize(Dimension size) {
		this.size = size;
		this.setChanged();
		this.notifyObservers("sizeChanged");
	}

	/**
	 * @return True if this label is currently being edited, false otherwise.
	 */
	public boolean isEditing() {
		return editing;
	}

	/**
	 * Notifies the view associated to this label that editing is to be 
	 * enabled or disabled on it.
	 * 
	 * @param editing True if editing is to be enabled, false if it is to be disabled.
	 * @see LabelView#enableEdit()
	 * @see LabelView#exitEdit()
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
		this.setChanged();
		this.notifyObservers("editingChanged");
	}

	/**
	 * Sets the alignment in the parent of this text label.
	 * 
	 * @param alignmentInParent The new alignment.
	 * 
	 * @see #alignmentInParent
	 */
	public void setAlignmentInParent(int alignmentInParent) {
		this.alignmentInParent = alignmentInParent;
	}
	
	/**
	 * @return The alignment in the parent of this text label.
	 * 
	 * @see #alignmentInParent
	 */
	public int getAlignmentInParent() {
		return this.alignmentInParent;
	}
	 /**
	  * @return True if this label is a class name label, false otherwise.
	  */
	public boolean isClassName() {
		return isClassName;
	}
	
	/**
	 * Called from LabelController to notify the model that mouse dragging is over and that a 
	 * CompoundEdit should be sent to the UndoableEditListener
	 * 
	 * @see uk.ac.aber.dcs.cs124group.controller.LabelController#mouseReleased(MouseEvent) LabelController.mouseReleased()
	 * @see #compoundEdit
	 */
	public void stopMoving() {
		this.compoundEdit.end();
		this.fireUndoableEvent(this.compoundEdit);
		this.compoundEdit = new CompoundEdit();
	}
	
	@Override
	/** No sub-elements to clean up here. */
	public void cleanUp() {}
	
	
	
}
