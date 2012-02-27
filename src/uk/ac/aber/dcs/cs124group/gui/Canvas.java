package uk.ac.aber.dcs.cs124group.gui;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.model.*;
import uk.ac.aber.dcs.cs124group.view.ManuallyDrawnElement;

import javax.swing.*;

import java.util.*;

public class Canvas extends JPanel {
	
	
	private Manager manager;
	
	private double zoomFactor = 1;
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	private ArrayList<ManuallyDrawnElement> manuallyDrawnElements = new ArrayList<ManuallyDrawnElement> ();

	
	public Canvas(Manager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		this.setLayout(new DiagramLayout());
		
		//this.addMouseMotionListener(manager);
		this.addMouseListener(manager);
		this.addKeyListener(manager);
		
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 setPreferredSize(new Dimension(2000,2000));
	         }
	      });
		
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
				
		
		
		for(int i = 0; i < manuallyDrawnElements.size(); i++) {
			manuallyDrawnElements.get(i).draw(g, this);

		}
		
	}
			
	
	@Override
	public Component add(Component c){
		super.add(c);
		this.setComponentZOrder(c, 0);
		return c;
	}
	
	public void addManuallyDrawnElement(ManuallyDrawnElement e) {
		this.manuallyDrawnElements.add(e);
	}
 }
