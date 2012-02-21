package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import java.awt.*;

public class SideBar extends JPanel {
	
	private JButton newClass, newRelationship, newTextLabel, export;
	private SpringLayout layout;
	
	public SideBar() {
		super();
		this.setPreferredSize(new Dimension(200, 786));
		layout = new SpringLayout();
		this.setLayout(layout);
		
		newClass = new JButton("New Class");
		newRelationship = new JButton("New Relationship");
		newTextLabel = new JButton("New Textlabel");
		export = new JButton("Export to Java...");
		
		newClass.setPreferredSize(newRelationship.getPreferredSize());
		newTextLabel.setPreferredSize(newRelationship.getPreferredSize());
		export.setPreferredSize(newRelationship.getPreferredSize());
		
		layout.putConstraint(SpringLayout.WEST, newClass, 20, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, newClass, 20, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, newRelationship, 20, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, newRelationship, 5, SpringLayout.SOUTH, newClass);
		
		this.add(newClass);
		this.add(newRelationship);
		this.add(newTextLabel);
		this.add(export);
	}
}
