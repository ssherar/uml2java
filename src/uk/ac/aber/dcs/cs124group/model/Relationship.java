package uk.ac.aber.dcs.cs124group.model;

import java.awt.Point;

public class Relationship extends DocumentElement {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type;
	private ClassRectangle goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private TextLabel label;
	
	public Relationship(ClassRectangle from, ClassRectangle to) {
		this.goingFrom = from;
		this.goingTo = to;
	}
	
	

	@Override
	public void move(Point newPos) {
		

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



	public TextLabel getLabel() {
		return label;
	}



	public void setLabel(TextLabel label) {
		this.label = label;
	}
	

}
