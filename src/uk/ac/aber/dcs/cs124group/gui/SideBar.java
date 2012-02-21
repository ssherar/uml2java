package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import java.awt.*;

public class SideBar extends JPanel {
	
	private final int SIDE_PADDING = 20;
	private final int TOP_PADDING = 20;
	private final int VERT_PADDING = 5;
	private JButton newClass, newRelationship, newTextLabel, export;
	private SpringLayout layout;
	
	public SideBar() {
		super();
		this.setPreferredSize(new Dimension(200, 786));
		layout = new SpringLayout();
		this.setLayout(layout);
		
		newClass = new JButton("New Class");
		newRelationship = new JButton("New Relationship");
		newTextLabel = new JButton("New label");
		export = new JButton("Export to Java...");
		
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
	}
}
