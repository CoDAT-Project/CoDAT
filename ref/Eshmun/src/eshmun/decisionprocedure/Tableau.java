package eshmun.decisionprocedure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import eshmun.expression.AbstractExpression;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.EUModality;

/**
 * 
 * @author Mouhamad Sakr, Kinan Dak Al Bab
 * @since 0.1
 * 
 * Represents a Tableau of a formula, a tableau has potential models of the formula
 * embedded in it. The tableau has a root, and it is on the form of a directed graph.
 * 
 * 
 */
public class Tableau {
	/**
	 * The root of the tableau.
	 */
	private OrNode root;
	
	/**
	 * A List of all the andNodes.
	 */
	private List<AndNode> andNodes;
	
	/**
	 * A List of all the orNodes.
	 */
	private List<OrNode> orNodes;
	
	/**
	 * A List of all the edges.
	 */
	private List<TableauEdge> edges;
	
	/**
	 * A List of all nodes with no successors.
	 */
	private List<TableauNode> noSuccessors;
	
	/**
	 * Creates a new tableau for the given formula.
	 * @param formula the formula to create a tableau for.
	 */
	public Tableau(AbstractExpression formula) {
		root = new OrNode(formula);
		
		andNodes = new LinkedList<AndNode>(); 
		orNodes = new LinkedList<OrNode>();
		edges = new LinkedList<TableauEdge>();
		noSuccessors = new LinkedList<TableauNode>();
		
		orNodes.add(root);
		noSuccessors.add(root);
	}
	
	/**
	 * Gets the root of the tableau.
	 * @return the root.
	 */
	public OrNode getRoot() {
		return root;
	}
	
	/**
	 * Checks if the root has been deleted.
	 * @return true if the root has not been deleted, false otherwise.
	 */
	public boolean hasRoot() {
		return orNodes.contains(root);
	}
	
	/**
	 * Returns a list of Nodes with no successors.
	 * @return a list of noSuccessors.
	 */
	public List<TableauNode> getNoSuccessors() {
		return new ArrayList<TableauNode>(noSuccessors);
	}
	
	/**
	 * Add the given node to the tableau, if the node already exist, 
	 * a pointer for the old node is returned.
	 * @param node the node to be added.
	 * @return a pointer to either the node itself, or a previously equal added node.
	 */
	public TableauNode addNode(TableauNode node) {
		if(node instanceof AndNode) { //And Node
			for(AndNode n : andNodes) {
				if(n.equals(node)) {
					return n;
				}
			}
			
			andNodes.add((AndNode) node);
		} else { //Or Node
			for(OrNode n : orNodes) {
				if(n.equals(node)) {
					return n;
				}
			}
			
			orNodes.add((OrNode) node);
		}
				
		noSuccessors.add(node);
		return node;
	}
	
