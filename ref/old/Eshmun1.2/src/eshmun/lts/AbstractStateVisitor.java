package eshmun.lts;

import eshmun.lts.kripke.KripkeState;

/**
 * this class describes an abstract state visitor that visits a class
 * and does some modifications / computations as described in the 
 * implementation of the visit method
 * @author Emile
 *
 */
public abstract class AbstractStateVisitor {
	
	/**
	 * users must implement this method for specifying the code done 
	 * on visiting the state.
	 * @param state: the state to be visited
	 * @return
	 */
	public abstract Object visit(KripkeState state) ;
}
