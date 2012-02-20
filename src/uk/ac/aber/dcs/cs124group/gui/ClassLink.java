package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import java.awt.event.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class ClassLink extends ModelLink {
	
	private ClassRectangle currentClass;
	
	public void ClassLink(ClassRectangle c) {
		this.currentClass = c;
	}
	
	public int getWidth() {
		return this.currentClass.getSize().width;
	}
	
	public int getHeight() {
		return this.currentClass.getSize().height;
	}
	
	public Point getPosition() {
		return this.currentClass.getPosition();
	}
	
	public String[] getDataFields() {
		//Placesetter...
		return new String[1];
	}
	
	public String[] getMethods() {
		return new String[1];
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
		
	}

}
