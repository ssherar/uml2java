package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.undo.*;

import java.io.*;

import uk.ac.aber.dcs.cs124group.model.*;
import uk.ac.aber.dcs.cs124group.undo.ExistenceEdit;
import uk.ac.aber.dcs.cs124group.view.*;
import uk.ac.aber.dcs.cs124group.export.*;
import uk.ac.aber.dcs.cs124group.gui.Canvas;
import uk.ac.aber.dcs.cs124group.gui.*;


/**
 * This class is the global GUI listener of the application. 
 * It handles events that happen in the menu bar, side bar or tool bar.
 * When placing elements on the canvas, will also register some events from other diagram elements.
 * This class also acts as the application undo manager - it is added to each and every document element
 * as an UndoableEditListener to which undoable edits are being sent.
 * 
 * @see javax.swing.UndoManager
 * @see uk.ac.aber.dcs.cs124group.model.DocumentElementModel#addUndoableEditListener(UndoableEditListener) DocumentElementmodel.addUndoableEditListener()
 * @see uk.ac.aber.dcs.cs124group.model.DocumentElementModel#fireUndoableEvent(UndoableEdit) DocumentElementModel.fireUndoableEvent(UndoableEdit e)
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class Manager extends UndoManager implements ActionListener, ChangeListener,  MouseListener, Observer  {

	/** A convenience variable that is set to true during testing so that annoying dialogs don't appear when exiting the application */
	private boolean inDebug = false;
	
	/** The state this manager is currently in. Influences how mouse events are handled. */
	private ListeningMode mode = ListeningMode.LISTEN_TO_ALL;

	/** The name of this application. */
	public static final String PROGRAM_NAME = "UML2Java";
	
	/** The file extension used for saved files. */
	public static final String FILE_EXTENSION = "umlj";

	/** The GUI components being controlled by this manager. */
	private MainFrame window;
	private Canvas canvas;
	private StatusBar status;
	private ToolBar toolBar;

	/** The DocumentModel object currently loaded into the application. */
	private DocumentModel document;
	
	/** If false, indicates that upon exiting, a dialog asking the user if he wants to save his changes should be brought up. */
	private boolean justSaved = true;

	/** A collection holding currently selected items. At present, only one element is ever contained here. */
	private Stack<DocumentElementModel> selectionStack = new Stack<DocumentElementModel> ();

	/** Invoked as the entry point of the application, constructs a Manager object and all the GUI components in the window, including the window itself. */
	public Manager() {
		super();
		window = new MainFrame(this);
		window.setTitle(PROGRAM_NAME);

		canvas = window.getCanvas();

		status = window.getStatus();
		toolBar = window.getToolbar();

		openNewDocument();
		status.setText("Welcome!");

		changeFont();
	}

	/************************************************************************************/
	//////////////////////////////////EVENT HANDLING\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/************************************************************************************/
	
	@Override
	/** If the user is currently placing an element on the canvas, gets the appropriate point for it from the mouse event. */
	public void mousePressed(MouseEvent e) {
		if (mode == ListeningMode.PLACING_CLASS) {
			addNewClass(new Point(
					(int) ((1 / canvas.getZoomFactor()) * e.getX()),
					(int) ((1 / canvas.getZoomFactor()) * e.getY())));
		} else if (mode == ListeningMode.PLACING_TEXT) {
			addNewLabel(new Point(
					(int) ((1 / canvas.getZoomFactor()) * e.getX()),
					(int) ((1 / canvas.getZoomFactor()) * e.getY())));
		} 
	}

	@Override
	/** Handles the state changes in the zoom JSlider and the font size JSpinner. */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof JSlider) {
			changeZoom(((JSlider) e.getSource()).getValue() / 100.0);

		} else if (e.getSource() instanceof JSpinner) {
			changeFont();
		}
	}

	@Override
	/** Takes appropriate action based on the button the user clicked. */
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if (c.equals("Exit")) {
			exit();
		} else if (c.equals("New")) {
			openNewDocument();
		} else if (c.equals("About")) {
			openAboutWindow();
		} else if (c.equals("New Class")) {
			mode = ListeningMode.PLACING_CLASS;
			status.setText("Click on the canvas to place your new class");
		} else if (c.equals("New label")) {
			mode = ListeningMode.PLACING_TEXT;
			status.setText("Click on the canvas to place your label");
		} else if (c.equals("New Relationship")) {
			addNewRelationship();
		} else if (c.equals("Save")) {
			save();
		} else if (c.equals("Save as...")) {
			saveAs();
		} else if (c.equals("Open...")) {
			openExisting();
		} else if (c.equals("fonts") || c.equals("fontsize")) {
			changeFont();
		} else if (c.equals("Image")) {
			Exporter exp = new Exporter(canvas, this);
			try {
				exp.exportImage();
			} catch (IIOException e1) {
				e1.printStackTrace();
			}
		} else if (c.equals("Code") || c.equals("Export to Java...")) {
				this.export();
		} else if (c.equals("Print")){
			PrinterDriver printer = new PrinterDriver(canvas);
			try {
				printer.print();
			} catch (PrinterException e1) {

			}
		} else if (c.equals("Resize...")) {
			this.resizeCanvasDialog();
		} else if (c.equals("Fit to diagram")) {
			this.fitToDiagram();
		} else if (c.equals("Undo")) {
			try {this.undo();}
			catch(Exception ex) {}
		} else if (c.equals("Redo")) {
			try {this.redo();}
			catch(Exception ex) {}
		}

	}
	
	
	@Override
	/** 
	 *  Called from individual document elements whenever the user makes an undoable action. 
	 *  If an element has been added or removed, resets the listening mode to avoid creation of elements 
	 *  that require the presence of an element that might now be non-existent.
	 *  This method also updates the status bar based on the presentation name of the UndoableEdit.
	 */ 
	public void undoableEditHappened(UndoableEditEvent e) {
		
		/* Indicates that a change has been made to the document model. 
		 * If the user exits now, a dialog box asking him to save will appear. */
		this.justSaved = false;
		
		if (e.getEdit().getPresentationName().length() > 0) 
			status.setText(e.getEdit().getPresentationName());
		
		if(e.getEdit() instanceof ExistenceEdit) {
			this.mode = ListeningMode.LISTEN_TO_ALL;
			this.selectionStack.removeAllElements();
		}
		
		super.undoableEditHappened(e);
	}



	@Override
	/** Used for relationship creation, this method will update the selection stack based on what class
	 *  the user clicked on. Also, when a relationship is requested by the user directly on a class rectangle,
	 *  the request is received here.
	 */
	public void update(Observable o, Object s) {
		if(!(s instanceof String)) {
			throw new IllegalArgumentException("String expected");
		}
		else if(o instanceof ClassModel && s.equals("paintStateChanged") && ((ClassModel) o).getPaintState() == ElementPaintState.SELECTED) {
			if(this.mode == ListeningMode.PLACING_RELATIONSHIP) {
				
				/* Calls the second addNewRelationshipMethod with the class on the stack as its first argument and the most recently selected class as the second. */
				if(selectionStack.size() == 1 && !((ClassModel)o).equals(selectionStack.peek())) {
					this.mode = ListeningMode.LISTEN_TO_ALL;
					this.addNewRelationship((ClassModel) o, (ClassModel) (selectionStack.pop()));
				}
				
				/* Pushes the selected class onto the selection stack. */
				else if(selectionStack.size() == 0) {
					selectionStack.push((ClassModel) o);
					status.setText("Now click on the class you want the relationship to go to.");
				}
			}
		}
		else if (o instanceof ClassModel && s.equals("relationshipRequested")) {
			selectionStack.removeAllElements();
			selectionStack.push((ClassModel) o);
			this.mode = ListeningMode.PLACING_RELATIONSHIP;
			status.setText("Now click on the class you want the relationship to go to.");
		}
		
	}
	
	
	
	/*****************************************************************************************/
	////////////////////////////////////FILE MANAGEMENT\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/*****************************************************************************************/
	
	/** Creates an Exporter that will attempt to create .java class files from the current document.
	 *  This method clears the document of all non-existent elements by calling cleanUp(), therefore
	 *  the UndoableEdit stack is emptied to avoid NullPointerExceptions, effectively disabling undos. 
	 *  
	 *  @see uk.ac.aber.dcs.cs124group.export.Exporter Exporter
	 *  @see uk.ac.aber.dcs.cs124group.model.DocumentModel#cleanUp() DocumentModel.cleanUp()
	 */ 
	private void export() {
		document.cleanUp();
		this.discardAllEdits();
		Exporter exp = new Exporter(document, this);
		try {
			exp.exportCode();
			status.setText("Exported successfully!");
		} catch (IOException e1) {
			
			if(e1.getMessage() != null) {
				status.setText(e1.getMessage());
			}
			else {
				status.setText("Could not export into Java code due to an unforeseen error.");
			}
		}
	}

	/**
	 * If the document has been changed since the last save, brings up a "do you want to save?" dialog.
	 * Then, empties the UndoableEdit stack, removes all components from the canvas, discards 
	 * the current DocumentModel in favour of a new one, resets font and canvas size
	 * and notifies the user that a new document has been opened. 
	 */
	private void openNewDocument() {
		
		if(!this.justSaved) {
			Object[] options = { "Save", "Don't Save", "Cancel" };
			int n = JOptionPane
					.showOptionDialog(window,"Would you like to save your changes? Any unsaved changes will be lost.",
							"Warning!", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, options,options[2]);
		
			switch(n) {
				case JOptionPane.CANCEL_OPTION : return;
				case JOptionPane.YES_OPTION : save();
				default : break;
			}
		}
		
		
		this.discardAllEdits();
		document = new DocumentModel();
		canvas.removeAll();
		canvas.repaint();
		window.setTitle("Unsaved class diagram - " + PROGRAM_NAME);

		DocumentPreferences preferences = document.getPreferences();
		preferences.setFont(new Font(toolBar.getFontName(), Font.PLAIN, toolBar
				.getFontSize()));
		canvas.setFont(new Font(toolBar.getFontName(), Font.PLAIN, toolBar
				.getFontSize()));
		canvas.setPreferredSize(preferences.getCanvasDefaultSize());

		status.setText("Opened a brand new class diagram");

	}
	
	/**
	 * If a filename is already set for this document, calls serialise() 
	 * which will write the DocumentModel to the file.
	 * @see #serialize(String filename)
	 */
	private void save() {
		if (document.getPreferences().getFilename() == null) {
			saveAs();
		} else {
			serialize(document.getPreferences().getFilename());
		}
	}

	/**
	 * Brings up a JFileChooser that lets the user select where the file should be saved to.
	 * NOTE: Due to an internal bug in Java, the dialog window may take a few seconds to load.
	 * If a filename and directory are selected, the path is saved in DocumentPreferences and 
	 * serialize() is called.
	 * 
	 * @see #serialize(String filename)
	 */
	private void saveAs() {
		setWaitCursor(true);
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileNameExtensionFilter(PROGRAM_NAME + " diagram (*." +FILE_EXTENSION+ ")", FILE_EXTENSION));
		int retVal = fc.showSaveDialog(null);
		setWaitCursor(false);

		if (retVal == JFileChooser.APPROVE_OPTION) {
			String filePath = fc.getSelectedFile().getAbsolutePath();
			if(filePath.contains(FILE_EXTENSION)) {
				filePath = filePath.substring(0, filePath.length() - 5);
			}
			File saveFile = new File(filePath + "." + FILE_EXTENSION);
			if (saveFile.exists()) {
				int choice = JOptionPane.showConfirmDialog(window,"This file already exists. Do you want to overwrite it?",
								"Warning!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				if(choice == JOptionPane.NO_OPTION) 
					return;
			}
			try {
				if (saveFile.isFile())
					saveFile.createNewFile();
			} catch (Exception e) {

			}
			document.getPreferences().setFilename(saveFile.getAbsoluteFile().getPath());
			serialize(saveFile.getAbsoluteFile().getPath());
			

		}

	}
	
	/**
	 * Writes the current DocumentModel into the file specified  in the argument.
	 * Updates the status bar to notify the user of the result.
	 * 
	 * @param fileName
	 * 			The path to the file that the current DocumentModel should be saved to.
	 */
	private void serialize(String fileName) {
		try {
			document.getPreferences().setCanvasDefaultSize(canvas.getPreferredSize());
			
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.document);
			out.close();
			status.setText("Document saved successfully");
			window.setTitle(fileName + " - " + PROGRAM_NAME);
			this.justSaved = true;
			
		} catch (Exception e) {
			status.setText("Could not write your document into the file. Sorry!");
			e.printStackTrace();
		}
	}

	/**
	 * Brings up a JFileChooser letting the user select a previously saved file to open.
	 * NOTE: Due to an internal bug in java, this dialog may take a few seconds to appear.
	 * Once a file is selected, resets the UndoableEdit stack, empties the canvas and replaces
	 * the current DocumentModel with one loaded from the file. Calls cleanUp() on the model
	 * to get rid of removed elements. Then, reassigns views to DocumentElementModels, adds them to 
	 * the canvas and links all the appropriate Observable - Observer pairs. Last, DocumentPreferences
	 * are loaded and set up.
	 */
	private void openExisting() {
		setWaitCursor(true);
		JFileChooser fc = new JFileChooser();

		fc.addChoosableFileFilter(new FileNameExtensionFilter(PROGRAM_NAME + " diagram (*." +FILE_EXTENSION+ ")", FILE_EXTENSION));
		fc.setAcceptAllFileFilterUsed(false);
		
		int retVal = fc.showOpenDialog(null);
		setWaitCursor(false);

		if (retVal == JFileChooser.APPROVE_OPTION) {
			File openFile = fc.getSelectedFile();
			try {
				document = null;
				this.discardAllEdits();
				canvas.removeAll();
				canvas.repaint();
				FileInputStream fos = new FileInputStream(openFile.getPath());
				ObjectInputStream in = new ObjectInputStream(fos);
				document = (DocumentModel) in.readObject();
				document.cleanUp();
				
				for (int i = 0; i < document.getElements().size(); i++) {
					DocumentElementModel e = document.getElements().get(i);
					e.addUndoableEditListener(this);
					DocumentElementView ew = e.getView();
					ew.setFont(document.getPreferences().getFont());
					e.addObserver(ew);			
					e.addObserver(this);
					document.getPreferences().addObserver(ew);
					canvas.add(ew);
				}
				
				canvas.setPreferredSize(document.getPreferences()
						.getCanvasDefaultSize());
				canvas.setFont(document.getPreferences().getFont());
				toolBar.overrideFont(document.getPreferences().getFont());
			
				canvas.setZoomFactor(document.getPreferences().getZoomLevel()); 
				
				status.setText("File " + openFile + " opened successfully");
				window.setTitle(openFile + " - " + PROGRAM_NAME);
			}
			catch (Exception e) {
				status.setText("Could not open file " + openFile);
			}

		}
	}
	
	/*******************************************************************************************/
	///////////////////////////////DOCUMENT MANIPULATION\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/*******************************************************************************************/

	/**
	 * Adds a new ClassModel to the document and sets all the appropriate new dependencies,
	 * including creating a view and adding it to the canvas.
	 * 
	 * @param p
	 * 		The location of the new ClassModel on the diagram.
	 */
	private void addNewClass(Point p) {
		mode = ListeningMode.LISTEN_TO_ALL;
		ClassModel c = new ClassModel(p);
		document.addElement(c);
		
		ExistenceEdit edit = new ExistenceEdit(c, true, "New class created");
		this.undoableEditHappened(new UndoableEditEvent(c, edit));

		ClassRectangle view = new ClassRectangle(c, true);
		c.addObserver(view);
		c.addObserver(this);
		c.addUndoableEditListener(this);

		document.getPreferences().addObserver(view);
		canvas.add(view);
		view.setFont(document.getPreferences().getFont());
		view.repaint();

		canvas.repaint();
	}

	/**
	 * Adds a new TextLabelModel to the document and sets all the appropriate new dependencies,
	 * including creating a view and adding it to the canvas.
	 * 
	 * @param p
	 * 		The location of the new text label on the diagram.
	 */
	private void addNewLabel(Point p) {
		mode = ListeningMode.LISTEN_TO_ALL;
		TextLabelModel mod = new TextLabelModel(p);
		
		ExistenceEdit edit = new ExistenceEdit(mod, true, "New label created");
		this.undoableEditHappened(new UndoableEditEvent(mod, edit));
		
		LabelView view = new LabelView(mod);
		view.setFont(document.getPreferences().getFont());

		mod.addObserver(view);
		mod.addUndoableEditListener(this);
		document.getPreferences().addObserver(view);
		
		document.addElement(mod);

		canvas.add(view);
		view.enableEdit();

		canvas.repaint();
	}
	
	/**
	 * Called before any classes are selected for a new relationship, simply
	 * empties the selection stack and alerts this listener to receive selection
	 * events from ClassModels. Also updates the status bar to guide the user.
	 */
	public void addNewRelationship() {
		selectionStack.removeAllElements();
		status.setText("Click on the class you want the relationship to go from");
		this.mode = ListeningMode.PLACING_RELATIONSHIP;
	}
	
	/**
	 * Adds a new Relationship between the two specified classes to the document
	 * and sets all appropriate new dependencies, including creating a view and 
	 * adding it to the canvas.
	 * 
	 * @param to
	 * 		The ClassModel the new Relationship is to go to.
	 * @param from
	 * 		The ClassModel the new Relationship is to go from.
	 */
	private void addNewRelationship(ClassModel to, ClassModel from) {
		
		Relationship r = new Relationship(from, to);
		
		from.addRelationship(r);
		from.addObserver(r);
		to.addRelationship(r);
		to.addObserver(r);
			
		ExistenceEdit edit = new ExistenceEdit(r, true, "New relationship created");
		this.undoableEditHappened(new UndoableEditEvent(r, edit));
		
		RelationshipArrow arrow = new RelationshipArrow(r);
		r.addObserver(arrow);
		document.getPreferences().addObserver(arrow);
		r.addUndoableEditListener(this);
		document.addElement(r);
		
		canvas.add(arrow);
		canvas.repaint();
	}
	

	/**
	 * Changes the font in the DocumentPreferences of the current DocumentModel.
	 * The information about the font to be set is taken from the toolbar.
	 * Observers of DocumentPreferences must set their own font accordingly.
	 */ 
	private void changeFont() {
		Font font = new Font(toolBar.getFontName(), Font.PLAIN,
				toolBar.getFontSize());
		document.getPreferences().setFont(font);

		canvas.setFont(font);
		canvas.repaint();
		status.setText("Font changed to " + font);
	}

	/**
	 * Changes the zoom factor in the DocumentPreferences of the current DocumentModel. 
	 * Observers of DocumentPreferences must set their own zoom accordingly. 
	 * NOTE: The zoom feature is currently disabled due to not being fully implemented. Future releases may enable it.
	 * @param zoom
	 * 			The zoom factor to be set.
	 */
	private void changeZoom(double zoom) {
		canvas.setZoomFactor(zoom);
		status.setText("Zoom factor is " + canvas.getZoomFactor());
		canvas.repaint();

		document.getPreferences().setZoomLevel(zoom);
	}
	
	/** Brings up a small dialog allowing the user to resize the canvas.
	 *  @see uk.ac.aber.dcs.cs124group.gui.ResizeDialog ResizeDialog
	 */
	private void resizeCanvasDialog() {
		JFrame resizeDialog = new ResizeDialog(this.canvas);
		resizeDialog.setLocationRelativeTo(window);
		resizeDialog.setVisible(true);
		
		
	}
	
	/**
	 * Resizes the canvas so that it's size is the least possible height and width while still fully 
	 * containing all elements added to it. 
	 * @see uk.ac.aber.dcs.cs124group.view.DiagramLayout DiagramLayout
	 */
	private void fitToDiagram() {
		int maxX = 300;
		int maxY = 300;
		Component[] components = canvas.getComponents();
		for(int i = 0; i < components.length; i++) {
			if(components[i].getBounds().getMaxX() > maxX)
				maxX = (int) (components[i].getBounds().getMaxX());
			if(components[i].getBounds().getMaxY() > maxY)
				maxY = (int) (components[i].getBounds().getMaxY());
		}
		
		canvas.setPreferredSize(new Dimension(maxX, maxY));
		canvas.getParent().doLayout();

		
	}
	
	
	/**********************************************************************/
	//////////////////////////MISCELLANEOUS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/**********************************************************************/
	
	/** Opens up a window with information about the program itself. */
	private void openAboutWindow() {
		JFrame aboutWindow = new JFrame("About " + PROGRAM_NAME);
		aboutWindow.setSize(450, 250);
		aboutWindow.setMaximumSize(new Dimension(450, 250));
		aboutWindow.setLocationRelativeTo(window);
		aboutWindow.setVisible(true);

		aboutWindow.add(new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));
				g.drawString(
						"A simple, easy-to-use Java design tool",
						40, 85);
				
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				g.drawString(
						"Developped by Daniel Maly, Samuel B Sherar and Lee Smith.",
						50, 150);
				g.drawString(
						"Version: 1.0.0.  (CS12420 group project release)",
						50, 175);
				g.setFont(new Font("Arial Black", Font.PLAIN, 50));
				g.drawString("UML2JAVA", 55, 50);
			}
		});

		aboutWindow.setSize(450, 250);
	}

	/**
	 * Attempts to set the system default wait cursor on the main window, indicating that processing is taking place. 
	 * @param value
	 * 		Whether the wait cursor is to be set or unset.
	 */ 
	public void setWaitCursor(boolean value) {
		if (value) {
			try {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			} catch (Exception e) {
				window.setCursor(Cursor.getDefaultCursor());
			}
		} else
			window.setCursor(Cursor.getDefaultCursor());
	}
	

	/** Exits the program. If any undoable events are present, brings up a dialog asking the user to save their work. */
	public void exit() {

		if (!justSaved && !this.inDebug) {
			Object[] options = { "Save", "Don't Save", "Cancel" };
			int n = JOptionPane
					.showOptionDialog(window,"Would you like to save your changes? Any unsaved changes will be lost.",
							"Warning!", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, options,options[2]);
			if (n != 2 || n != JOptionPane.CANCEL_OPTION) {
				if (n == 0) {
					save();
					System.exit(0);
				} else if (n == 1) {
					System.exit(0);
				}
			}
		} else {
			System.exit(0);
		}
	}

	/***************************************************************************************/
	//////////////////////////////UNWANTED METHODS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/***************************************************************************************/
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
