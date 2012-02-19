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
	

	public void setFont(Font f) {
		font = f;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFontSize(int s) {
		fontSize = s;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setCanvasDefaultSize(int width, int height) {
		canvasDefaultSize.setSize(width, height);
	}
	
	public int getCanvasDefaultWidth() {
		return canvasDefaultSize.width;
	}
	
	public int getCanvasDefaultHeight() {
		return canvasDefaultSize.height;
	}

}
