package eshmun.sat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JFileChooser;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.modelrepairer.FormulaStringCollection;

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
	private List<PredicateFormula> ConvertToListofORs() {
		List<PredicateFormula> Ops = new ArrayList<PredicateFormula>();
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
				Ops.add(formula);
			} else if (formula.getClass().equals(NotOperator.class)) {
				Ops.add( formula);
			} else if (formula.getClass().equals(BooleanPredicate.class)) {
				Ops.add( formula);
			}
		}
		return Ops;
	}
	
	private String convertToCNFString(PredicateFormula formula)
	{
		
		StringBuilder builder = new StringBuilder();
		PriorityQueue<PredicateFormula> q = new PriorityQueue<PredicateFormula>();
		q.add(formula);
		while (!q.isEmpty()) {
			formula = (PredicateFormula) q.poll();
			if (formula.getClass().equals(OrOperator.class))
			{
				OrOperator orOp = (OrOperator) formula;
				q.add(orOp.getLeftChild());
				q.add(orOp.getRightChild());
			}
			else if(formula.getClass().equals(NotOperator.class))
			{
				NotOperator notOp = (NotOperator) formula;
				if(notOp.getChild().getClass().equals(AndOperator.class))
				{
					AndOperator and = (AndOperator)notOp.getChild();
					builder.append("-" +  and.getLeftChild().toString().replaceAll("[\\D]", "") + " ");
					builder.append("-" +  and.getRightChild().toString().replaceAll("[\\D]", "") + " ");
				}
				else
				{
					
					builder.append("-" + notOp.getChild().toString().replaceAll("[\\D]", "") + " ");
				}
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

	public String ConvertToCNFFileFormat(List<PredicateFormula> ops) {
		
		StringBuilder builder = new StringBuilder();
		for (PredicateFormula f : ops) {
			builder.append(convertToCNFString(f) + ";");
		}
		return builder.toString();
	}
	
	public void SaveFile()
	{
		List<PredicateFormula> ops= ConvertToListofORs();
		
		String firstLine = "p cnf " + _variableList.getStringsList().size() + " " + ops.size();
		String allText = ConvertToCNFFileFormat(ops);
		
		try {
			File cnfFile = new File("dottool"+System.getProperty("file.separator") +"CNFFile.cnf");
			//File cnfFile = new File("CNFFile.cnf");
			if (cnfFile.exists())
				cnfFile.delete();
			cnfFile.createNewFile();
			FileWriter fw = new FileWriter(cnfFile);
			PrintWriter pw = new PrintWriter(fw);
			String[] stmts = allText.split(";");
			pw.println(firstLine);
			for (String str : stmts) {
				pw.println(str);
			}
			pw.flush();
			fw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
