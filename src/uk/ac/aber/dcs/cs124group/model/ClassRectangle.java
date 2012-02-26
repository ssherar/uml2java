package uk.ac.aber.dcs.cs124group.model;

import uk.ac.aber.dcs.cs124group.controller.*;
import uk.ac.aber.dcs.cs124group.gui.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Observable;


public class ClassRectangle extends DocumentElementView {

	private static final Dimension DEFAULT_RECTANGLE_SIZE = new Dimension(350,225);
	private static final Color RECTANGLE_BACKGROUND = new Color(255,255,190);


	private TextLabel name;

	private ClassModel model;


	public ClassRectangle(ClassModel model) {
		this.model = model;
		
		this.setLocation(model.getLocation());
		this.setPreferredSize(model.getSize());
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


		ClassController listener = new ClassController(this.model);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
		this.addMouseMotionListener(listener);
		this.name.addMouseListener(listener);

		RectanglePopupMenu popupMenu = new RectanglePopupMenu(listener);
		this.setComponentPopupMenu(popupMenu);
	}



	@Override
	public Container add(Component c) {
		super.add(c);

		if(!(c instanceof JTextArea))this.repositionAttributes();
		return this;
	}

	@Override
	public void remove(Component c) {
		super.remove(c);

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
		ArrayList<Attribute> dataFields = this.model.getDataFields();
		ArrayList<Attribute> methods = this.model.getMethods();
		
		for(int i = 0; dataFields != null && i < dataFields.size(); i++) {
			Attribute a = dataFields.get(i);
			a.setLocation(this.model.getNextDataFieldPoint(i));
		}

		for(int i = 0; methods != null && i < methods.size(); i++) {
			Attribute a = methods.get(i);
			a.setLocation(this.model.getNextMethodPoint(i));
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
			int y = this.model.getNextDataFieldPoint(this.model.getDataFields().size()).y;
			return (int) (y / (double)(this.model.getDataFields().size()) * (this.model.getDataFields().size() + 1)) + 3;
		}
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

	/** Defines class rectangle specific operations */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}

