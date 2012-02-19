package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

public class ClassRectangle extends DocumentElement {

	private static final long serialVersionUID = 249568855144662119L;
	
	private String name;
	private Dimension size;
	private ClassRectangle superClass = null;
	private boolean isAbstract = false;
	private boolean isFinal = false;

	public ClassRectangle(Point p) {
		position = p;
	}

	@Override
	public void move(Point newPos) {
		position = newPos;
		//act upon this new information accordingly...
	}
	
	public void resize(Dimension newSize) {
		this.size = newSize;
		//act upon this new information accordingly...
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

}
