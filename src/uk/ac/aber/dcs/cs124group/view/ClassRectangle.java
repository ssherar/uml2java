package uk.ac.aber.dcs.cs124group.view;

import uk.ac.aber.dcs.cs124group.controller.*;
import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import javax.swing.*;
import java.util.*;

/**
 * A view associated to the ClassModel Element.
 * It creates a new Rectangle on the canvas, and 
 * makes sure that all Attributes are positioned 
 * correctly 
 * 
 * @see ClassModel
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version v1.0.0
 */

public class ClassRectangle extends DocumentElementView {
	
	/**
	 * A constant for the background colour for the ClassRectangle when drawn.
	 */
	private static final Color RECTANGLE_BACKGROUND = new Color(255, 255, 190);

	/**
	 * The name of the class.
	 * @see LabelView
	 */
	private LabelView name;
	
	/**
	 * The data manipulation class
	 * @see ClassModel
	 */
	private ClassModel model;
	
	/**
	 * A list of all Data Fields as LabelViews
	 * @see LabelView
	 */
	private ArrayList<LabelView> dataFieldViews = new ArrayList<LabelView>();
	
	/**
	 * A list of all Data Fields as LabelViews
	 * @see LabelView
	 */
	private ArrayList<LabelView> methodViews = new ArrayList<LabelView>();

