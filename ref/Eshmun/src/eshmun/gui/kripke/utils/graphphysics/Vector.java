package eshmun.gui.kripke.utils.graphphysics;

/**
 * Represents a Vector class, used as a utility class for the graph physics.
 *
 * @author Mohammad Ali Beydoun
 * @since 1.0
 */
public class Vector {
	/**
	 * the x-coordinate.
	 */
	private double x;
	
	/**
	 * the y-coordinate.
	 */
	private double y;

	/**
	 * Create a new Vector with the given coordinates.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for the x-coordinate
	 * 
	 * @return the x-coordinate of this vector.
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Getter for the y-coordinate
	 * 
	 * @return the y-coordinate of this vector.
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Sums two vector, this and the given vector.
	 *	
	 * @param v the vector to add to this.
	 * @return The vector resulting from (this + v).
	 */
	public Vector add(Vector v) {
		return new Vector(this.x + v.x, this.y + v.y);
	}

	/**
	 * Subtracts two vector, this and the given vector.
	 *	
	 * @param v the vector to subtract from this.
	 * @return The vector resulting from (this - v).
	 */
	public Vector sub(Vector v) {
		return new Vector(this.x - v.x, this.y - v.y);
	}

	/**
	 * Multiplies a vector by a constant c.
	 *	
	 * @param c the constant to multiply with this.
	 * @return The vector resulting from (this * c).
	 */
	public Vector mul(double c) {
		return new Vector(c * this.x, c * this.y);
	}

	/**
	 * Computes the dot product of two vector, this and the given vector.
	 *	
	 * @param v the vector to computer its dot product with this.
	 * @return The vector resulting from (this . v)
	 */
	public double dot(Vector v) {
		return this.x * v.x + this.y * v.y;
	}

	/**
	 * Computes the norm of this vector, in terms of inner product.
	 *	
	 * @return The norm (this . this).
	 */
	public double norm() {
		return Math.sqrt(this.dot(this));
	}

	/**
	 * Computes a normalized version of this vector.
	 *	
	 * @return The normalized vector. (this * ( 1 / norm))
	 */
	public Vector normalize() {
		return this.mul(1.0 / this.norm());
	}

	/**
	 * Computes the distance between two vector, this and the given vector.
	 *
	 * @param v the vector to computer the distance from.
	 * @return The distance between this and v.
	 */
	public double distance(Vector v) {
		return this.sub(v).norm();
	}
}