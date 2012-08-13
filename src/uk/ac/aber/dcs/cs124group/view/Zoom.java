package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Zoom implements ChangeListener {
	
	private double zoomLevel;
	private Point viewportCenter;
	
	public Zoom(double level, Point viewportCenter) {
		this.zoomLevel = level;
		this.viewportCenter = viewportCenter;
	}
	
	public Zoom(Point viewportCenter) {
		this(1.0, viewportCenter);
	}
	
	public AffineTransform getAffineTransform() {
		double k = zoomLevel;
		
		int offsetX = (int) ((1-k)*viewportCenter.x);
		int offsetY = (int) ((1-k)*viewportCenter.y);
		
		return new AffineTransform(k, 0, 0, k, offsetX, offsetY);
	}
	
	public void setLevel(double level) {
		this.zoomLevel = level;
	}
	
	public void setViewportCenter(Point center) {
		this.viewportCenter = center;
	}
	
	public MouseEvent convertMouseEvent(MouseEvent e) {
		try {
			Point2D newPoint = getAffineTransform().inverseTransform(e.getPoint(), e.getPoint());
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//int x = (int) newPoint.getX();
		//int y = (int) newPoint.getY();
		
		
		return e;
		
		/*int id = e.getID();
		Component source = (Component) e.getSource();
		long when = e.getWhen();
		int modifiers = e.getModifiers();
		int clickCount = e.getClickCount();
		boolean popupTrigger = e.isPopupTrigger();
		int button = e.getButton();
		
		return new MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, button); */
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JViewport viewport = (JViewport) e.getSource();
		Point offset = viewport.getViewPosition();
		
		this.setViewportCenter(new Point(offset.x + (viewport.getSize().width / 2),
										 offset.y + (viewport.getSize().height / 2)));
		
		System.out.println(viewportCenter);
	}
	
	
}
