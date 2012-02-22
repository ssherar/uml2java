package uk.ac.aber.dcs.cs124group.prototype;
import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.*;

public class Handler implements MouseListener {
	
	private Canvas canvas;
	
	public Handler(Canvas c) {
		this.canvas = c;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount() == 2 && !arg0.isConsumed()) {
			arg0.consume();
			
			int ret = canvas.findLabel((JLabel)arg0.getSource());
			if(ret != -1) {
				//canvas.setVislibility(ret, false);
				canvas.setEditing(ret);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
