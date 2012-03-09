package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.*;

/**
 * An adapter for controllers of elements on the canvas.
 * @see ClassController
 * @see LabelController
 * @see RelationshipController
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class DiagramListener implements KeyListener, MouseMotionListener, MouseListener {
	
	/** The current state of this DiagramListener. Implementations use this to make decisions on how to process events. */
	private ListeningMode mode = ListeningMode.LISTEN_TO_ALL;

	/**
	 * @return The current ListeningMode of this DiagramListener.
	 */
	public ListeningMode getMode() {
		return mode;
	}

	/**
	 * Sets the current state of this DiagramListener.
	 * @param mode The new ListeningMode for this object.
	 */
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
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	


}
