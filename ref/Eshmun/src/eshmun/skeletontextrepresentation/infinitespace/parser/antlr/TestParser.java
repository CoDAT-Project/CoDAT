package eshmun.skeletontextrepresentation.infinitespace.parser.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ActionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ActionDefinitionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AdditiveoperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AndOrContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ArithOpContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AssignmentStatementContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AtomicPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BinaryFunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BinaryPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BlockContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BoolContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BoolLiteralContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BoundVariableContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ConjunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.DisjunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ElseBodyContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ExprContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.FormulaContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.FreeVariableContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.GlobalEffectContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.GlobalGuardContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IdentifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfBodyContext;

import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfStatementContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfboolPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfcondTermContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfconditionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfpredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IntialContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LeftIdentifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LeftPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LocalEffectContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LocalGuardContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LogicTermContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.MultiplicativeoperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.NameContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.NegatedContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.NegationContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.PredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ProcessContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ProcessnameContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ProgContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.QuantifiedFormulaContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.QuantifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RelOpContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RelationaloperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RighExprContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RightBoolContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SectionsContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SignContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SignedNumberContext;

import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SskipContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.StatementContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.StringContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.UnaryFunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.UnaryPLusContext;

public class TestParser implements FormulasAndStatementsListener {

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
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
	public void enterLogicTerm(LogicTermContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLogicTerm(LogicTermContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterQuantifiedFormula(QuantifiedFormulaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitQuantifiedFormula(QuantifiedFormulaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNegation(NegationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitNegation(NegationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNegated(NegatedContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitNegated(NegatedContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBoundVariable(BoundVariableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBoundVariable(BoundVariableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBool(BoolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBool(BoolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterString(StringContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitString(StringContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAssignmentStatement(AssignmentStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAssignmentStatement(AssignmentStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRelationaloperator(RelationaloperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRelationaloperator(RelationaloperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMultiplicativeoperator(MultiplicativeoperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMultiplicativeoperator(MultiplicativeoperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSign(SignContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSign(SignContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIfpredicate(IfpredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfpredicate(IfpredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBinaryPredicate(BinaryPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBinaryPredicate(BinaryPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterArithOp(ArithOpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitArithOp(ArithOpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAndOr(AndOrContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAndOr(AndOrContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPredicate(PredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPredicate(PredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterUnaryPLus(UnaryPLusContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitUnaryPLus(UnaryPLusContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConjunction(ConjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConjunction(ConjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSignedNumber(SignedNumberContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSignedNumber(SignedNumberContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExpr(ExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExpr(ExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAdditiveoperator(AdditiveoperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAdditiveoperator(AdditiveoperatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBinaryFunction(BinaryFunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBinaryFunction(BinaryFunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIdentifier(IdentifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIdentifier(IdentifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIfcondTerm(IfcondTermContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfcondTerm(IfcondTermContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterDisjunction(DisjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitDisjunction(DisjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIfcondition(IfconditionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfcondition(IfconditionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIfStatement(IfStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfStatement(IfStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterQuantifier(QuantifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitQuantifier(QuantifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRelOp(RelOpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRelOp(RelOpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAtomicPredicate(AtomicPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAtomicPredicate(AtomicPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFreeVariable(FreeVariableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFreeVariable(FreeVariableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterUnaryFunction(UnaryFunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitUnaryFunction(UnaryFunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFormula(FormulaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFormula(FormulaContext ctx) {
		// TODO Auto-generated method stub
		
	}

 

	@Override
	public void enterElseBody(ElseBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitElseBody(ElseBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIfBody(IfBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfBody(IfBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

 

	@Override
	public void enterIfboolPredicate(IfboolPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIfboolPredicate(IfboolPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLeftIdentifier(LeftIdentifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLeftIdentifier(LeftIdentifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRightBool(RightBoolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRightBool(RightBoolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLeftPredicate(LeftPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLeftPredicate(LeftPredicateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRighExpr(RighExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRighExpr(RighExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLocalEffect(LocalEffectContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLocalEffect(LocalEffectContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAction(ActionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAction(ActionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLocalGuard(LocalGuardContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLocalGuard(LocalGuardContext ctx) {
	 
		
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
	public void enterSections(SectionsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSections(SectionsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterName(NameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitName(NameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGlobalGuard(GlobalGuardContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGlobalGuard(GlobalGuardContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterProcess(ProcessContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitProcess(ProcessContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGlobalEffect(GlobalEffectContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGlobalEffect(GlobalEffectContext ctx) {
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
	public void enterIntial(IntialContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIntial(IntialContext ctx) {
		// TODO Auto-generated method stub
		
	}

 

	@Override
	public void enterBoolLiteral(BoolLiteralContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBoolLiteral(BoolLiteralContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterProcessname(ProcessnameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitProcessname(ProcessnameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSskip(SskipContext ctx) {
		 
	}

	@Override
	public void exitSskip(SskipContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
