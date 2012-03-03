package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import javax.swing.JTextField;
import javax.swing.undo.CompoundEdit;

import uk.ac.aber.dcs.cs124group.undo.LocationEdit;
import uk.ac.aber.dcs.cs124group.undo.TextEdit;
import uk.ac.aber.dcs.cs124group.view.LabelView;



public class TextLabelModel extends DocumentElementModel implements Moveable {
	private String text = "New Label";
	private Point location;
	private Dimension size;
	private boolean editing = false;
	private int alignmentInParent = JTextField.LEFT;
	private boolean isClassName = false;
	
	private CompoundEdit compoundEdit = new CompoundEdit();
	
	public TextLabelModel(Point p) {
		this.location = p;
	}
	
	
	public TextLabelModel(Point p, String text) {
		this.location = p;
		this.text = text;
	}
	
	public TextLabelModel(Point p, String text, boolean isClassName) {
		this.location = p;
		this.text = text;
		this.isClassName = isClassName;
	}
	
	
	@Override
	public LabelView getView() {
		return new LabelView(this);
	}

	public String getText() {
		return text;
	}

	public void setText(String text, boolean undoable) {
		if(undoable && !this.text.equals(text)) {
			TextEdit edit = new TextEdit(this, this.text, text);
			this.fireUndoableEvent(edit);
		}
		
		
		this.text = text;
		this.setChanged();
		this.notifyObservers("textChanged");
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location, boolean undoable) {
		if(undoable) {
			compoundEdit.addEdit(new LocationEdit(this, this.location, location));
		}
		
		this.location = location;
		this.setChanged();
		this.notifyObservers("locationChanged");
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
		this.setChanged();
		this.notifyObservers("sizeChanged");
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
		this.setChanged();
		this.notifyObservers("editingChanged");
	}

	public void setAlignmentInParent(int alignmentInParent) {
		this.alignmentInParent = alignmentInParent;
	}
	
	public int getAlignmentInParent() {
		return this.alignmentInParent;
	}
	
	public boolean isClassName() {
		return isClassName;
	}
	
	public void stopMoving() {
		this.compoundEdit.end();
		this.fireUndoableEvent(this.compoundEdit);
		this.compoundEdit = new CompoundEdit();
	}
	
	
	
}
