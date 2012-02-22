package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.awt.*;

public class DocumentPreferences implements Serializable {

	/**
	 * Serialised ID generated from the "serializable" interface.
	 */
	private static final long serialVersionUID = 8524925453344749559L;
	
	private Font font;
	private Dimension canvasDefaultSize;
	private double zoomLevel = 1;
		

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
		// TODO Auto-generated method stub
		return canvasDefaultSize;
	}

}
