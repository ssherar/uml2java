package uk.ac.aber.dcs.cs124group.model;

import java.util.*;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.undo.ExistenceEdit;
import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

/**
 * An abstract superclass of all the document element model classes. 
 * Implements methods relating to the overall state of the element in the whole diagram,
 * like existence and paint state.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public abstract class DocumentElementModel extends Observable implements java.io.Serializable {
	

	/** Version ID for file output */
	private static final long serialVersionUID = -8960995955260463413L;
	
	/** 
	 * Cogito ergo sum. Indicates whether this element is alive, e.g. either that it has been
	 * created by the user and not removed or that it has been resurrected by an undo. 
	 * Existence is a pre-requisite for having a view displayed on the canvas and therefore
	 * also necessary for controllers to receive events and update the element model.
	 * For more information about element existence, please refer to the design documentation.
	 * 
	 * @see remove()
	 * @see resurrect()
	 * @see uk.ac.aber.dcs.cs124group.undo.ExistenceEdit ExistenceEdit
	 * @see DocumentModel.cleanUp()
	 */
	private boolean exists = true;
	
	/**
	 * The paint state is a directive to paintComponent() methods that wish to differentiate between
	 * an element that is moused over, selected, being moved and so on. It is also a useful tool to detect mouse actions
	 * by objects that aren't directly listening to mouse events.
	 * For more information about the element paint state, please refer to the design documentation.
	 */
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	
	/** 
	 * The UndoableEditListener to which UndoableEdits are being sent. We do not want to save the whole
	 *  manager when saving a document, therefore this attribute is transient.
	 */
	private transient UndoableEditListener undoManager;
		
	
	/**
	 * Sets the element paint state and notifies any observers of the change.
	 * @param paintState
	 * 		The new paintState of this DocumentElementModel.
	 */
	public void setPaintState(ElementPaintState paintState) {
		this.paintState = paintState;
		setChanged();
		notifyObservers("paintStateChanged");
	}
	
	/**
	 * @return The current paint state.
	 */
	public ElementPaintState getPaintState() {
		return paintState;
	}
	

	/**
	 * @return Whether or not this element exists.
	 * @see exists
	 */
	public boolean exists() {
		return exists;
	}
	
	/**
	 * Called whenever the user requests the element to be removed from the document. 
	 * Sets exists to false and sends an ExistenceEdit to the undoManager so that 
	 * this action may be undone or redone by the user at any point.
	 * 
	 * @see exists
	 * @see uk.ac.aber.dcs.cs124group.undo.ExistenceEdit ExistenceEdit
	 */
	public void userRemove() {
		ExistenceEdit edit = new ExistenceEdit(this, false, "Element removed");
		this.fireUndoableEvent(edit);
		
		this.remove();
	}
	
	/**
	 * Sets exists to false but does not create an ExistenceEdit.
	 * Called when undoing the remove is either impossible or already taken care of,
	 * such as when cleaning up the document or when undoing an ExistenceEdit. 
	 * 
	 * @see exists
	 * @see DocumentModel.cleanUp()
	 */
	public void remove() {
		exists = false;
		setChanged();
		notifyObservers("wasRemoved");
	}
	
	/**
	 * Brings this element back into existence and notifies observers.
	 * 
	 * @see exists
	 * @see uk.ac.aber.dcs.cs124group.undo.ExistenceEdit ExistenceEdit
	 */
	public void resurrect() {
		exists = true;
		setChanged();
		notifyObservers("wasResurrected");
	}
		
	/**
	 * Assigns an UndoableEditListener to this element.
	 * @param l
	 * 		The UndoableEditListener to be assigned.
	 */
	public void addUndoableEditListener(UndoableEditListener l) {
		this.undoManager = l;
	}
	
	/**
	 * @return The UndoableEditListener assigned to this element.
	 */
	public UndoableEditListener getUndoableEditListener() {
		return this.undoManager;
	}
	
	/**
	 * Sends an UndoableEdit to the assigned UndoableEditListener.
	 * 
	 * @param e
	 * 		The UndoableEdit to be sent to the UndoableEditListener
	 * 
	 * @see undoManager
	 */
	protected void fireUndoableEvent(UndoableEdit e) {
		undoManager.undoableEditHappened(new UndoableEditEvent(this, e));
	}
	
	/**
	 * Clears this element of any non-existent sub-elements (such as deleted Attributes in classes)
	 * 
	 * @see exists
	 * @see DocumentModel#cleanUp()
	 */
	public abstract void cleanUp();
	
	/**
	 * Constructs a brand new DocumentElementView for this element. 
	 * Does NOT return the view that this element may or may not be assigned to,
	 * merely constructs a new one.
	 * 
	 * @return
	 * 		A new DocumentElementView for this element model.
	 */
	public abstract DocumentElementView getView();
}
