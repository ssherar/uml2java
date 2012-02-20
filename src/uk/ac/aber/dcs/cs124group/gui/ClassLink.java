package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import java.awt.event.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class ClassLink extends ModelLink {
	
	
	
	public ClassLink(ClassRectangle c) {
		this.linkedElement = c;
	}
	
	public int getWidth() {
		return ((ClassRectangle)this.linkedElement).getSize().width;
	}
	
	public int getHeight() {
		return ((ClassRectangle)this.linkedElement).getSize().height;
	}
	
	public Point getPosition() {
		return ((ClassRectangle)this.linkedElement).getPosition();
	}
	
	public String[] getDataFields() {
		//TODO define
		return new String[1];
	}
	
	public String[] getMethods() {
		//TODO define
		return new String[1];
	}
	
	@Override //We need the overriden method to be called in ClassRectangle
	public void markForRemoval() {
		((ClassRectangle) linkedElement).markForRemoval();
	}

	@Override
	public void processActionEvent(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMouseEvent(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processKeyEvent(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void calculateRange() {
		//TODO define
	}

}
