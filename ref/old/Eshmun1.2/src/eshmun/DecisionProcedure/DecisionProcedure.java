package eshmun.DecisionProcedure;

import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.*;
import eshmun.expression.propoperator.*;

public class DecisionProcedure {

	private PredicateFormula _formula;
	
	public DecisionProcedure(PredicateFormula formula) {
		_formula = convertImplyandEquivalent(formula);
		_formula = PredicateFormula.SendNegationToInside(_formula);
		System.out.println(_formula);
	}
	public DecisionProcedure() {
		
	}
	
	public static void main(String[] args) {
		BooleanPredicate tt = new BooleanPredicate(new BooleanConstant(true));
		BooleanPredicate ff = new BooleanPredicate(new BooleanConstant(false));
		BooleanPredicate q = new BooleanPredicate(new BooleanVariable("q"));
		BooleanPredicate p = new BooleanPredicate(new BooleanVariable("p"));
		
		AUOperator au1 = new AUOperator(tt, p);
		AUOperator au2 = new AUOperator(tt, q);
		EVOperator au3 = new EVOperator(ff, new OrOperator(new NotOperator(p), new NotOperator(q)));
		
		AndOperator an = new AndOperator(au1, new AndOperator(au2, au3));
		System.out.println(an);
		DecisionProcedure d= new DecisionProcedure(an);
		
		AFOperator af1 = new AFOperator(p);
		AFOperator af2 = new AFOperator(q);
		AFOperator af3 = new AFOperator(new AndOperator(p, q));
		
		ImpliesOperator im = new ImpliesOperator(new AndOperator(af1, af2), af3);
		
		//System.out.println(im);
		//DecisionProcedure d = new DecisionProcedure(new NotOperator(im));
		
		System.out.println(d.CheckStatisfiability() ? "Invalid" : "Valid");
	}
	
	public boolean CheckStatisfiability()
	{
		DPGraph graph =  generateTable();
		graph = optimizeTable(graph);
		boolean isSat = graph.hasStartState();
		return isSat;
	}
	
	public DPGraph generateTable()
	{
		DPGraph graph = new DPGraph();
		OrNode d0 = new OrNode("d0", _formula, true); 
		Stack<DPVertex> verticesToExplore = new Stack<DPVertex>();
		List<DPVertex> tempVertices = new ArrayList<DPVertex>();
		graph.AddOrNode(d0);
		int counter = 1;
		List<AndNode> generation1 = GenerateDBlocks(d0, counter);
		for (AndNode temp : generation1) {
			System.out.println(temp.toString());
		}
		counter += generation1.size(); 
		int count = 0;
		for (AndNode temp : generation1) {
			
			if(graph.ContainsAndNode(temp))
			{
				AndNode currentVertex = graph.AddAndNode(temp);
				graph.addEdge(d0, currentVertex);
				count++;
			}
			else
			{
				AndNode currentVertex = graph.AddAndNode(temp);
				graph.addEdge(d0, currentVertex);
				verticesToExplore.push(currentVertex);
			}
		}
		if(verticesToExplore.isEmpty())
			return graph;
		
		while(!verticesToExplore.isEmpty())
		{
			DPVertex current = verticesToExplore.pop();
			if(current instanceof OrNode && ((OrNode)current).isExpandable())
			{
				List<AndNode> generation = GenerateDBlocks((OrNode)current, counter);
				counter += generation.size(); 
				int count1 = 0;
				for (AndNode temp : generation) {
					if(graph.ContainsAndNode(temp))
					{
						AndNode currentVertex = graph.AddAndNode(temp);
						graph.addEdge(current, currentVertex);
						count1++;
					}
					else
					{
						AndNode currentVertex = graph.AddAndNode(temp);
						graph.addEdge(current, currentVertex);
						tempVertices.add(currentVertex);
						//System.out.println(temp.toString());
					}
				}
			}
			else if(current instanceof AndNode)
			{
				List<OrNode> generation = GenerateCTiles((AndNode)current, counter);
				counter += generation.size(); 
				int count2 = 0;
				for (OrNode temp : generation) {
					
					if(graph.ContainsOrNode(temp))
					{
						OrNode currentVertex = graph.AddOrNode(temp);
						graph.addEdge(current, currentVertex);
						count2++;
					}
					else
					{
						OrNode currentVertex = graph.AddOrNode(temp);
						graph.addEdge(current, currentVertex);
						tempVertices.add(currentVertex);
						//System.out.println(temp.toString());
						//if(counter > 40)
							//return null;
					}
					if(!temp.isExpandable())
					{
						AndNode currentVertex = graph.AddAndNode((AndNode)current);
						graph.addEdge(temp, currentVertex);
					}
				}
			}
			if(verticesToExplore.isEmpty())
			{
				verticesToExplore.addAll(tempVertices);
				tempVertices = new ArrayList<DPVertex>();
			}
		}
		
		
		return graph;
	}
	
