package uk.ac.aber.dcs.cs124group.model;

/**
 * Defines possible visibility modifiers for classes and attributes.
 * 
 * @see Attribute#getVisibility()
 * @see ClassModel#getVisibility()
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public enum IVisibility {
	PRIVATE,
	PROTECTED,
	PACKAGE,
	PUBLIC;
}
