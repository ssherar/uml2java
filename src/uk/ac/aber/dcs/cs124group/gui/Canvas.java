package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.model.*;
import javax.swing.*;
import java.util.*;

public class Canvas extends JPanel {
	
	private Manager manager;
	
	public Canvas(Manager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		
		this.addMouseMotionListener(manager);
		this.addMouseListener(manager);
		this.addKeyListener(manager);
		//this.setPreferredSize(new Dimension(624,600));
	}
	
	public void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		
		ArrayList<DocumentElement> elements = manager.getDrawableElements();
		for(int i = 0; i < elements.size(); i++) {
			DocumentElement e = elements.get(i);
			if(e instanceof ClassRectangle) {
				drawClassRectangle(g, (ClassRectangle) e);
			}
			else if (e instanceof Relationship) {
				drawRelationship(g, (Relationship) e);
			}
			else if (e instanceof TextLabel) {
				drawTextLabel(g, (TextLabel) e);
			}
		}
		
	}
	
	private void drawClassRectangle(Graphics2D g, ClassRectangle c) {
		
	}
	
	private void drawRelationship(Graphics2D g, Relationship r) {
		
	}
	
	private void drawTextLabel(Graphics2D g, TextLabel t) {
		
	}
}
