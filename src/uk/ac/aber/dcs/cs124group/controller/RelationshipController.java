package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.*;
import java.awt.Point;
import java.util.ArrayList;

import uk.ac.aber.dcs.cs124group.model.*;
import uk.ac.aber.dcs.cs124group.view.RelationshipEndPoint;

public class RelationshipController extends DiagramListener implements ActionListener {
	
	private Relationship model;
	private Point movedPoint;
	
	public RelationshipController(Relationship m) {
		this.model = m;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.model.storeInEdit();
		String c = arg0.getActionCommand();
		if(c.equals("Aggregation")) {
			model.setType(RelationshipType.AGGREGATION);
		}
		else if (c.equals("Composition")) {
			model.setType(RelationshipType.COMPOSITION);
		}
		else if (c.equals("Inheritance")) {
			model.setType(RelationshipType.INHERITANCE);
		}
		else if (c.equals("Uses")) {
			model.setType(RelationshipType.USES);
		}
		else if (c.equals("Implements")) {
			model.setType(RelationshipType.IMPLEMENTS);
		}
		else if (c.equals("Invert")) {
			model.setInverted();
		}
		else if (c.equals("Delete")) {
			model.userRemove();
		}
		else if (c.equals("Add/edit label")) {
			model.requestLabel();
		}
		else if (c.equals("Cardinality from")) {
			model.requestCardinality("from");
		}
		else if (c.equals("Cardinality to")) {
			model.requestCardinality("to");
		}
		

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		movedPoint = null;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && !e.isConsumed() && this.getMode() == ListeningMode.LISTEN_TO_ALL) {
			e.consume();
			ArrayList<Point> points = this.model.getPoints();
			for(int i = 1; i < points.size() - 1; i++) {
				if (e.getPoint().distance(points.get(i)) <= 10) {
					this.model.deletePoint(points.get(i));
				}
			}
		}
			
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(movedPoint == null) {
			for(Point p : this.model.getPoints()) {
				if(p.distance(e.getPoint()) < 10) {
					movedPoint = p;
					this.model.storeInEdit();
					break;
				}
			}
			if(movedPoint == null) {
				this.model.storeInEdit();
				movedPoint = this.model.addPoint(e.getPoint());
			}
		}
		if(movedPoint != null) {
			if(movedPoint instanceof RelationshipEndPoint) {
				movedPoint.move(e.getX() - movedPoint.x, e.getY() - movedPoint.y);
			}
			else movedPoint.move(e.getX(), e.getY());
			this.model.pointMoved();
		}
		this.model.setPaintState(ElementPaintState.SELECTED);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		model.setPaintState(ElementPaintState.MOUSED_OVER);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		model.setPaintState(ElementPaintState.DEFAULT);
	}

}
