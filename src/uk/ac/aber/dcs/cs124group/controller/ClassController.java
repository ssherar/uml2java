package uk.ac.aber.dcs.cs124group.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import uk.ac.aber.dcs.cs124group.gui.ListeningMode;
import uk.ac.aber.dcs.cs124group.model.*;

public class ClassController extends DiagramListener implements ActionListener {
	
	private ClassModel model;
	private Point startingMousePosition;
	
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
			model.addNewAttribute(AttributeType.DATA_FIELD);
			this.setMode(ListeningMode.EDITING_TEXT); 
		}
		if(c.equals("Add method")) {
			model.addNewAttribute(AttributeType.METHOD);
			this.setMode(ListeningMode.EDITING_TEXT); 

		}
		if(c.equals("Remove")) {
			model.remove();
		}

	}
	
	public void mousePressed(MouseEvent e){
		model.setPaintState(ElementPaintState.SELECTED);

	}
	
	public void mouseReleased(MouseEvent e){
		this.setMode(ListeningMode.LISTEN_TO_ALL);
	}

	public void mouseDragged(MouseEvent e){
		if(this.getMode() != ListeningMode.DRAGGING && model.getPaintState() != ElementPaintState.MOUSED_OVER_RESIZE) {
			this.setMode(ListeningMode.DRAGGING);
			startingMousePosition = e.getPoint();
		}
		
		if (this.getMode() == ListeningMode.DRAGGING){
			Rectangle r = model.getBounds();
            r.x += e.getX() - startingMousePosition.x;  
            r.y += e.getY() - startingMousePosition.y;
            r.setBounds(r);
			model.setLocation(r.getLocation());
			model.getParent().doLayout();
		}
		model.repaint();
	}

}
