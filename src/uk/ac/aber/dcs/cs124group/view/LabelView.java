package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.controller.DiagramListener;
import uk.ac.aber.dcs.cs124group.controller.LabelController;
import uk.ac.aber.dcs.cs124group.controller.ListeningMode;
import uk.ac.aber.dcs.cs124group.model.*;

public class LabelView extends DocumentElementView {

	private static final long serialVersionUID = -7388262736446472023L;
	private String text;
	private FontMetrics metrics;
	private JTextArea replacement;
	private TextLabelModel model;
	
	public LabelView(TextLabelModel m) {

		this.model = m;
		this.text = m.getText();
		this.setLocation(m.getLocation());
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(56,12));
		this.setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
		this.setName("label");
		model.setSize(this.getPreferredSize());
		this.resizeToText();
		this.setLayout(null);
		
		LabelController listener = new LabelController(this.model);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
		if(this.model instanceof Attribute) {
			if(((Attribute) this.model).getType() == AttributeType.DATA_FIELD)
				this.setComponentPopupMenu(new AttributePopup(listener, true));
			else
				this.setComponentPopupMenu(new AttributePopup(listener, false));
		}

	}
	
	public void paintComponent(Graphics d) {
		super.paintComponent(d);
		Graphics2D g = (Graphics2D) d;
		g.scale(this.getZoomFactor(), this.getZoomFactor());
		g.setFont(getFont());
		metrics = g.getFontMetrics();
		
		int textX = 0;
		double scaleY = this.getZoomFactor() < 1 ? 2 : 2 * this.getZoomFactor();
		int textY = (int) ((this.getPreferredSize().height + metrics.getAscent()) / scaleY);
		g.drawString(text, textX, textY);
	}
	
	public void setAlignmentInParent(int alignment) {
		this.model.setAlignmentInParent(alignment);
	}
	
	public void realign() {
		if(model.getAlignmentInParent() == JTextField.CENTER) {
			model.setLocation(new Point((getParent().getPreferredSize().width - getPreferredSize().width) / 2, model.getLocation().y), false);
		}
	}

	private void setText(String text) {
		this.text = text;
		resizeToText();		
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setFont(Font f) {
		super.setFont(f);
		if(this.getGraphics() != null && metrics != null) {
			resizeToText();
		}
	}
	
	@Override
	public void setZoomFactor(double zoom) {
		super.setZoomFactor(zoom);
		resizeToText();
	}
	
	private void resizeToText() {
		
		try {
			metrics = getGraphics().getFontMetrics();
			int width = metrics.stringWidth(text);
			setPreferredSize(new Dimension(
					(int) (getZoomFactor() * 1.1 * width + 1), 
					(int) (getZoomFactor() * metrics.getHeight())));
			model.setSize(this.getPreferredSize());
			getParent().doLayout();
		}
		catch(NullPointerException ex) {
		
			SwingUtilities.invokeLater(new Runnable() {
			
				@Override
				public void run() {
					try {
						metrics = getGraphics().getFontMetrics();
					}
					catch(NullPointerException ex) {
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
		JTextArea a = replacement;
		suspendedParent.remove(a);
		if(a.getText().length() < 1) {
			if(this.model.isClassName()) {
				a.setText(this.model.getText());
			}
			else {
				this.model.remove();
				this.setVisible(false);
				return;
			}		
		}
		
		model.setText(a.getText(), true);
		suspendedParent.add(this);
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
			Map<TextAttribute, Integer> underlineFont = new HashMap<TextAttribute, Integer>();
			underlineFont.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			Font finalChanged = new Font(this.getFont().getName(), Font.PLAIN, this.getFont().getSize()).deriveFont(underlineFont);
			Font abstractChanged = new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize());
			Attribute a = (Attribute) o;
			if(a.isFlagAbstract()) {
			this.setFont(abstractChanged);
			}
			
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
		private String[] dataModifiers = {"Final", "Abstract", "Transient", "None"};
		private String[] methodModifiers = {"Static", "Abstract", "None"};
		
		public AttributePopup(LabelController l, boolean data) {
			this.listener = l;
			JMenu modifers = new JMenu("Modifiers...");
			JMenuItem submenu;
			ButtonGroup group = new ButtonGroup();
			if(data) {
				for(String s : this.dataModifiers) {
					submenu = new JRadioButtonMenuItem(s, s.equals("None"));
					submenu.addActionListener(listener);
					group.add(submenu);
					modifers.add(submenu);
					submenu = null;
				}
			} else {
				for(String s : this.methodModifiers) {
					submenu = new JRadioButtonMenuItem(s, s.equals("None"));
					submenu.addActionListener(listener);
					group.add(submenu);
					modifers.add(submenu);
					submenu = null;
				}
			}
			this.add(modifers);
			
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(listener);
			this.add(delete);
		}
	}
}
