package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import java.awt.*;

public class ToolBar extends JToolBar {
	String[] fonts = {"Arial", "Helvetica", "Trebuchet MS", "Courier New"};
	JComboBox fontList;
	JTextField fontSize;
	public ToolBar() {
		SpringLayout layout = new SpringLayout();
		this.setPreferredSize(new Dimension(0, 30));
		this.setLayout(layout);
		
		fontList = new JComboBox(fonts);
		
		layout.putConstraint(SpringLayout.WEST, fontList, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, fontList, 0, SpringLayout.NORTH, this);
		
		this.add(fontList);
		fontSize = new JTextField(2);
		
		layout.putConstraint(SpringLayout.WEST, fontSize, 5, SpringLayout.EAST, fontList);
		layout.putConstraint(SpringLayout.NORTH, fontSize, 0, SpringLayout.NORTH, this);
		
		this.add(fontSize);
		
	}
	
	public String getFontName() {
		return (String) (fontList.getSelectedItem());	
	}
}
