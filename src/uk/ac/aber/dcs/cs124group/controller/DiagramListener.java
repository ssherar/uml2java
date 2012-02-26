package uk.ac.aber.dcs.cs124group.controller;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DiagramListener implements KeyListener, MouseMotionListener, MouseListener, java.io.Serializable {
	
	private ListeningMode mode = ListeningMode.LISTEN_TO_ALL;



	public ListeningMode getMode() {
		return mode;
	}

	public void setMode(ListeningMode mode) {
		this.mode = mode;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override 
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	


}
