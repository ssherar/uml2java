package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import javax.swing.*;

public abstract class DocumentElement extends JPanel implements java.io.Serializable {

	
	private static final long serialVersionUID = -253995425441515922L;
	private Point location;
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	private Font font;
	private double zoomFactor = 1;
	
	protected DocumentElement() {
		
	}
	
	public void setFont(Font font) {
		this.font = font;
		for(int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setFont(font);
		}
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
		this.setBounds(location.x, location.y, (int)(zoomFactor * width), (int)(zoomFactor * height));
	}
	
	@Override
	public void setLocation(Point p) {
		location = p;
	}

	@Override
	public Point getLocation() {
		return location;
	}
	
	public void setPaintState(ElementPaintState paintState) {
		this.paintState = paintState;
	}
	
	public ElementPaintState getPaintState() {
		return paintState;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	
	public abstract void move(Point newPos);
	
	

}
