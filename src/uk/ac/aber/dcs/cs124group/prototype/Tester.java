package uk.ac.aber.dcs.cs124group.prototype;

import uk.ac.aber.dcs.cs124group.gui.*;
import javax.swing.*;
import java.awt.*;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MainFrame window = new MainFrame();
		JPanel testPanel = new JPanel();
		window.add(testPanel);
		testPanel.setLayout(null);
		Insets insets = testPanel.getInsets();
		
		
		ClassRectangleComponent c = new ClassRectangleComponent(new Point(100,100));
		Dimension size = c.getSize();
		testPanel.add(c);
		
		c.setBounds(c.getPosition().x + insets.left, c.getPosition().y + insets.top, size.width, size.height);
		
	}

}
