package eshmun.gui.kripke.utils.graphphysics;

/**
 * Simple node in a graph.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class Node {
	/**
	 * Name of the node, should be unique.
	 */
	String name;
	
	/**
	 * Position of the node as a vector.
	 */
	Vector position;
	
	/**
	 * Create a Node with the given name, and position.
	 * 
	 * @param name the Name of the Node.
	 * @param x the x-coordinate of the Node.
	 * @param y the y-coordinate of the Node.
	 */
	public Node(String name, int x, int y) {
		this.name = name;
		this.position = new Vector(x, y);
	}
}
