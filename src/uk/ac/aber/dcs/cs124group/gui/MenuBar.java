package uk.ac.aber.dcs.cs124group.gui;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.controller.Manager;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.*;

/**
 * The menu bar at the top of the application window. 
 * Contains some of the program controls.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class MenuBar extends JMenuBar {
	
	/**
	 * The individual menus in the menu bar.
	 */
	private JMenu file = new JMenu("File"), 
			      edit = new JMenu("Edit"), 
			      canvas = new JMenu("Canvas"), 
			      export = new JMenu("Export"), 
			      help = new JMenu("Help");
	
	/**
	 * A linked list for each menu containing individual menu items.
	 */
	private LinkedList<JMenuItem> fileItems, editItems, canvasItems, exportItems, helpItems;
	
	/**
	 * The Manager object listening to clicks on the menu items.
	 */
	private Manager manager;
	
	/**
	 * The system default shortcut key.
	 */
	int shortCutKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	/**
	 * Constructs a new menu bar for the application with the specified manager.
	 * 
	 * @param manager
	 * 		The listener that takes action in response to user clicks in the menu bar.
	 */
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
	
	/**
	 * Adds and sets accelerators for menu items in the "File" menu.
	 */
	private void addFileItems() {
		fileItems = new LinkedList<JMenuItem>();
		fileItems.add(new JMenuItem("New"));
		fileItems.add(new JMenuItem("Open..."));
		fileItems.add(new JMenuItem("Save"));
		fileItems.add(new JMenuItem("Save as..."));
		fileItems.add(new JMenuItem("Print"));
		fileItems.add(new JMenuItem("Exit"));
		
		
		
		fileItems.get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortCutKey));
		fileItems.get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortCutKey));
		fileItems.get(2).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortCutKey));
		fileItems.get(4).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortCutKey));
		fileItems.get(5).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortCutKey));
		
		for(JMenuItem i : fileItems) {
			i.addActionListener(manager);
			file.add(i);
		}
		
	}
	
	/**
	 * Adds and sets accelerators for menu items in the "Edit" menu.
	 * Assigns the manager as ActionListener to these menu items.
	 */
	private void addEditItems() {
		editItems = new LinkedList<JMenuItem>();
		editItems.add(new JMenuItem("Undo"));
		editItems.add(new JMenuItem("Redo"));
		
		editItems.get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortCutKey));
		editItems.get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, shortCutKey));
		
		for(JMenuItem i : editItems) {
			i.addActionListener(manager);
			edit.add(i);
		}
	}
	
	/**
	 * Adds and sets accelerators for menu items in the "Canvas" menu.
	 * Assigns the manager as ActionListener to these menu items.
	 */
	private void addCanvasItems() {
		canvasItems = new LinkedList<JMenuItem>();
		canvasItems.add(new JMenuItem("Resize..."));
		canvasItems.add(new JMenuItem("Fit to diagram"));
		
		for(JMenuItem i : canvasItems) {
			i.addActionListener(manager);
			canvas.add(i);
		}
	}
	
	/**
	 * Adds and sets accelerators for menu items in the "Export" menu.
	 * Assigns the manager as ActionListener to these menu items.
	 */
	private void addExportItems() {
		exportItems = new LinkedList<JMenuItem>();
		exportItems.add(new JMenuItem("Code"));
		exportItems.add(new JMenuItem("Image"));
		
		for(JMenuItem i : exportItems) {
			i.addActionListener(manager);
			export.add(i);
		}
	}
	
	/**
	 * Adds and sets accelerators for menu items in the "Help" menu.
	 * Assigns the manager as ActionListener to these menu items.
	 */
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
