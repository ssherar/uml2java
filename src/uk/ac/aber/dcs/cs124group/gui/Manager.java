package uk.ac.aber.dcs.cs124group.gui;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class Manager implements ActionListener, ItemListener, KeyListener,
		MouseMotionListener, MouseListener {
	
	public static final String PROGRAM_NAME = "UML2Java";
	
	private MainFrame window;
	private Canvas canvas;
	private MenuBar menuBar;
	private SideBar sideBar;
	private StatusBar status;
	private ToolBar toolBar;
	
	private DocumentModel document;
	
	public Manager() {
		window = new MainFrame(this);
		window.setTitle(PROGRAM_NAME);
		
		canvas = window.getCanvas();
		menuBar = window.getMenu();
		sideBar = window.getSideBar();
		status = window.getStatus();
		toolBar = window.getToolbar();
	}
	
	public ArrayList<DocumentElement> getDrawableElements() {
		return document.getElements();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if(c.equals("Exit")) {
			exit();
		}
		else if(c.equals("New")) {
			openNewDocument();
		}
		else if(c.equals("About")) {
			openAboutWindow();
		}
	}
	
	private void openNewDocument() {
		document = new DocumentModel();
		window.setTitle("Unsaved class diagram - " + PROGRAM_NAME);
		
		DocumentPreferences preferences = document.getPreferences();
		preferences.setFont(new Font(toolBar.getFontName(), Font.PLAIN, 0/*TODO: get from toolbar*/));
		preferences.setCanvasDefaultSize(canvas.getSize());
		
		status.setText("Opened a brand new class diagram");
		
	}
	
	private void openAboutWindow() {
		JFrame aboutWindow = new JFrame("About " + PROGRAM_NAME);
		aboutWindow.setSize(450,250);
		aboutWindow.setMaximumSize(new Dimension(450,250));
		aboutWindow.setLocationRelativeTo(window);
		aboutWindow.setVisible(true);
		
		aboutWindow.add(new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawString("This program is being developed by Sam Sherar, Daniel Maly and Lee Smith.", 50, 150);
				g.drawString("You are advised to stay well away from it until it is finished.", 50, 165);
				g.setFont(new Font("Arial Black", Font.PLAIN, 50));
				g.drawString("UML2JAVA", 50,50);
			}
		});
		
		aboutWindow.setSize(450,250);
	}
	
	public void exit() {
		//TODO check for unsaved changes, display "do you want to save" if necessary
		System.exit(0);
	}

}
