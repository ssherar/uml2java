package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint;
import uk.ac.aber.dcs.cs124group.view.Vector2D;

/**
 * Being able to add cardinalities (as a form of the a
 * {@link TextLabelModel}) to a {@link uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint RelationshipEndPoint}.
 * <p> Whatever you place into this textfield will be validated by the
 * exporter and then data will be extracted so we can add the links in
 * the code.
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version v1.0.0
 *
 */

public class Cardinality extends TextLabelModel {
	/**
	 * The Location of the Relationship End Point.
	 * @see uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint RelationshipEndPoint
	 * 
	 */
	private RelationshipEndPoint point;
	
	/**
	 * The vector defining the relative position of this label to the {@link Cardinality#point point}
	 */
	private Vector2D toPoint;
	
	/**
	 * Set up the cardinality with some default values
	 * @param p 	The end point of the relationship.
	 */
	public Cardinality(RelationshipEndPoint p) {
		super(p);
		this.point = p;
		this.setText("0..*", false);
		
		this.toPoint = new Vector2D(0, 0);
	}
	
	/**
	 * Set the location for the cardinality. However the movement is constrained to a 50px
	 * radius around the end point.
	 * @see TextLabelModel#setLocation()
	 * @param p				The new point of the cardinality
	 * @param undoable		<code>True</code> if we want to create a record in the UndoMaager. <code>false</code> otherwise
	 */
	@Override
	public void setLocation(Point p, boolean undoable) {
		if(p.distance(point) < 50) {
			super.setLocation(p, undoable);
			this.toPoint = new Vector2D(p.x - point.x, p.y - point.y);
		}
	}
	
	
	/**
	 * Realign the cardinality to the relationship's end point based on the
	 * {@link Cardinality#toPoint vector}.
	 * 
	 */
	public void realign() {
		this.setLocation(new Point(point.x + toPoint.x, point.y + toPoint.y), false);
	}
}
