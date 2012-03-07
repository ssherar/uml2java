package uk.ac.aber.dcs.cs124group.model;

import java.util.ArrayList;

/**
 * The global container for all DocumentElementModels and the DocumentPreferences object.
 * Holds all the relevant data about a given document.
 * Exists mainly to facilitate binary IO and code export.
 * 
 * @see DocumentElementModel
 * @see DocumentPreferences
 * @see uk.ac.aber.dcs.cs124group.controller.Manager Manager
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class DocumentModel implements java.io.Serializable {

	/**
	* Version ID for Object IO validation. 
	* Change this if new version will not be compatible with files saved under the old version.
	*/
	private static final long serialVersionUID = -7136489795698324976L;
	
	/**
	 * The DocumentPreferences object associated with this DocumentModel.
	 */
	private DocumentPreferences preferences;
	
	/**
	 * An ArrayList holding all top-level elements (ClassModels, Relationships, TextLabelModels) in this document.
	 */
	private ArrayList<DocumentElementModel> elements = new ArrayList<DocumentElementModel>();
	
	
	/**
	 * Constructs a new empty DocumentModel and associates a new DocumentPreferences.
	 * object with it.
	 */
	public DocumentModel() {
		preferences = new DocumentPreferences();
	}
	
	/**
	 * @return The DocumentPreferences object associated with this DocumentModel.
	 */
	public DocumentPreferences getPreferences() {
		return preferences;
	}
	
	/**
	 * @return All top-level elements (ClassModels, Relationships, TextLabelModels) in this DocumentModel.
	 * @see DocumentElementModel
	 */
	public ArrayList<DocumentElementModel> getElements() {
		return elements;
	}
	
	/**
	 * Adds a new element to this DocumentModel
	 * 
	 * @param r The DocumentElementModel to be added.
	 * @see DocumentElementModel
	 */
	public void addElement(DocumentElementModel r) {
		elements.add(r);
	}
	
	/**
	 * Removes an element from this DocumentModel irrevocably.
	 * 
	 * @param r The DocumentElementModel to be removed.
	 * @see DocumentElementModel
	 * @see DocumentElementModel#exists()
	 */
	public void removeElement(DocumentElementModel r) {
		elements.remove(r);
	}
	
	/**
	 * Removes all non-existent top-level elements from this DocumentModel
	 * and calls cleanUp() on every single element. 
	 * <p>
	 * CAUTION: Calling this method from a point after which ExistenceEdits
	 * may still be undone or redone can lead to unpredictable behaviour. Make sure
	 * that the undo stack is either empty or is emptied straight after this method is
	 * called.
	 * 
	 * @see DocumentElementModel#exists()
	 * @see DocumentElementModel#cleanUp()
	 * @see uk.ac.aber.dcs.cs124group.controller.Manager#discardAllEdits() Manager.discardAllEdits()
	 * @see uk.ac.aber.dcs.cs124group.undo.ExistenceEdit ExistenceEdit
	 */
	public void cleanUp() {
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).cleanUp();
			if(!elements.get(i).exists()) {
				elements.remove(i);
				i--;
			}
		}
	}
	

}
