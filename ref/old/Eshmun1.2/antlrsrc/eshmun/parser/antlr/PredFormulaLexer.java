// $ANTLR 3.1.3 Mar 18, 2009 10:09:25 E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g 2010-11-11 06:53:56

  package eshmun.parser.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class PredFormulaLexer extends Lexer {
    public static final int PROPOSITIONAL_CONNECTIVE=4;
    public static final int SMALL_LETTER=14;
    public static final int WS=18;
    public static final int ATOMIC_PROPOSITION=13;
    public static final int ARITHMETIC_OPERATOR=6;
    public static final int LETTER=16;
    public static final int LEFT_PARANTHESIS=10;
    public static final int NOTHING=17;
    public static final int CONSTANT=9;
    public static final int NEGATIVE_SIGN=7;
    public static final int VAR=15;
    public static final int NOT_CONNECTIVE=5;
    public static final int DIGIT=8;
    public static final int EOF=-1;
    public static final int CAPS_LETTER=12;
    public static final int RIGHT_PARANTHESIS=11;

    // delegates
    // delegators

    public PredFormulaLexer() {;} 
    public PredFormulaLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public PredFormulaLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g"; }

    // $ANTLR start "PROPOSITIONAL_CONNECTIVE"
    public final void mPROPOSITIONAL_CONNECTIVE() throws RecognitionException {
        try {
            int _type = PROPOSITIONAL_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:11:26: ( '|' | '&' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:
            {
            if ( input.LA(1)=='&'||input.LA(1)=='|' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PROPOSITIONAL_CONNECTIVE"

    // $ANTLR start "NOT_CONNECTIVE"
    public final void mNOT_CONNECTIVE() throws RecognitionException {
        try {
            int _type = NOT_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:12:16: ( '!' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:12:18: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT_CONNECTIVE"

    // $ANTLR start "ARITHMETIC_OPERATOR"
    public final void mARITHMETIC_OPERATOR() throws RecognitionException {
        try {
            int _type = ARITHMETIC_OPERATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:21: ( '<' | '<=' | '>' | '>=' | '==' | '!=' )
            int alt1=6;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='=') ) {
                    alt1=2;
                }
                else {
                    alt1=1;}
                }
                break;
            case '>':
                {
                int LA1_2 = input.LA(2);

                if ( (LA1_2=='=') ) {
                    alt1=4;
                }
                else {
                    alt1=3;}
                }
                break;
            case '=':
                {
                alt1=5;
                }
                break;
            case '!':
                {
                alt1=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:23: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 2 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:29: '<='
                    {
                    match("<="); 


                    }
                    break;
                case 3 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:36: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 4 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:42: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 5 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:49: '=='
                    {
                    match("=="); 


                    }
                    break;
                case 6 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:14:56: '!='
                    {
                    match("!="); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ARITHMETIC_OPERATOR"

    // $ANTLR start "NEGATIVE_SIGN"
    public final void mNEGATIVE_SIGN() throws RecognitionException {
        try {
            int _type = NEGATIVE_SIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:15:15: ( '-' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:15:17: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEGATIVE_SIGN"

    // $ANTLR start "CONSTANT"
    public final void mCONSTANT() throws RecognitionException {
        try {
            int _type = CONSTANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:10: ( ( NEGATIVE_SIGN )? ( DIGIT )+ )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:12: ( NEGATIVE_SIGN )? ( DIGIT )+
            {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:12: ( NEGATIVE_SIGN )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='-') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:13: NEGATIVE_SIGN
                    {
                    mNEGATIVE_SIGN(); 

                    }
                    break;

            }

            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:29: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:16:30: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONSTANT"

    // $ANTLR start "LEFT_PARANTHESIS"
    public final void mLEFT_PARANTHESIS() throws RecognitionException {
        try {
            int _type = LEFT_PARANTHESIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:18:18: ( '(' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:18:20: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFT_PARANTHESIS"

    // $ANTLR start "RIGHT_PARANTHESIS"
    public final void mRIGHT_PARANTHESIS() throws RecognitionException {
        try {
            int _type = RIGHT_PARANTHESIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:19:19: ( ')' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:19:21: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHT_PARANTHESIS"

    // $ANTLR start "ATOMIC_PROPOSITION"
    public final void mATOMIC_PROPOSITION() throws RecognitionException {
        try {
            int _type = ATOMIC_PROPOSITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:20: ( ( CAPS_LETTER )+ ( DIGIT )+ )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:23: ( CAPS_LETTER )+ ( DIGIT )+
            {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:23: ( CAPS_LETTER )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='A' && LA4_0<='Z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:24: CAPS_LETTER
            	    {
            	    mCAPS_LETTER(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:38: ( DIGIT )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:21:39: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ATOMIC_PROPOSITION"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:5: ( ( SMALL_LETTER )+ ( DIGIT )* )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:7: ( SMALL_LETTER )+ ( DIGIT )*
            {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:7: ( SMALL_LETTER )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:8: SMALL_LETTER
            	    {
            	    mSMALL_LETTER(); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:23: ( DIGIT )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:22:24: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "CAPS_LETTER"
    public final void mCAPS_LETTER() throws RecognitionException {
        try {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:26:3: ( 'A' .. 'Z' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:26:5: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "CAPS_LETTER"

    // $ANTLR start "SMALL_LETTER"
    public final void mSMALL_LETTER() throws RecognitionException {
        try {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:29:3: ( 'a' .. 'z' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:29:5: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "SMALL_LETTER"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:32:3: ( SMALL_LETTER | CAPS_LETTER )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:36:3: ( '0' .. '9' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:36:5: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "NOTHING"
    public final void mNOTHING() throws RecognitionException {
        try {
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:39:3: ( '#' )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:39:5: '#'
            {
            match('#'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NOTHING"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:41:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:41:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=99;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:8: ( PROPOSITIONAL_CONNECTIVE | NOT_CONNECTIVE | ARITHMETIC_OPERATOR | NEGATIVE_SIGN | CONSTANT | LEFT_PARANTHESIS | RIGHT_PARANTHESIS | ATOMIC_PROPOSITION | VAR | WS )
        int alt8=10;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:10: PROPOSITIONAL_CONNECTIVE
                {
                mPROPOSITIONAL_CONNECTIVE(); 

                }
                break;
            case 2 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:35: NOT_CONNECTIVE
                {
                mNOT_CONNECTIVE(); 

                }
                break;
            case 3 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:50: ARITHMETIC_OPERATOR
                {
                mARITHMETIC_OPERATOR(); 

                }
                break;
            case 4 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:70: NEGATIVE_SIGN
                {
                mNEGATIVE_SIGN(); 

                }
                break;
            case 5 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:84: CONSTANT
                {
                mCONSTANT(); 

                }
                break;
            case 6 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:93: LEFT_PARANTHESIS
                {
                mLEFT_PARANTHESIS(); 

                }
                break;
            case 7 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:110: RIGHT_PARANTHESIS
                {
                mRIGHT_PARANTHESIS(); 

                }
                break;
            case 8 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:128: ATOMIC_PROPOSITION
                {
                mATOMIC_PROPOSITION(); 

                }
                break;
            case 9 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:147: VAR
                {
                mVAR(); 

                }
                break;
            case 10 :
                // E:\\eclipse-SDK-3.3.1-win32\\workspace\\EshmunImplementationv3.0\\antlrsrc\\eshmun\\parser\\antlr\\PredFormulaLexer.g:1:151: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\2\uffff\1\13\1\uffff\1\14\10\uffff";
    static final String DFA8_eofS =
        "\15\uffff";
    static final String DFA8_minS =
        "\1\11\1\uffff\1\75\1\uffff\1\60\10\uffff";
    static final String DFA8_maxS =
        "\1\174\1\uffff\1\75\1\uffff\1\71\10\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\uffff\1\5\1\6\1\7\1\10\1\11\1\12\1\2"+
        "\1\4";
    static final String DFA8_specialS =
        "\15\uffff}>";
    static final String[] DFA8_transitionS = {
            "\2\12\1\uffff\2\12\22\uffff\1\12\1\2\4\uffff\1\1\1\uffff\1"+
            "\6\1\7\3\uffff\1\4\2\uffff\12\5\2\uffff\3\3\2\uffff\32\10\6"+
            "\uffff\32\11\1\uffff\1\1",
            "",
            "\1\3",
            "",
            "\12\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( PROPOSITIONAL_CONNECTIVE | NOT_CONNECTIVE | ARITHMETIC_OPERATOR | NEGATIVE_SIGN | CONSTANT | LEFT_PARANTHESIS | RIGHT_PARANTHESIS | ATOMIC_PROPOSITION | VAR | WS );";
        }
    }
 

}