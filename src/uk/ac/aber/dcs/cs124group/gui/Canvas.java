package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.view.DiagramLayout;

import javax.swing.*;

/**
 * A JPanel displaying all visible DocumentElementViews. 
 * Represents the actual class diagram that's being created.
 * @see uk.ac.aber.dcs.cs124group.view.DocumentElementView DocumentElementView
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */

public class Canvas extends JPanel {
	
	
	/** The global GUI listener that manages adding objects to this canvas. */
	private Manager manager;
	
	/** The zoom factor currently set on the document. 
	 * NOTE: The zoom feature is currently disabled due to not being fully implemented. Future releases may enable it.
	 */
	private double zoomFactor = 1;


	/**
	 * Constructs a new blank canvas.
	 * @param manager
	 * 			The Manager that will manage adding objects to this canvas. 
	 */
	public Canvas(Manager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		this.setLayout(new DiagramLayout());
		
		this.addMouseListener(manager);
		
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 setLocation(new Point(0,0));
	        	 setPreferredSize(new Dimension(2000,2000));
	         }
	      });
		
	}
	
	/**
	 * NOTE: The zoom feature is currently disabled due to not being fully implemented. Future releases may enable it.
	 * @param zoomFactor
	 */
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
		
	}
	
	/**
	 * NOTE: The zoom feature is currently disabled due to not being fully implemented. Future releases may enable it.
	 * @return
	 * 		The current zoom factor
	 */
	public double getZoomFactor() {
		return zoomFactor;
	}
	
	@Override
	/**
	 * Overridden only to set rendering hints for all the children that will be painted later.
	 */
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;

		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);		

	}
			
	
	@Override
	/**
	 * Overridden to correctly layer components on top of one another so that the added components are displayed in a stack-like fashion.
	 */
	public Component add(Component c){
		super.add(c);
		this.setComponentZOrder(c, 0);
		return c;
	}
	
 }
