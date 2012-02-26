package uk.ac.aber.dcs.cs124group.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import uk.ac.aber.dcs.cs124group.model.*;

/**
 * An all-purpose listener controlling class models. Is added as a listener to ClassRectangles.
 * @authors Daniel Maly, Sam Sherar, Lee Smith
 *
 */
public class ClassController extends DiagramListener implements ActionListener {
	
	/** The class model this controller is assigned to. */
	private ClassModel model;
	
	/** A reference point for mouse dragging. */
	private Point startingMousePosition;
	
	/** Constructs a controller for the specified class model. 
	 * @param c The model this controller will be assigned to.
	 */
	public ClassController(ClassModel c) {
		this.model = c;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if(c.equals("Add relationship")) {
			//TODO: Implement
		}
		if(c.equals("Add data field")) {
			model.requestNewDataField();
			this.setMode(ListeningMode.EDITING_TEXT); 
		}
		if(c.equals("Add method")) {
			model.requestNewMethod();
			this.setMode(ListeningMode.EDITING_TEXT); 

		}
		if(c.equals("Remove")) {
			model.remove();
		}

	}
	
	@Override
	public void mousePressed(MouseEvent e){
		model.setPaintState(ElementPaintState.SELECTED);

	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		this.setMode(ListeningMode.LISTEN_TO_ALL);
	}

	@Override
	public void mouseDragged(MouseEvent e){
		if(this.getMode() != ListeningMode.DRAGGING && model.getPaintState() != ElementPaintState.MOUSED_OVER_RESIZE) {
			this.setMode(ListeningMode.DRAGGING);
			startingMousePosition = e.getPoint();
		}
		
		if (this.getMode() == ListeningMode.DRAGGING){
			Rectangle r = new Rectangle(model.getLocation(), model.getSize());
            r.x += e.getX() - startingMousePosition.x;  
            r.y += e.getY() - startingMousePosition.y;
            r.setBounds(r);
			model.setLocation(r.getLocation());
		}
	}

}
