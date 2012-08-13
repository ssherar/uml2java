package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.util.Observable;
import java.awt.*;

import uk.ac.aber.dcs.cs124group.view.Zoom;

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

	private Zoom zoom;
	
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
	

	public Zoom getZoom() {
		return zoom;
	}
	
	public void setZoom(Zoom zoom) {
		this.zoom = zoom;
	}

	/**
	 * @return The default size of the canvas for this document.
	 */
	public Dimension getCanvasDefaultSize() {
		return canvasDefaultSize;
	}

}
