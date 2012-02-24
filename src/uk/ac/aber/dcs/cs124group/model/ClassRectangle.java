package uk.ac.aber.dcs.cs124group.model;

import uk.ac.aber.dcs.cs124group.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.util.ArrayList;


public class ClassRectangle extends DocumentElement {

	private static final long serialVersionUID = 249568855144662119L;
	private static final Dimension DEFAULT_RECTANGLE_SIZE = new Dimension(150,100);
	private static final Color RECTANGLE_BACKGROUND = new Color(255,255,190);
	
	
	private TextLabel name;
	private Dimension size;
	private ClassRectangle superClass = null;
	private IVisibility visibility = IVisibility.PUBLIC;
	
	private ArrayList<Relationship> relationships;
	private ArrayList<Attribute> attributes;
	
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
		return this.attributes;
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
		
		if(getPaintState() == ElementPaintState.DEFAULT) {
	
			
			g.drawRoundRect(0, 0, width - 1 , height - 1, 10, 10);
			g.setColor(RECTANGLE_BACKGROUND);
			g.fillRoundRect(1, 1, width - 2, height - 2, 10, 10);
			
			int nameFieldHeight = name.getPreferredSize().height;
			g.setColor(Color.BLACK);
			g.drawLine(0, nameFieldHeight, width - 1, nameFieldHeight);
			
		}
	}
		
	public Point getNextDataFieldPoint() {
		return new Point(3,30);
	}
	
	public Point getNextMethodPoint() {
		return new Point(0,60);
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
	private class RectangleListener extends DiagramListener implements ActionListener {
		
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
						((ClassRectangle)diagram).getNextDataFieldPoint(), 
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
						((ClassRectangle)diagram).getNextMethodPoint(), 
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
		
	}

}
