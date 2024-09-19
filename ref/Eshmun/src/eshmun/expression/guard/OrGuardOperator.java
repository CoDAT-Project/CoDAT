package eshmun.expression.guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.OrOperator;

/**
 * 
 */
/**
 * Represents an OR Operator on Guards.
 * Semantically, this means that at least one inner guard expression must be true for this to be true.
 * The resulting assignment is the combined assignment of any inner guard expression that is satisfied. 
 * 
 * <p>
 * <i>Invariant</i>: it is guaranteed that no direct child of any OrGuardOperator is an OrGuardOperator.
 * </p>
 * 
 * @author chukris
 *
 */
public class OrGuardOperator extends GuardExpression {
	
	public static final String OR_GUARD_DELIMITER = "_|_";
	public static final char OR_GUARD_SYMBOL = '\u2295';
	
	/**
	 * The Identity element for this operator. 
	 */
	public static final AtomicGuardExpression IDENTITY = new AtomicGuardExpression(OrOperator.IDENTITY);
	
	/**
	 * All the children of the OrGuard as a list.
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrGuardOperator is an OrGuardOperator.
	 * </p>
	 */
	private ArrayList<GuardExpression> children;
	
	/**
	 * Creates a new Empty Or Guard.
	 */
	public OrGuardOperator() {
		this.children = new ArrayList<>();
	}
	
	/**
	 * Create a new OrGuardOperator with the given children.
	 * 
	 * <p>if any of the children is an OrGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrGuardOperator is an OrGuardOperator.
	 * </p>
	 * 
	 * @param children the children of the OrGuardOperator.
	 */
	public OrGuardOperator(GuardExpression... children) {			
		this.children = new ArrayList<>(children.length);
		
		for(GuardExpression exp : children)
			orGuard(exp);
	}
	
	/**
	 * Create a new OrGuardOperator with the given children.
	 * 
	 * <p>if any of the children is an OrGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrGuardOperator is an OrGuardOperator.
	 * </p>
	 * 
	 * @param children the children of the OrGuardOperator.
	 */
	public OrGuardOperator(ArrayList<GuardExpression> children) {			
		this.children = new ArrayList<>(children.size());
		
		for(GuardExpression exp : children)
			orGuard(exp);
	}
	
	/**
	 * Adds a new guard expression to the or statement.
	 * 
	 * <p>if the child is an OrGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrGuardOperator is an OrGuardOperator.
	 * </p>
	 * 
	 * @param exp the child to be added.
	 */
	public void orGuard(GuardExpression exp) {
		if(exp instanceof OrGuardOperator) {
			this.children.addAll(((OrGuardOperator) exp).children);
		} else {
			this.children.add(exp);
		}
		
	}

	/**
	 * Getter for the children of this OrGuard.
	 * @return the children as an array.
	 */
	public GuardExpression[] getChildren() {
		return children.toArray(new GuardExpression[children.size()]);
	}
	
	/**
	 * Getter for the children of this OrGuard.
	 * @return the children as an array list.
	 */
	public ArrayList<GuardExpression> getChildrenList() {
		return new ArrayList<>(children);
	}
	

	/**
	 * {@inheritDoc}
	 * @return true if any of the inner expressions reference the variable (in guard or assignment).
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		for(GuardExpression exp : children)
			if(exp.containsVariable(variable))
				return true;
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if any of the inner expressions reference the variable (in guard or assignment).
	 */
	@Override
	public boolean containsVariable(String variableName) {
		for(GuardExpression exp : children)
			if(exp.containsVariable(variableName))
				return true;
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashSet<HashSet<String>> satisfyGuard(Collection<String> state, ArrayList<String> processes) throws IllegalStateException {
		boolean satisfied = false;
		
		HashSet<HashSet<String>> effects = new HashSet<>();
		for(GuardExpression exp : children) {
			try {
				effects.addAll(exp.satisfyGuard(state, processes));
				satisfied = true;
			} catch(IllegalStateException ex) { }
		}
		
		if(satisfied) return effects;
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuardExpression simplify() {
		// TODO SIMPLIFY OR OPERATOR
		return this.clone();
	}

	/**
	 * Two OrGuardOperators are equal if they contain the same set of children (regardless of order).
	 */
	@Override
	protected boolean directCompare(GuardExpression exp) {
		OrGuardOperator other = (OrGuardOperator) exp;
		return this.children.containsAll(other.children) && other.children.contains(this.children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrGuardOperator clone() {
		return new OrGuardOperator(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < children.size(); i++) {
			if(i > 0) result += " " + OR_GUARD_DELIMITER + " ";
			result += children.get(i).toString();
		}
		
		if(children.size() > 1) result = "(" + result + ")";
		return result;
	};
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toSymbolsString() {
		String result = "";
		for(int i = 0; i < children.size(); i++) {
			if(i > 0) result += OR_GUARD_SYMBOL;
			result += children.get(i).toSymbolsString();
		}
		
		if(children.size() > 1) result = "(" + result + ")";
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(GuardVisitor visitor) {
		visitor.visit(this);
		for(GuardExpression child : children)
			child.accept(visitor);
	}
}
