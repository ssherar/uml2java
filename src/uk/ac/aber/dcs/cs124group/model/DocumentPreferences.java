package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.util.Observable;
import java.awt.*;

/**
 * A data-holder class that saves user-specified settings for 
 * a DocumentModel.
 * 
 * @see DocumentModel
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smih
 * @version 1.0.0
 */
public class DocumentPreferences extends Observable implements Serializable {

	/**
	 * Serialised ID generated from the "serializable" interface.
	 */
	private static final long serialVersionUID = 8524925453344749559L;
	
	/** The global font set on the document. */
	private Font font;
	
	/** The canvas size for the document. */
	private Dimension canvasDefaultSize;
	
	/** 
	 * @deprecated v0.9
	 */
	private double zoomLevel = 1;
	
	/** The filename associated with saving this document. */
	private String filename;
		
    /**
     * @return The filename last used when saving the document.
     */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename for the document.
	 * 
	 * @param filename
	 * 		The filename to be set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Sets the global font on the document and notifies any observers.
	 * 
	 * @param f
	 * 		The font to be set.
	 */
	public void setFont(Font f) {
		font = f;		
		this.setChanged();
		this.notifyObservers("fontChanged");
	}
	
	/**
	 * @return The current font.
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Sets a new default canvas size on the document.
	 * 
	 * @param newSize
	 * 		The new canvas size to be set.
	 */
	public void setCanvasDefaultSize(Dimension newSize) {
		canvasDefaultSize = newSize;
	}
	
	/**
	 * @deprecated v0.9
	 * @return The current zoom level
	 */
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	/**
	 * @deprecated v0.9
	 * @param zoomLevel
	 * 		The zoom level to be set.
	 */
	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * @return The default size of the canvas for this document.
	 */
	public Dimension getCanvasDefaultSize() {
		return canvasDefaultSize;
	}

}
