package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;
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
		
		RelationshipEndPoint start = new RelationshipEndPoint(new Point(goingFrom.getLocation().x + 10, goingFrom.getLocation().y), new Rectangle(goingFrom.getLocation(), goingFrom.getSize()));
		RelationshipEndPoint end = new RelationshipEndPoint(goingTo.getLocation(), new Rectangle(goingTo.getLocation(), goingTo.getSize()));
		
		points.add(start);
		points.add(end);
		
		
	}
	
	@Override
	public RelationshipArrow getView() {
		return new RelationshipArrow(this);
	}
	
	public void addPoint(Point p) {
		//TODO: Work out the order of points... convex hull?
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


	public Cardinality getCardinalityFrom() {
		return cardinalityFrom;
	}



	public void setCardinalityFrom(Cardinality cardinalityFrom) {
		this.cardinalityFrom = cardinalityFrom;
	}



	public Cardinality getCardinalityTo() {
		return cardinalityTo;
	}



	public void setCardinalityTo(Cardinality cardinalityTo) {
		this.cardinalityTo = cardinalityTo;
	}
	
	public void setInverted() {
		ClassModel tmp = this.goingTo;
		this.goingTo = this.goingFrom;
		this.goingFrom = tmp;
		
		
		Collections.reverse(this.points);
		setChanged();
		this.notifyObservers("wasInverted");
	}



	public RelationshipLabel getLabel() {
		return label;
	}



	public void setLabel(RelationshipLabel label) {
		this.label = label;
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
		}
		else if(s.equals("wasRemoved")) {
			this.remove();
		}
		
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
		
		this.setChanged();
		this.notifyObservers("restored");
	}
	


}
