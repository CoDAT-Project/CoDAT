package eshmun.DecisionProcedure;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import eshmun.FullFrameEvents;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;


/*
 * this class creates substructures of a Kripke structUre and update 
 * the original structure to replace all the states of
 * the substructure as one state
 * the original structure is intact instead a cloned version is updated
 */
public class KripkeSubStructGenerator {

	
	private Kripke parentKripke;
	private List<String> subStructStates;
	private List<String> statesNamesToBeLinked;
	private String subStructureName;
	private boolean isSubCreated;
	private String[] substructureLabels;
	
	
	public KripkeSubStructGenerator(Kripke kripke, List<String> mergedStates, List<String> LinkedStates,  String subName, String[] labels) 
	{
		parentKripke = kripke;
		subStructStates = mergedStates;
		subStructureName = subName;
		substructureLabels = labels;
		statesNamesToBeLinked = LinkedStates;
		isSubCreated = false;
	}
	
	public Kripke CreateSubStructure() throws CloneNotSupportedException
	{
		Kripke subStructure = parentKripke.clone();
		subStructure.setName(subStructureName);
		List<KripkeState> statesList = parentKripke.getListOfStates();
		
		
		for(int i=0; i<statesList.size();i++)
		{
			String stateName = statesList.get(i).getName();
			if(!specialStateContain(subStructStates, stateName) && !specialStateContain(statesNamesToBeLinked, stateName))
			{
				subStructure.msakrRemoveState(stateName);
			}
		}
		
		//remove merged states as theyare not included in listed states to choose from
		int mergedStatesCount = parentKripke.getListOfStates().size();
		for(int i=0;i<mergedStatesCount;i++)
		{
			KripkeState state = parentKripke.getState(i);
			if(state instanceof KripkeMergeState && specialStateContain(subStructure.getListOfStates(), state))
			{
				subStructure.msakrRemoveState(state.getName());
			}
		}
		isSubCreated = true;
		return subStructure;
	}
	/*
	@SuppressWarnings("unchecked")
	public void UpdateFullModel() throws CloneNotSupportedException
	{		
		List<KripkeState> statesToBeMerged = new ArrayList<KripkeState>();
		List<KripkeState> statesToBeLinked = new ArrayList<KripkeState>();
		for (String stateName : subStructStates) {
			statesToBeMerged.add(parentKripke.getState(stateName));
		}
		
		for (String stateName : statesNamesToBeLinked) {
			KripkeState state = parentKripke.getState(stateName);
			state.setPartOfMerge(true);
			statesToBeLinked.add(state);
		}
		
		KripkeMergeState state = new KripkeMergeState(parentKripke, subStructureName, statesToBeMerged,  statesToBeLinked, substructureLabels);
		state.replaceMergedStates();
	}*/
	
	private boolean specialStateContain(List<String> mergedStates, String searchState)
	{
		for (String state : mergedStates) {
			if(state.equals(searchState))
				return true;				
		}
		return false;
	}
	
	private boolean specialStateContain(List<KripkeState> mergedStates, KripkeState searchState)
	{
		for (KripkeState state : mergedStates) {
			if(state.getName().equals(searchState.getName()))
				return true;				
		}
		return false;
	}

}
