package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import javax.swing.*;

public abstract class DocumentElement extends JPanel implements java.io.Serializable {

	
	private static final long serialVersionUID = -253995425441515922L;
	private Point position;
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	private Font font;
	private double zoomFactor = 1;
	
	protected DocumentElement() {
		
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
		this.setBounds(position.x, position.y, (int)(zoomFactor * width), (int)(zoomFactor * height));
	}
	
	public void setPosition(Point p) {
		position = p;
	}

	public Point getPosition() {
		return position;
	}
	
	public void setPaintState(ElementPaintState paintState) {
		this.paintState = paintState;
	}
	
	public ElementPaintState getPaintState() {
		return paintState;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	
	public abstract void move(Point newPos);
	
	

}
