package eshmun.expression.ctl;

import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * Class implementing EX(child) in a CTL parsed tree
 * @author Emile
 *
 */
public class EXOperator extends SingleCTLOperator {
	/**
	 * Creates a new parsed tree for the CTL formula EX(child) 
	 * @param child: the child of the operator
	 */
	public EXOperator(PredicateFormula child) {
		super(child);
		this.toStringValue = "E" + getPathIndexesString() + "[X(" + child.toString() + ")]";
	}
	
	@Override
	public void setChild(PredicateFormula pchild)
	{
		child = pchild;
		this.toStringValue = "E" + getPathIndexesString() + "[X(" + child.toString() + ")]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula child = this.getChild().replaceParameters(indexReplacementContext);
		EXOperator euOperator = new EXOperator(child);
		return euOperator;
	}
	

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	@Override
	public PredicateFormula ConvertToCTL(){
		return new EXOperator(child.ConvertToCTL());
	}
}