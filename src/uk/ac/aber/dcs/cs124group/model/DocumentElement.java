package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

public abstract class DocumentElement implements java.io.Serializable {

	
	private static final long serialVersionUID = -253995425441515922L;
	protected Point position;
	private transient ElementPaintState paintState = ElementPaintState.DEFAULT;
	private String fileName;
	
	protected DocumentElement() {
		
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
	
	public void setFileName(String f) {
		fileName = f;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public abstract void move(Point newPos);
	
	

}
