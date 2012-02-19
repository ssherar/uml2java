package uk.ac.aber.dcs.cs124group.gui;

import java.awt.Point;
import java.awt.event.*;

public abstract class ModelLink {
	
	private Point rangeStart;
	private Point rangeEnd;
	private boolean selected;
	
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
	
	public abstract void processActionEvent(ActionEvent e);
	public abstract void processMouseEvent(MouseEvent e);
	public abstract void processKeyEvent(KeyEvent e);
}
