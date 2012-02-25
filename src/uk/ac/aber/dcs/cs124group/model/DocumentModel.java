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
	
	public DocumentModel() {
		preferences = new DocumentPreferences();
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
	
	public void removeElement(DocumentElement r) {
		elements.remove(r);
	}
	
	public void cleanUp() {
		for(int i = 0; i < elements.size(); i++) {
			if(!elements.get(i).isVisible()) {
				elements.remove(i);
			}
		}
	}

}
