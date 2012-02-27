package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.awt.Point;
import java.awt.Dimension;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.view.ClassRectangle;
import uk.ac.aber.dcs.cs124group.view.DocumentElementView;

public class ClassModel extends DocumentElementModel {
	
	private TextLabelModel nameLabel;
	private ClassModel superClass;
	private ArrayList<Relationship> relationships = new ArrayList<Relationship> ();
	
	private ArrayList<Attribute> dataFields = new ArrayList<Attribute>();
	private ArrayList<Attribute> methods = new ArrayList<Attribute>();
	
	private IVisibility visibility = IVisibility.PUBLIC;
	
	private boolean isAbstract = false;
	private boolean isFinal = false;
	private boolean isStatic = false;
	
	private Point location;
	private Dimension size = new Dimension(300,225);

	
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

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
		this.setChanged();
		notifyObservers("flagChanged");
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isStatic(){
		return isStatic;
	}

	public void setStatic(boolean isStatic){
		this.isStatic = isStatic;
		this.setChanged();
		notifyObservers("flagChanged");
	}

	public Dimension getSize() {
		return this.size;
	}
	
	public void setSize(Dimension size) {
		this.size = size;
		this.setChanged();
		notifyObservers("sizeChanged");
	}

	public void setSuperClass(ClassModel c) {
		this.superClass = c;
	}

	public ClassModel getSuperClass() {
		return this.superClass;
	}

	public void setVisibility(IVisibility visibility) {
		this.visibility = visibility;
	}

	public IVisibility getVisibility() {
		return visibility;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
		this.setChanged();
		notifyObservers("locationChanged");
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


}