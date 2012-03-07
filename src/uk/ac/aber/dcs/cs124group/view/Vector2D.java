package uk.ac.aber.dcs.cs124group.view;

import java.awt.Point;

/**
 * Represents a vector in two-dimensional space.
 * 
 * @author Daniel Maly
 * @version 1.0.0
 */
public class Vector2D extends Point {
	
	/**
	 * Constructs a new Vector2D with the specified coordinates.
	 * 
	 * @param x The x-coordinate of the vector.
	 * @param y The y-coordinate of the vector.
	 */
	public Vector2D(int x, int y) {
		super(x,y);	
	}

	/**
	 * @return The length of this vector
	 */
	public double length() {
		return (new Point(0,0).distance(new Point(this.x, this.y)));
	}
	
	/**
	 * @param b The second operand in the scalar product.
	 * @return The scalar product of this vector with the one specified by the argument.
	 */
	public int scalarProduct(Vector2D b) {
		return (this.x * b.x + this.y * b.y);
	}
	
	/**
	 * @param onto The Vector2D that is being projected onto.
	 * @return The projection of this Vector2D onto the Vector2D specified by the argument.
	 */
	public Vector2D projection(Vector2D onto) {
		int scalarProduct = onto.scalarProduct(this);
		int x = ((int) ((scalarProduct / Math.pow(onto.length(), 2)) * onto.x));
		int y = ((int) ((scalarProduct / Math.pow(onto.length(), 2)) * onto.y));
		
		return new Vector2D(x,y);
	}
	
	/**
	 * @param k The scalar to be multiplied by
	 * @return This Vector2D multiplied by the scalar specified in the argument.
	 */
	public Vector2D multiplyByScalar(double k) {
		return new Vector2D((int) (this.x * k), (int) (this.y * k));
	}
	
	/**
	 * Checks for colinearity between this vector and the one specified
	 * by the argument. 
	 * <p>
	 * NOTE: Colinearity here is defined on a close-enough basis to 
	 * improve responsiveness of RelationshipArrows. This method should
	 * not be relied on to produce a mathematically correct result.
	 * 
	 * @see uk.ac.aber.dcs.cs124group.view.RelationshipArrow#contains(Point) RelationshipArrow.contains(Point)
	 * 
	 * @param b The vector that is tested for colinearity with this vector
	 * @return True if the two vectors are colinear, false otherwise.
	 */
	public boolean colinear(Vector2D b) {
		if(this.nonZero() && b.nonZero()) {
			if(Math.abs(this.y) < 5) {
				return(Math.abs(b.y) < 5);			
			}
			else if (Math.abs(this.x) < 5) {
				return(Math.abs(b.x) < 5);
			}
			else {
				return (Math.abs((b.y / (double) this.y) - (b.x / (double) this.x)) < 0.1);
			}
		}
		else return false;
	}
	
	
	/**
	 * @return True if this is a non-zero vector, false otherwise.
	 */
	public boolean nonZero() {
		return (this.x != 0 || this.y != 0);
	}
	
	/** 
	 * @param b Another Vector2D
	 * @return The scalar by which this vector should be multiplied
	 * in order to obtain the vector specified in the argument. If
	 * the two vectors aren't colinear, returns 0. In other words,
	 * if b = k * this, returns k 
	 */
	public double getColinearityFactor(Vector2D b) {
		if(this.nonZero() && b.nonZero() && this.colinear(b)) {
			if(this.y != 0 && b.y != 0)
				return b.y / (double) this.y;
			else return b.x / (double) this.x;
		}
		else return 0;
	}
}
