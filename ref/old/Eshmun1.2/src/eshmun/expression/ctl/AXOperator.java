package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * Class implementing AX(child) in a CTL parsed tree
 * @author Emile
 *
 */
public class AXOperator extends SingleCTLOperator {
	
	/**
	 * Creates a new parsed tree for the CTL formula AX(child) 
	 * @param child: the child of the operator
	 */
	public AXOperator(PredicateFormula child) {
		super(child);
		this.toStringValue = "A" + getPathIndexesString() + "[X(" + child.toString() + ")]";
	}

	@Override
	public void setChild(PredicateFormula pchild)
	{
		child = pchild;
		this.toStringValue = "A" + getPathIndexesString() + "[X(" + child.toString() + ")]";
	}
	

	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula child = this.getChild().replaceParameters(indexReplacementContext);
		AXOperator euOperator = new AXOperator(child);
		return euOperator;
	}
	

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception  {
		return modelChecker.visit(this, structure, state);
	}

	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVsitor) {
		return expressionVsitor.fillSubFormulae(this);
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(this.getChild().getSubFormulea());
		subformulea.add(this);
		return subformulea;
		
	}
	@Override
	public PredicateFormula ConvertToCTL(){
		return new AXOperator(child.ConvertToCTL());
	}
}