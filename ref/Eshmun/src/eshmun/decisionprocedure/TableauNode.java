package eshmun.decisionprocedure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;

/**
 * An abstract class that represents node in the tableau, the node
 * could either be an AndNode or an OrNode.
 * 
 * <p>A node contains a list of incoming and outgoing edges, as well as
 * a formula (AbstractExpression).</p>
 * 
 * @author Mouhamad Sakr, Kinan Dak Al Bab
 * @since 0.1
 */
public abstract class TableauNode {
	/**
	 * The Outgoing edges.
	 */
	protected List<TableauEdge> edges;
	
	/**
	 * The Incoming edges.
	 */
	protected List<TableauEdge> incomingEdges;
	
	/**
	 * The Formulae inside this node, the list represents a big
	 * conjunction.
	 */
	protected List<AbstractExpression> formulae;
	
	/**
	 * Counter for unique name.
	 */
	private static int counter = 0;
	
	/**
	 * Unique name.
	 */
	private String name;
	
	/**
	 * Gets the unique name of the node.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Create a node with the given formula.
	 * @param formula the formula of the node.
	 */
	public TableauNode(AbstractExpression formula) {
		counter++;
		
		name = this instanceof AndNode ? "and" : "or";
		name += counter;
		
		edges = new LinkedList<TableauEdge>();
		incomingEdges = new LinkedList<TableauEdge>();
		
		formulae = new ArrayList<AbstractExpression>();
		formulae.add(formula);
	}
	
	/**
	 * Create a node with the given list of formulae (conjunction).
	 * @param formulae the conjunction of all these formulae is the formula of this node.
	 */
	public TableauNode(List<AbstractExpression> formulae) {
		counter++;
		
		name = this instanceof AndNode ? "and" : "or";
		name += counter;
		
		edges = new ArrayList<TableauEdge>();
		incomingEdges = new ArrayList<TableauEdge>();
		
		this.formulae = formulae;
	}
	
	/**
	 * Returns the out going edges.
	 * @return the out going edges.
	 */
	public List<TableauEdge> getEdges() {
		return edges;
	}

	/**
	 * Returns the in coming edges.
	 * @return the in coming edges.
	 */
	public List<TableauEdge> getIncomingEdges() {
		return incomingEdges;
	}
				
	/**
	 * Adds an outgoing edge.
	 * @param edge the outgoing edge.
	 */
	public void addEdge(TableauEdge edge) {
		edges.add(edge);
	}
	
	/**
	 * Adds an incoming edge.
	 * @param edge the incoming edge.
	 */
	public void addIncomingEdge(TableauEdge edge) {
		incomingEdges.add(edge);
	}
	
	/**
	 * Deletes an out going edge.
	 * @param edge the edge to delete.
	 */
	public void deleteEdge(TableauEdge edge) {
		edges.remove(edge);
	}
		
	/**
	 * Deletes an in coming edge.
	 */
	public void deleteIncomingEdges() {
		incomingEdges = new LinkedList<TableauEdge>();
	}
	
	/**
	 * Checks whether this node has children.
	 * @return true if this node has children, false otherwise.
	 */
	public boolean hasChildren() {
		return edges.size() > 0;
	}
	
	/**
	 * Returns the formulae inside this node.
	 * @return the formulae in this node.
	 */
	public List<AbstractExpression> getFormulae() {
		return formulae;
	}
	
	/**
	 * Checks if the given formula is contained inside the node.
	 * @param frm the formula to look for.
	 * @param T the tableau in which to look (the tableau that contains this node).
	 * @return true if frm is contained in this node, false otherwise.
	 */
	public boolean formulaHoldsIn(AbstractExpression frm, Tableau T) {
		if(formulae.contains(frm))
			return true;
		
		if(frm.getType() == ExpressionType.BooleanLiteral) {
			return ((BooleanLiteral) frm).getValue();
		}
		
		if(frm.getType() == ExpressionType.NotOperator) {
			return !formulaHoldsIn(((NotOperator) frm).getChild(), T);
		}
		
		if(frm.getType() == ExpressionType.AndOperator) {
			for(AbstractExpression e : ((AndOperator) frm).getChildren()) {
				if(!formulaHoldsIn(e, T))
					return false;
			}
			
			return true;
		}
		
		if(frm.getType() == ExpressionType.OrOperator) {
			for(AbstractExpression e : ((OrOperator) frm).getChildren()) {
				if(formulaHoldsIn(e, T))
					return true;
			}
			
			return false;
		}
		
		if(frm.getType() == ExpressionType.AUModality) {
			AUModality auMod = (AUModality) frm;
			return T.CheckAU(this, auMod.getLeftChild(), auMod.getRightChild(), new HashSet<TableauNode>());
		}
		
		if(frm.getType() == ExpressionType.EUModality) {
			EUModality euMod = (EUModality) frm;
			return T.CheckEU(this, euMod.getLeftChild(), euMod.getRightChild());
		}
		
		return false;
	}
			
	/**
	 * Checks if this node's formula is inconsistent.
	 * @return true if the formula is inconsistent, false otherwise.
	 */
	public boolean isInconsistent() {
		for(AbstractExpression e : formulae) {
			if(e.getType() == ExpressionType.BooleanLiteral)
				if(!((BooleanLiteral) e).getValue())
					return true;
			
			if(e.getType() == ExpressionType.BooleanVariable)
				for(AbstractExpression c : formulae) 
					if(c.getType() == ExpressionType.NotOperator)
						if(((NotOperator) c).getChild().equals(e))
							return true;
			
		}
		
		return false;
	}
	
	/**
	 * Checks if this node's formula contains an AUModality.
	 * @return a list of all AUModalities in this node.
	 */
	public List<AUModality> getAllAU() {
		List<AUModality> result = new LinkedList<AUModality>();
		
		for(AbstractExpression f : formulae) {
			if(f.getType() == ExpressionType.AUModality)
				result.add((AUModality) f);
		}
		
		return result;
	}
	
	/**
	 * Checks if this node's formula contains an EUModality.
	 * @return a list of all EUModalities in this node.
	 */
	public List<EUModality> getAllEU() {
		List<EUModality> result = new LinkedList<EUModality>();

		for(AbstractExpression f : formulae) {
			if(f.getType() == ExpressionType.EUModality)
				result.add((EUModality) f);
		}
		
		return result;
	}
}
