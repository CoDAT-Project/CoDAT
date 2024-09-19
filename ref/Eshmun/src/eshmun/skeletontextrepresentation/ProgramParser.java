
package eshmun.skeletontextrepresentation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import eshmun.skeletontextrepresentation.effect.Effect;
import eshmun.skeletontextrepresentation.effect.GlobalEffect;
import eshmun.skeletontextrepresentation.effect.LocalEffect;
import eshmun.skeletontextrepresentation.grammar.ProgLexer;
import eshmun.skeletontextrepresentation.grammar.ProgListener;
import eshmun.skeletontextrepresentation.grammar.ProgParser;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ActionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ActionDefinitionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AddopContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AndGuardExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AndOpContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AssignmentSymbolContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AtomicGuardExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.AuxPropsContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.BoolOperatorContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.CtlspecContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.DesignatorContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.EffectContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.EquivalenceOpContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ExprContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.FactorContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.FaultActionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.FaultsContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.GlobalEffectContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.GlobalGuardContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.GuardContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.GuardExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ImplicationOpContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.IntialContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.LabelContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.LiteralContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.LocalEffectContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.LocalGuardContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.MulopContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.MultipleDesignatorContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NameContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotAndExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotAndOrExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotBoolExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotEqExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotImpExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotOpContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.NotOrExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.OrGuardExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.OrOpContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ParaGuardExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ParenthesizedExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.PredicateContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ProcessContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ProcessNameContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.ProgContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.SectionsContext;

import eshmun.skeletontextrepresentation.grammar.ProgParser.SharedvariablesContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.StatementContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.TempctlContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.TermContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.VardomainContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.VardomainassignmentContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.VariableContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.VariableExpressionContext;
import eshmun.skeletontextrepresentation.grammar.ProgParser.VariablenameContext;

import eshmun.skeletontextrepresentation.grammar.ProgParser.VariablevalueContext;
import eshmun.skeletontextrepresentation.guard.AndGuard;
import eshmun.skeletontextrepresentation.guard.Guard;
import eshmun.skeletontextrepresentation.guard.LabelGuard;
import eshmun.skeletontextrepresentation.guard.LiteralGuard;
import eshmun.skeletontextrepresentation.guard.NotGuard;
import eshmun.skeletontextrepresentation.guard.OrGuard;

/**
 * <p>
 * This class parses a Program imported as text representation.
 * </p>
 * 
 * <p>
 * The parser fills in all actions in ProgramFactory class static objects. For
 * each action, Guards and Effects are parsed to their respective
 * representations. Guards had a tree-like structure defined in
 * eshmun.skeletontextrepresentation.guard.Guard. Effects are represented in
 * eshmun.skeletontextrepresentation.effect.Effect.
 * </p>
 *
 * <p>
 * Details about the syntax:
 * </p>
 * 
 * <p>
 * A program defines: initial (condition for starting states), and a collection
 * of sections. Each section can either be a Process or a Fault. A Process has a
 * name and a collection of Actions.
 * </p>
 * 
 * <p>
 * The Action is defined with a name and 4 properties:
 * </p>
 * <ul>
 * <li>localGuard: Boolean formula over AP_i. No guards means True</li>
 * <li>globalGuard: Boolean formulas over AP - AP_i and shared variables. No
 * guards means True</li>
 * <li>localEffect: multiple assignments; LHS are props from AP_i , RHS are
 * general boolean expressions. Skip means do nothing</li>
 * <li>globalEffect: multiple assignments; LHS is a list of shared variables ,
 * RHS are expressions with values from domain of the shared variables. Skip
 * means do nothing</li>
 * </ul>
 * 
 * <p>
 * E.g.:
 * </p>
 * 
 * <pre>
 * 
action {  
	l_grd: N1 
	g_grd: T2
	l_eff: N1 ,T1 :=  ff ,tt
	g_eff: x := 2;
}
 * </pre>
 * 
 * <pre>
action {  
	l_grd: T1 
	g_grd: N2 | (T2 &amp; x=1) 
	l_eff: T1 ,C1 :=  ff ,tt
	g_eff: x :=  null;
}
 * </pre>
 * 
 * 
 * @author chukris
 *
 */
public class ProgramParser implements ProgListener {

