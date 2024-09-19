package eshmun.parser;

import java.util.List;

import eshmun.expression.PredicateFormula;

/**
 * parses a predicate formula String and generates the corresponding PredicateFormula object
 * 
 * @author Emile Chartouni
 *
 */
public abstract class Parser {
	
	public abstract List<PredicateFormula> parse(List<String> predicateFormula) throws Exception;
	public abstract PredicateFormula parse(String predicateFormula) throws Exception;
}
