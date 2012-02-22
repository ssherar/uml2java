package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import javax.swing.*;

public class TextLabel extends DocumentElement {

	private static final long serialVersionUID = -7388262736446472023L;
	private String text = "New label";
	private FontMetrics metrics;
	
	public TextLabel(Point p) {
		setPosition(p);
		
		this.setBackground(Color.RED); //debug statement, obv
		this.setOpaque(true);
		
		this.setPreferredSize(new Dimension(53,18));
		this.setBounds(getPosition().x, getPosition().y, getPreferredSize().width, getPreferredSize().height);
		//textArea.setPreferredSize(this.getPreferredSize());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(getFont());
		metrics = g.getFontMetrics();
		

		
		g.drawString(text, 0, metrics.getHeight() - 1);
	}
	
	public void setText(String text) {
		this.text = text;
		resizeToText();		
	}
	
	public void setFont(Font f) {
		super.setFont(f);
		if(this.getGraphics() != null && metrics != null) {
			resizeToText();
		}
	}
	
	private void resizeToText() {
		
		metrics = this.getGraphics().getFontMetrics();
		int width = metrics.stringWidth(this.text);
		this.setPreferredSize(new Dimension((int) (1.1 * width + 1),metrics.getHeight()));
		this.setBounds(getPosition().x, getPosition().y, getPreferredSize().width, getPreferredSize().height);
	}

	@Override
	public void move(Point newPos) {
		

	}

}
