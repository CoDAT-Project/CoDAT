package eshmun.decisionprocedure;

/**
 * Represents an Edge between two nodes in the tableau.
 * 
 * @author Mouahmad Sakr, Kinan Dak Al Bab
 * @since 0.1
 */
public class TableauEdge {
	/**
	 * The node from which this node starts.
	 */
	private TableauNode from;
	
	/**
	 * The node in which this node ends.
	 */
	private TableauNode to;

	/**
	 * Creates a new edge between from and to.
	 * @param from the from node.
	 * @param to the to node.
	 */
	public TableauEdge(TableauNode from, TableauNode to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Returns the start node of this edge.
	 * @return the start node.
	 */
	public TableauNode getFrom() {
		return from;
	}

	/**
	 * Returns the end node of this edge.
	 * @return the end node.
 	 */
	public TableauNode getTo() {
		return to;
	}

	/**
	 * Checks if two edges are equal by checking if both their
	 * start and end nodes are equal.
	 * @return true if obj equals this, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof TableauEdge))
			return false;
		
		if(this == obj)
			return true;
		
		TableauEdge other = (TableauEdge) obj;
		return from.equals(other.from) && to.equals(other.to);
	}
}
