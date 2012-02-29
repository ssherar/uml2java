package uk.ac.aber.dcs.cs124group.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uk.ac.aber.dcs.cs124group.model.Relationship;

public class RelationshipController extends DiagramListener implements ActionListener {
	private Relationship model;
	
	public RelationshipController(Relationship m) {
		this.model = m;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String c = arg0.getActionCommand();
		if(c.equals("Aggregation")) {
			System.out.println("Aggregation fired");
		}

	}

}
