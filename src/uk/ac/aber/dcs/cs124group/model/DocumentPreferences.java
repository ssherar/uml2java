package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.util.Observable;
import java.awt.*;

public class DocumentPreferences extends Observable implements Serializable {

	/**
	 * Serialised ID generated from the "serializable" interface.
	 */
	private static final long serialVersionUID = 8524925453344749559L;
	
	private Font font;
	private Dimension canvasDefaultSize;
	private double zoomLevel = 1;
	private String filename;
		

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFont(Font f) {
		font = f;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setCanvasDefaultSize(Dimension newSize) {
		canvasDefaultSize = newSize;
	}
	
	
	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}


	public Dimension getCanvasDefaultSize() {
		return canvasDefaultSize;
	}

}