	public DPGraph optimizeTable(DPGraph g)
	{
		boolean changed = true;
		while(changed)
		{
			//Delete vertices that contain p and !p
			boolean ch1 = DeleteP(g);
			boolean ch2 = DeleteOr(g);
			boolean ch3 = DeleteAND(g);
			boolean ch4 = DeleteEU(g);
			boolean ch5 = DeleteAU(g);
			boolean ch6 = DeleteEF(g);
			boolean ch7 = DeleteAF(g);
			if(!ch1 && !ch2 && !ch3 && !ch4 && !ch5 && !ch6 && !ch7)
				changed = false;				
		}
		return g;
	}
	
	//msakr:fix:31-03-2015: we should not remove expanded formulas 
	public List<AndNode> GenerateDBlocks(OrNode ornode, int counter)
	{
		//as each and node is basically a list of pf here we create a stack/list of lists
		//in other words it is a stack/list of the "and nodes" to be created
		//the number of these nodes is defined by the beta expansions according to the 
		//decision procedure.
		
		List<DPVertexPf> frmls;
		List<PredicateFormula> tempFrmls;
		//"And Nodes" to be created
		List<DPVertex> DBlocks = new ArrayList<DPVertex>();
		List<AndNode> andNodes = new ArrayList<AndNode>();
		
		//"And Nodes" to be fetched
		Stack<DPVertex> childrenStack = new Stack<DPVertex>();
		//we first clone the "Or Node" as "AndNode" and we start processing it
		childrenStack.add(new AndNode("c0", ornode.getFormulas(), false));
		int count = 1;//will be used for temporary naming
		//keep expanding(alpha and beta) until it is not possible 
		while(!childrenStack.isEmpty())
		{
			DPVertex stackPoped = childrenStack.pop();
			DPVertex returnToStack = stackPoped;
			boolean listHasChanged = false;
			boolean enterToStack = false;
			frmls = stackPoped.getDPVformulas();
			tempFrmls = new ArrayList<PredicateFormula>();
			for (DPVertexPf frml : frmls) {
				if(!frml.isExpanded() && isEligibleForAlphaExpansion(frml.getPf()))
				{					
					//because it is either alpha or beta, and it will be expanded
					//returnToStack.remove(frml);//msakr:fix:31-03-2015
					frml.setExpanded(true);//msakr:fix:31-03-2015
					
					//these returned alpha expansion should all be added to the generated and nodes
					tempFrmls.addAll(AlphaExpansion(frml.getPf()));
					
					listHasChanged = true;
					//Although we poped up a node from children stack, we might need to return it 
					//to the stack if it was not Beta expanded 
					enterToStack = true;
				}
			}
			if(listHasChanged)
				stackPoped.addFormulasNotExpanded(tempFrmls);
			//we are using stackPoped as it contains the expanded version of pfs
			frmls = stackPoped.getDPVformulas();
			for (DPVertexPf frml : frmls) {
				if(!frml.isExpanded() && isEligibleForBetaExpansion(frml.getPf())){
					listHasChanged = true;
					enterToStack = false;
					frml.setExpanded(true);//msakr:fix:31-03-2015
					List<PredicateFormula> betaTuple = BetaExpansion(frml.getPf());
					//first we add to the new and nodes all the pf obtained from the alpha expansion
					DPVertex child1 = new DPVertex("temp"+count, frmls, false, true);
					count++;
					DPVertex child2 = new DPVertex("temp"+count, frmls, false, true);
					count++;
					//in the below 2 lines we add to each created "and node" one child from the alpha expansion
					child1.addSingleFormulaNotExpanded(betaTuple.get(0));
					child2.addSingleFormulaNotExpanded(betaTuple.get(1));
					//child1.remove(frml);//msakr:fix:31-03-2015
					//child2.remove(frml);//msakr:fix:31-03-2015
					childrenStack.push(child1);
					childrenStack.push(child2);
					break;
				}
			}
			//if the node can not be expanded anymore
			if(!listHasChanged)
			{
				DBlocks.add(returnToStack);
			}
			//if the node was only "And expanded" return it back to the stack as it might be eligible
			//for more expansion
			if(enterToStack)
			{
				childrenStack.push(returnToStack);
			}
		}
		//convert the DBlocks into andnodes
		for (DPVertex vertex : DBlocks) {
			String nodeName = "C" + (++counter);
			AndNode andNode = new AndNode(nodeName, vertex.getFormulas(), false);
			andNodes.add(andNode);
		}
		return andNodes;
	}
	
	
	private List<OrNode> GenerateCTiles(AndNode andNode, int counter)
	{
		
		//as each or node is basically a list of pf here we create a list of lists
		//in other words it is a list of the "or nodes" to be created
		
		List<PredicateFormula> formulas = andNode.getFormulas();
		List<List<PredicateFormula>> cTiles = new ArrayList<List<PredicateFormula>>();
		List<OrNode> orNodes = new ArrayList<OrNode>();
		List<PredicateFormula> AXs = new ArrayList<PredicateFormula>();
		List<PredicateFormula> EXs = new ArrayList<PredicateFormula>();
		for (PredicateFormula formula : formulas) {
			if(formula instanceof AXOperator)
			{
				AXs.add(((AXOperator) formula).getChild());
			}
			if(formula instanceof EXOperator)
			{
				EXs.add(((EXOperator) formula).getChild());
			}
			
		}
		if(EXs.size() == 0 && AXs.size() == 0)
		{
			String nodeName = "D" + (++counter);
			OrNode temp = new OrNode(nodeName, formulas, false);
			temp.setExpandable(false);//msakr:fix:01-04-2015
			orNodes.add(temp);			
			return orNodes;
		}
		if(EXs.size() == 0 )
		{
			//BooleanPredicate trueChild = new BooleanPredicate(new BooleanConstant(true));			
			
			//EXs.add(exOperator);//msakr:fix:01-04-2015
			//EXs.add(trueChild);//msakr:fix:01-04-2015
			
			List<PredicateFormula> cTile = new ArrayList<PredicateFormula>(AXs);
			cTiles.add(cTile);
			
		}
		else
		{
			for (PredicateFormula exFormula : EXs) {
				//be careful that here the same set will be referenced in all the ctiles
				//for now we see no bad effects for such
				
				List<PredicateFormula> cTile = new ArrayList<PredicateFormula>(AXs);
				cTile.add(exFormula);
				cTiles.add(cTile);
			}
		}
		
		for (List<PredicateFormula> list : cTiles) {
			String nodeName = "D" + (++counter);
			OrNode temp = new OrNode(nodeName, list, false);
			orNodes.add(temp);
		}
		return orNodes;
	}
	
