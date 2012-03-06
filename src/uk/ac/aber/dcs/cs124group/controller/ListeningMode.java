package uk.ac.aber.dcs.cs124group.controller;

/**
 * Includes possible states in which a DiagramListener may currently be. 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 * 
 * @see DiagramListener
 * @see Manager
 */
public enum ListeningMode {
	LISTEN_TO_ALL,
	PLACING_CLASS,
	PLACING_RELATIONSHIP,
	PLACING_TEXT,
	EDITING_TEXT,
	DRAGGING;
}
