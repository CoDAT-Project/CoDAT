package eshmun.gui.kripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.parser.LogicParser;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.structures.AbstractState;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeAbstractionManager;
import eshmun.structures.kripke.KripkeState;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.KripkeTransition;

/**
 * GraphPanel Container for a single Kripke structure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class KripkeGraphPanelContainer implements GraphPanelContainer {
	/**
	 * The instance of the application.
	 */
	private Eshmun eshmun;

	/**
	 * The component containing the graphPanel.
	 */
	private JScrollPane scrollPane;

	/**
	 * The GraphPanel.
	 */
	private GraphPanel graphPanel;

	/**
	 * Create a new Container for panel residing inside component scroll.
	 * 
	 * @param scroll the component containing the GraphPanel.
	 * @param panel the GraphPanel.
	 * @param eshmun the original enclosing eshmun frame.
	 */
	public KripkeGraphPanelContainer(JScrollPane scroll, GraphPanel panel, Eshmun eshmun) {
		scrollPane = scroll;
		graphPanel = panel;
		this.eshmun = eshmun;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getGraphPanelComponent() {
		return scrollPane;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphPanel getCurrentGraphPanel() {
		return graphPanel;
	}

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the save-able was not of type Kripke.
	 */
	@Override
	public void load(Saveable saveable) throws IllegalArgumentException {
		if(saveable.getStructureType() != StructureType.Kripke) {
			throw new IllegalArgumentException();
		}

		SaveObject obj = (SaveObject) saveable;
		graphPanel.load(obj);

		eshmun.setSpecificationFormula(obj.getSpecifications());		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SaveObject[] getSaveObjects() {
		return new SaveObject[] { graphPanel.getSaveObject(eshmun.getSpecificationFormula(), eshmun.getStructureFormula()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String stringRepresentation() {
		return graphPanel.getStructureName();
	}

	/**
	 * {@inheritDoc}
	 * @return StructureType.Kripke .
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.Kripke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String constructDefinition() {
		return graphPanel.constructDefinition(eshmun.getSpecificationFormula());
	}

	/**
	 * Constructs the string representation / definition of the contained structure.
	 * @param specifications the specifications for this structure.
	 * @return the string representation of the contained structure.
	 */
	public String constructDefinition(String specifications) {
		return graphPanel.constructDefinition(specifications);
	}

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the type is not Kripke.
	 */
	@Override
	public String loadDefinition(String definition, StructureType structureType) throws IllegalArgumentException {
		if(structureType != StructureType.Kripke)
			throw new IllegalArgumentException();

		Scanner scan = new Scanner(definition.trim());

		String states = "";
		String transitions = "";
		String specifications = ""; 

		String name = scan.nextLine();
		String tmp = scan.nextLine();

		boolean isState = false;
		boolean isTransition = false;
		boolean isSpecifications = false;

		while(scan.hasNextLine()) {
			if(tmp.equalsIgnoreCase("States:")) {
				isState = true; isTransition = false;
				isSpecifications = false;
			}

			tmp = scan.nextLine();

			if(tmp.equalsIgnoreCase("Transitions:")) {
				isState = false;
				isTransition = true;
				isSpecifications = false;

				continue;
			}

			if(tmp.equalsIgnoreCase("Specifications:")) {
				isSpecifications = true;
				isTransition = false; isState = false;

				continue;
			}

			if(isState) {
				states += tmp;
			} else if (isTransition) {
				transitions += tmp;
			} else if(isSpecifications) {
				specifications += tmp;
			}
		}

		scan.close();
		graphPanel.loadDefinition(name, states, transitions);

		return specifications;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addStructure(String name) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getStructureNames() {
		ArrayList<String> names = new ArrayList<>();
		names.add(graphPanel.getStructureName());
		return names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashSet<String> getAllActionNames() {
		return graphPanel.getActionNames();
	}

	private HashMap<String, String> subformulaRename;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KripkeStructure constructStructure() {
		subformulaRename = new HashMap<String, String>();
		int index = 1;

		String name = graphPanel.getStructureName();
		KripkeStructure kripke = new KripkeStructure(name);

		for(State s : graphPanel.copyStates()) { //Add the states
			if(s.isDashed()) continue;

			String labels = s.getLabels();
			if(eshmun.isAbstracted() && abstractedByFormula) {
				String tmp = "";
				for(String l : labels.split(",")) {
					if(l.trim().equals("")) continue;

					String t = subformulaRename.get(l.trim());
					if(t == null) {
						t = "F_RENAME_"+index;
						subformulaRename.put(l.trim(), t);
						index++;
					}

					tmp += t + ",";
				}

				if(tmp.endsWith(","))
					tmp = tmp.substring(0, tmp.length() - 1);

				labels = tmp;
			}

			KripkeState kState = new KripkeState(s.getName(), labels, s.getVariableName(name), s.getStart(), s.getRetain());
			kripke.add(kState);
		}

		for(Transition t : graphPanel.copyTransitions()) {
			if(t.isDashed()) continue;

			KripkeState sFrom = kripke.getState(t.getFrom().getName());
			KripkeState sTo = kripke.getState(t.getTo().getName());

			String[] prs = new String[] { t.getProcessName() };
			if(prs[0] == null) prs = null; // TODO Process names migration from transitions to KripkeTrans.
			else if(prs[0].isEmpty()) prs = null;

			KripkeTransition kTransition = new KripkeTransition(sFrom, sTo, t.getVariableName(name), prs, t.getActionNames(), t.getRetain());
			kripke.add(kTransition);
		}

		return kripke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Repairable constructCurrentStructure() {
		return this.constructStructure();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSpecifications() {
		String specs = eshmun.getSpecificationFormula();
		if(specs.trim().equals("") || specs.trim().equals("CTL Specifications"))
			specs = "true";

		ArrayList<String> result = new ArrayList<String>();
		result.add(applyRenameToSpec(specs));

		return result;
	}

	/**
	 * Does nothing, shown here for completeness.
	 */
	@Override
	public void setSpecifications(String specs) {	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String applyRenameToSpec(String specifications) {
		if(!eshmun.isAbstracted() || !abstractedByFormula || subformulaRename == null) {
			return specifications;			
		}

		for(String l : subformulaRename.keySet()) {
			String newL = subformulaRename.get(l);
			specifications = specifications.replace(l, newL);
		}

		return specifications;		
	}

	/**
	 * A collection of var names of the last deleted states and transitions.
	 */
	private Collection<String> lastDeletions;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyDeletions(Collection<String> deletions) {
		restore();

		lastDeletions = new LinkedList<String>(deletions);
		graphPanel.dash(deletions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyUnsatCore(Collection<String> unsatCore) {
		restore();

		graphPanel.dot(unsatCore);
	}

	@Override
	public void applyStateByState(Collection<String> stateByState) {
		graphPanel.stripe(stateByState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore() {
		graphPanel.restore();
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteDashed() {
		graphPanel.deleteDashed();
	}

	/**
	 * Stores the original structure before abstraction.
	 */
	private KripkeAbstractionManager abstractionManger;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByLabel(String specs, Collection<String> allVars) {
		abstractedByFormula = false;

		SaveObject oldKripke = graphPanel.getSaveObject(specs, eshmun.getStructureFormula());
		abstractionManger = constructStructure().abstractByLabel(allVars, oldKripke);

		graphPanel.assignKripke(abstractionManger.getAbstractKripke());
	}

	/**
	 * Flags whether the structure is currently abstracted by formula.
	 */
	public boolean abstractedByFormula = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByFormula(String specs) {
		abstractedByFormula = true;
		LogicParser parser = new LogicParser();
		AbstractExpression specifications = parser.parse(specs);

		SaveObject oldKripke = graphPanel.getSaveObject(specs, eshmun.getStructureFormula());
		abstractionManger = constructStructure().abstractBySubFormula(specifications, oldKripke);

		graphPanel.assignKripke(abstractionManger.getAbstractKripke());
	}

	//store the truthTable for the checked specifications
	private ArrayList<ArrayList<Integer>> truthTable;
	//store the values of the distinct abstraction classes along with the 
	//states that belong to every class
	private HashMap<Integer, Integer> classes;
	//store the number of distinct classes that was obtained 
	private ArrayList<Integer> classValues;
	//map of every class to the corresponding concrete states
	private HashMap <Integer, ArrayList <Integer>> abstractClasses;
	//map every abstract state to a set of concrete states
	private HashMap <Integer, Set <State>> abstractStructure;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByCTLFormulae(ArrayList<String> specifications) {
		//generating the abtraction classes
		generateAbstractClasses(specifications);
		//generating a representation of the abstract structure
		getAbstractStruture();
		//save the current kripke structure (concrete model)
		SaveObject oldKripke;
		if(abstractionManger == null)
			oldKripke = graphPanel.getSaveObject(eshmun.getSpecificationFormula(), eshmun.getStructureFormula());
		else
			oldKripke = abstractionManger.getOldKripke();
		//applying the abstraction to the model 
		for(int i = 0; i<abstractStructure.size(); i++) {
			abstractionManger = constructStructure().abstractByManualSelection(abstractStructure.get(i), oldKripke);
			graphPanel.assignKripke(abstractionManger.getAbstractKripke());
			graphPanel.repaint();
		}
		//displaying a success message with a map from the abstract states to the concrete states
		JOptionPane.showMessageDialog(null, abstractClasses.toString(),
				"Abstract States", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * generate the abstract structure
	 */
	private void getAbstractStruture() {
		abstractStructure = new HashMap<Integer, Set <State>>();
		for(int i = 0; i< abstractClasses.size(); i++) {
			Set <State> temp  = getClassStates(abstractClasses.get(i));
			abstractStructure.put(i, temp);
		}
		System.out.println("Done, Generating Abstract Structure.");
	}

	/**
	 * this method takes an integer representation of the states and return an actual set of the states
	 * that belong to 1 class
	 * @param states integer representation of the states
	 * @return return the states that belong to the same abstract class
	 */
	private Set<State> getClassStates(ArrayList <Integer> states) {
		
		Set <State> temp = new HashSet <State>();
		
		
	 
		List<State> structureStatesAsList =  getCurrentGraphPanel().getStates( );
		
		
		for(int i = 0; i < states.size(); i++) {
			
			
			temp.add(getCurrentGraphPanel().getStateByName(structureStatesAsList.get(states.get(i)).getName()));
		}
		return temp;
	}

	/**
	 * This method generate the abstraction classes based on some specifications
	 * @param specifications the set of CTL specifications that we want to do abstraction based on.
	 */
	private void generateAbstractClasses(ArrayList<String> specifications) {
		truthTable = Eshmun.eshmun.stateByStateModelCheck(specifications);
		System.out.println("Done. Truth Table Generated");
		classes = generateClasses(truthTable);
		System.out.println("Done. Classes Generated");
		classValues = getClasses(classes);
		System.out.println(classValues.toString());
		System.out.println("Done. ClassValues Generated");
		abstractClasses = groupStates(classValues, classes);
		System.out.println(abstractClasses.toString());
		System.out.println("Done. Abstract States Map Generated");
	}
	
	/**
	 * this function will group all the concrete states that belongs to the same abstract state together
	 * @param classValues a numeric value representing a specific class
	 * @param classes the set of all the classes
	 * @return HashMap representing the abstract structure
	 */
	private HashMap <Integer, ArrayList <Integer>> groupStates(ArrayList<Integer> classValues, HashMap<Integer, Integer> classes) {
		HashMap <Integer, ArrayList <Integer>> temp = new HashMap <Integer, ArrayList <Integer>>();
		for(int i = 0; i < classValues.size(); i++) {
			ArrayList<Integer> states = new ArrayList<Integer>();
			for(int j = 0; j < classes.size(); j++) {
				if(classValues.get(i) == classes.get(j)) {
					states.add(j);
				}
			}
			temp.put(i, states);
		}
		return temp;
	}

	/**
	 * this method return the total number of the abtraction classes
	 * @param classes integer mapping from every state to its class ID
	 * @return the number of states and their distinct values
	 */
	private ArrayList<Integer> getClasses(HashMap<Integer, Integer> classes){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i = 0; i < classes.size(); i++) {
			if(!temp.contains(classes.get(i))) {
				temp.add(classes.get(i));
			}
		}
		return temp;
	}

	/**
	 * use the truth table to map every state to a distinct abstraction class
	 * @param truthTable the table containing truth values for every state given some specification
	 * @return map every state to its given abstract class
	 */
	private HashMap<Integer, Integer> generateClasses(ArrayList<ArrayList<Integer>> truthTable) {
		
		HashMap<Integer, Integer> classes = new HashMap<Integer, Integer>(); 
	
		for(int i = 0; i < truthTable.get(0).size(); i++) {
			ArrayList <Integer> temp = getStateValues(truthTable, i);
			int classVal = evaluateClass(temp);
			classes.put(i, classVal);
		}
		System.out.println(classes.toString());
		return classes;
	}

	/**
	 * this method takes a state number and the truth table 
	 * and return the binary values corresponding to every state
	 * @param truthTable a table containing binary values 0 and 1
	 * @param state a given state
	 * @return the binary values corresponding to every state
	 */
	private ArrayList <Integer> getStateValues(ArrayList<ArrayList<Integer>> truthTable, int state){
		ArrayList <Integer> temp = new ArrayList<Integer>();
		for(int i = 0; i< truthTable.size(); i++) {
			temp.add(truthTable.get(i).get(state));
		}
		return temp;
	}

	/**
	 * this method generate specific abstract class IDs 
	 * @param binaryValues 0 and 1 values denoting true and false
	 * @return a specific class ID
	 */
	private int evaluateClass(ArrayList<Integer> binaryValues) {
		int sum = 0;
		for(int i = binaryValues.size() - 1; i >= 0; i--) {
			sum = sum + (int)(Math.pow(2, i)*binaryValues.get(binaryValues.size()-i-1));
		}
		return sum;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> concretize(String specs) {
		boolean tmp = abstractedByFormula;
		abstractedByFormula = false;

		LogicParser parser = new LogicParser();
		if(lastDeletions == null) return new ArrayList<String>();

		graphPanel.load(abstractionManger.getOldKripke(), true);

		Collection<String> deletions = abstractionManger.concretizeDeletions(lastDeletions);
		deletions = graphPanel.dash(deletions, true);

		AbstractExpression specifications = parser.parse(specs);
		ArrayList<AbstractExpression> specsList = new ArrayList<AbstractExpression>();
		specsList.add(specifications);

		if(!constructStructure().modelCheck(specsList)) {
			abstractedByFormula = tmp;
			return null;
		}

		abstractedByFormula = false;
		return deletions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		graphPanel.getUndoManager().undo();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register() {
		abstractedByFormula = false;
		graphPanel.getUndoManager().register();
		graphPanel.repaint();

		abstractionManger = null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetAbstraction() {
		if(abstractionManger == null) return;

		abstractedByFormula = false;
		graphPanel.load(abstractionManger.getOldKripke());
	}
}