	//the returned formulas should exist in all the generated and nodes.
	private List<PredicateFormula> AlphaExpansion(PredicateFormula formula) 
	{
		List<PredicateFormula> formulas = new ArrayList<PredicateFormula>();
		
		//p&q
		if(formula instanceof AndOperator)
		{
			formulas.add(((AndOperator) formula).getLeftChild());
			formulas.add(((AndOperator) formula).getRightChild());
		}
		//A[f V g] = g & ( f | AXA[f V g])
		else if(formula instanceof AVOperator)
		{
			formulas.add(((AVOperator) formula).getRightChild());
			//AXA[f V g]
			AXOperator ax = new AXOperator(formula);			
			OrOperator or = new OrOperator(((AVOperator) formula).getLeftChild(), ax);
			formulas.add(or);			
		}
		//E[f V g] = g & ( f | EXE[f V g])
		else if(formula instanceof EVOperator)
		{
			formulas.add(((EVOperator) formula).getRightChild());
			//EXE[f V g]
			EXOperator ex = new EXOperator(formula);			
			OrOperator or = new OrOperator(((EVOperator) formula).getLeftChild(), ex);
			formulas.add(or);	
		}
		//AG(g) = g & AXAG(g)
		else if(formula instanceof AGOperator)
		{
			formulas.add(((AGOperator) formula).getChild());
			AXOperator ax = new AXOperator(formula);
			formulas.add(ax);
		}
		//EG(g) = g & EXEG(g)
		else if(formula instanceof EGOperator)
		{
			formulas.add(((EGOperator) formula).getChild());
			EXOperator ex = new EXOperator(formula);
			formulas.add(ex);
		}
		return formulas;
	}
	private List<PredicateFormula> BetaExpansion(PredicateFormula formula) 
	{
		List<PredicateFormula> formulas = new ArrayList<PredicateFormula>();
		
		// p|q
		if(formula instanceof OrOperator)
		{
			formulas.add(((OrOperator) formula).getLeftChild());
			formulas.add(((OrOperator) formula).getRightChild());
		}
		//A[f U g] = g | (f & AXA[f U g])
		else if(formula instanceof AUOperator)
		{
			formulas.add(((AUOperator) formula).getRightChild());
			//AXA[f U g]
			AXOperator ax = new AXOperator(formula);			
			AndOperator and = new AndOperator(((AUOperator) formula).getLeftChild(), ax);
			formulas.add(and);			
		}
		//E[f U g] = g | ( f & EXE[f U g])
		else if(formula instanceof EUOperator)
		{
			formulas.add(((EUOperator) formula).getRightChild());
			//EXE[f U g]
			EXOperator ex = new EXOperator(formula);			
			AndOperator and = new AndOperator(((EUOperator) formula).getLeftChild(), ex);
			formulas.add(and);	
		}
		//AF(g) = g | AXAF(g)
		else if(formula instanceof AFOperator)
		{
			formulas.add(((AFOperator) formula).getChild());
			AXOperator ax = new AXOperator(formula);
			formulas.add(ax);
		}
		//EF(g) = g | EXEF(g)
		else if(formula instanceof EFOperator)
		{
			formulas.add(((EFOperator) formula).getChild());
			EXOperator ex = new EXOperator(formula);
			formulas.add(ex);
		}
		return formulas;
	}
	
