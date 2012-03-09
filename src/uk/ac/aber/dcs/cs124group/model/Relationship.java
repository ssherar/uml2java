package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import uk.ac.aber.dcs.cs124group.undo.ExistenceEdit;
import uk.ac.aber.dcs.cs124group.undo.RelationshipStateEdit;
import uk.ac.aber.dcs.cs124group.view.*;

public class Relationship extends DocumentElementModel implements Observer, Cloneable {

	/** Serialization version ID */
	private static final long serialVersionUID = 272724938449188987L;
	
	/**
	 * The type of this Relationship
	 * @see RelationshipType
	 * @see uk.ac.aber.dcs.cs124group.view.RelationshipArrow#paintComponent(java.awt.Graphics) RelationshipArrow.paintComponent(Graphics)
	 */
	private RelationshipType type = RelationshipType.USES;
	
	/** The two classes this Relationship joins. */
	private ClassModel goingFrom, goingTo;
	
	/** Cardinalities of this Relationship. */
	private Cardinality cardinalityFrom, cardinalityTo;
	
	/** An optional label on the relationship arrow. */
	private RelationshipLabel label;
	
	/** 
	 * An ArrayList holding user-defined points the relationship passes through on the diagram.
	 * @see uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint RelationshipEndPoint
	 */
	private ArrayList<Point> points = new ArrayList<Point>();
	
	/**
	 * If this Relationship was removed along with a ClassModel, stores the ClassModel here.
	 */
	private ClassModel destinyBond;
	
	/**
	 * Constructs a new Relationship joining the two specified classes.
	 * 
	 * @param from
	 * 		The class at whose end the relationship arrowhead should be drawn.
	 * 
	 * @param to
	 * 		The second class joined by this relationship.
	 */		
	public Relationship(ClassModel from, ClassModel to) {
		this.goingFrom = from;
		this.goingTo = to;
		
		RelationshipEndPoint start = new RelationshipEndPoint(goingFrom.getLocation(), new Rectangle(goingFrom.getLocation(), goingFrom.getSize()), this);
		RelationshipEndPoint end = new RelationshipEndPoint(goingTo.getLocation(), new Rectangle(goingTo.getLocation(), goingTo.getSize()), this);
		
		points.add(start);
		points.add(end);
	}
	
	@Override
	/* See superclass */
	public RelationshipArrow getView() {
		return new RelationshipArrow(this);
	}
	
