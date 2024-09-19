package eshmun.skeletontextrepresentation.infinitespace.visitors;

import java.util.Collections;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;

import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.IfStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.Statement;

public class WeakestPreConditionVisitor implements CommandLogicVisitor {

	@Override
	public BoolExpr visit(IfStatement statement, BoolExpr q, Context ctx, int stateNumber) {
	
		if (statement.elseBody == null) {
		 

			BoolExpr b = (BoolExpr) statement.condition;
			BoolExpr first = ctx.mkImplies(b, statement.thenBody.accept(this, q, ctx, stateNumber));
			BoolExpr second = ctx.mkImplies(ctx.mkNot(b), q);
			return ctx.mkAnd(first, second);

		} else {
		 

			BoolExpr b = (BoolExpr) statement.condition;
			BoolExpr first = ctx.mkImplies(b, statement.thenBody.accept(this, q, ctx, stateNumber));
			BoolExpr second = ctx.mkImplies(ctx.mkNot(b), statement.elseBody.accept(this, q, ctx, stateNumber));
			return ctx.mkAnd(first, second);
		}
	}

	@Override
	public BoolExpr visit(BlockStatement statement, BoolExpr q, Context ctx, int stateNumber) {
		
		if (statement.children.size() == 0) {
			return q;
		}

		
		BoolExpr sp = null;
		
		Collections.reverse(statement.children);
		
		for (Statement s : statement.children) {
			if (sp == null)
				sp = s.accept(this, q, ctx, stateNumber);
			else {
				sp = s.accept(this, sp, ctx, stateNumber);
			}
		}
		
		Collections.reverse(statement.children);

		return sp;
	}

	@Override
	public BoolExpr visit(AssignmentStatement statement, BoolExpr q, Context ctx, int stateNumber) {
		 
		
		IntExpr left = (IntExpr) statement.left;
		IntExpr right = (IntExpr) statement.right;
		
		
		BoolExpr new_q = (BoolExpr) q.substitute(left, right);	
		
		
		
		return (BoolExpr) new_q.simplify();
	}

}
