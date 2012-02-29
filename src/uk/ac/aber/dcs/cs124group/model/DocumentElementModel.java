package uk.ac.aber.dcs.cs124group.model;

import java.util.*;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

public abstract class DocumentElementModel extends Observable implements java.io.Serializable{
	
	//private Hashtable<String, Object> edits = new Hashtable<String, Object>();
	protected Stack<Edit> edits = new Stack<Edit>();
	private int index = 0;

	private static final long serialVersionUID = -8960995955260463413L;
	
	private boolean exists = true;
	
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	
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
	
	public DocumentElementView getView() {
		return null;
	}
	
	public void storeState(String key, Object value){
		edits.push(new Edit(key, value));
		index++;
	}
	
	public void undo() {
		Edit tmp = edits.get(index);
		this.restoreState(index);
		index--;
	}
	
	public abstract void restoreState(int i);
}
