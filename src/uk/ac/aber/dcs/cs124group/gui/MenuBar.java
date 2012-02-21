package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar {
	
	private JMenu file = new JMenu("File"), 
			      edit = new JMenu("Edit"), 
			      canvas = new JMenu("Canvas"), 
			      export = new JMenu("Export"), 
			      help = new JMenu("Help");
	private LinkedList<JMenuItem> fileItems, editItems, canvasItems, exportItems, helpItems;
	private Manager manager;
	
	public MenuBar(Manager manager) {
		super();
		this.manager = manager;
		
		this.add(file);
		this.add(edit);
		this.add(canvas);
		this.add(export);
		this.add(help);
		
		this.addFileItems();
		this.addEditItems();
		this.addCanvasItems();
		this.addExportItems();
		this.addHelpItems();
	}
	
	private void addFileItems() {
		fileItems = new LinkedList<JMenuItem>();
		fileItems.add(new JMenuItem("New"));
		fileItems.add(new JMenuItem("Open..."));
		fileItems.add(new JMenuItem("Save"));
		fileItems.add(new JMenuItem("Save as..."));
		fileItems.add(new JMenuItem("Print"));
		fileItems.add(new JMenuItem("Exit"));
		
		fileItems.get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		fileItems.get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileItems.get(2).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileItems.get(4).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		fileItems.get(5).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		
		for(JMenuItem i : fileItems) {
			i.addActionListener(manager);
			file.add(i);
		}
		
	}
	
	private void addEditItems() {
		editItems = new LinkedList<JMenuItem>();
		editItems.add(new JMenuItem("Undo"));
		editItems.add(new JMenuItem("Redo"));
		
		editItems.get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editItems.get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		
		for(JMenuItem i : editItems) {
			i.addActionListener(manager);
			edit.add(i);
		}
	}
	
	private void addCanvasItems() {
		canvasItems = new LinkedList<JMenuItem>();
		canvasItems.add(new JMenuItem("Resize..."));
		canvasItems.add(new JMenuItem("Fit to diagram"));
		
		for(JMenuItem i : canvasItems) {
			i.addActionListener(manager);
			canvas.add(i);
		}
	}
	
	private void addExportItems() {
		exportItems = new LinkedList<JMenuItem>();
		exportItems.add(new JMenuItem("Code"));
		exportItems.add(new JMenuItem("Image"));
		
		for(JMenuItem i : exportItems) {
			i.addActionListener(manager);
			export.add(i);
		}
	}
	
	private void addHelpItems() {
		helpItems = new LinkedList<JMenuItem>();
		helpItems.add(new JMenuItem("Documentation"));
		helpItems.add(new JMenuItem("About"));
		
		for(JMenuItem i : helpItems) {
			i.addActionListener(manager);
			help.add(i);
		}
	}
	
}
