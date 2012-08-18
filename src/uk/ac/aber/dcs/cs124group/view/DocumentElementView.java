package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Observable;

import javax.swing.*;

import uk.ac.aber.dcs.cs124group.controller.Manager;

/**
 * The abstract class for all the views to inherit generic methods
 * to keep consistency across the board
 * 
 * @see javax.swing.JPanel
 * @see java.util.Observer
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public abstract class DocumentElementView extends JPanel implements java.util.Observer {
	/**
	 * The standardised font from the canvas
	 */
	private Font font;
	
	
	/**
	 * If this view is removed from it's parent due to the model
	 * not existing, this is a way to access it.
	 */
	protected Container suspendedParent;
	
	
	/**
	 * Placeholder Constructor
	 */
	protected DocumentElementView() {
		
	}
	
	/**
	 * Set the font with no overridding style
	 * @param font		the font of type {@link java.awt.Font}
	 */
	@Override
	public void setFont(Font font) {
		this.setFont(font, false);
	}
	
	/**
	 * Set the font with a boolean <code>overrideStyle</code> which forces
	 * the new font style onto the component.
	 * 
	 * @param font				The font to be set
	 * @param overrideStyle 	<code>True</code> if you want to force the font, <code>false</code> otherwise.
	 */
	public void setFont(Font font, boolean overrideStyle) {
		if(!overrideStyle && this.font != null) 
			this.font = new Font(font.getName(), this.font.getStyle(), font.getSize());
		else
			this.font = font;

		doLayout();
	}
	
	/**
	 * @see java.awt.Font
	 * @return		returns the font
	 */
	public Font getFont() {
		return font;
	}
	
	
	
	public Point getZoomOrigin() {
		Component parent = this.getParent();
		if(parent instanceof DocumentElementView) {
			int x = this.getLocation().x + ((DocumentElementView) parent).getZoomOrigin().x;
			int y = this.getLocation().y + ((DocumentElementView) parent).getZoomOrigin().y;
			
			return new Point(x, y);
		}
		else return this.getLocation();
	}

	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public void repaint() {
		if(this.getParent() != null)
			this.getParent().repaint();
		super.repaint();
	}
	
	/**
	 * Called from an notifyObserver(), this methods works out where it comes from and acts upon
	 * it if the second parameter is a STRING, as Observer params are (Observable o, Object obj)
	 * <p> 
	 * @see java.util.Oberserver#update()
	 * @param o		Where the notifyObserver was called from
	 * @param s		The string object of which we want to do
	 */
	@Override
	public void update(Observable o, Object s) {
		if(!(s instanceof String))
			throw new IllegalArgumentException("String expected");
		if(s.equals("wasRemoved")) {
			this.suspendedParent = this.getParent();
			try {
				this.getParent().remove(this);
				suspendedParent.repaint();
			}
			catch(NullPointerException ex) {
				
			}
			this.setVisible(false);

		}
		else if(s.equals("wasResurrected")) {
			this.setVisible(true);
			this.suspendedParent.add(this);
			this.getParent().doLayout();
			this.getParent().repaint();
		}
	}
	

	
	

}
