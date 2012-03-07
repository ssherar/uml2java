package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;
import uk.ac.aber.dcs.cs124group.model.*;

/**
 * A class defining the endpoint of a relationship
 * based on the side of the class rectangle and the 
 * distance from the corner.
 * 
 * @see Relationship
 * @see RelationshipArrow#update(java.util.Observable, Object)
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class RelationshipEndPoint extends Point {
	
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	/** The side of the class rectangle this endpoint is on. */
	private int side = NORTH;
	
	/** The distance of this endpoint from the corner of the class rectangle. */
	private int pixelsFromStart;
	
	/** The geometrical representation of the associated class rectangle. */
	private Rectangle rect;
	
	/** The relationship this endpoint is on. */
	private Relationship relationship;
	
	/**
	 * Constructs a new relationship endpoint at the specified location,
	 * attached to the specified Rectangle, belonging to the specified
	 * relationship.
	 * 
	 * @param p The initial location of this RelationshipEndPoint
	 * @param r The rectangle this endpoint is attached to.
	 * @param relationship The relationship this endpoint belongs to.
	 */
	public RelationshipEndPoint(Point p, Rectangle r, Relationship relationship) {
		this.x = p.x;
		this.y = p.y;
		this.rect = r;
		this.relationship = relationship;
		
		wrapFromCoordinates(r);
	}
	
	/**
	 * Update the (x,y) coordinates of this RelationshipEndPoint
	 * to fit a new Rectangle based on the current set of (side, distance)
	 * coordinates.
	 * 
	 * @param r The updated Rectangle
	 */
	public void updateTo(Rectangle r) {
		this.rect = r;
		
		
		if((this.side == NORTH || this.side == SOUTH) && this.pixelsFromStart > r.width) {
			this.pixelsFromStart = r.width;
		}
		else if ((this.side == EAST || this.side == WEST) && this.pixelsFromStart > r.height) {
			this.pixelsFromStart = r.height;
		}
		
		
		
		
		if (this.side == NORTH) {
			this.y = r.y;
			this.x = r.x + pixelsFromStart;
		}
		else if (this.side == WEST) {
			this.x = r.x;
			this.y = r.y + pixelsFromStart;
		}
		else if (this.side == EAST) {
			this.x = r.x + r.width;
			this.y = r.y + pixelsFromStart;
		}
		else if (this.side == SOUTH) {
			this.y = r.height + r.y;
			this.x = r.x + pixelsFromStart;
		}
		
		if((this.side == NORTH || this.side == SOUTH) && this.x < r.x) {
			this.x = r.x;
		}
		else if ((this.side == EAST || this.side == WEST) && this.y < r.y) {
			this.y = r.y;
		}
		
		

	}
	
	/**
	 * Initialise the (side, distance) coordinates based on the current (x,y) coordinates
	 * and the specified Rectangle.
	 * 
	 * @param r The Rectangle around which this RelationshipEndPoint is to be wrapped.
	 */
	private void wrapFromCoordinates(Rectangle r) {
		if (this.x == r.x) {
			this.side = WEST;
			this.pixelsFromStart = this.y - r.y;
		}
		else if(this.x == r.x + r.width) {
			this.side = EAST;
			this.pixelsFromStart = this.y - r.y;
		}
		else if(this.y == r.y) {
			this.side = NORTH;
			this.pixelsFromStart = this.x - r.x;
		}
		else if(this.y == r.y + r.height) {
			this.side = SOUTH;
			this.pixelsFromStart = this.x - r.x;
		}
		else throw new IllegalArgumentException(this + " is not on the Rectangle " + r);

	}
	
	@Override
	/**
	 * Moves this RelationshipEndPoint so that it stays on its associated Rectangle.
	 */
	public void move(int x, int y) {
		if (this.side == NORTH || this.side == SOUTH) {
			if(this.side == NORTH && y > 30) {
				if(this.pixelsFromStart > (0.5) * this.rect.width)
					this.flipClockwise();
				else this.flipAntiClockwise();
				this.pixelsFromStart = 0;
			}
			else if (this.side == SOUTH && y < -30) {
				if(this.pixelsFromStart > (0.5) * this.rect.width)
					this.flipClockwise();
				else this.flipAntiClockwise();
				this.pixelsFromStart = 0;
			}
			else pixelsFromStart += x;
		}
		else if(this.side == EAST || this.side == WEST) {
			if(this.side == EAST && x < -30) {
				if(this.pixelsFromStart > (0.5) * this.rect.height)
					this.flipClockwise();
				else this.flipAntiClockwise();
				this.pixelsFromStart = 0;
			}
			else if (this.side == WEST && x > 30) {
				if(this.pixelsFromStart > (0.5) * this.rect.height)
					this.flipClockwise();
				else this.flipAntiClockwise();
				this.pixelsFromStart = 0;
			}
			
			else {
				pixelsFromStart +=y;
			}
		}
		
		this.updateTo(this.rect);
		relationship.realignSubelements();
		
	}
	
	/**
	 * Changes the side coordinate of this RelationshipEndPoint clockwise by one step.
	 */
	private void flipClockwise() {
		this.side = (this.side + 1) % 3;
	}
	
	/**
	 * Changes the side coordinate of this RelationshipEndPoint anti-clockwise by one step.
	 */
	private void flipAntiClockwise() {
		this.side = this.side == 0 ? 3 : (this.side - 1);
	}
	

}
