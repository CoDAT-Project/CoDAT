package eshmun.skeletontextrepresentation.infinitespace.structures;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.print.attribute.standard.RequestingUserName;

import org.stringtemplate.v4.compiler.CodeGenerator.includeExpr_return;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateAction;
import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;

public class InfiniteState {
	
	public int stateNumber = 0;

	private BoolExpr statePredicate = null;
	public boolean isStart = false;
	public HashSet<String> stateLabels = new HashSet<>();
	
	public Map<InfiniteStateAction, BoolExpr> strongestPostConditions = new HashMap<InfiniteStateAction, BoolExpr>();
	
	public Map<InfiniteStateAction, Boolean> actionsSAT	= new HashMap<InfiniteStateAction, Boolean>();
	
	
	public HashSet<InfiniteState> successors = new HashSet<>();
	
 
	
	public Map<BoolExpr, HashSet<InfiniteState>> notsuccessors	= new HashMap<BoolExpr, HashSet<InfiniteState>>();
	
	
	public void setPredicate(BoolExpr b) {
		statePredicate = b;
	}
	
	public BoolExpr getPredicate() {
		return  statePredicate;
	}

	public InfiniteState(BoolExpr b) {
		statePredicate = b;
	}
	
	public InfiniteState(BoolExpr statePredicate, HashSet<String> stateLabels) {
		this.statePredicate = statePredicate;
		this.stateLabels = stateLabels;
	}
	
	
	public InfiniteState(BoolExpr b, boolean isStart) {
		statePredicate = b;
		this.isStart = isStart;
	}
	
	public void addLabel(String l) {
		stateLabels.add(l);
	}
	
	public void setNumber(int n) {
		this.stateNumber = n;
	}
	
	public String getLabels() {
		
		if(stateLabels != null) {
			List<String> list = new ArrayList<String>(stateLabels);
			
			Collections.sort(list, new Comparator<String>() 
			  {
			    public int compare(String str1, String str2) 
			    {                 
			       String substr1 = str1.substring(str1.length()-2);
			       String substr2 = str2.substring(str2.length()-2);
			                
			       return String.valueOf(substr1).compareTo(String.valueOf(substr2));                           
			    }
			  });
			
			
			Collections.sort(list, new Comparator<String>() 
			  {
			    public int compare(String str1, String str2) 
			    {                 
			       String substr1 = str1.substring(str1.length()-1);
			       String substr2 = str2.substring(str2.length()-1);
			                
			       return String.valueOf(substr1).compareTo(String.valueOf(substr2));                           
			    }
			  });

			return String.join(",", list);
		}else {
			return null;
		}
		
		
	}

	public String toString() {

		return statePredicate.simplify().toString();
	}

	// Overriding equals() to compare two State objects
//	@Override
//	public boolean equals(Object o) {
//
//		// If the object is compared with itself then return true
//		if (o == this) {
//			return true;
//		}
//
//		/* Check if o is an instance of State or not */
//		if (!(o instanceof InfiniteState)) {
//			return false;
//		}
//
//		InfiniteState s = (InfiniteState) o;
//
//		return this.toString().equals(s.toString());
//
//	}
	
	public boolean IsEquals(Object o) {

		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		/* Check if o is an instance of State or not */
		if (!(o instanceof InfiniteState)) {
			return false;
		}

		InfiniteState s = (InfiniteState) o;
		
		//return s == this;

		// Compare the data members and return accordingly

		Context ctx = InfiniteStateActionFactory.context;

		Solver solver = ctx.mkSolver();
		 
		solver.add(ctx.mkNot(ctx.mkEq(this.statePredicate, s.statePredicate)));
		
		Status result = solver.check();
		 
		if (result == Status.UNSATISFIABLE) {
			return true;
		} else {
			
			if(this.stateNumber == 14 && s.stateNumber == 18 || (this.stateNumber == 18 && s.stateNumber == 14)) {
				return false;
			}
			return false;
		}

	}

	// Idea from effective Java : Item 9
//	@Override
//	public int hashCode() {
//
//		return Objects.hash(31);
//	}

}
