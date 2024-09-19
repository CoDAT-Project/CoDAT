parser grammar SpecParserAST;

options {
  language = Java;
  output = AST;
  tokenVocab = SpecLexer;
}

tokens {
  MPCTL_STATE_FORMULA;
  MPCTL_STATE_SUB_FORMULA;
  MPCTL_PATH_FORMULA;
  //CTL_STATE_FORMULA;
  //CTL_STATE_SUB_FORMULA;
  //CTL_PATH_FORMULA;
  ATOMIC_SUB_FORMULA;  
}

@header {
//options { backtrack = true; }
  package eshmun.parser.antlr.parser;
}


ctl_formula  :
  //: ctl_state_formula EOF!
  //| 
  mpctl_formula EOF!;

mpctl_formula 
  : MPCTL_CONNECTIVE LEFT_BRACKET mpctl_state_formula RIGHT_BRACKET -> ^(MPCTL_CONNECTIVE mpctl_state_formula);
  
mpctl_state_formula  
  : mpctl_state_sub_formula (PROPOSITIONAL_CONNECTIVE mpctl_state_sub_formula)* -> ^(MPCTL_STATE_FORMULA mpctl_state_sub_formula (PROPOSITIONAL_CONNECTIVE mpctl_state_sub_formula)*);

mpctl_state_sub_formula
  : mpctl_neg_sub_formula
  | CTL_BRANCH_OP LEFT_BRACKET mpctl_path_formula RIGHT_BRACKET -> ^(MPCTL_STATE_SUB_FORMULA CTL_BRANCH_OP mpctl_path_formula)
  | atomic_formula;

mpctl_path_formula 
  : CTL_PATH_OP (LEFT_BRACKET VAR RIGHT_BRACKET)* LEFT_PARANTHESIS mpctl_state_formula RIGHT_PARANTHESIS -> ^(MPCTL_PATH_FORMULA CTL_PATH_OP (VAR)* mpctl_state_formula)
  | LEFT_PARANTHESIS mpctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS mpctl_state_formula RIGHT_PARANTHESIS
  -> ^(MPCTL_PATH_FORMULA mpctl_state_formula CTL_PATH_CONNECTIVE mpctl_state_formula);

mpctl_neg_sub_formula 
  : (NOT_CONNECTIVE)? LEFT_PARANTHESIS mpctl_state_formula RIGHT_PARANTHESIS -> ^(MPCTL_STATE_SUB_FORMULA (NOT_CONNECTIVE)? mpctl_state_formula);

/*  
ctl_state_formula  
  : ctl_state_sub_formula (PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula)* -> ^(CTL_STATE_FORMULA ctl_state_sub_formula (PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula)*);
  
ctl_state_sub_formula
  : ctl_neg_sub_formula
  | CTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET -> ^(CTL_STATE_SUB_FORMULA CTL_BRANCH_OP ctl_path_formula)
  //| MPCTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET -> ^(CTL_STATE_SUB_FORMULA MPCTL_BRANCH_OP ctl_path_formula)
  | atomic_formula -> ^(CTL_STATE_SUB_FORMULA atomic_formula);



ctl_neg_sub_formula 
  : (NOT_CONNECTIVE)? LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^(CTL_STATE_SUB_FORMULA (NOT_CONNECTIVE)? ctl_state_formula);
  
ctl_path_formula 
  : CTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^(CTL_PATH_FORMULA CTL_PATH_OP ctl_state_formula)
  | LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS
  -> ^(CTL_PATH_FORMULA ctl_state_formula CTL_PATH_CONNECTIVE ctl_state_formula);
  */
atomic_formula
  : var_expression -> ^(ATOMIC_SUB_FORMULA var_expression)
  | BOOLEAN -> ^(ATOMIC_SUB_FORMULA BOOLEAN);
  
var_expression
  : var1=VAR COMPARISON_OPERATOR var2=VAR -> ^(COMPARISON_OPERATOR $var1 $var2) 
  | VAR COMPARISON_OPERATOR CONSTANT -> ^(COMPARISON_OPERATOR VAR CONSTANT)
  | VAR COMPARISON_OPERATOR STRING ->^(COMPARISON_OPERATOR VAR STRING)
  | VAR;  
