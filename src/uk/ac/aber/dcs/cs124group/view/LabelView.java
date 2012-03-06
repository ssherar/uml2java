package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.controller.LabelController;
import uk.ac.aber.dcs.cs124group.model.*;
/**
 * This Class handles the general methods of the textareas
 * on the class diagrams, including editing, removing and updating
 * the label.
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version v.1.0.0
 */
public class LabelView extends DocumentElementView {
	/**
	 * The constant from the {@link java.uo.Serailizable} interface so we can 
	 * save attributes
	 */
	private static final long serialVersionUID = -7388262736446472023L;
	
	/**
	 * The value of the label to show/edit
	 */
	private String text;
	
	/**
	 * A Class to manipulate the fonts.
	 */
	private FontMetrics metrics;
	
	/**
	 * A dummy TextArea to replace the Label for editing
	 */
	private JTextArea replacement;
	
	/**
	 * A reference to the model class for data manipulation
	 */
	private TextLabelModel model;
	
	/**
	 * To initialise variables with default values and grab data from
	 * the Model class
	 * @see TextLabelModel
	 * @param m		
	 */
	public LabelView(TextLabelModel m) {
		/*
		 * Initialise the global variables
		 */
		this.model = m;
		this.text = m.getText();
		
		/*
		 * ... and manipulate the textarea according to the model
		 */
		this.setLocation(m.getLocation());
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(56,12));
		this.setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
		this.setName("label");
		model.setSize(this.getPreferredSize());
		this.resizeToText();
		this.setLayout(null);
		
