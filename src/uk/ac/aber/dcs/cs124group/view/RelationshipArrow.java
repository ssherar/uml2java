package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Observable;

import uk.ac.aber.dcs.cs124group.model.Relationship;
import uk.ac.aber.dcs.cs124group.model.RelationshipType;

public class RelationshipArrow extends DocumentElementView {
	
	private Relationship model;
	
	public RelationshipArrow(Relationship model) {
		this.model = model;
		this.setLocation(new Point(0,0));
		this.setPreferredSize(new Dimension(100000,100000));
		this.setOpaque(false);
	}
	
	@Override
	public boolean contains(Point p) {
		
		for(int j = 1; j < this.model.getPoints().size(); j++) {
			Point p1 = this.model.getPoints().get(j - 1);
			Point p2 = this.model.getPoints().get(j);
			
			Vector2D pVector = new Vector2D(p.x - p1.x, p.y - p1.y);
			Vector2D segmentVector = new Vector2D(p2.x - p1.x, p2.y - p1.y);
			Vector2D projection = pVector.projection(segmentVector);
			
			if(segmentVector.getColinearityFactor(projection) < 1.001 && segmentVector.getColinearityFactor(projection) > 0)  {
				double distanceToSegment = p.distance(p1.x + projection.x, p1.y + projection.y);
				return (distanceToSegment < 10);
			}

		}
		
		return false;
	}
	
	@Override
	public boolean contains(int x, int y) {		
		return this.contains(new Point(x,y));		
	}
	
	@Override
	public void update(Observable o, Object s) {
		if(!(s instanceof String)) {
			throw new IllegalArgumentException("String expected");
		}
		else if(s.equals("wasRemoved")) {
			this.setVisible(false);
			this.getParent().remove(this);
		}
		else this.repaint();

	}
	
	public void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		
		ArrayList<Point> points = this.model.getPoints();
		
		int[] xpoints = new int[points.size()];
		int[] ypoints = new int[points.size()];
		
		for(int i = 0; i < points.size(); i++) {
			xpoints[i] = points.get(i).x;
			ypoints[i] = points.get(i).y;
		}
		
		Stroke tmp = g.getStroke();
		
		//We are drawing the arrows at the fromPoint!!!
		Point fromPoint = points.get(0);
		Point toPoint   = points.get(1);
		
		Vector2D firstSegmentVector = new Vector2D(toPoint.x - fromPoint.x, toPoint.y - fromPoint.y); 
		Vector2D referencePointVector = firstSegmentVector.multiplyByScalar(15.0 / firstSegmentVector.length());
		
		Point referencePoint = new Point(fromPoint.x + referencePointVector.x, fromPoint.y + referencePointVector.y);
		
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(Math.PI / 6, fromPoint.x, fromPoint.y);
		
		Point arrowPoint1 = new Point(0,0);
		Point arrowPoint2 = new Point(0,0);
		
		rotation.transform(referencePoint, arrowPoint1);
		rotation.rotate(-Math.PI / 3, fromPoint.x, fromPoint.y);
		rotation.transform(referencePoint, arrowPoint2);
		
		g.drawLine(arrowPoint1.x, arrowPoint1.y, fromPoint.x, fromPoint.y);
		g.drawLine(arrowPoint2.x, arrowPoint2.y, fromPoint.x, fromPoint.y);
		
		if(this.model.getType() == RelationshipType.COMPOSITION || this.model.getType() == RelationshipType.AGGREGATION) {
			
			
		}
		
		if(this.model.getType() == RelationshipType.IMPLEMENTS) {
			g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {10}, 0));
		}
		g.drawPolyline(xpoints, ypoints, points.size());
		g.setStroke(tmp);
		

	}

}
