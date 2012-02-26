package uk.ac.aber.dcs.cs124group.controller;

/**
 * Includes possible states in which a DiagramListener may currently be. 
 * @authors Daniel Maly, Sam Sherar, Lee Smith
 */
public enum ListeningMode {
	LISTEN_TO_ALL,
	PLACING_CLASS,
	PLACING_RELATIONSHIP,
	PLACING_TEXT,
	EDITING_TEXT,
	DRAGGING;
}
