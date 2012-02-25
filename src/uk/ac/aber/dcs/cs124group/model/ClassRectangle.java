package uk.ac.aber.dcs.cs124group.model;

import uk.ac.aber.dcs.cs124group.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.util.ArrayList;


public class ClassRectangle extends DocumentElement {

	private static final long serialVersionUID = 249568855144662119L;
	private static final Dimension DEFAULT_RECTANGLE_SIZE = new Dimension(350,225);
	private static final Color RECTANGLE_BACKGROUND = new Color(255,255,190);


	private TextLabel name;
	private Dimension size;
	private ClassRectangle superClass = null;
	private IVisibility visibility = IVisibility.PUBLIC;

	private ArrayList<Relationship> relationships = new ArrayList<Relationship> ();
	private ArrayList<Attribute> dataFields = new ArrayList<Attribute>();
	private ArrayList<Attribute> methods = new ArrayList<Attribute>();

	//private ArrayList<Attribute> attributes = new ArrayList<Attribute> ();

	private boolean isAbstract = false;
	private boolean isFinal = false;
	private boolean isStatic = false;

	public ClassRectangle(Point p) {
		this.setLocation(p);
		this.setPreferredSize(DEFAULT_RECTANGLE_SIZE);
		this.setOpaque(false);
		this.setLayout(new DiagramLayout());


		name = new TextLabel(new Point(0,0)); //TODO: Fixme
		this.add(name);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				name.setLocation(new Point((getPreferredSize().width - name.getPreferredSize().width) / 2, 0));
			}
		});
		name.setAlignmentInParent(JTextField.CENTER);


		RectangleListener listener = new RectangleListener(this);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
		this.addMouseMotionListener(listener);
		this.name.addMouseListener(listener);

		RectanglePopupMenu popupMenu = new RectanglePopupMenu(listener);
		this.setComponentPopupMenu(popupMenu);
	}

	public String getClassName() {
		return this.name.getText();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		if(dataFields == null && methods == null)
			return null;
		else if(dataFields == null)
			return methods;
		else if(methods == null)
			return dataFields;
		else {
			attributes.addAll(dataFields);
			attributes.addAll(methods);
			return attributes;
		}

	}

	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}

	public void addRelationship(Relationship r) {
		relationships.add(r);
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isStatic(){
		return isStatic;
	}

	public void setStatic(boolean isStatic){
		this.isStatic = isStatic;
	}

	@Override
	public void move(Point newPos) {
		setLocation(newPos);
		//TODO act upon this new information accordingly...
	}

	public Dimension getSize() {
		return this.getPreferredSize();
	}

	public void setSuperClass(ClassRectangle c) {
		this.superClass = c;
	}

	public ClassRectangle getSuperClass() {
		return this.superClass;
	}

	public void setVisibility(IVisibility visibility) {
		this.visibility = visibility;
	}

	public IVisibility getVisibility() {
		return visibility;
	}


	@Override
	public Container add(Component c) {
		super.add(c);
		if(c instanceof Attribute) {
			if(((Attribute) c).getType() == AttributeType.DATA_FIELD)
				this.dataFields.add((Attribute) c);
			else this.methods.add((Attribute)c);
		}
		this.repositionAttributes();
		return this;
	}

	@Override
	public void remove(Component c) {
		super.remove(c);
		if(c instanceof Attribute) {
			if(((Attribute) c).getType() == AttributeType.DATA_FIELD)
				this.dataFields.remove((Attribute) c);
			else this.methods.remove((Attribute)c);
		}	
		this.repositionAttributes();
	}

	@Override
	public void setFont(Font f) {
		super.setFont(f);
		if(name != null) name.setFont(f);
		for(int i = 0; this.getAttributes() != null && i < this.getAttributes().size(); i++) {
			this.getAttributes().get(i).setFont(f);

		}
		this.repositionAttributes();
		this.doLayout();
	}

	private void repositionAttributes() {

		for(int i = 0; dataFields != null && i < dataFields.size(); i++) {
			Attribute a = dataFields.get(i);
			a.setLocation(this.getNextDataFieldPoint(i));
		}

		for(int i = 0; methods != null && i < methods.size(); i++) {
			Attribute a = methods.get(i);
			a.setLocation(this.getNextMethodPoint(i));
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
		if(this.dataFields.size() == 0)
			return this.getPreferredSize().height / 2;
		else {
			int y = this.getNextDataFieldPoint(dataFields.size()).y;
			return (int) (y / (double)(dataFields.size()) * (dataFields.size() + 1)) + 3;
		}
	}

	/** Calculates the location of the next data field label from the top of the rectangle down to the attribute specified by the argument.
	 * 
	 * @param afterDataFieldNumber	The index at which a new data label is to be inserted minus one. 
	 * @return 						The Point at which it is safe to insert a new data field below the one specified by the argument.
	 */
	public Point getNextDataFieldPoint(int afterDataFieldNumber) {
		int y = name.getPreferredSize().height + 5;

		for(int i = 0; i < afterDataFieldNumber && i >= 0 ;i++) {
			int height = dataFields.get(i).getPreferredSize().height;
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
			int height = methods.get(i).getPreferredSize().height;
			y += (int) (height * 1.25);	
		}
		return new Point(4,y);
	}

	private class RectanglePopupMenu extends JPopupMenu {

		private RectangleListener listener;

		public RectanglePopupMenu(RectangleListener listener) {

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

	/** Defines class rectangle specific operations */
	private class RectangleListener extends DiagramListener implements ActionListener, java.io.Serializable {
		
		private Point startingMousePosition;
		
		public RectangleListener(ClassRectangle c) {
			this.assignTo(c);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String c = e.getActionCommand();
			if(c.equals("Add relationship")) {
				//TODO: Implement
			}
			if(c.equals("Add data field")) {
				Attribute newDataField = new Attribute(
						((ClassRectangle)diagram).getNextDataFieldPoint(-1), 
						"- dataField : Type",
						AttributeType.DATA_FIELD);
				newDataField.repaint();
				diagram.add(newDataField);
				diagram.revalidate();
				diagram.repaint();
				this.mode = ListeningMode.EDITING_TEXT; 
				this.enableLabelEdit(newDataField);
			}
			if(c.equals("Add method")) {
				Attribute newMethod = new Attribute(
						((ClassRectangle)diagram).getNextMethodPoint(-1), 
						"+ method(args : ArgType) : ReturnType",
						AttributeType.METHOD);
				newMethod.repaint();
				diagram.add(newMethod);
				diagram.revalidate();
				diagram.repaint();
				this.mode = ListeningMode.EDITING_TEXT; 
				this.enableLabelEdit(newMethod);

			}
			if(c.equals("Remove")) {
				diagram.setVisible(false);
			}

		}
		public void mousePressed(MouseEvent e){
			((ClassRectangle) diagram).setPaintState(ElementPaintState.SELECTED);

		}
		
		public void mouseReleased(MouseEvent e){
			mode = ListeningMode.LISTEN_TO_ALL;
		}

		public void mouseDragged(MouseEvent e){
			if(mode != ListeningMode.DRAGGING && ((ClassRectangle) diagram).getPaintState() != ElementPaintState.MOUSED_OVER_RESIZE) {
				mode = ListeningMode.DRAGGING;
				startingMousePosition = e.getPoint();
			}
			
			if (mode == ListeningMode.DRAGGING){
				Rectangle r = diagram.getBounds();
                r.x += e.getX() - startingMousePosition.x;  
                r.y += e.getY() - startingMousePosition.y;
                r.setBounds(r);
				diagram.setLocation(r.getLocation());
				diagram.getParent().doLayout();
			}
			diagram.repaint();
		}
	}

}

