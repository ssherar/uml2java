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
	
	public MainFrame(Manager manager) {
		this.manager = manager;
		
		addWindowListener(this);
		
		/** Layout the GUI */
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		JPanel dummyPanel = new JPanel();
		add(dummyPanel);
		
		
	    canvas = new Canvas(manager);
	    JScrollPane scroll = new JScrollPane();
	    scroll.setViewportView(canvas);
	    scroll.setPreferredSize(new Dimension(900,800));
		canvas.setPreferredSize(new Dimension(2000,2000));
	    
	    dummyPanel.setBackground(Color.GRAY);
	    dummyPanel.setLayout(new DiagramLayout());
	    dummyPanel.add(scroll);   
	    
	    
	    sideBar = new SideBar(manager);
	    menu = new MenuBar(manager);
	    toolbar = new ToolBar(manager);
	    status = new StatusBar();
	    
	    add(sideBar, BorderLayout.WEST);
		add(toolbar, BorderLayout.NORTH);
		add(status, BorderLayout.SOUTH);
		
		setJMenuBar(menu);
		
	    setSize(1024,768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		this.setMinimumSize(new Dimension(1024,600));

	    
	}
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public SideBar getSideBar() {
		return sideBar;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public MenuBar getMenu() {
		return menu;
	}

	public ToolBar getToolbar() {
		return toolbar;
	}

	public StatusBar getStatus() {
		return status;
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
