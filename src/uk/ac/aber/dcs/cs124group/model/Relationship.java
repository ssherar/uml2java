package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;
import java.util.ArrayList;

import uk.ac.aber.dcs.cs124group.view.ClassRectangle;
import uk.ac.aber.dcs.cs124group.view.LabelView;

public class Relationship extends DocumentElementModel {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type;
	private ClassRectangle goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private LabelView label;
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public Relationship(ClassRectangle from, ClassRectangle to) {
		this.goingFrom = from;
		this.goingTo = to;
	}
	
	


	public RelationshipType getType() {
		return type;
	}



	public void setType(RelationshipType type) {
		this.type = type;
	}



	public ClassRectangle getGoingFrom() {
		return goingFrom;
	}



	public void setGoingFrom(ClassRectangle goingFrom) {
		this.goingFrom = goingFrom;
	}



	public ClassRectangle getGoingTo() {
		return goingTo;
	}



	public void setGoingTo(ClassRectangle goingTo) {
		this.goingTo = goingTo;
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



	public LabelView getLabel() {
		return label;
	}



	public void setLabel(LabelView label) {
		this.label = label;
	}
	


	

}
