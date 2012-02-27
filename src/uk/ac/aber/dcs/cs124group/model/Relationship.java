package uk.ac.aber.dcs.cs124group.model;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import uk.ac.aber.dcs.cs124group.view.ClassRectangle;
import uk.ac.aber.dcs.cs124group.view.LabelView;
import uk.ac.aber.dcs.cs124group.view.ManuallyDrawnElement;

public class Relationship extends DocumentElementModel implements ManuallyDrawnElement {

	
	private static final long serialVersionUID = 272724938449188987L;
	
	private RelationshipType type;
	private ClassModel goingFrom, goingTo;
	private Cardinality cardinalityFrom, cardinalityTo;
	private TextLabelModel label;
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public Relationship(ClassModel from, ClassModel to) {
		this.goingFrom = from;
		this.goingTo = to;
		
		//Work out points
		Point fromPoint = this.workOutOptimalEndPoints()[0];
		Point toPoint = this.workOutOptimalEndPoints()[1];
		
		points.add(fromPoint);
		points.add(toPoint);
		
		
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
	
	private Point[] workOutOptimalEndPoints() {
		Point fromPoint = new Point(0,0);
		Point toPoint = new Point(0,0);
		
		int horizontalDifference = goingTo.getLocation().x - goingFrom.getLocation().x;
		int verticalDifference = goingTo.getLocation().y - goingFrom.getLocation().y;
		
		//If below
			//If verticalDifference less than goingFrom height
				//If to the right EAST -> WEST
				//If to the left WEST -> EAST
			//If verticalDifference more than goingFrom height
				//If to the right
					//If horizontalDifference more than goingFrom width SOUTH -> WEST
					//If horizontalDifference less than goingFrom width SOUTH -> NORTH
				//If to the left
					//If horDif more than goingFrom width SOUTH -> EAST
					//If horDif less than
					
	
		
		
		
		return new Point[] {fromPoint, toPoint}; 
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}


	@Override
	public void draw(Graphics2D g, Component parent) {
		// TODO Auto-generated method stub
		
	}


}
