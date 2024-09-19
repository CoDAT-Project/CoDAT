package eshmun.expression.guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import eshmun.expression.AbstractExpression;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.visitors.GuardSatisfyVisitor;

/**
 * Represents an Atomic Guard.
 * @author kinan
 *
 * Atomic guards are the building block for guard expressions. They are formatted like this:
 *    (the guard's boolean expression) -&gt; (var1:=val1); (var2:=val2); ...
 *    
 * The boolean expression is saved as an AbstractExpression, while the assignments are a list
 * of boolean variables. The names of these variables contain the assignment and value as well.
 */
public class AtomicGuardExpression extends GuardExpression {
	
	public static final String ASSIGNMENTS_DELIMITER = "->";
	public static final char ASSIGNMENTS_SYMBOL = '\u279E';
	public static final String ASSIGNMENTS_SEPERATOR = ";";
	
	public static final String BOTTOM_DELIMITER = "/|\\";
	public static final char BOTTOM_SYMBOL = '\u22A5';
		
	/**
	 * The guard's boolean expression.
	 */
	private AbstractExpression guard;
	
	/**
	 * A list of assignments that are the effect of this guard.
	 */
	private ArrayList<BooleanVariable> assignments;
	
	/**
	 * Empty guard and assignments.
	 */
	public AtomicGuardExpression() {
		this(new BooleanLiteral(true));
	}
	
	/**
	 * Creates a new Atomic Guard Expression, with the given guard and empty assignment.
	 * @param guard the guard of the expression.
	 */
	public AtomicGuardExpression(AbstractExpression guard) {
		this(guard, new ArrayList<BooleanVariable>());
	}
	
	/**
	 * Creates a new Atomic Guard Expression, with the given guard and assignments.
	 * @param guard the guard of the expression.
	 * @param assignments the assignments in a list (duplicates will be ignored).
	 */
	public AtomicGuardExpression(AbstractExpression guard, ArrayList<BooleanVariable> assignments) {
		this.guard = guard;
		this.assignments = new ArrayList<>(new HashSet<BooleanVariable>(assignments));
	}
	
	/**
	 * @return The Guard's boolean expression.
	 */
	public AbstractExpression getGuard() {
		return guard;
	}
	
	/**
	 * @return A shallow copy of the assignments list.
	 */
	public ArrayList<BooleanVariable> getAssignments() {
		return new ArrayList<>(assignments);
	}
		
	/**
	 * Add an Assignment to this guard expression.
	 * If the assignment is a duplicate, it will be ignored.
	 * @param assignment the new assignment.
	 */
	public void addAssignment(BooleanVariable assignment) {
		if(assignments.contains(assignment)) return;
		assignments.add(assignment);
	}
	
	/**
	 * @return true if the guard or assignments contain the given variable.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return assignments.contains(variable) || guard.containsVariable(variable);
	}

	/**
	 * @return true if the guard or assignments contain the given variable.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		for(BooleanVariable var : assignments) if(var.containsVariable(variableName)) return true;
		return guard.containsVariable(variableName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashSet<HashSet<String>> satisfyGuard(Collection<String> state, ArrayList<String> processes) throws IllegalStateException {
		GuardSatisfyVisitor visitor = new GuardSatisfyVisitor(state, processes);
		if(!visitor.run(guard)) throw new IllegalStateException();
		
		HashSet<String> effects = new HashSet<>(visitor.getActions());
		for(BooleanVariable v : assignments) 
			effects.add(v.getName());
		
		HashSet<HashSet<String>> result = new HashSet<>();
		result.add(effects);
		return result;
	}

	/**
	 * @return a cloned (shallow copy) instance, this is an atomic expression which cannot be simplified more.
	 */
	@Override
	public GuardExpression simplify() {
		return this.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(GuardExpression exp) {
		AtomicGuardExpression other = (AtomicGuardExpression) exp;
		
		return guard.equals(other.guard) && assignments.containsAll(other.assignments) && other.assignments.containsAll(assignments);
	}

	/**
	 * Clones this GuardExpression.
	 * 
	 * <p>Please note that while GuardExpression is copied, it is a shallow copy.
	 * The assignment list is copied but not the elements themselves, 
	 * similarly the guard is copied by reference.</p>
	 * @return a shallow copy of this GuardExpression.
	 */
	@Override
	public AtomicGuardExpression clone() {
		return new AtomicGuardExpression(guard, new ArrayList<BooleanVariable>(assignments));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return internalToString(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toSymbolsString() {
		return internalToString(true);
	}
		
	/**
	 * @param symb if true use unicode symbols, otherwise use text.
	 * @return A readable string with the operator represented as symb.
	 */
	private String internalToString(boolean symb) {
		String result = guard.toString(true).trim();
		boolean resultIsComposite = result.contains("&") || result.contains("|")
				|| result.contains("=>");
		
		if(assignments.size() > 0) result += symb ? ASSIGNMENTS_SYMBOL : ASSIGNMENTS_DELIMITER;
		
		for(BooleanVariable assignment : assignments) {
			String assign = assignment.getName().trim();
			if(symb) assign = assign.replace(BOTTOM_DELIMITER, BOTTOM_SYMBOL+"");
			result += assign + ASSIGNMENTS_SEPERATOR;
		}
		
		if(assignments.size() > 0 || resultIsComposite)
			return "(" + result + ")";
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(GuardVisitor visitor) {
		visitor.visit(this);
	}
}
