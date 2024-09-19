package eshmun.lts.kripke;

import java.util.List;

public class Box {
	
	private KripkeState inState;
	private List<KripkeState> outStates;
	private String boxName;
	
	public Box(String name, KripkeState in, List<KripkeState> out)
	{
		inState = in;
		outStates = out;
		boxName = name;
		for (KripkeState state : outStates) {
			
			Transition tr = new Transition(inState, state, boxName+ "_" + inState.getName()+ "_" + state.getName() , boxName+ "_" + inState.getName()+ "_" + state.getName());
			inState.addOutgoingTransition(tr);
		}
	}
	
	
	public void addOutState(KripkeState out)
	{
		outStates.add(out);
		Transition tr = new Transition(inState, out, boxName+ "_" + inState.getName()+ "_" + out.getName() , boxName+ "_" + inState.getName()+ "_" + out.getName());
		inState.addOutgoingTransition(tr);
	}
	
	public KripkeState getInState() {
		return inState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxName == null) ? 0 : boxName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (boxName == null) {
			if (other.boxName != null)
				return false;
		} else if (!boxName.equals(other.boxName))
			return false;
		return true;
	}


	public List<KripkeState> getOutStates() {
		return outStates;
	}

	public String getBoxName() {
		return boxName;
	}
	
	public String generateDotClusterString(int count)
	{
		StringBuilder builder = new StringBuilder();
		
		for (KripkeState out : outStates) {
			builder.append(inState.getName() + " -> " + out.getName() + ";\r\n\t");
		}
		String cluster = "subgraph cluster_0 {\r\n\t"
		+"style=filled;\r\n\t"
		+"color=lightgrey;\r\n\t"
		+"node [style=filled,color=white];\r\n\t"
		+builder.toString()
		+"label = \"" + boxName + "\";\r\n\t"
		+ "}";
		return cluster;
	}

}
