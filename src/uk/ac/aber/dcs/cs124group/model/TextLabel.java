package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import javax.swing.*;

public class TextLabel extends DocumentElement {

	private static final long serialVersionUID = -7388262736446472023L;
	private String text = "Double-click to edit";
	private FontMetrics metrics;
	private int alignmentInParent = JTextField.LEFT;
	
	public TextLabel(Point p) {
		setLocation(p);
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(56,12));
		this.setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
		this.setName("label");

		this.resizeToText();
		//textArea.setPreferredSize(this.getPreferredSize());
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
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				metrics = getGraphics().getFontMetrics();
				int width = metrics.stringWidth(text);
				setPreferredSize(new Dimension(
						(int) (getZoomFactor() * 1.1 * width + 1), 
						(int) (getZoomFactor() * metrics.getHeight())));
				setBounds(getLocation().x, getLocation().y, getPreferredSize().width, getPreferredSize().height);
			}
			
		});
		

	}

	@Override
	public void move(Point newPos) {
		

	}

}
