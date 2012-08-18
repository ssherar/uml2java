package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import com.jidesoft.swing.*;

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
public class LabelView extends DocumentElementView implements DocumentListener {
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
	 * A dummy TextArea to replace the Label for editing
	 */
	private JTextArea replacement;
	private StyledLabel label;
	
	/**
	 * A reference to the model class for data manipulation
	 */
	private TextLabelModel model;
	
	private CardLayout layout = new CardLayout();
	
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
		label = new StyledLabel(this.text);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setLineWrap(true);
		replacement = new JTextArea(this.text + " ");
		replacement.getDocument().addDocumentListener(this);
		
		/*
		 * ... and manipulate the textarea according to the model
		 */
		this.setLocation(m.getLocation());
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(150,50));
		this.setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
		this.setName("label");
		model.setSize(this.getPreferredSize());
		this.setLayout(layout);
		
		this.add(label, "label");
		this.add(replacement, "replacement");
		
		/*
		 * Add all the various listeners for the functionality of moving
		 * and right clicking.
		 */
		LabelController listener = new LabelController(this.model);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
		replacement.addKeyListener(listener);
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
		text = text.trim();
		text = text.replaceAll("\\{", "\\{");
		text = text.replaceAll("\\}", "\\}");
		text = text.replaceAll("\\(", "\\(");
		text = text.replaceAll("\\)", "\\)");
		text = text.replaceAll("#", "\\#");
		this.text = text;
		label.setText(text);
		replacement.setText(text);
		this.adjust();
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
		if(label != null) label.setFont(f);
		if(replacement != null) replacement.setFont(f);
		adjust();
	}
	
	
	/**
	 * Enable the edit of a label when the {@link LabelController} fires on a double
	 * click. <p> We extract the text from the label, set it invisible and place the new
	 * value into a JTextArea for ease of editing.
	 * @see LabelView#replacement
	 */
	public void enableEdit() {
		
		
		
		replacement.setPreferredSize(label.getSize());
		replacement.setLocation(label.getLocation());
		replacement.setOpaque(true);
		replacement.setBackground(Color.yellow);
		replacement.setFont(this.getFont());
		replacement.setLineWrap(true);
		replacement.setWrapStyleWord(true);
		replacement.repaint();
		replacement.getDocument().addDocumentListener(this);
		

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				/*
				 * As there is a problem requesting focus while doing
				 * the rest of the creation, we have to run the requestFocus()
				 * and SelectAll at the end of the queue.
				 */
				replacement.requestFocus();
				replacement.selectAll();
			}
			
		});
		
		layout.next(this);
	}
	
	/**
	 * Exits the editing phase when fired from the {@link LabelController}. 
	 * This does the opposite of {@link LabelView#enableEdit()} and returns the
	 * new values to the label
	 */
	public void exitEdit() {
		/*
		 * Clean up if something has been deleted in the meantime
		 */
		if(!this.model.exists()) {
			replacement.setVisible(false);
			return;
		}
		
	
		JTextArea a = replacement;

		
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
		setText(a.getText());
		label.setPreferredSize(a.getPreferredSize());
		layout.next(this);
		this.getParent().repaint();
	}
	
	
	/*
	 * Similar to the Superclass "Update", this handles DocumentPreferences and TextLabelModel
	 * @see uk.ac.aber.dcs.cs124group.view.DocumentElementView#update(java.util.Observable, java.lang.Object)
	 */
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
	
	/**
	 * Update the model with the new values from the {@link TextLabelModel}
	 * @param o			The new values for the label
	 * @param arg		The command we want to do
	 */
	private void updateModel(TextLabelModel o, String arg) {
		//TextChanged
		if(arg.equals("textChanged")) {
			this.setText(model.getText());
		}
		//location
		else if(arg.equals("locationChanged")) {
			this.setLocation(o.getLocation());
		}
		//Editing the label
		else if(arg.equals("editingChanged")) {
			if(o.isEditing()) {
				this.enableEdit();
			} else {
				this.exitEdit();
			}
		
		} 
		//Changing the flag means changing the font!
		else if(arg.equals("flagChanged")) {
			
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
		} 
		
			
	}

	/**
	 * A Rightclick Popup menu for the Attribute labels for editing
	 * deleting and adding modifiers with ease.
	 * <p>A private class only used by LabelView - it doesn't
	 * need scope of any other class.
	 * @author Daniel Maly
	 * @author Samuel B Sherar
	 * @author Lee Smith
	 * @version 1.0.0
	 */
	public class AttributePopup extends JPopupMenu {
		/**
		 * The main listener for the actionEvents thrown
		 */
		private LabelController listener;
		
		/**
		 * Constants for the modifiers. One for Data and one for Methods
		 */
		private String[] dataModifiers = {"Final", "Static", "Transient", "None"};
		private String[] methodModifiers = {"Static", "Abstract", "Final", "None"};
		JMenu modifers = new JMenu("Modifiers...");
		
		/**
		 * Construct the new popup menu
		 * @param l			the actionListener for the events
		 * @param data		<code> true</code> if its a datafield, <code>false</code> if its a method
		 */
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
		
		/**
		 * Check if the option "None" is set. If so, unset it!
		 */
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
		
		/**
		 * Cycle through all the different options setting them to false
		 * and set None to <code>True</code>
		 */
		public void setNone() {
			for(int i = 0; i < this.modifers.getItemCount(); i++) {
				if(this.modifers.getItem(i).isSelected()) {
					this.modifers.getItem(i).setSelected(false);
				}
				if(this.modifers.getItem(i).getText().equals("None")) 
					this.modifers.getItem(i).setSelected(true);
			}
		}
		
		/**
		 * Set the flag specified in the parameter as true
		 * @param s		The flag in question
		 */
		public void setFlag(String s) {
			for(int i = 0; i < this.modifers.getItemCount(); i++) {
				if(this.modifers.getItem(i).getText().equals(s)) {
					this.modifers.getItem(i).setSelected(true);
					break;
				}
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		adjust();
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		adjust();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		adjust();
		
	}
	
	private void adjust() {
		if(replacement == null || label == null) return;
		
		int height = replacement.getPreferredSize().height;
		Dimension currentSize = replacement.getPreferredSize();
		int lines = countLines(replacement);
		
		int fontSizeDif = replacement.getFont().getSize() - 11;
		int properSize = (int) (lines * (Math.pow(replacement.getFont().getSize(), 1.08 + fontSizeDif/65.0)));
		
		
		if(height < properSize || height > properSize) {
			replacement.setPreferredSize(new Dimension(currentSize.width, properSize));
			this.setPreferredSize(replacement.getPreferredSize());
			this.doLayout();
			if(this.getParent() != null) this.getParent().doLayout();
			this.model.setSize(this.getPreferredSize());
		}
	}
	
	private int countLines(JTextArea textArea) {
		if(textArea.getText().length() == 0) return 1;
		
	    AttributedString text = new AttributedString(textArea.getText());
	    FontRenderContext frc = textArea.getFontMetrics(textArea.getFont())
	        .getFontRenderContext();
	    AttributedCharacterIterator charIt = text.getIterator();
	    LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
	    float formatWidth = (float) (textArea.getSize().width);
	    lineMeasurer.setPosition(charIt.getBeginIndex());

	    int noLines = 0;
	    while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
	      lineMeasurer.nextLayout(formatWidth);
	      noLines++;
	    }

	    return noLines;
	  }
	
	
}
