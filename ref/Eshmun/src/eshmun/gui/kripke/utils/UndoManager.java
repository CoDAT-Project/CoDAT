package eshmun.gui.kripke.utils;

import java.util.ArrayList;
import java.util.HashMap;

import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

public class UndoManager {
	static final int LIMIT = 5;
	int size;
	
	GraphPanel draw;
	StateNode current;
	StateNode start;
	
	public UndoManager(GraphPanel draw) {
		this.draw = draw;
		
		ArrayList<State> copiedStates = draw.copyStates();
		current = new StateNode(copiedStates, draw.copyTransitions(), draw.copyStateNames(copiedStates));
		start = current;
		
		size = 1;
	}
		
	public boolean canUndo() {
		return current.previous != null;
	}
	
	public void undo() {
		if(current.previous == null)
			return;
		
		current = current.previous;
		current.apply(draw);
		size--;
		
		draw.repaint();
	}
	
	public boolean canRedo() {
		return current.next != null;
	}
	
	public void redo() {
		if(current.next == null)
			return;
		
		current = current.next;
		current.apply(draw);
		size++;
		
		draw.repaint();
	}
	
	public void register() {
		ArrayList<State> copiedStates = draw.copyStates();
		current.next = new StateNode(copiedStates, draw.copyTransitions(), draw.copyStateNames(copiedStates));
		
		current.next.previous = current;
		current = current.next;
		size++;
		
		if(size > LIMIT) {
			size--;
			
			start = start.next;
			start.previous = null;
		}
	}
	
	private class StateNode {
		private ArrayList<State> states;
		private ArrayList<Transition> edges;
		private HashMap<String, State> stateNames;
		
		private StateNode previous;
		private StateNode next;
		
		private StateNode(ArrayList<State> states, ArrayList<Transition> edges, HashMap<String, State> stateNames) {
			this.states = states;
			this.edges = edges;
			this.stateNames = stateNames;
			
			next = null;
			previous = null;
		}
		
		private void apply(GraphPanel d) {
			d.setStates(states);
			d.setTransitions(edges);
			d.setStateNames(stateNames);
		}
	}
}
