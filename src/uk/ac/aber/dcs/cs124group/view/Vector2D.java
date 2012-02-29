package uk.ac.aber.dcs.cs124group.view;

import java.awt.Point;


public class Vector2D extends Point {
	
	public Vector2D(int x, int y) {
		super(x,y);	
	}

	public double length() {
		return (new Point(0,0).distance(new Point(this.x, this.y)));
	}
	
	public int scalarProduct(Vector2D b) {
		return (this.x * b.x + this.y * b.y);
	}
	
	public Vector2D projection(Vector2D onto) {
		int scalarProduct = onto.scalarProduct(this);
		int x = ((int) ((scalarProduct / Math.pow(onto.length(), 2)) * onto.x));
		int y = ((int) ((scalarProduct / Math.pow(onto.length(), 2)) * onto.y));
		
		return new Vector2D(x,y);
	}
	
	public Vector2D multiplyByScalar(double k) {
		return new Vector2D((int) (this.x * k), (int) (this.y * k));
	}
	
	public boolean colinear(Vector2D b) {
		if(this.nonZero() && b.nonZero()) {
			if(this.y == 0) {
				return(b.y == 0);
			}
			else if (this.x == 0) {
				return(b.x == 0);
			}
			else return (Math.abs(b.y / this.y) - Math.abs(b.x / this.x) < 0.1);
		}
		else return false;
	}
	
	public boolean nonZero() {
		return (this.x != 0 || this.y != 0);
	}
	
	/** If b = k * this, returns k */
	public double getColinearityFactor(Vector2D b) {
		if(this.nonZero() && b.nonZero() && this.colinear(b)) {
			if(this.y != 0)
				return b.y / (double) this.y;
			else return b.x / (double) this.x;
		}
		else return 0;
	}
}
