package eshmun.expression.operators;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents a Not Operator.
 * 
 * <p>A not operator is a unary operator, therefore it contains only one AbstractExpression (child).</p>
 * 
 * <p>A Not operator is by definition is CNF only if its child is a variable or literal.</p>
 * 
 * <p>It is also not CTL, nor literal. when simplified the not operator goes to inside the child.</p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class NotOperator extends AbstractExpression {
	public static final char NOT_CHAR = '\u00AC';

	/**
	 * The child expression this operator negates.
	 */
	private AbstractExpression child;

	/**
	 * Create a new NotOperator the negates The given child.
	 * @param child The child expression this operator negates.
	 */
	public NotOperator(AbstractExpression child) {
		super(ExpressionType.NotOperator);
		this.child = child;
	}
	
	/**
	 * Getter for the child of this not operator.
	 * @return child.
	 */
	public AbstractExpression getChild() {
		return child;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true if child is atomic, false otherwise.
	 */

	@Override
	public boolean isAtomic() {
		return child.isAtomic();
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
	 * @return true if its child is a BooleanVariable or Literal, false Otherwise.
	 */
	@Override
	public boolean isCNF() {
		return child.isAtomic() && child.getType() != ExpressionType.NotOperator;
	}

	/**
	 * {@inheritDoc}
	 * @return true if child is a CTL modality, false otherwise.
	 */
	@Override
	public boolean isCTL() {
		if(child.isCTL()) {
			return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false
	 */
	@Override
	public boolean isLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if child contains CTL, false otherwise.
	 */
	@Override
	public boolean containsCTL() {
		return child.containsCTL();
	}

	/**
	 * {@inheritDoc}
	 * @return true if child contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return child.containsVariable(variable);
	}

	/**
	 * {@inheritDoc}
	 * @return true if child contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		return child.containsVariable(variableName);
	}

	/**
	 * {@inheritDoc}
	 * @return a simplified version of the child.
	 */
	@Override
	public AbstractExpression negate() {
		return child.simplify();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression abs = child;
		if(abs.getType() == ExpressionType.NotOperator) {
			return ((NotOperator) abs).child;
		}
		
		return child.negate();
	}

	/**
	 * if this is in CNF 
	 * 		return this
	 * if the child is a not operator (double negation)
	 * 		return CNF(child of child)
	 * if the child is an And or an Or operator
	 * 		tmp := send not to inside (DeMorgan)
	 * 		return CNF(tmp)
	 * else (CTL or implication or equivalence)
	 * 		simple_child := simplify child
	 * 		CNF(simple_child)
	 * 
	 * {@inheritDoc}
	 * @return an expression that is the CNF equivalent of this expression, guaranteed to be an AndOperator.
	 */
	@Override
	public AbstractExpression toCNF() {
		AbstractExpression cnfExpression;
		
		if(child.getType() == ExpressionType.NotOperator) {
			cnfExpression = ((NotOperator) child).child.toCNF();
		} else if(child.isLiteral()) {
			cnfExpression = child.negate();
		} else if(child.getType() == ExpressionType.AndOperator || child.getType() == ExpressionType.OrOperator) {
			cnfExpression = simplify().toCNF();
		} else if(child.isCTL()) {
			return new NotOperator(child.toCNF());
		} else if(isCNF()) {
			cnfExpression = this;
		} else {
			NotOperator notOp = new NotOperator(child.simplify());
			cnfExpression = notOp.toCNF();
		}
		
		return cnfExpression;
	}
	
	/**
	 * {@inheritDoc}
	 * @return an equivalent NNF expression, by sending the negation inside.
	 */
	@Override
	public AbstractExpression toNNF() {
		if(child.getType() == ExpressionType.NotOperator) {
			return ((NotOperator) child).child.toNNF();
		}
	
		if(child.isAtomic()) {
			return child.negate();
		}
		
		if(child.getType() == ExpressionType.AndOperator) {
			if(((AndOperator) child).getChildren().length == 1) {
				return new NotOperator(((AndOperator) child).getChildren()[0]).toNNF();
			} else if(((AndOperator) child).getChildren().length == 0) {
				return AndOperator.IDENTITY.negate();
			}
			
			OrOperator orOp = new OrOperator();
			
			for(AbstractExpression e : ((AndOperator) child).getChildren()) {
				orOp.or(new NotOperator(e).toNNF());
			}
			
			return orOp;
		}
		
		if(child.getType() == ExpressionType.OrOperator) {
			if(((OrOperator) child).getChildren().length == 1) {
				return new NotOperator(((OrOperator) child).getChildren()[0]).toNNF();
			} else if(((OrOperator) child).getChildren().length == 0) {
				return OrOperator.IDENTITY.negate();
			}
			
			AndOperator andOp = new AndOperator();
			
			for(AbstractExpression e : ((OrOperator) child).getChildren()) {
				andOp.and(new NotOperator(e).toNNF());
			}
			
			return andOp;
		}
		
		if(child.getType() == ExpressionType.EquivalenceOperator) {
			AbstractExpression[] children = ((EquivalenceOperator) child).getChildren();
			
			if(children.length == 1) {
				return new NotOperator(children[0]).toNNF();
			} else if(children.length == 0) {
				return EquivalenceOperator.IDENTITY.negate();
			}
			
			OrOperator orOp = new OrOperator(); //negated simplification
			for(int i = 0; i < children.length - 1; i++) {
				AbstractExpression first = children[i];
				AbstractExpression second = children[i+1];
				
				orOp.or(new AndOperator(first, new NotOperator(second)));
				orOp.or(new AndOperator(new NotOperator(first), second));
			}
			
			return orOp.toNNF();
		}
		
		
		if(child.getType() == ExpressionType.ImplicationOperator) {
			ImplicationOperator impOp = (ImplicationOperator) child;
			AndOperator andOp = new AndOperator();
			
			andOp.and(impOp.getLeftChild()); //negated simplification
			andOp.and(new NotOperator(impOp.getRightChild()));
			
			return andOp.toNNF();
		}
		
		if(child.getType() == ExpressionType.AVModality) {
			NotOperator notOp = ((AVModality) child).reduceToEU();
			return notOp.child.toNNF();
		}
		
		if(child.getType() == ExpressionType.EVModality) {
			NotOperator notOp = ((EVModality) child).reduceToAU();
			return notOp.child.toNNF();
		}
		
		if(child.getType() == ExpressionType.AXModality) {
			NotOperator notOp = new NotOperator(((AXModality) child).getChild());
			return new EXModality(notOp).toNNF();
		}
		
		if(child.getType() == ExpressionType.EXModality) {
			NotOperator notOp = new NotOperator(((EXModality) child).getChild());
			return new AXModality(notOp).toNNF();
		}
		
		return simplify().toNNF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		NotOperator notOp = (NotOperator) exp;
		return child.equals(notOp.child);
	}
	
	/**
	 * Clones this NotOperator.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one NotOperator.</p>
	 * 
	 * @return a shallow copy of this OrOperator.
	 */
	@Override
	public NotOperator clone() {
		return new NotOperator(this.child);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		child.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		child.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.visit(this);
		visitor.separator();
	}
}
