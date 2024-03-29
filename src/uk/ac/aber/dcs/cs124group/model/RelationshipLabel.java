package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import java.util.*;

import uk.ac.aber.dcs.cs124group.view.Vector2D;

/**
 * The label that is being placed on Relationships.
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class RelationshipLabel extends TextLabelModel {
	
	/** The Relationship this label is attached to. */
	private Relationship associatedRelationship;
	
	/**
	 * The vector defining the relative position of this label 
	 * to the label reference point.
	 * 
	 * @see Relationship#getLabelReferencePoint()
	 * @see uk.ac.aber.dcs.cs124group.view.Vector2D Vector2D
	 */
	private Vector2D toReferencePoint;
	
	/**
	 * Constructs a new RelationshipLabel attached to the specified Relationship.
	 * 
	 * @param p The point at which the label is to be located.
	 * @param r The associated relationship.
	 */
	public RelationshipLabel(Point p, Relationship r) {
		super(p);
		this.associatedRelationship = r;
		this.toReferencePoint = new Vector2D(0,0);
	}
	
	/**
	 * Overridden to ensure that the centre of the label is never moved further
	 * than 50px away from a segment belonging to the associated relationship.
	 */
	@Override
	public void setLocation(Point p, boolean undoable) {
		
		Point newCentre = new Point(p.x + this.getSize().width / 2, p.y + this.getSize().height / 2);
		ArrayList<Point> linePoints = associatedRelationship.getPoints();
		
		
		for(int i = 1; i < linePoints.size(); i++) {
			Point p1 = linePoints.get(i - 1);
			Point p2 = linePoints.get(i);
			
			Vector2D cVector = new Vector2D(newCentre.x - p1.x, newCentre.y - p1.y);
			Vector2D segmentVector = new Vector2D(p2.x - p1.x, p2.y - p1.y);
			Vector2D projection = cVector.projection(segmentVector);
			
			if(segmentVector.getColinearityFactor(projection) < 1.001 && segmentVector.getColinearityFactor(projection) > 0)  {
				double distanceToSegment = newCentre.distance(p1.x + projection.x, p1.y + projection.y);
				if (distanceToSegment < 50) {
					super.setLocation(p, undoable);
					this.toReferencePoint = new Vector2D(-associatedRelationship.getLabelReferencePoint().x + p.x, 
													     -associatedRelationship.getLabelReferencePoint().y + p.y);
					return;
				}
			}
		}
		
		
	}
	
	/**
	 * @return The Attribute the associated relationship represents, if there is one.
	 */
	public Attribute getAttribute() {
		Attribute a = new Attribute(new Point(0,0), this.getText(), AttributeType.DATA_FIELD);
		if(this.getText().charAt(0) == '-')
			a.setVisibility(IVisibility.PRIVATE);
		else if(this.getText().charAt(0) == '+')
			a.setVisibility(IVisibility.PUBLIC);
		else if(this.getText().charAt(0) == '#')
			a.setVisibility(IVisibility.PROTECTED);
		else a.setVisibility(IVisibility.PACKAGE);
		
		if(this.associatedRelationship.getType() == RelationshipType.USES) {
			a.setAttributeType(associatedRelationship.getGoingFrom().getClassName());
		}
		else a.setAttributeType(associatedRelationship.getGoingTo().getClassName());
		
		a.setAttributeName(this.getText().substring(0,1).matches("[#+-]") ? this.getText().substring(1).trim() : this.getText().trim());
			
		return a;
	}	
	
	/**
	 * Sets the location of this RelationshipLabel based on the {@link #toReferencePoint} vector. 
	 */
	public void realign() {
		super.setLocation(new Point(associatedRelationship.getLabelReferencePoint().x + toReferencePoint.x, 
									associatedRelationship.getLabelReferencePoint().y + toReferencePoint.y), false);
	}
	

}