	ProgParser parser;
	ProgLexer lexer;

	public ProgramParser(ProgParser parser, ProgLexer lexer) {
		this.parser = parser;
		this.lexer = lexer;
	}

	/**
	 * Stack is needed to build guard expressions for actions
	 */
	Stack<Guard> guardStack = new Stack<>();

	public int currentAction = 0;

	public boolean isInAction = false;

	public Effect currentEffect = new LocalEffect();

	@Override
	public void enterAction(ActionContext ctx) {
		// TODO Auto-generated method stub
		isInAction = true;
		ProgramHelper.currentAction.processNumber = processNumberAndName;
	}

	@Override
	public void exitAction(ActionContext ctx) {
		// TODO Auto-generated method stub
		ProgramHelper.newAction();
		isInAction = false;
	}

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		// System.out.println(arg0);
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterOrGuardExpression(OrGuardExpressionContext ctx) {

	}

	@Override
	public void exitOrGuardExpression(OrGuardExpressionContext ctx) {

	}

	@Override
	public void enterNotOrExpression(NotOrExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotOrExpression(NotOrExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterMultipleDesignator(MultipleDesignatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitMultipleDesignator(MultipleDesignatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterGlobalGuard(GlobalGuardContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitGlobalGuard(GlobalGuardContext ctx) {

		// Fills in the Global Guard for an action
		ProgramHelper.currentAction.gGuard = guardStack.pop();
		ProgramHelper.currentAction.gGuard.guardText = getParserRuleText(ctx);
		guardStack = new Stack<>();

	}

	@Override
	public void enterGuard(GuardContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitGuard(GuardContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterImplicationOp(ImplicationOpContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitImplicationOp(ImplicationOpContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterAddop(AddopContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitAddop(AddopContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterBoolOperator(BoolOperatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitBoolOperator(BoolOperatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterAtomicGuardExpression(AtomicGuardExpressionContext ctx) {

	}

	@Override
	public void exitAtomicGuardExpression(AtomicGuardExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterParaGuardExpression(ParaGuardExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitParaGuardExpression(ParaGuardExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterFaults(FaultsContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitFaults(FaultsContext ctx) {
		// TODO Auto-generated method stub

	}

	Boolean isVariableExpression = false;

	@Override
	public void enterVariableExpression(VariableExpressionContext ctx) {
		// TODO Auto-generated method stub
		isVariableExpression = true;
	}

	@Override
	public void exitVariableExpression(VariableExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	boolean isLiteral = false;

	@Override
	public void enterLiteral(LiteralContext ctx) {
		if (isRecordingGuard)
			isLiteral = true;
	}

	@Override
	public void exitLiteral(LiteralContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override

	public void enterOrOp(OrOpContext ctx) {
		// TODO Auto-generated method stub
		if (isRecordingGuard) {

			guardStack.push(null);
		}

	}

	@Override
	public void exitOrOp(OrOpContext ctx) {
		if (isRecordingGuard) {
			OrGuard orGuard = new OrGuard();

			LinkedList<Guard> ors = new LinkedList<>();

			Guard current = guardStack.pop();

			while (current != null) {
				ors.addFirst(current);
				current = guardStack.pop();
			}

			if (ors.size() > 1) {
				for (Guard child : ors)
					orGuard.orGuard(child);

				guardStack.push(orGuard);
			} else {
				guardStack.push(ors.getFirst());
			}
		}
	}

	@Override
	public void enterDesignator(DesignatorContext ctx) {

	}

	@Override
	public void exitDesignator(DesignatorContext ctx) {
		// TODO Auto-generated method stub
		
		if(isLocalEffect) {
			ProgramHelper.allProgramLabels.add(getParserRuleText(ctx));
		}

		currentEffect.pushVariable(getParserRuleText(ctx));
	}

	@Override
	public void enterAndOp(AndOpContext ctx) {
		// TODO Auto-generated method stub

		if (isRecordingGuard) {

			guardStack.push(null);
		}

	}

	@Override
	public void exitAndOp(AndOpContext ctx) {

		if (isRecordingGuard) {

			AndGuard andGuard = new AndGuard();

			LinkedList<Guard> ands = new LinkedList<>();

			Guard current = guardStack.pop();

			while (current != null) {
				ands.addFirst(current);
				current = guardStack.pop();
			}

			if (ands.size() > 1) {
				for (Guard child : ands)
					andGuard.andGuard(child);

				guardStack.push(andGuard);
			} else {
				guardStack.push(ands.getFirst());
			}
		}

	}

	boolean isParathesized = false;

	@Override
	public void enterParenthesizedExpression(ParenthesizedExpressionContext ctx) {
		// TODO Auto-generated method stub
		isParathesized = true;
	}

	@Override
	public void exitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
		// TODO Auto-generated method stub
		isParathesized = false;
	}

	@Override
	public void enterMulop(MulopContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitMulop(MulopContext ctx) {
		// TODO Auto-generated method stub

	}

	boolean isRecordingGuard = false;

	@Override
	public void enterGuardExpression(GuardExpressionContext ctx) {

		isRecordingGuard = true;

	}

	private String getParserRuleText(ParserRuleContext ctx) {
		String text;
		int a = ctx.start.getStartIndex();
		int b = ctx.stop.getStopIndex();
		Interval interval = new Interval(a, b);

		CharStream input = ctx.start.getInputStream();

		text = input.getText(interval);
		return text;
	}

	@Override
	public void exitGuardExpression(GuardExpressionContext ctx) {
		// TODO Auto-generated method stub

		isRecordingGuard = false;
	}

	@Override
	public void enterLocalEffect(LocalEffectContext ctx) {
		// TODO Auto-generated method stub

		isLocalEffect = true;
		currentEffect = new LocalEffect();

	}

	@Override
	public void exitLocalEffect(LocalEffectContext ctx) {
		// TODO Auto-generated method stub

		isLocalEffect = false;
		ProgramHelper.currentAction.lEffect = currentEffect;

	}

	@Override
	public void enterNotOp(NotOpContext ctx) {
		// TODO Auto-generated method stub
		if (isRecordingGuard) {
			//guardStack.push(null);
		}

	}

	@Override
	public void exitNotOp(NotOpContext ctx) {

		if (isRecordingGuard) {

			Guard current = guardStack.pop();
			if (current != null) {
				guardStack.push(new NotGuard(current));
			}

		}

	}

	@Override
	public void enterFaultAction(FaultActionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitFaultAction(FaultActionContext ctx) {
		// TODO Auto-generated method stub

	}

	boolean isLocalEffect = false;
	boolean isGloablEffect = false;

	@Override
	public void enterStatement(StatementContext ctx) {

	}

	@Override
	public void exitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub

	}

	boolean isLocalGuard = false;

	@Override
	public void enterLocalGuard(LocalGuardContext ctx) {
		// TODO Auto-generated method stub

		isLocalGuard = true;

	}

	@Override
	public void exitLocalGuard(LocalGuardContext ctx) {
		// TODO Auto-generated method stub
		isLocalGuard = false;

		ProgramHelper.currentAction.lGuard = guardStack.pop();
		ProgramHelper.currentAction.lGuard.guardText = getParserRuleText(ctx);
		guardStack = new Stack<>();
	}

	@Override
	public void enterNotImpExpression(NotImpExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotImpExpression(NotImpExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterExpr(ExprContext ctx) {
		currentEffect.pushValue(getParserRuleText(ctx));

	}

	@Override
	public void exitExpr(ExprContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterTerm(TermContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitTerm(TermContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterAndGuardExpression(AndGuardExpressionContext ctx) {

	}

	@Override
	public void exitAndGuardExpression(AndGuardExpressionContext ctx) {

	}

	@Override
	public void enterFactor(FactorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitFactor(FactorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterAuxProps(AuxPropsContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitAuxProps(AuxPropsContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterProcess(ProcessContext ctx) {
		// TODO Auto-generated method stub

		//ProgramHelper.numberOfProcesses++;

	}

	@Override
	public void exitProcess(ProcessContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterNotBoolExpression(NotBoolExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotBoolExpression(NotBoolExpressionContext ctx) {

		if (!getParserRuleText(ctx).contains("("))
			ProgramHelper.allProgramLabels.add(getParserRuleText(ctx));
		isVariableExpression = false;

	}

	@Override
	public void enterNotEqExpression(NotEqExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotEqExpression(NotEqExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterNotAndOrExpression(NotAndOrExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotAndOrExpression(NotAndOrExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterGlobalEffect(GlobalEffectContext ctx) {
		// TODO Auto-generated method stub
		isGloablEffect = true;
		currentEffect = new GlobalEffect();

	}

	@Override
	public void exitGlobalEffect(GlobalEffectContext ctx) {
		// TODO Auto-generated method stub
		isGloablEffect = false;
		ProgramHelper.currentAction.gEffect = currentEffect;
	}

	@Override
	public void enterActionDefinition(ActionDefinitionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitActionDefinition(ActionDefinitionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterProg(ProgContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitProg(ProgContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterSections(SectionsContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitSections(SectionsContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterNotAndExpression(NotAndExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitNotAndExpression(NotAndExpressionContext ctx) {
		// TODO Auto-generated method stub

	}

	boolean inInitial = false;

	@Override
	public void enterIntial(IntialContext ctx) {
		// TODO Auto-generated method stub
		inInitial = true;
	}

	@Override
	public void exitIntial(IntialContext ctx) {

		ProgramHelper.initialGuard = guardStack.pop();
		guardStack = new Stack<>();

		inInitial = false;
	}

	@Override
	public void enterEffect(EffectContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitEffect(EffectContext ctx) {
		// TODO Auto-generated method stub

	}
	
	String currentName = "";

	@Override
	public void enterName(NameContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitName(NameContext ctx) {
		// TODO Auto-generated method stub
		if (isInAction) {
			
			ProgramHelper.currentAction.name = getParserRuleText(ctx);
			ProgramHelper.currentAction.processNumber = processNumberAndName;
			
		}
	}

	@Override
	public void enterVariable(VariableContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitVariable(VariableContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterEquivalenceOp(EquivalenceOpContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitEquivalenceOp(EquivalenceOpContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterLabel(LabelContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitLabel(LabelContext ctx) {
		if (!isRecordingGuard)
			return;

		String labelString = getParserRuleText(ctx);

		if (isLiteral) {
			guardStack.push(new LiteralGuard(labelString));

		} else {
			guardStack.push(new LabelGuard(labelString));
		}
		isLiteral = false;

	}

	@Override
	public void enterAssignmentSymbol(AssignmentSymbolContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitAssignmentSymbol(AssignmentSymbolContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterVariablevalue(VariablevalueContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitVariablevalue(VariablevalueContext ctx) {

		HashSet<String> values = ProgramHelper.sharedVariables.get(currentVarName);
		values.add(getParserRuleText(ctx));
		ProgramHelper.sharedVariables.put(currentVarName, values);
	}

	@Override
	public void enterVardomainassignment(VardomainassignmentContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitVardomainassignment(VardomainassignmentContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterVariablename(VariablenameContext ctx) {
		// TODO Auto-generated method stub

	}

	String currentVarName = "";

	@Override
	public void exitVariablename(VariablenameContext ctx) {

		currentVarName = getParserRuleText(ctx);
		if (ProgramHelper.isAbstractView) { //Initialize  with null if abstract view
			ProgramHelper.sharedVariables.put(currentVarName, new HashSet<String>(Arrays.asList("null")));
		} else
			ProgramHelper.sharedVariables.put(currentVarName, new HashSet<String>());
	}

	@Override
	public void enterSharedvariables(SharedvariablesContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitSharedvariables(SharedvariablesContext ctx) {

	}

	@Override
	public void enterVardomain(VardomainContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitVardomain(VardomainContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterTempctl(TempctlContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTempctl(TempctlContext ctx) {
		// TODO Auto-generated method stub
		String x= getParserRuleText(ctx);
		ProgramHelper.ctlSpec  = x;
		
	}

	@Override
	public void enterCtlspec(CtlspecContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCtlspec(CtlspecContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPredicate(PredicateContext ctx) {
		String x= getParserRuleText(ctx);
		
	}

	@Override
	public void exitPredicate(PredicateContext ctx) {
		 
		System.out.println( getParserRuleText(ctx));
		
		
	}

	int processNumberAndName = 0;
	@Override
	public void enterProcessName(ProcessNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitProcessName(ProcessNameContext ctx) {
		int pNum = Integer.parseInt(getParserRuleText(ctx));
		processNumberAndName =pNum;
		ProgramHelper.numberOfProcesses.add(pNum);
		
	}

}
