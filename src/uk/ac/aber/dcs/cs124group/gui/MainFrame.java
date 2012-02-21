package uk.ac.aber.dcs.cs124group.gui;

import java.awt.BorderLayout;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private SideBar sideBar;
	private Canvas canvas;
	private MenuBar menu;
	
	public MainFrame() {  
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
	    canvas = new Canvas();
	    JScrollPane scroll = new JScrollPane(canvas);
	    sideBar = new SideBar();
	    menu = new MenuBar();
	    
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    add(sideBar, BorderLayout.WEST);
		add(scroll, BorderLayout.CENTER);
		
		setJMenuBar(menu);
		
	    setSize(1024,768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	    
	}

}
