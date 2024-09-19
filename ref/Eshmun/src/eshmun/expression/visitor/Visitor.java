package eshmun.expression.visitor;

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
 * Standard Visitor Pattern, Visitors should implement this interface fully. 
 * 
 * <p>If you want to implement a visitor for AbstractExpression and certain sub-types use VisitorAdapter. </p>
 * 
 * @see eshmun.expression.visitor.Visitable
 * @see eshmun.expression.visitor.VisitorAdapter
 * @see eshmun.expression.AbstractExpression
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public interface Visitor {
	/**
	 * Visits a BooleanVariable.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public void visit(BooleanVariable v);
	
	/**
	 * Visits a BooleanLiteral.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	public void visit(BooleanLiteral v);
	
	/**
	 * Visits a NotOperator.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	public void visit(NotOperator v);
	
	/**
	 * Visits a AndOperator.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	public void visit(AndOperator v);
	
	/**
	 * Visits a OrOperator.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	public void visit(OrOperator v);
	
	/**
	 * Visits a ImplicationOperator.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.operators.ImplicationOperator
	 */
	public void visit(ImplicationOperator v);
	
	/**
	 * Visits a EquivalenceOperator.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	public void visit(EquivalenceOperator v);
	
	/**
	 * Visits a AFModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.AFModality
	 */
	public void visit(AFModality v);
	
	/**
	 * Visits a AGModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.AGModality
	 */
	public void visit(AGModality v);
	
	/**
	 * Visits a AXModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.AXModality
	 */
	public void visit(AXModality v);
	
	/**
	 * Visits a EFModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.EFModality
	 */
	public void visit(EFModality v);
	
	/**
	 * Visits a EGModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.EGModality
	 */
	public void visit(EGModality v);
	
	/**
	 * Visits a EXModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	public void visit(EXModality v);
	
	/**
	 * Visits a AUModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	public void visit(AUModality v);
	
	/**
	 * Visits a AWModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.AWModality
	 */
	public void visit(AWModality v);
	
	/**
	 * Visits a AVModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	public void visit(AVModality v);
	
	/**
	 * Visits a EUModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.EUModality
	 */
	public void visit(EUModality v);
	
	/**
	 * Visits a EWModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.EWModality
	 */
	public void visit(EWModality v);
	
	/**
	 * Visits a EVModality.
	 * @param v the visited element.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	public void visit(EVModality v);
	
	/**
	 * Indicates the end of a child, called after visiting a child.
	 */
	public void childrenSeparator();
	
	/**
	 * Indicates the end of an element.
	 */
	public void separator();
}