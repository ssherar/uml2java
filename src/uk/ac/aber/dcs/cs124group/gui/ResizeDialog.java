package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;

/**
 * A small dialog window that allows the user to resize the canvas.
 * @see MainFrame
 * @see Canvas
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class ResizeDialog extends JFrame {
	
	/**
	 * The Canvas object to be resized.
	 */
	private Canvas canvas;
	
	/**
	 * Initialises the resize window and adds appropriate listeners.
	 * @param c
	 * 		The Canvas object to be resized.
	 */
	public ResizeDialog(Canvas c) {
		this.canvas = c;
		
		this.setSize(new Dimension(200,120));
		this.setResizable(false);
		this.setTitle("Resizing");
		
		/* Lays out two panels - one for the input fields and another one for the confirm and cancel buttons. */
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2,2,10,10));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		JLabel width = new JLabel("Width");
		JLabel height = new JLabel("Height");
		
		final JTextField widthField = new JTextField();
		widthField.setText(canvas.getPreferredSize().width + "");
		final JTextField heightField = new JTextField();
		heightField.setText(canvas.getPreferredSize().height + "");

		
		inputPanel.add(width);
		inputPanel.add(widthField);
		inputPanel.add(height);
		inputPanel.add(heightField);
		
		final JButton okButton = new JButton("Confirm");
		buttonPanel.add(okButton);
		
		/* On clicking confirm, this listener will resize the canvas and close the resize dialog
		 * but only if there are no parsing errors when getting input from the JTextFields.
		 */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int width = Integer.parseInt(widthField.getText());
					int height = Integer.parseInt(heightField.getText());
					
					canvas.setPreferredSize(new Dimension(width, height));
					canvas.getParent().doLayout();
					setVisible(false);
				}
				catch(Exception ex) {
					
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		
		/*
		 * Upon clicking cancel, this listener closes the window. 
		 */
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
				
		this.setAlwaysOnTop(true);
		this.add(inputPanel);
		this.add(buttonPanel, BorderLayout.SOUTH);
		

	}

}