		/*
		 * Add all the various listeners for the functionality of moving
		 * and right clicking.
		 */
		LabelController listener = new LabelController(this.model);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
		/*
		 * Only attributes need a rightclick method for modifying
		 */
		if(this.model instanceof Attribute) {
			if(((Attribute) this.model).getType() == AttributeType.DATA_FIELD)
				this.setComponentPopupMenu(new AttributePopup(listener, true));
			else
				this.setComponentPopupMenu(new AttributePopup(listener, false));
		}

	}
	
	/**
	 * Brings back the model which is associated with this
	 * Label
	 * @return		The TextLabelModel
	 */
	public TextLabelModel getModel() {
		return this.model;
	}
	
	/**
	 * Draws the string to the component with scaling
	 * and font styles through FontMetrics
	 * @see FontMetrics
	 * @see DocumentElementView#setFont(font)
	 * @param g		Graphics of the JPanel for drawing
	 */
	public void paintComponent(Graphics d) {
		super.paintComponent(d);
		Graphics2D g = (Graphics2D) d;
		// Deprecated
		g.scale(this.getZoomFactor(), this.getZoomFactor());
		g.setFont(getFont());
		metrics = g.getFontMetrics();
		
		int textX = 0;
		//Deprecated 
		double scaleY = this.getZoomFactor() < 1 ? 2 : 2 * this.getZoomFactor();
		int textY = (int) ((this.getPreferredSize().height + metrics.getAscent()) / scaleY);
		g.drawString(text, textX, textY);
	}
	
	/**
	 * Aligns the Label in the parent according to the constants in
	 * JTextField
	 * @see JTextField#LEFT
	 * @see JTextField#CENTER
	 * @see JTextField#RIGHT
	 * 
	 * @param alignment		Set through JTextField.LEFT, .CENTER or .RIGHT
	 */
	public void setAlignmentInParent(int alignment) {
		this.model.setAlignmentInParent(alignment);
	}
	
	/**
	 * Manual realignment according to {@link LabelView#setAlignmentInParent(alignment) setAlignmentInParent(alignment)}
	 * @see LabelView#setAlignmentInParent(alignment) 
	 */
	public void realign() {
		if(model.getAlignmentInParent() == JTextField.CENTER) {
			model.setLocation(new Point((getParent().getPreferredSize().width - getPreferredSize().width) / 2, model.getLocation().y), false);
		}
	}
	
	/**
	 * Set the text and resize the Model to the new length
	 * @param text		The new text of the label
	 */
	private void setText(String text) {
		this.text = text;
		resizeToText();		
	}
	
	/**
	 * Returns the text held in the label (which is also in the model)
	 * @return
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Set the font of the label, and resizes if both
	 * Graphics and FontMetrics are available
	 * @param f		The new font to apply
	 */
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		if(this.getGraphics() != null && metrics != null) {
			resizeToText();
		}
	}
	
	/**
	 * Set the zoom factor of the label and resize to the new height
	 * and width of the label
	 * @deprecated v0.9
	 */
	@Override
	public void setZoomFactor(double zoom) {
		super.setZoomFactor(zoom);
		resizeToText();
	}
	
	/**
	 * 
	 */
	private void resizeToText() {
		/*
		 * Try resizing
		 */
		try {
			metrics = getGraphics().getFontMetrics();
			int width = metrics.stringWidth(text);
			setPreferredSize(new Dimension(
					(int) (getZoomFactor() * 1.1 * width + 1), 
					(int) (getZoomFactor() * metrics.getHeight())));
			model.setSize(this.getPreferredSize());
			getParent().doLayout();
			realign();
		}
		catch(NullPointerException ex) {
			/*
			 * If it throws an error, do it at the end of the thread
			 */
			SwingUtilities.invokeLater(new Runnable() {
			
				@Override
				public void run() {
					try {
						metrics = getGraphics().getFontMetrics();
					}
					catch(NullPointerException ex) {
						/*
						 * And we give up if we can't do it at the end of the
						 * thread
						 */
						return;
					}
					int width = metrics.stringWidth(text);
					setPreferredSize(new Dimension(
							(int) (getZoomFactor() * 1.1 * width + 1), 
							(int) (getZoomFactor() * metrics.getHeight())));
					model.setSize(getPreferredSize());
					getParent().doLayout(); 
					
					realign();
				}
			
			});
		}

	}
	/**
	 * 
	 */
	public void enableEdit() {
		this.suspendedParent = this.getParent();
		this.getParent().remove(this);
		
		final JTextArea labelTextArea = new JTextArea();
		
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		
		int diagramWidth = suspendedParent.getPreferredSize().width;
		int diagramHeight = suspendedParent.getPreferredSize().height;
		
		labelTextArea.setPreferredSize(new Dimension(diagramWidth - x, diagramHeight - y));
		labelTextArea.setLocation(new Point(x,y));
		labelTextArea.setOpaque(false);
		labelTextArea.setFont(this.getFont());
		labelTextArea.setLineWrap(true);
		labelTextArea.setWrapStyleWord(true);
		labelTextArea.repaint();
		
		
		labelTextArea.setText(this.getText());
		labelTextArea.addKeyListener(this.getKeyListeners()[0]);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTextArea.requestFocus();
				labelTextArea.selectAll();
			}
			
		});
		suspendedParent.add(labelTextArea);
		((JPanel)(suspendedParent)).revalidate();
		suspendedParent.repaint();
		
		replacement = labelTextArea;
	}
	
	public void exitEdit() {
		if(!this.model.exists()) {
			replacement.setVisible(false);
			return;
		}
		
		JTextArea a = replacement;
		suspendedParent.remove(a);
		suspendedParent.add(this);
		
		if(a.getText().length() < 1) {
			if(this.model.isClassName()) {
				a.setText(this.model.getText());
			}
			else {
				this.model.userRemove();
				return;
			}		
		}
		
		model.setText(a.getText(), true);
		
		this.getParent().repaint();
	}
	
	

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		
		if(!(arg instanceof String)) {
			throw new IllegalArgumentException("Invalid argument: Need a string");
		}
		
		if(o instanceof TextLabelModel) {
			this.updateModel((TextLabelModel)o, (String) arg);
		} else if(o instanceof DocumentPreferences) {
			this.updatePreferences((DocumentPreferences)o, (String) arg);
		}
		
	}
	
	private void updateModel(TextLabelModel o, String arg) {
		if(arg.equals("textChanged")) {
			this.setText(model.getText());
		}
		//location
		else if(arg.equals("locationChanged")) {
			this.setLocation(o.getLocation());
		}
		//editing
		else if(arg.equals("editingChanged")) {
			if(o.isEditing()) {
				//true
				this.enableEdit();
			} else {
				this.exitEdit();
			}
			
		} else if(arg.equals("flagChanged")) {
			
			/* Reference: http://stackoverflow.com/questions/325840/what-is-the-constant-value-of-the-underline-font-in-java */
			Map<TextAttribute, Integer> underlineFont = new HashMap<TextAttribute, Integer>();
			underlineFont.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			Font staticChanged = new Font(this.getFont().getName(), Font.PLAIN, this.getFont().getSize()).deriveFont(underlineFont);
			
			Font abstractChanged = new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize());
			Font normal = new Font(this.getFont().getName(), Font.PLAIN, this.getFont().getSize());
			
			Attribute a = (Attribute) o;
			if(a.isFlagAbstract()) {
				this.setFont(normal, false);
				this.setFont(abstractChanged, true);
				((AttributePopup)this.getComponentPopupMenu()).setFlag("Abstract");
			} else if(a.isFlagStatic()) {
				this.setFont(normal, false);
				this.setFont(staticChanged, true);
				((AttributePopup)this.getComponentPopupMenu()).setFlag("Static");
			} else if(a.isFlagFinal()) {
				((AttributePopup)this.getComponentPopupMenu()).setFlag("Final");
			} else if(a.isFlagTransient()) {
				((AttributePopup)this.getComponentPopupMenu()).setFlag("Transient");
			} else {
				this.setFont(normal, true);
				((AttributePopup)this.getComponentPopupMenu()).setNone();
			}
			((AttributePopup)this.getComponentPopupMenu()).checkIfSet();
			this.repaint();
			
		}
	}
	
	private void updatePreferences(DocumentPreferences o, String arg) {
		//setFont
		if(arg.equals("fontChanged")) {
			this.setFont(o.getFont());
		} else if(arg.equals("zoomLevelChanged")) {
			this.setZoomFactor(o.getZoomLevel());
		}
			
	}

	
	public class AttributePopup extends JPopupMenu {
		
		private LabelController listener;
		private String[] dataModifiers = {"Final", "Static", "Transient", "None"};
		private String[] methodModifiers = {"Static", "Abstract", "Final", "None"};
		JMenu modifers = new JMenu("Modifiers...");
		
		public AttributePopup(LabelController l, boolean data) {
			this.listener = l;
			
			JMenuItem submenu;
			ButtonGroup group = new ButtonGroup();
			if(data) {
				for(String s : this.dataModifiers) {
					submenu = new JRadioButtonMenuItem(s, s.equals("None"));
					submenu.addActionListener(listener);
					if(!(s.equals("Static") || s.equals("Final"))) {
						//group.add(submenu);
					}
					modifers.add(submenu);
					submenu = null;
				}
			} else {
				for(String s : this.methodModifiers) {
					submenu = new JRadioButtonMenuItem(s, s.equals("None"));
					submenu.addActionListener(listener);
					//group.add(submenu);
					modifers.add(submenu);
					submenu = null;
				}
			}
			this.add(modifers);
			
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(listener);
			this.add(delete);
		}
		
		public void checkIfSet() {
			boolean set = false;
			int none = -1;
			for(int i = 0; i < this.modifers.getItemCount(); i++) {
				if(this.modifers.getItem(i).isSelected()) {
					if(this.modifers.getItem(i).getText().equals("None")) {
						none = i;
					} else {
						set = true;
					}
					
				}
			}
			if(none > -1 )
				this.modifers.getItem(none).setSelected(!(set));
		}
		
		public void setNone() {
			for(int i = 0; i < this.modifers.getItemCount(); i++) {
				if(this.modifers.getItem(i).isSelected()) {
					this.modifers.getItem(i).setSelected(false);
				}
				if(this.modifers.getItem(i).getText().equals("None")) 
					this.modifers.getItem(i).setSelected(true);
			}
		}
		
		public void setFlag(String s) {
			for(int i = 0; i < this.modifers.getItemCount(); i++) {
				if(this.modifers.getItem(i).getText().equals(s))
					this.modifers.getItem(i).setSelected(true);
			}
		}
	}
}
