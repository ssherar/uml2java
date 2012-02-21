package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import java.util.*;

public class MenuBar extends JMenuBar {
	
	private JMenu file = new JMenu("File"), edit = new JMenu("Edit"), canvas = new JMenu("Canvas"), export = new JMenu("Export"), help = new JMenu("Help");
	private ArrayList<JMenuItem> fileItems;
	
	public MenuBar() {
		super();
		this.add(file);
		this.add(edit);
		this.add(canvas);
		this.add(export);
		this.add(help);
		
		this.fileItems();
	}
	
	private void fileItems() {
		fileItems = new ArrayList<JMenuItem>();
		fileItems.add(new JMenuItem("New"));
		fileItems.add(new JMenuItem("Open..."));
		fileItems.add(new JMenuItem("Save"));
		fileItems.add(new JMenuItem("Save as..."));
		fileItems.add(new JMenuItem("Print"));
		fileItems.add(new JMenuItem("Exit"));
		
		for(JMenuItem i : fileItems) {
			//i.addActionListener(); act
			file.add(i);
		}
		
	}
	
}
