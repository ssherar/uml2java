package uk.ac.aber.dcs.cs124group.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;

import uk.ac.aber.dcs.cs124group.model.*;

/**
 * An all-purpose listener controlling labels (this includes attributes and cardinalities). Is added as a listener to LabelViews.
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class LabelController extends DiagramListener implements ActionListener {
	
	/** The label model this controller is assigned to. */
	private TextLabelModel model;
	
	private Point startingMousePos;
	
	/** Constructs a controller for the specified label. 
	 * @param m 
	 * 		The TextLabelModel to be controlled by this object.
	 */
	public LabelController(TextLabelModel m) {
		this.model = m;
	}
	

	@Override
	/**
	 * If the user double clicks the label, calls setEditing(true) in TextLabelModel to enable the user edit the label.
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && !e.isConsumed() && this.getMode() == ListeningMode.LISTEN_TO_ALL) {
			e.consume();
			this.setMode(ListeningMode.EDITING_TEXT);
			model.setEditing(true);
		}
	}
	
	@Override 
	/**
	 * If the user presses ENTER or ESC while editing a label, this will make it exit editing mode.
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			model.setEditing(false);
			this.setMode(ListeningMode.LISTEN_TO_ALL);
		}
		
	}
	
	@Override 
	/**
	 * If this is a free label (e.g. not an Attribute or a class name), moves it around.
	 */
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
	/**
	 * Stops moving the label.
	 */
	public void mouseReleased(MouseEvent e){
		if(this.isNotAttribute()) {
			if(this.getMode() == ListeningMode.DRAGGING)
				this.model.stopMoving();
			this.setMode(ListeningMode.LISTEN_TO_ALL);
			
		}
	}
	
	@Override
	/**
	 * Sets the paintState of a free label to SELECTED.
	 */
	public void mousePressed(MouseEvent e){
		if(!this.isNotAttribute()) return;
		model.setPaintState(ElementPaintState.SELECTED);
	}
	
	/**
	 * Checks that the model controlled by this listener is not an Attribute.
	 * Attributes cannot be moved by the user.
	 * @return
	 * 		True if the model is not an instance of Attribute, false otherwise.
	 */
	private boolean isNotAttribute() {
		return (this.model instanceof Attribute) ? false : true;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.model instanceof Attribute) {
			String c = e.getActionCommand();
			if(c.equals("Delete")) {
				this.model.userRemove();
				
			} else if(c.equals("Abstract")) {
				((Attribute)this.model).setNone(true, false);
				((Attribute)this.model).setAbstract(true, true);
				
			} else if(c.equals("Static")) {
				((Attribute)this.model).setNone(true, false);
				((Attribute)this.model).setStatic(true, true);
				
			} else if(c.equals("Final")) {
				boolean isSelected = ((Attribute)this.model).isFlagFinal();
				((Attribute)this.model).setFinal(!(isSelected), true);
				
			} else if(c.equals("Transient")) {
				boolean isSelected = ((Attribute)this.model).isFlagTransient();
				((Attribute)this.model).setTransient(!(isSelected), true);
				
			} else if(c.equals("None")) {
				((Attribute)this.model).setNone(true, true);
			}

		}
	}
	
}
