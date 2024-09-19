package eshmun.gui.synchronizationskeletons.utils;

import java.util.ArrayList;

import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

public class UndoManager {
	static final int LIMIT = 5;
	int size;
	
	SynchronizationSkeletonPanel draw;
	StateNode current;
	StateNode start;
	
	public UndoManager(SynchronizationSkeletonPanel draw) {
		this.draw = draw;
		
		ArrayList<SkeletonState> copiedStates = draw.copyStates();
		current = new StateNode(copiedStates, draw.copyTransitions());
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
		ArrayList<SkeletonState> copiedStates = draw.copyStates();
		current.next = new StateNode(copiedStates, draw.copyTransitions());
		
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
		private ArrayList<SkeletonState> states;
		private ArrayList<SkeletonTransition> edges;
		
		private StateNode previous;
		private StateNode next;
		
		private StateNode(ArrayList<SkeletonState> states, ArrayList<SkeletonTransition> edges) {
			this.states = states;
			this.edges = edges;
			
			next = null;
			previous = null;
		}
		
		private void apply(SynchronizationSkeletonPanel d) {
			d.setStates(states);
			d.setTransitions(edges);
		}
	}
}
