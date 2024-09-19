package eshmun.modelrepair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.TimeoutException;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.visitor.visitors.EvaluaterVisitor;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.modelrepair.sat.SAT4jSolver;
import eshmun.structures.AbstractState;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.MultiKripkeStructure;

/**
 * This class is a model repairer, it repairs a given model according to its specifications.
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class ModelRepairer {
	/**
	 * flags whether the model has been repaired. 
	 */
	private boolean repaired;
	
	/**
	 * The SAT Solver to use in solving.
	 */
	private SAT4jSolver solver;
	
	/**
	 * The repair formula not in CNF.
	 */
	private AndOperator notCNF;
	
	/**
	 * The repair formula.
	 */
	private AndOperator repairFormula;
	
	/**
	 * The model.
	 */
	private Repairable model;
	
	/**
	 * After repair, this will contain structure and repair related numbers,
	 * for example: number of literals in the formula, number of deleted states, etc..
	 * Order and meaning match that of StatsDialog.
	 */
	private int[] numbers;
	
	/**
	 * Flags whether or not to sync based on action names.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#actionSynchronizationFormula()
	 */
	private boolean actionSync;
	
	/**
	 * Flags whether or not to sync on process labels combined with from and to states labels.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#processSynchronizationFormula()
	 */
	private boolean processSync;
	
	/**
	 * Create a new model repairer that will repair model against specifications.
	 * @param model the model to repair.
	 * @param specifications the specifications to repair against.
	 * @param structureFormula an additional user defined structural formula that will be appended to the repair formula.
	 * @param actionSync flags whether or not to sync based on action names.
	 * @param processSync flags whether or not to sync on process labels combined with from and to states labels.
	 * @throws IOException if an error occurred while reading the expression by the solver.
	 * @throws ParseFormatException if the expression was not in CNF.
	 * @throws NotCNFException if the expression was not in CNF.
	 * @throws IllegalArgumentException if the specifications refer to labels not present in structure.
	 */
	public ModelRepairer(Repairable model, List<AbstractExpression> specifications, AbstractExpression structureFormula, boolean actionSync, boolean processSync) 
			throws NotCNFException, ParseFormatException, IOException, IllegalArgumentException {
		
		this.model = model;
		this.repaired = false;
		this.actionSync = actionSync;
		this.processSync = processSync;
		
		repairFormula = model.generateRepairFormula(specifications, this.actionSync, this.processSync); //IN CNF BY CONSTRUCTION
		
		if(structureFormula != null) {
			repairFormula.and(structureFormula.toCNF()); 
		}
		
		if(Eshmun.DEBUG_FLAG) {
			model.setCNF(false);
			notCNF = model.generateRepairFormula(specifications, this.actionSync, this.processSync);
			if(structureFormula != null) {
				notCNF.and(structureFormula);
			}
		}
			
		try {
			this.solver = new SAT4jSolver(repairFormula);
		} catch(ParseFormatException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	/**
	 * Repairs the Model.
	 * 
	 * @return true if model is repair-able, false otherwise.
	 * @throws TimeoutException if time run out while attempting to solve.
	 */
	public boolean modelRepair() throws TimeoutException {
		repaired = solver.isSatisfiable();
		return repaired;
	}
	
	/**
	 * Returns the deleted states and deleted transitions after repair, if the model was not
	 * repaired, it throws an exception.
	 * @return a list of strings, each is a name of a transition or state that was deleted.
	 * @throws IllegalStateException if the model was not repaired prior to calling this method.
 	 * @throws TimeoutException if time run out while attempting to solve. 
	 */
	public Collection<String> getNextDeletions() throws IllegalStateException, TimeoutException {
		if(!repaired) 
			throw new IllegalStateException();
		
		//If no new repair available then deletes will be null.
		Collection<String> deletes = solver.getNextRepair();
		
		if(Eshmun.DEBUG_FLAG) {
			EvaluaterVisitor ev = new EvaluaterVisitor(deletes, false);
			System.out.println("CNF Equivalent to NotCNF: "+ev.run(notCNF));
		}
		
		if(Eshmun.ECHO_STATS && deletes != null) {
			//Deletions Counts
			int deletedStateCount = 0;
			int deletedTransitionCount = 0;
			
			for(String var : deletes) {
				if(var.length() - var.replace(".", "").length() != 2)
					continue;
				
				else if(var.startsWith(State.STATE_PREFIX))
					deletedStateCount++;
				
				else if(var.startsWith(Transition.TRANSITION_PREFIX))
					deletedTransitionCount++;
			}
			
			//Process Model
			ArrayList<KripkeStructure> structures = new ArrayList<KripkeStructure>();
			if(model instanceof KripkeStructure) {
				structures.add((KripkeStructure) model);
			} else if(model instanceof MultiKripkeStructure) {
				structures = ((MultiKripkeStructure) model).getKripkeList();
			}
			
			//Total Count
			int totalNumberOfStates = 0;
			int totalNumberOfTransitions = 0;
			for(KripkeStructure kripke : structures) {
				Collection<AbstractState> states = kripke.getStates();
				
				for(AbstractState state : states) {
					totalNumberOfTransitions += state.getOutTransition().size();
				}
				
				totalNumberOfStates += states.size();
			}
			
			int numberOfClauses = solver.getNumberOfClauses();
			int numberOfLiterals = solver.getNumberOfLiterals();
			
			//Store Stats
			this.numbers = new int[] { totalNumberOfStates, totalNumberOfTransitions, 
										deletedStateCount, deletedTransitionCount, 
										numberOfClauses, numberOfLiterals };
		}
		
		return deletes;
	}
	
	/**
	 * @return The last repair benchmarks (or null if no last repair occurred).
	 */
	public int[] getNumbers() {
		return numbers;
	}
}
