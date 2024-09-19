package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

public class EFOperator extends SingleCTLOperator {

	/**
	 * Creates a new parsed tree for the CTL formula EF(child) 
	 * @param child: the child of the operator
	 */
	public EFOperator(PredicateFormula child) {
		super(child);
		this.toStringValue = "E" + getPathIndexesString() + "[F(" + child.toString() + ")]";
	}

	@Override
	public void setChild(PredicateFormula pchild)
	{
		child = pchild;
		this.toStringValue = "E" + getPathIndexesString() + "[F(" + child.toString() + ")]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula child = this.getChild().replaceParameters(indexReplacementContext);
		EFOperator euOperator = new EFOperator(child);
		return euOperator;
	}
	
	

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		BooleanPredicate leftChild = new BooleanPredicate(new BooleanConstant(true));
		EUOperator euOperator = new EUOperator(leftChild, getChild());
		euOperator.setPathIndexes(this.getPathIndexes());
		subformulea.addAll(euOperator.getSubFormulea(expressionVisitor));
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		BooleanPredicate leftChild = new BooleanPredicate(new BooleanConstant(true));
		EUOperator euOperator = new EUOperator(leftChild, getChild());
		return euOperator.getSubFormulea();
	}
	
	@Override
	public PredicateFormula ConvertToCTL(){
		BooleanPredicate leftChild = new BooleanPredicate(new BooleanConstant(true));
		EUOperator euOperator = new EUOperator(leftChild, getChild());
		return euOperator.ConvertToCTL();
	}
}