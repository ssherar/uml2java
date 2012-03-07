package uk.ac.aber.dcs.cs124group.model;

/**
 * Defines different states a diagram element may currently be in.
 * Used widely by mouse event listeners.
 * 
 * @see DocumentElementModel#setPaintState(ElementPaintState)
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public enum ElementPaintState {
	DEFAULT,
	SELECTED,
	MOUSED_OVER,
	MOUSED_OVER_RESIZE;
}
