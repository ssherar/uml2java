package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;
import uk.ac.aber.dcs.cs124group.model.*;
import javax.swing.*;
import java.util.*;

public class Canvas extends JPanel {
	
	private static final Color RECTANGLE_BACKGROUND = new Color(255,255,190);
	
	private Manager manager;
	
	private double zoomFactor = 1;
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	public Canvas(Manager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		
		this.addMouseMotionListener(manager);
		this.addMouseListener(manager);
		this.addKeyListener(manager);
		this.setPreferredSize(new Dimension(924,700));
	}
	
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	public double getZoomFactor() {
		return zoomFactor;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setFont(font);
		g.scale(zoomFactor, zoomFactor);
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
				
		
		ArrayList<DocumentElement> elements = new ArrayList<DocumentElement>();
		try {
			elements = manager.getDrawableElements();
		}
		catch (NullPointerException ex) {
			System.out.println("Could not draw anything");
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
			g.setColor(RECTANGLE_BACKGROUND);
			g.fillRoundRect(c.getPosition().x + 1, c.getPosition().y + 1,
					        c.getSize().width - 1, c.getSize().height - 1, 10, 10);
			
			int nameFieldHeight = 2 + metrics.getHeight();
			g.setColor(Color.BLACK);
			g.drawLine(c.getPosition().x, c.getPosition().y + nameFieldHeight, 
					   c.getPosition().x + c.getSize().width, c.getPosition().y + nameFieldHeight);
			
			int nameStartPointX = (c.getSize().width - metrics.stringWidth(c.getName())) / 2;
			int nameStartPointY = metrics.getAscent() + metrics.getDescent();
			g.drawString(c.getName(), c.getPosition().x + nameStartPointX, c.getPosition().y + nameStartPointY);
		}
		
	}
	
	private void drawRelationship(Graphics2D g, Relationship r) {
		
	}
	
	private void drawTextLabel(Graphics2D g, TextLabel t) {
		
	}
	
	public void setNewSize(Dimension d) {
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);
	}
}
