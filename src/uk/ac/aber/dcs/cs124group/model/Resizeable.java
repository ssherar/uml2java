package uk.ac.aber.dcs.cs124group.model;

import java.awt.Dimension;

public interface Resizeable {

	public void setSize(Dimension d, boolean undoable);
	public Dimension getSize();
}
