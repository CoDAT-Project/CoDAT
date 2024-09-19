package eshmun.decisionprocedure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.visitors.ProcessIndicesListerVisitor;

/**
 * @author Mouhamad Sakr, Kinan Dak Al Bab
 * @since 0.1
 * 
 * The Decision Procedure as stated in Emersopn and Clarke 82, 
 * The Decision Procedure takes a general AbstractExpression, then checks if it
 * is satisfiable. 
 */
public class DecisionProcedure {
	/**
	 * The formula to check satisfiability for.
	 */
	private AbstractExpression formula;
	
	/**
	 * All the process indices used inside formula.
	 */
	private ArrayList<String> indices;
	
	/**
	 * Creates an instance of the decision procedure for the given formula.
	 * @param formula the formula to check satisfiability for.
	 */
	public DecisionProcedure(AbstractExpression formula) {
		this.formula = formula.toNNF();
		
		if(Eshmun.DEBUG_FLAG)
			System.out.println(this.formula.toString(true));
	}
	
	/**
	 * Checks if formula is satisfiable.
	 * @return true if the formula is satisfiable, false otherwise.
	 */
	public boolean checkStatisfiability() {
		Tableau tableau =  generateTable();
		tableau.deletionRules();
		
		return tableau.hasRoot();
	}
	
	/**
	 * Constructs the tableau.
	 * @return the tableau before applying any deletions.
	 */
	public Tableau getTableau() {
		Tableau tableau =  generateTable();
		return tableau;
	}
	
	
	/**
	 * Generate the tableau for the given formula, the tableau has potential models 
	 * for the formula embedded in it.
	 * @return The Tableau (Directed Graph).
	 */
	public Tableau generateTable() {
		ProcessIndicesListerVisitor v = new ProcessIndicesListerVisitor();
		formula.accept(v);
		indices = v.getIndices();
		if(indices.isEmpty())
			indices.add("none");
		
		Tableau tableau = new Tableau(formula); //The new tableau (Tableau)
		
		while(!tableau.getNoSuccessors().isEmpty()) {
			List<TableauNode> next = tableau.getNoSuccessors();
			for(int i = 0; i < next.size(); i++) {
				TableauNode current = next.get(i);
				
				List<TableauNode> generation;
				if(current instanceof AndNode) { //And Node
					generation = GenerateCTiles((AndNode) current);
				} else { //Or Node
					generation = GenerateDBlocks((OrNode) current);
				}
				
				for(TableauNode n : generation) {
					n = tableau.addNode(n);
					tableau.addEdge(current, n);
				}
			}
		}
		
		return tableau;
	}
		
