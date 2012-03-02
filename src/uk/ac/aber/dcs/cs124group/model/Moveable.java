package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

public interface Moveable {
	
	public void setLocation(Point d, boolean undoable);
	public Point getLocation();
	

}
