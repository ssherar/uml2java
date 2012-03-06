package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import uk.ac.aber.dcs.cs124group.controller.Manager;

import java.awt.*;

/**
 * The application Toolbar, currently containing global font selection and zoom slider.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class ToolBar extends JToolBar {
	
	/** The available font families. */
	private String[] fonts = {"Arial", "Helvetica", "Trebuchet MS", "Courier New"};
	
	/** The combo box containing available font names. */
	private JComboBox fontList;
	
	/** A JSpinner allowing the user to set the font size. */
	private JSpinner fontSize;
	
	/**
	 * Zoom slider.
	 * @deprecated v0.9
	 */
	private JSlider zoom;
	
	/** A helpful label next to the zoom slider */
	private JLabel zoomLabel = new JLabel("Zoom: ");
	
	/**
	 * Constructs a new ToolBar for the application.
	 * 
	 * @param manager
	 * 		The listener taking action in response to user clicks.
	 */
	public ToolBar(Manager manager) {
		
		SpringLayout layout = new SpringLayout();
		
		this.setLayout(layout);
		
		fontList = new JComboBox(fonts);
		fontList.setActionCommand("fonts");
		fontList.addActionListener(manager);
		
		layout.putConstraint(SpringLayout.WEST, fontList, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, fontList, 5, SpringLayout.NORTH, this);
		
		this.add(fontList);
		
		/* Defining the JSpinner for font sizes */
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

		layout.putConstraint(SpringLayout.EAST, zoom, 2, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, zoom, 0, SpringLayout.NORTH, this);
		
		this.add(zoom);
		
		/*Zoom is intentionally disabled here */
		zoom.setEnabled(false);
		
		layout.putConstraint(SpringLayout.NORTH, zoomLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, zoomLabel, -9, SpringLayout.WEST, zoom);
		
		this.add(zoomLabel);
		this.setPreferredSize(new Dimension(0, zoom.getPreferredSize().height));
	}
	
	/**
	 * @return The name of the font currently selected in the JComboBox.
	 * @see Manager#changeFont()
	 */
	public String getFontName() {
		return (String) (fontList.getSelectedItem());	
	}
	
	/**
	 * @return The number currently selected in the font size JSpinner.
	 * @see Manager#changeFont()
	 */
	public int getFontSize() {
		return (Integer) (fontSize.getValue());
	}
	
	/**
	 * @return The current value of the zoom slider
	 * @deprecated v0.9
	 */
	public int getZoom() {
		return zoom.getValue();
	}
	
	/**
	 * Sets the name and size of the specified font into the ToolBar.
	 * @param f
	 * 		The overriding font
	 */
	public void overrideFont(Font f) {
		fontList.setSelectedItem(f.getFontName());
		fontSize.setValue(f.getSize());
	}
	
}
