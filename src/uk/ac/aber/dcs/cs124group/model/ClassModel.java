package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.awt.Point;
import java.awt.Dimension;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.*;

import uk.ac.aber.dcs.cs124group.undo.ExistenceEdit;
import uk.ac.aber.dcs.cs124group.undo.FlagEdit;
import uk.ac.aber.dcs.cs124group.undo.LocationEdit;
import uk.ac.aber.dcs.cs124group.undo.SizeEdit;
import uk.ac.aber.dcs.cs124group.view.ClassRectangle;
import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

public class ClassModel extends DocumentElementModel implements Moveable, Resizeable {
	
	private TextLabelModel nameLabel;
	private ArrayList<Relationship> relationships = new ArrayList<Relationship> ();
	
	private ArrayList<Attribute> dataFields = new ArrayList<Attribute>();
	private ArrayList<Attribute> methods = new ArrayList<Attribute>();
	
	private IVisibility visibility = IVisibility.PUBLIC;
	
	private boolean isAbstract = false;
	private boolean isFinal = false;
	private boolean isStatic = false;

	
	private Point location;
	private Dimension size = new Dimension(220,150);

	private CompoundEdit compoundEdit = new CompoundEdit();
	
	public ClassModel(Point p) {
		this.location = p;
	}
	
	@Override
	public ClassRectangle getView() {
		return new ClassRectangle(this, false);
	}
	
	public String getClassName() {
		return this.nameLabel.getText();
	}
	
	public void setNameLabel(TextLabelModel n) {
		this.nameLabel = n;
	}
	
	public TextLabelModel getNameLabel() {
		return nameLabel;
	}

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
	
	public ArrayList<Attribute> getDataFields() {
		return dataFields;
	}

	public ArrayList<Attribute> getMethods() {
		return methods;
	}

	public void addAttribute(Attribute a)  {
		a.addUndoableEditListener(this.getUndoableEditListener());
		
		ExistenceEdit edit = new ExistenceEdit(a, true);
		this.fireUndoableEvent(edit);
		
		if(a.getType() == AttributeType.METHOD)
			this.methods.add(a);
		else this.dataFields.add(a);
		
	}
	
	public void requestNewDataField() {
		this.setChanged();
		this.notifyObservers("addDataFieldRequested");
	}
	
	public void requestNewMethod() {
		this.setChanged();
		this.notifyObservers("addMethodRequested");
	}

	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}

	public void addRelationship(Relationship r) {
		relationships.add(r);
	}
	
	public void removeRelationship(Relationship r) {
		relationships.remove(r);
	}

	public boolean isAbstract() {
		return isAbstract;
	}

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

	public boolean isFinal() {
		return isFinal;
	}

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

	public boolean isStatic(){
		return isStatic;
	}

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

	public Dimension getSize() {
		return this.size;
	}
	
	public void setSize(Dimension s, boolean undoable) {
		if(undoable) {
			this.compoundEdit.addEdit(new SizeEdit(this, this.size, s));
		}
		
		this.size = s;
		this.setChanged();
		notifyObservers("sizeChanged");
	}

	public void setVisibility(IVisibility visibility, boolean undoable) {
		if(undoable) {
			FlagEdit edit = new FlagEdit(this, "visibility",this.visibility, visibility);
			this.fireUndoableEvent(edit);
		}
		this.visibility = visibility;
	}

	public IVisibility getVisibility() {
		return visibility;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public void setLocation(Point l, boolean undoable) {
		if(undoable) {
			compoundEdit.addEdit(new LocationEdit(this, this.location, l));
		}
		
		this.location = l;
		this.setChanged();
		notifyObservers("locationChanged");
	}
	
	public void stopMoving() {
		this.compoundEdit.end();
		this.fireUndoableEvent(this.compoundEdit);
		this.compoundEdit = new CompoundEdit();
	}
	
	
	public void cleanUp() {
		if(dataFields != null && methods != null) {
			for(int i = 0; i < dataFields.size(); i++) {
				if(!dataFields.get(i).exists()) {
					dataFields.remove(i);
					System.out.println("Removed");
				}
				
			}
			for(int i = 0; i < methods.size(); i++) {
				if(!methods.get(i).exists())
					methods.remove(i);
			}
		}
	}
	
	public void requestOutgoingRelationship() {
		this.setChanged();
		this.notifyObservers("relationshipRequested");
	}
	
	@Override
	public void addUndoableEditListener(UndoableEditListener l) {
		super.addUndoableEditListener(l);
		nameLabel.addUndoableEditListener(l);
	}


}