package eshmun.expression.guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.AndOperator;

/**
 * Represents an AND Operator on Guards.
 * Semantically, this means that all inner guard expression must be true for this to be true, and all their
 * effects/assignments are combined into this assignment. 
 * 
 * <p>
 * <i>Invariant</i>: it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
 * </p>
 * 
 * @author kinan
 *
 */
public class AndGuardOperator extends GuardExpression {
	
	public static final String AND_GUARD_DELIMITER = "_&_";
	public static final char AND_GUARD_SYMBOL = '\u2297';
	
	/**
	 * The Identity element for this operator. 
	 */
	public static final AtomicGuardExpression IDENTITY = new AtomicGuardExpression(AndOperator.IDENTITY);
	
	/**
	 * All the children of the AndGuard as a list.
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
	 * </p>
	 */
	private ArrayList<GuardExpression> children;
	
	/**
	 * Creates a new Empty And Guard.
	 */
	public AndGuardOperator() {
		this.children = new ArrayList<>();
	}
	
	/**
	 * Create a new AndGuardOperator with the given children.
	 * 
	 * <p>if any of the children is an AndGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
	 * </p>
	 * 
	 * @param children the children of the AndGuardOperator.
	 */
	public AndGuardOperator(GuardExpression... children) {			
		this.children = new ArrayList<>(children.length);
		
		for(GuardExpression exp : children)
			andGuard(exp);
	}
	
	/**
	 * Create a new AndGuardOperator with the given children.
	 * 
	 * <p>if any of the children is an AndGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
	 * </p>
	 * 
	 * @param children the children of the AndGuardOperator.
	 */
	public AndGuardOperator(ArrayList<GuardExpression> children) {			
		this.children = new ArrayList<>(children.size());
		
		for(GuardExpression exp : children)
			andGuard(exp);
	}
	
	/**
	 * Adds a new guard expression to the and statement.
	 * 
	 * <p>if the child is an AndGuardOperator its children will be added. </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
	 * </p>
	 * 
	 * @param exp the child to be added.
	 */
	public void andGuard(GuardExpression exp) {
		if(exp instanceof AndGuardOperator) {
			this.children.addAll(((AndGuardOperator) exp).children);
		} else {
			this.children.add(exp);
		}
		
	}

	/**
	 * Getter for the children of this AndGuard.
	 * @return the children as an array.
	 */
	public GuardExpression[] getChildren() {
		return children.toArray(new GuardExpression[children.size()]);
	}
	
	/**
	 * Getter for the children of this AndGuard.
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
		HashSet<HashSet<String>> effects = null;
		
		// All children should be satisfied.
		for(GuardExpression exp : children) {
			if(effects == null) {
				effects = exp.satisfyGuard(state, processes);
				continue;
			}
			
			// Combine effects of all children.
			HashSet<HashSet<String>> child = exp.satisfyGuard(state, processes);
			HashSet<HashSet<String>> crossProduct = new HashSet<>();
			for(HashSet<String> childEffect : child) { // Cross Product.
				this_element:
				for(HashSet<String> accumulationEffect : effects) {
					HashSet<String> copy = new HashSet<>();
					HashMap<String, String> assignmentMap = new HashMap<>();
					
					// Copy child while checking for contradictions.
					for(String s : childEffect) {
						copy.add(s);
						if(s.contains("=")) {
							String name = s.substring(0, s.indexOf("="));
							String val = s.substring(s.indexOf("=") + 1);
							String oldVal = assignmentMap.put(name, val);
							if(oldVal != null && !oldVal.equals(val))
								continue this_element;
						}
					}
					
					// Copy accumulation while checking for contradictions.
					for(String s : accumulationEffect) {
						copy.add(s);
						if(s.contains("=")) {
							String name = s.substring(0, s.indexOf("="));
							String val = s.substring(s.indexOf("=") + 1);
							String oldVal = assignmentMap.put(name, val);
							if(oldVal != null && !oldVal.equals(val))
								continue this_element;
						}
					}				
					
					crossProduct.add(copy);
				}
				
				effects = new HashSet<>(crossProduct);
			}
		}
				
		return effects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuardExpression simplify() {
		// TODO SIMPLIFY AND GUARD OPERATOR
		return this.clone();
	}

	/**
	 * Two AndGuardOperators are equal if they contain the same set of children (regardless of order).
	 */
	@Override
	protected boolean directCompare(GuardExpression exp) {
		AndGuardOperator other = (AndGuardOperator) exp;
		return this.children.containsAll(other.children) && other.children.contains(this.children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AndGuardOperator clone() {
		return new AndGuardOperator(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < children.size(); i++) {
			if(i > 0) result += " " + AND_GUARD_DELIMITER + " ";
			result += children.get(i).toString();
		}
		
		if(children.size() > 1) result = "(" + result + ")";
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toSymbolsString() {
		String result = "";
		for(int i = 0; i < children.size(); i++) {
			if(i > 0) result += AND_GUARD_SYMBOL;
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
