package eshmun.structures.kripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import eshmun.structures.AbstractState;

/**
 * This class represents a Kripke State.
 * 
 * <p>A KripkeState has in addition to the content of AbstractState a list of labels that it satisfy.</p>
 * 
 * @author Ali Cherri, Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.structures.AbstractState
 */
public class KripkeState extends AbstractState {
	/**
	 * The labels that this AbstractState satisfy, NO DUPLICATES.
	 */
	private ArrayList<String> labels;

	/**
	 * Concrete Constructor for KripkeState.
	 * @param name the name of the state.
	 * @param labels the labels this state satisfies, comma separated.
	 * @param varName the name of the related variable.
	 */
	public KripkeState(String name, String labels, String varName) {
		this(name, labels, varName, false, false);
	}

	/**
	 * Concrete Constructor for KripkeState.
	 * @param name the name of the state.
	 * @param labels the labels this state satisfies, comma separated.
	 * @param varName the name of the related variable.
	 * @param isStart if this state is a startState.
	 * @param isRetain if this state is a retainState.
	 */
	public KripkeState(String name, String labels, String varName, boolean isStart, boolean isRetain) {
		super(name, varName, isStart, isRetain);

		this.labels = new ArrayList<String>();
		
		String currentLabel = "";
		boolean insideBrac = false;
		for(int i = 0; i < labels.length(); i++) {
			char c = labels.charAt(i);
			
			if(c == '{') insideBrac = true;
			if(c == '}') insideBrac = false;
			
			if(c == ',' && !insideBrac) {
				this.labels.add(currentLabel);
				currentLabel = "";
			} else {
				currentLabel += c;
			}
		}
		
		if(!currentLabel.isEmpty()) this.labels.add(currentLabel);
	}
	
	/**
	 * Gets the labels this state satisfy.
	 * @return a copy of the labels this state satisfy.
	 */
	public String[] getStateLabel() {
		return labels.toArray(new String[labels.size()]);
	}
	
	/**
	 * Gets the labels this state satisfy.
	 * @return a copy of the labels this state satisfy.
	 */
	public Collection<String> getStateLabelAsCollection() {
		return new HashSet<String>(labels);
	}
	
	/**
	 * Adds a label.
	 * @param label the label to be added.
	 */
	public void addLabel(String label) {
		if(labels.contains(label))
			return;
					
		labels.add(label);
	}
	
	/**
	 * Gets a label by index.
	 * @param index the index of the required label.
	 * @return the label at the given index.
	 * @throws ArrayIndexOutOfBoundsException if index is outside the labels bound.
	 */
	public String getLabelAt(int index) {
		return labels.get(index);
	}
	
	/**
	 * Removes the given label.
	 * @param label the label to be removed.
	 */
	public void removeLabel(String label){
		labels.remove(label);
	}
	
	
	/**
	 * Returns a mapping between the processes and their respective labels.
	 * The labels within each process are sorted alphabetically. 
	 * 
	 * @param processes the processes participating in this state's structure.
	 * @return the required mapping.
	 */
	public HashMap<String, ArrayList<String>> mapProcessToLabel(String... processes) {	
		//process to labels.
		HashMap<String, ArrayList<String>> p2l = new HashMap<String, ArrayList<String>>();
		for(String p : processes) {
			p2l.put(p, new ArrayList<String>());
		}
		
		p2l.put("", new ArrayList<String>());
		
		for(String l : labels) {
			String cmp = l;
			if(l.contains(":="))
				cmp = l.substring(0, l.indexOf(":="));
			
			boolean noProcess = true;
			for(String p : processes) {
				if(cmp.endsWith(p)) {
					noProcess = false;
					p2l.get(p).add(l);
					break;
				}
			}
			
			if(noProcess) { //Shared Variable
				p2l.get("").add(l);
			}
		}
		
		for(String key : p2l.keySet()) {
			Collections.sort(p2l.get(key));
			
			//Remove duplicates.
			for(int i = 0; i < p2l.get(key).size() - 1; i++) {
				if(p2l.get(key).get(i) == null) p2l.get(key).remove(i);
				else if(p2l.get(key).get(i).equals(p2l.get(key).get(i + 1))) p2l.get(key).remove(i);
				else continue;
				
				i--;
			}
		}
		
		return p2l;
	}
}
