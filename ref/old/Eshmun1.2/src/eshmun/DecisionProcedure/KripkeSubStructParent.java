package eshmun.DecisionProcedure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.modelchecker.FairnessType;

public class KripkeSubStructParent extends Kripke {

	private Map<String, Kripke> _subStructures;
	private Map<String, KripkeMergeState> _mergedStates;
	private Map<String, KripkeMergeState> _linkedStates;
	private Kripke _baseKripke;
	private List<String> statesNamesToBeLinked;
	
	public Map<String, Kripke> getSubStructures() {
		return _subStructures;
	}


	public Map<String, KripkeMergeState> getMergedStates() {
		return _mergedStates;
	}


	public KripkeSubStructParent(Kripke baseKripke, Kripke parentKripke) {
		_baseKripke = baseKripke;		
		name = parentKripke.getName();		
		statesList = parentKripke.getListOfStates() ; 
		states = parentKripke.getStatesMap();
		setLabeledExi(parentKripke.isLabeledExi());
		setLabeledEni(parentKripke.isLabeledEni());
		setParticipantsFairnessType(new HashMap<String, FairnessType>(parentKripke.getParticipantsFairnessType()));
		index = 0;
		_subStructures = new HashMap<String, Kripke>();
		_mergedStates = new HashMap<String, KripkeMergeState>();	
	}
	
	
	public Kripke GenerateSubStructure(List<String> statesToBeMerged, List<String> statesToBeLinked, String subName, String[] labels) throws CloneNotSupportedException
	{
		KripkeSubStructGenerator subGenerator = new KripkeSubStructGenerator(this, statesToBeMerged, statesToBeLinked, subName, labels); 
		Kripke subStructure = subGenerator.CreateSubStructure();
		statesNamesToBeLinked = statesToBeLinked;
		UpdateFullModel(statesToBeMerged, subName, labels);
		
		_subStructures.put(subName, subStructure);
		return subStructure;
	}
	
	@SuppressWarnings("unchecked")
	public void UpdateFullModel(List<String> subStructStates, String subStructureName, String[] substructureLabels) throws CloneNotSupportedException
	{		
		List<KripkeState> statesToBeMerged = new ArrayList<KripkeState>();
		for (String stateName : subStructStates) {
			statesToBeMerged.add(getState(stateName));
		}
		List<KripkeState> statesToBeLinked = new ArrayList<KripkeState>();		
		for (String stateName : statesNamesToBeLinked) {
			KripkeState state = getState(stateName);
			state.setPartOfMerge(true);
			statesToBeLinked.add(state);
		}
		KripkeMergeState state = new KripkeMergeState(this, subStructureName, statesToBeMerged, statesToBeLinked, substructureLabels);
		state.replaceMergedStates();
		_mergedStates.put(state.getName(), state);
	}
	
	public void DeleteSubstructure(Kripke subStructure)
	{
		KripkeMergeState state = _mergedStates.get(subStructure.getName());
		_subStructures.remove(state.getName());
		_mergedStates.remove(state.getName());
		msakrRemoveState(state.getName());
		List<KripkeState> statesList = state.getMergedStates();
		for (KripkeState kripkeState : statesList) {
			this.addState(kripkeState, _baseKripke.GetStateIndex(kripkeState.getName()));
		}
		Map<String,List<Transition>> outTrx = state.getOriginalOutgoingTransition();
		Map<String,List<Transition>> revTrx = state.getOriginalReverseTransition();
		for (KripkeState kripkeState : statesList) {
			List<Transition> orgoutTrx = outTrx.get(kripkeState.getName());
			List<Transition> orgrevTrx = revTrx.get(kripkeState.getName());
			for (Transition trx : orgoutTrx) {
				if(!this.containsName(trx.getEndState().getName()))
				{
					KripkeMergeState parent = GetParentMergedState(trx.getEndState());
					trx.setEndState(parent);					
				}
				kripkeState.addOutgoingTransition(trx);
			}
			for (Transition trx : orgrevTrx) {
				if(!this.containsName(trx.getEndState().getName()))
				{
					KripkeMergeState parent = GetParentMergedState(trx.getEndState());
					trx.setEndState(parent);					
				}
				if(!specialStateContain(statesList, trx.getEndState()))
				{
					Transition tr = new Transition(trx.getEndState(), trx.getStartState(), trx.getName(), trx.getTaskName());
					trx.getEndState().addOutgoingTransition(tr);
				}
			}
		}
		
		
		
	}
	
	public KripkeMergeState GetParentMergedState(KripkeState state)
	{
		for (KripkeMergeState parent : _mergedStates.values()) {
			if(parent.containsName(state.getName()))
				return parent;
		}
		return null;
	}
	
	protected boolean specialStateContain(List<KripkeState> mergedStates, KripkeState searchState)
	{
		for (KripkeState state : mergedStates) {
			if(state.getName().equals(searchState.getName()))
				return true;				
		}
		return false;
	}
	
	

}
