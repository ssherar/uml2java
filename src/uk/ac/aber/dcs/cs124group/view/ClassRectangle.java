package uk.ac.aber.dcs.cs124group.view;

import uk.ac.aber.dcs.cs124group.controller.*;
import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.Attribute;
import uk.ac.aber.dcs.cs124group.model.AttributeType;
import uk.ac.aber.dcs.cs124group.model.ClassModel;
import uk.ac.aber.dcs.cs124group.model.DocumentPreferences;
import uk.ac.aber.dcs.cs124group.model.TextLabelModel;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Observable;


public class ClassRectangle extends DocumentElementView {

	private static final Dimension DEFAULT_RECTANGLE_SIZE = new Dimension(350,225);
	private static final Color RECTANGLE_BACKGROUND = new Color(255,255,190);


	private LabelView name;
	private ClassModel model;
	
	private ArrayList<LabelView> dataFieldViews = new ArrayList<LabelView>();
	private ArrayList<LabelView> methodViews = new ArrayList<LabelView>();


	public ClassRectangle(ClassModel model) {
		this.model = model;
		
		this.setLocation(model.getLocation());
		this.setPreferredSize(model.getSize());
		this.setOpaque(false);
		this.setLayout(new DiagramLayout());


		TextLabelModel nameLabel = new TextLabelModel(new Point(0,0), "NewClass");
		name = new LabelView(nameLabel);
		
		nameLabel.addObserver(name);
		model.setNameLabel(nameLabel);
		this.add(name);
		name.enableEdit();



		name.setAlignmentInParent(JTextField.CENTER); //TODO: fix name label positioning 


		ClassController listener = new ClassController(this.model);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
		this.addMouseMotionListener(listener);
		this.name.addMouseListener(listener);

		RectanglePopupMenu popupMenu = new RectanglePopupMenu(listener);
		this.setComponentPopupMenu(popupMenu);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repaint();
			}
		});	
	}



	@Override
	public Container add(Component c) {
		super.add(c);

		if(!(c instanceof JTextArea))this.repositionAttributes();
		return this;
	}


	//@Override
	/*public void setFont(Font f) {
		super.setFont(f);
		if(name != null) name.setFont(f);
		for(int i = 0; this.model.getAttributes() != null && i < this.model.getAttributes().size(); i++) {
			this.model.getAttributes().get(i).setFont(f);

		}
		this.repositionAttributes();
		this.doLayout();
	}*/

	private void repositionAttributes() {
		this.cleanUp();
		ArrayList<Attribute> dataFields = this.model.getDataFields();
		ArrayList<Attribute> methods = this.model.getMethods();
		
		for(int i = 0; dataFields != null && i < dataFields.size(); i++) {
			Attribute a = dataFields.get(i);
			a.setLocation(this.getNextDataFieldPoint(i));
		}

		for(int i = 0; methods != null && i < methods.size(); i++) {
			Attribute a = methods.get(i);
			a.setLocation(this.getNextMethodPoint(i));
		}

	}
	
	private void addAttributeToModel(AttributeType type) {
		String defaultRepresentation = type == AttributeType.METHOD? "+ method() : void" : "- dataField : Type";
		Attribute newAttribute = new Attribute(
				this.getNextDataFieldPoint(-1), 
				defaultRepresentation, type);
		LabelView newView = new LabelView(newAttribute);
		newAttribute.addObserver(newView);
		model.addAttribute(newAttribute);
		
		if(type == AttributeType.DATA_FIELD) {
			this.dataFieldViews.add(newView);
		}
		else this.methodViews.add(newView);
		
		this.add(newView);
		newView.enableEdit();
		this.repaint();
	}
	
	private void cleanUp() {
		this.model.cleanUp();
		for(int i = 0; i < dataFieldViews.size(); i++) {
			if(!dataFieldViews.get(i).isVisible())
				dataFieldViews.remove(i);
		}
		for(int i = 0; i < methodViews.size(); i++) {
			if(!methodViews.get(i).isVisible())
				methodViews.remove(i);
		}
		
	}
	


	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		Font f = this.getFont();
		g.setFont(f);

		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		FontMetrics metrics = g.getFontMetrics();

		int width = getPreferredSize().width;
		int height = getPreferredSize().height;

		g.drawRoundRect(0, 0, width - 1 , height - 1, 10, 10);
		g.setColor(RECTANGLE_BACKGROUND);
		g.fillRoundRect(1, 1, width - 2, height - 2, 10, 10);

		int nameFieldHeight = name.getPreferredSize().height;
		g.setColor(Color.BLACK);
		g.drawLine(0, nameFieldHeight, width - 1, nameFieldHeight);

		g.drawLine(0, this.getSeparatorCoordinate(), width - 1, this.getSeparatorCoordinate());

	}

	private int getSeparatorCoordinate() {
		if(this.model.getDataFields().size() == 0)
			return this.getPreferredSize().height / 2;
		else {
			int y = this.getNextDataFieldPoint(this.model.getDataFields().size()).y;
			return (int) (y / (double)(this.model.getDataFields().size()) * (this.model.getDataFields().size() + 1)) + 3;
		}
	}
	
	/** Calculates the location of the next data field label from the top of the rectangle down to the attribute specified by the argument.
	 * 
	 * @param afterDataFieldNumber	The index at which a new data label is to be inserted minus one. 
	 * @return 						The Point at which it is safe to insert a new data field below the one specified by the argument.
	 */
	public Point getNextDataFieldPoint(int afterDataFieldNumber) {
		int y = this.name.getPreferredSize().height + 5;

		for(int i = 0; i < afterDataFieldNumber && i >= 0 ;i++) {
			int height = this.dataFieldViews.get(i).getPreferredSize().height;
			y += (int) (height * 1.25);	
		}
		return new Point(4,y);
	}

	/** Calculates the location of the next method label from the top of the rectangle down to the method specified by the argument.
	 * 
	 * @param afterMethodNumber		The index at which a new method label is to be inserted minus one. 
	 * @return 						The Point at which it is safe to insert a new method label below the one specified by the argument.
	 */
	public Point getNextMethodPoint(int afterMethodNumber) {
		int y = this.getSeparatorCoordinate() + 5;

		for(int i = 0; i < afterMethodNumber && i >= 0 ;i++) {
			int height = this.methodViews.get(i).getPreferredSize().height;
			y += (int) (height * 1.25);	
		}
		return new Point(4,y);
	}

	@Override
	public void update(Observable o, Object arg) {
		
		if(!(arg instanceof String)) {
			throw new IllegalArgumentException("Invalid argument: Need a string");
		}
		
		if(o instanceof ClassModel) {
			this.updateModel((ClassModel)o, (String) arg);
		} else if(o instanceof DocumentPreferences) {
			this.updatePreferences((DocumentPreferences)o, (String) arg);
		}
		
	}
	
	private void updateModel(ClassModel o, String arg) {
		if(arg.equals("locationChanged")) {
			this.setLocation(o.getLocation());
			this.getParent().doLayout();
			
		} else if(arg.equals("sizeChanged")) {
			this.setPreferredSize(o.getSize());
			
		} else if(arg.equals("attributeChanged")) {
			
		} else if(arg.equals("flagChanged")) {
			if(o.isStatic()) {
				//TODO: set underlined font
			} else if(o.isAbstract()) {
				//TODO: set italic font
			}
			
		} else if(arg.equals("nameChanged")) {
			this.setName(o.getClassName());
			
		} else if(arg.equals("addDataFieldRequested")) {
			this.addAttributeToModel(AttributeType.DATA_FIELD);
			
		} else if(arg.equals("addMethodRequested")) {
			this.addAttributeToModel(AttributeType.METHOD);
		
		} else if(arg.equals("wasRemoved")) {
			
			this.setVisible(false);
		}
		
	}
	
	private void updatePreferences(DocumentPreferences o, String arg) {
		//setFont
		if(arg.equals("fontChanged")) {
			this.setFont(o.getFont());
		} else if(arg.equals("zoomLevelChanged")) {
			this.setZoomFactor(o.getZoomLevel());
		}
		//setZoomLevel
	}
	
	private class RectanglePopupMenu extends JPopupMenu {

		private ClassController listener;

		public RectanglePopupMenu(ClassController listener) {

			this.listener = listener;

			JMenuItem addRelationship = new JMenuItem("Add relationship");
			addRelationship.addActionListener(listener);

			JMenuItem addDataField = new JMenuItem("Add data field");
			addDataField.addActionListener(listener);

			JMenuItem addMethod = new JMenuItem("Add method");
			addMethod.addActionListener(listener);

			JMenuItem remove = new JMenuItem("Remove");
			remove.addActionListener(listener);
			remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));


			add(addRelationship);
			add(addDataField);
			add(addMethod);
			add(remove);


		}
	}

}

