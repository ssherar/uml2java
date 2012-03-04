package uk.ac.aber.dcs.cs124group.view;

import java.awt.*;

import uk.ac.aber.dcs.cs124group.model.Relationship;

public class RelationshipEndPoint extends Point {
	
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	private int side = NORTH;
	private int pixelsFromStart;
	
	private Rectangle rect;
	
	private Relationship relationship;
	
	public RelationshipEndPoint(Point p, Rectangle r, Relationship relationship) {
		this.x = p.x;
		this.y = p.y;
		this.rect = r;
		this.relationship = relationship;
		
		wrapFromCoordinates(r);
	}
	
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
		relationship.realignCardinalities();
		
	}
	
	private void flipClockwise() {
		this.side = (this.side + 1) % 3;
	}
	
	private void flipAntiClockwise() {
		this.side = this.side == 0 ? 3 : (this.side - 1);
	}
	

}