	private boolean isEligibleForAlphaExpansion(PredicateFormula formula)
	{
		if((formula instanceof AndOperator) || (formula instanceof AVOperator) ||
				(formula instanceof EVOperator) || (formula instanceof AGOperator) ||
				(formula instanceof EGOperator))
		{
			return true;
		}
			return false;
	}
	
	private boolean isEligibleForBetaExpansion(PredicateFormula formula)
	{
		if((formula instanceof OrOperator) || (formula instanceof AUOperator) ||
				(formula instanceof EUOperator) || (formula instanceof AFOperator) ||
				(formula instanceof EFOperator))
		{
			return true;
		}
		return false;
	}
	
	
	
	/*
	 * Compute power set
	 */
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	    	Set<T> newSet = new HashSet<T>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
	
	
	//Delete vertices that contain p and !p or those that contains false
	private boolean DeleteP(DPGraph graph)
	{
		 List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		 List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		 boolean changed = false;
		for (AndNode and : andNodes) {
			if(and.isInconsistent())
			{
				graph.DeleteNode(and);
				changed = true;
			}
		}
		for (OrNode or : orNodes) {
			if(or.isInconsistent())
			{
				graph.DeleteNode(or);
				changed = true;
			}
		}
		return changed;
	}
	
	
	/*
	 * Delete any OR-node D all of whose original AND-node sons Cl 
	 * are already deleted.
	 */
	private boolean DeleteOr(DPGraph graph)
	{
		List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		boolean changed = false;
		for (OrNode or : orNodes) {
			if(!or.hasChildren())
			{
				graph.DeleteNode(or);
				changed = true;
			}
		}
		return changed;
	}
	
