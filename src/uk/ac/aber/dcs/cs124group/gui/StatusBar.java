package uk.ac.aber.dcs.cs124group.gui;


import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
	
	private JLabel textLabel = new JLabel();
	
	public StatusBar() {
		
		this.setPreferredSize(new Dimension(0,20));
		add(textLabel);
	}
	
	public void setText(String text) {
		textLabel.setText(text);
	}
	
	public String getText() {
		return textLabel.getText();
	}
	
	
}
