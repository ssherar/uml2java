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
import uk.ac.aber.dcs.cs124group.gui.DiagramLayout;
import uk.ac.aber.dcs.cs124group.model.*;

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
		this.setLayout(new DiagramLayout());
		
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
		
		if(this.model.getLabel() != null) {
			LabelView view = this.model.getLabel().getView();
			this.model.getLabel().addUndoableEditListener(this.model.getUndoableEditListener());
			this.model.getLabel().addObserver(view);
			view.setFont(this.getFont());
			
			this.add(view);
		}
		
		if(this.model.getCardinalityFrom() != null) {
			LabelView view = this.model.getCardinalityFrom().getView();
			this.model.getCardinalityFrom().addUndoableEditListener(this.model.getUndoableEditListener());
			this.model.getCardinalityFrom().addObserver(view);
			view.setFont(this.getFont());
			
			this.add(view);
		}
		
		if(this.model.getCardinalityTo() != null) {
			LabelView view = this.model.getCardinalityTo().getView();
			this.model.getCardinalityTo().addUndoableEditListener(this.model.getUndoableEditListener());
			this.model.getCardinalityTo().addObserver(view);
			view.setFont(this.getFont());
			
			this.add(view);
		}
	}
	
	@Override
	public boolean contains(Point p) {
		
		for(int i = 0; i < model.getPoints().size(); i++) {
			if(p.distance(model.getPoints().get(i)) < 10)
				return true;
		}
		
		for(int j = 1; j < this.model.getPoints().size(); j++) {
			Point p1 = this.model.getPoints().get(j - 1);
			Point p2 = this.model.getPoints().get(j);
			
			Vector2D pVector = new Vector2D(p.x - p1.x, p.y - p1.y);
			Vector2D segmentVector = new Vector2D(p2.x - p1.x, p2.y - p1.y);
			Vector2D projection = pVector.projection(segmentVector);
			
			if(segmentVector.getColinearityFactor(projection) < 1.001 && segmentVector.getColinearityFactor(projection) > 0)  {
				double distanceToSegment = p.distance(p1.x + projection.x, p1.y + projection.y);
				if (distanceToSegment < 10) return true;
			}

		}
		
		for (int i = 0; i < this.getComponentCount(); i++) {
			if(this.getComponent(i).getBounds().contains(p)) {
				return true;
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
		super.update(o, s);
		if(s.equals("typeChanged") || s.equals("restored")) {
			((RelationshipPopup) (this.getComponentPopupMenu())).setSelectedType(this.model.getType().toString());
			this.repaint();
		}
		else if (s.equals("labelRequested")) {
			this.addLabelToModel();
		}
		else if(s.equals("fontChanged")) {
			this.setFont(((DocumentPreferences)o).getFont());
		}
		else if(((String)s).contains("cardinalityRequested")) {
			if(((String)s).contains("from")) {
				this.addCardinalityToModel(true);
			}
			else this.addCardinalityToModel(false);
		}
		else this.repaint();

	}
	
	private void addCardinalityToModel(boolean from) {
		RelationshipEndPoint point = (RelationshipEndPoint) 
				(from ? this.model.getPoints().get(0) : this.model.getPoints().get(this.model.getPoints().size() - 1));
		Cardinality c = new Cardinality(point);
		if(from) {
			this.model.setCardinalityFrom(c);
		}
		else {
			this.model.setCardinalityTo(c);
		}
		
		LabelView l = c.getView();
		l.setFont(this.getFont());
		this.add(l);
		c.addObserver(l);
		c.addUndoableEditListener(this.model.getUndoableEditListener());
		l.enableEdit();
		
		this.repaint();
	}
	
	private void addLabelToModel() {
		int i = this.model.getPoints().size() / 2;
		Point p1 = this.model.getPoints().get(i - 1);
		Point p2 = this.model.getPoints().get(i);
		
		Point p = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
		
		RelationshipLabel label = new RelationshipLabel(p,this.model);
		label.addUndoableEditListener(this.model.getUndoableEditListener());
		
		
		LabelView view = label.getView();
		label.addObserver(view);
		view.setFont(this.getFont());
		
		this.model.addLabel(label);
		this.add(view);
		view.enableEdit();

		this.repaint();
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		for(int i = 0; i < this.getComponentCount(); i++)
			this.getComponent(i).setFont(f);
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
		
		if(this.model.getPaintState() == ElementPaintState.MOUSED_OVER || this.model.getPaintState() == ElementPaintState.SELECTED) {
			g.setColor(Color.GREEN);
			for(int i = 0; i < points.size(); i++) {
				Point p = points.get(i);
				int ovalSize = (int) (10 / Math.sqrt(2));
				g.fillOval(p.x - 3, p.y - 3, ovalSize, ovalSize);
			}
		}
		
		/* Set proper bounds on this component */
		int maxX = 0;
		int maxY = 0;
		for(int i = 0; i < points.size(); i++) {
			if (points.get(i).x > maxX)
				maxX = points.get(i).x;
			if (points.get(i).y > maxY)
				maxY = points.get(i).y;
		}
		for(int i = 0; i < this.getComponentCount(); i++) {
			if(this.getComponent(i).getBounds().getMaxX() > maxX)
				maxX = (int) (this.getComponent(i).getBounds().getMaxX());
			if(this.getComponent(i).getBounds().getMaxY() > maxY)
				maxY = (int) (this.getComponent(i).getBounds().getMaxY());
		}
		this.setPreferredSize(new Dimension(maxX + 20, maxY + 20));
		this.getParent().doLayout();
	}
	
	private class RelationshipPopup extends JPopupMenu {
		private RelationshipController listener;
		private JMenu changeRelationship;
		
		private String[] rTypes = {"Aggregation","Composition","Inheritance","Uses","Implements"};
		public RelationshipPopup(RelationshipController l) {
			this.listener = l;
			changeRelationship = new JMenu("Change Relationship");
			JMenuItem submenu;
			
			ButtonGroup typeGroup = new ButtonGroup();
			for(String s : rTypes) {
				submenu = new JRadioButtonMenuItem(s, s.equals("Uses"));
				submenu.setName(s);
				submenu.addActionListener(listener);
				typeGroup.add(submenu);
				changeRelationship.add(submenu);
				
			}
			
			JMenuItem label = new JMenuItem("Add/edit label");
			label.addActionListener(listener);
			
			JMenu cardinalities = new JMenu("Cardinalities");
			JMenuItem cardFrom = new JMenuItem("Cardinality from");
			cardFrom.addActionListener(listener);
			JMenuItem cardTo = new JMenuItem("Cardinality to");
			cardTo.addActionListener(listener);
			cardinalities.add(cardFrom);
			cardinalities.add(cardTo);
			
			JMenuItem invert = new JMenuItem("Invert");
			invert.addActionListener(listener);
			
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(listener);
				
			add(changeRelationship);
			add(cardinalities);
			add(label);
			add(invert);
			add(delete);
		}
		
		public void setSelectedType(String t) {
			String type = t.toLowerCase();
			for(int i = 0; i < changeRelationship.getItemCount(); i++) {
				JMenuItem currentItem = changeRelationship.getItem(i);
				if (currentItem.getName().toLowerCase().equals(type)) {
					currentItem.setSelected(true);
				}
			}
		}
	}

}
