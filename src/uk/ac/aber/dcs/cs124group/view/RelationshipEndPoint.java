package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;

public class RelationshipEndPoint extends Point {
	
	private static final int NORTH = 1;
	private static final int WEST = 2;
	private static final int SOUTH = 3;
	private static final int EAST = 4;
	
	private int side = NORTH;
	private int pixelsFromStart = 25;
	
	public RelationshipEndPoint(Point p, Rectangle r) {
		this.x = p.x;
		this.y = p.y;
		
		wrapFromCoordinates(r);
	}
	
	public void updateTo(Rectangle r) {
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
		
		if(this.x > r.x + r.width) this.x = r.x + r.width;
		if(this.y > r.y + r.height) this.y = r.y + r.height;
	}
	
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
	

}
