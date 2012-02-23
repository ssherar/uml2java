package uk.ac.aber.dcs.cs124group.gui;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.*;

import uk.ac.aber.dcs.cs124group.model.*;
import uk.ac.aber.dcs.cs124group.export.*;

public class Manager implements ActionListener, ItemListener, KeyListener,
		MouseMotionListener, MouseListener, ChangeListener {
	
	private boolean inDebug = true;
	
	public static final String PROGRAM_NAME = "UML2Java";
	
	private MainFrame window;
	private Canvas canvas;
	private MenuBar menuBar;
	private SideBar sideBar;
	private StatusBar status;
	private ToolBar toolBar;
	
	private DocumentModel document;
	
	private ListeningMode mode = ListeningMode.LISTEN_TO_ALL;
	
	private boolean edited = false;
	private boolean editingLabel = false;
	private TextLabel currentEdited;
	//private JTextArea labelTextArea;
	
	public Manager() {
		window = new MainFrame(this);
		window.setTitle(PROGRAM_NAME);
		
		canvas = window.getCanvas();
		menuBar = window.getMenu();
		sideBar = window.getSideBar();
		status = window.getStatus();
		toolBar = window.getToolbar();
		
		openNewDocument();
		canvas.setPreferredSize(new Dimension(500,500));
		status.setText("Welcome!");
		
		changeFont();
	}
	
	public ArrayList<DocumentElement> getDrawableElements() {
		return document.getElements();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getComponent().getName() == "CanvasLabel") {
			if(e.getClickCount() == 2 && !e.isConsumed() && mode == ListeningMode.LISTEN_TO_ALL) {
				e.consume();
				TextLabel l = (TextLabel)(e.getComponent());
				if(l != null) {
					mode = ListeningMode.EDITING_TEXT;
					enableLabelEdit(l);
				}
				
			}
		}
	}
	
	private void enableLabelEdit(TextLabel label) {
		status.setText("Editing a label! When finished, please press ENTER to commit.");
		// Move the element to be edited
		// Clean up on Aisle #3
		currentEdited = null;
		currentEdited = label;
		canvas.remove(currentEdited);
			
		final JTextArea labelTextArea = new JTextArea();
		
		int x = label.getLocation().x;
		int y = label.getLocation().y;
		
		int canvasWidth = canvas.getPreferredSize().width;
		int canvasHeight = canvas.getPreferredSize().height;
		
		labelTextArea.setBounds(x, y, canvasWidth - x, canvasHeight - y);
		
		labelTextArea.setName("EditingCanvasLabel");
		labelTextArea.setOpaque(false);
		

		labelTextArea.setSize(labelTextArea.getWidth()+30, labelTextArea.getHeight()+5);
		labelTextArea.setFont(document.getPreferences().getFont());
		labelTextArea.setText(currentEdited.getText());
		labelTextArea.addKeyListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTextArea.requestFocus();
				labelTextArea.selectAll();
			}
			
		});
		canvas.add(labelTextArea);
		canvas.repaint();
		
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
		if(mode == ListeningMode.PLACING_CLASS) {
			addNewClass(new Point((int)((1/canvas.getZoomFactor())*e.getX()), (int)((1/canvas.getZoomFactor())*e.getY())));
		}
		else if (mode == ListeningMode.PLACING_TEXT) {
			addNewLabel(new Point((int)((1/canvas.getZoomFactor())*e.getX()), (int)((1/canvas.getZoomFactor())*e.getY())));
		} else {
			//Editing text...
			
		}

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
		if(e.getComponent().getName() == "EditingCanvasLabel" && e.getKeyCode() == e.VK_ENTER) {
			JTextArea tmp = (JTextArea) e.getComponent();
			if(tmp.getText().length() < 1) {
				status.setText("A label cannot be set to nothing. To delete please Right Click and click Delete.");
				e.consume();
				return;
			}
			canvas.remove(tmp);
			currentEdited.setText(tmp.getText());
			canvas.add(currentEdited);
			canvas.repaint();
			mode = ListeningMode.LISTEN_TO_ALL;
			status.setText("Label modified successfully!");
		}
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
		//TODO implement

	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() instanceof JSlider) {
			changeZoom(((JSlider)e.getSource()).getValue() / 100.0);
			
		}
		else if (e.getSource() instanceof JSpinner) {
			changeFont();
		}
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
		else if(c.equals("New Class")) {
			mode = ListeningMode.PLACING_CLASS;
			status.setText("Click on the canvas to place your new class");
		}
		else if(c.equals("New label")) {
			mode = ListeningMode.PLACING_TEXT;
			status.setText("Click on the canvas to place your label");
		}
		else if(c.equals("Save")) {
			save();
		}
		else if(c.equals("Save as...")) {
			saveAs();
		}
		else if(c.equals("Open...")) {
			openExisting();
		}
		else if(c.equals("fonts") || c.equals("fontsize")) {
			changeFont();
		}
		else if(c.equals("Image")){
			exportImage();
		}
		
	}
	
	public void exportImage(){
		Exporter exp = new Exporter(canvas);
		try {
			exp.exportImage();
		} catch (IIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void openNewDocument() {
		document = new DocumentModel();
		canvas.removeAll();
		window.setTitle("Unsaved class diagram - " + PROGRAM_NAME);
		
		DocumentPreferences preferences = document.getPreferences();
		preferences.setFont(new Font(toolBar.getFontName(), Font.PLAIN, toolBar.getFontSize()));
		canvas.setFont(new Font(toolBar.getFontName(), Font.PLAIN, toolBar.getFontSize()));
		preferences.setCanvasDefaultSize(canvas.getSize());
		
		status.setText("Opened a brand new class diagram");
		
	}
	
	private void addNewClass(Point p) {
		mode = ListeningMode.LISTEN_TO_ALL;
		ClassRectangle c = new ClassRectangle(p);
		c.setFont(document.getPreferences().getFont());
		document.addElement(c);
		status.setText("New class rectangle created at " + p.x + "," + p.y);
		
		canvas.add(c);
		c.repaint();
		
		canvas.repaint();
		this.edited = true;
	}
	
	private void addNewLabel(Point p) {
		mode = ListeningMode.LISTEN_TO_ALL;
		TextLabel l = new TextLabel(p);
		l.setFont(document.getPreferences().getFont());
		l.setName("CanvasLabel");
		l.addMouseListener(this);
		document.addElement(l);
		status.setText("New label created at " + p.x + "," + p.y);
		
		canvas.add(l);
		l.repaint();
		
		canvas.repaint();
		this.edited = true;
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
	
	private void save() {
		if(document.getFileName() == null) {
			saveAs();
		} else {
			serialise(document.getFileName());
		}
	}
	
	private void saveAs() {
		setWaitCursor(true);
		JFileChooser fc = new JFileChooser();
		int retVal = fc.showSaveDialog(null);
		setWaitCursor(false);
		
		if(retVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = fc.getSelectedFile();
			try {
				if(saveFile.isFile()) saveFile.createNewFile();
			} catch (Exception e) {
				
			}
			serialise(saveFile.getAbsoluteFile().getPath());
			document.setFileName(saveFile.getAbsoluteFile().getPath());
			
		}
		
	}
	
	private void serialise(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.document);
			out.close();
			status.setText("Document saved successfully");
		} catch(Exception e) {
			status.setText("Could not write your document into the file. Sorry!");
			System.out.println(e.getStackTrace());
		}
	}
	
	private void openExisting() {
		setWaitCursor(true);
		JFileChooser fc = new JFileChooser();
		int retVal = fc.showOpenDialog(null);
		setWaitCursor(false);
		
		if(retVal == JFileChooser.APPROVE_OPTION) {
			File openFile = fc.getSelectedFile();
			try {
				document = null;
				FileInputStream fos = new FileInputStream(openFile.getPath());
				ObjectInputStream in = new ObjectInputStream(fos);
				document = (DocumentModel)in.readObject();
				for(int i = 0; i < getDrawableElements().size(); i++) {
					DocumentElement e = getDrawableElements().get(i);
					e.setPaintState(ElementPaintState.DEFAULT);
					canvas.add(e);
				}
				canvas.setPreferredSize(document.getPreferences().getCanvasDefaultSize());
				canvas.setFont(document.getPreferences().getFont());
				toolBar.overrideFont(document.getPreferences().getFont());
				canvas.setZoomFactor(document.getPreferences().getZoomLevel()); //TODO: fixme, JSlider will not have updated
				canvas.repaint();
				status.setText("File " + openFile + " opened successfully");
				window.setTitle(openFile + " - " + PROGRAM_NAME);
			} catch(Exception e) {
				status.setText("Could not open file " + openFile);
			}
			
		}
	}
	
	private void changeFont() {
		setWaitCursor(true);
		Font font = new Font(toolBar.getFontName(), Font.PLAIN, toolBar.getFontSize());
		document.getPreferences().setFont(font);
		
		ArrayList<DocumentElement> elements = document.getElements();
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).setFont(font);
			elements.get(i).repaint();
		}
		
		canvas.setFont(font);
		canvas.repaint();
		status.setText("Font changed to " + font);
		setWaitCursor(false);
	}
	
	private void changeZoom (double zoom) {
		ArrayList<DocumentElement> elements = document.getElements();
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).setZoomFactor(zoom);
			elements.get(i).repaint();
		}
		canvas.setZoomFactor(zoom);
		status.setText("Zoom factor is " + canvas.getZoomFactor());
		canvas.repaint();
		
		document.getPreferences().setZoomLevel(zoom);
	}
	
	private void setWaitCursor(boolean value) {
		if(value) {
			try {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
			catch (Exception e) {
				window.setCursor(Cursor.getDefaultCursor());
			}
		}
		else window.setCursor(Cursor.getDefaultCursor());
	}
	
	public boolean isEditingLabel() {
		return this.editingLabel;
	}
	
	public void exit() {
		
		if(this.edited && !this.inDebug) {
			Object[] options = {"Save", "Don't Save", "Cancel"};
			int n = JOptionPane.showOptionDialog(window, "Would you like to save your changes? Any unsaved changes will be lost.", "Warning!", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[2]);
			if(n != 2 || n != JOptionPane.CANCEL_OPTION) {
				if(n == 0) {
					save();
					System.exit(0);
				} else if(n == 1) {
					System.exit(0);
				}
			}
		} else {
			System.exit(0);
		}
	}

}
