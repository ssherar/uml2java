package uk.ac.aber.dcs.cs124group.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import uk.ac.aber.dcs.cs124group.model.*;

/**
 * An all-purpose listener controlling labels (this includes attributes and cardinalities). Is added as a listener to LabelViews.
 * @authors Daniel Maly, Sam Sherar, Lee Smith
 */
public class LabelController extends DiagramListener implements ActionListener {
	
	/** The label model this controller is assigned to. */
	private TextLabelModel model;
	
	private Point startingMousePos;
	
	/** Constructs a controller for the specified label. 
	 * @param m The TextLabelModel to be controlled by this object.
	 */
	public LabelController(TextLabelModel m) {
		this.model = m;
		System.out.println(m);
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && !e.isConsumed() && this.getMode() == ListeningMode.LISTEN_TO_ALL) {
			e.consume();
			this.setMode(ListeningMode.EDITING_TEXT);
			model.setEditing(true);
		}
	}
	
	@Override 
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			model.setEditing(false);
			this.setMode(ListeningMode.LISTEN_TO_ALL);
		}
		
	}
	
	@Override 
	public void mouseDragged(MouseEvent e) {
		if(!this.isNotAttribute() || this.model.isClassName()) return;
		if(this.getMode() != ListeningMode.DRAGGING) {
			this.setMode(ListeningMode.DRAGGING);
			startingMousePos = e.getPoint();
		}
		
		if(this.getMode() == ListeningMode.DRAGGING) {
			Rectangle r = new Rectangle(model.getLocation(),model.getSize());
			r.x += e.getX() - startingMousePos.x;
			r.y += e.getY() - startingMousePos.y;
			this.model.setLocation(r.getLocation(), true);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		if(this.isNotAttribute()) {
			if(this.getMode() == ListeningMode.DRAGGING)
				this.model.stopMoving();
			this.setMode(ListeningMode.LISTEN_TO_ALL);
			
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(!this.isNotAttribute()) return;
		model.setPaintState(ElementPaintState.SELECTED);
	}
	
	private boolean isNotAttribute() {
		return (this.model instanceof Attribute) ? false : true;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.model instanceof Attribute) {
			String c = e.getActionCommand();
			if(c.equals("Delete")) {
				this.model.remove();
			} else if(c.equals("Abstract")) {
				((Attribute)this.model).setAbstract(true, true);
			} else if(c.equals("Static")) {
				((Attribute)this.model).setAbstract(true, true);
			 
			
			} else if(c.equals("None")) {
				((Attribute)this.model).setNone(true, true);
			}
		}
	}
	
}
