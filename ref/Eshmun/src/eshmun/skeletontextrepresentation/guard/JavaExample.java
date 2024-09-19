package eshmun.skeletontextrepresentation.guard;

/*++
Copyright (c) 2012 Microsoft Corporation

Module Name:

   Program.java

Abstract:

   Z3 Java API: Example program

Author:

   Christoph Wintersteiger (cwinter) 2012-11-27

Notes:
   
--*/

import java.util.*;

import org.antlr.v4.parse.GrammarTreeVisitor.astOperand_return;

import com.microsoft.z3.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class JavaExample {
	@SuppressWarnings("serial")
	class TestFailedException extends Exception {
		public TestFailedException() {
			super("Check FAILED");
		}
	};

	 
	 

	public void simpleExample() {
		System.out.println("SimpleExample");
		Log.append("SimpleExample");

		{
			Context ctx = new Context();
			/* do something with the context */

			/* be kind to dispose manually and not wait for the GC. */
			ctx.close();
		}
	}

	Model check(Context ctx, BoolExpr f, Status sat) throws TestFailedException {
		Solver s = ctx.mkSolver();
		s.add(f);
		if (s.check() != sat)
			throw new TestFailedException();
		if (sat == Status.SATISFIABLE)
			return s.getModel();
		else
			return null;
	}

 void prove(Context ctx, BoolExpr f, boolean useMBQI) throws TestFailedException {
		BoolExpr[] assumptions = new BoolExpr[0];
		prove(ctx, f, useMBQI, assumptions);
	}

	void prove(Context ctx, BoolExpr f, boolean useMBQI, BoolExpr... assumptions) throws TestFailedException {
		System.out.println("Proving: " + f);
		Solver s = ctx.mkSolver();
		Params p = ctx.mkParams();
		p.add("mbqi", useMBQI);
		s.setParameters(p);
		for (BoolExpr a : assumptions)
			s.add(a);
		s.add(ctx.mkNot(f));
		Status q = s.check();

		switch (q) {
		case UNKNOWN:
			System.out.println("Unknown because: " + s.getReasonUnknown());
			break;
		case SATISFIABLE:
			throw new TestFailedException();
		case UNSATISFIABLE:
			System.out.println("OK, proof: " + s.getProof());
			break;
		}
	}

	void disprove(Context ctx, BoolExpr f, boolean useMBQI) throws TestFailedException {
		BoolExpr[] a = {};
		disprove(ctx, f, useMBQI, a);
	}

	void disprove(Context ctx, BoolExpr f, boolean useMBQI, BoolExpr... assumptions) throws TestFailedException {
		System.out.println("Disproving: " + f);
		Solver s = ctx.mkSolver();
		Params p = ctx.mkParams();
		p.add("mbqi", useMBQI);
		s.setParameters(p);
		for (BoolExpr a : assumptions)
			s.add(a);
		s.add(ctx.mkNot(f));
		Status q = s.check();

		switch (q) {
		case UNKNOWN:
			System.out.println("Unknown because: " + s.getReasonUnknown());
			break;
		case SATISFIABLE:
			System.out.println("OK, model: " + s.getModel());
			break;
		case UNSATISFIABLE:
			throw new TestFailedException();
		}
	}

 

	void quantifierExample1(Context ctx) {
		System.out.println("QuantifierExample");
		Log.append("QuantifierExample");

		Sort[] types = new Sort[3];
		IntExpr[] xs = new IntExpr[3];
		Symbol[] names = new Symbol[3];
		IntExpr[] vars = new IntExpr[3];

		for (int j = 0; j < 3; j++) {
			types[j] = ctx.getIntSort();
			names[j] = ctx.mkSymbol("x_" + Integer.toString(j));
			xs[j] = (IntExpr) ctx.mkConst(names[j], types[j]);
			vars[j] = (IntExpr) ctx.mkBound(2 - j, types[j]); // <-- vars
																// reversed!
		}

		Expr body_vars = ctx.mkAnd(ctx.mkEq(ctx.mkAdd(vars[0], ctx.mkInt(1)), ctx.mkInt(2)),
				ctx.mkEq(ctx.mkAdd(vars[1], ctx.mkInt(2)), ctx.mkAdd(vars[2], ctx.mkInt(3))));

		Expr body_const = ctx.mkAnd(ctx.mkEq(ctx.mkAdd(xs[0], ctx.mkInt(1)), ctx.mkInt(2)),
				ctx.mkEq(ctx.mkAdd(xs[1], ctx.mkInt(2)), ctx.mkAdd(xs[2], ctx.mkInt(3))));

		Expr x = ctx.mkForall(types, names, body_vars, 1, null, null, ctx.mkSymbol("Q1"), ctx.mkSymbol("skid1"));
		System.out.println("Quantifier X: " + x.toString());

		Expr y = ctx.mkForall(xs, body_const, 1, null, null, ctx.mkSymbol("Q2"), ctx.mkSymbol("skid2"));
		System.out.println("Quantifier Y: " + y.toString());
	}

	void quantifierExample2(Context ctx) {

		System.out.println("QuantifierExample2");
		Log.append("QuantifierExample2");

		Expr q1, q2;
		FuncDecl f = ctx.mkFuncDecl("f", ctx.getIntSort(), ctx.getIntSort());
		FuncDecl g = ctx.mkFuncDecl("g", ctx.getIntSort(), ctx.getIntSort());

		// Quantifier with Exprs as the bound variables.
		{
			Expr x = ctx.mkConst("x", ctx.getIntSort());
			Expr y = ctx.mkConst("y", ctx.getIntSort());
			Expr f_x = ctx.mkApp(f, x);
			Expr f_y = ctx.mkApp(f, y);
			Expr g_y = ctx.mkApp(g, y);
			@SuppressWarnings("unused")
			Pattern[] pats = new Pattern[] { ctx.mkPattern(f_x, g_y) };
			Expr[] no_pats = new Expr[] { f_y };
			Expr[] bound = new Expr[] { x, y };
			Expr body = ctx.mkAnd(ctx.mkEq(f_x, f_y), ctx.mkEq(f_y, g_y));

			q1 = ctx.mkForall(bound, body, 1, null, no_pats, ctx.mkSymbol("q"), ctx.mkSymbol("sk"));

			System.out.println(q1);
		}

		// Quantifier with de-Bruijn indices.
		{
			Expr x = ctx.mkBound(1, ctx.getIntSort());
			Expr y = ctx.mkBound(0, ctx.getIntSort());
			Expr f_x = ctx.mkApp(f, x);
			Expr f_y = ctx.mkApp(f, y);
			Expr g_y = ctx.mkApp(g, y);
			@SuppressWarnings("unused")
			Pattern[] pats = new Pattern[] { ctx.mkPattern(f_x, g_y) };
			Expr[] no_pats = new Expr[] { f_y };
			Symbol[] names = new Symbol[] { ctx.mkSymbol("x"), ctx.mkSymbol("y") };
			Sort[] sorts = new Sort[] { ctx.getIntSort(), ctx.getIntSort() };
			Expr body = ctx.mkAnd(ctx.mkEq(f_x, f_y), ctx.mkEq(f_y, g_y));

			q2 = ctx.mkForall(sorts, names, body, 1, null, // pats,
					no_pats, ctx.mkSymbol("q"), ctx.mkSymbol("sk"));
			System.out.println(q2);
		}

		System.out.println(q1.equals(q2));
	}

	 
	void logicExample(Context ctx) throws TestFailedException {
		System.out.println("LogicTest");
		Log.append("LogicTest");

		com.microsoft.z3.Global.ToggleWarningMessages(true);

		BitVecSort bvs = ctx.mkBitVecSort(32);
		Expr x = ctx.mkConst("x", bvs);
		Expr y = ctx.mkConst("y", bvs);
		BoolExpr eq = ctx.mkEq(x, y);

		// Use a solver for QF_BV
		Solver s = ctx.mkSolver("QF_BV");
		s.add(eq);
		Status res = s.check();
		System.out.println("solver result: " + res);

		// Or perhaps a tactic for QF_BV
		Goal g = ctx.mkGoal(true, false, false);
		g.add(eq);

		Tactic t = ctx.mkTactic("qfbv");
		ApplyResult ar = t.apply(g);
		System.out.println("tactic result: " + ar);

		if (ar.getNumSubgoals() != 1 || !ar.getSubgoals()[0].isDecidedSat())
			throw new TestFailedException();
	}

 
	public void findModelExample1(Context ctx) throws TestFailedException {
		System.out.println("FindModelExample1");
		Log.append("FindModelExample1");

		BoolExpr x = ctx.mkBoolConst("x");
		BoolExpr y = ctx.mkBoolConst("y");
		BoolExpr x_xor_y = ctx.mkXor(x, y);

		Model model = check(ctx, x_xor_y, Status.SATISFIABLE);
		System.out.println("x = " + model.evaluate(x, false) + ", y = " + model.evaluate(y, false));
	}

	// / Find a model for <tt>x < y + 1, x > 2</tt>.
	// / Then, assert <tt>not(x = y)</tt>, and find another model.

	public void findModelExample2(Context ctx) throws TestFailedException {
		System.out.println("FindModelExample2");
		Log.append("FindModelExample2");

		IntExpr x = ctx.mkIntConst("x");
		
		IntExpr y = ctx.mkIntConst("y");
		IntExpr one = ctx.mkInt(1);
		IntExpr two = ctx.mkInt(2);
 
		ArithExpr y_plus_one = ctx.mkAdd(y, one);

		BoolExpr c1 = ctx.mkLt(x, y_plus_one);
		BoolExpr c2 = ctx.mkGt(x, two);

		BoolExpr q = ctx.mkAnd(c1, c2);
 
		 
		
 		 
		System.out.println("model for: x < y + 1, x > 2");
		Model model = check(ctx, q, Status.SATISFIABLE);
		System.out.println("x = " + model.evaluate(x, false) + ", y =" + model.evaluate(y, false));

		/* assert not(x = y) */
		BoolExpr x_eq_y = ctx.mkEq(x, y);
		BoolExpr c3 = ctx.mkNot(x_eq_y);

		q = ctx.mkAnd(q, c3);

		System.out.println("model for: x < y + 1, x > 2, not(x = y)");
		model = check(ctx, q, Status.SATISFIABLE);
		System.out.println("x = " + model.evaluate(x, false) + ", y = " + model.evaluate(y, false));
	}

	// / Prove <tt>x = y implies g(x) = g(y)</tt>, and
	// / disprove <tt>x = y implies g(g(x)) = g(y)</tt>.

	// / <remarks>This function demonstrates how to create uninterpreted
	// / types and functions.</remarks>
	public void proveExample1(Context ctx) throws TestFailedException {
		System.out.println("ProveExample1");
		Log.append("ProveExample1");

		/* create uninterpreted type. */
		Sort U = ctx.mkUninterpretedSort(ctx.mkSymbol("U"));

		/* declare function g */
		FuncDecl g = ctx.mkFuncDecl("g", U, U);

		/* create x and y */
		Expr x = ctx.mkConst("x", U);
		Expr y = ctx.mkConst("y", U);
		/* create g(x), g(y) */
		Expr gx = g.apply(x);
		Expr gy = g.apply(y);

		/* assert x = y */
		BoolExpr eq = ctx.mkEq(x, y);

		/* prove g(x) = g(y) */
		BoolExpr f = ctx.mkEq(gx, gy);
		System.out.println("prove: x = y implies g(x) = g(y)");
		prove(ctx, ctx.mkImplies(eq, f), false);

		/* create g(g(x)) */
		Expr ggx = g.apply(gx);

		/* disprove g(g(x)) = g(y) */
		f = ctx.mkEq(ggx, gy);
		System.out.println("disprove: x = y implies g(g(x)) = g(y)");
		disprove(ctx, ctx.mkImplies(eq, f), false);

		/* Print the model using the custom model printer */
		Model m = check(ctx, ctx.mkNot(f), Status.SATISFIABLE);
		System.out.println(m);
	}

	// / Prove <tt>not(g(g(x) - g(y)) = g(z)), x + z <= y <= x implies z < 0
	// </tt>.
	// / Then, show that <tt>z < -1</tt> is not implied.

	// / <remarks>This example demonstrates how to combine uninterpreted
	// functions
	// / and arithmetic.</remarks>
	public void proveExample2(Context ctx) throws TestFailedException {
		System.out.println("ProveExample2");
		Log.append("ProveExample2");

		/* declare function g */
		Sort I = ctx.getIntSort();

		FuncDecl g = ctx.mkFuncDecl("g", I, I);

		/* create x, y, and z */
		IntExpr x = ctx.mkIntConst("x");
		IntExpr y = ctx.mkIntConst("y");
		IntExpr z = ctx.mkIntConst("z");

		/* create gx, gy, gz */
		Expr gx = ctx.mkApp(g, x);
		Expr gy = ctx.mkApp(g, y);
		Expr gz = ctx.mkApp(g, z);

		/* create zero */
		IntExpr zero = ctx.mkInt(0);

		/* assert not(g(g(x) - g(y)) = g(z)) */
		ArithExpr gx_gy = ctx.mkSub((IntExpr) gx, (IntExpr) gy);
		Expr ggx_gy = ctx.mkApp(g, gx_gy);
		BoolExpr eq = ctx.mkEq(ggx_gy, gz);
		BoolExpr c1 = ctx.mkNot(eq);

		/* assert x + z <= y */
		ArithExpr x_plus_z = ctx.mkAdd(x, z);
		BoolExpr c2 = ctx.mkLe(x_plus_z, y);

		/* assert y <= x */
		BoolExpr c3 = ctx.mkLe(y, x);

		/* prove z < 0 */
		BoolExpr f = ctx.mkLt(z, zero);
		System.out.println("prove: not(g(g(x) - g(y)) = g(z)), x + z <= y <= x implies z < 0");
		prove(ctx, f, false, c1, c2, c3);

		/* disprove z < -1 */
		IntExpr minus_one = ctx.mkInt(-1);
		f = ctx.mkLt(z, minus_one);
		System.out.println("disprove: not(g(g(x) - g(y)) = g(z)), x + z <= y <= x implies z < -1");
		disprove(ctx, f, false, c1, c2, c3);
	}

 
 
 
	// / <remarks></remarks>
	public void parserExample5(Context ctx) {
		System.out.println("ParserExample5");

		try {
			ctx.parseSMTLIB2String(
					/*
					 * the following string has a parsing error: missing parenthesis
					 */
					"(declare-const x Int (declare-const y Int)) (assert (> x y))", null, null, null, null);
		} catch (Z3Exception e) {
			System.out.println("Z3 error: " + e);
		}
	}

	 
 
 
  
	public void evalExample1(Context ctx) {
		System.out.println("EvalExample1");
		Log.append("EvalExample1");

		IntExpr x = ctx.mkIntConst("x");
		IntExpr y = ctx.mkIntConst("y");
		IntExpr two = ctx.mkInt(2);

		Solver solver = ctx.mkSolver();

		/* assert x < y */
		solver.add(ctx.mkLt(x, y));

		/* assert x > 2 */
		solver.add(ctx.mkGt(x, two));

		/* find model for the constraints above */
		Model model = null;
		if (Status.SATISFIABLE == solver.check()) {
			model = solver.getModel();
			System.out.println(model);
			System.out.println("\nevaluating x+y");
			Expr v = model.evaluate(ctx.mkAdd(x, y), false);
			if (v != null) {
				System.out.println("result = " + (v));
			} else {
				System.out.println("Failed to evaluate: x+y");
			}
		} else {
			System.out.println("BUG, the constraints are satisfiable.");
		}
	}

	// / Demonstrate how to use #Eval on tuples.

	public void evalExample2(Context ctx) {
		System.out.println("EvalExample2");
		Log.append("EvalExample2");

		Sort int_type = ctx.getIntSort();
		TupleSort tuple = ctx.mkTupleSort(ctx.mkSymbol("mk_tuple"), // name of
																	// tuple
																	// constructor
				new Symbol[] { ctx.mkSymbol("first"), ctx.mkSymbol("second") }, // names
																				// of
																				// projection
																				// operators
				new Sort[] { int_type, int_type } // types of projection
													// operators
		);
		FuncDecl first = tuple.getFieldDecls()[0]; // declarations are for
													// projections
		FuncDecl second = tuple.getFieldDecls()[1];
		Expr tup1 = ctx.mkConst("t1", tuple);
		Expr tup2 = ctx.mkConst("t2", tuple);

		Solver solver = ctx.mkSolver();

		/* assert tup1 != tup2 */
		solver.add(ctx.mkNot(ctx.mkEq(tup1, tup2)));
		/* assert first tup1 = first tup2 */
		solver.add(ctx.mkEq(ctx.mkApp(first, tup1), ctx.mkApp(first, tup2)));

		/* find model for the constraints above */
		Model model = null;
		if (Status.SATISFIABLE == solver.check()) {
			model = solver.getModel();
			System.out.println(model);
			System.out.println("evaluating tup1 " + (model.evaluate(tup1, false)));
			System.out.println("evaluating tup2 " + (model.evaluate(tup2, false)));
			System.out.println("evaluating second(tup2) " + (model.evaluate(ctx.mkApp(second, tup2), false)));
		} else {
			System.out.println("BUG, the constraints are satisfiable.");
		}
	}

 

	public void simplifierExample(Context ctx) {
		System.out.println("SimplifierExample");
		Log.append("SimplifierExample");

		IntExpr x = ctx.mkIntConst("x");
		IntExpr y = ctx.mkIntConst("y");
		IntExpr z = ctx.mkIntConst("z");
		@SuppressWarnings("unused")
		IntExpr u = ctx.mkIntConst("u");

		Expr t1 = ctx.mkAdd(x, ctx.mkSub(y, ctx.mkAdd(x, z)));
		Expr t2 = t1.simplify();
		System.out.println((t1) + " -> " + (t2));
		
		
		//ctx.mkForall(arg0, arg1, arg2, arg3, arg4, arg5, arg6)
		
		
	}

	// / Extract unsatisfiable core example

	public void unsatCoreAndProofExample(Context ctx) {
		System.out.println("UnsatCoreAndProofExample");
		Log.append("UnsatCoreAndProofExample");

		Solver solver = ctx.mkSolver();

		BoolExpr pa = ctx.mkBoolConst("PredA");
		BoolExpr pb = ctx.mkBoolConst("PredB");
		BoolExpr pc = ctx.mkBoolConst("PredC");
		BoolExpr pd = ctx.mkBoolConst("PredD");
		BoolExpr p1 = ctx.mkBoolConst("P1");
		BoolExpr p2 = ctx.mkBoolConst("P2");
		BoolExpr p3 = ctx.mkBoolConst("P3");
		BoolExpr p4 = ctx.mkBoolConst("P4");

		BoolExpr[] assumptions = new BoolExpr[] { ctx.mkNot(p1), ctx.mkNot(p2), ctx.mkNot(p3), ctx.mkNot(p4) };
		BoolExpr f1 = ctx.mkAnd(pa, pb, pc);
		BoolExpr f2 = ctx.mkAnd(pa, ctx.mkNot(pb), pc);
		BoolExpr f3 = ctx.mkOr(ctx.mkNot(pa), ctx.mkNot(pc));
		BoolExpr f4 = pd;

		solver.add(ctx.mkOr(f1, p1));
		solver.add(ctx.mkOr(f2, p2));
		solver.add(ctx.mkOr(f3, p3));
		solver.add(ctx.mkOr(f4, p4));
		Status result = solver.check(assumptions);

		if (result == Status.UNSATISFIABLE) {
			System.out.println("unsat");
			System.out.println("proof: " + solver.getProof());
			System.out.println("core: ");
			for (Expr c : solver.getUnsatCore()) {
				System.out.println(c);
			}
		}
	}

	public void unsatCoreAndProofExampleGuard(Context ctx) {
		System.out.println("UnsatCoreAndProofExample");
		Log.append("UnsatCoreAndProofExample");

		Solver solver = ctx.mkSolver();

		List<BoolExpr> stateExp = new ArrayList<>();
		String sous = "C1,N2";
		for (String s : Arrays.asList(sous.split(","))) {
			stateExp.add(ctx.mkBoolConst(s.replace("=", "eq")));
		}

		List<BoolExpr> guardExp = new ArrayList<>();

		String gS = "T1,N2";
		for (String s : Arrays.asList(gS.split(","))) {
			guardExp.add(ctx.mkBoolConst(s.replace("=", "eq")));
		}

		BoolExpr stateAnds = ctx.mkAnd(stateExp.toArray(new BoolExpr[stateExp.size()]));

		BoolExpr guardAll = ctx.mkAnd(guardExp.toArray(new BoolExpr[guardExp.size()]));

		BoolExpr tobesolved = ctx.mkImplies(guardAll, stateAnds);

		solver.add(ctx.mkNot(tobesolved));

		Status result = solver.check();

		if (result != Status.UNSATISFIABLE) {
			System.out.println("Do not pass the guard");
			// System.out.println("proof: " + solver.getProof());
			// System.out.println("core: ");
			// for (Expr c : solver.getUnsatCore())
			// {
			// System.out.println(c);
			// }
		} else {

			System.out.println("Passes");
		}
	}

	/// Extract unsatisfiable core example with AssertAndTrack

	public void unsatCoreAndProofExample2(Context ctx) {
		System.out.println("UnsatCoreAndProofExample2");
		Log.append("UnsatCoreAndProofExample2");

		Solver solver = ctx.mkSolver();

		BoolExpr pa = ctx.mkBoolConst("PredA");
		BoolExpr pb = ctx.mkBoolConst("PredB");
		BoolExpr pc = ctx.mkBoolConst("PredC");
		BoolExpr pd = ctx.mkBoolConst("PredD");

		BoolExpr f1 = ctx.mkAnd(new BoolExpr[] { pa, pb, pc });
		BoolExpr f2 = ctx.mkAnd(new BoolExpr[] { pa, ctx.mkNot(pb), pc });
		BoolExpr f3 = ctx.mkOr(ctx.mkNot(pa), ctx.mkNot(pc));
		BoolExpr f4 = pd;

		BoolExpr p1 = ctx.mkBoolConst("P1");
		BoolExpr p2 = ctx.mkBoolConst("P2");
		BoolExpr p3 = ctx.mkBoolConst("P3");
		BoolExpr p4 = ctx.mkBoolConst("P4");

		solver.assertAndTrack(f1, p1);
		solver.assertAndTrack(f2, p2);
		solver.assertAndTrack(f3, p3);
		solver.assertAndTrack(f4, p4);
		Status result = solver.check();

		if (result == Status.UNSATISFIABLE) {
			System.out.println("unsat");
			System.out.println("core: ");
			for (Expr c : solver.getUnsatCore()) {
				System.out.println(c);
			}
		}
	}

 
	public void floatingPointExample1(Context ctx) throws TestFailedException {
		System.out.println("FloatingPointExample1");
		Log.append("FloatingPointExample1");

		FPSort s = ctx.mkFPSort(11, 53);
		System.out.println("Sort: " + s);

		FPNum x = (FPNum) ctx.mkNumeral("-1e1", s); /* -1 * 10^1 = -10 */
		FPNum y = (FPNum) ctx.mkNumeral("-10", s); /* -10 */
		FPNum z = (FPNum) ctx.mkNumeral("-1.25p3", s); /* -1.25 * 2^3 = -1.25 * 8 = -10 */
		System.out.println("x=" + x.toString() + "; y=" + y.toString() + "; z=" + z.toString());

		BoolExpr a = ctx.mkAnd(ctx.mkFPEq(x, y), ctx.mkFPEq(y, z));
		check(ctx, ctx.mkNot(a), Status.UNSATISFIABLE);

		/*
		 * nothing is equal to NaN according to floating-point equality, so NaN == k
		 * should be unsatisfiable.
		 */
		FPExpr k = (FPExpr) ctx.mkConst("x", s);
		FPExpr nan = ctx.mkFPNaN(s);

		/* solver that runs the default tactic for QF_FP. */
		Solver slvr = ctx.mkSolver("QF_FP");
		slvr.add(ctx.mkFPEq(nan, k));
		if (slvr.check() != Status.UNSATISFIABLE)
			throw new TestFailedException();
		System.out.println("OK, unsat:" + System.getProperty("line.separator") + slvr);

		/* NaN is equal to NaN according to normal equality. */
		slvr = ctx.mkSolver("QF_FP");
		slvr.add(ctx.mkEq(nan, nan));
		if (slvr.check() != Status.SATISFIABLE)
			throw new TestFailedException();
		System.out.println("OK, sat:" + System.getProperty("line.separator") + slvr);

		/* Let's prove -1e1 * -1.25e3 == +100 */
		x = (FPNum) ctx.mkNumeral("-1e1", s);
		y = (FPNum) ctx.mkNumeral("-1.25p3", s);
		FPExpr x_plus_y = (FPExpr) ctx.mkConst("x_plus_y", s);
		FPNum r = (FPNum) ctx.mkNumeral("100", s);
		slvr = ctx.mkSolver("QF_FP");

		slvr.add(ctx.mkEq(x_plus_y, ctx.mkFPMul(ctx.mkFPRoundNearestTiesToAway(), x, y)));
		slvr.add(ctx.mkNot(ctx.mkFPEq(x_plus_y, r)));
		if (slvr.check() != Status.UNSATISFIABLE)
			throw new TestFailedException();
		System.out.println("OK, unsat:" + System.getProperty("line.separator") + slvr);
	}

	public void floatingPointExample2(Context ctx) throws TestFailedException {
		System.out.println("FloatingPointExample2");
		Log.append("FloatingPointExample2");
		FPSort double_sort = ctx.mkFPSort(11, 53);
		FPRMSort rm_sort = ctx.mkFPRoundingModeSort();

		FPRMExpr rm = (FPRMExpr) ctx.mkConst(ctx.mkSymbol("rm"), rm_sort);
		BitVecExpr x = (BitVecExpr) ctx.mkConst(ctx.mkSymbol("x"), ctx.mkBitVecSort(64));
		FPExpr y = (FPExpr) ctx.mkConst(ctx.mkSymbol("y"), double_sort);
		FPExpr fp_val = ctx.mkFP(42, double_sort);

		BoolExpr c1 = ctx.mkEq(y, fp_val);
		BoolExpr c2 = ctx.mkEq(x, ctx.mkFPToBV(rm, y, 64, false));
		BoolExpr c3 = ctx.mkEq(x, ctx.mkBV(42, 64));
		BoolExpr c4 = ctx.mkEq(ctx.mkNumeral(42, ctx.getRealSort()), ctx.mkFPToReal(fp_val));
		BoolExpr c5 = ctx.mkAnd(c1, c2, c3, c4);
		System.out.println("c5 = " + c5);

		/* Generic solver */
		Solver s = ctx.mkSolver();
		s.add(c5);

		if (s.check() != Status.SATISFIABLE)
			throw new TestFailedException();

		System.out.println("OK, model: " + s.getModel().toString());
	}

	public void optimizeExample(Context ctx) {
		System.out.println("Opt");

		Optimize opt = ctx.mkOptimize();

		// Set constraints.
		IntExpr xExp = ctx.mkIntConst("x");
		IntExpr yExp = ctx.mkIntConst("y");

		opt.Add(ctx.mkEq(ctx.mkAdd(xExp, yExp), ctx.mkInt(10)), ctx.mkGe(xExp, ctx.mkInt(0)),
				ctx.mkGe(yExp, ctx.mkInt(0)));

		// Set objectives.
		Optimize.Handle mx = opt.MkMaximize(xExp);
		Optimize.Handle my = opt.MkMaximize(yExp);

		System.out.println(opt.Check());
		System.out.println(mx);
		System.out.println(my);
	}

	public void translationExample() {
		Context ctx1 = new Context();
		Context ctx2 = new Context();

		Sort s1 = ctx1.getIntSort();
		Sort s2 = ctx2.getIntSort();
		Sort s3 = s1.translate(ctx2);

		System.out.println(s1 == s2);
		System.out.println(s1.equals(s2));
		System.out.println(s2.equals(s3));
		System.out.println(s1.equals(s3));

		Expr e1 = ctx1.mkIntConst("e1");
		Expr e2 = ctx2.mkIntConst("e1");
		Expr e3 = e1.translate(ctx2);

		System.out.println(e1 == e2);
		System.out.println(e1.equals(e2));
		System.out.println(e2.equals(e3));
		System.out.println(e1.equals(e3));
	}

	public static void mainJunk() {
		JavaExample p = new JavaExample();
		try {
			com.microsoft.z3.Global.ToggleWarningMessages(true);
			Log.open("test.log");

			System.out.print("Z3 Major Version: ");
			System.out.println(Version.getMajor());
			System.out.print("Z3 Full Version: ");
			System.out.println(Version.getString());
			System.out.print("Z3 Full Version String: ");
			System.out.println(Version.getFullVersion());

			p.simpleExample();

			// { // These examples need model generation turned on.
			// HashMap<String, String> cfg = new HashMap<String, String>();
			// cfg.put("model", "true");
			// Context ctx = new Context(cfg);
			//
			// p.optimizeExample(ctx);
			// p.basicTests(ctx);
			// p.castingTest(ctx);
			// p.sudokuExample(ctx);
			// p.quantifierExample1(ctx);
			// p.quantifierExample2(ctx);
			// p.logicExample(ctx);
			// p.parOrExample(ctx);
			// p.findModelExample1(ctx);
			// p.findModelExample2(ctx);
			// p.pushPopExample1(ctx);
			// p.arrayExample1(ctx);
			// p.arrayExample3(ctx);
			// p.bitvectorExample1(ctx);
			// p.bitvectorExample2(ctx);
			// // p.parserExample1(ctx);
			// //p.parserExample2(ctx);
			// p.parserExample5(ctx);
			// p.iteExample(ctx);
			// p.evalExample1(ctx);
			// p.evalExample2(ctx);
			// p.findSmallModelExample(ctx);
			// p.simplifierExample(ctx);
			// p.finiteDomainExample(ctx);
			// p.floatingPointExample1(ctx);
			// // core dumps: p.floatingPointExample2(ctx);
			// }

			{ // These examples need proof generation turned on.
				HashMap<String, String> cfg = new HashMap<String, String>();
				cfg.put("proof", "true");
				Context ctx = new Context(cfg);
				// p.proveExample1(ctx);
				// p.proveExample2(ctx);
				// p.arrayExample2(ctx);
				// p.tupleExample(ctx);
				// // throws p.parserExample3(ctx);
				// p.enumExample(ctx);
				// p.listExample(ctx);
				// p.treeExample(ctx);
				// p.forestExample(ctx);
				// p.unsatCoreAndProofExample(ctx);
				// p.unsatCoreAndProofExample2(ctx);
				p.unsatCoreAndProofExampleGuard(ctx);
			}

			// { // These examples need proof generation turned on and auto-config
			// // set to false.
			// HashMap<String, String> cfg = new HashMap<String, String>();
			// cfg.put("proof", "true");
			// cfg.put("auto-config", "false");
			// Context ctx = new Context(cfg);
			// p.quantifierExample3(ctx);
			// p.quantifierExample4(ctx);
			// }

			p.translationExample();

			Log.close();
			if (Log.isOpen())
				System.out.println("Log is still open!");
		} catch (Z3Exception ex) {
			System.out.println("Z3 Managed Exception: " + ex.getMessage());
			System.out.println("Stack trace: ");
			ex.printStackTrace(System.out);
		} catch (Exception ex) {
			System.out.println("Unknown Exception: " + ex.getMessage());
			System.out.println("Stack trace: ");
			ex.printStackTrace(System.out);
		}

	}
}