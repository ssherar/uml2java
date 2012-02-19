package uk.ac.aber.dcs.cs124group.model;

import java.io.*;
import java.awt.*;

public class DocumentPreferences implements Serializable {

	/**
	 * Serialised ID generated from the "serializable" interface.
	 */
	private static final long serialVersionUID = 8524925453344749559L;
	
	private String preferenceFile;
	private Font font;
	private int fontSize;
	private Dimension canvasDefaultSize;
	
	public void DocumentPreferences() { }
	
	public void DocumentPreferences(String fileName) {
		this.preferenceFile = fileName;
	}

}
