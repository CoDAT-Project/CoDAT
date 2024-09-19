// $ANTLR 3.4 C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g 2013-07-20 12:13:17

  package eshmun.parser.antlr.lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SpecLexer extends Lexer {
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

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public SpecLexer() {} 
    public SpecLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SpecLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g"; }

    // $ANTLR start "PROPOSITIONAL_CONNECTIVE"
    public final void mPROPOSITIONAL_CONNECTIVE() throws RecognitionException {
        try {
            int _type = PROPOSITIONAL_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:13:26: ( '|' | '&' | '=>' | '<=>' )
            int alt1=4;
            switch ( input.LA(1) ) {
            case '|':
                {
                alt1=1;
                }
                break;
            case '&':
                {
                alt1=2;
                }
                break;
            case '=':
                {
                alt1=3;
                }
                break;
            case '<':
                {
                alt1=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:13:28: '|'
                    {
                    match('|'); 

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:13:34: '&'
                    {
                    match('&'); 

                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:13:40: '=>'
                    {
                    match("=>"); 



                    }
                    break;
                case 4 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:13:47: '<=>'
                    {
                    match("<=>"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PROPOSITIONAL_CONNECTIVE"

    // $ANTLR start "NOT_CONNECTIVE"
    public final void mNOT_CONNECTIVE() throws RecognitionException {
        try {
            int _type = NOT_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:14:16: ( '!' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:14:18: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOT_CONNECTIVE"

    // $ANTLR start "COMPARISON_OPERATOR"
    public final void mCOMPARISON_OPERATOR() throws RecognitionException {
        try {
            int _type = COMPARISON_OPERATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:21: ( '<' | '<=' | '>' | '>=' | '==' | '!=' )
            int alt2=6;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='=') ) {
                    alt2=2;
                }
                else {
                    alt2=1;
                }
                }
                break;
            case '>':
                {
                int LA2_2 = input.LA(2);

                if ( (LA2_2=='=') ) {
                    alt2=4;
                }
                else {
                    alt2=3;
                }
                }
                break;
            case '=':
                {
                alt2=5;
                }
                break;
            case '!':
                {
                alt2=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:23: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:29: '<='
                    {
                    match("<="); 



                    }
                    break;
                case 3 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:36: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 4 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:42: '>='
                    {
                    match(">="); 



                    }
                    break;
                case 5 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:49: '=='
                    {
                    match("=="); 



                    }
                    break;
                case 6 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:18:56: '!='
                    {
                    match("!="); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMPARISON_OPERATOR"

    // $ANTLR start "NEGATIVE_SIGN"
    public final void mNEGATIVE_SIGN() throws RecognitionException {
        try {
            int _type = NEGATIVE_SIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:19:15: ( '-' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:19:17: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEGATIVE_SIGN"

    // $ANTLR start "CONSTANT"
    public final void mCONSTANT() throws RecognitionException {
        try {
            int _type = CONSTANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:20:10: ( ( NEGATIVE_SIGN )? ( DIGIT )+ )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:20:12: ( NEGATIVE_SIGN )? ( DIGIT )+
            {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:20:12: ( NEGATIVE_SIGN )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
                    {
                    if ( input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:20:29: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CONSTANT"

    // $ANTLR start "LEFT_PARANTHESIS"
    public final void mLEFT_PARANTHESIS() throws RecognitionException {
        try {
            int _type = LEFT_PARANTHESIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:23:18: ( '(' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:23:20: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEFT_PARANTHESIS"

    // $ANTLR start "RIGHT_PARANTHESIS"
    public final void mRIGHT_PARANTHESIS() throws RecognitionException {
        try {
            int _type = RIGHT_PARANTHESIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:24:19: ( ')' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:24:21: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RIGHT_PARANTHESIS"

    // $ANTLR start "LEFT_BRACKET"
    public final void mLEFT_BRACKET() throws RecognitionException {
        try {
            int _type = LEFT_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:25:14: ( '[' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:25:16: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEFT_BRACKET"

    // $ANTLR start "RIGHT_BRACKET"
    public final void mRIGHT_BRACKET() throws RecognitionException {
        try {
            int _type = RIGHT_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:26:15: ( ']' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:26:17: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RIGHT_BRACKET"

    // $ANTLR start "CTL_BRANCH_OP"
    public final void mCTL_BRANCH_OP() throws RecognitionException {
        try {
            int _type = CTL_BRANCH_OP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:29:15: ( 'A' | 'E' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( input.LA(1)=='A'||input.LA(1)=='E' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CTL_BRANCH_OP"

    // $ANTLR start "CTL_PATH_CONNECTIVE"
    public final void mCTL_PATH_CONNECTIVE() throws RecognitionException {
        try {
            int _type = CTL_PATH_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:30:21: ( 'U' | 'V' | 'W' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= 'U' && input.LA(1) <= 'W') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CTL_PATH_CONNECTIVE"

    // $ANTLR start "CTL_PATH_OP"
    public final void mCTL_PATH_OP() throws RecognitionException {
        try {
            int _type = CTL_PATH_OP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:31:13: ( ( 'X' | 'F' | 'G' ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= 'F' && input.LA(1) <= 'G')||input.LA(1)=='X' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CTL_PATH_OP"

    // $ANTLR start "MPCTL_CONNECTIVE"
    public final void mMPCTL_CONNECTIVE() throws RecognitionException {
        try {
            int _type = MPCTL_CONNECTIVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:33:17: ( 'AND' ( SMALL_LETTER )+ | 'OR' ( SMALL_LETTER )+ )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='A') ) {
                alt7=1;
            }
            else if ( (LA7_0=='O') ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:33:19: 'AND' ( SMALL_LETTER )+
                    {
                    match("AND"); 



                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:33:25: ( SMALL_LETTER )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
                    	    {
                    	    if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


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
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:33:43: 'OR' ( SMALL_LETTER )+
                    {
                    match("OR"); 



                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:33:48: ( SMALL_LETTER )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0 >= 'a' && LA6_0 <= 'z')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
                    	    {
                    	    if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


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


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MPCTL_CONNECTIVE"

    // $ANTLR start "BOOLEAN"
    public final void mBOOLEAN() throws RecognitionException {
        try {
            int _type = BOOLEAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:35:9: ( 'true' | 'false' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='t') ) {
                alt8=1;
            }
            else if ( (LA8_0=='f') ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:35:11: 'true'
                    {
                    match("true"); 



                    }
                    break;
                case 2 :
                    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:35:20: 'false'
                    {
                    match("false"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BOOLEAN"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:36:8: ( '\\'' ( LETTER | DIGIT )* '\\'' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:36:10: '\\'' ( LETTER | DIGIT )* '\\''
            {
            match('\''); 

            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:36:15: ( LETTER | DIGIT )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= '0' && LA9_0 <= '9')||(LA9_0 >= 'A' && LA9_0 <= 'Z')||(LA9_0 >= 'a' && LA9_0 <= 'z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:40:5: ( ( LETTER | DIGIT | '_' | '.' )+ )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:40:7: ( LETTER | DIGIT | '_' | '.' )+
            {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:40:7: ( LETTER | DIGIT | '_' | '.' )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='.'||(LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= 'A' && LA10_0 <= 'Z')||LA10_0=='_'||(LA10_0 >= 'a' && LA10_0 <= 'z')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "CAPS_LETTER"
    public final void mCAPS_LETTER() throws RecognitionException {
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:44:3: ( 'A' .. 'Z' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CAPS_LETTER"

    // $ANTLR start "SMALL_LETTER"
    public final void mSMALL_LETTER() throws RecognitionException {
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:47:3: ( 'a' .. 'z' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SMALL_LETTER"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:50:3: ( SMALL_LETTER | CAPS_LETTER )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:54:3: ( '0' .. '9' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "NOTHING"
    public final void mNOTHING() throws RecognitionException {
        try {
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:57:3: ( '#' )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:57:5: '#'
            {
            match('#'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOTHING"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:59:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:59:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=99;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:8: ( PROPOSITIONAL_CONNECTIVE | NOT_CONNECTIVE | COMPARISON_OPERATOR | NEGATIVE_SIGN | CONSTANT | LEFT_PARANTHESIS | RIGHT_PARANTHESIS | LEFT_BRACKET | RIGHT_BRACKET | CTL_BRANCH_OP | CTL_PATH_CONNECTIVE | CTL_PATH_OP | MPCTL_CONNECTIVE | BOOLEAN | STRING | VAR | WS )
        int alt11=17;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:10: PROPOSITIONAL_CONNECTIVE
                {
                mPROPOSITIONAL_CONNECTIVE(); 


                }
                break;
            case 2 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:35: NOT_CONNECTIVE
                {
                mNOT_CONNECTIVE(); 


                }
                break;
            case 3 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:50: COMPARISON_OPERATOR
                {
                mCOMPARISON_OPERATOR(); 


                }
                break;
            case 4 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:70: NEGATIVE_SIGN
                {
                mNEGATIVE_SIGN(); 


                }
                break;
            case 5 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:84: CONSTANT
                {
                mCONSTANT(); 


                }
                break;
            case 6 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:93: LEFT_PARANTHESIS
                {
                mLEFT_PARANTHESIS(); 


                }
                break;
            case 7 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:110: RIGHT_PARANTHESIS
                {
                mRIGHT_PARANTHESIS(); 


                }
                break;
            case 8 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:128: LEFT_BRACKET
                {
                mLEFT_BRACKET(); 


                }
                break;
            case 9 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:141: RIGHT_BRACKET
                {
                mRIGHT_BRACKET(); 


                }
                break;
            case 10 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:155: CTL_BRANCH_OP
                {
                mCTL_BRANCH_OP(); 


                }
                break;
            case 11 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:169: CTL_PATH_CONNECTIVE
                {
                mCTL_PATH_CONNECTIVE(); 


                }
                break;
            case 12 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:189: CTL_PATH_OP
                {
                mCTL_PATH_OP(); 


                }
                break;
            case 13 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:201: MPCTL_CONNECTIVE
                {
                mMPCTL_CONNECTIVE(); 


                }
                break;
            case 14 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:218: BOOLEAN
                {
                mBOOLEAN(); 


                }
                break;
            case 15 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:226: STRING
                {
                mSTRING(); 


                }
                break;
            case 16 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:233: VAR
                {
                mVAR(); 


                }
                break;
            case 17 :
                // C:\\Univ\\Thesis\\ANTLRTest\\SpecLexer.g:1:237: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA11_eotS =
        "\3\uffff\1\5\1\27\1\uffff\1\30\1\31\4\uffff\1\33\1\34\1\35\1\33"+
        "\3\24\3\uffff\1\5\3\uffff\1\24\3\uffff\4\24\1\46\2\24\1\46\1\uffff"+
        "\1\51\1\24\1\uffff\1\51";
    static final String DFA11_eofS =
        "\53\uffff";
    static final String DFA11_minS =
        "\1\11\1\uffff\3\75\1\uffff\1\60\1\56\4\uffff\4\56\1\122\1\162\1"+
        "\141\3\uffff\1\76\3\uffff\1\104\3\uffff\1\141\1\165\1\154\1\141"+
        "\1\56\1\145\1\163\1\56\1\uffff\1\56\1\145\1\uffff\1\56";
    static final String DFA11_maxS =
        "\1\174\1\uffff\1\76\2\75\1\uffff\1\71\1\172\4\uffff\4\172\1\122"+
        "\1\162\1\141\3\uffff\1\76\3\uffff\1\104\3\uffff\1\172\1\165\1\154"+
        "\2\172\1\145\1\163\1\172\1\uffff\1\172\1\145\1\uffff\1\172";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\3\uffff\1\3\2\uffff\1\6\1\7\1\10\1\11\7\uffff\1\17"+
        "\1\20\1\21\1\uffff\1\2\1\4\1\5\1\uffff\1\12\1\13\1\14\10\uffff\1"+
        "\15\2\uffff\1\16\1\uffff";
    static final String DFA11_specialS =
        "\53\uffff}>";
    static final String[] DFA11_transitionS = {
            "\2\25\1\uffff\2\25\22\uffff\1\25\1\4\4\uffff\1\1\1\23\1\10\1"+
            "\11\3\uffff\1\6\1\24\1\uffff\12\7\2\uffff\1\3\1\2\1\5\2\uffff"+
            "\1\14\3\24\1\17\2\16\7\24\1\20\5\24\3\15\1\16\2\24\1\12\1\uffff"+
            "\1\13\1\uffff\1\24\1\uffff\5\24\1\22\15\24\1\21\6\24\1\uffff"+
            "\1\1",
            "",
            "\1\5\1\1",
            "\1\26",
            "\1\5",
            "",
            "\12\31",
            "\1\24\1\uffff\12\7\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "",
            "",
            "",
            "\1\24\1\uffff\12\24\7\uffff\15\24\1\32\14\24\4\uffff\1\24\1"+
            "\uffff\32\24",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\36",
            "\1\37",
            "\1\40",
            "",
            "",
            "",
            "\1\1",
            "",
            "",
            "",
            "\1\41",
            "",
            "",
            "",
            "\32\42",
            "\1\43",
            "\1\44",
            "\32\45",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\42",
            "\1\47",
            "\1\50",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\45",
            "",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\52",
            "",
            "\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24"
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( PROPOSITIONAL_CONNECTIVE | NOT_CONNECTIVE | COMPARISON_OPERATOR | NEGATIVE_SIGN | CONSTANT | LEFT_PARANTHESIS | RIGHT_PARANTHESIS | LEFT_BRACKET | RIGHT_BRACKET | CTL_BRANCH_OP | CTL_PATH_CONNECTIVE | CTL_PATH_OP | MPCTL_CONNECTIVE | BOOLEAN | STRING | VAR | WS );";
        }
    }
 

}