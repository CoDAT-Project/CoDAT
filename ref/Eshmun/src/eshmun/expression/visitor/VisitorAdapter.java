package eshmun.expression.visitor;

import eshmun.expression.AbstractExpression;
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
 * Visitor Adapter, by default all types would be redirected to visit(AbstractExpression).
 * 
 * <p>You should override visit(AbstractExpression) to have a general purpose code that works on all AbstractExpression.
 * You can also override specific visit methods for specific types if you don't want them to change default behavior.</p>
 * 
 * @see eshmun.expression.visitor.Visitor
 * @see eshmun.expression.visitor.Visitable
 * @see eshmun.expression.AbstractExpression
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public abstract class VisitorAdapter implements Visitor {
	/**
	 * General Purpose Visit method, all other methods are redirected to it by default.
	 * @param v AbstractExpression being visited.
	 */
	protected abstract void visit(AbstractExpression v);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanVariable v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanLiteral v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(NotOperator v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AndOperator v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(OrOperator v) {
		visit((AbstractExpression) v);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(ImplicationOperator v) {
		visit((AbstractExpression) v);	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EquivalenceOperator v) {
		visit((AbstractExpression) v);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AFModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AGModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AXModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EFModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EGModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EXModality v) {
		visit((AbstractExpression) v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AUModality v) {
		visit((AbstractExpression) v);
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AWModality v) {
		visit((AbstractExpression) v);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AVModality v) {
		visit((AbstractExpression) v);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EUModality v) {
		visit((AbstractExpression) v);
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EWModality v) {
		visit((AbstractExpression) v);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EVModality v) {
		visit((AbstractExpression) v);		
	}
}
