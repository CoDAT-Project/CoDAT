package eshmun.skeletontextrepresentation.infinitespace;

 

import java.util.HashSet;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
 

import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;
import eshmun.skeletontextrepresentation.infinitespace.commands.Statement;
import eshmun.skeletontextrepresentation.infinitespace.parser.InfiniteSpaceParser;
import eshmun.skeletontextrepresentation.infinitespace.visitors.WeakestPreConditionVisitor;

public class TransitionTripleChecker {

	public TransitionValidationResult checkTriple(String pred_S, String pred_T, String pre_t, String statement,
			String q) {

		TransitionValidationResult returnValue = null;;

		try {

			returnValue = new TransitionValidationResult();

			BoolExpr P_s = null;
			BoolExpr P_t = null;
			BoolExpr Pre_t = null;
			BoolExpr Q = null;

			Solver solver = null;
			Status result = null;

			Context ctx = InfiniteStateActionFactory.context;
			if (pred_S == null || pred_S.isEmpty()) {

				P_s = ctx.mkBool(true);
				
			} else {
				String parseme =  cleanFromBools(InfiniteStateActionFactory.getZ3SMTDeclarations() + "(assert " + pred_S.trim() + ")");
				System.out.println(parseme);
				P_s = ctx.mkAnd(ctx.parseSMTLIB2String(parseme, null, null, null, null));
				
				P_s = solveAndSimplify(ctx, P_s);
				 
			}

			if (pred_T == null || pred_T.isEmpty()) {

				P_t = ctx.mkBool(true);
			} else {
				String parseme =  cleanFromBools(InfiniteStateActionFactory.getZ3SMTDeclarations() + "(assert " + pred_T.trim() + ")");
				System.out.println(parseme);
				P_t = ctx.mkAnd(ctx.parseSMTLIB2String(parseme, null, null, null, null));
				P_t = solveAndSimplify(ctx, P_t);
			}

			if (pre_t == null || pre_t.isEmpty()) {

				Pre_t = ctx.mkBool(true);
			} else {
				String parseme = cleanFromBools(InfiniteStateActionFactory.getZ3SMTDeclarations() + "(assert " + pre_t.trim() + ")");
				System.out.println(parseme);
				
				Pre_t = ctx.mkAnd(ctx.parseSMTLIB2String(parseme, null, null, null, null));
				Pre_t = solveAndSimplify(ctx, Pre_t);
			}

			if (q == null || q.isEmpty()) {

				Q = ctx.mkBool(true);
			} else {
				String parseme =  cleanFromBools(InfiniteStateActionFactory.getZ3SMTDeclarations() + "(assert " + q.trim() + ")");
				System.out.println(parseme);
				Q = ctx.mkAnd(ctx.parseSMTLIB2String(parseme, null, null, null, null));
				Q = solveAndSimplify(ctx, Q);
			}

			Statement command = InfiniteSpaceParser.parseStatement("{" + statement + "}", ctx);

			/*
			 * Warn if Pred_S => Pre_t is UNSAT
			 */
			
			solver = ctx.mkSolver();
			solver.add(ctx.mkImplies(P_s, Pre_t));
			result = solver.check();
			if (result == Status.UNSATISFIABLE) {

				// UNSAT
				returnValue.hasWarning = true;
				returnValue.beginningError = true;

			} else {
				
				//SAT
				returnValue.hasWarning = false;
				returnValue.beginningError = false;
			}
			
			
			/*
			 * Validate Triplet : {Pred_S & Pre_t} S Q
			 */

			WeakestPreConditionVisitor visitor = new WeakestPreConditionVisitor();
			BoolExpr weakestPrecondition = command.accept(visitor, ctx.mkAnd(Q, P_t), ctx, 1);

			solver = ctx.mkSolver();
			solver.add(ctx.mkNot(ctx.mkImplies(ctx.mkAnd(P_s, Pre_t), weakestPrecondition)));
		 
			
			
			result = solver.check();
			if (result == Status.UNSATISFIABLE) {

				// VALID
				returnValue.isValid = true;

			} else {
				returnValue.isValid = false;
			}

			/*
			 * Validate Q => Pred_t
			 */
			//TODO
			returnValue.endError = false;

		 
			return returnValue;

		} catch (Exception e) {

			returnValue.isValid = false;
			returnValue.errorMessage = e.getMessage();

			return returnValue;
		}

	}

	private String cleanFromBools(String s) {
		  return s;
		 //return StringHelpers.cleanFromBools(s, InfiniteStateActionFactory.allAPs);
		 
	}
	
	
	private static BoolExpr solveAndSimplify(Context ctx, BoolExpr Q) {
		Solver solver = ctx.mkSolver();
		solver.add(Q);
		Status solverresult = solver.check();
		BoolExpr finalValue = null;
		if (solverresult == Status.SATISFIABLE) {

			Model model = solver.getModel();
			
			HashSet<String> allInts = InfiniteStateActionFactory.allIntVariables;

			for (String v : allInts) {
				if (finalValue == null) {
					finalValue = ctx.mkEq(ctx.mkIntConst(v), (IntExpr) model.eval(ctx.mkIntConst(v), false));

				} else {
					finalValue = ctx.mkAnd(finalValue,
							ctx.mkEq(ctx.mkIntConst(v), (IntExpr) model.eval(ctx.mkIntConst(v), false)));
				}
			}

			return (BoolExpr) finalValue.simplify();

		} else {
			return Q;
		}

	}

	public class TransitionValidationResult {

		public boolean beginningError;
		public boolean endError;

		public boolean isValid;
		public boolean hasWarning;

		public String errorMessage = null;

	}

}
