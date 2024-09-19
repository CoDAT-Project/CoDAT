// Generated from T7.g4 by ANTLR 4.4
package t7.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link T7Parser}.
 */
public interface T7Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link T7Parser#triplet}.
	 * @param ctx the parse tree
	 */
	void enterTriplet(@NotNull T7Parser.TripletContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#triplet}.
	 * @param ctx the parse tree
	 */
	void exitTriplet(@NotNull T7Parser.TripletContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#while_loop}.
	 * @param ctx the parse tree
	 */
	void enterWhile_loop(@NotNull T7Parser.While_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#while_loop}.
	 * @param ctx the parse tree
	 */
	void exitWhile_loop(@NotNull T7Parser.While_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#else_cond}.
	 * @param ctx the parse tree
	 */
	void enterElse_cond(@NotNull T7Parser.Else_condContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#else_cond}.
	 * @param ctx the parse tree
	 */
	void exitElse_cond(@NotNull T7Parser.Else_condContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_exp}.
	 * @param ctx the parse tree
	 */
	void enterBool_exp(@NotNull T7Parser.Bool_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_exp}.
	 * @param ctx the parse tree
	 */
	void exitBool_exp(@NotNull T7Parser.Bool_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#var_definition}.
	 * @param ctx the parse tree
	 */
	void enterVar_definition(@NotNull T7Parser.Var_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#var_definition}.
	 * @param ctx the parse tree
	 */
	void exitVar_definition(@NotNull T7Parser.Var_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(@NotNull T7Parser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(@NotNull T7Parser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#del}.
	 * @param ctx the parse tree
	 */
	void enterDel(@NotNull T7Parser.DelContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#del}.
	 * @param ctx the parse tree
	 */
	void exitDel(@NotNull T7Parser.DelContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#transition_def}.
	 * @param ctx the parse tree
	 */
	void enterTransition_def(@NotNull T7Parser.Transition_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#transition_def}.
	 * @param ctx the parse tree
	 */
	void exitTransition_def(@NotNull T7Parser.Transition_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_op}.
	 * @param ctx the parse tree
	 */
	void enterBool_op(@NotNull T7Parser.Bool_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_op}.
	 * @param ctx the parse tree
	 */
	void exitBool_op(@NotNull T7Parser.Bool_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#arith_exp}.
	 * @param ctx the parse tree
	 */
	void enterArith_exp(@NotNull T7Parser.Arith_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#arith_exp}.
	 * @param ctx the parse tree
	 */
	void exitArith_exp(@NotNull T7Parser.Arith_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#search}.
	 * @param ctx the parse tree
	 */
	void enterSearch(@NotNull T7Parser.SearchContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#search}.
	 * @param ctx the parse tree
	 */
	void exitSearch(@NotNull T7Parser.SearchContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#load}.
	 * @param ctx the parse tree
	 */
	void enterLoad(@NotNull T7Parser.LoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#load}.
	 * @param ctx the parse tree
	 */
	void exitLoad(@NotNull T7Parser.LoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#arith_exp3}.
	 * @param ctx the parse tree
	 */
	void enterArith_exp3(@NotNull T7Parser.Arith_exp3Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#arith_exp3}.
	 * @param ctx the parse tree
	 */
	void exitArith_exp3(@NotNull T7Parser.Arith_exp3Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#arith_exp2}.
	 * @param ctx the parse tree
	 */
	void enterArith_exp2(@NotNull T7Parser.Arith_exp2Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#arith_exp2}.
	 * @param ctx the parse tree
	 */
	void exitArith_exp2(@NotNull T7Parser.Arith_exp2Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#structure_definition}.
	 * @param ctx the parse tree
	 */
	void enterStructure_definition(@NotNull T7Parser.Structure_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#structure_definition}.
	 * @param ctx the parse tree
	 */
	void exitStructure_definition(@NotNull T7Parser.Structure_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull T7Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull T7Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#arith_exp1}.
	 * @param ctx the parse tree
	 */
	void enterArith_exp1(@NotNull T7Parser.Arith_exp1Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#arith_exp1}.
	 * @param ctx the parse tree
	 */
	void exitArith_exp1(@NotNull T7Parser.Arith_exp1Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#for_loop}.
	 * @param ctx the parse tree
	 */
	void enterFor_loop(@NotNull T7Parser.For_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#for_loop}.
	 * @param ctx the parse tree
	 */
	void exitFor_loop(@NotNull T7Parser.For_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#lines}.
	 * @param ctx the parse tree
	 */
	void enterLines(@NotNull T7Parser.LinesContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#lines}.
	 * @param ctx the parse tree
	 */
	void exitLines(@NotNull T7Parser.LinesContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#export}.
	 * @param ctx the parse tree
	 */
	void enterExport(@NotNull T7Parser.ExportContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#export}.
	 * @param ctx the parse tree
	 */
	void exitExport(@NotNull T7Parser.ExportContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull T7Parser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull T7Parser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#instantiation}.
	 * @param ctx the parse tree
	 */
	void enterInstantiation(@NotNull T7Parser.InstantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#instantiation}.
	 * @param ctx the parse tree
	 */
	void exitInstantiation(@NotNull T7Parser.InstantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#specifications_def}.
	 * @param ctx the parse tree
	 */
	void enterSpecifications_def(@NotNull T7Parser.Specifications_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#specifications_def}.
	 * @param ctx the parse tree
	 */
	void exitSpecifications_def(@NotNull T7Parser.Specifications_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#if_body}.
	 * @param ctx the parse tree
	 */
	void enterIf_body(@NotNull T7Parser.If_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#if_body}.
	 * @param ctx the parse tree
	 */
	void exitIf_body(@NotNull T7Parser.If_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(@NotNull T7Parser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(@NotNull T7Parser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_arith_exp}.
	 * @param ctx the parse tree
	 */
	void enterBool_arith_exp(@NotNull T7Parser.Bool_arith_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_arith_exp}.
	 * @param ctx the parse tree
	 */
	void exitBool_arith_exp(@NotNull T7Parser.Bool_arith_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#if_cond}.
	 * @param ctx the parse tree
	 */
	void enterIf_cond(@NotNull T7Parser.If_condContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#if_cond}.
	 * @param ctx the parse tree
	 */
	void exitIf_cond(@NotNull T7Parser.If_condContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#atomic_bool_exp}.
	 * @param ctx the parse tree
	 */
	void enterAtomic_bool_exp(@NotNull T7Parser.Atomic_bool_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#atomic_bool_exp}.
	 * @param ctx the parse tree
	 */
	void exitAtomic_bool_exp(@NotNull T7Parser.Atomic_bool_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#state_def}.
	 * @param ctx the parse tree
	 */
	void enterState_def(@NotNull T7Parser.State_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#state_def}.
	 * @param ctx the parse tree
	 */
	void exitState_def(@NotNull T7Parser.State_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#new_line}.
	 * @param ctx the parse tree
	 */
	void enterNew_line(@NotNull T7Parser.New_lineContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#new_line}.
	 * @param ctx the parse tree
	 */
	void exitNew_line(@NotNull T7Parser.New_lineContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#clear}.
	 * @param ctx the parse tree
	 */
	void enterClear(@NotNull T7Parser.ClearContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#clear}.
	 * @param ctx the parse tree
	 */
	void exitClear(@NotNull T7Parser.ClearContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(@NotNull T7Parser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(@NotNull T7Parser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#triplet_term}.
	 * @param ctx the parse tree
	 */
	void enterTriplet_term(@NotNull T7Parser.Triplet_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#triplet_term}.
	 * @param ctx the parse tree
	 */
	void exitTriplet_term(@NotNull T7Parser.Triplet_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#transitions_def}.
	 * @param ctx the parse tree
	 */
	void enterTransitions_def(@NotNull T7Parser.Transitions_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#transitions_def}.
	 * @param ctx the parse tree
	 */
	void exitTransitions_def(@NotNull T7Parser.Transitions_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(@NotNull T7Parser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(@NotNull T7Parser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#def_term}.
	 * @param ctx the parse tree
	 */
	void enterDef_term(@NotNull T7Parser.Def_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#def_term}.
	 * @param ctx the parse tree
	 */
	void exitDef_term(@NotNull T7Parser.Def_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#any}.
	 * @param ctx the parse tree
	 */
	void enterAny(@NotNull T7Parser.AnyContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#any}.
	 * @param ctx the parse tree
	 */
	void exitAny(@NotNull T7Parser.AnyContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(@NotNull T7Parser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(@NotNull T7Parser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#extract}.
	 * @param ctx the parse tree
	 */
	void enterExtract(@NotNull T7Parser.ExtractContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#extract}.
	 * @param ctx the parse tree
	 */
	void exitExtract(@NotNull T7Parser.ExtractContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_exp2}.
	 * @param ctx the parse tree
	 */
	void enterBool_exp2(@NotNull T7Parser.Bool_exp2Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_exp2}.
	 * @param ctx the parse tree
	 */
	void exitBool_exp2(@NotNull T7Parser.Bool_exp2Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#definition_body}.
	 * @param ctx the parse tree
	 */
	void enterDefinition_body(@NotNull T7Parser.Definition_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#definition_body}.
	 * @param ctx the parse tree
	 */
	void exitDefinition_body(@NotNull T7Parser.Definition_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_exp3}.
	 * @param ctx the parse tree
	 */
	void enterBool_exp3(@NotNull T7Parser.Bool_exp3Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_exp3}.
	 * @param ctx the parse tree
	 */
	void exitBool_exp3(@NotNull T7Parser.Bool_exp3Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#states_def}.
	 * @param ctx the parse tree
	 */
	void enterStates_def(@NotNull T7Parser.States_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#states_def}.
	 * @param ctx the parse tree
	 */
	void exitStates_def(@NotNull T7Parser.States_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#bool_exp1}.
	 * @param ctx the parse tree
	 */
	void enterBool_exp1(@NotNull T7Parser.Bool_exp1Context ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#bool_exp1}.
	 * @param ctx the parse tree
	 */
	void exitBool_exp1(@NotNull T7Parser.Bool_exp1Context ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(@NotNull T7Parser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(@NotNull T7Parser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#atomic_arith_exp}.
	 * @param ctx the parse tree
	 */
	void enterAtomic_arith_exp(@NotNull T7Parser.Atomic_arith_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#atomic_arith_exp}.
	 * @param ctx the parse tree
	 */
	void exitAtomic_arith_exp(@NotNull T7Parser.Atomic_arith_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(@NotNull T7Parser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(@NotNull T7Parser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link T7Parser#code_body}.
	 * @param ctx the parse tree
	 */
	void enterCode_body(@NotNull T7Parser.Code_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link T7Parser#code_body}.
	 * @param ctx the parse tree
	 */
	void exitCode_body(@NotNull T7Parser.Code_bodyContext ctx);
}