package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;

import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.gui.Canvas;

public class Zoom implements ChangeListener, Serializable {
	
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

	
	public AffineTransform getScalingOnlyTransform() {
		return new AffineTransform(zoomLevel, 0, 0, zoomLevel, 0, 0);
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
	
	public Point convertPoint(Point2D p, boolean scaleOnly) {
		try {
			Point2D pp = this.getAffineTransform().inverseTransform(p, null);
			return new Point((int) pp.getX(), (int) pp.getY());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Point inverseConvertPoint(Point p, boolean scaleOnly) {
		AffineTransform a = scaleOnly ? this.getScalingOnlyTransform() : this.getAffineTransform();
		
		Point2D pp = a.transform(p, null);
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
