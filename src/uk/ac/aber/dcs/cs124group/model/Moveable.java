package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

/**
 * An interface implemented by DocumentElementModels that the user can move around.
 * 
 * @see uk.ac.aber.dcs.cs124group.undo.LocationEdit LocationEdit
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public interface Moveable {
	
	public void setLocation(Point d, boolean undoable);
	public Point getLocation();
	

}
