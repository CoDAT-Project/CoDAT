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
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.EFOperator;
import eshmun.expression.propoperator.AndOperator;
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

public class TestNegation {

	public TestNegation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BooleanPredicate p = new BooleanPredicate("p");
		NotOperator notP = new NotOperator(p);
		EFOperator efP = new EFOperator(p);
		EFOperator efNotP = new EFOperator(notP);
		AndOperator and = new AndOperator(efP, efNotP);
		NotOperator not = new NotOperator(and);
		System.out.println(PredicateFormula.SendNegationToInside(not));
	}

}
