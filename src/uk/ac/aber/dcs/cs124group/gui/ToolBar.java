package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import java.awt.*;

public class ToolBar extends JToolBar {
	String[] fonts = {"Arial", "Helvetica", "Trebuchet MS", "Courier New"};
	JComboBox fontList;
	JSpinner fontSize;
	JSlider zoom;
	public ToolBar() {
		SpringLayout layout = new SpringLayout();
		this.setPreferredSize(new Dimension(0, 30));
		this.setLayout(layout);
		
		fontList = new JComboBox(fonts);
		
		layout.putConstraint(SpringLayout.WEST, fontList, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, fontList, 5, SpringLayout.NORTH, this);
		
		this.add(fontList);
		
		fontSize = new JSpinner();
		fontSize.setValue(10);
		
		layout.putConstraint(SpringLayout.WEST, fontSize, 5, SpringLayout.EAST, fontList);
		layout.putConstraint(SpringLayout.NORTH, fontSize, 5, SpringLayout.NORTH, this);
		
		this.add(fontSize);
		
		zoom = new JSlider();
		zoom.setMaximum(200);
		zoom.setMinimum(25);
		zoom.setValue(100);
		zoom.setMajorTickSpacing(50);
		zoom.setMinorTickSpacing(25);
		zoom.setPaintTicks(true);
		layout.putConstraint(SpringLayout.WEST, zoom, 0, SpringLayout.EAST, fontSize);
		layout.putConstraint(SpringLayout.NORTH, zoom, 0, SpringLayout.NORTH, this);
		
		this.add(zoom);
		
	}
	
	public String getFontName() {
		return (String) (fontList.getSelectedItem());	
	}
	
}
