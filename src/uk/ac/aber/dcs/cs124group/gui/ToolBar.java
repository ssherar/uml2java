package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import java.awt.*;

public class ToolBar extends JToolBar {
	String[] fonts = {"Arial", "Helvetica", "Trebuchet MS", "Courier New"};
	JComboBox fontList;
	public ToolBar() {
		this.setPreferredSize(new Dimension(0, 30));
		this.setLayout(new GridLayout());
		fontList = new JComboBox(fonts);
		this.add(fontList, BorderLayout.WEST);
	}
	
	public String getFontName() {
		return (String) (fontList.getSelectedItem());
	}
}