	/**
	 * Attempts to add a new point to this relationship.
	 * For each existing segment, uses vectors to check whether the new
	 * point would lie on the given segment. If yes, adds the point 
	 * at the appropriate index.
	 * 
	 * @see uk.ac.aber.dcs.cs124group.view.Vector2D Vector2D
	 * @see uk.ac.aber.dcs.cs124group.controller.RelationshipController#mouseDragged(java.awt.event.MouseEvent) Relationshipcontroller.mouseDragged(MouseEvent)
	 * 
	 * @param p The point to try and add to the relationship.
	 * @return The added point
	 */
	public Point addPoint(Point p) {
		for(int i = 0; i < this.points.size() - 1; i++) {
			Point p1 = this.points.get(i);
			Point p2 = this.points.get(i + 1);
			
			Vector2D segmentVector = new Vector2D(p2.x - p1.x, p2.y - p1.y);
			Vector2D newSegmentVector = new Vector2D(p.x - p1.x, p.y - p1.y);
			
			if(segmentVector.colinear(newSegmentVector)) {
				this.points.add(i + 1, p);
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Attempts to remove the specified point from this relationship.
	 * @param p The point to be removed.
	 */
	public void deletePoint(Point p) {
		for(int i = 0; i < this.points.size(); i++) {
			if (p.equals(points.get(i))) {
				points.remove(i);
				this.setChanged();
				this.notifyObservers("pointRemoved");
			}
		}
	}
	
	/**
	 * Returns the midpoint of the middle segment of this relationship.
	 * If the relationship has an even number of segments, returns the midpoint
	 * of the one that is closer to the arrowhead. 
	 * 
	 * @see RelationshipLabel#realign()
	 * 
	 * @return The label reference point.
	 */
	public Point getLabelReferencePoint() {
		int i = points.size() / 2;
		Point p1 = points.get(i - 1);
		Point p2 = points.get(i);
		
		Point p = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
		return p;
	}

	/**
	 * @return The RelationshipType of this relationship.
	 */
	public RelationshipType getType() {
		return type;
	}

	
	/**
	 * Changes the relationship type to the one specified by the argument
	 * and notifies observers.
	 * 
	 * @param type The new RelationshipType for this relationship.
	 */
	public void setType(RelationshipType type) {
		this.type = type;
		this.setChanged();
		this.notifyObservers("typeChanged");
	}


	/**
	 * @return The ClassModel at the arrowhead end of the relationship.
	 */
	public ClassModel getGoingFrom() {
		return goingFrom;
	}

	/**
	 * @return The ClassModel at the plain end of the relationship.
	 */
	public ClassModel getGoingTo() {
		return goingTo;
	}
	
	/**
	 * Requests a new Cardinality to be added to this relationship by the view.
	 * NOTE: Does nothing if this is an INHERITANCE or IMPLEMENTS relationship.
	 * If the specified cardinality is already present, simply enables editing on it.
	 * If the specified cardinality is present but does not exist, resurrects it.
	 * 
	 * @see DocumentElementModel#exists()
	 * 
	 * @param whichOne
	 * 			The relationship end of the requested cardinality.
	 */
	public void requestCardinality(String whichOne) {
		if(this.type == RelationshipType.INHERITANCE || this.type == RelationshipType.IMPLEMENTS)
			return;		
		
		if(whichOne.equals("from") && this.cardinalityFrom != null) {
			if(!this.cardinalityFrom.exists())
				this.cardinalityFrom.resurrect();
			this.cardinalityFrom.setEditing(true);
		}
		else if(whichOne.equals("to") && this.cardinalityTo != null) {
			if(!this.cardinalityTo.exists())
				this.cardinalityTo.resurrect();
			this.cardinalityTo.setEditing(true);
		}
		else {
			this.setChanged();
			this.notifyObservers("cardinalityRequested" + whichOne);
		}
	}


	/**
	 * @return The Cardinality at the arrowhead end of this relationship.
	 */
	public Cardinality getCardinalityFrom() {
		return cardinalityFrom;
	}



	/**
	 * Adds a cardinality to this relationship at the arrowhead end.
	 * @param cardinalityFrom
	 * 		The Cardinality to be added.
	 */
	public void setCardinalityFrom(Cardinality cardinalityFrom) {
		this.cardinalityFrom = cardinalityFrom;
		
		ExistenceEdit edit = new ExistenceEdit(cardinalityFrom, true, "Cardinality created");
		this.fireUndoableEvent(edit);
	}


	/**
	 * @return The Cardinality at the plain end of this relationship.
	 */
	public Cardinality getCardinalityTo() {
		return cardinalityTo;
	}


	/**
	 * Adds a cardinality to this relationship at the plain end.
	 * @param cardinalityTo
	 * 		The Cardinality to be added.
	 */
	public void setCardinalityTo(Cardinality cardinalityTo) {
		this.cardinalityTo = cardinalityTo;
		
		ExistenceEdit edit = new ExistenceEdit(cardinalityTo, true, "Cardinality created");
		this.fireUndoableEvent(edit);
	}
	
	/**
	 * Inverts this relationship and notifies observers.
	 */
	public void setInverted() {
		ClassModel tmp = this.goingTo;
		this.goingTo = this.goingFrom;
		this.goingFrom = tmp;
		
		Collections.reverse(this.points);
		this.realignSubelements();
		setChanged();
		this.notifyObservers("wasInverted");
	}


	/**
	 * @return The label associated to this relationship.
	 */
	public RelationshipLabel getLabel() {
		return label;
	}

	/**
	 * Requests a new RelationshipLabel to be added to this relationship by the view.
	 * NOTE: Does nothing if this is an INHERITANCE or IMPLEMENTS relationship.
	 * If the label is already present, simply enables editing on it.
	 * If the label is present but does not exist, resurrects it.
	 * 
	 * @see DocumentElementModel#exists()
	 */
	public void requestLabel() {
		if(this.type == RelationshipType.INHERITANCE || this.type == RelationshipType.IMPLEMENTS)
			return;	
		
		if(this.label != null) {
			if(!this.label.exists())
				this.label.resurrect();
			this.label.setEditing(true);
		}
		else {
			this.setChanged();
			this.notifyObservers("labelRequested");
		}
	}

	/**
	 * Adds the specified label to this relationship.
	 * @param label
	 * 		The label to be added.
	 */
	public void addLabel(RelationshipLabel label) {
		this.label = label;
		ExistenceEdit edit = new ExistenceEdit(label, true);
		this.fireUndoableEvent(edit);
	}
	
	/**
	 * @return The ArrayList of user-defined points on this relationship.
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	
	@Override
	/** Deletes observers from this relationship and calls the superclass remove() method */
	public void remove() {
		goingFrom.deleteObserver(this);
		goingTo.deleteObserver(this);
		super.remove();
	}
	
	@Override
	/** Resurrects this relationship but only if both endpoint classes exist. */
	public void resurrect() {
		if(this.goingFrom.exists() && this.goingTo.exists()) {
			goingFrom.addObserver(this);
			goingTo.addObserver(this);
			super.resurrect();
		}
	}
	
	/**
	 * Notifies observers that a point on this relationship has been moved.
	 * 
	 * @see #realignSubelements()
	 */
	public void pointMoved() {
		this.setChanged();
		this.realignSubelements();
		this.notifyObservers("pointMoved");
	}

	

	
	/** 
	 * Called whenever an endpoint class is moved or resized, updates the locations of endpoints.
	 * @see uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint#updateTo(Rectangle) RelationshipEndPoint.updateTo(Rectangle)
	 * @see #realignSubelements()
	 */
	@Override
	public void update(Observable o, Object s) {
		if(!(s instanceof String)) {
			throw new IllegalArgumentException("String expected");
		}
		else if(s.equals("locationChanged") || s.equals("sizeChanged")) {
			if(((ClassModel) o).equals(this.goingFrom)) {
				((RelationshipEndPoint)this.points.get(0)).updateTo(
						new Rectangle(this.goingFrom.getLocation(), this.goingFrom.getSize()));
				
				this.setChanged();
				this.notifyObservers("startPointChanged");
			}
			else {
				((RelationshipEndPoint)this.points.get(this.points.size() - 1)).updateTo(
						new Rectangle(this.goingTo.getLocation(), this.goingTo.getSize()));

				this.setChanged();
				this.notifyObservers("endPointChanged");
			}
			this.realignSubelements();
		}
		else if(s.equals("wasRemoved")) {
			this.destinyBond = (ClassModel) o;
			this.remove();
		}
		
	}
	
	/**
	 * Makes all subelements (label and cardinalities) realign themselves to their
	 * respective reference points.
	 * 
	 * @see Cardinality#realign()
	 * @see RelationshipLabel#realign()
	 */
	public void realignSubelements() {
		if(this.cardinalityFrom != null) this.cardinalityFrom.realign();
		if(this.cardinalityTo != null) this.cardinalityTo.realign();
		if(this.label != null) this.label.realign();
	}
	
	/**
	 * Clones this relationship and all of its user-defined points.
	 * @return The cloned Relationship.
	 */
	public Relationship clone() {
		try {
			Relationship storedState = (Relationship) super.clone();
			storedState.points = new ArrayList<Point> ();
			for(int i = 0; i < this.points.size(); i++) {
				storedState.points.add((Point) this.points.get(i).clone()); 
			}
			return storedState;
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	
	}
	
	/**
	 * Called from RelationshipController whenever the state
	 * of this relationship is about to change, creates a new
	 * RelationshipStateEdit from a clone of this object and sends
	 * it to the UndoableEditListener.
	 * 
	 * @see uk.ac.aber.dcs.cs124group.undo.RelationshipStateEdit RelationshipStateEdit
	 * @see DocumentElementModel#fireUndoableEvent(javax.swing.undo.UndoableEdit)
	 * @see #restoreFromPrevious(Relationship)
	 */
	public void storeInEdit() {
		RelationshipStateEdit edit = new RelationshipStateEdit(this.clone(), this);
		this.fireUndoableEvent(edit);
	}
	
	/**
	 * Restores every single data field in this Relationship from the one
	 * specified by the argument.
	 * 
	 * @see uk.ac.aber.dcs.cs124group.undo.RelationshipStateEdit RelationshipStateEdit
	 * 
	 * @param previous 
	 * 		The Relationship whose set of data fields is to be copied into this object.
	 */
	public void restoreFromPrevious(Relationship previous) {
		this.points = previous.points;
		this.goingFrom = previous.goingFrom;
		this.goingTo = previous.goingTo;
		this.type = previous.type;
		this.realignSubelements();
		
		this.setChanged();
		this.notifyObservers("restored");
	}
	
	/**
	 * @return The class this relationship was removed with, if any.
	 */
	public ClassModel getDestinyBond() {
		return destinyBond;
	}
	
	/**
	 * Removes non-existent subelements.
	 * @see DocumentModel#cleanUp()
	 * @see DocumentElementModel#exists()
	 */
	@Override
	public void cleanUp() {
		if(cardinalityFrom != null && !cardinalityFrom.exists())
			cardinalityFrom = null;
		
		if(cardinalityTo != null && !cardinalityTo.exists())
			cardinalityTo = null;
		
		if(label != null && !label.exists())
			label = null;
	}

}
