package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.gui.Canvas;

public class ResizeDialog extends JFrame {
	
	private Canvas canvas;
	
	public ResizeDialog(Canvas c) {
		this.canvas = c;
		
		this.setSize(new Dimension(200,120));
		this.setResizable(false);
		this.setTitle("Resizing");
		
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