	/**
	 * Checks if the tableau contains the given node.
	 * @param node the node to check for.
	 * @return true if node is already in the tableau, false otherwise.
	 */
	public boolean containsNode(TableauNode node) {
		if(node instanceof AndNode) { //And Node
			for(AndNode n : andNodes) {
				if(n.equals(node)) {
					return true;
				}
			}
		} else { //Or Node
			for(OrNode n : orNodes) {
				if(n.equals(node)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Deletes the given node.
	 * @param node the node to be deleted.
	 */
	private void deleteNode(TableauNode node) {
		if(node instanceof AndNode) { //And Node
			andNodes.remove(node);
		} else { //Or Node
			orNodes.remove(node);
		}
		
		for(TableauEdge e : node.incomingEdges) {
			e.getFrom().deleteEdge(e);
			edges.remove(e);
		}
		
		node.deleteIncomingEdges();
		
		noSuccessors.remove(node);
	}
	
	/**
	 * Adds an edge between from and to, removes from from the list of nodes with
	 * no successors, adds the edge inside from and to as well.
	 * @param from the node from which the edge starts.
	 * @param to the node in which the edge ends.
	 */
	public void addEdge(TableauNode from, TableauNode to) {
		TableauEdge edge = new TableauEdge(from, to);
		edges.add(edge);
		
		from.addEdge(edge);
		to.addIncomingEdge(edge);
		noSuccessors.remove(from);		
	}
			
	/**
	 * Test this tableau for consistency by deleting inconsistent nodes, applies the deletion rules
	 * specified in Emerson and Clarke 82.
	 * 
	 * <p>Note that the AF, EF rules are redundant, since expressions are reduced beforehand.</p>
	 */
	public void deletionRules() {
		boolean changed = true;
		while(changed) {
			boolean ch1 = DeleteP();
			boolean ch2 = DeleteOr();
			boolean ch3 = DeleteAND();
			boolean ch4 = DeleteEU();
			boolean ch5 = DeleteAU();

			if(!ch1 && !ch2 && !ch3 && !ch4 && !ch5)
				changed = false;				
		}
	}
	
	/**
	 * Delete The nodes that contain p with its negation, or false.
	 * @return true if the tableau was changed, false otherwise.
	 */
	private boolean DeleteP() {
		LinkedList<TableauNode> toDelete = new LinkedList<TableauNode>();
		
		for (AndNode and : andNodes) {
			if(and.isInconsistent()) {
				toDelete.push(and);
			}
		}
		
		for (OrNode or : orNodes) {
			if(or.isInconsistent()) {
				toDelete.push(or);
			}
		}
		
		for(TableauNode n : toDelete) {
			deleteNode(n);
		}
		
		return !toDelete.isEmpty();
	}
	
	
	/**
	 * Delete The OrNodes which all of their successors were deleted.
	 * @return true if the tableau was changed, false otherwise.
	 */
	private boolean DeleteOr() {
		LinkedList<TableauNode> toDelete = new LinkedList<TableauNode>();

		for(OrNode or : orNodes) {	
			if(!or.hasChildren()) toDelete.push(or);
		}
		
		for(TableauNode n : toDelete) {
			deleteNode(n);
		}
		
		return !toDelete.isEmpty();
	}
	
	/**
	 * Delete any AndNode that had one child deleted.
	 * @return true if the tableau was changed, false otherwise.
	 */
	private boolean DeleteAND() {
		LinkedList<TableauNode> toDelete = new LinkedList<TableauNode>();
		
		for (AndNode and : andNodes) {
			if(and.isToBeDeleted()) toDelete.push(and);
		}	

		for(TableauNode n : toDelete) {
			deleteNode(n);
		}
		
		return !toDelete.isEmpty();
	}
	
	/**
   	 * Delete any node that contain an AUModality and there does not exist a full sub 
	 * directed graph rooted at that node with the given properties.
	 * 
	 * <p>The right child of AU must hold on every node on the frontier of the sub graph.</p>
	 * <p>The left child of AU must hold on every node in the sub graph that's not on the frontier.</p>  
	 * 
	 * @return true if the tableau was changed, false otherwise.
	 */
	private boolean DeleteAU() {
		LinkedList<TableauNode> toDelete = new LinkedList<TableauNode>();

		for (AndNode and : andNodes) {
			for(AUModality auOp : and.getAllAU()) {
				if(!CheckAU(and, auOp.getLeftChild(), auOp.getRightChild(), new HashSet<TableauNode>()))	{
					toDelete.push(and);
					break;
				}
			}
		}
		for (OrNode or : orNodes) {
			for(AUModality auOp : or.getAllAU()) {
				if(!CheckAU(or, auOp.getLeftChild(), auOp.getRightChild(), new HashSet<TableauNode>())) {
					toDelete.push(or);
					break;
				}
			}
		}

		for(TableauNode n : toDelete) {
			deleteNode(n);
		}
		
		return !toDelete.isEmpty();
	}
	
	/**
	 * Delete any node that contains an EUModality that doesn't satisfy the given properties.
	 * 
	 * <p>There exist an AndNode reachable from the node where the right child of EU holds.</p>
	 * <p>There exist a path to that AndNode from the node where the left child of EU holds 
	 * in every AndNode on the path.</p>
	 * 
	 * @return true if the tableau was changed, false otherwise.
	 */ 
	private boolean DeleteEU()	{
		LinkedList<TableauNode> toDelete = new LinkedList<TableauNode>();

		for (AndNode and : andNodes) {
			for(EUModality euOp : and.getAllEU()) {
				if(!CheckEU(and, euOp.getLeftChild(), euOp.getRightChild())) {
					toDelete.push(and);
					break;
				}
			}
		}
		for (OrNode or : orNodes) {
			for(EUModality euOp : or.getAllEU()) {
				if(!CheckEU(or, euOp.getLeftChild(), euOp.getRightChild())) {
					toDelete.push(or);
					break;
				}
			}
		}
		
		for(TableauNode n : toDelete) {
			deleteNode(n);
		}
		
		return !toDelete.isEmpty();
	}
	
	/**
	 * Checks if bNode is at the root of a full sub directed graph that satisfies the following properties.
	 * 
	 * <p>g must hold on every AndNode on the frontier of the sub graph.</p>
	 * <p>f must hold on every AndNode in the sub graph that's not on the frontier.</p>  
	 * 
	 * @param bNode the Node to investigate about.
	 * @param f the formula that has to hold inside the sub graph.
	 * @param g the formula that has to hold at the frontier.
	 * @param visited keeps a set of already visited nodes.
	 * @return true if such sub graph was found, false otherwise.
	 */
	public boolean CheckAU(TableauNode bNode, AbstractExpression f, AbstractExpression g, Set<TableauNode> visited) {
		HashSet<TableauNode> tmp = new HashSet<TableauNode>(visited);
		tmp.add(bNode);
		if(bNode instanceof AndNode && bNode.formulaHoldsIn(g, this))
			return true;
		
		if(bNode instanceof AndNode && !bNode.formulaHoldsIn(f, this))
			return false;
		
		if(bNode instanceof AndNode) {
			for(TableauEdge e : bNode.getEdges()) {
				TableauNode n = e.getTo();
				if(tmp.contains(n)) return false;
			}
		} else {
			boolean flag = false;
			for(TableauEdge e : bNode.getEdges()) {
				TableauNode n = e.getTo();
				if(!tmp.contains(n)) flag = true;
			}
			
			if(!flag) return false;
		}
		
		for(TableauEdge e : bNode.getEdges()) {
			TableauNode n = e.getTo();
			if(!CheckAU(n, f, g, tmp)) {
				if(bNode instanceof AndNode) 
					return false;
			} else {
				if(bNode instanceof OrNode)
					return true;
			}
		}
		
		return bNode instanceof AndNode;
	}
	
	/**
	 * Checks if bNode satisfy the following properties.
	 * 
	 * <p>There exist an AndNode reachable from bNode where the g holds.</p>
	 * <p>There exist a path to that AndNode from the node where the f in every 
	 * AndNode on the path.</p>
	 * 
	 * @param bNode the Node to investigate about.
	 * @param f the formula that has to hold on the path.
	 * @param g the formula that has to hold at the end.
	 * @return true if bNode satisfy the properties, false otherwise.
	 */
	public boolean CheckEU(TableauNode bNode, AbstractExpression f, AbstractExpression g) {
		Queue<TableauNode> queue = new LinkedList<TableauNode>(); //BFS Queue
		List<TableauNode> visited = new ArrayList<TableauNode>(); //BFS Visited
		
		queue.add(bNode);
		visited.add(bNode); //Visited one-step ahead
		
		while(!queue.isEmpty()) { //BFS 
			TableauNode current = queue.poll();
			
			if(current instanceof AndNode) { //And Node
				if(current.formulaHoldsIn(g, this)) { //if g holds, then we found the path
					return true;
				} else if(current.formulaHoldsIn(f, this)) { //if f holds, this node could be on the path
					List<TableauEdge> edges = current.getEdges();
					for (TableauEdge edge : edges) {
						TableauNode child = edge.getTo();
						if(!visited.contains(child)) { //not visited
							visited.add(child);
							queue.add(child);
						}
					}
				} //otherwise, dead end
				
			} else { //OrNode
				List<TableauEdge> edges = current.getEdges();
				for (TableauEdge edge : edges) { //add new children and keep going
					TableauNode child = edge.getTo();
					if(!visited.contains(child)) { //not visited
						visited.add(child);
						queue.add(child);
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Outputs the tableau in a format readable by Eshmun.
	 * @return the string definition of the tableau.
	 */
	public String getStringDefinition() {
		String s = "States:" + System.lineSeparator();
		String e = "Transitions:" + System.lineSeparator();
		
		Queue<TableauNode> queue = new LinkedList<TableauNode>(); //BFS Queue
		List<TableauNode> visited = new ArrayList<TableauNode>(); //BFS Visited
		
		queue.add(root);
		visited.add(root); //Visited one-step ahead
		
		while(!queue.isEmpty()) { //BFS
			TableauNode current = queue.poll();
			
			String state = current.getName();
			if(current instanceof AndNode) { //And Node
				List<TableauEdge> edges = current.getEdges();
				for (TableauEdge edge : edges) {
					TableauNode child = edge.getTo();
					e += current.getName() + ":" + child.getName() + ":false;" + System.lineSeparator();
					if(!visited.contains(child)) { //not visited
						visited.add(child);
						queue.add(child);
					}
				}
				
			} else { //OrNode
				List<TableauEdge> edges = current.getEdges();
				for (TableauEdge edge : edges) { //add new children and keep going
					TableauNode child = edge.getTo();
					e += current.getName() + ":" + child.getName() + ":false;" + System.lineSeparator();
					if(!visited.contains(child)) { //not visited
						visited.add(child);
						queue.add(child);
					}
				}
			}
			
			state += ":";
			for(AbstractExpression a : current.formulae) {
				state += a +",";
			}
			
			if(current.formulae.size() > 0)
				state = state.substring(0, state.length() - 1);
			
			
			String extra = "false";
			if(current instanceof AndNode && ((AndNode) current).isToBeDeleted()) {
				extra = "true";
			}
			state += ":" + (root == current ? "true" : "false") + ":" + extra + ";";
			s += state + System.lineSeparator();
		}
		
		return ("Tableau\n"+s+e).replaceAll(" ", "");
	}
}
