package eshmun.modelrepairer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;






import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.*;
import eshmun.sat.SAT4jSolver;



/**
 * this class takes a kripke structure and a list of specification formulea
 * and does the repair of the kripke structure wrt the spec if any exist.
 * 
 *
 */
public class ModelRepairer {

	
	



	protected List<Transition> transitionsToRetain;
	protected List<KripkeState> statesToRetain;
	protected Kripke modelKripke;
	protected PredicateFormula specFormulae;
	protected FormulaStringCollection formulaStringList;
	protected boolean isRepaired;
	protected SAT4jSolver satSolver;
	List<FormulaString> TrxToDeleteFrmlStr;
	/**
	 * PRECONDITION: model must be Total
	 * creates the model repairer for a kripke structure
	 * @param kripke the structure to repair
	 * @param spec the specification to which the structure will be repaired
	 */
	public ModelRepairer(Kripke kripke, PredicateFormula spec) {		
		modelKripke = kripke;
		specFormulae = spec;
		transitionsToRetain = null;
		statesToRetain = null;
		isRepaired = false;
		formulaStringList = null;
		satSolver = null;
	}
	public ModelRepairer(Kripke kripke, PredicateFormula spec, List<Transition> trans, List<KripkeState> states) {		
		modelKripke = kripke;
		specFormulae = spec;
		transitionsToRetain = trans;
		statesToRetain = states;
		isRepaired = false;
		formulaStringList = null;
		satSolver = null;
	}
	
	
	
	public boolean repair(){
		
		//optimizer
		OptimizedFormulaeGenerator formulaGenerator = new OptimizedFormulaeGenerator(modelKripke, specFormulae);
		
		//generate repair formula
		PredicateFormula CNFFormula = formulaGenerator.ComputegeneratedFormula(transitionsToRetain, statesToRetain);
		
		//get variables
		formulaStringList = formulaGenerator.getVariableList();
		
		//print - debug
		PrintFormulaStringList(formulaStringList);
		
		//SAT on formula
		SAT4jSolver satSolver = new SAT4jSolver(CNFFormula, formulaStringList);
		boolean success = satSolver.isSatisfiable();
		if(success) //if success save the satisfying assignment 
		{
			isRepaired = true;
			TrxToDeleteFrmlStr = formulaStringList.GetByIndices(satSolver.getSolution());
		}
		return success;
		
	}
	
	public List<List<Transition>> getAllPossibleRepairs()
	{
		List<List<Transition>> allRepairs = new ArrayList<List<Transition>>();
		OptimizedFormulaeGenerator formulaGenerator = new OptimizedFormulaeGenerator(modelKripke, specFormulae);
		PredicateFormula CNFFormula = formulaGenerator.ComputegeneratedFormula(transitionsToRetain, statesToRetain);
		formulaStringList = formulaGenerator.getVariableList();
		PrintFormulaStringList(formulaStringList);
		SAT4jSolver satSolver = new SAT4jSolver(CNFFormula, formulaStringList);
		boolean success = satSolver.isSatisfiable();
		if(success)
		{
			isRepaired = true;
			List<String> allStrSolutions = satSolver.getAllSolutions();
			for (String solut : allStrSolutions) {
				List<FormulaString> temp = formulaStringList.GetByIndices(solut.split((" ")));
				allRepairs.add(GetTransitionsTobeDeleted(temp));
			}
		}
		return allRepairs;
	}
	
	private void PrintFormulaStringList(FormulaStringCollection formulaStringList)
	{
		for (FormulaString frmlStr : formulaStringList.getStringsList()) {
			String state1 =  (frmlStr.getState() != null) ? frmlStr.getState().getName() : " ";
			String state2 =  (frmlStr.getEndState() != null) ? frmlStr.getEndState().getName() : " ";
			String frml =  (frmlStr.getFormula() != null) ? frmlStr.getFormula().toString() : " ";
			/*System.out.println(
					"Variable: " + frmlStr.toString() +
					" Type: " + frmlStr.getEorX() 
					+ " State1: " + state1
					+ " State2: " + state2
					+ " frml: " + frml
					+ " script: " + frmlStr.getSuperScriptIndex()
					+ " " );*/
		}
	}
	
	
	public List<Transition> getTransitionsToDelete() {
		if(isRepaired)
		{
			List<Transition> deletedTrans = GetTransitionsTobeDeleted(TrxToDeleteFrmlStr);
			return deletedTrans;
		}
		return null;
	}
	public boolean isRepaired() {
		return isRepaired;
	}
	
	private List<Transition> GetTransitionsTobeDeleted(List<FormulaString> frmls)
	{
		List<Transition> deletedTrans = new ArrayList<Transition>();
		for (FormulaString frmlStr : frmls) {
			String EorX = frmlStr.getEorX().trim();
			KripkeState startState = frmlStr.getState();
			KripkeState endState = frmlStr.getEndState();
			if(EorX.equals("E") && startState!= null && endState != null && !frmlStr.getValuation())
			{				
				Collection<Transition> stateTransitions =  startState.getTransitions();
				for (Transition transition : stateTransitions) {
					if((transition.getEndState() == null && endState == null) || ( transition.getEndState() != null && endState != null && transition.getEndState().getName().trim().equals(endState.getName().trim())))
					{
						deletedTrans.add(transition);
						break;
					}
				}
			}
		}
		return deletedTrans;
		
	}
}
