package uk.ac.aber.dcs.cs124group.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

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
		if(c.equals("Add Relationship")) {
			//TODO: Implement
		}
		if(c.equals("Add Data Field")) {
			model.requestNewDataField();
			this.setMode(ListeningMode.EDITING_TEXT); 
		}
		if(c.equals("Add Method")) {
			model.requestNewMethod();
			this.setMode(ListeningMode.EDITING_TEXT); 
		}
		if(c.equals("Remove")) {
			model.remove();
		}
		if (c.equals("Abstract")){
			model.setAbstract(true, true);

		}
		if (c.equals("Final")){
			model.setFinal(true, true);

		}
		if (c.equals("Static")){
			model.setStatic(true, true);

		}
		if (c.equals("None")){
			model.removeFlags();
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(model.getPaintState() != ElementPaintState.MOUSED_OVER_RESIZE) {
			model.setPaintState(ElementPaintState.SELECTED);
		}

	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		

		Point bottomRightCorner = new Point (model.getSize().width, model.getSize().height);

		
		Rectangle notResizeRectangle = new Rectangle(new Point(0,0), model.getSize());
		notResizeRectangle.grow(-5, -5);
		
		
		if(e.getPoint().distance(bottomRightCorner) < 30) {
			model.setPaintState(ElementPaintState.MOUSED_OVER_RESIZE);
		}
		else {
			model.setPaintState(ElementPaintState.MOUSED_OVER);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		this.setMode(ListeningMode.LISTEN_TO_ALL);
		this.model.stopMoving();
	}

	@Override
	public void mouseDragged(MouseEvent e){
		if(this.getMode() != ListeningMode.DRAGGING) {
			this.setMode(ListeningMode.DRAGGING);
			startingMousePosition = e.getPoint();
		}
		
		if (this.getMode() == ListeningMode.DRAGGING && !(model.getPaintState().toString().contains("RESIZE"))){
			Rectangle r = new Rectangle(model.getLocation(), model.getSize());
            r.x += e.getX() - startingMousePosition.x;  
            r.y += e.getY() - startingMousePosition.y;
			model.setLocation(r.getLocation(), true);
		}
		else if(model.getPaintState() == ElementPaintState.MOUSED_OVER_RESIZE) {
			Rectangle r = new Rectangle(model.getLocation(), model.getSize());
            r.width = e.getX();  
            r.height = e.getY();
            if(r.width < 50) r.width = 50;
            if(r.height < 50) r.height = 50;
			model.setSize(r.getSize(), true);
		}
	}

}
