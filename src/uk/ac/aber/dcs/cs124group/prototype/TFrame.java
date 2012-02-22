package uk.ac.aber.dcs.cs124group.prototype;
import java.awt.*;

import javax.swing.*;


public class TFrame extends JFrame {
	Canvas canvas;
	public TFrame() {
		setTitle("Testing text");
		setSize(1024,768);
		setMinimumSize(new Dimension(1024,768));
		setMaximumSize(new Dimension(1024,768));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		this.canvas = new Canvas();
		this.add(this.canvas, BorderLayout.CENTER);
	}
}
