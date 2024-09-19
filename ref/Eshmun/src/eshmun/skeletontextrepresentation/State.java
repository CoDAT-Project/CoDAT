package eshmun.skeletontextrepresentation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.TreeSet;

public class State {

	private ArrayList<String> atomicPropositions = new ArrayList<String>();

	private Map<String, String> sharedVariables = new LinkedHashMap<String, String>();

	private TreeSet<String> getListOfVariableAssignments(boolean withNulls) {

		TreeSet<String> assignments = new TreeSet<>();

		Map<String, String> sorted = new LinkedHashMap<>();
		// Copy all data from hashMap into TreeMap
		sorted.putAll(sharedVariables);

		for (Map.Entry<String, String> entry : sorted.entrySet())
			if (withNulls)
				assignments.add(entry.getKey() + "=" + entry.getValue());
			else {
				if (!entry.getValue().equals("null"))
					assignments.add(entry.getKey() + "=" + entry.getValue());
			}

		return assignments;
	}

	/*
	 * Public Methods
	 */
 
	public State(String labels) {

		List<String> labelsList = Arrays.asList(labels.split(","));
		for (String string : labelsList) {
			if (!this.atomicPropositions.contains(string))
				this.atomicPropositions.add(string);
		}
	 
		
		//System.out.println("Before Compare :" + atomicPropositions);
	
		 Collections.sort(this.atomicPropositions, new StateChainedComparator(
	                new StateProcessNumberComparator(), new StateProcessLabelsComparator())
	               
	        );
		

	//	System.out.println("After Compare :" + atomicPropositions);

	}

	public State(ArrayList<String> aPs, Map<String, String> sh) {
		
		for (String x : aPs) {
			if (!this.atomicPropositions.contains(x))
				this.atomicPropositions.add(x);
		}

		this.sharedVariables = new LinkedHashMap<>(sh);
		
		//System.out.println("Before Compare :" + this.atomicPropositions);
		
		
 
		 Collections.sort(this.atomicPropositions, new StateChainedComparator(
	                new StateProcessNumberComparator(), new StateProcessLabelsComparator())
	               
	        );
		
	 
		
		
		

		//System.out.println("After Compare :" + this.atomicPropositions);

	}

	public State replaceOrAddAtomicProposition(String toRemove, String toAdd) {
 
		
		if (this.atomicPropositions.contains(toRemove)) {
			
			ArrayList<String> aPs = new ArrayList<>(this.atomicPropositions);
			int indexOfOldAP = aPs.indexOf(toRemove);
			aPs.set(indexOfOldAP, toAdd);
			
			State state = new State(aPs, this.sharedVariables);

			return state;
		}else {
			ArrayList<String> aPs = new ArrayList<>(this.atomicPropositions);
			aPs.add(toAdd);
			State state = new State(aPs, this.sharedVariables);
			return state;
		}

		
	}
	
	public State addAtomicProposition(String toAdd) {
		ArrayList<String> aPs = new ArrayList<>(this.atomicPropositions);
		aPs.add(toAdd);
		State state = new State(aPs, this.sharedVariables);
		return state;
	}
	
	public State removeAtomicProposition(String toRemove) {
		ArrayList<String> aPs = new ArrayList<>(this.atomicPropositions);
		aPs.remove(toRemove);
		State state = new State(aPs, this.sharedVariables);
		return state;
		
	}
	

	public State addOrUpdateVariableAssignment(String varName, String varValue) {

		 
		
		Map<String, String> sh = new LinkedHashMap<String, String>(this.sharedVariables);
		sh.put(varName, varValue);

		State state = new State(this.atomicPropositions, sh);

		return state;
	}

	public boolean stateContainsLabel(String l) {

		return atomicPropositions.contains(l) || getListOfVariableAssignments(true).contains(l);

	}

	public boolean stateContainsVariable(String v) {

		return sharedVariables.containsKey(v);

	}

	public Map<String,String> getVariableAssignments() {

		return this.sharedVariables;
	}
	
	public ArrayList<String> getLabels() {

		return atomicPropositions;
	}
	
	public int countOfaProcessLabels(int processNumber) {
		
		int count = 0;
		String process = processNumber+""; 
		for (String lbl : getLabels()) {
			if(lbl.endsWith(process))
				count++;
		}
		
		return count;
	}

	@Override
	public String toString() {

		if (getListOfVariableAssignments(true).size() > 0) {
			return String.join(",", atomicPropositions) + "," + String.join(",", getListOfVariableAssignments(true));
		} else {
			return String.join(",", atomicPropositions);
		}

	}

	public String toStringWithoutNull() {

		TreeSet<String> vars = getListOfVariableAssignments(false);

		return String.join(",", atomicPropositions) + (vars.size() > 0 ? ("," + String.join(",", vars)) : "");

	}

	// Overriding equals() to compare two State objects
	@Override
	public boolean equals(Object o) {

		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		/* Check if o is an instance of State or not */
		if (!(o instanceof State)) {
			return false;
		}

		State s = (State) o;

		// Compare the data members and return accordingly
		
	 
		
		return this.toString().equals(s.toString());

	}

	// Idea from effective Java : Item 9
	@Override
	public int hashCode() {
		
		

		return Objects.hash(this.toString());
	}

}
