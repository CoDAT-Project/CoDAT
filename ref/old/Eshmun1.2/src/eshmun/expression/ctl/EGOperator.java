package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;


/**
 * class describing a new CTL operator of the form EG[child]
 * 
 * @author Emile Chartouni
 *
 */
public class EGOperator extends SingleCTLOperator {

	
	/**
	 * Creates a new parsed tree for the CTL formula EG(child) 
	 * @param child: the child of the operator
	 */
	public EGOperator(PredicateFormula child) {
		super(child);
		this.toStringValue = "E" + "[G" + getPathIndexesString() + "(" + child.toString() + ")]";
	}

	@Override
	public void setChild(PredicateFormula pchild)
	{
		child = pchild;
		this.toStringValue = "E" + "[G" + getPathIndexesString() + "(" + child.toString() + ")]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula child = this.getChild().replaceParameters(indexReplacementContext);
		EGOperator euOperator = new EGOperator(child);
		return euOperator;
	}

	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		NotOperator child = new NotOperator(getChild());
		AFOperator afOperator = new AFOperator(child);
		NotOperator rootOperator = new NotOperator(afOperator);
		subformulea.addAll(rootOperator.getSubFormulea(expressionVisitor));
		subformulea.add(this);
		return subformulea;	
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		BooleanConstant bc = new BooleanConstant(false);
		BooleanPredicate bp = new BooleanPredicate(bc);
		EVOperator ev = new EVOperator(bp, getChild());		
		return ev.getSubFormulea();	
	}
	@Override
	public PredicateFormula ConvertToCTL()
	{
		BooleanConstant bc = new BooleanConstant(false);
		BooleanPredicate bp = new BooleanPredicate(bc);
		EVOperator ev = new EVOperator(bp, getChild());		
		return ev.ConvertToCTL();	
	}


	public PredicateFormula getChild() {
		return child;
	}	
	
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state)  throws Exception {
		return modelChecker.visit(this, structure, state);
	}
}