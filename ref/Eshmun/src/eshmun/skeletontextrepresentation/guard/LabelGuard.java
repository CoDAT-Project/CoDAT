package eshmun.skeletontextrepresentation.guard;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 * This is the basic Guard. It deals with atomic propositions in the form of Labels e.g. N1 or variable checks (x=1)
 * @author chukrisoueidi
 *
 */
public class LabelGuard extends Guard {

	public String label;
	
	 

	public LabelGuard(String l) {
		label = l;
	}

	/**
	 * Checks if a label is in a state
	 * @param s
	 * @return
	 */
	public boolean isInState(State s) {

		return s.stateContainsLabel(label.replaceAll(" ",""));
	}
	

	/**
	 * Calls the visitor operation on LabelGuards
	 */
	@Override
	public boolean accept(GuardVisitor visitor, State state) {
		return visitor.visit(this, state);

	}

	@Override
	public BoolExpr accept(Z3GuardVisitor visitor, Context ctx) {
		return visitor.visit(this,ctx);

	}

}
