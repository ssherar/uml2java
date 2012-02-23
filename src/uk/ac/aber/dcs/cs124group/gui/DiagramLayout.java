package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;

public class DiagramLayout implements LayoutManager {



	@Override
	public void layoutContainer(Container canvas) {
		
		int numberOfElements = canvas.getComponentCount();
		
		Insets insets = canvas.getInsets();
		
		//for(int i = numberOfElements - 1; i >= 0; i--) {
		for(int i = 0; i < numberOfElements; i++) {
			Component c = canvas.getComponent(i);
			Dimension d = c.getPreferredSize();
			Point p = c.getLocation();
			
			c.setBounds(p.x + insets.left, p.y + insets.right, d.width, d.height);
		}
		
		
		

	}

	@Override
	public Dimension minimumLayoutSize(Container c) {
		return c.getPreferredSize();
	}

	@Override
	public Dimension preferredLayoutSize(Container c) {
		return c.getPreferredSize();
	}
	
	
	


	/* Unwanted methods */
	@Override
	public void removeLayoutComponent(Component c) {}
	
	@Override
	public void addLayoutComponent(String arg0, Component c) {}

}
