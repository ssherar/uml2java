package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;
import uk.ac.aber.dcs.cs124group.view.*;

public class Relationship extends DocumentElementModel implements Observer {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type = RelationshipType.AGGREGATION;
	private ClassModel goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private TextLabelModel label;
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public Relationship(ClassModel from, ClassModel to) {
		this.goingFrom = from;
		this.goingTo = to;
		
		RelationshipEndPoint start = new RelationshipEndPoint(goingFrom.getLocation(), new Rectangle(goingFrom.getLocation(), goingFrom.getSize()));
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



	public TextLabelModel getLabel() {
		return label;
	}



	public void setLabel(TextLabelModel label) {
		this.label = label;
	}
	
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	private void updateWrapAroundEndPoints() {
		
	}
	
	@Override
	public void remove() {
		goingFrom.removeRelationship(this);
		goingTo.removeRelationship(this);
		super.remove();
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


}
