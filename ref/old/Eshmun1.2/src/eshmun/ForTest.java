package eshmun;

import java.io.IOException;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.AFOperator;
import eshmun.expression.ctl.AGOperator;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.EFOperator;
import eshmun.expression.ctl.EGOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.expression.ctl.EVOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.ImpliesOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.modelrepairer.FormulaStringCollection;
import eshmun.parser.ANTLRParser;
import eshmun.parser.antlr.lexer.SpecLexer;
import eshmun.parser.antlr.parser.SpecParserAST;
import eshmun.sat.CNFFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.sat4j.minisat.*;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;
import java.util.*;
import java.io.*;
import eshmun.DecisionProcedure.*;

public class ForTest {
	
	public static void main(String[] args) throws IOException {
		
		//fALSE  not satisfiable  ok
		/*
		BooleanConstant bc = new BooleanConstant(false);
		BooleanPredicate formula = new BooleanPredicate(bc);
		*/
		
		//true  satisfiable ok
		/*
		BooleanConstant bc = new BooleanConstant(true);
		BooleanPredicate formula = new BooleanPredicate(bc);
		*/
		
		//true and false  not satisfiable ok
		/*
		BooleanConstant bc = new BooleanConstant(false);
		BooleanPredicate f = new BooleanPredicate(bc);
		BooleanConstant bc1 = new BooleanConstant(true);
		BooleanPredicate f1 = new BooleanPredicate(bc);
		AndOperator formula = new AndOperator(f, f1);
		*/
		
		//EFp & EF(!p) satisfiable  ok
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		NotOperator notP = new NotOperator(p);
		EFOperator efP = new EFOperator(p);
		EFOperator efNotP = new EFOperator(notP);
		AndOperator formula = new AndOperator(efP, efNotP);
		*/
		
		//--EF(p &!p) not satisfiable  ok
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		NotOperator notP = new NotOperator(p);
		AndOperator and = new AndOperator(p, notP);
		EFOperator formula = new EFOperator(and);
		*/
		
		//\neg p /\ AF(p): satisfiable    ok
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		NotOperator notP = new NotOperator(p);
		AFOperator afP = new AFOperator(p);
		AndOperator formula = new AndOperator(afP, notP);
		*/
		
		//AG(p) /\ EF(\neg p): not satisfiable     ok
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		NotOperator notP = new NotOperator(p);
		AGOperator agP = new AGOperator(p);
		EFOperator efNotP = new EFOperator(notP);
		AndOperator formula = new AndOperator(agP, efNotP);
		*/
		
		
		//A[p U q] /\ EG(\neg q): not satisfiable    ok
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		AUOperator au = new AUOperator(p,q);
		EGOperator eg = new EGOperator(notq);
		AndOperator formula = new AndOperator(au, eg);
		*/
		
		//AF(p /\ \neg p): not satisfiable   ok
		/*
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		AndOperator and = new AndOperator(q, notq);
		AFOperator formula = new AFOperator(and);
		*/
		
		//EF(p /\ \neg p): not satisfiable    ok
		/*
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		AndOperator and = new AndOperator(q, notq);
		EFOperator formula = new EFOperator(and);
		*/
		
		//EG(p) /\ EG(\neg p): not satsifiable   ok
		/*
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		EGOperator eg = new EGOperator(q);
		EGOperator eg2 = new EGOperator(notq);
		AndOperator formula = new AndOperator(eg, eg2);
		*/
		
		//EXEG(p) /\ EXEG(\neg p)
		///*
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		EGOperator eg = new EGOperator(q);
		EXOperator ex1 = new EXOperator(eg);
		EGOperator eg2 = new EGOperator(notq);
		EXOperator ex2 = new EXOperator(eg2);
		AndOperator formula = new AndOperator(ex1, ex2);
		//*/
		
		
		//EG(p) /\ AF(\neg p): not satisfiable
		/*
		BooleanPredicate q = new BooleanPredicate("q");
		NotOperator notq = new NotOperator(q);
		EGOperator eg = new EGOperator(q);
		AFOperator af = new AFOperator(notq);
		AndOperator formula = new AndOperator(eg, af);
		*/

		//E[p U q] /\ E[p V q]: satisfiable
		/*
		BooleanPredicate p = new BooleanPredicate("p");
		BooleanPredicate q = new BooleanPredicate("q");
		EUOperator eu = new EUOperator(p,q);
		EVOperator ev = new EVOperator(p,q);
		AndOperator formula = new AndOperator(eu, ev);
		*/
		
		//   f2 and f3 => f1
		
		//AG((A1 | A2) =>  AF(sc) ).......f1
		/*
		BooleanPredicate a1 = new BooleanPredicate("A1");
		BooleanPredicate a2 = new BooleanPredicate("A2");
		BooleanPredicate sc = new BooleanPredicate("Sc");
		BooleanPredicate ok1 = new BooleanPredicate("ok1");
		BooleanPredicate ok2 = new BooleanPredicate("ok2");
		OrOperator a1Ora2 = new OrOperator(a1, a2);		
		AFOperator afSc = new AFOperator(sc);
		ImpliesOperator f1Imply = new ImpliesOperator(a1Ora2, afSc);
		AGOperator f1 = new AGOperator(f1Imply);
		//AG(A1 => AF(ok1)) & AG(A2 => AF(ok2))...............f2
		AFOperator afOk1 = new AFOperator(ok1);
		AFOperator afOk2 = new AFOperator(ok2);
		ImpliesOperator a1ImplAfOk1 = new ImpliesOperator(a1,afOk1);
		AGOperator agA1ImplAfOk1 = new AGOperator(a1ImplAfOk1);
		ImpliesOperator a2ImplAfOk2 = new ImpliesOperator(a2,afOk2);
		AGOperator agA2ImplAfOk2 = new AGOperator(a2ImplAfOk2);
		AndOperator f2 = new AndOperator(agA1ImplAfOk1, agA2ImplAfOk2);
		//AG( (ok1 | ok2) => AF(sc) )............f3
		OrOperator orOk1Ok2 = new OrOperator(ok1, ok2);
		ImpliesOperator orOk1Ok2ImplyAFSc = new ImpliesOperator(orOk1Ok2, afSc);
		AGOperator f3 = new AGOperator(orOk1Ok2ImplyAFSc);
		
		AndOperator f2AndF3 = new AndOperator(f2, f3);
		ImpliesOperator f2AndF3ImplyF1 = new ImpliesOperator(f2AndF3, f1);
		// f2 anf f3 implies f1..........................
		PredicateFormula formula = new NotOperator(f2AndF3ImplyF1);
		*/
		
		
		List<PredicateFormula> list = new ArrayList<PredicateFormula>();
		list.add(formula);
		DecisionProcedure  proc = new DecisionProcedure(formula);
		DPGraph graph =  proc.generateTable();
		graph = proc.optimizeTable(graph);
		List<AndNode> dBlocks = graph.getAndNodes();
		List<OrNode> cTyles = graph.getOrNodes();
		boolean isSat = false;
		for (OrNode orNode : cTyles) {
			if(orNode.isStartState())
				isSat = true;
		}
		
		if(isSat)
			System.out.println("staisfiable");
		else			
			System.out.println("not staisfiable");
		//dBlocks = proc.GenerateDBlocks(list);
		for (AndNode andNode : dBlocks) {		
			System.out.println(andNode.getName());
			for (PredicateFormula predicateFormula : andNode.getFormulas()) {
				System.out.println(predicateFormula.toString());
			}
		
		}
		for (OrNode orNode : cTyles) {		
			System.out.println(orNode.getName());
			for (PredicateFormula predicateFormula : orNode.getFormulas()) {
				System.out.println(predicateFormula.toString());
			}
		
		}
		List<DPEdge> edges = graph.getEdges();
		for(DPEdge edge : edges)
		{
			System.out.println(edge.getvFrom().getName() + "-->" + edge.getvTo().getName());
		}
		if(isSat)
			System.out.println("staisfiable");
		else			
			System.out.println("not staisfiable");
		
	}

