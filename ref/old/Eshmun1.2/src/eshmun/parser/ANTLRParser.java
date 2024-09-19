package eshmun.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import eshmun.expression.PredicateFormula;
import eshmun.parser.antlr.lexer.SpecLexer;
import eshmun.parser.antlr.parser.SpecParserAST;
import eshmun.parser.antlr.tree.SpecTreeParser;

public class ANTLRParser extends Parser{

	@Override
	public List<PredicateFormula> parse(List<String> inputPredicateFormulas) throws Exception {
		List<PredicateFormula> parsedPredicateFormula = new ArrayList<PredicateFormula>();
		for (String inputPredicateFormula : inputPredicateFormulas) {
			PredicateFormula pf = parse(inputPredicateFormula);
			parsedPredicateFormula.add(pf);
		}
		return parsedPredicateFormula;
	}
	
	public PredicateFormula parse(String predicateFormula) throws Exception{
		PredicateFormula pf = null;

		CharStream input = new ANTLRStringStream(predicateFormula);
		SpecLexer lexer = new SpecLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		SpecParserAST parser = new SpecParserAST(tokens);
		SpecParserAST.ctl_formula_return root = parser.ctl_formula();
		CommonTree ast = (CommonTree) root.getTree();
		if (ast != null)  {
			ast.sanityCheckParentAndChildIndexes();
			SpecTreeParser treeParser = new SpecTreeParser(new CommonTreeNodeStream(ast));
			pf = treeParser.ctl_formula();
			if (pf == null) {
				throw new RuntimeException("Could not parse: " + predicateFormula);
			}
		}
		
		return pf;
	}
}
