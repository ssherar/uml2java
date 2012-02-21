package uk.ac.aber.dcs.cs124group.prototype;

import java.awt.Point;
import java.awt.Dimension;
import java.util.ArrayList;
import uk.ac.aber.dcs.cs124group.model.*;

public class ClassRectangle extends DocumentElement {

	private static final long serialVersionUID = 249568855144662119L;
	
	private String name;
	private ClassRectangle superClass = null;
	private IVisibility visibility = IVisibility.PUBLIC;
	
	private ArrayList<Relationship> relationships;
	
	private boolean isAbstract = false;
	private boolean isFinal = false;
	
	public ClassRectangle() {
		
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

	public void addRelationship(Relationship r) {
		relationships.add(r);
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
