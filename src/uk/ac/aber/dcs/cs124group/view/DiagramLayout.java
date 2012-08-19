package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.geom.Point2D;

import javax.swing.JLayer;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.gui.Canvas;
import uk.ac.aber.dcs.cs124group.view.*;

/**
 * A custom LayoutManager used on the diagram canvas that respects 
 * the preferred size and location of components. Works very similarly
 * to absolute positioning.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class DiagramLayout implements LayoutManager {



	@Override
	/** 
	 * Sets bounds on every contained component according to the latter's own
	 * preferred size and location.
	 */
	public void layoutContainer(Container canvas) {
		
		int numberOfElements = canvas.getComponentCount();
		Insets insets = canvas.getInsets();
		
		
		
		for(int i = 0; i < numberOfElements; i++) {
			Component c = canvas.getComponent(i);
			Dimension d = c.getPreferredSize();
			Point p = c.getLocation();
			System.out.println(p);
			
			if(canvas instanceof Canvas || canvas instanceof DocumentElementView) {
				Zoom zoom = Manager.getInstance().getZoom();
				if(c instanceof Zoomable) {
					p = ((Zoomable) c).getPreferredLocation();
				}
				else {
					System.out.println("Why am I here?");
					p = new Point(0,0);
				}
				if(c instanceof Zoomable) {
					if(canvas instanceof Canvas) {
						p = zoom.inverseConvertPoint(p, false);
					}
					else {
						p = zoom.inverseConvertPoint(p, true);
					}
					d.height = (int) (d.height * zoom.getZoomLevel());
					d.width = (int) (d.width * zoom.getZoomLevel());
				}
			
			}
			else if(c instanceof JLayer && Manager.getInstance() != null) {
				p = Manager.getInstance().getCanvas().getPreferredLocation();
				//System.out.println(p);
				Zoom zoom = Manager.getInstance().getZoom();
				d.height = (int) (d.height * zoom.getZoomLevel());
				d.width = (int) (d.width * zoom.getZoomLevel());
			}
			
			
			
			c.setBounds(p.x + insets.left, p.y + insets.right, d.width, d.height);
		}
			

	}

	@Override
	public Dimension minimumLayoutSize(Container c) {
		return c.getSize();
	}

	@Override
	public Dimension preferredLayoutSize(Container c) {
		Dimension s = c.getSize();
		return s;
	}
	

	/* Unwanted methods */
	@Override
	public void removeLayoutComponent(Component c) {}
	
	@Override
	public void addLayoutComponent(String arg0, Component c) {}

}