	/**
	 * Generate the DBlocks using alpha and beta expansions as explained in
	 * Emerson and Clarke 82.
	 * 
	 * @param orNode the OrNode for which to generate the DBlocks.
	 * @return a list of AndNodes that are the successors of orNode.
	 */
	public List<TableauNode> GenerateDBlocks(OrNode orNode) {
		Stack<TableauNode> stack = new Stack<TableauNode>();
		List<TableauNode> blocks = new ArrayList<TableauNode>();

		stack.add(orNode); //And node or Or node doesn't matter.
		
		while(!stack.isEmpty()) {
			List<AbstractExpression> exps = stack.pop().getFormulae();
			boolean hasNoneElementary = false;
			
			for(int i = 0; i < exps.size(); i++) {
				AbstractExpression current = exps.get(i);
				if(isElementary(current)) //nothing to do
					continue;
				
				hasNoneElementary = true;
				
				//copy list without current
				ArrayList<AbstractExpression> tmp = new ArrayList<AbstractExpression>(exps.size() - 1);
				for(int j = 0; j < exps.size(); j++) {
					if(i == j) continue;
					tmp.add(exps.get(j));
				}
				
				if(isAlphaFormula(current)) { //apply alpha expansion					
					tmp.addAll(AlphaExpansion(current));
					stack.add(new AndNode(tmp));
				} else if(isBetaFormula(current)) {//apply beta expansion					
					List<AbstractExpression> beta = BetaExpansion(current);
					
					for(AbstractExpression b : beta) {
						ArrayList<AbstractExpression> n = new ArrayList<AbstractExpression>(tmp);
						n.add(b);
						stack.add(new AndNode(n));
					}
				} else {
					if(current.getType() == ExpressionType.AXModality) { //AX with index
						AndOperator and = new AndOperator();
						AbstractExpression child = ((AXModality) current).getChild();
						for(String index : indices) { //conjunction over all process
							and.and(new AXModality(child, index));
						}
						
						ArrayList<AbstractExpression> n = new ArrayList<AbstractExpression>(tmp);
						n.add(and);
						stack.add(new AndNode(n));
					} else if(current.getType() == ExpressionType.EXModality) { //EX with index
						OrOperator or = new OrOperator();
						AbstractExpression child = ((EXModality) current).getChild();
						for(String index : indices) { //disjunction over all processes
							or.or(new EXModality(child, index));
						}
						
						ArrayList<AbstractExpression> n = new ArrayList<AbstractExpression>(tmp);
						n.add(or);
						stack.add(new AndNode(n));
					} else { //panic : probably the formula is not in correct NNF.
						assert false;
					}
				}
				
				break;
			}
				
				
			if(!hasNoneElementary) {
				blocks.add(new AndNode(exps)); //no more expansion
			}
		}
		
		return blocks;
	}
	
	/**
	 * Generate the CTiles by expanding the AX, EX as explained in Emerson and Clarke 82.
	 * 
	 * <p>AX can be satisfied easily by not having any children, however, EX forces at least one child, 
	 * thus, that child must satisfy AX.</p>
	 * 
	 * <p>AX and EX can have process indices, in which case children resulting from an EX_i must satisfy 
	 * the AX_i conditions and the other AXs don't matter.</p>
	 * 
	 * @param andNode the AndNode to generate CTiles for.
	 * @return a list of OrNodes that are the CTiles.
	 */
	private List<TableauNode> GenerateCTiles(AndNode andNode) {
		List<AbstractExpression> formulae = andNode.getFormulae();
		List<TableauNode> tiles = new ArrayList<TableauNode>(); //stores the result		
		
		//the children of all AX, EX: map from process index to list of children
		HashMap<String, List<AbstractExpression>> axs = new HashMap<String, List<AbstractExpression>>();
		HashMap<String, List<AbstractExpression>> exs = new HashMap<String, List<AbstractExpression>>();
		
		//Fill the AX, EX map and lists
		for (AbstractExpression formula : formulae) {
			if(formula.getType() == ExpressionType.AXModality) {
				AXModality ax = (AXModality) formula;
				
				String index = ax.getIndexAsString();
				List<AbstractExpression> tmp = axs.get(index);
				if(tmp == null) {
					tmp = new ArrayList<AbstractExpression>();
					axs.put(index, tmp);
				}
				
				tmp.add(ax.getChild());
			} else if(formula.getType() == ExpressionType.EXModality) {
				EXModality ex = (EXModality) formula;
				
				String index = ex.getIndexAsString();
				List<AbstractExpression> tmp = exs.get(index);
				if(tmp == null) {
					tmp = new ArrayList<AbstractExpression>();
					exs.put(index, tmp);
				}
				
				tmp.add(ex.getChild());
			}
		}
		
		if(axs.isEmpty() && exs.isEmpty()) { //if both maps empty, return self in an OrNode
			tiles.add(new OrNode(formulae));	
			return tiles;
		}
		
		if(exs.isEmpty()) { //if EX is empty only, add an EX(true) for each process index found in the AX map
			for(String processIndex : axs.keySet()) {
				ArrayList<AbstractExpression> tmp = new ArrayList<AbstractExpression>();
				tmp.add(new EXModality(new BooleanLiteral(true), processIndex));
				
				exs.put(processIndex, tmp);
			}
		} 
		
		//All is good, both AX and EX are not empty
		for(String processIndex : exs.keySet()) { 
			for(AbstractExpression g : exs.get(processIndex)) {
				List<AbstractExpression> tmp = axs.get(processIndex);
				if(tmp == null) {
					tmp = new ArrayList<AbstractExpression>();
				} else {
					tmp = new ArrayList<AbstractExpression>(tmp);
				}
				
				tmp.add(g);
				tiles.add(new OrNode(tmp));
			}
		}
		
		return tiles;
	}
	
