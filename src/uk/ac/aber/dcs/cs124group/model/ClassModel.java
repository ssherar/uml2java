package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;

public class ClassModel extends DocumentElementModel {
	
	private String name;
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
	private int nameFieldHeight;
	private int separatorCoordinate;
	
	public ClassModel(Point p) {
		this.location = p;
		
	}
	
	public String getClassName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		notifyObservers("nameChanged");
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

	public void addNewAttribute(AttributeType type) {
		Attribute newAttribute = new Attribute(
				this.getNextDataFieldPoint(-1), 
				"- dataField : Type",
				type);
		if(type == AttributeType.DATA_FIELD)
			dataFields.add(newAttribute);
		else methods.add(newAttribute);
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
		notifyObservers("flagChanged");
	}

	public Dimension getSize() {
		return this.size;
	}
	
	public void setSize(Dimension size) {
		this.size = size;
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
		notifyObservers("locationChanged");
	}

	
	/** Calculates the location of the next data field label from the top of the rectangle down to the attribute specified by the argument.
	 * 
	 * @param afterDataFieldNumber	The index at which a new data label is to be inserted minus one. 
	 * @return 						The Point at which it is safe to insert a new data field below the one specified by the argument.
	 */
	public Point getNextDataFieldPoint(int afterDataFieldNumber) {
		int y = nameFieldHeight + 5;

		for(int i = 0; i < afterDataFieldNumber && i >= 0 ;i++) {
			int height = dataFields.get(i).getPreferredSize().height;
			y += (int) (height * 1.25);	
		}
		return new Point(4,y);
	}

	/** Calculates the location of the next method label from the top of the rectangle down to the method specified by the argument.
	 * 
	 * @param afterMethodNumber		The index at which a new method label is to be inserted minus one. 
	 * @return 						The Point at which it is safe to insert a new method label below the one specified by the argument.
	 */
	public Point getNextMethodPoint(int afterMethodNumber) {
		int y = separatorCoordinate + 5;

		for(int i = 0; i < afterMethodNumber && i >= 0 ;i++) {
			int height = methods.get(i).getPreferredSize().height;
			y += (int) (height * 1.25);	
		}
		return new Point(4,y);
	}

	public int getNameFieldHeight() {
		return nameFieldHeight;
	}

	public void setNameFieldHeight(int nameFieldHeight) {
		this.nameFieldHeight = nameFieldHeight;
	}

	public void setSeparatorCoordinate(int separatorCoordinate) {
		this.separatorCoordinate = separatorCoordinate;
	}


}