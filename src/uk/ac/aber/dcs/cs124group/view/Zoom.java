package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.gui.Canvas;

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
	
	public AffineTransform getInternalTransform(Point origin) {
		Point2D newOrigin = this.getAffineTransform().transform(origin, null);
		int newX = (int) newOrigin.getX();
		int newY = (int) newOrigin.getY();
		return new AffineTransform(zoomLevel, 0, 0, zoomLevel, newX - origin.x, newY - origin.y);
	}
	
	public void setLevel(double level) {
		Manager.getInstance().setStatusText("Viewpoint center is at " + viewportCenter);
		this.zoomLevel = level;
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	public void setViewportCenter(Point center) {
		this.viewportCenter = center;
	}
	
	public Point2D convertPoint(Point2D p, boolean internal, Point origin) {
		try {
			if(internal) {
				return this.getInternalTransform(origin).inverseTransform(p, null);
			}
			else return this.getAffineTransform().inverseTransform(p, null);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Point inverseConvertPoint(Point p) {
		Point2D pp = this.getAffineTransform().transform(p, null);
		return new Point((int) pp.getX(), (int) pp.getY());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JViewport viewport = (JViewport) e.getSource();
		Point offset = viewport.getViewPosition();
		Canvas canvas = Manager.getInstance().getCanvas();
		
		Dimension size = canvas.isBiggerThanViewport() ? viewport.getSize() : canvas.getSize(); 
		
		this.setViewportCenter(new Point(offset.x + (size.width / 2),
										 offset.y + (size.height / 2)));
		
		canvas.repaint();
		
	}
	
	
}