	/**
	 * Constructor: creates a new view for the chosen
	 * ClassModel and initialises all the appropriate listeners
	 * Observers and Models and makes sure if it is correctly
	 * Bootstrapped
	 * 
	 * @param m			The ClassModel to create a view for
	 * @param isNew		<code>True</code> if associating with a newly created class
	 * 					<code>False</code> otherwise.
	 */
	public ClassRectangle(ClassModel m, boolean isNew) {

		this.model = m;
		this.setLocation(m.getLocation());
		this.setPreferredSize(m.getSize());
		this.setOpaque(false);
		this.setLayout(new DiagramLayout());
		
		/*
		 * If the model is a newly created class
		 */
		if (isNew) {
			TextLabelModel nameLabel = new TextLabelModel(new Point(0, 0),
					"NewClass", true);
			name = new LabelView(nameLabel);
			name.setFont(this.getFont());

			nameLabel.addObserver(name);

			m.setNameLabel(nameLabel);
			this.add(name);
			name.enableEdit();
		} 
		/*
		 * Otherwise clean up the variables/methods
		 */
		else {
			m.cleanUp();
			
			name = new LabelView(m.getNameLabel());
			name.setFont(this.getFont());
			m.getNameLabel().addObserver(name);
			m.getNameLabel().addUndoableEditListener(
					m.getUndoableEditListener());
			this.add(name);

			for (int i = 0; m.getDataFields() != null
					&& i < m.getDataFields().size(); i++) {
				LabelView l = m.getDataFields().get(i).getView();
				m.getDataFields().get(i).addObserver(l);
				m.getDataFields().get(i).addUndoableEditListener(this.model.getUndoableEditListener());
				
				this.add(l);
				l.setFont(this.getFont());
				
				m.getDataFields().get(i).setStatic(m.getDataFields().get(i).isFlagStatic(), false);
				m.getDataFields().get(i).setFinal(m.getDataFields().get(i).isFlagFinal(), false);
				m.getDataFields().get(i).setTransient(m.getDataFields().get(i).isFlagTransient(), false);
				
				this.dataFieldViews.add(l);
				
				
			}

			for (int i = 0; m.getMethods() != null
					&& i < m.getMethods().size(); i++) {
				LabelView l = m.getMethods().get(i).getView();
				m.getMethods().get(i).addObserver(l);
				m.getMethods().get(i).addUndoableEditListener(this.model.getUndoableEditListener());
				l.setFont(this.getFont());
				this.add(l);
				
				m.getMethods().get(i).setStatic(m.getMethods().get(i).isFlagStatic(), false);
				m.getMethods().get(i).setFinal(m.getMethods().get(i).isFlagFinal(), false);
				m.getMethods().get(i).setAbstract(m.getMethods().get(i).isFlagAbstract(), false);
				
				this.methodViews.add(l);
			}

		}

		name.setAlignmentInParent(JTextField.CENTER);

		/*
		 * Add all the listeners
		 */
		ClassController listener = new ClassController(this.model);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
		this.addMouseMotionListener(listener);
		
		/*
		 * Create a popup menu for editing
		 */
		RectanglePopupMenu popupMenu = new RectanglePopupMenu(listener);
		this.setComponentPopupMenu(popupMenu);
		
		/*
		 * As we can't modify the flag now, we have to do
		 * it at the end.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repositionAttributes();
				updateModel(model, "flagChanged");
				repaint();
			}
		});
	}

	/**
	 * Adds the component from the view.
	 * If the component happened to be a TextArea, the Attributes needs
	 * to be repositioned
	 * 
	 * @see DocumentElementView#add(Component)
	 * 
	 * @param c		The Component to be added
	 * @return		The Container which the Component has been
	 * 				added to
	 */
	@Override
	public Container add(Component c) {
		super.add(c);

		if (!(c instanceof JTextArea))
			this.repositionAttributes();
		return this;
	}
	
	
	/**
	 * Removes the component from the view.
	 * If the component happened to be a TextArea, the Attributes needs
	 * to be repositioned
	 * 
	 * @see DocumentElementView#remove(Component)
	 * 
	 * @param c		The Component to be removed
	 */
	@Override
	public void remove(Component c) {
		super.remove(c);
		if(c instanceof LabelView) {
			if(!((LabelView)c).getModel().exists())
				this.repositionAttributes();
		}
	}

	/**
	 * Set the font for the Name label as well as for all
	 * the Components
	 * @see DocumentElementView#setFont(Font)
	 * 
	 * @param f		The new font to be applied
	 */
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		if (name != null)
			name.setFont(new Font(f.getName(),name.getFont().getStyle(), f.getSize()));
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setFont(f);

		}
		if (this.getComponentCount() > 0)
			this.repositionAttributes();
		this.doLayout();
	}

	/**
	 * Reposition all the Attributes to stop overlapping
	 * and making them adhere to a correct UML notation.
	 * 
	 * @see Attribute#setLocation(Point, boolean)
	 * @see #getNextDataFieldPoint(int)
	 * @see #getNextMethodPoint(int)
	 */
	private void repositionAttributes() {
		ArrayList<Attribute> dataFields = this.model.getDataFields();
		ArrayList<Attribute> methods = this.model.getMethods();

		for (int i = 0; dataFields != null && i < dataFields.size(); i++) {
			Attribute a = dataFields.get(i);
			a.setLocation(this.getNextDataFieldPoint(i), false);
		}

		for (int i = 0; methods != null && i < methods.size(); i++) {
			Attribute a = methods.get(i);
			a.setLocation(this.getNextMethodPoint(i), false);
		}

	}

	/**
	 * Called when the model requests an Attribute to be added to it.
	 * Creates a new LabelView with a default values, adds the observers, then
	 * adds the view to this component and bootstraps everything.
	 * 
	 * @param from Whether or not the requested cardinality is on the "from" end.
	 * 
	 * @see Relationship#requestCardinality(String)
	 */
	private void addAttributeToModel(AttributeType type) {
		String defaultRepresentation = (type == AttributeType.METHOD) ? "+ method() : void"
				: "- dataField : Type";
		Attribute newAttribute = new Attribute(this.getNextDataFieldPoint(-1),
				defaultRepresentation, type);
		LabelView newView = new LabelView(newAttribute);
		newAttribute.addObserver(newView);

		newView.setFont(this.getFont());
		model.addAttribute(newAttribute);

		if (type == AttributeType.DATA_FIELD) {
			this.dataFieldViews.add(newView);
		} else
			this.methodViews.add(newView);

		this.add(newView);
		newView.enableEdit();
		this.repaint();
	}

	/**
	 * Paints the ClassRectangle onto the Canvas with a predefined separator
	 * between Datafields and Methods
	 * 
	 * @param gg	The graphics object needed to paint the rectangle
	 */
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		Font f = this.getFont();
		g.setFont(f);

		int width = getPreferredSize().width;
		int height = getPreferredSize().height;

		g.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
		g.setColor(RECTANGLE_BACKGROUND);
		g.fillRoundRect(1, 1, width - 2, height - 2, 10, 10);

		int nameFieldHeight = name.getPreferredSize().height;
		g.setColor(Color.BLACK);
		g.drawLine(0, nameFieldHeight, width - 1, nameFieldHeight);

		g.drawLine(0, this.getSeparatorCoordinate(), width - 1,
				this.getSeparatorCoordinate());

	}

	/**
	 * Calculates the Y coordinate depending on if there are any Datafields are initialised.
	 * 
	 * @return	If none, it finds the size of the name label with an added constant
	 * 			If there are some, it finds out the next point on the diagram
	 */
	private int getSeparatorCoordinate() {
		int numberOfExistingDataFields = 0;
		for(int i = 0; i < this.model.getDataFields().size(); i++) {
			if(this.model.getDataFields().get(i).exists())
				numberOfExistingDataFields++;
		}
		if (numberOfExistingDataFields == 0)
			return this.name.getPreferredSize().height + 40;
		else {
			return this.getNextDataFieldPoint(this.model.getDataFields().size()).y;
		}
	}

	/**
	 * Calculates the location of the next data field label from the top of the
	 * rectangle down to the attribute specified by the argument.
	 * 
	 * @param afterDataFieldNumber
	 *            The index at which a new data label is to be inserted.
	 *            
	 * @return The Point at which it is safe to insert a new data field below
	 *         the one specified by the argument.
	 */
	public Point getNextDataFieldPoint(int afterDataFieldNumber) {
		int y = this.name.getPreferredSize().height + 5;

		for (int i = 0; i < this.dataFieldViews.size()
				&& i < afterDataFieldNumber && i >= 0; i++) {
			if(this.dataFieldViews.get(i).isVisible()) {
				int height = this.dataFieldViews.get(i).getPreferredSize().height;
				if(this.dataFieldViews.get(i).getModel().exists())
					y += (int) (height * 1.25);
			}
		}
		return new Point(4, y);
	}

	/**
	 * Calculates the location of the next method label from the top of the
	 * rectangle down to the method specified by the argument.
	 * 
	 * @param afterMethodNumber
	 *            The index at which a new method label is to be inserted.
	 *            
	 * @return The Point at which it is safe to insert a new method label below
	 *         the one specified by the argument.
	 */
	public Point getNextMethodPoint(int afterMethodNumber) {
		int y = this.getSeparatorCoordinate() + 5;

		for (int i = 0; i < this.methodViews.size() && i < afterMethodNumber
				&& i >= 0; i++) {
			if(this.methodViews.get(i).isVisible()) {
				int height = this.methodViews.get(i).getPreferredSize().height;
				if(this.methodViews.get(i).getModel().exists())
					y += (int) (height * 1.25);
			}
		}
		return new Point(4, y);
	}

	/**
	 * Update the {@link ClassModel} according to what has been called in
	 * the "arg" parameters
	 * 
	 * @param o		The new values for the {@link ClassModel}
	 * @param arg	What are we changing
	 */
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		if (o instanceof ClassModel) {
			this.updateModel((ClassModel) o, (String) arg);
		} else if (o instanceof DocumentPreferences) {
			this.updatePreferences((DocumentPreferences) o, (String) arg);
		}

	}

	/**
	 * Update the model with the new values from the {@link ClassModel}
	 * @param o			The new values for the label
	 * @param arg		The command we want to do
	 */
	private void updateModel(ClassModel o, String arg) {
		// Location Changed
		if (arg.equals("locationChanged")) {
			this.setLocation(o.getLocation());
			this.getParent().doLayout();
		} 
		//Paint State Changed
		else if (arg.equals("paintStateChanged")) {
			if (model.getPaintState().toString().contains("RESIZE"))
				this.setResizeCursor();
			else
				this.setDefaultCursor();

		} 
		// Size changed
		else if (arg.equals("sizeChanged")) {
			this.setPreferredSize(o.getSize());
			this.name.realign();
			this.getParent().doLayout();
		} 
		// Flag Changed
		else if (arg.equals("flagChanged")) {
			/*
			 * Set up the different type of font styles
			 */
			Font abstractChanged = new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize());
			
			/* Reference: http://stackoverflow.com/questions/325840/what-is-the-constant-value-of-the-underline-font-in-java */
			Map<TextAttribute, Integer> underlineFont = new HashMap<TextAttribute, Integer>();
			underlineFont.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			
			Font staticChanged = new Font(this.getFont().getName(), Font.PLAIN, this.getFont().getSize()).deriveFont(underlineFont);
			
			/*
			 * And apply these new font styles!
			 */
			if (o.isStatic()) {
				this.name.setFont(staticChanged, true);
				((RectanglePopupMenu)(this.getComponentPopupMenu())).setStatic();
				this.repaint();
				
			} else if (o.isAbstract()) {
				this.name.setFont(abstractChanged, true);
				((RectanglePopupMenu)(this.getComponentPopupMenu())).setAbstract();
				this.repaint();
				
			} else if (o.isFinal()){
				((RectanglePopupMenu)(this.getComponentPopupMenu())).setFinal();
				this.repaint();
				
			} else if (!o.isAbstract() && !o.isStatic() && !o.isFinal()){
				this.name.setFont(this.getFont(), true);
				((RectanglePopupMenu)(this.getComponentPopupMenu())).setNone();
				this.repaint();
			}

		} 
		// Name Changed
		else if (arg.equals("nameChanged")) {
			this.setName(o.getClassName());

		} 
		// New Data Field Requested
		else if (arg.equals("addDataFieldRequested")) {
			this.addAttributeToModel(AttributeType.DATA_FIELD);

		} 
		// New Method Requested
		else if (arg.equals("addMethodRequested")) {
			this.addAttributeToModel(AttributeType.METHOD);
		}

	}

	/**
	 * Update the preferences with the new values from the {@link TextLabelModel}
	 * @param o			The new values for the label
	 * @param arg		The command we want to do
	 */
	private void updatePreferences(DocumentPreferences o, String arg) {
		if (arg.equals("fontChanged")) {
			this.setFont(o.getFont());
		} else if (arg.equals("zoomLevelChanged")) {
			this.setZoomFactor(o.getZoomLevel());
		}
	}

	/**
	 * Change the cursor to show that the user is in a resizable
	 * hotspot of the ClassRectangle
	 */
	private void setResizeCursor() {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
		} catch (Exception ex) {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}

	/**
	 * Change the cursor to default
	 */
	private void setDefaultCursor() {
		this.setCursor(Cursor.getDefaultCursor());
	}

	/**
	  * A Rightclick Popup menu for the Class Rectangle for editing
	 * deleting and adding modifiers, as well as adding Attributes with ease
	 * <p>A private class only used by ClassRectangle - it doesn't
	 * need scope from any other class.
	 * @author Daniel Maly
	 * @author Samuel B Sherar
	 * @author Lee Smith
	 * @version v1.0.0
	 */
	private class RectanglePopupMenu extends JPopupMenu {
		
		/**
		 * The Action Listener for the menu
		 */
		private ClassController listener;
		
		/**
		 * The default types of modifiers of the class
		 */
		private String[] modTypes = {"Abstract", "Final", "Static", "None"};
		
		/**
		 * The submenu for the modifiers
		 */
		private JMenu addModifiers;
		
		/**
		 * Constructor: Create the menu items and make sure they are
		 * Bootstrapped
		 * @param listener		The action listener
		 */
		public RectanglePopupMenu(ClassController listener) {
			
			this.listener = listener;
			addModifiers = new JMenu("Class Modifiers");
			JMenuItem modMenu;
			
			/*
			 * Loop through the modifiers and add them to the parent
			 * menu
			 */
			ButtonGroup bg = new ButtonGroup();
			for (String s : modTypes){
				modMenu = new JRadioButtonMenuItem(s, s.equals("None"));
				modMenu.addActionListener(listener);
				bg.add(modMenu);
				addModifiers.add(modMenu);
			}
			
			/*
			 * Add the rest of the Parent menu items
			 */
			JMenuItem addRelationship = new JMenuItem("Add Relationship");
			addRelationship.addActionListener(listener);

			JMenuItem addDataField = new JMenuItem("Add Data Field");
			addDataField.addActionListener(listener);

			JMenuItem addMethod = new JMenuItem("Add Method");
			addMethod.addActionListener(listener);

			JMenuItem remove = new JMenuItem("Remove");
			remove.addActionListener(listener);
			remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

			add(addModifiers);
			add(addRelationship);
			add(addDataField);
			add(addMethod);
			add(remove);

		}
		
		/**
		 * Loops through all the modifiers in the menu and sets the Abstract option
		 * to selected
		 */
		public void setAbstract() {
			for(int i = 0; i < this.addModifiers.getItemCount(); i++) {
				if(this.addModifiers.getItem(i).getText().equals("Abstract")) {
					this.addModifiers.getItem(i).setSelected(true);
				}
			}
		}
		
		/**
		 * Loops through all the modifiers in the menu and sets the Final option
		 * to selected
		 */
		public void setFinal() {
			for(int i = 0; i < this.addModifiers.getItemCount(); i++) {
				if(this.addModifiers.getItem(i).getText().equals("Final")) {
					this.addModifiers.getItem(i).setSelected(true);
				}
			}
			
		}
		
		/**
		 * Loops through all the modifiers in the menu and sets the Static option
		 * to selected
		 */
		public void setStatic() {
			for(int i = 0; i < this.addModifiers.getItemCount(); i++) {
				if(this.addModifiers.getItem(i).getText().equals("Static")) {
					this.addModifiers.getItem(i).setSelected(true);
				}
			}
			
		}
		
		/**
		 * Loops through all the modifiers in the menu and sets the None option
		 * to selected
		 */
		public void setNone() {
			for(int i = 0; i < this.addModifiers.getItemCount(); i++) {
				if(this.addModifiers.getItem(i).getText().equals("None")) {
					this.addModifiers.getItem(i).setSelected(true);
				}
			}
		}
	}

}