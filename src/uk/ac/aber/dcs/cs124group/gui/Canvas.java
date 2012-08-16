package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.view.DiagramLayout;
import uk.ac.aber.dcs.cs124group.view.Zoom;

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
	
	private Zoom zoom;


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
	        	 setPreferredSize(Manager.DEFAULT_CANVAS_SIZE);
	         }
	      });
		
	}
	
	@Override
	/**
	 * Overridden only to set rendering hints for all the children that will be painted later.
	 */
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		
		g.transform(zoom.getAffineTransform());

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
	
	public void setZoom(Zoom zoom) {
		this.zoom = zoom;
	}
	
	public Zoom getZoom() {
		return zoom;
	}
	
	public boolean isBiggerThanViewport() {
		Dimension parentSize = this.getParent().getSize();
		
		return (this.getPreferredSize().width  >= parentSize.width
			 && this.getPreferredSize().height >= parentSize.height);
				
	}
	
	public void center(Rectangle r) {

		if(r.width > this.getPreferredSize().width && r.height > this.getPreferredSize().height) {
			
			int x = (r.width / 2) - (this.getPreferredSize().width / 2);
			int y = (r.height / 2) - (this.getPreferredSize().height / 2);
			
			this.setLocation(new Point(x, y));
			
			this.getParent().doLayout();
		}
		else {
			this.setLocation(new Point(0,0));
			this.getParent().doLayout();
		}
	}
	
 }
