package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.controller.Manager;

import java.awt.*;

/**
 * The BlueJ-like sidebar containing buttons for adding classes,
 * relationships, labels and exporting to java code.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */


public class SideBar extends JPanel {
	
	private final int SIDE_PADDING = 10;
	private final int TOP_PADDING = 10;
	private final int VERT_PADDING = 5;
	
	/** The buttons on the sidebar */
	private JButton newClass, newRelationship, newTextLabel, export;
	
	/** The layout manager used for this component. */
	private SpringLayout layout;
	
	/** The listener that acts upon receiving events from the buttons. */
	private Manager manager;
	
	/**
	 * Constructs a sidebar for the application.
	 * 
	 * @param manager
	 * 		The listener for the sidebar buttons.
	 */
	public SideBar(Manager manager) {
		this.manager = manager;
		layout = new SpringLayout();
		this.setLayout(layout);
		
		newClass = new JButton("New Class");
		newRelationship = new JButton("New Relationship");
		newTextLabel = new JButton("New label");
		export = new JButton("Export to Java...");
		
		newClass.addActionListener(manager);
		newRelationship.addActionListener(manager);
		newTextLabel.addActionListener(manager);
		export.addActionListener(manager);
		
		newClass.setPreferredSize(newRelationship.getPreferredSize());
		newTextLabel.setPreferredSize(newRelationship.getPreferredSize());
		export.setPreferredSize(newRelationship.getPreferredSize());
		
		layout.putConstraint(SpringLayout.WEST, newClass, SIDE_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, newClass, TOP_PADDING, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, newRelationship, SIDE_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, newRelationship, VERT_PADDING, SpringLayout.SOUTH, newClass);
		
		layout.putConstraint(SpringLayout.WEST, newTextLabel, SIDE_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, newTextLabel, VERT_PADDING, SpringLayout.SOUTH, newRelationship);
		
		layout.putConstraint(SpringLayout.WEST, export, SIDE_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, export, VERT_PADDING, SpringLayout.SOUTH, newTextLabel);
		
		this.add(newClass);
		this.add(newRelationship);
		this.add(newTextLabel);
		this.add(export);
		
		this.setPreferredSize(new Dimension(newRelationship.getPreferredSize().width + 20, 786));
	}
}