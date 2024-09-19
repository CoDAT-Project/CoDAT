package eshmun.skeletontextrepresentation.guard;

import eshmun.skeletontextrepresentation.State;

import eshmun.skeletontextrepresentation.guard.Guard.Z3GuardVisitor;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.z3.*;

/**
 * This visitor operates on a guard to check if a state is enabled or not by it.
 * 
 * @author chukrisoueidi
 *
 */
public class Z3EnableStateVisitor implements Z3GuardVisitor {

	@Override
	public BoolExpr visit(OrGuard or, Context ctx) {

		List<BoolExpr> guardExp = new ArrayList<>();

		for (Guard gd : or.getChildren()) {
			guardExp.add(gd.accept(this, ctx));
		}

		return ctx.mkOr(guardExp.toArray(new BoolExpr[guardExp.size()]));

	}

	@Override
	public BoolExpr visit(AndGuard and, Context ctx) {

		List<BoolExpr> guardExp = new ArrayList<>();

		for (Guard gd : and.getChildren()) {
			guardExp.add(gd.accept(this, ctx));
		}

		return ctx.mkAnd(guardExp.toArray(new BoolExpr[guardExp.size()]));

	}

	@Override
	public BoolExpr visit(NotGuard not, Context ctx) {

		return ctx.mkNot(not.child.accept(this, ctx));
	}

	@Override
	public BoolExpr visit(LabelGuard label, Context ctx) {
		return ctx.mkBoolConst(label.label.replace("=", "eq"));
	}

	@Override
	public BoolExpr visit(LiteralGuard literalGuard, Context ctx) {
		// TODO Auto-generated method stub
		return ctx.mkBool(literalGuard.getValuation() ? true : false);
	}

}
