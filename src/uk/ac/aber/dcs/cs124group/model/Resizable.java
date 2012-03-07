package uk.ac.aber.dcs.cs124group.model;

import java.awt.Dimension;

/**
 * An interface implemented by DocumentElementModels that the user can resize.
 * 
 * @see uk.ac.aber.dcs.cs124group.undo.LocationEdit SizeEdit
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public interface Resizable {

	public void setSize(Dimension d, boolean undoable);
	public Dimension getSize();
}
