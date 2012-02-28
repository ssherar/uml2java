package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
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
				System.out.println("Distance to segment is " + distanceToSegment);
				if (distanceToSegment < 10) System.out.println("CONTAINS the point " + p);
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
		// TODO Auto-generated method stub

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
		
		double finalSegmentSlope = (points.get(points.size() - 1).y - points.get(points.size() - 2).y) /
                				   (points.get(points.size() - 1).x - points.get(points.size() - 2).x);
		
		if(this.model.getType() == RelationshipType.IMPLEMENTS) {
			g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {10}, 5));
		}
		
		if(this.model.getType() == RelationshipType.COMPOSITION) {
			//Move last point 7px back along slope
			
		}
		g.drawPolyline(xpoints, ypoints, points.size());
		g.setStroke(tmp);
		

	}

}