	/**
	 * Performs Alpha Expansion (as explained in Emerson, Clarke 82) on e,
	 * if e is not an Alpha Formula this method returns nulls.
	 * 
	 * @param e The formula to expand.
	 * @return The alpha Expansion as a list (conjunction).
	 */
	private List<AbstractExpression> AlphaExpansion(AbstractExpression e) {
		List<AbstractExpression> formulae = new ArrayList<AbstractExpression>();
		
		switch(e.getType()) {
			case AndOperator: 
				formulae = Arrays.asList(((AndOperator) e).getChildren());
				break;
				
			case AVModality:
				AVModality av = (AVModality) e;
				formulae.add(av.getRightChild());
				formulae.add(new OrOperator(av.getLeftChild(), new AXModality(av)));
				break;
			
			case EVModality:
				EVModality ev = (EVModality) e;
				formulae.add(ev.getRightChild());
				formulae.add(new OrOperator(ev.getLeftChild(), new EXModality(ev)));
				break;
				
			default:
				formulae = null;
				break;
		}

		return formulae;
	}
	
	/**
	 * Performs Beta Expansion (as explained in Emerson, Clarke 82) on e,
	 * if e is not an Beta Formula this method returns nulls.
	 * 
	 * @param e The formula to expand.
	 * @return The beta Expansion as a list (disjunction).
	 */
	private List<AbstractExpression> BetaExpansion(AbstractExpression e) {
		List<AbstractExpression> formulae = new ArrayList<AbstractExpression>();
		
		switch(e.getType()) {
			case OrOperator: 
				formulae = Arrays.asList(((OrOperator) e).getChildren());
				break;
				
			case AUModality:
				AUModality av = (AUModality) e;
				formulae.add(av.getRightChild());
				formulae.add(new AndOperator(av.getLeftChild(), new AXModality(av)));
				break;
			
			case EUModality:
				EUModality ev = (EUModality) e;
				formulae.add(ev.getRightChild());
				formulae.add(new AndOperator(ev.getLeftChild(), new EXModality(ev)));
				break;
				
			default:
				formulae = null;
				break;
		}

		return formulae;
	}
	
	/**
	 * Checks if formula is elementary.
	 * @param formula the formula to check.
	 * @return true if formula is elementary, false otherwise.
	 */
	private boolean isElementary(AbstractExpression formula) {
		return formula.isAtomic() || (formula.getType() == ExpressionType.AXModality
				&& ((AXModality) formula).isIndexed() ) || (formula.getType() == ExpressionType.EXModality
				&& ((EXModality) formula).isIndexed() );
	}
	
	/**
	 * Checks if formula is an alpha formula.
	 * @param formula the formula to check.
	 * @return true if formula is alpha, false otherwise.
	 */
	private boolean isAlphaFormula(AbstractExpression formula) {
		return formula.getType() == ExpressionType.AndOperator || formula.getType() == ExpressionType.AVModality
				|| formula.getType() == ExpressionType.EVModality;
	}
	
	/**
	 * Checks if formula is a beta formula.
	 * @param formula the formula to check.
	 * @return true if formula is beta, false otherwise.
	 */
	private boolean isBetaFormula(AbstractExpression formula) {
		return formula.getType() == ExpressionType.OrOperator || formula.getType() == ExpressionType.AUModality
				|| formula.getType() == ExpressionType.EUModality;
	}
}
