package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import java.awt.*;

public class ToolBar extends JToolBar {
	
	private String[] fonts = {"Arial", "Helvetica", "Trebuchet MS", "Courier New"};
	private JComboBox fontList;
	private JSpinner fontSize;
	private JSlider zoom;
	private JLabel zoomLabel = new JLabel("Zoom: ");
	
	private Manager manager;
	
	public ToolBar(Manager manager) {
		this.manager = manager;
		
		SpringLayout layout = new SpringLayout();
		this.setPreferredSize(new Dimension(0, 30));
		this.setLayout(layout);
		
		fontList = new JComboBox(fonts);
		
		layout.putConstraint(SpringLayout.WEST, fontList, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, fontList, 5, SpringLayout.NORTH, this);
		
		this.add(fontList);
		
		fontSize = new JSpinner();
		fontSize.setValue(11);
		
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
		zoom.addChangeListener(manager);
		//layout.putConstraint(SpringLayout.WEST, zoom, 0, SpringLayout.EAST, fontSize);
		layout.putConstraint(SpringLayout.EAST, zoom, 2, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, zoom, 0, SpringLayout.NORTH, this);
		
		this.add(zoom);
		
		layout.putConstraint(SpringLayout.NORTH, zoomLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, zoomLabel, -9, SpringLayout.WEST, zoom);
		
		this.add(zoomLabel);
		
	}
	
	public String getFontName() {
		return (String) (fontList.getSelectedItem());	
	}
	
	public int getFontSize() {
		return (Integer) (fontSize.getValue());
	}
	
	public int getZoom() {
		return zoom.getValue();
	}
	
	public void overrideFont(Font f) {
		fontList.setSelectedItem(f.getFontName());
		fontSize.setValue(f.getSize());
	}
	
}
