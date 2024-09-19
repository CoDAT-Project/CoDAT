package eshmun.expression.operators;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an Or Operator.
 * 
 * <p>The or operator can have many children.
 * e.g.: X or Y or Z or ....</p>
 * 
 * <p>
 * <i>Invariant</i>: it is guaranteed that no direct child of any OrOperator is an OrOperator.
 * </p>
 * 
 * <p>Notice that Or is commutative.</p>
 *   
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class OrOperator extends AbstractExpression {
	public static final char OR_CHAR = '\u2228';
	
	/**
	 * The Identity element for this operator. 
	 */
	public static final BooleanLiteral IDENTITY = new BooleanLiteral(false);
	
	/**
	 * All the children of the Or as a list.
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrOperator is an OrOperator.
	 * </p>
	 */
	private ArrayList<AbstractExpression> children;
	
	/**
	 * Create a new empty OrOperator.
	 */
	public OrOperator() {
		super(ExpressionType.OrOperator);
		this.children = new ArrayList<AbstractExpression>();
	}
	
	/**
	 * Create a new OrOperator with the given children.
	 * 
	 * <p>if any of the children is an OrOperator its children will be added
	 * For example (X or Y) or Z will yield (X or Y or Z). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrOperator is an OrOperator.
	 * </p>
	 * 
	 * @param children the children of the OrOperator.
	 */
	public OrOperator(AbstractExpression...children) {
		super(ExpressionType.OrOperator);
					
		this.children = new ArrayList<AbstractExpression>(children.length);
		
		for(AbstractExpression exp : children) {
			if(exp.getType() == ExpressionType.OrOperator) {
				for(AbstractExpression e : ((OrOperator) exp).children) {
					this.children.add(e);
				}
			} else {
				this.children.add(exp);
			}
		}
	}
	
	/**
	 * Adds a new clause to the or statement.
	 * e.g: (X or Y).or(Z) =&gt; X or Y or Z.
	 * 
	 * <p>if the child is an OrOperator its children will be added
	 * For example Z.or(X or Y) will yield (Z or X or Y). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any OrOperator is an OrOperator.
	 * </p>
	 * 
	 * @param exp the child to be added.
	 */
	public void or(AbstractExpression exp) {
		if(exp.getType() == ExpressionType.OrOperator) {
			for(AbstractExpression e : ((OrOperator) exp).children) {
				this.children.add(e);
			}
		} else {
			this.children.add(exp);
		}
	}
	
	/**
	 * Getter for the children of this or.
	 * @return the children.
	 */
	public AbstractExpression[] getChildren() {
		return children.toArray(new AbstractExpression[children.size()]);
	}
	
	/**
	 * If this is on the form X or (Y1 and Y2 ...), distributes the or on the ands.
	 * In this example the result would be (X or Y1) and (X or Y2) ...
	 * 
	 * <p>This method works for as many terms, each containing many sub terms. 
	 * and It doesn't require that one of the terms is atomic.</p>
	 * 
	 * <p>The resulting operator is guaranteed to be an equivalent. Further more
	 * if this operator contained two or more terms, then the equivalent is guaranteed
	 * to be an AndOperator.</p>
	 * 
	 * @return AbstractExpression that is equivalent to this OrOperator with or 
	 * distributed over ands if possible.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	public AbstractExpression distributeOr() {
		if(children.size() == 0)
			return IDENTITY;
		
		else if (children.size() == 1) 
			return children.get(0);
		
		
		AndOperator andOp = new AndOperator();
		OrOperator newVarOr = new OrOperator();
		
		for(AbstractExpression exp : children) {
			exp = exp.toCNF();
			if(exp.getType() == ExpressionType.OrOperator) {
				return toCNF();
			} else if(exp.getType() == ExpressionType.AndOperator) {
				BooleanVariable newVar = new BooleanVariable();
				NotOperator notNewVar = new NotOperator(newVar);
				for(AbstractExpression andChild : ((AndOperator) exp).getChildren()) {
					OrOperator tmp = new OrOperator(andChild, notNewVar);
					andOp.and(tmp);
				}
				
				newVarOr.or(newVar);
			} else {
				newVarOr.or(exp);
			}
		}
		
		andOp.and(newVarOr);
		return andOp;
	}
		
	/**
	 * {@inheritDoc}
	 * @return false.
	 */

	@Override
	public boolean isAtomic() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if all the children are CNF but not AndOperator, false otherwise.
	 */
	@Override
	public boolean isCNF() {
		for(AbstractExpression exp : children) {
			if(exp.getType() == ExpressionType.AndOperator || !exp.isCNF())
				return false;
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one child or more contains a CTL, false otherwise.
	 */
	@Override
	public boolean containsCTL() {	
		for(AbstractExpression exp : children) {
			if(exp.containsCTL())
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one or more children contains the variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		for(AbstractExpression exp : children) {
			if(exp.containsVariable(variable))
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one or more children contains the variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		for(AbstractExpression exp : children) {
			if(exp.containsVariable(variableName))
				return true;
		}
		
		return false;
	}

	/**
	 * Applies DeMorgan's law to get the negation, which is guaranteed to be an AndOperator.
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public AbstractExpression negate() {
		AndOperator andOp = new AndOperator();
		
		for(AbstractExpression exp : children) {
			andOp.and(exp.negate());
		}
		
		return andOp;
	}

	/**
	 * {@inheritDoc}
	 * @return a simplified equivalent expression.
	 */
	@Override
	public AbstractExpression simplify() {
		if(children.size() == 1) {
			return children.get(0).simplify();
		} else if(children.size() == 0) {
			return IDENTITY;
		}
		
		OrOperator orOp = new OrOperator();
		
		for(AbstractExpression exp : children) {
			orOp.or(exp.simplify());
		}
		
		return orOp;
	}

	/**
	 * Converts the current expression to CNF.
	 * 
	 * If this OrOperator is given as: (X1 &amp; Y1 &amp; Q1 ...) | (X1 &amp; Y2 &amp; Q2 ...) | ... | (Xn &amp; Yn &amp; Qn ...).
	 * 
	 * <p>The convertion is done by adding a new variable Zi, such that if Zi is true
	 * it means that Xi, Yi, Qi, ... are all true. </p>
	 * 
	 * <p>The result will be on the form:
	 * (!Z1 | X1) &amp; (!Z1 | Y1) (!Z! | Q1) &amp; (!Z2 | X2) &amp; (!Z2 | Y2) &amp; (!Z2 | Q2) ... &amp; (Z1 | Z2 | ... | Z3)
	 * </p>
	 * 
	 * @return an expression that is the CNF equivalent of this expression.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 * @see <a href="http://en.wikipedia.org/wiki/Conjunctive_normal_form#Conversion_into_CNF">Conversion into CNF</a>
	 */
	@Override
	public AbstractExpression toCNF() {
		if(children.size() == 0) 
			return IDENTITY;
		
		if(children.size() == 1) {
			return children.get(0);
		}
		
		if(isCNF())
			return clone();
		
		OrOperator orCNF = new OrOperator();
		for(AbstractExpression exp : children) {
			orCNF.or(exp.toCNF());
		}
		
		return orCNF.distributeOr();
	}
	
	/**
	 * {@inheritDoc}
	 * @return an equivalent NNF expression.
	 */
	@Override
	public AbstractExpression toNNF() {
		if(children.size() == 1) {
			return children.get(0).toNNF();
		} else if(children.size() == 0) { 
			return IDENTITY;
		}
		
		OrOperator orOp = new OrOperator();
		
		for(AbstractExpression exp : children) {
			orOp.or(exp.toNNF());
		}
		
		return orOp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		OrOperator orOp = (OrOperator) exp;
		return orOp.children.containsAll(children) && children.containsAll(orOp.children);
	}
	
	/**
	 * Clones this OrOperator.
	 * 
	 * <p>Please note that while the array of children is copied, the children themselves are not.
	 * Changes in one of the children could affect more than one OrOperator.</p>
	 * 
	 * @return a shallow copy of this OrOperator.
	 */
	@Override
	public OrOperator clone() {
		return new OrOperator(getChildren());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		for(AbstractExpression e : children) {
			e.accept(visitor);
			visitor.childrenSeparator();
		}
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		for(AbstractExpression e : children) {
			e.accept(visitor);
			visitor.childrenSeparator();
		}
		
		visitor.visit(this);
		visitor.separator();
	}
}