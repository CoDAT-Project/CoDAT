package eshmun.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.AndOperator;
import eshmun.parser.antlr.lexer.SpecLexer;
import eshmun.parser.antlr.parser.SpecParserAST;
import eshmun.parser.antlr.tree.SpecTreeParser;

public class MainParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Lexeronly();
		//tokenParserOnly();
		//treeParserOnly();
		SpecTreeParser();
		/*try {
           CharStream input = new ANTLRStringStream("!a1 & b1 & c1");
           PredFormulaLexer lex = new PredFormulaLexer(input);
           CommonTokenStream tokens = new  CommonTokenStream(lex);

           //PredFormulaParser parser = new PredFormulaParser(tokens);
           //parser.pred_formula();
           
           PredFormulaParserAST parserAST = new PredFormulaParserAST(tokens);
           PredFormulaParserAST.pred_formula_return ret = parserAST.pred_formula();
           int x =0;
       }  catch(Throwable t) {
           System.out.println("exception: "+t);
           t.printStackTrace();
       }*/
	}
	/*
	public static void Lexeronly() {
		try {
			//CharStream input = new ANTLRFileStream(args[0]);
			CharStream input = new ANTLRStringStream("!a1 & b1 & c1");
	           
			PredFormulaLexer lexer = new PredFormulaLexer(input);
	        Token token;
	        while ((token = lexer.nextToken())!=Token.EOF_TOKEN) {
	        	System.out.println("Token: "+token.getText() + " - " + token.getType());
	        }
	    } catch(Throwable t) {
	        System.out.println("Exception: "+t);
	        t.printStackTrace();
	    }
	}
	
	public static void tokenParserOnly() {
		try {
			// CharStream input = new ANTLRFileStream(args[0]);
			CharStream input = new ANTLRStringStream("!A1 &C1 | C2 & !B1 &B2");
			PredFormulaLexer lexer = new PredFormulaLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			PredFormulaParser parser = new PredFormulaParser(tokens);
			parser.pred_formula();
		} catch (Throwable t) {
			System.out.println("exception: " + t);
			t.printStackTrace();
		}
	}
	*/
	
	public static void SpecTreeParser () {
		try {
			String inputString = "A1 | B2 & C3";
			//inputString = "A[X(A1|B2)] & E[(C3|x==2)V(A[G(X1)])]";
			//inputString = "A1 | B1";
			//inputString = "A[(T1) U (T3)]";
			//inputString = "AG(!(T1) | AFC1)";
			//inputString = "A[F(p)]";
			inputString = "A[G(!(T1) | A[F(C1)] )]";
			System.out.println(inputString);
			//inputString = "A[X(A1|B1)] & E[X(T1)] | A[(T1) U (T3)]";
			CharStream input = new ANTLRStringStream(inputString);
			//System.out.println("yalla");
			SpecLexer lexer = new SpecLexer(input);
			Token token;
			 //while ((token = lexer.nextToken())!=Token.EOF_TOKEN) {
	             //System.out.println("Token: "+token.getText());
	           //}
			//System.out.println("yalla");
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			SpecParserAST parser = new SpecParserAST(tokens);
			//System.out.println("yalla");
			SpecParserAST.ctl_formula_return root = parser.ctl_formula();
			System.out.println("yalla");
			//System.out.println("tree="+((Tree)root.tree).toStringTree());
			
			CommonTree ast = (CommonTree) root.getTree();
			if (ast == null) return; // line is empty

			SpecTreeParser treeParser = new SpecTreeParser(new CommonTreeNodeStream(ast));
			PredicateFormula pf = treeParser.ctl_formula();
			System.out.println(pf.getClass().toString());
			
	        // Use the tree parser to process the AST.
	        //PreFromulaTreeParser treeParser = new PreFromulaTreeParser(new CommonTreeNodeStream(ast));
	        //PredicateFormula pf = treeParser.pred_formula(); // start rule method
	        //PredicateFormula pf =  treeParser.getPredicateFormula();	
			/*CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree)root.tree);
	        XMLTreeParser walker = new XMLTreeParser(nodes);
	         walker.document();*/
			//System.out.println("predicate Formula: " + pf);
		} catch (Throwable t) {
			System.out.println("exception: " + t);
			t.printStackTrace();
		}
	}
	
	
	
	/*public static void treeParserOnly () {
		try {
			CharStream input = new ANTLRStringStream("A1 | B2 & C3");//"x1<=x2|!(B2|B1) & C3");//"A1 &C1 | C2 & !B1 &B2");
			PredFormulaLexer lexer = new PredFormulaLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			
			PredFormulaParserAST parser = new PredFormulaParserAST(tokens);
			PredFormulaParserAST.pred_formula_return root = parser.pred_formula();
			//System.out.println("tree="+((Tree)root.tree).toStringTree());
			System.out.println("yalla");
			CommonTree ast = (CommonTree) root.getTree();
			if (ast == null) return; // line is empty

	        // Use the tree parser to process the AST.
	        PreFromulaTreeParser treeParser = new PreFromulaTreeParser(new CommonTreeNodeStream(ast));
	        PredicateFormula pf = treeParser.pred_formula(); // start rule method
	        //PredicateFormula pf =  treeParser.getPredicateFormula();	
			CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree)root.tree);
	        XMLTreeParser walker = new XMLTreeParser(nodes);
	         walker.document();
	        System.out.println("predicate Formula: " + pf);
		} catch (Throwable t) {
			System.out.println("exception: " + t);
			t.printStackTrace();
		}
	}*/
}
