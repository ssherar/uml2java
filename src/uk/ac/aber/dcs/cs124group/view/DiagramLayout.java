package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;

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
