package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import uk.ac.aber.dcs.cs124group.model.*;

public class LabelController extends DiagramListener {
	private TextLabelModel model;
	
	public LabelController(TextLabelModel m) {
		this.model = m;
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
	
}
