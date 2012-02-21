package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.awt.*;

public class DocumentPreferences implements Serializable {

	/**
	 * Serialised ID generated from the "serializable" interface.
	 */
	private static final long serialVersionUID = 8524925453344749559L;
	
	private Font font;
	private int fontSize;
	private Dimension canvasDefaultSize;
	
	//TODO define no-arg constructor setting predefined defaults
	

	public void setFont(Font f) {
		font = f;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setCanvasDefaultSize(Dimension newSize) {
		canvasDefaultSize = newSize;
	}
	
	public int getCanvasDefaultWidth() {
		return canvasDefaultSize.width;
	}
	
	public int getCanvasDefaultHeight() {
		return canvasDefaultSize.height;
	}

}
