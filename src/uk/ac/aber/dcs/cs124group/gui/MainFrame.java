package uk.ac.aber.dcs.cs124group.gui;

import java.awt.BorderLayout;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class MainFrame extends JFrame implements WindowListener {
	
	private SideBar sideBar;
	private Canvas canvas;
	private MenuBar menu;
	private ToolBar toolbar;
	private StatusBar status;
	private Manager manager;
	
	public MainFrame() {
		
		addWindowListener(this);
		
		/** Layout the GUI */
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
	    canvas = new Canvas();
	    JScrollPane scroll = new JScrollPane(canvas);
	    sideBar = new SideBar();
	    menu = new MenuBar();
	    toolbar = new ToolBar();
	    status = new StatusBar();
	    
	    add(sideBar, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
		add(toolbar, BorderLayout.NORTH);
		add(status, BorderLayout.SOUTH);
		
		setJMenuBar(menu);
		
	    setSize(1024,768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		this.setMinimumSize(new Dimension(1024,600));
		
		//Dummy code
		manager = new Manager();
	    
	}

	@Override
	public void windowClosing(WindowEvent e) {
		manager.exit();
		
	}

	/** Unwanted methods */
	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
		
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
		
	}

}
