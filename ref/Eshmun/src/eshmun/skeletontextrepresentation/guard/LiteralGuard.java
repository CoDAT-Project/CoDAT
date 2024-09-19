package eshmun.skeletontextrepresentation.guard;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 * This is a basic literal Guard. It is either false or true.
 * @author chukrisoueidi
 *
 */

public class LiteralGuard extends Guard {

	private boolean literal;

	public LiteralGuard(String l) {

		if (l.equals("true") || l.equals("tt") || l.equals("True"))
			literal = true;
		else
			literal = false;
	}

	/**
	 * Getter for the literal
	 * @return
	 */
	public boolean getValuation() {
		return literal;
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
