package uk.ac.aber.dcs.cs124group.prototype;

import javax.swing.JComponent;
import java.awt.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class ClassRectangleComponent extends JComponent {
	
	private ClassRectangle model = new uk.ac.aber.dcs.cs124group.prototype.ClassRectangle();
	private Point position;
	
	public ClassRectangleComponent(Point p) {
		setSize(new Dimension(200,300));
		position = p;
		setOpaque(true);
	}
	
	public void paintComponent(Graphics g) {
		boolean focused = isFocusOwner();
		int lineWidth = focused ? 2 : 1;
		
		g.drawRect(0,0, getSize().width -1, getSize().height - 1);
		g.setColor(Color.WHITE);
		g.fillRect(1, 1, getSize().width - 5, getSize().height - 5);
		
	}
	
	public Point getPosition() {
		return position;
	}
	

}
