package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.util.*;
import javax.swing.undo.*;
import uk.ac.aber.dcs.cs124group.view.*;

public class Relationship extends DocumentElementModel implements Observer {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type = RelationshipType.USES;
	private ClassModel goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private TextLabelModel label;
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public Relationship(ClassModel from, ClassModel to) {
		this.goingFrom = from;
		this.goingTo = to;
		
		points.add(goingFrom.getLocation());
		points.add(goingTo.getLocation());
		
		
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

	

	@Override
	public void update(Observable o, Object s) {
		// TODO Auto-generated method stub
		
	}


}
