package uk.ac.aber.dcs.cs124group.gui;


import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
	
	private JLabel textLabel = new JLabel(" Welcome!");
	private JLabel mousePosX = new JLabel("0",JLabel.RIGHT);
	private JLabel mousePosY = new JLabel("0",JLabel.RIGHT);
	
	public StatusBar() {
		setLayout(new GridLayout(1,1,5,0));	
		this.setPreferredSize(new Dimension(0,20));
		add(textLabel);
		add(mousePosX);
		add(mousePosY);		
	}
	
	public void setText(String text) {
		textLabel.setText(" " + text);
	}
	
	public String getText() {
		return textLabel.getText().trim();
	}
	
	public void setMousePos(int x, int y){
		mousePosX.setText("x co-ord = " + Integer.toString(x));
		mousePosY.setText("y co-ord = " + Integer.toString(y));
	}
	
}
