package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

public abstract class DocumentElement implements java.io.Serializable {

	
	private static final long serialVersionUID = -253995425441515922L;
	protected Point position;
	private boolean exists = true;
	
	protected DocumentElement() {
		
	}
	
	public Point getPosition() {
		return position;
	}
	
	public boolean exists() {
		return exists;
	}
	
	public void markForRemoval() {
		exists = false;
	}
	
	public abstract void move(Point newPos);
	
	

}
