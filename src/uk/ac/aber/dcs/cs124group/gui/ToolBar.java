package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import uk.ac.aber.dcs.cs124group.controller.Manager;

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
		
		this.setLayout(layout);
		
		fontList = new JComboBox(fonts);
		fontList.setActionCommand("fonts");
		fontList.addActionListener(manager);
		
		layout.putConstraint(SpringLayout.WEST, fontList, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, fontList, 5, SpringLayout.NORTH, this);
		
		this.add(fontList);
		
		fontSize = new JSpinner(new SpinnerNumberModel() {
			
			/* Set minimum and maximum font sizes */
			public Comparable getMinimum() {
				return 8;
			}
			
			public Comparable getMaximum() {
				return 20;
			}
			
			/* Fix a bug in Java allowing the Spinner to exceed minimum / maximum by 1 and stop working*/
			public Object getPreviousValue() {
				if((Integer) super.getPreviousValue() < (Integer) (getMinimum()))
					return null;
				else return super.getPreviousValue();
			}
			
			public Object getNextValue() {
				if((Integer) super.getNextValue() > (Integer) (getMaximum()))
					return null;
				else return super.getNextValue();
			}
			
			
		});
		fontSize.setValue(11);
		fontSize.addChangeListener(manager);
		
		
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
		this.setPreferredSize(new Dimension(0, zoom.getPreferredSize().height));
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
