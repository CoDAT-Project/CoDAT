package eshmun.lts.kripke.DFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class StrongComponentsGenerator {
	private List<KripkeState> componentRoots;
	private Map<Transition, EdgeType> edgeTypes;
	private Stack<KripkeState> pointsStack;
	private int lastNumber;
	private Map<KripkeState, Integer> number;
	private Map<KripkeState, Integer> lowLink;
	private List<StrongComponent> strongComponents;
	private Kripke kripke;
	
	public StrongComponentsGenerator(Kripke kripke) {
		this.kripke = kripke;
		this.strongComponents = new ArrayList<StrongComponent>();
		this.number = new HashMap<KripkeState, Integer>();
		this.lowLink = new HashMap<KripkeState, Integer>();
		this.pointsStack = new Stack<KripkeState>();
		this.edgeTypes = new HashMap<Transition, EdgeType>();
		this.componentRoots = new ArrayList<KripkeState>();
	}
	
	public void computeStrongComponents() {
		boolean unNumberedStatesExist = true;
		while(unNumberedStatesExist) {
			KripkeState toHandle = null;
			for (KripkeState u : kripke.getStatesList()) {
				if (!numbered(u)) {
					toHandle = u;
					break;
				}
			}
			if(toHandle != null) {
				strongConnected(toHandle);
				lastNumber = 0;
				pointsStack.clear();
			} else {
				unNumberedStatesExist = false;
			}
		}
	}
	
	protected void strongConnected(KripkeState u) {
		this.lastNumber++;
		number(u, this.lastNumber);
		lowLink(u, this.lastNumber);
		pointsStack.push(u);
		
		for (Transition transition : u.getTransitions()) {
			KripkeState w = transition.getEndState();
			if (!numbered(w)) {
				edgeTypes.put(transition, EdgeType.treeArc);
				strongConnected(w);
				int newLowLinkU = Math.min(getLowLink(u), getLowLink(w));
				this.lowLink(u, newLowLinkU);
			} else if (getNumber(w) < getNumber(u)){
				edgeTypes.put(transition, EdgeType.frondCrossLink);
				if (pointsStack.contains(w)) {
					int newLowLinkU = Math.min(getLowLink(u), getLowLink(w));
					this.lowLink(u, newLowLinkU);
				}
			}
		}
		
		if (getLowLink(u) == getNumber(u)) {
			componentRoots.add(u);
			StrongComponent component =new StrongComponent();
			this.strongComponents.add(component);
			while (!pointsStack.isEmpty()) {
				KripkeState w = pointsStack.peek();
				if (getNumber(w) >= getNumber(u)) {
					w = pointsStack.pop();
					component.addState(w);
				} else {
					break;
				}
			}
		}
	}
	
	protected void number(KripkeState u, Integer number) {
		this.number.put(u, number);
	}
	
	protected int getNumber(KripkeState u) {
		int number = -1;
		if (this.number.get(u) != null) {
			number = this.number.get(u);
		}
		return number;
	}
	
	protected boolean numbered(KripkeState u) {
		return number.get(u) != null;
	}
	
	protected void lowLink(KripkeState u, Integer number) {
		lowLink.put(u, number);
	}
	
	protected int getLowLink(KripkeState u) {
		int number = 9999999;
		if (this.lowLink.get(u) != null) {
			number = this.lowLink.get(u);
		}
		return number;
	}
	
	public Iterator<StrongComponent> getStrongComponents() {
		return strongComponents.iterator();
	}
	
	private enum EdgeType {
		treeArc, frondCrossLink, componentRoot;
	}
}
