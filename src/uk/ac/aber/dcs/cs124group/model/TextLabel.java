package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import javax.swing.*;

public class TextLabel extends DocumentElement {

	private static final long serialVersionUID = -7388262736446472023L;
	private String text = "New label";
	private FontMetrics metrics;
	
	public TextLabel(Point p) {
		setPosition(p);
		
		//this.setBackground(Color.RED); //debug statement, obv
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(56,12));
		this.setBounds(getPosition().x, getPosition().y, getPreferredSize().width, getPreferredSize().height);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				metrics = getGraphics().getFontMetrics();
				resizeToText();
			}
			
		});
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
			metrics = this.getGraphics().getFontMetrics();
		}
		catch (NullPointerException ex) {
			
		}
		int width = metrics.stringWidth(this.text);
		this.setPreferredSize(new Dimension(
				(int) (this.getZoomFactor() * 1.1 * width + 1), 
				(int) (this.getZoomFactor() * metrics.getAscent())));
		this.setBounds(getPosition().x, getPosition().y, getPreferredSize().width, getPreferredSize().height);
	}

	@Override
	public void move(Point newPos) {
		

	}

}
