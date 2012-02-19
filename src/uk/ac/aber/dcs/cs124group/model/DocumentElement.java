package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

public abstract class DocumentElement implements java.io.Serializable {

	
	private static final long serialVersionUID = -253995425441515922L;
	private Point position;
	
	protected DocumentElement() {
		
	}
	
	public abstract void move(Point newPos);
	

}
