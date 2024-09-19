package eshmun.DecisionProcedure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AFOperator;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.EFOperator;
import eshmun.expression.ctl.EUOperator;

public class DPVertex {
	
	
	private String _name ;
	private List<DPVertexPf> _dPVformulas;
	private List<DPEdge> _edges;
	private List<DPEdge> _incomingEdges;
	private boolean _isStartState;
	
	
	
	private void setDPVformulas(List<DPVertexPf> dPVfs, boolean notexpanded)
	{
		if(notexpanded)
			createNewForNotExpanded(dPVfs);
		else
		{
			_dPVformulas = new ArrayList<DPVertexPf>();
			for (DPVertexPf dpVertexPf : dPVfs) {
				addDPVformula(dpVertexPf);
			}
		}
	}
	
	private void addDPVformula(PredicateFormula pf)
	{
		if(!doesFormulaExists(pf))
			_dPVformulas.add(new DPVertexPf(pf));
		
	}
	private void addDPVformula(DPVertexPf dpPf)
	{
		if(!doesFormulaExists(dpPf.getPf()))
			_dPVformulas.add(dpPf);
		
	}
	
	private boolean doesFormulaExists(PredicateFormula pf)
	{
		for (DPVertexPf DPVpf : _dPVformulas) {
			if(DPVpf.getPf().toString().trim().equals(pf.toString().trim()))
				return true;
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return "[_name=" + _name + ", _dPVformulas=" + _dPVformulas
				+ "]";
	}




	public List<DPEdge> getEdges() {
		return _edges;
	}




	public List<DPVertexPf> getDPVformulas() {
		return _dPVformulas;
	}




	public List<DPEdge> getIncomingEdges() {
		return _incomingEdges;
	}
	
	public void RemoveIncomingEdge(DPEdge edge)
	{
		_incomingEdges.remove(edge);
	}
	public void RemoveOutcomingEdge(DPEdge edge)
	{
		_edges.remove(edge);
	}

	public boolean hasChildren()
	{
		return _edges.size() > 0;
	}


	public String getName() {
		return _name;
	}


	

	public List<PredicateFormula> getFormulas() {
		List<PredicateFormula> _formulas = new ArrayList<PredicateFormula>();
		for (DPVertexPf dpvf : _dPVformulas) {
			_formulas.add(dpvf.getPf());
		}
		return _formulas;
	}
	
	public void addFormulasNotExpanded(List<PredicateFormula> _formulas) {
		for (PredicateFormula pf : _formulas) {
			addDPVformula(pf);
		}
	}
	public void addSingleFormulaNotExpanded(PredicateFormula pf) {
		addDPVformula(pf);
	}

	public void setFormulas(List<PredicateFormula> _formulas) {
		List<DPVertexPf> _dPVf = new ArrayList<DPVertexPf>();
		for (PredicateFormula pf : _formulas) {
			_dPVf.add(new DPVertexPf(pf));
		}
			
		setDPVformulas(_dPVf, false);
	}

	public void setDPVFormulas(List<DPVertexPf> _dPVf) {
		setDPVformulas(_dPVf, false);
	}

	public DPVertex(String name, List<PredicateFormula> formulas, boolean isStartState) {
		_name = name;
		setFormulas(formulas);
		_edges = new ArrayList<DPEdge>();
		_incomingEdges = new ArrayList<DPEdge>();
		_isStartState = isStartState;
	}
	
	public DPVertex(String name, List<DPVertexPf> _dPVf, boolean isStartState, boolean createNewForNotExpanded) {
		_name = name;
		setDPVformulas(_dPVf, createNewForNotExpanded);
		_edges = new ArrayList<DPEdge>();
		_incomingEdges = new ArrayList<DPEdge>();
		_isStartState = isStartState;
	}
	
	
	public void createNewForNotExpanded(List<DPVertexPf> _dPVf) {
		_dPVformulas = new ArrayList<DPVertexPf>();
		for (DPVertexPf dpvf : _dPVf) {
			if(dpvf.isExpanded())
				addDPVformula(dpvf);
			else
				addDPVformula(new DPVertexPf(dpvf.getPf()));
		}
	}
	
	public DPVertex(String name, PredicateFormula formula, boolean isStartState) {
		_name = name;
		List<PredicateFormula> formulas = new ArrayList<PredicateFormula>();
		formulas.add(formula);
		setFormulas(formulas);
		_edges = new ArrayList<DPEdge>();
		_incomingEdges = new ArrayList<DPEdge>();
		_isStartState = isStartState;
	}
	
	
	
	
	public boolean isStartState() {
		return _isStartState;
	}




	protected void AddEdge(DPEdge edge)
	{
		if(edge.getvFrom().equals(this)){
			_edges.add(edge);
		}
	}
	protected void AddIncomingEdge(DPEdge edge)
	{
		if(edge.getvTo().equals(this)){
			_incomingEdges.add(edge);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		List<PredicateFormula> _formulas = getFormulas();
		result = prime * result
				+ ((_formulas == null) ? 0 : _formulas.hashCode());
		return result;
	}
	
	
	
	public boolean ContainsFormula(PredicateFormula pred)
	{
		List<PredicateFormula> _formulas = getFormulas();
		for (PredicateFormula frml : _formulas) {
				if(frml.toString().equals(pred.toString()))
					return true;
		}
		return false;
	}

/*
 * 2 VERTEX ARE EQUAL IF THEY HAVE THE SAME SET OF FORMULAS
 * @see java.lang.Object#equals(java.lang.Object)
 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DPVertex other = (DPVertex) obj;
		List<PredicateFormula> _formulas = getFormulas();
		if (_formulas == null) {
			if (other.getFormulas() != null)
				return false;
		} else if (!_formulas.equals(other.getFormulas()))
			return false;
		return true;
	}
	
	
	public boolean isInconsistent()
	{
		List<PredicateFormula> _formulas = getFormulas();
		List<PredicateFormula> frmls = new ArrayList<PredicateFormula>(_formulas);
		boolean isFalse = false;
		for (PredicateFormula frml : frmls) {
			if(frml.toString().trim().equals("false"))
				return true;
			for (PredicateFormula innerfrml : frmls) {
				if(!frml.equals(innerfrml) && frml.isMyNegation(innerfrml))
					return true;				
			}
		}
		return false;
	}
	
	public EUOperator containsEU()
	{
		List<PredicateFormula> _formulas = getFormulas();
		for (PredicateFormula frml : _formulas) {
			if(frml instanceof EUOperator)
				return (EUOperator)frml;
		}
		return null;
	}
	
	public AUOperator containsAU()
	{
		List<PredicateFormula> _formulas = getFormulas();
		for (PredicateFormula frml : _formulas) {
			if(frml instanceof AUOperator)
				return (AUOperator)frml;
		}
		return null;
	}
	
	public List<EFOperator> containsEF()
	{
		List<EFOperator> efPs = new ArrayList<EFOperator>();
		List<PredicateFormula> _formulas = getFormulas();
		for (PredicateFormula frml : _formulas) {
			if(frml instanceof EFOperator)
				efPs.add((EFOperator)frml);
		}
		return efPs;
	}
	
	public AFOperator containsAF()
	{
		List<PredicateFormula> _formulas = getFormulas();
		for (PredicateFormula frml : _formulas) {
			if(frml instanceof AFOperator)
				return (AFOperator)frml;
		}
		return null;
	}


}