	/*
	 * Delete any AND-node C one of whose original OR-node sons Di 
	 * has already been deleted.
	 */
	private boolean DeleteAND(DPGraph graph)
	{
		List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		boolean changed = false;
		for (AndNode and : andNodes) {
			if(and.isToBeDeleted())
			{
				graph.DeleteNode(and);
				changed = true;
			}
		}	
		return changed;
	}
	
	
	
	/*
	*Delete any node B such that E[fUg] in B and there does not exist 
	*some AND-node C� reachable from B such that g in C� and for all
	*AND-nodes C� on some path from C� back to B, f in C�.
	*/
	private boolean DeleteEU(DPGraph graph)
	{
		List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		boolean changed = false;
		for (AndNode and : andNodes) {
			EUOperator euOp = and.containsEU();
			if( euOp != null && !graph.CheckEU(and, euOp.getLeftChild(), euOp.getRightChild()))
			{
				graph.DeleteNode(and);
				changed = true;
			}
		}
		for (OrNode or : orNodes) {
			EUOperator euOp = or.containsEU();
			if( euOp != null && !graph.CheckEU(or, euOp.getLeftChild(), euOp.getRightChild()))
			{
				graph.DeleteNode(or);
				changed = true;
			}
		}
		return changed;
	}
	
	
	/*
	*Delete any node B such that A[fUg] in B and there does not exist 
	*a full subdag Q rooted at B such that for all nodes C� on the frontier 
	*of Q, g in C� and for all non-frontier AND-nodes C� in Q, f in C�.
	*/
	private boolean DeleteAU(DPGraph graph)
	{
		List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		boolean changed = false;
		for (AndNode and : andNodes) {
			AUOperator auOp = and.containsAU();
			if( auOp != null && !graph.CheckAU(and, auOp.getLeftChild(), auOp.getRightChild()))
			{
				graph.DeleteNode(and);
				changed = true;
			}
		}
		for (OrNode or : orNodes) {
			AUOperator auOp = or.containsAU();
			if( auOp != null && !graph.CheckAU(or, auOp.getLeftChild(), auOp.getRightChild()))
			{
				graph.DeleteNode(or);
				changed = true;
			}
		}
		return changed;
	}
	
	/*
	*Delete any node B such that EFg E B and there does not exist some
	*AND-node C� reachable from B such that g in C�.
	*/	
	private boolean DeleteEF(DPGraph graph)
	{
		List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		boolean changed = false;
		for (AndNode and : andNodes) {
			List<EFOperator> efOps = and.containsEF();
			for (EFOperator efOp : efOps) {			
				if(!graph.CheckEF(and, efOp.getChild()))
				{
					graph.DeleteNode(and);
					changed = true;
				}
			}
		}
		for (OrNode or : orNodes) {
			List<EFOperator> efOps = or.containsEF();
			for (EFOperator efOp : efOps) {	
				if(!graph.CheckEF(or, efOp.getChild()))
				{
					graph.DeleteNode(or);
					changed = true;
				}
			}
		}
		return changed;
	}
	
	
	
	/*
	 * DeleteAF: Delete any node B such that AFg E B and there does not exist a
	 * full subdag Q rooted at B such that for all nodes C� on the frontier
	 * of Q, g in C�.
	 */	
	private boolean DeleteAF(DPGraph graph)
	{
		List<AndNode> andNodes =  new ArrayList<AndNode>(graph.getAndNodes());
		List<OrNode> orNodes = new ArrayList<OrNode>(graph.getOrNodes());
		boolean changed = false;
		for (AndNode and : andNodes) {
			AFOperator afOp = and.containsAF();
			if( afOp != null && !graph.CheckAF(and, afOp.getChild()))
			{
				graph.DeleteNode(and);
				changed = true;
			}
		}
		for (OrNode or : orNodes) {
			AFOperator afOp = or.containsAF();
			if( afOp != null && !graph.CheckAF(or, afOp.getChild()))
			{
				graph.DeleteNode(or);
				changed = true;
			}
		}
		return changed;
	}
	
