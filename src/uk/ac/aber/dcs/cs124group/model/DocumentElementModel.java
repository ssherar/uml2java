package uk.ac.aber.dcs.cs124group.model;

import java.util.Observable;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

public abstract class DocumentElementModel extends Observable implements java.io.Serializable, UndoableEdit{

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


	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getRedoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getUndoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isSignificant() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void redo() throws CannotRedoException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean replaceEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void undo() throws CannotUndoException {
		// TODO Auto-generated method stub
		
	}


}
