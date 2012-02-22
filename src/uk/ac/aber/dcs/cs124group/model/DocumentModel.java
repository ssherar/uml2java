package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.Font;

public class DocumentModel implements java.io.Serializable {

	/**
	* Version ID for Object IO validation. 
	* Change this if new version will not be compatible with files saved under the old version.
	*/
	private static final long serialVersionUID = -7136489795698324976L;
	
	private DocumentPreferences preferences;
	private ArrayList<DocumentElement> elements = new ArrayList<DocumentElement>();
	private String fileName;
	
	public DocumentModel() {
		preferences = new DocumentPreferences();
	}
	
	public DocumentModel(String filename) {
		this.fileName = fileName;
		preferences = new DocumentPreferences();
	}
	
	public void setFileName(String filename) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public DocumentPreferences getPreferences() {
		return preferences;
	}
	
	public ArrayList<DocumentElement> getElements() {
		return elements;
	}
	
	public void addElement(DocumentElement r) {
		elements.add(r);
	}
	
	public void removeClass(DocumentElement r) {
		elements.remove(r);
	}
	
	public void removeUnlinkedElements() {
		//TODO define purge
	}

}
