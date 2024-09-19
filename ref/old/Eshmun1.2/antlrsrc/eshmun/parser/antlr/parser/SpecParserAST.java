// $ANTLR 3.4 C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g 2013-07-13 11:08:19

//options { backtrack = true; }
  package eshmun.parser.antlr.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class SpecParserAST extends Parser {
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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public SpecParserAST(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public SpecParserAST(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return SpecParserAST.tokenNames; }
    public String getGrammarFileName() { return "C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g"; }


    public static class ctl_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ctl_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:25:1: ctl_formula : ctl_state_formula EOF !;
    public final SpecParserAST.ctl_formula_return ctl_formula() throws RecognitionException {
        SpecParserAST.ctl_formula_return retval = new SpecParserAST.ctl_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token EOF2=null;
        SpecParserAST.ctl_state_formula_return ctl_state_formula1 =null;


        Object EOF2_tree=null;

        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:26:3: ( ctl_state_formula EOF !)
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:26:5: ctl_state_formula EOF !
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_ctl_state_formula_in_ctl_formula90);
            ctl_state_formula1=ctl_state_formula();

            state._fsp--;

            adaptor.addChild(root_0, ctl_state_formula1.getTree());

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_ctl_formula92); 

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ctl_formula"


    public static class ctl_state_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ctl_state_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:52:1: ctl_state_formula : ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* -> ^( CTL_STATE_FORMULA ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* ) ;
    public final SpecParserAST.ctl_state_formula_return ctl_state_formula() throws RecognitionException {
        SpecParserAST.ctl_state_formula_return retval = new SpecParserAST.ctl_state_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PROPOSITIONAL_CONNECTIVE4=null;
        SpecParserAST.ctl_state_sub_formula_return ctl_state_sub_formula3 =null;

        SpecParserAST.ctl_state_sub_formula_return ctl_state_sub_formula5 =null;


        Object PROPOSITIONAL_CONNECTIVE4_tree=null;
        RewriteRuleTokenStream stream_PROPOSITIONAL_CONNECTIVE=new RewriteRuleTokenStream(adaptor,"token PROPOSITIONAL_CONNECTIVE");
        RewriteRuleSubtreeStream stream_ctl_state_sub_formula=new RewriteRuleSubtreeStream(adaptor,"rule ctl_state_sub_formula");
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:3: ( ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* -> ^( CTL_STATE_FORMULA ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:5: ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )*
            {
            pushFollow(FOLLOW_ctl_state_sub_formula_in_ctl_state_formula115);
            ctl_state_sub_formula3=ctl_state_sub_formula();

            state._fsp--;

            stream_ctl_state_sub_formula.add(ctl_state_sub_formula3.getTree());

            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:27: ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==PROPOSITIONAL_CONNECTIVE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:28: PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula
            	    {
            	    PROPOSITIONAL_CONNECTIVE4=(Token)match(input,PROPOSITIONAL_CONNECTIVE,FOLLOW_PROPOSITIONAL_CONNECTIVE_in_ctl_state_formula118);  
            	    stream_PROPOSITIONAL_CONNECTIVE.add(PROPOSITIONAL_CONNECTIVE4);


            	    pushFollow(FOLLOW_ctl_state_sub_formula_in_ctl_state_formula120);
            	    ctl_state_sub_formula5=ctl_state_sub_formula();

            	    state._fsp--;

            	    stream_ctl_state_sub_formula.add(ctl_state_sub_formula5.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            // AST REWRITE
            // elements: ctl_state_sub_formula, ctl_state_sub_formula, PROPOSITIONAL_CONNECTIVE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:77: -> ^( CTL_STATE_FORMULA ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* )
            {
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:80: ^( CTL_STATE_FORMULA ctl_state_sub_formula ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(CTL_STATE_FORMULA, "CTL_STATE_FORMULA")
                , root_1);

                adaptor.addChild(root_1, stream_ctl_state_sub_formula.nextTree());

                // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:53:122: ( PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula )*
                while ( stream_ctl_state_sub_formula.hasNext()||stream_PROPOSITIONAL_CONNECTIVE.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_PROPOSITIONAL_CONNECTIVE.nextNode()
                    );

                    adaptor.addChild(root_1, stream_ctl_state_sub_formula.nextTree());

                }
                stream_ctl_state_sub_formula.reset();
                stream_PROPOSITIONAL_CONNECTIVE.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ctl_state_formula"


    public static class ctl_state_sub_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ctl_state_sub_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:55:1: ctl_state_sub_formula : ( ctl_neg_sub_formula | CTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET -> ^( CTL_STATE_SUB_FORMULA CTL_BRANCH_OP ctl_path_formula ) | atomic_formula -> ^( CTL_STATE_SUB_FORMULA atomic_formula ) );
    public final SpecParserAST.ctl_state_sub_formula_return ctl_state_sub_formula() throws RecognitionException {
        SpecParserAST.ctl_state_sub_formula_return retval = new SpecParserAST.ctl_state_sub_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CTL_BRANCH_OP7=null;
        Token LEFT_BRACKET8=null;
        Token RIGHT_BRACKET10=null;
        SpecParserAST.ctl_neg_sub_formula_return ctl_neg_sub_formula6 =null;

        SpecParserAST.ctl_path_formula_return ctl_path_formula9 =null;

        SpecParserAST.atomic_formula_return atomic_formula11 =null;


        Object CTL_BRANCH_OP7_tree=null;
        Object LEFT_BRACKET8_tree=null;
        Object RIGHT_BRACKET10_tree=null;
        RewriteRuleTokenStream stream_CTL_BRANCH_OP=new RewriteRuleTokenStream(adaptor,"token CTL_BRANCH_OP");
        RewriteRuleTokenStream stream_LEFT_BRACKET=new RewriteRuleTokenStream(adaptor,"token LEFT_BRACKET");
        RewriteRuleTokenStream stream_RIGHT_BRACKET=new RewriteRuleTokenStream(adaptor,"token RIGHT_BRACKET");
        RewriteRuleSubtreeStream stream_atomic_formula=new RewriteRuleSubtreeStream(adaptor,"rule atomic_formula");
        RewriteRuleSubtreeStream stream_ctl_path_formula=new RewriteRuleSubtreeStream(adaptor,"rule ctl_path_formula");
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:56:3: ( ctl_neg_sub_formula | CTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET -> ^( CTL_STATE_SUB_FORMULA CTL_BRANCH_OP ctl_path_formula ) | atomic_formula -> ^( CTL_STATE_SUB_FORMULA atomic_formula ) )
            int alt2=3;
            switch ( input.LA(1) ) {
            case LEFT_PARANTHESIS:
            case NOT_CONNECTIVE:
                {
                alt2=1;
                }
                break;
            case CTL_BRANCH_OP:
                {
                alt2=2;
                }
                break;
            case BOOLEAN:
            case VAR:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:56:5: ctl_neg_sub_formula
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_ctl_neg_sub_formula_in_ctl_state_sub_formula149);
                    ctl_neg_sub_formula6=ctl_neg_sub_formula();

                    state._fsp--;

                    adaptor.addChild(root_0, ctl_neg_sub_formula6.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:57:5: CTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET
                    {
                    CTL_BRANCH_OP7=(Token)match(input,CTL_BRANCH_OP,FOLLOW_CTL_BRANCH_OP_in_ctl_state_sub_formula155);  
                    stream_CTL_BRANCH_OP.add(CTL_BRANCH_OP7);


                    LEFT_BRACKET8=(Token)match(input,LEFT_BRACKET,FOLLOW_LEFT_BRACKET_in_ctl_state_sub_formula157);  
                    stream_LEFT_BRACKET.add(LEFT_BRACKET8);


                    pushFollow(FOLLOW_ctl_path_formula_in_ctl_state_sub_formula159);
                    ctl_path_formula9=ctl_path_formula();

                    state._fsp--;

                    stream_ctl_path_formula.add(ctl_path_formula9.getTree());

                    RIGHT_BRACKET10=(Token)match(input,RIGHT_BRACKET,FOLLOW_RIGHT_BRACKET_in_ctl_state_sub_formula161);  
                    stream_RIGHT_BRACKET.add(RIGHT_BRACKET10);


                    // AST REWRITE
                    // elements: CTL_BRANCH_OP, ctl_path_formula
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 57:63: -> ^( CTL_STATE_SUB_FORMULA CTL_BRANCH_OP ctl_path_formula )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:57:66: ^( CTL_STATE_SUB_FORMULA CTL_BRANCH_OP ctl_path_formula )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CTL_STATE_SUB_FORMULA, "CTL_STATE_SUB_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_CTL_BRANCH_OP.nextNode()
                        );

                        adaptor.addChild(root_1, stream_ctl_path_formula.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:59:5: atomic_formula
                    {
                    pushFollow(FOLLOW_atomic_formula_in_ctl_state_sub_formula180);
                    atomic_formula11=atomic_formula();

                    state._fsp--;

                    stream_atomic_formula.add(atomic_formula11.getTree());

                    // AST REWRITE
                    // elements: atomic_formula
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 59:20: -> ^( CTL_STATE_SUB_FORMULA atomic_formula )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:59:23: ^( CTL_STATE_SUB_FORMULA atomic_formula )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CTL_STATE_SUB_FORMULA, "CTL_STATE_SUB_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, stream_atomic_formula.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ctl_state_sub_formula"


    public static class ctl_neg_sub_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ctl_neg_sub_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:63:1: ctl_neg_sub_formula : ( NOT_CONNECTIVE )? LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_STATE_SUB_FORMULA ( NOT_CONNECTIVE )? ctl_state_formula ) ;
    public final SpecParserAST.ctl_neg_sub_formula_return ctl_neg_sub_formula() throws RecognitionException {
        SpecParserAST.ctl_neg_sub_formula_return retval = new SpecParserAST.ctl_neg_sub_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token NOT_CONNECTIVE12=null;
        Token LEFT_PARANTHESIS13=null;
        Token RIGHT_PARANTHESIS15=null;
        SpecParserAST.ctl_state_formula_return ctl_state_formula14 =null;


        Object NOT_CONNECTIVE12_tree=null;
        Object LEFT_PARANTHESIS13_tree=null;
        Object RIGHT_PARANTHESIS15_tree=null;
        RewriteRuleTokenStream stream_NOT_CONNECTIVE=new RewriteRuleTokenStream(adaptor,"token NOT_CONNECTIVE");
        RewriteRuleTokenStream stream_LEFT_PARANTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARANTHESIS");
        RewriteRuleTokenStream stream_RIGHT_PARANTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARANTHESIS");
        RewriteRuleSubtreeStream stream_ctl_state_formula=new RewriteRuleSubtreeStream(adaptor,"rule ctl_state_formula");
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:3: ( ( NOT_CONNECTIVE )? LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_STATE_SUB_FORMULA ( NOT_CONNECTIVE )? ctl_state_formula ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:5: ( NOT_CONNECTIVE )? LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS
            {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:5: ( NOT_CONNECTIVE )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==NOT_CONNECTIVE) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:6: NOT_CONNECTIVE
                    {
                    NOT_CONNECTIVE12=(Token)match(input,NOT_CONNECTIVE,FOLLOW_NOT_CONNECTIVE_in_ctl_neg_sub_formula202);  
                    stream_NOT_CONNECTIVE.add(NOT_CONNECTIVE12);


                    }
                    break;

            }


            LEFT_PARANTHESIS13=(Token)match(input,LEFT_PARANTHESIS,FOLLOW_LEFT_PARANTHESIS_in_ctl_neg_sub_formula206);  
            stream_LEFT_PARANTHESIS.add(LEFT_PARANTHESIS13);


            pushFollow(FOLLOW_ctl_state_formula_in_ctl_neg_sub_formula208);
            ctl_state_formula14=ctl_state_formula();

            state._fsp--;

            stream_ctl_state_formula.add(ctl_state_formula14.getTree());

            RIGHT_PARANTHESIS15=(Token)match(input,RIGHT_PARANTHESIS,FOLLOW_RIGHT_PARANTHESIS_in_ctl_neg_sub_formula210);  
            stream_RIGHT_PARANTHESIS.add(RIGHT_PARANTHESIS15);


            // AST REWRITE
            // elements: ctl_state_formula, NOT_CONNECTIVE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 64:76: -> ^( CTL_STATE_SUB_FORMULA ( NOT_CONNECTIVE )? ctl_state_formula )
            {
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:79: ^( CTL_STATE_SUB_FORMULA ( NOT_CONNECTIVE )? ctl_state_formula )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(CTL_STATE_SUB_FORMULA, "CTL_STATE_SUB_FORMULA")
                , root_1);

                // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:64:103: ( NOT_CONNECTIVE )?
                if ( stream_NOT_CONNECTIVE.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_NOT_CONNECTIVE.nextNode()
                    );

                }
                stream_NOT_CONNECTIVE.reset();

                adaptor.addChild(root_1, stream_ctl_state_formula.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ctl_neg_sub_formula"


    public static class ctl_path_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ctl_path_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:66:1: ctl_path_formula : ( CTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_PATH_FORMULA CTL_PATH_OP ctl_state_formula ) | LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_PATH_FORMULA ctl_state_formula CTL_PATH_CONNECTIVE ctl_state_formula ) );
    public final SpecParserAST.ctl_path_formula_return ctl_path_formula() throws RecognitionException {
        SpecParserAST.ctl_path_formula_return retval = new SpecParserAST.ctl_path_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CTL_PATH_OP16=null;
        Token LEFT_PARANTHESIS17=null;
        Token RIGHT_PARANTHESIS19=null;
        Token LEFT_PARANTHESIS20=null;
        Token RIGHT_PARANTHESIS22=null;
        Token CTL_PATH_CONNECTIVE23=null;
        Token LEFT_PARANTHESIS24=null;
        Token RIGHT_PARANTHESIS26=null;
        SpecParserAST.ctl_state_formula_return ctl_state_formula18 =null;

        SpecParserAST.ctl_state_formula_return ctl_state_formula21 =null;

        SpecParserAST.ctl_state_formula_return ctl_state_formula25 =null;


        Object CTL_PATH_OP16_tree=null;
        Object LEFT_PARANTHESIS17_tree=null;
        Object RIGHT_PARANTHESIS19_tree=null;
        Object LEFT_PARANTHESIS20_tree=null;
        Object RIGHT_PARANTHESIS22_tree=null;
        Object CTL_PATH_CONNECTIVE23_tree=null;
        Object LEFT_PARANTHESIS24_tree=null;
        Object RIGHT_PARANTHESIS26_tree=null;
        RewriteRuleTokenStream stream_LEFT_PARANTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARANTHESIS");
        RewriteRuleTokenStream stream_CTL_PATH_CONNECTIVE=new RewriteRuleTokenStream(adaptor,"token CTL_PATH_CONNECTIVE");
        RewriteRuleTokenStream stream_CTL_PATH_OP=new RewriteRuleTokenStream(adaptor,"token CTL_PATH_OP");
        RewriteRuleTokenStream stream_RIGHT_PARANTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARANTHESIS");
        RewriteRuleSubtreeStream stream_ctl_state_formula=new RewriteRuleSubtreeStream(adaptor,"rule ctl_state_formula");
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:67:3: ( CTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_PATH_FORMULA CTL_PATH_OP ctl_state_formula ) | LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS -> ^( CTL_PATH_FORMULA ctl_state_formula CTL_PATH_CONNECTIVE ctl_state_formula ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==CTL_PATH_OP) ) {
                alt4=1;
            }
            else if ( (LA4_0==LEFT_PARANTHESIS) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:67:5: CTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS
                    {
                    CTL_PATH_OP16=(Token)match(input,CTL_PATH_OP,FOLLOW_CTL_PATH_OP_in_ctl_path_formula236);  
                    stream_CTL_PATH_OP.add(CTL_PATH_OP16);


                    LEFT_PARANTHESIS17=(Token)match(input,LEFT_PARANTHESIS,FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula238);  
                    stream_LEFT_PARANTHESIS.add(LEFT_PARANTHESIS17);


                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula240);
                    ctl_state_formula18=ctl_state_formula();

                    state._fsp--;

                    stream_ctl_state_formula.add(ctl_state_formula18.getTree());

                    RIGHT_PARANTHESIS19=(Token)match(input,RIGHT_PARANTHESIS,FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula242);  
                    stream_RIGHT_PARANTHESIS.add(RIGHT_PARANTHESIS19);


                    // AST REWRITE
                    // elements: CTL_PATH_OP, ctl_state_formula
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 67:70: -> ^( CTL_PATH_FORMULA CTL_PATH_OP ctl_state_formula )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:67:73: ^( CTL_PATH_FORMULA CTL_PATH_OP ctl_state_formula )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CTL_PATH_FORMULA, "CTL_PATH_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_CTL_PATH_OP.nextNode()
                        );

                        adaptor.addChild(root_1, stream_ctl_state_formula.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:68:5: LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS
                    {
                    LEFT_PARANTHESIS20=(Token)match(input,LEFT_PARANTHESIS,FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula258);  
                    stream_LEFT_PARANTHESIS.add(LEFT_PARANTHESIS20);


                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula260);
                    ctl_state_formula21=ctl_state_formula();

                    state._fsp--;

                    stream_ctl_state_formula.add(ctl_state_formula21.getTree());

                    RIGHT_PARANTHESIS22=(Token)match(input,RIGHT_PARANTHESIS,FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula262);  
                    stream_RIGHT_PARANTHESIS.add(RIGHT_PARANTHESIS22);


                    CTL_PATH_CONNECTIVE23=(Token)match(input,CTL_PATH_CONNECTIVE,FOLLOW_CTL_PATH_CONNECTIVE_in_ctl_path_formula264);  
                    stream_CTL_PATH_CONNECTIVE.add(CTL_PATH_CONNECTIVE23);


                    LEFT_PARANTHESIS24=(Token)match(input,LEFT_PARANTHESIS,FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula266);  
                    stream_LEFT_PARANTHESIS.add(LEFT_PARANTHESIS24);


                    pushFollow(FOLLOW_ctl_state_formula_in_ctl_path_formula268);
                    ctl_state_formula25=ctl_state_formula();

                    state._fsp--;

                    stream_ctl_state_formula.add(ctl_state_formula25.getTree());

                    RIGHT_PARANTHESIS26=(Token)match(input,RIGHT_PARANTHESIS,FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula270);  
                    stream_RIGHT_PARANTHESIS.add(RIGHT_PARANTHESIS26);


                    // AST REWRITE
                    // elements: CTL_PATH_CONNECTIVE, ctl_state_formula, ctl_state_formula
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 69:3: -> ^( CTL_PATH_FORMULA ctl_state_formula CTL_PATH_CONNECTIVE ctl_state_formula )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:69:6: ^( CTL_PATH_FORMULA ctl_state_formula CTL_PATH_CONNECTIVE ctl_state_formula )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CTL_PATH_FORMULA, "CTL_PATH_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, stream_ctl_state_formula.nextTree());

                        adaptor.addChild(root_1, 
                        stream_CTL_PATH_CONNECTIVE.nextNode()
                        );

                        adaptor.addChild(root_1, stream_ctl_state_formula.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ctl_path_formula"


    public static class atomic_formula_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "atomic_formula"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:71:1: atomic_formula : ( var_expression -> ^( ATOMIC_SUB_FORMULA var_expression ) | BOOLEAN -> ^( ATOMIC_SUB_FORMULA BOOLEAN ) );
    public final SpecParserAST.atomic_formula_return atomic_formula() throws RecognitionException {
        SpecParserAST.atomic_formula_return retval = new SpecParserAST.atomic_formula_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token BOOLEAN28=null;
        SpecParserAST.var_expression_return var_expression27 =null;


        Object BOOLEAN28_tree=null;
        RewriteRuleTokenStream stream_BOOLEAN=new RewriteRuleTokenStream(adaptor,"token BOOLEAN");
        RewriteRuleSubtreeStream stream_var_expression=new RewriteRuleSubtreeStream(adaptor,"rule var_expression");
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:72:3: ( var_expression -> ^( ATOMIC_SUB_FORMULA var_expression ) | BOOLEAN -> ^( ATOMIC_SUB_FORMULA BOOLEAN ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==VAR) ) {
                alt5=1;
            }
            else if ( (LA5_0==BOOLEAN) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:72:5: var_expression
                    {
                    pushFollow(FOLLOW_var_expression_in_atomic_formula296);
                    var_expression27=var_expression();

                    state._fsp--;

                    stream_var_expression.add(var_expression27.getTree());

                    // AST REWRITE
                    // elements: var_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 72:20: -> ^( ATOMIC_SUB_FORMULA var_expression )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:72:23: ^( ATOMIC_SUB_FORMULA var_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(ATOMIC_SUB_FORMULA, "ATOMIC_SUB_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, stream_var_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:73:5: BOOLEAN
                    {
                    BOOLEAN28=(Token)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_atomic_formula310);  
                    stream_BOOLEAN.add(BOOLEAN28);


                    // AST REWRITE
                    // elements: BOOLEAN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 73:13: -> ^( ATOMIC_SUB_FORMULA BOOLEAN )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:73:16: ^( ATOMIC_SUB_FORMULA BOOLEAN )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(ATOMIC_SUB_FORMULA, "ATOMIC_SUB_FORMULA")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_BOOLEAN.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "atomic_formula"


    public static class var_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "var_expression"
    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:75:1: var_expression : (var1= VAR COMPARISON_OPERATOR var2= VAR -> ^( COMPARISON_OPERATOR $var1 $var2) | VAR COMPARISON_OPERATOR CONSTANT -> ^( COMPARISON_OPERATOR VAR CONSTANT ) | VAR COMPARISON_OPERATOR STRING -> ^( COMPARISON_OPERATOR VAR STRING ) | VAR );
    public final SpecParserAST.var_expression_return var_expression() throws RecognitionException {
        SpecParserAST.var_expression_return retval = new SpecParserAST.var_expression_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token var1=null;
        Token var2=null;
        Token COMPARISON_OPERATOR29=null;
        Token VAR30=null;
        Token COMPARISON_OPERATOR31=null;
        Token CONSTANT32=null;
        Token VAR33=null;
        Token COMPARISON_OPERATOR34=null;
        Token STRING35=null;
        Token VAR36=null;

        Object var1_tree=null;
        Object var2_tree=null;
        Object COMPARISON_OPERATOR29_tree=null;
        Object VAR30_tree=null;
        Object COMPARISON_OPERATOR31_tree=null;
        Object CONSTANT32_tree=null;
        Object VAR33_tree=null;
        Object COMPARISON_OPERATOR34_tree=null;
        Object STRING35_tree=null;
        Object VAR36_tree=null;
        RewriteRuleTokenStream stream_CONSTANT=new RewriteRuleTokenStream(adaptor,"token CONSTANT");
        RewriteRuleTokenStream stream_VAR=new RewriteRuleTokenStream(adaptor,"token VAR");
        RewriteRuleTokenStream stream_COMPARISON_OPERATOR=new RewriteRuleTokenStream(adaptor,"token COMPARISON_OPERATOR");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:76:3: (var1= VAR COMPARISON_OPERATOR var2= VAR -> ^( COMPARISON_OPERATOR $var1 $var2) | VAR COMPARISON_OPERATOR CONSTANT -> ^( COMPARISON_OPERATOR VAR CONSTANT ) | VAR COMPARISON_OPERATOR STRING -> ^( COMPARISON_OPERATOR VAR STRING ) | VAR )
            int alt6=4;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==VAR) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==COMPARISON_OPERATOR) ) {
                    switch ( input.LA(3) ) {
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
                            new NoViableAltException("", 6, 2, input);

                        throw nvae;

                    }

                }
                else if ( (LA6_1==EOF||LA6_1==PROPOSITIONAL_CONNECTIVE||LA6_1==RIGHT_PARANTHESIS) ) {
                    alt6=4;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:76:5: var1= VAR COMPARISON_OPERATOR var2= VAR
                    {
                    var1=(Token)match(input,VAR,FOLLOW_VAR_in_var_expression332);  
                    stream_VAR.add(var1);


                    COMPARISON_OPERATOR29=(Token)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression334);  
                    stream_COMPARISON_OPERATOR.add(COMPARISON_OPERATOR29);


                    var2=(Token)match(input,VAR,FOLLOW_VAR_in_var_expression338);  
                    stream_VAR.add(var2);


                    // AST REWRITE
                    // elements: var1, COMPARISON_OPERATOR, var2
                    // token labels: var1, var2
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_var1=new RewriteRuleTokenStream(adaptor,"token var1",var1);
                    RewriteRuleTokenStream stream_var2=new RewriteRuleTokenStream(adaptor,"token var2",var2);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 76:43: -> ^( COMPARISON_OPERATOR $var1 $var2)
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:76:46: ^( COMPARISON_OPERATOR $var1 $var2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        stream_COMPARISON_OPERATOR.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, stream_var1.nextNode());

                        adaptor.addChild(root_1, stream_var2.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:77:5: VAR COMPARISON_OPERATOR CONSTANT
                    {
                    VAR30=(Token)match(input,VAR,FOLLOW_VAR_in_var_expression357);  
                    stream_VAR.add(VAR30);


                    COMPARISON_OPERATOR31=(Token)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression359);  
                    stream_COMPARISON_OPERATOR.add(COMPARISON_OPERATOR31);


                    CONSTANT32=(Token)match(input,CONSTANT,FOLLOW_CONSTANT_in_var_expression361);  
                    stream_CONSTANT.add(CONSTANT32);


                    // AST REWRITE
                    // elements: VAR, COMPARISON_OPERATOR, CONSTANT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 77:38: -> ^( COMPARISON_OPERATOR VAR CONSTANT )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:77:41: ^( COMPARISON_OPERATOR VAR CONSTANT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        stream_COMPARISON_OPERATOR.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_VAR.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_CONSTANT.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:78:5: VAR COMPARISON_OPERATOR STRING
                    {
                    VAR33=(Token)match(input,VAR,FOLLOW_VAR_in_var_expression377);  
                    stream_VAR.add(VAR33);


                    COMPARISON_OPERATOR34=(Token)match(input,COMPARISON_OPERATOR,FOLLOW_COMPARISON_OPERATOR_in_var_expression379);  
                    stream_COMPARISON_OPERATOR.add(COMPARISON_OPERATOR34);


                    STRING35=(Token)match(input,STRING,FOLLOW_STRING_in_var_expression381);  
                    stream_STRING.add(STRING35);


                    // AST REWRITE
                    // elements: STRING, COMPARISON_OPERATOR, VAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 78:36: -> ^( COMPARISON_OPERATOR VAR STRING )
                    {
                        // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:78:38: ^( COMPARISON_OPERATOR VAR STRING )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        stream_COMPARISON_OPERATOR.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_VAR.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_STRING.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 4 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecParserAST.g:79:5: VAR
                    {
                    root_0 = (Object)adaptor.nil();


                    VAR36=(Token)match(input,VAR,FOLLOW_VAR_in_var_expression396); 
                    VAR36_tree = 
                    (Object)adaptor.create(VAR36)
                    ;
                    adaptor.addChild(root_0, VAR36_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "var_expression"

    // Delegated rules


 

    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_formula90 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_ctl_formula92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ctl_state_sub_formula_in_ctl_state_formula115 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_PROPOSITIONAL_CONNECTIVE_in_ctl_state_formula118 = new BitSet(new long[]{0x0000000001042110L});
    public static final BitSet FOLLOW_ctl_state_sub_formula_in_ctl_state_formula120 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_ctl_neg_sub_formula_in_ctl_state_sub_formula149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CTL_BRANCH_OP_in_ctl_state_sub_formula155 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_LEFT_BRACKET_in_ctl_state_sub_formula157 = new BitSet(new long[]{0x0000000000002400L});
    public static final BitSet FOLLOW_ctl_path_formula_in_ctl_state_sub_formula159 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_RIGHT_BRACKET_in_ctl_state_sub_formula161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_formula_in_ctl_state_sub_formula180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_CONNECTIVE_in_ctl_neg_sub_formula202 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_LEFT_PARANTHESIS_in_ctl_neg_sub_formula206 = new BitSet(new long[]{0x0000000001042110L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_neg_sub_formula208 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PARANTHESIS_in_ctl_neg_sub_formula210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CTL_PATH_OP_in_ctl_path_formula236 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula238 = new BitSet(new long[]{0x0000000001042110L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula240 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula258 = new BitSet(new long[]{0x0000000001042110L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula260 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula262 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CTL_PATH_CONNECTIVE_in_ctl_path_formula264 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_LEFT_PARANTHESIS_in_ctl_path_formula266 = new BitSet(new long[]{0x0000000001042110L});
    public static final BitSet FOLLOW_ctl_state_formula_in_ctl_path_formula268 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PARANTHESIS_in_ctl_path_formula270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_expression_in_atomic_formula296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOLEAN_in_atomic_formula310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_var_expression332 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression334 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_VAR_in_var_expression338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_var_expression357 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression359 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_CONSTANT_in_var_expression361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_var_expression377 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COMPARISON_OPERATOR_in_var_expression379 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_in_var_expression381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_var_expression396 = new BitSet(new long[]{0x0000000000000002L});

}