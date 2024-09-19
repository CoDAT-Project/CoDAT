package eshmun.structures.kripke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.structures.Repairable;

/**
 * This class represents a Multi-Kripke Structure, a set of KripkeStructures each matched to two processes.
 * 
 * @author Ali Cherri, Kinan Dak Al Bab
 * @since 1.0
 */
public class MultiKripkeStructure implements Repairable {
	/**
	 * Flags whether the repair formula must be generated in cnf or not.
	 */
	public boolean cnf = true;
	
	/**
	 * The KripkeStructures inside this MultiStructure.
	 */
	private ArrayList<KripkeStructure> kripkeList;
	
	/**
	 * The Process pair related to each KripkeStructure, matched by index.
	 */
	private ArrayList<String[]> processes;

	/**
	 * Create a new empty MultiKripke Structure.
	 */
	public MultiKripkeStructure() {
		kripkeList = new ArrayList<KripkeStructure>();
		processes = new ArrayList<String[]>();
	}

	/**
	 * Adds the following KripkeStructure to this MultiStructure.
	 * 
	 * <p>The name of the KripkeStructure should be on the following form : <br>
	 *  &nbsp;&nbsp;&nbsp;&nbsp; {STRUCTURE_NAME}({PROCESS1}, {PROCESS2}) <br>
	 *  White spaces do not matter, the name is used to determine the pair process.
	 * </p>
	 * 
	 * @param kripke the KripkeStructure to be added.
	 */
	public void add(KripkeStructure kripke) {
		kripkeList.add(kripke);
		
		String name = kripke.getStructureName().trim();
		int startIndex = name.indexOf("(");
		int endIndex = name.indexOf(")");
		
		String[] processes = name.substring(startIndex + 1, endIndex).split(",");
		
		String p1 = processes[0].trim();
		String p2 = processes[1].trim();
		
		this.processes.add(new String[] {p1, p2});
	}

	/**
	 * Remove the given KripkeStructure and its Process Pair.
	 * @param kripke the KripkeStructure to be removed.
	 */
	public void remove(KripkeStructure kripke) {
		int index = kripkeList.indexOf(kripke);
		
		kripkeList.remove(kripke);
		processes.remove(index);
	}

	/**
	 * Gets all the KripkeStructures in this MultiStructure.
	 * @return a shallow copy of all the KripkeStructures.
	 */
	public ArrayList<KripkeStructure> getKripkeList() {
		return kripkeList = new ArrayList<KripkeStructure>(kripkeList);
	}

	/**
	 * Gets the process pairs for all KripkeStructures in this MultiStructure.
	 * @return a shallow copy of all the process pairs.
	 */
	public ArrayList<String[]> getProcesses() {
		return new ArrayList<String[]>(processes);
	}
	
