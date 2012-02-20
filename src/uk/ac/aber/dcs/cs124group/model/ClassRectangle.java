package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.awt.Dimension;
import java.util.ArrayList;

public class ClassRectangle extends DocumentElement {

	private static final long serialVersionUID = 249568855144662119L;
	
	private String name;
	private Dimension size;
	private ClassRectangle superClass = null;
	private IVisibility visibility = IVisibility.PUBLIC;
	
	private ArrayList<Relationship> relationships;
	
	private boolean isAbstract = false;
	private boolean isFinal = false;
	
	public ClassRectangle(Point p) {
		position = p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(ArrayList<Relationship> relationships) {
		this.relationships = relationships;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	@Override
	public void move(Point newPos) {
		position = newPos;
		//TODO act upon this new information accordingly...
	}
	
	public void resize(Dimension newSize) {
		this.size = newSize;
		//TODO act upon this new information accordingly...
	}
	
	public Dimension getSize() {
		return this.size;
	}
	
	public void setSuperClass(ClassRectangle c) {
		this.superClass = c;
	}
	
	public ClassRectangle getSuperClass() {
		return this.superClass;
	}
	
	public void setVisibility(IVisibility visibility) {
		this.visibility = visibility;
	}
	
	public IVisibility getVisibility() {
		return visibility;
	}
	

}
