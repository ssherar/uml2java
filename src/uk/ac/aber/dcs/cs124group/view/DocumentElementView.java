package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.util.Observable;

import javax.swing.*;

public abstract class DocumentElementView extends JPanel implements java.util.Observer {

	
	
	private Font font;
	private double zoomFactor = 1;
	
	protected Container suspendedParent;
	
	protected DocumentElementView() {
		
	}
	
	public void setFont(Font font) {
		this.font = font;
		super.setFont(font);
		doLayout();
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
	
	@Override
	public void update(Observable o, Object s) {
		if(!(s instanceof String))
			throw new IllegalArgumentException("String expected");
		if(s.equals("wasRemoved")) {
			this.suspendedParent = this.getParent();
			try {
				this.getParent().remove(this);
				suspendedParent.repaint();
			}
			catch(NullPointerException ex) {
				
			}
			this.setVisible(false);

		}
		else if(s.equals("wasResurrected")) {
			this.setVisible(true);
			this.suspendedParent.add(this);
			this.getParent().doLayout();
			this.getParent().repaint();
		}
	}
	

	
	

}
