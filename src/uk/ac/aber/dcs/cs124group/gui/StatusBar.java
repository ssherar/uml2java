package uk.ac.aber.dcs.cs124group.gui;


import javax.swing.*;
import java.awt.*;

/**
 * The status bar at the bottom of the application window.
 * Displays helpful hints and status messages for the user.
 * 
 * @see uk.ac.aber.dcs.cs124group.controller.Manager#undoableEditHappened(UndoableEditEvent) Manager.undoableEditHappened()
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */

public class StatusBar extends JPanel {
	
	/**
	 * The JLabel containing the actual text of the status bar.
	 */
	private JLabel textLabel = new JLabel(" Welcome!");
	
	/**
	 * Constructs a new status bar for the application window.
	 */
	public StatusBar() {
		setLayout(new GridLayout(1,1,5,0));	
		this.setPreferredSize(new Dimension(0,20));
		add(textLabel);	
	}
	/**
	 * Sets the displayed text of the status bar.
	 * 
	 * @param text
	 * 		The new status bar text.
	 */
	public void setText(String text) {
		textLabel.setText(" " + text);
	}
	
	/**
	 * @return The current status bar text.
	 */
	public String getText() {
		return textLabel.getText().trim();
	}
	
	
}
