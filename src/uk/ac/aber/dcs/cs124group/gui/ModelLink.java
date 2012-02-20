package uk.ac.aber.dcs.cs124group.gui;

import java.awt.Point;
import java.awt.event.*;
import uk.ac.aber.dcs.cs124group.model.DocumentElement;

public abstract class ModelLink {
	
	private Point rangeStart;
	private Point rangeEnd;
	private boolean selected;
	
	protected DocumentElement linkedElement;
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean s) {
		selected = s;
	}
	
	public boolean isInRange(Point p) {
		return (p.x >= rangeStart.x && p.x <= rangeEnd.x 
			 && p.y >= rangeStart.y && p.y <= rangeEnd.y);
	}
	
	public void markForRemoval() {
		linkedElement.markForRemoval();
	}
	
	
	public abstract void processActionEvent(ActionEvent e);
	public abstract void processMouseEvent(MouseEvent e);
	public abstract void processKeyEvent(KeyEvent e);
	protected abstract void calculateRange();
}
