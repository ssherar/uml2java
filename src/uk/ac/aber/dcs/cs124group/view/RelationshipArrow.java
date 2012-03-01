package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import uk.ac.aber.dcs.cs124group.controller.RelationshipController;
import uk.ac.aber.dcs.cs124group.model.Relationship;
import uk.ac.aber.dcs.cs124group.model.RelationshipType;

public class RelationshipArrow extends DocumentElementView {
	
	private Relationship model;
	
	public RelationshipArrow(Relationship model) {
		this.model = model;
		this.setLocation(new Point(0,0));
		this.setPreferredSize(new Dimension(100000,100000));
		this.setOpaque(false);
		RelationshipController controller = new RelationshipController(this.model);
		RelationshipPopup menu = new RelationshipPopup(controller);
		this.setComponentPopupMenu(menu);
		
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
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
		else if(s.equals("wasInverted")) {
			this.getParent().doLayout();
			this.repaint();
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
		Vector2D referencePointVector = firstSegmentVector.multiplyByScalar(19.0 / firstSegmentVector.length());
		
		Point referencePoint = new Point(fromPoint.x + referencePointVector.x, fromPoint.y + referencePointVector.y);
		
		
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(Math.PI / 6, fromPoint.x, fromPoint.y);
		
		Point arrowPoint1 = new Point(0,0);
		Point arrowPoint2 = new Point(0,0);
		
		rotation.transform(referencePoint, arrowPoint1);
		rotation.rotate(-Math.PI / 3, fromPoint.x, fromPoint.y);
		rotation.transform(referencePoint, arrowPoint2);
		
		
		
		if(this.model.getType() == RelationshipType.INHERITANCE || this.model.getType() == RelationshipType.IMPLEMENTS) {
			g.drawLine(arrowPoint1.x, arrowPoint1.y, fromPoint.x, fromPoint.y);
			g.drawLine(arrowPoint2.x, arrowPoint2.y, fromPoint.x, fromPoint.y);
			g.drawLine(arrowPoint1.x, arrowPoint1.y, arrowPoint2.x, arrowPoint2.y);
			xpoints[0] = referencePoint.x;
			ypoints[0] = referencePoint.y;
		}
		
		else if(this.model.getType() == RelationshipType.USES) {
			g.drawLine(arrowPoint1.x, arrowPoint1.y, fromPoint.x, fromPoint.y);
			g.drawLine(arrowPoint2.x, arrowPoint2.y, fromPoint.x, fromPoint.y);
		}
		
		else if(this.model.getType() == RelationshipType.COMPOSITION || this.model.getType() == RelationshipType.AGGREGATION) {
			Vector2D doubleReferenceVector = referencePointVector.multiplyByScalar(2 * (Math.cos(Math.PI / 6)));
			Point doubleReferencePoint = new Point(fromPoint.x + doubleReferenceVector.x, fromPoint.y + doubleReferenceVector.y);
			Polygon diamond = new Polygon();
			diamond.addPoint(fromPoint.x, fromPoint.y);
			diamond.addPoint(arrowPoint1.x, arrowPoint1.y);
			diamond.addPoint(doubleReferencePoint.x, doubleReferencePoint.y);
			diamond.addPoint(arrowPoint2.x, arrowPoint2.y);
			
			xpoints[0] = doubleReferencePoint.x;
			ypoints[0] = doubleReferencePoint.y;
			
			if(this.model.getType() == RelationshipType.COMPOSITION) {
				g.drawPolygon(diamond);
			}
			else {
				g.fillPolygon(diamond);
			}
			
		}
		
		if(this.model.getType() == RelationshipType.IMPLEMENTS) {
			g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {10}, 0));
		}
		g.drawPolyline(xpoints, ypoints, points.size());
	
		g.setStroke(tmp);
		

	}
	
	public class RelationshipPopup extends JPopupMenu {
		private RelationshipController listener;
		
		private String[] rTypes = {"Aggregation","Composition","Inheritance","Uses","Implements"};
		public RelationshipPopup(RelationshipController l) {
			this.listener = l;
			JMenu changeRelationship = new JMenu("Change Relationship");
			JMenuItem submenu;
			
			ButtonGroup typeGroup = new ButtonGroup();
			for(String s : rTypes) {
				//submenu = null;
				submenu = new JRadioButtonMenuItem(s, s.equals("Uses"));
				submenu.addActionListener(listener);
				typeGroup.add(submenu);
				changeRelationship.add(submenu);
				
			}
			
			JMenuItem invert = new JMenuItem("Invert");
			invert.addActionListener(listener);
			
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(listener);
				
			add(changeRelationship);
			add(invert);
			add(delete);
		}
	}

}
