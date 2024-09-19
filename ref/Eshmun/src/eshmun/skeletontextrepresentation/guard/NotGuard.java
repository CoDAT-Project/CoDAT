package eshmun.skeletontextrepresentation.guard;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 *
 * Represents a Not Operator on Guards. Semantically, this means that this negates the evaluation of the child Guard.
 * 
 * @author chukris
 *
 */
  
public class NotGuard extends Guard {

	/**
	 * Can have only one child
	 */
	public Guard child;

	/**
	 * Constructor with setting of child
	 * @param g
	 */
	public NotGuard(Guard g) {
		child = g;
	}

	/**
	 * Default constructor
	 */
	public NotGuard() {

	}
	
	/**
	 * Calls the visitor AndGuard implementation
	 */
	@Override
	public boolean accept(GuardVisitor visitor, State state) {
		return visitor.visit(this, state);
		 

	}

	@Override
	public BoolExpr accept(Z3GuardVisitor visitor,Context ctx) {
		return visitor.visit(this,  ctx);
	}

}
