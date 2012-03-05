package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import uk.ac.aber.dcs.cs124group.undo.ExistenceEdit;
import uk.ac.aber.dcs.cs124group.undo.RelationshipStateEdit;
import uk.ac.aber.dcs.cs124group.view.*;

public class Relationship extends DocumentElementModel implements Observer, Cloneable {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type = RelationshipType.USES;
	private ClassModel goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private RelationshipLabel label;
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public Relationship(ClassModel from, ClassModel to) {
		this.goingFrom = from;
		this.goingTo = to;
		
		RelationshipEndPoint start = new RelationshipEndPoint(goingFrom.getLocation(), new Rectangle(goingFrom.getLocation(), goingFrom.getSize()), this);
		RelationshipEndPoint end = new RelationshipEndPoint(goingTo.getLocation(), new Rectangle(goingTo.getLocation(), goingTo.getSize()), this);
		
		points.add(start);
		points.add(end);
		
		if(this.label != null)
			this.label.addUndoableEditListener(this.getUndoableEditListener());
	}
	
	@Override
	public RelationshipArrow getView() {
		return new RelationshipArrow(this);
	}
	
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
	
	public void deletePoint(Point p) {
		for(int i = 0; i < this.points.size(); i++) {
			if (p.equals(points.get(i))) {
				points.remove(i);
				this.setChanged();
				this.notifyObservers("pointRemoved");
			}
		}
	}
	
	public Point getLabelReferencePoint() {
		int i = points.size() / 2;
		Point p1 = points.get(i - 1);
		Point p2 = points.get(i);
		
		Point p = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
		return p;
	}

	public RelationshipType getType() {
		return type;
	}



	public void setType(RelationshipType type) {
		this.type = type;
		this.setChanged();
		this.notifyObservers("typeChanged");
	}



	public ClassModel getGoingFrom() {
		return goingFrom;
	}


	public ClassModel getGoingTo() {
		return goingTo;
	}
	
	public void requestCardinality(String whichOne) {
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


	public Cardinality getCardinalityFrom() {
		return cardinalityFrom;
	}



	public void setCardinalityFrom(Cardinality cardinalityFrom) {
		this.cardinalityFrom = cardinalityFrom;
		
		ExistenceEdit edit = new ExistenceEdit(cardinalityFrom, true, "Cardinality created");
		this.fireUndoableEvent(edit);
	}



	public Cardinality getCardinalityTo() {
		return cardinalityTo;
	}



	public void setCardinalityTo(Cardinality cardinalityTo) {
		this.cardinalityTo = cardinalityTo;
		
		ExistenceEdit edit = new ExistenceEdit(cardinalityTo, true, "Cardinality created");
		this.fireUndoableEvent(edit);
	}
	
	public void setInverted() {
		ClassModel tmp = this.goingTo;
		this.goingTo = this.goingFrom;
		this.goingFrom = tmp;
		
		Collections.reverse(this.points);
		this.realignCardinalities();
		setChanged();
		this.notifyObservers("wasInverted");
	}



	public RelationshipLabel getLabel() {
		return label;
	}

	public void requestLabel() {
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

	public void addLabel(RelationshipLabel label) {
		this.label = label;
		ExistenceEdit edit = new ExistenceEdit(label, true);
		this.fireUndoableEvent(edit);
	}
	
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	
	@Override
	public void remove() {
		goingFrom.deleteObserver(this);
		goingTo.deleteObserver(this);
		super.remove();
	}
	
	@Override
	public void resurrect() {
		if(this.goingFrom.exists() && this.goingTo.exists()) {
			goingFrom.addObserver(this);
			goingTo.addObserver(this);
			super.resurrect();
		}
	}
	
	public void pointMoved() {
		this.setChanged();
		this.realignCardinalities();
		this.notifyObservers("pointMoved");
	}

	

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
			this.realignCardinalities();
		}
		else if(s.equals("wasRemoved")) {
			this.remove();
		}
		
	}
	
	public void realignCardinalities() {
		if(this.cardinalityFrom != null) this.cardinalityFrom.realign();
		if(this.cardinalityTo != null) this.cardinalityTo.realign();
		if(this.label != null) this.label.realign();
	}
	
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
	
	public void storeInEdit() {
		RelationshipStateEdit edit = new RelationshipStateEdit(this.clone(), this);
		this.fireUndoableEvent(edit);
	}
	
	public void restoreFromPrevious(Relationship previous) {
		this.points = previous.points;
		this.goingFrom = previous.goingFrom;
		this.goingTo = previous.goingTo;
		this.type = previous.type;
		this.realignCardinalities();
		
		this.setChanged();
		this.notifyObservers("restored");
	}
	
	


}
