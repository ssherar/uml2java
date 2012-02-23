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
		
		
		
	    canvas = new Canvas(manager);
	    JScrollPane scroll = new JScrollPane();
	    scroll.setViewportView(canvas);
	    scroll.setPreferredSize(new Dimension(924,700));
	    
	    
	    dummyPanel.setBackground(Color.GRAY);
	    dummyPanel.setLayout(null);
	    dummyPanel.add(scroll);
	    
	    Dimension size = scroll.getPreferredSize();
	    Insets insets = dummyPanel.getInsets();
	    scroll.setBounds(insets.left, insets.top, size.width, size.height);
	    
	    // TODO: Debugging scrollbars
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    
	    
	    sideBar = new SideBar(manager);
	    menu = new MenuBar(manager);
	    toolbar = new ToolBar(manager);
	    status = new StatusBar();
	    
	    add(sideBar, BorderLayout.WEST);
	    add(dummyPanel, BorderLayout.CENTER);
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
