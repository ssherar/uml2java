package uk.ac.aber.dcs.cs124group.gui;

import java.awt.BorderLayout;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private SideBar sideBar;
	private Canvas canvas;
	private MenuBar menu;
	
	public MainFrame() {   
	    canvas = new Canvas();
	    sideBar = new SideBar();
	    menu = new MenuBar();
	    add(sideBar, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
		
		setJMenuBar(menu);
		
	    setSize(1024,768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	    
	}

}
