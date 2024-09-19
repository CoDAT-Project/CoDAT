package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;

/**
 * Class that describes a CTL parsed tree for some CTL formula. 
 * a CTL formula is described in the following BNF:
 * 
 * @see PredicateFormula
 * 
 * @author Emile Chartouni
 *
 */
public abstract class CTLParseTree extends PredicateFormula{

	/** the indexes of the pathes to be considered in model checking */
	protected List<String> pathIndexes;

	public CTLParseTree() {
		super();
		this.pathIndexes = new ArrayList<String>();
	}

	
	/**
	 * return false since a CTL formula is never in CNF format
	 */
	@Override
	public boolean isCNF() {
		return false;
	}

	public List<String> getPathIndexes() {
		return pathIndexes;
	}

	public void setPathIndexes(List<String> pathIndexes) {
		this.pathIndexes = pathIndexes;
	}
	
	public String getPathIndexesString() {
		String pathIndexesStr = "";
		for (String pathIndex : pathIndexes) {
			pathIndexesStr += "[" + pathIndex + "]"; 
		}
		return pathIndexesStr;
	}
}
