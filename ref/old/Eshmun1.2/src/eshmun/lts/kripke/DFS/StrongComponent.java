package eshmun.lts.kripke.DFS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eshmun.lts.kripke.KripkeState;

public class StrongComponent {
	private List<KripkeState> states;

	public StrongComponent() {
		super();
		this.states = new ArrayList<KripkeState>();
	}
	
	
	public int size() {
		return states.size();
	}
	
	public void addState(KripkeState state) {
		this.states.add(state);
	}
	
	public List<KripkeState> getStatesList() {
		return this.states;
	}
	
	public Iterator<KripkeState> getStates() {
		return states.iterator();
	}
}
