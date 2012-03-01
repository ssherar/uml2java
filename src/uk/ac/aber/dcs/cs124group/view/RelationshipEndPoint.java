package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;

public class RelationshipEndPoint extends Point {
	
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	private int side = NORTH;
	private int pixelsFromStart = 25;
	
	private Rectangle rect;
	
	public RelationshipEndPoint(Point p, Rectangle r) {
		this.x = p.x;
		this.y = p.y;
		this.rect = r;
		
		wrapFromCoordinates(r);
	}
	
	public void updateTo(Rectangle r) {
		this.rect = r;
		
		if(this.side == NORTH || this.side == SOUTH && this.pixelsFromStart > r.width) {
			this.pixelsFromStart = r.width;
		}
		else if (this.side == EAST || this.side == WEST && this.pixelsFromStart > r.height) {
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
	
	@Override
	public void move(int x, int y) {
		
		Vector2D[] sideVectors = new Vector2D[] {new Vector2D (rect.width, 0), new Vector2D(0, rect.height), new Vector2D(rect.width, 0), new Vector2D(0, rect.height)};
		
		Vector2D pointDisplacement = new Vector2D(x, y);
		Vector2D projectionOnSide = pointDisplacement.projection(sideVectors[this.side]);
		
		System.out.println(sideVectors[this.side].getColinearityFactor(projectionOnSide));
		//System.out.println(this.side);
		
		if(sideVectors[this.side].getColinearityFactor(projectionOnSide) > 1) {
			this.side = (this.side + 1) % 3;
			this.pixelsFromStart = 5;
			this.updateTo(this.rect);
			this.move(x, y);
		}
		else if(sideVectors[this.side].getColinearityFactor(projectionOnSide) < 0) {
			this.side = this.side == 0 ? 3 : (this.side - 1) % 3;
			this.pixelsFromStart = 5;
			this.updateTo(this.rect);
			this.move(x, y);
		}
		else {
			this.pixelsFromStart = (int)(sideVectors[this.side].getColinearityFactor(projectionOnSide) * sideVectors[this.side].length());
			System.out.println("We are on side " + this.side + ", " + this.pixelsFromStart + "px from start.");
			this.updateTo(rect);
			//System.out.println(this.pixelsFromStart);
		}
	}
	

}
