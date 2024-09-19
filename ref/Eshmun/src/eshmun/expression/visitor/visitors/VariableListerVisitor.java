package eshmun.expression.visitor.visitors;

import java.util.ArrayList;
import java.util.HashMap;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.NotCNFException;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.VisitorAdapter;

/**
 * This visitor lists the variables within an AndExpression, variables are listed in order and grouped by clause.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class VariableListerVisitor extends VisitorAdapter {	
	/**
	 * Saves the distinct variable count.
	 */
	private int variableCount;
	
	/**
	 * Maps IDs to variables
	 */
	private HashMap<Integer, String> ID2Variables;
	
	/**
	 * Maps Variables to IDs.
	 */
	private HashMap<String, Integer> variables2ID;
	
	/**
	 * List of CNF clauses, each clause is a term in the big AND operator.
	 */
	private ArrayList<String> clauseList;
	
	/**
	 * Flags if something illegal occurs.
	 */
	private boolean legal;
	
	/**
	 * Flags if inside OrOperator
	 */
	private boolean inOrOperator;
	
	/**
	 * Flags if inside NotOperator.
	 */
	private boolean inNotOperator;
	
	/**
	 * Flags if inside Boolean Variable / Boolean Literal.
	 */
	private boolean inAtomicOperator;
	
	/**
	 * Flags if it can be satisfiable (no clear contradiction).
	 */
	private boolean satisfiable;
	
	/**
	 * Stores the current clause.
	 */
	private String currentClause;
	
	/**
	 * Flags if an And is to be expected.
	 */
	private boolean expectAnd;
	
	/**
	 * contains false if current clause is unsatisfiable.
	 */
	private boolean currentClauseIsFalse;
		
	public VariableListerVisitor() {
		legal = true;
		satisfiable = true;
		
		variableCount = 0;
		
		ID2Variables = new HashMap<Integer, String>();
		variables2ID = new HashMap<String, Integer>();
		clauseList = new ArrayList<String>();
		
		currentClause = "";
		currentClauseIsFalse = false;
		expectAnd = true;
	}
	
	/**
	 * Lists the variables, maps them to an ID, constructs the clauses as strings.
	 * @param exp the formula to visit.
	 * @return true if everything is legal, false if the given formula is unsatisfiable.
	 * @throws NotCNFException if the given formula is not in CNF format.
	 */
	public boolean listVariables(AbstractExpression exp) throws NotCNFException {
		if(exp.getType() == ExpressionType.AndOperator)
			expectAnd = true;
		
		exp.accept(this);
		
		if(!legal)
			throw new NotCNFException();
		
		if(!currentClause.trim().equals("")) {
			clauseList.add(currentClause + "0" + System.lineSeparator());
			currentClause = "";
		}
		
		return satisfiable;
	}
	
	/**
	 * Gets the number of clauses.
	 * @return the number of clauses.
	 */
	public int getClauseCount() {
		return clauseList.size();
	}
	
	/**
	 * the number of distinct variables in this expression.
	 * @return the count of variables.
	 */
	public int getVariableCount() {
		return variableCount;
	}
	
	/**
	 * Gets the CNF formula in a string, formatted for sat solver.
	 * @return CNF formula in a String format.
	 */
	public String getCNFString() {
		StringBuilder builder = new StringBuilder();
		for(String s : clauseList) {
			builder.append(s);
			builder.append(System.lineSeparator());
		}
		
		return builder.toString();
	}
	
	/**
	 * Gets the clause list.
	 * @return a list of clauses.
	 */
	public ArrayList<String> getClauseList() {
		return clauseList;
	}

	/**
	 * Gets the mapping from IDs to Variable Names.
	 * @return useful for deletion.
	 */
	public HashMap<Integer, String> getIDMap() {
		return new HashMap<Integer, String>(ID2Variables);
	}
	
	/**
	 * Visited an illegal Expression, set legal to false.
	 */
	@Override
	protected void visit(AbstractExpression v) {
		legal = false;
	}
	
	/**
	 * Visit an AndOperator.
	 * 
	 * In valid CNF format, only one And is visited (the head).
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	@Override
	public void visit(AndOperator v) {
		if(!expectAnd) { 
			legal = false;
		}
		
		expectAnd = false;
	}
	
	/**
	 * Visit an OrOperator.
	 * 
	 * Marks the position as in an OrOperator.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	@Override
	public void visit(OrOperator v) {
		if(!legal || !satisfiable)
			return;
		
		inOrOperator = true;
	}
	
	/**
	 * Visit a NotOperator.
	 * 
	 * Marks the next variable as negated.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public void visit(NotOperator v) {
		if(!legal || !satisfiable)
			return;
		
		inNotOperator = true;
	}
	
	/**
	 * Visit a BooleanVariable.
	 * 
	 * The variable is added to the current clause, and to the list (if it wasn't there).
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	@Override
	public void visit(BooleanVariable v) {
		if(!legal || !satisfiable)
			return;
		
		inAtomicOperator = true;
		
		Integer id = variables2ID.get(v.getName());
		
		if(id == null) {
			variableCount++;
			id = variableCount;
			
			variables2ID.put(v.getName(), id);
			if(!v.getName().matches("Z(\\d)+"))
				ID2Variables.put(id, v.getName());
		}
		
		if(inNotOperator)
			currentClause += "-";

		currentClause += id +" ";
	}
	
	/**
	 * Visit a BooleanLiteral.
	 * 
	 * Literals are not allowed by SatSolver,
	 * if the literal is a true in an or, it is replaced by a new free dummy variable.
	 * if the literal is a false in an and, this yields the formula unsatisfiable.
	 * otherwise the literal is ignored (literal is useless).
	 * 
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	@Override
	public void visit(BooleanLiteral v) {
		if(!legal || !satisfiable)
			return;
		
		inAtomicOperator = true;
		
		if(inOrOperator) { //Or
			if(v.getValue()) { //Or true, replace with dummy new variable.
				BooleanVariable var = new BooleanVariable();
				visit(var);
			} else {
				currentClauseIsFalse = true;
			}
		}
		
		else { //And
			if(!v.getValue()) { //And false, unsatisfiable.
				satisfiable = false;
			}
		}
	}
	
	/**
	 * If this child is a child of the and operator, then
	 * the current clause is completed, and must be added to the list of
	 * clauses.
	 */
	@Override
	public void childrenSeparator() {
		if(!legal || !satisfiable)
			return;
		
		if(currentClause.trim().equals("")) {
			if(currentClauseIsFalse)
				satisfiable = false;
			
			return;
		}
		
		if(!inAtomicOperator && !inNotOperator && !inOrOperator) {
			inOrOperator = false;
			
			currentClause += "0" + System.lineSeparator();
			clauseList.add(currentClause);
			currentClause = "";
			currentClauseIsFalse = false;
		}
	}

	/**
	 * Marks the exit of some Expression.
	 * 
	 */
	@Override
	public void separator() {
		if(!legal || !satisfiable)
			return;
		
		if(inAtomicOperator) {
			inAtomicOperator = false;
		} else if(inNotOperator) {
			inNotOperator = false;
		} else if(inOrOperator) {
			inOrOperator = false;
		}
	}
}
