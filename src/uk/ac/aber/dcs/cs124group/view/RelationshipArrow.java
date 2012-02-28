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
		return this.contains(p.x, p.y);
	}
	
	@Override
	public boolean contains(int x, int y) {
		return false;
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
