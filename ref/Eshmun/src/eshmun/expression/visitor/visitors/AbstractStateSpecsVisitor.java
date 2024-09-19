package eshmun.expression.visitor.visitors;

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

/**
 * This class defines an abstract visitor with a generic return type 
 * and argument, implementing sub classes may implement a variety of 
 * specific algorithms. 
 *
 * @see eshmun.expression.visitor.Visitable
 * @see eshmun.expression.visitor.visitors.AbstractStateSpecsVisitor
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.structures.AbstractStructure
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public abstract class AbstractStateSpecsVisitor<T extends Object, P extends Object> {	
	/**
	 * Calls the right visit method and pass it the parameters depending on the 
	 * type of the expression. 
	 * 
	 * @param specifications the specifications.
	 * @param arg the generic argument argument to pass with the element.
	 * @return the generic result.
	 */
	protected T visit(AbstractExpression specifications, P arg) {
		if(specifications.getType() == ExpressionType.AndOperator) {
			return visit((AndOperator)specifications, arg);
		} else if(specifications.getType() == ExpressionType.OrOperator) {
			return visit((OrOperator) specifications, arg);
		} else if(specifications.getType() == ExpressionType.NotOperator) {
			return visit((NotOperator)specifications, arg);
		} else if(specifications.getType() == ExpressionType.BooleanVariable) {
			return visit((BooleanVariable)specifications, arg);
		} else if(specifications.getType() == ExpressionType.BooleanLiteral) {
			return visit((BooleanLiteral)specifications, arg);
		} else if(specifications.getType() == ExpressionType.ImplicationOperator) {
			return visit((ImplicationOperator)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EquivalenceOperator) {
			return visit((EquivalenceOperator)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AFModality) {
			return visit((AFModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AGModality) {
			return visit((AGModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AXModality) {
			return visit((AXModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EFModality) {
			return visit((EFModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EGModality) {
			return visit((EGModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EXModality) {
			return visit((EXModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AUModality) {
			return visit((AUModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AWModality) {
			return visit((AWModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EUModality) {
			visit((EUModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EWModality) {
			return visit((EWModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.AVModality) {
			return visit((AVModality)specifications, arg);
		} else if(specifications.getType() == ExpressionType.EVModality) {
			return visit((EVModality)specifications, arg);
		}
		
		throw new IllegalArgumentException("Bad type: " + specifications.getType());
	}
	
	/**
	 * Starts the visitor.
	 * @param specifications the logical expression to visit.
	 * @return the generic result.
	 */
	public abstract T run(AbstractExpression specifications);
	
	/**
	 * Visits a BooleanVariable.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public abstract T visit(BooleanVariable v, P arg);
	
	/**
	 * Visits a BooleanLiteral.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	public abstract T visit(BooleanLiteral v, P arg);
	
	/**
	 * Visits a NotOperator.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	public abstract T visit(NotOperator v, P arg);
	
	/**
	 * Visits a AndOperator.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	public abstract T visit(AndOperator v, P arg);
	
	/**
	 * Visits a OrOperator.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * 
	 * @return the generic result.
	 * @see eshmun.expression.operators.OrOperator
	 */
	public abstract T visit(OrOperator v, P arg);
	
	/**
	 * Visits a ImplicationOperator.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.operators.ImplicationOperator
	 */
	public abstract T visit(ImplicationOperator v, P arg);
	
	/**
	 * Visits a EquivalenceOperator.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	public abstract T visit(EquivalenceOperator v, P arg);
	
	/**
	 * Visits a AFModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.AFModality
	 */
	public abstract T visit(AFModality  v, P arg);
	
	/**
	 * Visits a AGModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.AGModality
	 */
	public abstract T visit(AGModality  v, P arg);
	
	/**
	 * Visits a AXModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.AXModality
	 */
	public abstract T visit(AXModality  v, P arg);
	
	/**
	 * Visits a EFModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.EFModality
	 */
	public abstract T visit(EFModality  v, P arg);
	
	/**
	 * Visits a EGModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.EGModality
	 */
	public abstract T visit(EGModality  v, P arg);
	
	/**
	 * Visits a EXModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	public abstract T visit(EXModality  v, P arg);
	
	/**
	 * Visits a AUModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	public abstract T visit(AUModality  v, P arg);
	
	/**
	 * Visits a AWModality.
	 * @param v the visited element.
	 * @param arg the passed generic argument.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.AWModality
	 */
	public abstract T visit(AWModality  v, P arg);
	
	/**
	 * Visits a EUModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.EUModality
	 */
	public abstract T visit(EUModality  v, P arg);
	
	/**
	 * Visits a EWModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.EWModality
	 */
	public abstract T visit(EWModality  v, P arg);

	
	/**
	 * Visits a AVModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	public abstract T visit(AVModality v, P arg);
	
	/**
	 * Visits a EVModality.
	 * @param v the visited element.
	 * @param arg the generic argument passed with this element.
	 * @return the generic result.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	public abstract T visit(EVModality v, P arg);
	
}