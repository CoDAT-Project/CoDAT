package eshmun.modelchecker;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.AWModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.binary.EWModality;
import eshmun.expression.modalities.unary.AFModality;
import eshmun.expression.modalities.unary.AGModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EFModality;
import eshmun.expression.modalities.unary.EGModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.structures.AbstractState;

/**
 * This class defines an abstract model checker, implementing sub classes may 
 * implement a variety of specific algorithms. 
 *
 * @see eshmun.expression.visitor.Visitable
 * @see eshmun.modelchecker.AbstractModelChecker
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.structures.AbstractStructure
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public abstract class AbstractModelChecker {	
	/**
	 * Calls the right visit method and pass it the parameters depending on the 
	 * type of the expression. 
	 * 
	 * @param specifications is the CTL formula to be model checked
	 * @param state is the state in which tro model check the CTL formula specifications
	 * @param index the remaining states to traverse before reaching the total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index,
	 * 		   this result is complete when the state is the start state and specifications are the entire specs.
	 */
	protected boolean visit(AbstractExpression specifications, AbstractState state, int index) {
		//specifications.getType() returns the type of the top level operator of the CTL formula specifications
		if(specifications.getType() == ExpressionType.AndOperator) {
			return visit((AndOperator)specifications, state, index);
		} else if(specifications.getType() == ExpressionType.OrOperator) {
			return visit((OrOperator) specifications,state, index);
		} else if(specifications.getType() == ExpressionType.NotOperator) {
			return visit((NotOperator)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.BooleanVariable) {
			return visit((BooleanVariable)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.BooleanLiteral) {
			return visit((BooleanLiteral)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.ImplicationOperator) {
			return visit((ImplicationOperator)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EquivalenceOperator) {
			return visit((EquivalenceOperator)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AFModality) {
			return visit((AFModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AGModality) {
			return visit((AGModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AXModality) {
			return visit((AXModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EFModality) {
			return visit((EFModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EGModality) {
			return visit((EGModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EXModality) {
			return visit((EXModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AUModality) {
			return visit((AUModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AWModality) {
			return visit((AWModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EUModality) {
			return visit((EUModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EWModality) {
			return visit((EWModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.AVModality) {
			return visit((AVModality)specifications,state, index);
		} else if(specifications.getType() == ExpressionType.EVModality){
			return visit((EVModality)specifications,state, index);
		}
		
		return false;
	}
	
	/**
	 * Model-checks a structure against CTL specifications, the structure should be provided elsewhere.
	 * 
	 * <p>This method should be overridden in the concrete model checker.</p>
	 * 
	 * @param specifications CTL specifications to model-check against.
	 * @return true if the structure satisfies the specifications, false otherwise.
	 */
	public abstract boolean run(AbstractExpression specifications);
	
	/**
	 * Visits a BooleanVariable.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public abstract boolean visit(BooleanVariable v, AbstractState state, int index);
	
	/**
	 * Visits a BooleanLiteral.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	public abstract boolean visit(BooleanLiteral v, AbstractState state, int index);
	
	/**
	 * Visits a NotOperator.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.operators.NotOperator
	 */
	public abstract boolean visit(NotOperator v, AbstractState state, int index);
	
	/**
	 * Visits a AndOperator.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.operators.AndOperator
	 */
	public abstract boolean visit(AndOperator v, AbstractState state, int index);
	
	/**
	 * Visits a OrOperator.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.operators.OrOperator
	 */
	public abstract boolean visit(OrOperator v, AbstractState state, int index);
	
	/**
	 * Visits a ImplicationOperator.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.operators.ImplicationOperator
	 */
	public abstract boolean visit(ImplicationOperator v, AbstractState state, int index);
	
	/**
	 * Visits a EquivalenceOperator.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	public abstract boolean visit(EquivalenceOperator v, AbstractState state, int index);
	
	/**
	 * Visits a AFModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.AFModality
	 */
	public abstract boolean visit(AFModality  v, AbstractState state, int index);
	
	/**
	 * Visits a AGModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.AGModality
	 */
	public abstract boolean visit(AGModality  v, AbstractState state, int index);
	
	/**
	 * Visits a AXModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.AXModality
	 */
	public abstract boolean visit(AXModality  v, AbstractState state, int index);
	
	/**
	 * Visits a EFModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.EFModality
	 */
	public abstract boolean visit(EFModality  v, AbstractState state, int index);
	
	/**
	 * Visits a EGModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.EGModality
	 */
	public abstract boolean visit(EGModality  v, AbstractState state, int index);
	
	/**
	 * Visits a EXModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	public abstract boolean visit(EXModality  v, AbstractState state, int index);
	
	/**
	 * Visits a AUModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	public abstract boolean visit(AUModality  v, AbstractState state, int index);
	
	/**
	 * Visits a AWModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.AWModality
	 */
	public abstract boolean visit(AWModality  v, AbstractState state, int index);
	
	/**
	 * Visits a EUModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.EUModality
	 */
	public abstract boolean visit(EUModality  v, AbstractState state, int index);
	
	/**
	 * Visits a EWModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.EWModality
	 */
	public abstract boolean visit(EWModality  v, AbstractState state, int index);

	
	/**
	 * Visits a AVModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	public abstract boolean visit(AVModality v, AbstractState state, int index);
	
	/**
	 * Visits a EVModality.
	 * @param v the visited element.
	 * @param state the current state of the structure.
	 * @param index the remaining state to traverse before reaching total number of states.
	 * @return the result of the (partial) modelChecking of state against the specifications with the index.
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	public abstract boolean visit(EVModality v, AbstractState state, int index);
}