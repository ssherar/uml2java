package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.gui.DiagramListener;
import uk.ac.aber.dcs.cs124group.gui.ListeningMode;

public class TextLabel extends DocumentElement {

	private static final long serialVersionUID = -7388262736446472023L;
	private String text = "Double-click to edit";
	private FontMetrics metrics;
	private int alignmentInParent = JTextField.LEFT;
	private Container suspendedParent;
	private JTextArea replacement;
	
	public TextLabel(Point p) {
		setLocation(p);
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(56,12));
		this.setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
		this.setName("label");
		this.resizeToText();
		this.setLayout(null);
		
		LabelListener listener = new LabelListener(this);
		this.addMouseListener(listener);
		this.addKeyListener(listener);

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
	
	public int getAlignmentInParent() {
		return alignmentInParent;
	}

	public void setAlignmentInParent(int alignmentInParent) {
		this.alignmentInParent = alignmentInParent;
	}

	public void setText(String text) {
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
					getParent().doLayout();
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
		labelTextArea.setName("EditingDiagramLabel");
		labelTextArea.setOpaque(false);
		labelTextArea.setFont(this.getFont());
		labelTextArea.setLineWrap(true);
		labelTextArea.setWrapStyleWord(true);
		
		//labelTextArea.setFont(document.getPreferences().getFont()); TODO: Fixme
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
			this.setVisible(false);
			return;
		}
		this.setText(a.getText());
		suspendedParent.add(this);
		this.getParent().repaint();
	}
	
	private class LabelListener extends DiagramListener implements java.io.Serializable {
		
		private TextLabel diagram;
		
		public LabelListener(TextLabel l) {
			this.diagram = l;
		}
		
	
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2 && !e.isConsumed() && this.getMode() == ListeningMode.LISTEN_TO_ALL) {
				e.consume();
				this.setMode(ListeningMode.EDITING_TEXT);
				diagram.enableEdit();
					
			}
		}
		
		@Override 
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				diagram.exitEdit();
				this.setMode(ListeningMode.LISTEN_TO_ALL);
			}
			
		}
	}
}
