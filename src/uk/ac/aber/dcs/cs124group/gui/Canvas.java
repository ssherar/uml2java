package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.model.*;
import javax.swing.*;
import java.util.*;

public class Canvas extends JPanel {
	
	private Manager manager;
	
	private double zoomFactor;
	private Font font = new Font("Arial", Font.PLAIN, 10);
	
	public Canvas(Manager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		
		this.addMouseMotionListener(manager);
		this.addMouseListener(manager);
		this.addKeyListener(manager);
		//this.setPreferredSize(new Dimension(624,600));
	}
	
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setFont(font);
		
		ArrayList<DocumentElement> elements = new ArrayList<DocumentElement>();
		try {
			elements = manager.getDrawableElements();
		}
		catch (NullPointerException ex) {
			return;
		}
		
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
		FontMetrics metrics = g.getFontMetrics();
		
		if(c.getPaintState() == ElementPaintState.DEFAULT) {
			g.drawRoundRect(c.getPosition().x, c.getPosition().y,
					        c.getSize().width, c.getSize().height, 10, 10);
			int nameFieldHeight = 2 + metrics.getHeight();
			g.drawLine(c.getPosition().x, c.getPosition().y + nameFieldHeight, 
					   c.getPosition().x + c.getSize().width, c.getPosition().y + nameFieldHeight);
		}
		
	}
	
	private void drawRelationship(Graphics2D g, Relationship r) {
		
	}
	
	private void drawTextLabel(Graphics2D g, TextLabel t) {
		
	}
}
