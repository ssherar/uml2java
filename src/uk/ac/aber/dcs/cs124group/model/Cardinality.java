package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint;
import uk.ac.aber.dcs.cs124group.view.Vector2D;


public class Cardinality extends TextLabelModel {
	
	private RelationshipEndPoint point;
	private Vector2D toPoint;
	
	public Cardinality(RelationshipEndPoint p) {
		super(p);
		this.point = p;
		this.setText("0..*", false);
		
		this.toPoint = new Vector2D(p.x, p.y);
	}
	
	@Override
	public void setLocation(Point p, boolean undoable) {
		if(p.distance(point) < 30) {
			super.setLocation(p, undoable);
			this.toPoint = new Vector2D(p.x - point.x, p.y - point.y);
		}
	}
	
	public void realign() {
		this.setLocation(new Point(point.x + toPoint.x, point.y + toPoint.y), false);
	}

	

}