	/*public static void main(String[] args) throws IOException {
		
		//System.out.println(CheckSatisfiability());
		
		ANTLRParser parser = new ANTLRParser();
		PredicateFormula pf = null;
		try {
			
			CharStream input = new ANTLRStringStream("s=>t");
			SpecLexer lexer = new SpecLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			Dictionary tokenDictionary = getTokensDictionary("C:\\SpecLexer.tokens");
			int counter = 1;
			while(tokens.LA(counter) != -1)
			{
				String tokenName = (String)tokenDictionary.get(Integer.toString(tokens.LA(counter)));
				tokenName += "   Line number: " + tokens.LT(counter).getLine();
				System.out.print(tokens.LT(counter).getText() + ": ");
				System.out.println(tokenName);
				counter ++;
			}
			//pf = parser.parse("(!(x) | ( (!(y) | z) & (!(s) | t) ) )&( !( (!(y) | z) & (!(s) | t) ) | x)");
			//pf = parser.parse("x | ((y & z) | (s & t))");
			SpecParserAST parsert = new SpecParserAST(tokens);
			SpecParserAST.ctl_formula_return root = parsert.ctl_formula();
			pf = parser.parse("A[G(s|t)]");
			//pf = parser.parse("x | (y | z)");
			//pf = parser.parse("A[(p) U (q)]");
			//pf = parser.parse("A[(p&q) U (c&d)]");
			//pf = parser.parse("(A[(p&q) U (c&d)] | t)");
			//pf = parser.parse("(p&d)");
			List<PredicateFormula> list = new ArrayList<PredicateFormula>();
			pf.GetNotCTLSubFormulae(list, pf, false);
			KripkeState st = new KripkeState(null, "test", false);
			BooleanPredicate pred1 = new BooleanPredicate(new BooleanVariable("p"));
			BooleanPredicate pred2 = new BooleanPredicate(new BooleanVariable("q"));
			PredicateFormulaValuation predValu1 = new PredicateFormulaValuation(
					null, st, pred1, true);
			PredicateFormulaValuation predValu2 = new PredicateFormulaValuation(
					null, st, pred2, true);
			st.addPredicateFormulaValuation(predValu1);
			st.addPredicateFormulaValuation(predValu2);
			System.out.println(list.get(0).isSatisfiedBy(st));
			for (PredicateFormula predicateFormula : list) {
				System.out.println(predicateFormula.toString());
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//char c =  00B2;
		//System.out.println((char)c);
		//System.out.println("X\u00B2\u00B2");
		//System.out.println("\u2074");
		//System.in.read();
//		Random random = new  Random();
//		System.out.println(pf.toString());
//		PredicateFormula pf1 = pf.ConvertToCNF(pf, random );
//		System.out.println(pf1.toString());

//		List<PredicateFormula> list = pf.getSubFormulea();
//		for (PredicateFormula predicateFormula : list) {
//			System.out.println(predicateFormula.toString());
//		}
//		
		// TODO Auto-generated method stub

	}
	private static Dictionary getTokensDictionary(String filePath)  throws Exception 
	{
		Scanner scan = new Scanner(new File(filePath));
		Dictionary tokenDictionary = new Hashtable<String, String>();
		while(scan.hasNext())
            {
                String[] tokenclass = new String[2];
				tokenclass = (scan.nextLine()).split("=");
                tokenDictionary.put(tokenclass[1], tokenclass[0]);
            }
		return tokenDictionary;
	}
	
	private static List<OrOperator> ConvertToListofORs(PredicateFormula CNFFormula)
	{
		List<OrOperator> orOps = new ArrayList<OrOperator>();
		PriorityQueue<PredicateFormula> q = new PriorityQueue<PredicateFormula>();  
		q.add(CNFFormula);
		while(!q.isEmpty())
		{
			PredicateFormula formula = (PredicateFormula)q.poll();
			if(formula.getClass().equals(AndOperator.class))///AND/////AND/////AND/////AND/////AND/////AND//
			{
				AndOperator andOp = (AndOperator) formula;
				q.add(andOp.getLeftChild());
				PredicateFormula ff = andOp.getRightChild();
				q.add(ff);
			}
			else if(formula.getClass().equals(OrOperator.class))
			{
				orOps.add((OrOperator)formula);
			}
		}
		return orOps;
	}
	private static String CheckSatisfiability()
	{
		StringBuilder result = new StringBuilder();
		
		ISolver solver = SolverFactory.newDefault ();
		solver . setTimeout (3600); // 1 hour timeout
		Reader reader = new DimacsReader ( solver );
		
		try {
		IProblem problem = reader.parseInstance ("CNFFile.cnf");
		if ( problem.isSatisfiable ()) {
			result.append(" Satisfiable  ! \n");
			result.append(reader.decode(problem.model()));
			return result.toString();
		} else {
			result.append(" Unsatisfiable  !");
			return result.toString();
		}
		} catch ( FileNotFoundException e) {
			result.append(e.getMessage());
			return result.toString();
		} catch ( ParseFormatException e) {
			result.append(e.getMessage());
			return result.toString();
		} catch ( IOException e) {
			result.append(e.getMessage());
			return result.toString();
		} catch ( ContradictionException e) {
			result.append(" Unsatisfiable  ( trivial )!");
			 return result.toString();
		} catch ( TimeoutException e) {
			result.append(" Timeout ,  sorry !");
			return result.toString();
		}
	}*/

}