	/**
	 * Gets all the participating processes.
	 * @return an array with all the processes. 
	 */
	public String[] getParticipatingProcesses() {
		ArrayList<String> result = new ArrayList<String>();
		
		for(String[] pair : processes) {
			if(!result.contains(pair[0]))
				result.add(pair[0]);
			
			if(!result.contains(pair[1]))
				result.add(pair[1]);
		}
		
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Gets the process pair related to the given KripkeStructure.
	 * @param kripke the Structure to get the process pair related to.
	 * @return the process pair related to the given structure.
	 */
	public String[] getPairProcess(KripkeStructure kripke) {
		int index = kripkeList.indexOf(kripke);
		return processes.get(index);
	}

	/**
	 * Construct the process-index based synchronization formula, this formula 
	 * synchronizes "matching" transitions for each processes over all the structures.
	 * 
	 * <p>This formula is deduced, the state labels are projected into one process alone
	 * (i.e. labels not belonging to that process is ignored). Synchronization will occur
	 * over transition that will take the state from the same set of labels to the same 
	 * resulting set of labels (projected to one process). This will be repeated for all
	 * processes.</p>
	 * 
	 * <p>The formula necessitates that for a specific process, if all the transitions
	 * taking that process from one state (labels projected into that process alone)
	 * to another were deleted in one of the pair structures, they must be also deleted 
	 * in all of the pair structures.</p>
	 * 
	 * @return the Structure Formula for this MultiKripkeStructure.
	 */
	public AndOperator processSynchronizationFormula() {
		HashMap<String, EquivalenceOperator> equiMap = new HashMap<String, EquivalenceOperator>();
		
		AndOperator andOp = new AndOperator();
	
		for(KripkeStructure s : kripkeList) {
			HashMap<String, ArrayList<KripkeTransition>> map = s.mapLabelsToTransitions();
			
			for(String key : map.keySet()) {
				if(key.isEmpty()) continue;
				
				if(equiMap.get(key) == null)
					equiMap.put(key, new EquivalenceOperator());
				
				OrOperator orOp = new OrOperator();
				for(KripkeTransition t : map.get(key)) {
					orOp.or(new BooleanVariable(t.getVarName()));
				}
				
				equiMap.get(key).equate(orOp);
			}
		}
		
		for(String key : equiMap.keySet()) {
			EquivalenceOperator equi = equiMap.get(key);
			if(equi.getChildren().length < 2) continue;
			
			andOp.and(equi);
		}
		
		return andOp;
	}
	
	/**
	 * Construct the action based synchronization formula, this formula synchronizes transition
	 * that are linked to the same action name over all pair structures.
	 * 
	 * <p>The formula necessitates that for a specific action name, if all the transitions
	 * that had that action name were deleted in one of the pair structures, they must be 
	 * also deleted in all of the structures. Otherwise, at least one such transition must
	 * remain in all pair structures.</p>
	 * 
	 * @return the Structure Formula for this MultiKripkeStructure.
	 */
	public AndOperator actionSynchronizationFormula() {
		HashMap<String, EquivalenceOperator> equiMap = new HashMap<String, EquivalenceOperator>();
		
		AndOperator andOp = new AndOperator();
	
		for(KripkeStructure s : kripkeList) {
			HashMap<String, ArrayList<KripkeTransition>> map = s.mapActionsToTransition();
			
			for(String key : map.keySet()) {
				if(key == null || key.isEmpty()) continue;
				
				if(equiMap.get(key) == null)
					equiMap.put(key, new EquivalenceOperator());
				
				OrOperator orOp = new OrOperator();
				for(KripkeTransition t : map.get(key)) {
					orOp.or(new BooleanVariable(t.getVarName()));
				}
				
				equiMap.get(key).equate(orOp);
			}
		}
		
		for(String key : equiMap.keySet()) {
			EquivalenceOperator equi = equiMap.get(key);
			if(equi.getChildren().length < 2) continue;
			
			andOp.and(equi);
		}
		
		return andOp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean modelCheck(List<AbstractExpression> specifications) {
		for(int i = 0; i < kripkeList.size(); i++) {
			KripkeStructure kripke = kripkeList.get(i);
			
			ArrayList<AbstractExpression> specs = new ArrayList<AbstractExpression>();
			specs.add(specifications.get(i));

			if(!kripke.modelCheck(specs)) 
				return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AndOperator generateRepairFormula(List<AbstractExpression> specifications, boolean actionSync, boolean processSync) throws NotCNFException {
		AndOperator andOp = new AndOperator();
		for(int i = 0; i < kripkeList.size(); i++) {
			KripkeStructure kripke = kripkeList.get(i);
			kripke.setCNF(cnf);			
			
			ArrayList<AbstractExpression> specs = new ArrayList<AbstractExpression>();
			specs.add(specifications.get(i));
			
			andOp.and(kripke.generateRepairFormula(specs, actionSync, processSync));
		}
		
		if(actionSync) {
			AbstractExpression synchFormula = actionSynchronizationFormula();
			System.out.println(synchFormula.toString(true));
			if(cnf) synchFormula = synchFormula.toCNF();
			andOp.and(synchFormula);
			
			System.out.println(synchFormula.toString(true));
		}
		
		
		if(processSync) {
			AbstractExpression synchFormula = processSynchronizationFormula();
			if(cnf) synchFormula = synchFormula.toCNF();
			andOp.and(synchFormula);
		}
		
		return andOp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCNF(boolean cnf) {
		this.cnf = cnf;
	}
}
