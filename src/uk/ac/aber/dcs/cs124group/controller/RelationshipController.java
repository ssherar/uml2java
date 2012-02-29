package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uk.ac.aber.dcs.cs124group.model.Relationship;
import uk.ac.aber.dcs.cs124group.model.RelationshipType;

public class RelationshipController extends DiagramListener implements ActionListener {
	private Relationship model;
	
	public RelationshipController(Relationship m) {
		this.model = m;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
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

	}

}
