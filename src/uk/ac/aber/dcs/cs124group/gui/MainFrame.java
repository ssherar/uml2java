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
	    toolbar = new ToolBar();
	    status = new StatusBar();
	    
	    JPanel contentPane = new JPanel(new GridBagLayout());
	    //contentPane.setPreferredSize(new Dimension(624,768));
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 1.0;
	    contentPane.add(toolbar, c);
	    
	    c.fill = GridBagConstraints.BOTH;
	    c.gridheight = GridBagConstraints.REMAINDER;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.weightx = c.weighty = 1.0;
	    contentPane.add(scroll,c);
	    
	    
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    add(sideBar, BorderLayout.WEST);
		add(contentPane, BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);
		
		setJMenuBar(menu);
		
	    setSize(1024,768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		this.setMinimumSize(new Dimension(1024,768));
	    
	}

	@Override
	public void windowClosing(WindowEvent e) {
		manager.exit();
		
	}

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
