package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

public class TextLabelModel extends DocumentElementModel {
	private String text = "New Label";
	private Point location;
	private Dimension size;
	private boolean editing = false;
	
	public TextLabelModel(Point p) {
		this.location = p;
		
	}
	
	public TextLabelModel(Point p, String text) {
		this.location = p;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.setChanged();
		this.notifyObservers("textChanged");
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
		this.setChanged();
		this.notifyObservers("locationChanged");
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
		this.setChanged();
		this.notifyObservers("sizeChanged");
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
		this.setChanged();
		this.notifyObservers("editingChanged");
	}
	
	
}