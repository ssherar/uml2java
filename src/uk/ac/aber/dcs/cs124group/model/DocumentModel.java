package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.*;
import java.awt.Font;
import java.util.Observer;

public class DocumentModel implements java.io.Serializable {

	/**
	* Version ID for Object IO validation. 
	* Change this if new version will not be compatible with files saved under the old version.
	*/
	private static final long serialVersionUID = -7136489795698324976L;
	
	private DocumentPreferences preferences;
	private ArrayList<DocumentElementModel> elements = new ArrayList<DocumentElementModel>();
	
	private ArrayList<ArrayList<DocumentElementModel>> pastStates = new ArrayList<ArrayList<DocumentElementModel>>();
	private int undoIndex = 0;
	
	public DocumentModel() {
		preferences = new DocumentPreferences();
	}
	
	
	public DocumentPreferences getPreferences() {
		return preferences;
	}
	
	public ArrayList<DocumentElementModel> getElements() {
		return elements;
	}
	
	public void addElement(DocumentElementModel r) {
		elements.add(r);
	}
	
	public void removeElement(DocumentElementModel r) {
		elements.remove(r);
	}
	
	public void cleanUp() {
		for(int i = 0; i < elements.size(); i++) {
			if(!elements.get(i).exists()) {
				elements.remove(i);
			}
		}
	}
	

}
