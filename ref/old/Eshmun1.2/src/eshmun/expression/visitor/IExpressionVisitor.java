package eshmun.expression.visitor;

import java.util.List;

import eshmun.expression.PredicateFormula;
/*import eshmun.expression.atomic.EqualOperator;
import eshmun.expression.atomic.GreaterThanEqualOperator;
import eshmun.expression.atomic.GreaterThanOperator;
import eshmun.expression.atomic.LessThanEqualOperator;
import eshmun.expression.atomic.LessThanOperator;
import eshmun.expression.atomic.NotEqualOperator;*/
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AFOperator;
import eshmun.expression.ctl.AGOperator;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.AVOperator;
import eshmun.expression.ctl.AWOperator;
import eshmun.expression.ctl.AXOperator;
import eshmun.expression.ctl.EFOperator;
import eshmun.expression.ctl.EGOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.expression.ctl.EVOperator;
import eshmun.expression.ctl.EWOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.EquivalentOperator;
import eshmun.expression.propoperator.ImpliesOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;

public interface IExpressionVisitor {
	//CTL operators
	public Boolean visit(AUOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EUOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(AXOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EXOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(AGOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EGOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(AFOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EFOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(AVOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EVOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(AWOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EWOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(BooleanPredicate operator, Kripke kripke, KripkeState state) throws Exception;
	
	//propositional operators
	public Boolean visit(AndOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(OrOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(NotOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(ImpliesOperator operator, Kripke kripke, KripkeState state) throws Exception;
	public Boolean visit(EquivalentOperator operator, Kripke kripke, KripkeState state) throws Exception;
	
	//arithmetic operations modelChecking
	//public Boolean visit(EqualOperator operator, Kripke kripke, KripkeState state) throws Exception;
	//public Boolean visit(NotEqualOperator operator, Kripke kripke, KripkeState state) throws Exception;
	//public Boolean visit(GreaterThanEqualOperator operator, Kripke kripke, KripkeState state) throws Exception;
	//public Boolean visit(GreaterThanOperator operator, Kripke kripke, KripkeState state) throws Exception;
	//public Boolean visit(LessThanEqualOperator operator, Kripke kripke, KripkeState state) throws Exception;
	//public Boolean visit(LessThanOperator operator, Kripke kripke, KripkeState state) throws Exception;
	
	public List<PredicateFormula> fillSubFormulae(AUOperator operator);
	public List<PredicateFormula> fillSubFormulae(AWOperator operator);
	public List<PredicateFormula> fillSubFormulae(AXOperator operator);
}