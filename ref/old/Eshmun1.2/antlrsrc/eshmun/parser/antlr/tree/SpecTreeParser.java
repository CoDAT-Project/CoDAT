// $ANTLR 3.4 C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g 2013-07-20 12:15:11

  package eshmun.parser.antlr.tree;
  
  import eshmun.expression.*;
  import eshmun.expression.ctl.*;
  import eshmun.expression.atomic.arithmetic.*;
  import eshmun.expression.atomic.bool.*;
  import eshmun.expression.atomic.string.*;
  import eshmun.expression.propoperator.*;
  import eshmun.expression.atomic.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SpecTreeParser extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BOOLEAN", "CAPS_LETTER", "COMPARISON_OPERATOR", "CONSTANT", "CTL_BRANCH_OP", "CTL_PATH_CONNECTIVE", "CTL_PATH_OP", "DIGIT", "LEFT_BRACKET", "LEFT_PARANTHESIS", "LETTER", "MPCTL_CONNECTIVE", "NEGATIVE_SIGN", "NOTHING", "NOT_CONNECTIVE", "PROPOSITIONAL_CONNECTIVE", "RIGHT_BRACKET", "RIGHT_PARANTHESIS", "SMALL_LETTER", "STRING", "VAR", "WS", "ATOMIC_SUB_FORMULA", "CTL_PATH_FORMULA", "CTL_STATE_FORMULA", "CTL_STATE_SUB_FORMULA"
    };

    public static final int EOF=-1;
    public static final int BOOLEAN=4;
    public static final int CAPS_LETTER=5;
    public static final int COMPARISON_OPERATOR=6;
    public static final int CONSTANT=7;
    public static final int CTL_BRANCH_OP=8;
    public static final int CTL_PATH_CONNECTIVE=9;
    public static final int CTL_PATH_OP=10;
    public static final int DIGIT=11;
    public static final int LEFT_BRACKET=12;
    public static final int LEFT_PARANTHESIS=13;
    public static final int LETTER=14;
    public static final int MPCTL_CONNECTIVE=15;
    public static final int NEGATIVE_SIGN=16;
    public static final int NOTHING=17;
    public static final int NOT_CONNECTIVE=18;
    public static final int PROPOSITIONAL_CONNECTIVE=19;
    public static final int RIGHT_BRACKET=20;
    public static final int RIGHT_PARANTHESIS=21;
    public static final int SMALL_LETTER=22;
    public static final int STRING=23;
    public static final int VAR=24;
    public static final int WS=25;
    public static final int ATOMIC_SUB_FORMULA=26;
    public static final int CTL_PATH_FORMULA=27;
    public static final int CTL_STATE_FORMULA=28;
    public static final int CTL_STATE_SUB_FORMULA=29;

    // delegates
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public SpecTreeParser(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public SpecTreeParser(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return SpecTreeParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g"; }


     /* private ComparisonOperator buildVarVarOperator(String op, String left, String right) {
        ComparisonOperator operator = null;
        ExpressionVariable leftValue = new ExpressionVariable(left);
        ExpressionVariable rightValue = new ExpressionVariable(right);
        if (op.equals("<")) {
          operator = new LessThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new LessThanEqualOperator(leftValue, rightValue);
        } else if (op.equals(">")) {
          operator = new GreaterThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new GreaterThanEqualOperator(leftValue, rightValue);
        } else if (op.equals("==")) {
          operator = new EqualOperator(leftValue, rightValue);
        } else if (op.equals("!=")) {
          operator = new NotEqualOperator(leftValue, rightValue);
        }
        return operator;
      }
      
      private ComparisonOperator buildVarConstOperator(String op, String left, float right) {
        ComparisonOperator operator = null;
        ArithmeticVariable leftValue = new ArithmeticVariable(left);
        ArithmeticConstant rightValue = new ArithmeticConstant(right);
        if (op.equals("<")) {
          operator = new LessThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new LessThanEqualOperator(leftValue, rightValue);
        } else if (op.equals(">")) {
          operator = new GreaterThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new GreaterThanEqualOperator(leftValue, rightValue);
        } else if (op.equals("==")) {
          operator = new EqualOperator(leftValue, rightValue);
        } else if (op.equals("!=")) {
          operator = new NotEqualOperator(leftValue, rightValue);
        }
        return operator;
      }
      
      private ComparisonOperator buildVarStringOperator(String op, String left, String right) {
        ComparisonOperator operator = null;
        StringVariable leftValue = new StringVariable(left);
        StringConstant rightValue = new StringConstant(right);
        if (op.equals("<")) {
          operator = new LessThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new LessThanEqualOperator(leftValue, rightValue);
        } else if (op.equals(">")) {
          operator = new GreaterThanOperator(leftValue, rightValue);
        } else if (op.equals("<=")) {
          operator = new GreaterThanEqualOperator(leftValue, rightValue);
        } else if (op.equals("==")) {
          operator = new EqualOperator(leftValue, rightValue);
        } else if (op.equals("!=")) {
          operator = new NotEqualOperator(leftValue, rightValue);
        }
        return operator;
      }
      */
      private BooleanPredicate buildVarBoolOperator(String var) {
        BooleanVariable value = new BooleanVariable(var);
        BooleanPredicate operator = new BooleanPredicate(value);
        return operator;
      }
      
      // This converts the text of a given AST node to a double.
      private float toFloat(String text) {
        float value = 0.0f;
        try {
          value = Float.parseFloat(text);
        } catch (NumberFormatException e) {
          throw new RuntimeException("Cannot convert \"" + text + "\" to a float.");
        }
        return value;
      }



    // $ANTLR start "ctl_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:103:1: ctl_formula returns [PredicateFormula ctlFormula] : ctl_state_formula ;
    public final PredicateFormula ctl_formula() throws RecognitionException {
        PredicateFormula ctlFormula = null;


        PredicateFormula ctl_state_formula1 =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:103:50: ( ctl_state_formula )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:104:3: ctl_state_formula
            {
            pushFollow(FOLLOW_ctl_state_formula_in_ctl_formula65);
            ctl_state_formula1=ctl_state_formula();

            state._fsp--;



                ctlFormula = ctl_state_formula1;
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ctlFormula;
    }
    // $ANTLR end "ctl_formula"


    protected static class ctl_state_formula_scope {
        PredicateFormula oper;
    }
    protected Stack ctl_state_formula_stack = new Stack();



    // $ANTLR start "ctl_state_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:217:1: ctl_state_formula returns [PredicateFormula ctlStateFormula] : ^( CTL_STATE_FORMULA leftSubFormula= ctl_state_sub_formula (operator= PROPOSITIONAL_CONNECTIVE rightSubFormula= ctl_state_sub_formula )* ) ;
    public final PredicateFormula ctl_state_formula() throws RecognitionException {
        ctl_state_formula_stack.push(new ctl_state_formula_scope());
        PredicateFormula ctlStateFormula = null;


        CommonTree operator=null;
        PredicateFormula leftSubFormula =null;

        PredicateFormula rightSubFormula =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:219:3: ( ^( CTL_STATE_FORMULA leftSubFormula= ctl_state_sub_formula (operator= PROPOSITIONAL_CONNECTIVE rightSubFormula= ctl_state_sub_formula )* ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:220:3: ^( CTL_STATE_FORMULA leftSubFormula= ctl_state_sub_formula (operator= PROPOSITIONAL_CONNECTIVE rightSubFormula= ctl_state_sub_formula )* )
            {
            match(input,CTL_STATE_FORMULA,FOLLOW_CTL_STATE_FORMULA_in_ctl_state_formula102); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_ctl_state_sub_formula_in_ctl_state_formula108);
            leftSubFormula=ctl_state_sub_formula();

            state._fsp--;


            ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper = leftSubFormula;

            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:222:7: (operator= PROPOSITIONAL_CONNECTIVE rightSubFormula= ctl_state_sub_formula )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==PROPOSITIONAL_CONNECTIVE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:223:9: operator= PROPOSITIONAL_CONNECTIVE rightSubFormula= ctl_state_sub_formula
            	    {
            	    operator=(CommonTree)match(input,PROPOSITIONAL_CONNECTIVE,FOLLOW_PROPOSITIONAL_CONNECTIVE_in_ctl_state_formula132); 

            	    pushFollow(FOLLOW_ctl_state_sub_formula_in_ctl_state_formula147);
            	    rightSubFormula=ctl_state_sub_formula();

            	    state._fsp--;



            	              if ((operator!=null?operator.getText():null).equals("&")) {
            	                ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper = new AndOperator(((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper, rightSubFormula);
            	              } else if ((operator!=null?operator.getText():null).equals("|")) {
            	                ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper = new OrOperator(((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper, rightSubFormula);
            	              } else if ((operator!=null?operator.getText():null).equals("=>")) {
            	                ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper = new ImpliesOperator(((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper, rightSubFormula);
            	              } else if ((operator!=null?operator.getText():null).equals("<=>")) {
            	                ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper = new EquivalentOperator(((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper, rightSubFormula);
            	              }           
            	            

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            match(input, Token.UP, null); 



                  ctlStateFormula = ((ctl_state_formula_scope)ctl_state_formula_stack.peek()).oper;
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            ctl_state_formula_stack.pop();
        }
        return ctlStateFormula;
    }
    // $ANTLR end "ctl_state_formula"



    // $ANTLR start "ctl_state_sub_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:244:1: ctl_state_sub_formula returns [PredicateFormula subFormula] : ( ctl_neg_sub_formula | ^( CTL_STATE_SUB_FORMULA op= CTL_BRANCH_OP path_formula= ctl_path_formula[$op.text] ) | ^( CTL_STATE_SUB_FORMULA atomic_formula ) );
    public final PredicateFormula ctl_state_sub_formula() throws RecognitionException {
        PredicateFormula subFormula = null;


        CommonTree op=null;
        PredicateFormula path_formula =null;

        PredicateFormula ctl_neg_sub_formula2 =null;

        PredicateFormula atomic_formula3 =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:245:3: ( ctl_neg_sub_formula | ^( CTL_STATE_SUB_FORMULA op= CTL_BRANCH_OP path_formula= ctl_path_formula[$op.text] ) | ^( CTL_STATE_SUB_FORMULA atomic_formula ) )
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==CTL_STATE_SUB_FORMULA) ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==DOWN) ) {
                    switch ( input.LA(3) ) {
                    case CTL_BRANCH_OP:
                        {
                        alt2=2;
                        }
                        break;
                    case NOT_CONNECTIVE:
                    case CTL_STATE_FORMULA:
                        {
                        alt2=1;
                        }
                        break;
                    case ATOMIC_SUB_FORMULA:
                        {
                        alt2=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 2, input);

                        throw nvae;

                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:245:5: ctl_neg_sub_formula
                    {
                    pushFollow(FOLLOW_ctl_neg_sub_formula_in_ctl_state_sub_formula193);
                    ctl_neg_sub_formula2=ctl_neg_sub_formula();

                    state._fsp--;


                    subFormula = ctl_neg_sub_formula2;

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:246:4: ^( CTL_STATE_SUB_FORMULA op= CTL_BRANCH_OP path_formula= ctl_path_formula[$op.text] )
                    {
                    match(input,CTL_STATE_SUB_FORMULA,FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_state_sub_formula201); 

                    match(input, Token.DOWN, null); 
                    op=(CommonTree)match(input,CTL_BRANCH_OP,FOLLOW_CTL_BRANCH_OP_in_ctl_state_sub_formula205); 

                    pushFollow(FOLLOW_ctl_path_formula_in_ctl_state_sub_formula209);
                    path_formula=ctl_path_formula((op!=null?op.getText():null));

                    state._fsp--;


                    match(input, Token.UP, null); 


                    subFormula =  path_formula;

                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:248:5: ^( CTL_STATE_SUB_FORMULA atomic_formula )
                    {
                    match(input,CTL_STATE_SUB_FORMULA,FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_state_sub_formula222); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_atomic_formula_in_ctl_state_sub_formula224);
                    atomic_formula3=atomic_formula();

                    state._fsp--;


                    match(input, Token.UP, null); 


                     subFormula =  atomic_formula3;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return subFormula;
    }
    // $ANTLR end "ctl_state_sub_formula"



    // $ANTLR start "ctl_path_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:251:1: ctl_path_formula[String branchOperator] returns [PredicateFormula subFormula] : ( ^( CTL_PATH_FORMULA path_op= CTL_PATH_OP ctl_state_formula ) | ^( CTL_PATH_FORMULA state_formula1= ctl_state_formula path_op= CTL_PATH_CONNECTIVE state_formula2= ctl_state_formula ) );
    public final PredicateFormula ctl_path_formula(String branchOperator) throws RecognitionException {
        PredicateFormula subFormula = null;


        CommonTree path_op=null;
        PredicateFormula state_formula1 =null;

        PredicateFormula state_formula2 =null;

        PredicateFormula ctl_state_formula4 =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:252:3: ( ^( CTL_PATH_FORMULA path_op= CTL_PATH_OP ctl_state_formula ) | ^( CTL_PATH_FORMULA state_formula1= ctl_state_formula path_op= CTL_PATH_CONNECTIVE state_formula2= ctl_state_formula ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==CTL_PATH_FORMULA) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==DOWN) ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2==CTL_PATH_OP) ) {
                        alt3=1;
                    }
                    else if ( (LA3_2==CTL_STATE_FORMULA) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:252:5: ^( CTL_PATH_FORMULA path_op= CTL_PATH_OP ctl_state_formula )
                    {
                    match(input,CTL_PATH_FORMULA,FOLLOW_CTL_PATH_FORMULA_in_ctl_path_formula246); 

                    match(input, Token.DOWN, null); 
                    path_op=(CommonTree)match(input,CTL_PATH_OP,FOLLOW_CTL_PATH_OP_in_ctl_path_formula252); 

                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula254);
                    ctl_state_formula4=ctl_state_formula();

                    state._fsp--;


                    match(input, Token.UP, null); 



                          if (branchOperator.equals("A")) {
                            if ((path_op!=null?path_op.getText():null).equals("X")) {
                              subFormula = new AXOperator(ctl_state_formula4);
                            } else if ((path_op!=null?path_op.getText():null).equals("G")) {
                              subFormula = new AGOperator(ctl_state_formula4);
                            } else if ((path_op!=null?path_op.getText():null).equals("F")) {
                              subFormula = new AFOperator(ctl_state_formula4);
                            } 
                          } else if (branchOperator.equals("E")) {
                            if ((path_op!=null?path_op.getText():null).equals("X")) {
                              subFormula = new EXOperator(ctl_state_formula4);
                            } else if ((path_op!=null?path_op.getText():null).equals("G")) {
                              subFormula = new EGOperator(ctl_state_formula4);
                            } else if ((path_op!=null?path_op.getText():null).equals("F")) {
                              subFormula = new EFOperator(ctl_state_formula4);
                            } 
                          }
                        

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:272:5: ^( CTL_PATH_FORMULA state_formula1= ctl_state_formula path_op= CTL_PATH_CONNECTIVE state_formula2= ctl_state_formula )
                    {
                    match(input,CTL_PATH_FORMULA,FOLLOW_CTL_PATH_FORMULA_in_ctl_path_formula268); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula274);
                    state_formula1=ctl_state_formula();

                    state._fsp--;


                    path_op=(CommonTree)match(input,CTL_PATH_CONNECTIVE,FOLLOW_CTL_PATH_CONNECTIVE_in_ctl_path_formula280); 

                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula286);
                    state_formula2=ctl_state_formula();

                    state._fsp--;


                    match(input, Token.UP, null); 



                          if (branchOperator.equals("A")) {
                            if ((path_op!=null?path_op.getText():null).equals("V")) {
                              subFormula = new AVOperator(state_formula1, state_formula2);
                            } else if ((path_op!=null?path_op.getText():null).equals("U")) {
                              subFormula = new AUOperator(state_formula1, state_formula2);
                            } else if ((path_op!=null?path_op.getText():null).equals("W")) {
                              subFormula = new AWOperator(state_formula1, state_formula2);
                            }  
                          } else if (branchOperator.equals("E")) {
                            if ((path_op!=null?path_op.getText():null).equals("V")) {
                              subFormula = new EVOperator(state_formula1, state_formula2);
                            } else if ((path_op!=null?path_op.getText():null).equals("U")) {
                              subFormula = new EUOperator(state_formula1, state_formula2);
                            } else if ((path_op!=null?path_op.getText():null).equals("W")) {
                              subFormula = new EWOperator(state_formula1, state_formula2);
                            } 
                          } 
                        

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return subFormula;
    }
    // $ANTLR end "ctl_path_formula"



    // $ANTLR start "ctl_neg_sub_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:294:1: ctl_neg_sub_formula returns [PredicateFormula subFormula] : ^( CTL_STATE_SUB_FORMULA (neg= (neg= NOT_CONNECTIVE ) )? ctl_state_formula ) ;
    public final PredicateFormula ctl_neg_sub_formula() throws RecognitionException {
        PredicateFormula subFormula = null;


        CommonTree neg=null;
        PredicateFormula ctl_state_formula5 =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:294:58: ( ^( CTL_STATE_SUB_FORMULA (neg= (neg= NOT_CONNECTIVE ) )? ctl_state_formula ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:295:3: ^( CTL_STATE_SUB_FORMULA (neg= (neg= NOT_CONNECTIVE ) )? ctl_state_formula )
            {
            match(input,CTL_STATE_SUB_FORMULA,FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_neg_sub_formula311); 

            match(input, Token.DOWN, null); 
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:295:31: (neg= (neg= NOT_CONNECTIVE ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==NOT_CONNECTIVE) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:295:31: neg= (neg= NOT_CONNECTIVE )
                    {
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:295:33: (neg= NOT_CONNECTIVE )
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:295:34: neg= NOT_CONNECTIVE
                    {
                    neg=(CommonTree)match(input,NOT_CONNECTIVE,FOLLOW_NOT_CONNECTIVE_in_ctl_neg_sub_formula322); 

                    }


                    }
                    break;

            }


            pushFollow(FOLLOW_ctl_state_formula_in_ctl_neg_sub_formula326);
            ctl_state_formula5=ctl_state_formula();

            state._fsp--;


            match(input, Token.UP, null); 



                if (neg == null) {
                  subFormula = ctl_state_formula5;
                } else {
                  subFormula = new NotOperator(ctl_state_formula5);
                }
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return subFormula;
    }
    // $ANTLR end "ctl_neg_sub_formula"



    // $ANTLR start "atomic_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:304:1: atomic_formula returns [PredicateFormula subFormula] : ( ^( ATOMIC_SUB_FORMULA var_expression ) | ^( ATOMIC_SUB_FORMULA bool= BOOLEAN ) );
    public final PredicateFormula atomic_formula() throws RecognitionException {
        PredicateFormula subFormula = null;


        CommonTree bool=null;
        PredicateFormula var_expression6 =null;


        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:304:54: ( ^( ATOMIC_SUB_FORMULA var_expression ) | ^( ATOMIC_SUB_FORMULA bool= BOOLEAN ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ATOMIC_SUB_FORMULA) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==DOWN) ) {
                    int LA5_2 = input.LA(3);

                    if ( (LA5_2==BOOLEAN) ) {
                        alt5=2;
                    }
                    else if ( (LA5_2==COMPARISON_OPERATOR||LA5_2==VAR) ) {
                        alt5=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:305:3: ^( ATOMIC_SUB_FORMULA var_expression )
                    {
                    match(input,ATOMIC_SUB_FORMULA,FOLLOW_ATOMIC_SUB_FORMULA_in_atomic_formula347); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_var_expression_in_atomic_formula349);
                    var_expression6=var_expression();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        subFormula =  var_expression6;
                      

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:309:5: ^( ATOMIC_SUB_FORMULA bool= BOOLEAN )
                    {
                    match(input,ATOMIC_SUB_FORMULA,FOLLOW_ATOMIC_SUB_FORMULA_in_atomic_formula361); 

                    match(input, Token.DOWN, null); 
                    bool=(CommonTree)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_atomic_formula367); 

                    match(input, Token.UP, null); 



                        if ((bool!=null?bool.getText():null) != null && ((bool!=null?bool.getText():null).equals("true") || (bool!=null?bool.getText():null).equals("false"))) {
                          boolean value = (bool!=null?bool.getText():null).equals("true");
                          subFormula = new BooleanPredicate(new BooleanConstant(value));
                        } 
                      

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return subFormula;
    }
    // $ANTLR end "atomic_formula"



    // $ANTLR start "var_expression"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:318:1: var_expression returns [PredicateFormula subFormula] : ( ^(op= COMPARISON_OPERATOR var1= VAR var2= VAR ) | ^(op= COMPARISON_OPERATOR var= VAR con= CONSTANT ) | ^(op= COMPARISON_OPERATOR var= VAR str= STRING ) |var= VAR );
    public final PredicateFormula var_expression() throws RecognitionException {
        PredicateFormula subFormula = null;


        CommonTree op=null;
        CommonTree var1=null;
        CommonTree var2=null;
        CommonTree var=null;
        CommonTree con=null;
        CommonTree str=null;

        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:319:3: ( ^(op= COMPARISON_OPERATOR var1= VAR var2= VAR ) | ^(op= COMPARISON_OPERATOR var= VAR con= CONSTANT ) | ^(op= COMPARISON_OPERATOR var= VAR str= STRING ) |var= VAR )
            int alt6=4;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==COMPARISON_OPERATOR) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==DOWN) ) {
                    int LA6_3 = input.LA(3);

                    if ( (LA6_3==VAR) ) {
                        switch ( input.LA(4) ) {
                        case VAR:
                            {
                            alt6=1;
                            }
                            break;
                        case CONSTANT:
                            {
                            alt6=2;
                            }
                            break;
                        case STRING:
                            {
                            alt6=3;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 4, input);

                            throw nvae;

                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 3, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA6_0==VAR) ) {
                alt6=4;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:319:5: ^(op= COMPARISON_OPERATOR var1= VAR var2= VAR )
                    {
                    op=(CommonTree)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression396); 

                    match(input, Token.DOWN, null); 
                    var1=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_expression402); 

                    var2=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_expression408); 

                    match(input, Token.UP, null); 



                        //subFormula = buildVarVarOperator((op!=null?op.getText():null), (var1!=null?var1.getText():null), (var2!=null?var2.getText():null));
                    subFormula = null;

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:323:5: ^(op= COMPARISON_OPERATOR var= VAR con= CONSTANT )
                    {
                    op=(CommonTree)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression425); 

                    match(input, Token.DOWN, null); 
                    var=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_expression431); 

                    con=(CommonTree)match(input,CONSTANT,FOLLOW_CONSTANT_in_var_expression437); 

                    match(input, Token.UP, null); 



                        //subFormula = buildVarConstOperator((op!=null?op.getText():null), (var!=null?var.getText():null), toFloat((con!=null?con.getText():null)));
                        subFormula = null;

                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:327:5: ^(op= COMPARISON_OPERATOR var= VAR str= STRING )
                    {
                    op=(CommonTree)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression453); 

                    match(input, Token.DOWN, null); 
                    var=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_expression457); 

                    str=(CommonTree)match(input,STRING,FOLLOW_STRING_in_var_expression461); 

                    match(input, Token.UP, null); 



                        //subFormula = buildVarStringOperator((op!=null?op.getText():null), (var!=null?var.getText():null), (str!=null?str.getText():null));
                        subFormula = null;

                    }
                    break;
                case 4 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecTreeParser.g:331:5: var= VAR
                    {
                    var=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_expression476); 


                        subFormula = buildVarBoolOperator((var!=null?var.getText():null));
                      

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return subFormula;
    }
    // $ANTLR end "var_expression"

    // Delegated rules


 

    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_formula65 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CTL_STATE_FORMULA_in_ctl_state_formula102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ctl_state_sub_formula_in_ctl_state_formula108 = new BitSet(new long[]{0x0000000000080008L});
    public static final BitSet FOLLOW_PROPOSITIONAL_CONNECTIVE_in_ctl_state_formula132 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ctl_state_sub_formula_in_ctl_state_formula147 = new BitSet(new long[]{0x0000000000080008L});
    public static final BitSet FOLLOW_ctl_neg_sub_formula_in_ctl_state_sub_formula193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_state_sub_formula201 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CTL_BRANCH_OP_in_ctl_state_sub_formula205 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_ctl_path_formula_in_ctl_state_sub_formula209 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_state_sub_formula222 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_atomic_formula_in_ctl_state_sub_formula224 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CTL_PATH_FORMULA_in_ctl_path_formula246 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CTL_PATH_OP_in_ctl_path_formula252 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula254 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CTL_PATH_FORMULA_in_ctl_path_formula268 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula274 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CTL_PATH_CONNECTIVE_in_ctl_path_formula280 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula286 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CTL_STATE_SUB_FORMULA_in_ctl_neg_sub_formula311 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NOT_CONNECTIVE_in_ctl_neg_sub_formula322 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_neg_sub_formula326 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOMIC_SUB_FORMULA_in_atomic_formula347 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_var_expression_in_atomic_formula349 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOMIC_SUB_FORMULA_in_atomic_formula361 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOLEAN_in_atomic_formula367 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression396 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_in_var_expression402 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_VAR_in_var_expression408 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression425 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_in_var_expression431 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_CONSTANT_in_var_expression437 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression453 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_in_var_expression457 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_in_var_expression461 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VAR_in_var_expression476 = new BitSet(new long[]{0x0000000000000002L});

}