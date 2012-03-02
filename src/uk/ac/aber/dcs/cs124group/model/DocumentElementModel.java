package uk.ac.aber.dcs.cs124group.model;

import java.util.*;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

public abstract class DocumentElementModel extends Observable implements java.io.Serializable{
	

	private static final long serialVersionUID = -8960995955260463413L;
	
	private boolean exists = true;
	
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	private transient UndoableEditListener undoManager;
	
	protected DocumentElementModel() {
		
	}
	
	
	public void setPaintState(ElementPaintState paintState) {
		this.paintState = paintState;
		setChanged();
		notifyObservers("paintStateChanged");
	}
	
	public ElementPaintState getPaintState() {
		return paintState;
	}
	

	public boolean exists() {
		return exists;
	}
	
	public void remove() {
		exists = false;
		setChanged();
		notifyObservers("wasRemoved");
	}
	
	public void resurrect() {
		exists = true;
		setChanged();
		notifyObservers("wasResurrected");
	}
		
	public void addUndoableEditListener(UndoableEditListener l) {
		this.undoManager = l;
	}
	
	public UndoableEditListener getUndoableEditListener() {
		return this.undoManager;
	}
	
	protected void fireUndoableEvent(UndoableEdit e) {
		undoManager.undoableEditHappened(new UndoableEditEvent(this, e));
	}
	
	public abstract DocumentElementView getView();
}
