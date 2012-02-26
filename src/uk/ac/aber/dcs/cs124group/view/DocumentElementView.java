package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;

import javax.swing.*;

public abstract class DocumentElementView extends JPanel implements java.util.Observer {

	
	
	private Font font;
	private double zoomFactor = 1;
	
	protected DocumentElementView() {
		
	}
	
	public void setFont(Font font) {
		this.font = font;
		
	}
	
	public Font getFont() {
		return font;
	}
	
	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
		
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		this.setBounds(this.getLocation().x, this.getLocation().y, (int)(zoomFactor * width), (int)(zoomFactor * height));

	}
	


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	

	
	

}