	private PredicateFormula convertImplyandEquivalent(PredicateFormula frml)
	{
		if(frml instanceof BooleanPredicate)
		{
			return frml;
		}
		if(frml instanceof NotOperator)
		{
			return new NotOperator(convertImplyandEquivalent(((NotOperator)frml).getChild()));
		}
		if(frml instanceof OrOperator)
		{
			OrOperator or = (OrOperator)frml;
			return new OrOperator(convertImplyandEquivalent(or.getLeftChild()), convertImplyandEquivalent(or.getRightChild()));
		}
		if(frml instanceof AndOperator)
		{
			AndOperator and = (AndOperator)frml;
			return new AndOperator(convertImplyandEquivalent(and.getLeftChild()), convertImplyandEquivalent(and.getRightChild()));
		}
		if(frml instanceof EquivalentOperator)
		{
			EquivalentOperator equivOp = (EquivalentOperator)frml;
			ImpliesOperator newLeftChild = new ImpliesOperator(equivOp.getLeftChild(), equivOp.getRightChild());
			ImpliesOperator newRightChild = new ImpliesOperator(equivOp.getRightChild(), equivOp.getLeftChild());
			return new AndOperator(convertImplyandEquivalent(newLeftChild), convertImplyandEquivalent(newRightChild));
		}
		if(frml instanceof ImpliesOperator)
		{
			ImpliesOperator implyOp = (ImpliesOperator)frml;
			NotOperator notChild = new NotOperator(implyOp.getLeftChild());
			return new OrOperator(convertImplyandEquivalent(notChild), convertImplyandEquivalent(implyOp.getRightChild()));
		}
		if(frml instanceof AFOperator)
		{
			AFOperator afOp = (AFOperator)frml;
			return new AFOperator(convertImplyandEquivalent(afOp.getChild()));
		}
		if(frml instanceof AGOperator)
		{
			
			AGOperator agOp = (AGOperator)frml;
			return new AGOperator(convertImplyandEquivalent(agOp.getChild()));
		}
		if(frml instanceof AUOperator)
		{
			AUOperator auOp = (AUOperator)frml;
			return new AUOperator(convertImplyandEquivalent(auOp.getLeftChild()), convertImplyandEquivalent(auOp.getRightChild()));
		}
		if(frml instanceof AVOperator)
		{
			AVOperator avOp = (AVOperator)frml;
			return new AVOperator(convertImplyandEquivalent(avOp.getLeftChild()), convertImplyandEquivalent(avOp.getRightChild()));
		}
		if(frml instanceof AXOperator)
		{
			AXOperator axOp = (AXOperator)frml;
			return new AXOperator(convertImplyandEquivalent(axOp.getChild()));
		}
		
		if(frml instanceof EFOperator)
		{
			EFOperator efOp = (EFOperator)frml;
			return new EFOperator(convertImplyandEquivalent(efOp.getChild()));
		}
		
		if(frml instanceof EGOperator)
		{
			EGOperator egOp = (EGOperator)frml;
			return new EGOperator(convertImplyandEquivalent(egOp.getChild()));
		}
		
		if(frml instanceof EUOperator)
		{
			EUOperator euOp = (EUOperator)frml;
			return new EUOperator(convertImplyandEquivalent(euOp.getLeftChild()), convertImplyandEquivalent(euOp.getRightChild()));
		}
		if(frml instanceof EVOperator)
		{
			return frml;
		}
		if(frml instanceof EXOperator)
		{
			EXOperator exOp = (EXOperator)frml;
			return new EXOperator(convertImplyandEquivalent(exOp.getChild()));
		}
		
		
		return null;
		
	}
	
	
}
