package eshmun.skeletontextrepresentation.z3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import eshmun.skeletontextrepresentation.State;
import eshmun.skeletontextrepresentation.guard.Guard;
import eshmun.skeletontextrepresentation.guard.Z3EnableStateVisitor;

public class GuardSolver {

	public static boolean checkIfStatePassesGuard(Guard gd, State state) {

		try {

			Context ctx = new Context();
			Solver solver = ctx.mkSolver();

			List<BoolExpr> stateExp = new ArrayList<>();
			String sous = state.toString();
			for (String s : Arrays.asList(sous.split(","))) {
				stateExp.add(ctx.mkBoolConst(s.replace("=", "eq")));
			}

			Z3EnableStateVisitor visitor = new Z3EnableStateVisitor();
			BoolExpr guardExp = gd.accept(visitor, ctx);

			BoolExpr stateAnds = ctx.mkAnd(stateExp.toArray(new BoolExpr[stateExp.size()]));

			BoolExpr tobesolved = ctx.mkImplies( stateAnds, guardExp);

			solver.add(ctx.mkNot(tobesolved));

			Status result = solver.check();

			if (result != Status.UNSATISFIABLE) {
				return false;

			} else {

				return true;
			}

		} catch (Z3Exception ex) {
			System.out.println("Z3 Managed Exception: " + ex.getMessage());
			System.out.println("Stack trace: ");
			ex.printStackTrace(System.out);
		} catch (Exception ex) {
			System.out.println("Unknown Exception: " + ex.getMessage());
			System.out.println("Stack trace: ");
			ex.printStackTrace(System.out);
		}
		
		return false;

	}

}
