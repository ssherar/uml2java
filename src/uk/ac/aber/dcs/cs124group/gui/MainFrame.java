package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.view.DiagramLayout;

/**
 * The main window of the application. 
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class MainFrame extends JFrame implements WindowListener {
	
	/**
	 * The sidebar containing the buttons to create new elements.
	 */
	private SideBar sideBar;
	
	/**
	 * The JPanel on which the class diagram is being drawn.
	 */
	private Canvas canvas;
	
	/**
	 * The top menu bar.
	 */
	private MenuBar menu;
	
	/**
	 * The toolbar containing font configuration selection and the zoom slider.
	 */
	private ToolBar toolbar;
	
	/**
	 * A small JPanel at the bottom of the window informing the user of the current status.
	 */
	private StatusBar status;
	
	/**
	 * The Manager controlling this window.
	 */
	private Manager manager;
	
	/**
	 * Constructs a new window for the application and lays out the GUI within the window.
	 * @param manager
	 * 		The application Manager.
	 */
	public MainFrame(Manager manager) {
		this.manager = manager;
		
		addWindowListener(this);
		
		/* Layout the GUI */
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		/* The dummy panel holds the canvas to ensure the JScrollPane is never smaller than the space assigned to it. */
		final JPanel dummyPanel = new JPanel();
	
		
	    canvas = new Canvas(manager);
	    final JScrollPane scroll = new JScrollPane();
	   
	    
	
	    
	    dummyPanel.setBackground(Color.GRAY);
	    dummyPanel.setLayout(new DiagramLayout());
	    dummyPanel.add(canvas);
	    dummyPanel.setOpaque(true);

	    scroll.setViewportView(dummyPanel);  
	   
	    this.addComponentListener(new ComponentAdapter() {
	    	@Override
			public void componentResized(ComponentEvent e) {
	    		dummyPanel.setSize(new Dimension(scroll.getSize().width - 30, scroll.getSize().height - 30));
	    	}
	    });
	    
	    canvas.addComponentListener(new ComponentAdapter() {
	    	@Override
			public void componentResized(ComponentEvent e) {
	    		dummyPanel.setPreferredSize(canvas.getSize());
	    	}
	    });
	    
	    
	    sideBar = new SideBar(manager);
	    menu = new MenuBar(manager);
	    toolbar = new ToolBar(manager);
	    status = new StatusBar();
	    
	    add(scroll);
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

	/** 
	 * Used by the Manager to obtain the SideBar created within this MainFrame.
	 * @return
	 * 		The SideBar of this window.
	 */
	public SideBar getSideBar() {
		return sideBar;
	}

	/** 
	 * Used by the Manager to obtain the Canvas created within this MainFrame.
	 * @return
	 * 		The Canvas of this window.
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/** 
	 * Used by the Manager to obtain the ToolBar created within this MainFrame.
	 * @return
	 * 		The ToolBar of this window.
	 */
	public ToolBar getToolbar() {
		return toolbar;
	}

	/** 
	 * Used by the Manager to obtain the StatusBar created within this MainFrame.
	 * @return
	 * 		The StatusBar of this window.
	 */
	public StatusBar getStatus() {
		return status;
	}

	
	/**
	 * Whenever the window is about to be closed, calls Manager.exit() to handle the situation.
	 * 
	 * param e
	 * 		The event created as a result of the user closing the window.
	 * 
	 * @see uk.ac.aber.dcs.cs124group.controller.Manager#exit() Manager.exit()
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		manager.exit();	
	}

	/**************************************************************/
	////////////////////UNWANTED METHODS\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/**************************************************************/
	
	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
	
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

}
