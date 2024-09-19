package eshmun.skeletontextrepresentation.infinitespace.commands;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import eshmun.skeletontextrepresentation.infinitespace.visitors.CommandLogicVisitor;

public class IfStatement extends Statement {

	public Expr condition;
	
	public String conditionString;

	public Statement thenBody;
	public Statement elseBody;

	public String toString() {

		String ret = "if ( " + conditionString + " ) " + System.lineSeparator();
		ret += "{" + thenBody.toString() + "}";
		if (elseBody != null) {
			ret +=    "else {" + System.lineSeparator();
			ret += elseBody.toString();
			ret += "} " + System.lineSeparator();
		}
		return ret;
	}

	@Override
	public BoolExpr accept(CommandLogicVisitor v, BoolExpr p, Context ctx, int stateNumber) {

		return v.visit(this, p, ctx, stateNumber);

	}

}
