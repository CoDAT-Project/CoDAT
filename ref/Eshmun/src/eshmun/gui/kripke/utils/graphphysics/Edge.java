package eshmun.gui.kripke.utils.graphphysics;

/**
 * Simple directed edge in a graph.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class Edge {
	/**
	 * The Node from which the edge is going out.
	 */
	Node from;
	
	/**
	 * The Node to which the edge is going in.
	 */
	Node to;
	
	/**
	 * Create a Node with the given name, and position.
	 * 
	 * @param from out node.
	 * @param to in node.
	 */
	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
}
