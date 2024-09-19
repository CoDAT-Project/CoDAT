package eshmun.skeletontextrepresentation.infinitespace.visitors;



import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_lbool;


import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;
import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.IfStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.Statement;
import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement.AssignmentType;

public class StrongestPostConditionVisitor implements CommandLogicVisitor {

	@Override
	public BoolExpr visit(IfStatement statement, BoolExpr p, Context ctx, int stateNumber) {

		if (statement.elseBody == null) {
			// sp(P, if b then c) ⇔ (b ⇒ sp(P, c)) ∧ (¬b ⇒ P)

			BoolExpr b = (BoolExpr) statement.condition;
			BoolExpr first = ctx.mkImplies(b, statement.thenBody.accept(this, p, ctx, stateNumber));
			BoolExpr second = ctx.mkImplies(ctx.mkNot(b), p);
			return ctx.mkAnd(first, second);

		} else {
			// sp(P,if b then c1 else c2)⇔(b⇒sp(P,c1))∧(¬b⇒sp(P,c2))

			BoolExpr b = (BoolExpr) statement.condition;
			BoolExpr first = ctx.mkImplies(b, statement.thenBody.accept(this, p, ctx, stateNumber));
			BoolExpr second = ctx.mkImplies(ctx.mkNot(b), statement.elseBody.accept(this, p, ctx, stateNumber));
			return ctx.mkAnd(first, second);
		}

	}

	@Override
	public BoolExpr visit(BlockStatement statement, BoolExpr p, Context ctx, int stateNumber) {

		// sp(P, c1; c2) ⇔ sp(sp(P, c1), c2)

		if (statement.children.size() == 0) {
			return p;
		}

		BoolExpr sp = null;
		for (Statement s : statement.children) {
			if (sp == null)
				sp = s.accept(this, p, ctx, stateNumber);
			else {
				sp = s.accept(this, sp, ctx, stateNumber);
			}
		}

		return sp;
	}

	@Override
	public BoolExpr visit(AssignmentStatement statement, BoolExpr p, Context ctx, int stateNumber) {
		// sp(P,x :=e) ⇔ ∃x0 :P[x0/x]∧x =e[x0/x]

//		if (statement.assignmentType == AssignmentType.BoolAssignment) {
//
//			// Create x0
//			BoolExpr x = ctx.mkBoolConst(statement.left.toString());
//
//			Z3_lbool value = statement.right.getBoolValue();
//
//			 
//
//			if (value == Z3_lbool.Z3_L_TRUE) {
//
//				if (!p.toString().contains(statement.left.toString())) {
// 
//					return (BoolExpr) ctx.mkAnd(p, x);
//
//				}  
//
//			} else {
//
//				if (p.toString().contains(statement.left.toString())) {
//
//					// Substitute
//					Expr x0 = ctx.mkBool(true);
//					return (BoolExpr) p.substitute(x, x0);
//
//				}  
//
//			}
//
//			return   p;
//
//		} 
		
		if (statement.assignmentType == AssignmentType.BoolAssignment) {

			// Create x0
			BoolExpr x = ctx.mkBoolConst(statement.left.toString());

			Z3_lbool value = statement.right.getBoolValue();

			BoolExpr px0_x = null;
			

			BoolExpr left = ctx.mkBoolConst(statement.left.toString());
			
			//check if p has left true
			Solver solver = ctx.mkSolver();
			solver.add(p);
			solver.add(left);
			
			Status result = solver.check();
			if (result == Status.SATISFIABLE) {
				 //left is true in P
				
				
				if (value == Z3_lbool.Z3_L_TRUE) {
					//Add to formula
					return ctx.mkAnd(p, left);
					
				}else {
					//If it is in formula, replace occurence with nots
					if(p.toString().contains(left.toString())) {
						 				
						return (BoolExpr) p.substitute(left, ctx.mkNot(left));
					}else {
						  					
					 return ctx.mkAnd(p, ctx.mkNot(left));
					}
				
				}
				
			}else {
				//left is false in P		
				if (value == Z3_lbool.Z3_L_TRUE) { //setting it to true
					
					return (BoolExpr) p.substitute(ctx.mkNot(left), left);
					
				}else { //keep it false
					return p;
				}
				
			}
			

			} 
		else {


			// Create x0
			Expr x = ctx.mkIntConst(statement.left.toString());
			
			//String newLabel = "var_" + stateNumber;

			String newLabel = statement.left.toString()+ "o"  ;
			//			while(p.toString().contains(newLabel)) {
//				newLabel = newLabel+"_";
//			}
			
			Expr x0 = ctx.mkIntConst(newLabel); // left guaranteed to be an identifier

			// Create P[x0/x]
			BoolExpr px0_x = (BoolExpr) p.substitute(x, x0);			
			
			
			// Expr xNew = ctx.mkIntConst(statement.left.toString()+ "_" + InfiniteStateActionFactory.visitorCounter++);
			
			
			// Create x =e[x0/x]
			IntExpr ex0_x = (IntExpr) statement.right.substitute(x, x0);
			BoolExpr newExpression = ctx.mkEq(x , ex0_x);
			
			
			BoolExpr existsBody = ctx.mkAnd(px0_x, newExpression);

			
			//return (BoolExpr) existsBody.simplify();
			
			 Expr xExists = ctx.mkExists(new Expr[] { x0 }, existsBody.simplify(), 1, null, null, null, null);

	    return (BoolExpr) xExists.simplify();
			 
			 
			 //For testing just return
			 
			//BoolExpr equivalenceTest = ctx.mkEq(ctx.mkIntConst(statement.left.toString()), statement.right);
			//BoolExpr existsBodyTest = ctx.mkAnd(p, equivalenceTest);
				
				// return existsBodyTest;
			 
		}
	}

}
