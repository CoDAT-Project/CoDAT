package eshmun.modelrepairer;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;

public class CNFFile {
	
	private PredicateFormula _cnfFormula;
	private FormulaStringCollection _variableList;
	
	
	public CNFFile(PredicateFormula _cnfFormula,
			FormulaStringCollection _variableList) {
		super();
		this._cnfFormula = _cnfFormula;
		this._variableList = _variableList;
	}

	public PredicateFormula get_cnfFormula() {
		return _cnfFormula;
	}

	public void set_cnfFormula(PredicateFormula _cnfFormula) {
		this._cnfFormula = _cnfFormula;
	}

	public FormulaStringCollection getVariableList() {
		return _variableList;
	}

	public void setVariableList(FormulaStringCollection _variableList) {
		this._variableList = _variableList;
	}

	/*
	 * Convert a CNF formula into a list of OR operators in order to make it
	 * easier for processing or display
	 * 
	 * @param PredicateFormula in CNF format
	 * 
	 * @return a list of OrOperator
	 */
	private List<OrOperator> ConvertToListofORs() {
		List<OrOperator> orOps = new ArrayList<OrOperator>();
		PriorityQueue<PredicateFormula> q = new PriorityQueue<PredicateFormula>();
		q.add(_cnfFormula);
		while (!q.isEmpty()) {
			PredicateFormula formula = (PredicateFormula) q.poll();
			if (formula.getClass().equals(AndOperator.class))
			{
				AndOperator andOp = (AndOperator) formula;
				q.add(andOp.getLeftChild());
				q.add(andOp.getRightChild());
			} else if (formula.getClass().equals(OrOperator.class)) {
				orOps.add((OrOperator) formula);
			}
		}
		return orOps;
	}
	
	private String convertOrToString(PredicateFormula formula)
	{
		StringBuilder builder = new StringBuilder();
		PriorityQueue<PredicateFormula> q = new PriorityQueue<PredicateFormula>();
		q.add(formula);
		while (!q.isEmpty()) {
			if (formula.getClass().equals(OrOperator.class))
			{
				OrOperator orOp = (OrOperator) formula;
				q.add(orOp.getLeftChild());
				q.add(orOp.getRightChild());
			}
			else if(formula.getClass().equals(NotOperator.class))
			{
				NotOperator notOp = (NotOperator) formula;
				builder.append("-" + notOp.getChild().toString().replaceAll("[\\D]", "") + " ");
			}
			else if(formula.getClass().equals(BooleanPredicate.class))
			{
				BooleanPredicate pred = (BooleanPredicate) formula;
				builder.append(pred.toString().replaceAll("[\\D]", "") + " ");
			}
		}
		builder.append("0");
		return builder.toString().trim();
	}

	public String ConvertToCNFFileFormat() {
		List<OrOperator> orOps= ConvertToListofORs();
		StringBuilder builder = new StringBuilder();
		for (OrOperator or : orOps) {
			builder.append(convertOrToString(or) + "\n");
		}
		return builder.toString();
	}


}
