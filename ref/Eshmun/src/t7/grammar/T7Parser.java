// Generated from T7.g4 by ANTLR 4.4
package t7.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class T7Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LINE=1, COMMENT=2, TRUE=3, FALSE=4, GT=5, GE=6, LT=7, LE=8, BQ=9, NQ=10, 
		AND=11, OR=12, NOT=13, PLUS=14, MINUS=15, MULT=16, DIV=17, MOD=18, POW=19, 
		CLEAR=20, LET=21, INSTANCE=22, DEFINEO=23, DEFINE=24, FOR=25, WHILE=26, 
		IF=27, ELSE=28, END=29, IN=30, LOAD=31, EXPORTO=32, EXPORT=33, START=34, 
		LIST=35, DEL=36, EXTRACT=37, SEARCH=38, STATES=39, TRANSITIONS=40, SPECIFICATIONS=41, 
		EQ=42, TP=43, SEMI=44, COMMA=45, LP=46, RP=47, LC=48, RC=49, LS=50, RS=51, 
		DOT=52, SINGLEAND=53, SINGLECOL=54, ID=55, INT=56, WS=57, ANYTHING_ELSE=58;
	public static final String[] tokenNames = {
		"<INVALID>", "LINE", "'//'", "'true'", "'false'", "'>'", "'>='", "'<'", 
		"'<='", "'=='", "'!='", "'&&'", "'||'", "'!'", "'+'", "'-'", "'*'", "'/'", 
		"'%'", "'^'", "'clear'", "'let'", "'instance'", "'@define'", "'define'", 
		"'for'", "'while'", "'if'", "'else'", "'end'", "'in'", "'load'", "'@export'", 
		"'export'", "'start'", "'list'", "'del'", "'extract'", "'search'", "'states:'", 
		"'transitions:'", "'specifications:'", "'='", "':'", "';'", "','", "'('", 
		"')'", "'{'", "'}'", "'['", "']'", "'.'", "'&'", "'|'", "ID", "INT", "WS", 
		"ANYTHING_ELSE"
	};
	public static final int
		RULE_statement = 0, RULE_var_definition = 1, RULE_instantiation = 2, RULE_structure_definition = 3, 
		RULE_clear = 4, RULE_load = 5, RULE_export = 6, RULE_start = 7, RULE_list = 8, 
		RULE_search = 9, RULE_del = 10, RULE_extract = 11, RULE_header = 12, RULE_for_loop = 13, 
		RULE_while_loop = 14, RULE_if_cond = 15, RULE_code_body = 16, RULE_else_cond = 17, 
		RULE_if_body = 18, RULE_condition = 19, RULE_definition_body = 20, RULE_states_def = 21, 
		RULE_transitions_def = 22, RULE_specifications_def = 23, RULE_lines = 24, 
		RULE_state_def = 25, RULE_transition_def = 26, RULE_def_term = 27, RULE_identifier = 28, 
		RULE_range = 29, RULE_arith_exp = 30, RULE_arith_exp1 = 31, RULE_arith_exp2 = 32, 
		RULE_arith_exp3 = 33, RULE_atomic_arith_exp = 34, RULE_bool_exp = 35, 
		RULE_bool_exp1 = 36, RULE_bool_exp2 = 37, RULE_bool_exp3 = 38, RULE_atomic_bool_exp = 39, 
		RULE_bool_arith_exp = 40, RULE_bool_op = 41, RULE_value = 42, RULE_triplet = 43, 
		RULE_triplet_term = 44, RULE_comment = 45, RULE_new_line = 46, RULE_any = 47;
	public static final String[] ruleNames = {
		"statement", "var_definition", "instantiation", "structure_definition", 
		"clear", "load", "export", "start", "list", "search", "del", "extract", 
		"header", "for_loop", "while_loop", "if_cond", "code_body", "else_cond", 
		"if_body", "condition", "definition_body", "states_def", "transitions_def", 
		"specifications_def", "lines", "state_def", "transition_def", "def_term", 
		"identifier", "range", "arith_exp", "arith_exp1", "arith_exp2", "arith_exp3", 
		"atomic_arith_exp", "bool_exp", "bool_exp1", "bool_exp2", "bool_exp3", 
		"atomic_bool_exp", "bool_arith_exp", "bool_op", "value", "triplet", "triplet_term", 
		"comment", "new_line", "any"
	};

	@Override
	public String getGrammarFileName() { return "T7.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public T7Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StatementContext extends ParserRuleContext {
		public While_loopContext while_loop() {
			return getRuleContext(While_loopContext.class,0);
		}
		public List<CommentContext> comment() {
			return getRuleContexts(CommentContext.class);
		}
		public InstantiationContext instantiation() {
			return getRuleContext(InstantiationContext.class,0);
		}
		public StartContext start() {
			return getRuleContext(StartContext.class,0);
		}
		public Structure_definitionContext structure_definition() {
			return getRuleContext(Structure_definitionContext.class,0);
		}
		public ClearContext clear() {
			return getRuleContext(ClearContext.class,0);
		}
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public ExtractContext extract() {
			return getRuleContext(ExtractContext.class,0);
		}
		public New_lineContext new_line() {
			return getRuleContext(New_lineContext.class,0);
		}
		public Var_definitionContext var_definition() {
			return getRuleContext(Var_definitionContext.class,0);
		}
		public CommentContext comment(int i) {
			return getRuleContext(CommentContext.class,i);
		}
		public ExportContext export() {
			return getRuleContext(ExportContext.class,0);
		}
		public DelContext del() {
			return getRuleContext(DelContext.class,0);
		}
		public SearchContext search() {
			return getRuleContext(SearchContext.class,0);
		}
		public For_loopContext for_loop() {
			return getRuleContext(For_loopContext.class,0);
		}
		public If_condContext if_cond() {
			return getRuleContext(If_condContext.class,0);
		}
		public LoadContext load() {
			return getRuleContext(LoadContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			switch (_input.LA(1)) {
			case DEFINEO:
			case DEFINE:
				{
				setState(96); structure_definition();
				}
				break;
			case LET:
			case ID:
				{
				setState(97); var_definition();
				}
				break;
			case INSTANCE:
				{
				setState(98); instantiation();
				}
				break;
			case COMMENT:
				{
				setState(99); comment();
				}
				break;
			case CLEAR:
				{
				setState(100); clear();
				}
				break;
			case FOR:
				{
				setState(101); for_loop();
				}
				break;
			case WHILE:
				{
				setState(102); while_loop();
				}
				break;
			case IF:
				{
				setState(103); if_cond();
				}
				break;
			case LOAD:
				{
				setState(104); load();
				}
				break;
			case EXPORTO:
			case EXPORT:
				{
				setState(105); export();
				}
				break;
			case START:
				{
				setState(106); start();
				}
				break;
			case LIST:
				{
				setState(107); list();
				}
				break;
			case DEL:
				{
				setState(108); del();
				}
				break;
			case SEARCH:
				{
				setState(109); search();
				}
				break;
			case EXTRACT:
				{
				setState(110); extract();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(115);
			switch (_input.LA(1)) {
			case COMMENT:
				{
				setState(113); comment();
				}
				break;
			case LINE:
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(117); new_line();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_definitionContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode EQ() { return getToken(T7Parser.EQ, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LET() { return getToken(T7Parser.LET, 0); }
		public Var_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterVar_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitVar_definition(this);
		}
	}

	public final Var_definitionContext var_definition() throws RecognitionException {
		Var_definitionContext _localctx = new Var_definitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_var_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			switch (_input.LA(1)) {
			case LET:
				{
				setState(119); match(LET);
				}
				break;
			case ID:
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(123); identifier();
			setState(124); match(EQ);
			setState(125); value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstantiationContext extends ParserRuleContext {
		public TerminalNode INSTANCE() { return getToken(T7Parser.INSTANCE, 0); }
		public RangeContext range(int i) {
			return getRuleContext(RangeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(T7Parser.COMMA); }
		public TerminalNode LT() { return getToken(T7Parser.LT, 0); }
		public TerminalNode GT() { return getToken(T7Parser.GT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<RangeContext> range() {
			return getRuleContexts(RangeContext.class);
		}
		public TerminalNode COMMA(int i) {
			return getToken(T7Parser.COMMA, i);
		}
		public InstantiationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instantiation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterInstantiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitInstantiation(this);
		}
	}

	public final InstantiationContext instantiation() throws RecognitionException {
		InstantiationContext _localctx = new InstantiationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_instantiation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127); match(INSTANCE);
			setState(128); identifier();
			setState(129); match(LT);
			setState(130); range();
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(131); match(COMMA);
				setState(132); range();
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138); match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Structure_definitionContext extends ParserRuleContext {
		public List<LinesContext> lines() {
			return getRuleContexts(LinesContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LT() { return getToken(T7Parser.LT, 0); }
		public TerminalNode DEFINEO() { return getToken(T7Parser.DEFINEO, 0); }
		public TerminalNode GT() { return getToken(T7Parser.GT, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public TerminalNode COMMA(int i) {
			return getToken(T7Parser.COMMA, i);
		}
		public LinesContext lines(int i) {
			return getRuleContext(LinesContext.class,i);
		}
		public TerminalNode DEFINE() { return getToken(T7Parser.DEFINE, 0); }
		public TerminalNode RC() { return getToken(T7Parser.RC, 0); }
		public TerminalNode LC() { return getToken(T7Parser.LC, 0); }
		public List<TerminalNode> COMMA() { return getTokens(T7Parser.COMMA); }
		public Definition_bodyContext definition_body() {
			return getRuleContext(Definition_bodyContext.class,0);
		}
		public Structure_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structure_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterStructure_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitStructure_definition(this);
		}
	}

	public final Structure_definitionContext structure_definition() throws RecognitionException {
		Structure_definitionContext _localctx = new Structure_definitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_structure_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			_la = _input.LA(1);
			if ( !(_la==DEFINEO || _la==DEFINE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(141); identifier();
			setState(142); match(LT);
			setState(143); identifier();
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(144); match(COMMA);
				setState(145); identifier();
				}
				}
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(151); match(GT);
			setState(152); lines();
			setState(153); match(LC);
			setState(154); lines();
			setState(155); definition_body();
			setState(156); match(RC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClearContext extends ParserRuleContext {
		public TerminalNode CLEAR() { return getToken(T7Parser.CLEAR, 0); }
		public ClearContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clear; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterClear(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitClear(this);
		}
	}

	public final ClearContext clear() throws RecognitionException {
		ClearContext _localctx = new ClearContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_clear);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158); match(CLEAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadContext extends ParserRuleContext {
		public TerminalNode LOAD() { return getToken(T7Parser.LOAD, 0); }
		public AnyContext any() {
			return getRuleContext(AnyContext.class,0);
		}
		public LoadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_load; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterLoad(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitLoad(this);
		}
	}

	public final LoadContext load() throws RecognitionException {
		LoadContext _localctx = new LoadContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_load);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160); match(LOAD);
			setState(161); any();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExportContext extends ParserRuleContext {
		public TerminalNode EXPORTO() { return getToken(T7Parser.EXPORTO, 0); }
		public AnyContext any() {
			return getRuleContext(AnyContext.class,0);
		}
		public TerminalNode EXPORT() { return getToken(T7Parser.EXPORT, 0); }
		public ExportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_export; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterExport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitExport(this);
		}
	}

	public final ExportContext export() throws RecognitionException {
		ExportContext _localctx = new ExportContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_export);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_la = _input.LA(1);
			if ( !(_la==EXPORTO || _la==EXPORT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(164); any();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StartContext extends ParserRuleContext {
		public TerminalNode START() { return getToken(T7Parser.START, 0); }
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166); match(START);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListContext extends ParserRuleContext {
		public TerminalNode LIST() { return getToken(T7Parser.LIST, 0); }
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitList(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_list);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168); match(LIST);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchContext extends ParserRuleContext {
		public TerminalNode SEARCH() { return getToken(T7Parser.SEARCH, 0); }
		public AnyContext any() {
			return getRuleContext(AnyContext.class,0);
		}
		public SearchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_search; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterSearch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitSearch(this);
		}
	}

	public final SearchContext search() throws RecognitionException {
		SearchContext _localctx = new SearchContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_search);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170); match(SEARCH);
			setState(171); any();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DelContext extends ParserRuleContext {
		public HeaderContext header(int i) {
			return getRuleContext(HeaderContext.class,i);
		}
		public TerminalNode DEL() { return getToken(T7Parser.DEL, 0); }
		public List<HeaderContext> header() {
			return getRuleContexts(HeaderContext.class);
		}
		public DelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_del; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterDel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitDel(this);
		}
	}

	public final DelContext del() throws RecognitionException {
		DelContext _localctx = new DelContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_del);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173); match(DEL);
			setState(175); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(174); header();
				}
				}
				setState(177); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExtractContext extends ParserRuleContext {
		public HeaderContext header(int i) {
			return getRuleContext(HeaderContext.class,i);
		}
		public TerminalNode EXTRACT() { return getToken(T7Parser.EXTRACT, 0); }
		public List<HeaderContext> header() {
			return getRuleContexts(HeaderContext.class);
		}
		public ExtractContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extract; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterExtract(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitExtract(this);
		}
	}

	public final ExtractContext extract() throws RecognitionException {
		ExtractContext _localctx = new ExtractContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_extract);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179); match(EXTRACT);
			setState(181); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(180); header();
				}
				}
				setState(183); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public TerminalNode INT(int i) {
			return getToken(T7Parser.INT, i);
		}
		public TerminalNode RP() { return getToken(T7Parser.RP, 0); }
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LP() { return getToken(T7Parser.LP, 0); }
		public List<TerminalNode> COMMA() { return getTokens(T7Parser.COMMA); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public List<TerminalNode> INT() { return getTokens(T7Parser.INT); }
		public TerminalNode COMMA(int i) {
			return getToken(T7Parser.COMMA, i);
		}
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitHeader(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185); identifier();
			setState(186); match(LP);
			setState(189);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(187); identifier();
				}
				break;
			case INT:
				{
				setState(188); match(INT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(191); match(COMMA);
				setState(194);
				switch (_input.LA(1)) {
				case ID:
					{
					setState(192); identifier();
					}
					break;
				case INT:
					{
					setState(193); match(INT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(201); match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_loopContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(T7Parser.FOR, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public TerminalNode IN() { return getToken(T7Parser.IN, 0); }
		public Code_bodyContext code_body() {
			return getRuleContext(Code_bodyContext.class,0);
		}
		public For_loopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_loop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterFor_loop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitFor_loop(this);
		}
	}

	public final For_loopContext for_loop() throws RecognitionException {
		For_loopContext _localctx = new For_loopContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_for_loop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203); match(FOR);
			setState(204); identifier();
			setState(205); match(IN);
			setState(206); range();
			setState(207); code_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class While_loopContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(T7Parser.WHILE, 0); }
		public New_lineContext new_line() {
			return getRuleContext(New_lineContext.class,0);
		}
		public Code_bodyContext code_body() {
			return getRuleContext(Code_bodyContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public While_loopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_loop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterWhile_loop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitWhile_loop(this);
		}
	}

	public final While_loopContext while_loop() throws RecognitionException {
		While_loopContext _localctx = new While_loopContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_while_loop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209); match(WHILE);
			setState(210); condition();
			setState(211); new_line();
			setState(212); code_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_condContext extends ParserRuleContext {
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public TerminalNode IF() { return getToken(T7Parser.IF, 0); }
		public If_bodyContext if_body() {
			return getRuleContext(If_bodyContext.class,0);
		}
		public TerminalNode END() { return getToken(T7Parser.END, 0); }
		public Else_condContext else_cond() {
			return getRuleContext(Else_condContext.class,0);
		}
		public New_lineContext new_line() {
			return getRuleContext(New_lineContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public If_condContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterIf_cond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitIf_cond(this);
		}
	}

	public final If_condContext if_cond() throws RecognitionException {
		If_condContext _localctx = new If_condContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_if_cond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214); match(IF);
			setState(215); condition();
			setState(216); new_line();
			setState(217); lines();
			setState(218); if_body();
			setState(221);
			switch (_input.LA(1)) {
			case ELSE:
				{
				setState(219); else_cond();
				}
				break;
			case END:
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(223); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Code_bodyContext extends ParserRuleContext {
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public TerminalNode END() { return getToken(T7Parser.END, 0); }
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public Code_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterCode_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitCode_body(this);
		}
	}

	public final Code_bodyContext code_body() throws RecognitionException {
		Code_bodyContext _localctx = new Code_bodyContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_code_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225); lines();
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COMMENT) | (1L << CLEAR) | (1L << LET) | (1L << INSTANCE) | (1L << DEFINEO) | (1L << DEFINE) | (1L << FOR) | (1L << WHILE) | (1L << IF) | (1L << LOAD) | (1L << EXPORTO) | (1L << EXPORT) | (1L << START) | (1L << LIST) | (1L << DEL) | (1L << EXTRACT) | (1L << SEARCH) | (1L << ID))) != 0)) {
				{
				{
				setState(226); statement();
				}
				}
				setState(231);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(232); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Else_condContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(T7Parser.ELSE, 0); }
		public If_bodyContext if_body() {
			return getRuleContext(If_bodyContext.class,0);
		}
		public New_lineContext new_line() {
			return getRuleContext(New_lineContext.class,0);
		}
		public Else_condContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterElse_cond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitElse_cond(this);
		}
	}

	public final Else_condContext else_cond() throws RecognitionException {
		Else_condContext _localctx = new Else_condContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_else_cond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234); match(ELSE);
			setState(237);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(235); new_line();
				}
				break;
			case 2:
				{
				}
				break;
			}
			setState(239); if_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_bodyContext extends ParserRuleContext {
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public If_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterIf_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitIf_body(this);
		}
	}

	public final If_bodyContext if_body() throws RecognitionException {
		If_bodyContext _localctx = new If_bodyContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_if_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241); lines();
			setState(245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COMMENT) | (1L << CLEAR) | (1L << LET) | (1L << INSTANCE) | (1L << DEFINEO) | (1L << DEFINE) | (1L << FOR) | (1L << WHILE) | (1L << IF) | (1L << LOAD) | (1L << EXPORTO) | (1L << EXPORT) | (1L << START) | (1L << LIST) | (1L << DEL) | (1L << EXTRACT) | (1L << SEARCH) | (1L << ID))) != 0)) {
				{
				{
				setState(242); statement();
				}
				}
				setState(247);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_condition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248); bool_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Definition_bodyContext extends ParserRuleContext {
		public States_defContext states_def() {
			return getRuleContext(States_defContext.class,0);
		}
		public Specifications_defContext specifications_def() {
			return getRuleContext(Specifications_defContext.class,0);
		}
		public Transitions_defContext transitions_def() {
			return getRuleContext(Transitions_defContext.class,0);
		}
		public Definition_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterDefinition_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitDefinition_body(this);
		}
	}

	public final Definition_bodyContext definition_body() throws RecognitionException {
		Definition_bodyContext _localctx = new Definition_bodyContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_definition_body);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250); states_def();
			setState(251); transitions_def();
			setState(252); specifications_def();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class States_defContext extends ParserRuleContext {
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public List<State_defContext> state_def() {
			return getRuleContexts(State_defContext.class);
		}
		public State_defContext state_def(int i) {
			return getRuleContext(State_defContext.class,i);
		}
		public TerminalNode STATES() { return getToken(T7Parser.STATES, 0); }
		public States_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_states_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterStates_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitStates_def(this);
		}
	}

	public final States_defContext states_def() throws RecognitionException {
		States_defContext _localctx = new States_defContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_states_def);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(254); match(STATES);
			setState(255); lines();
			setState(259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(256); state_def();
					}
					} 
				}
				setState(261);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Transitions_defContext extends ParserRuleContext {
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public List<Transition_defContext> transition_def() {
			return getRuleContexts(Transition_defContext.class);
		}
		public Transition_defContext transition_def(int i) {
			return getRuleContext(Transition_defContext.class,i);
		}
		public TerminalNode TRANSITIONS() { return getToken(T7Parser.TRANSITIONS, 0); }
		public Transitions_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transitions_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterTransitions_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitTransitions_def(this);
		}
	}

	public final Transitions_defContext transitions_def() throws RecognitionException {
		Transitions_defContext _localctx = new Transitions_defContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_transitions_def);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(262); match(TRANSITIONS);
			setState(263); lines();
			setState(267);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(264); transition_def();
					}
					} 
				}
				setState(269);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Specifications_defContext extends ParserRuleContext {
		public List<LinesContext> lines() {
			return getRuleContexts(LinesContext.class);
		}
		public LinesContext lines(int i) {
			return getRuleContext(LinesContext.class,i);
		}
		public List<TerminalNode> RC() { return getTokens(T7Parser.RC); }
		public TerminalNode SPECIFICATIONS() { return getToken(T7Parser.SPECIFICATIONS, 0); }
		public TerminalNode RC(int i) {
			return getToken(T7Parser.RC, i);
		}
		public Specifications_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specifications_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterSpecifications_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitSpecifications_def(this);
		}
	}

	public final Specifications_defContext specifications_def() throws RecognitionException {
		Specifications_defContext _localctx = new Specifications_defContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_specifications_def);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(270); match(SPECIFICATIONS);
			setState(271); lines();
			setState(275);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(272);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==RC) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					} 
				}
				setState(277);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
			setState(278); lines();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LinesContext extends ParserRuleContext {
		public New_lineContext new_line(int i) {
			return getRuleContext(New_lineContext.class,i);
		}
		public List<New_lineContext> new_line() {
			return getRuleContexts(New_lineContext.class);
		}
		public LinesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lines; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterLines(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitLines(this);
		}
	}

	public final LinesContext lines() throws RecognitionException {
		LinesContext _localctx = new LinesContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_lines);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(280); new_line();
					}
					} 
				}
				setState(285);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_defContext extends ParserRuleContext {
		public List<TerminalNode> TP() { return getTokens(T7Parser.TP); }
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public List<Def_termContext> def_term() {
			return getRuleContexts(Def_termContext.class);
		}
		public TerminalNode SEMI() { return getToken(T7Parser.SEMI, 0); }
		public TerminalNode TP(int i) {
			return getToken(T7Parser.TP, i);
		}
		public Def_termContext def_term(int i) {
			return getRuleContext(Def_termContext.class,i);
		}
		public State_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterState_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitState_def(this);
		}
	}

	public final State_defContext state_def() throws RecognitionException {
		State_defContext _localctx = new State_defContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_state_def);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286); def_term();
			setState(287); match(TP);
			setState(288); def_term();
			setState(297);
			switch (_input.LA(1)) {
			case TP:
				{
				{
				setState(289); match(TP);
				setState(290); def_term();
				setState(294);
				switch (_input.LA(1)) {
				case TP:
					{
					setState(291); match(TP);
					setState(292); def_term();
					}
					break;
				case SEMI:
					{
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				break;
			case SEMI:
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(299); match(SEMI);
			setState(300); lines();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Transition_defContext extends ParserRuleContext {
		public List<TerminalNode> TP() { return getTokens(T7Parser.TP); }
		public LinesContext lines() {
			return getRuleContext(LinesContext.class,0);
		}
		public List<Def_termContext> def_term() {
			return getRuleContexts(Def_termContext.class);
		}
		public TerminalNode SEMI() { return getToken(T7Parser.SEMI, 0); }
		public TerminalNode TP(int i) {
			return getToken(T7Parser.TP, i);
		}
		public Def_termContext def_term(int i) {
			return getRuleContext(Def_termContext.class,i);
		}
		public Transition_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transition_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterTransition_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitTransition_def(this);
		}
	}

	public final Transition_defContext transition_def() throws RecognitionException {
		Transition_defContext _localctx = new Transition_defContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_transition_def);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302); def_term();
			setState(303); match(TP);
			setState(304); def_term();
			setState(313);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				{
				setState(305); match(TP);
				setState(306); def_term();
				setState(307); match(TP);
				setState(308); def_term();
				}
				}
				break;
			case 2:
				{
				setState(310); match(TP);
				setState(311); def_term();
				}
				break;
			case 3:
				{
				}
				break;
			}
			setState(315); match(SEMI);
			setState(316); lines();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Def_termContext extends ParserRuleContext {
		public List<TerminalNode> TP() { return getTokens(T7Parser.TP); }
		public List<TerminalNode> SEMI() { return getTokens(T7Parser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(T7Parser.SEMI, i);
		}
		public TerminalNode TP(int i) {
			return getToken(T7Parser.TP, i);
		}
		public Def_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterDef_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitDef_term(this);
		}
	}

	public final Def_termContext def_term() throws RecognitionException {
		Def_termContext _localctx = new Def_termContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_def_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(318);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==TP || _la==SEMI) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				setState(321); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LINE) | (1L << COMMENT) | (1L << TRUE) | (1L << FALSE) | (1L << GT) | (1L << GE) | (1L << LT) | (1L << LE) | (1L << BQ) | (1L << NQ) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PLUS) | (1L << MINUS) | (1L << MULT) | (1L << DIV) | (1L << MOD) | (1L << POW) | (1L << CLEAR) | (1L << LET) | (1L << INSTANCE) | (1L << DEFINEO) | (1L << DEFINE) | (1L << FOR) | (1L << WHILE) | (1L << IF) | (1L << ELSE) | (1L << END) | (1L << IN) | (1L << LOAD) | (1L << EXPORTO) | (1L << EXPORT) | (1L << START) | (1L << LIST) | (1L << DEL) | (1L << EXTRACT) | (1L << SEARCH) | (1L << STATES) | (1L << TRANSITIONS) | (1L << SPECIFICATIONS) | (1L << EQ) | (1L << COMMA) | (1L << LP) | (1L << RP) | (1L << LC) | (1L << RC) | (1L << LS) | (1L << RS) | (1L << DOT) | (1L << SINGLEAND) | (1L << SINGLECOL) | (1L << ID) | (1L << INT) | (1L << WS) | (1L << ANYTHING_ELSE))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(T7Parser.ID, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitIdentifier(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(T7Parser.SEMI, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitRange(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_range);
		try {
			setState(330);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(325); value();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(326); identifier();
				setState(327); match(SEMI);
				setState(328); bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arith_expContext extends ParserRuleContext {
		public Arith_exp1Context arith_exp1() {
			return getRuleContext(Arith_exp1Context.class,0);
		}
		public Arith_exp3Context arith_exp3() {
			return getRuleContext(Arith_exp3Context.class,0);
		}
		public Arith_exp2Context arith_exp2() {
			return getRuleContext(Arith_exp2Context.class,0);
		}
		public Atomic_arith_expContext atomic_arith_exp() {
			return getRuleContext(Atomic_arith_expContext.class,0);
		}
		public Arith_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterArith_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitArith_exp(this);
		}
	}

	public final Arith_expContext arith_exp() throws RecognitionException {
		Arith_expContext _localctx = new Arith_expContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_arith_exp);
		try {
			setState(336);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(332); atomic_arith_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(333); arith_exp1();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(334); arith_exp2();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(335); arith_exp3();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arith_exp1Context extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(T7Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(T7Parser.MINUS, 0); }
		public Arith_exp2Context arith_exp2() {
			return getRuleContext(Arith_exp2Context.class,0);
		}
		public Atomic_arith_expContext atomic_arith_exp() {
			return getRuleContext(Atomic_arith_expContext.class,0);
		}
		public Arith_exp1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_exp1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterArith_exp1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitArith_exp1(this);
		}
	}

	public final Arith_exp1Context arith_exp1() throws RecognitionException {
		Arith_exp1Context _localctx = new Arith_exp1Context(_ctx, getState());
		enterRule(_localctx, 62, RULE_arith_exp1);
		int _la;
		try {
			setState(343);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(338); atomic_arith_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(339); arith_exp2();
				setState(340);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(341); arith_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arith_exp2Context extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public TerminalNode MULT() { return getToken(T7Parser.MULT, 0); }
		public Arith_exp3Context arith_exp3() {
			return getRuleContext(Arith_exp3Context.class,0);
		}
		public TerminalNode MOD() { return getToken(T7Parser.MOD, 0); }
		public TerminalNode DIV() { return getToken(T7Parser.DIV, 0); }
		public Atomic_arith_expContext atomic_arith_exp() {
			return getRuleContext(Atomic_arith_expContext.class,0);
		}
		public Arith_exp2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_exp2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterArith_exp2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitArith_exp2(this);
		}
	}

	public final Arith_exp2Context arith_exp2() throws RecognitionException {
		Arith_exp2Context _localctx = new Arith_exp2Context(_ctx, getState());
		enterRule(_localctx, 64, RULE_arith_exp2);
		int _la;
		try {
			setState(350);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(345); atomic_arith_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(346); arith_exp3();
				setState(347);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULT) | (1L << DIV) | (1L << MOD))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(348); arith_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arith_exp3Context extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public TerminalNode POW() { return getToken(T7Parser.POW, 0); }
		public Atomic_arith_expContext atomic_arith_exp() {
			return getRuleContext(Atomic_arith_expContext.class,0);
		}
		public Arith_exp3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_exp3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterArith_exp3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitArith_exp3(this);
		}
	}

	public final Arith_exp3Context arith_exp3() throws RecognitionException {
		Arith_exp3Context _localctx = new Arith_exp3Context(_ctx, getState());
		enterRule(_localctx, 66, RULE_arith_exp3);
		try {
			setState(357);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(352); atomic_arith_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(353); atomic_arith_exp();
				setState(354); match(POW);
				setState(355); arith_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Atomic_arith_expContext extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public TerminalNode RP() { return getToken(T7Parser.RP, 0); }
		public TerminalNode LP() { return getToken(T7Parser.LP, 0); }
		public TerminalNode MINUS() { return getToken(T7Parser.MINUS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode INT() { return getToken(T7Parser.INT, 0); }
		public Atomic_arith_expContext atomic_arith_exp() {
			return getRuleContext(Atomic_arith_expContext.class,0);
		}
		public Atomic_arith_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomic_arith_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterAtomic_arith_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitAtomic_arith_exp(this);
		}
	}

	public final Atomic_arith_expContext atomic_arith_exp() throws RecognitionException {
		Atomic_arith_expContext _localctx = new Atomic_arith_expContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_atomic_arith_exp);
		try {
			setState(367);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(359); match(INT);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(360); identifier();
				}
				break;
			case LP:
				enterOuterAlt(_localctx, 3);
				{
				setState(361); match(LP);
				setState(362); arith_exp();
				setState(363); match(RP);
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 4);
				{
				setState(365); match(MINUS);
				setState(366); atomic_arith_exp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_expContext extends ParserRuleContext {
		public Bool_exp1Context bool_exp1() {
			return getRuleContext(Bool_exp1Context.class,0);
		}
		public Bool_exp2Context bool_exp2() {
			return getRuleContext(Bool_exp2Context.class,0);
		}
		public Atomic_bool_expContext atomic_bool_exp() {
			return getRuleContext(Atomic_bool_expContext.class,0);
		}
		public Bool_exp3Context bool_exp3() {
			return getRuleContext(Bool_exp3Context.class,0);
		}
		public Bool_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_exp(this);
		}
	}

	public final Bool_expContext bool_exp() throws RecognitionException {
		Bool_expContext _localctx = new Bool_expContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_bool_exp);
		try {
			setState(373);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(369); atomic_bool_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(370); bool_exp1();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(371); bool_exp2();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(372); bool_exp3();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_exp1Context extends ParserRuleContext {
		public TerminalNode NQ() { return getToken(T7Parser.NQ, 0); }
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public Bool_exp2Context bool_exp2() {
			return getRuleContext(Bool_exp2Context.class,0);
		}
		public TerminalNode BQ() { return getToken(T7Parser.BQ, 0); }
		public Atomic_bool_expContext atomic_bool_exp() {
			return getRuleContext(Atomic_bool_expContext.class,0);
		}
		public Bool_exp1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_exp1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_exp1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_exp1(this);
		}
	}

	public final Bool_exp1Context bool_exp1() throws RecognitionException {
		Bool_exp1Context _localctx = new Bool_exp1Context(_ctx, getState());
		enterRule(_localctx, 72, RULE_bool_exp1);
		try {
			setState(384);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(375); atomic_bool_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(376); bool_exp2();
				setState(377); match(BQ);
				setState(378); bool_exp();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(380); bool_exp2();
				setState(381); match(NQ);
				setState(382); bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_exp2Context extends ParserRuleContext {
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public TerminalNode AND() { return getToken(T7Parser.AND, 0); }
		public Atomic_bool_expContext atomic_bool_exp() {
			return getRuleContext(Atomic_bool_expContext.class,0);
		}
		public Bool_exp3Context bool_exp3() {
			return getRuleContext(Bool_exp3Context.class,0);
		}
		public Bool_exp2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_exp2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_exp2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_exp2(this);
		}
	}

	public final Bool_exp2Context bool_exp2() throws RecognitionException {
		Bool_exp2Context _localctx = new Bool_exp2Context(_ctx, getState());
		enterRule(_localctx, 74, RULE_bool_exp2);
		try {
			setState(391);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(386); atomic_bool_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(387); bool_exp3();
				setState(388); match(AND);
				setState(389); bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_exp3Context extends ParserRuleContext {
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public Atomic_bool_expContext atomic_bool_exp() {
			return getRuleContext(Atomic_bool_expContext.class,0);
		}
		public TerminalNode OR() { return getToken(T7Parser.OR, 0); }
		public Bool_exp3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_exp3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_exp3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_exp3(this);
		}
	}

	public final Bool_exp3Context bool_exp3() throws RecognitionException {
		Bool_exp3Context _localctx = new Bool_exp3Context(_ctx, getState());
		enterRule(_localctx, 76, RULE_bool_exp3);
		try {
			setState(398);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(393); atomic_bool_exp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(394); atomic_bool_exp();
				setState(395); match(OR);
				setState(396); bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Atomic_bool_expContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(T7Parser.NOT, 0); }
		public TerminalNode RP() { return getToken(T7Parser.RP, 0); }
		public TerminalNode FALSE() { return getToken(T7Parser.FALSE, 0); }
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(T7Parser.TRUE, 0); }
		public TerminalNode LP() { return getToken(T7Parser.LP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Atomic_bool_expContext atomic_bool_exp() {
			return getRuleContext(Atomic_bool_expContext.class,0);
		}
		public Bool_arith_expContext bool_arith_exp() {
			return getRuleContext(Bool_arith_expContext.class,0);
		}
		public Atomic_bool_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomic_bool_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterAtomic_bool_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitAtomic_bool_exp(this);
		}
	}

	public final Atomic_bool_expContext atomic_bool_exp() throws RecognitionException {
		Atomic_bool_expContext _localctx = new Atomic_bool_expContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_atomic_bool_exp);
		try {
			setState(410);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(400); match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(401); match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(402); identifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(403); bool_arith_exp();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(404); match(LP);
				setState(405); bool_exp();
				setState(406); match(RP);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(408); match(NOT);
				setState(409); atomic_bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_arith_expContext extends ParserRuleContext {
		public List<Arith_expContext> arith_exp() {
			return getRuleContexts(Arith_expContext.class);
		}
		public Arith_expContext arith_exp(int i) {
			return getRuleContext(Arith_expContext.class,i);
		}
		public Bool_opContext bool_op() {
			return getRuleContext(Bool_opContext.class,0);
		}
		public Bool_arith_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_arith_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_arith_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_arith_exp(this);
		}
	}

	public final Bool_arith_expContext bool_arith_exp() throws RecognitionException {
		Bool_arith_expContext _localctx = new Bool_arith_expContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_bool_arith_exp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412); arith_exp();
			setState(413); bool_op();
			setState(414); arith_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_opContext extends ParserRuleContext {
		public TerminalNode GE() { return getToken(T7Parser.GE, 0); }
		public TerminalNode NQ() { return getToken(T7Parser.NQ, 0); }
		public TerminalNode LT() { return getToken(T7Parser.LT, 0); }
		public TerminalNode BQ() { return getToken(T7Parser.BQ, 0); }
		public TerminalNode GT() { return getToken(T7Parser.GT, 0); }
		public TerminalNode LE() { return getToken(T7Parser.LE, 0); }
		public Bool_opContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_op; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterBool_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitBool_op(this);
		}
	}

	public final Bool_opContext bool_op() throws RecognitionException {
		Bool_opContext _localctx = new Bool_opContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_bool_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << GE) | (1L << LT) | (1L << LE) | (1L << BQ) | (1L << NQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public Bool_expContext bool_exp() {
			return getRuleContext(Bool_expContext.class,0);
		}
		public TripletContext triplet() {
			return getRuleContext(TripletContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_value);
		try {
			setState(422);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(418); identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(419); arith_exp();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(420); triplet();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(421); bool_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TripletContext extends ParserRuleContext {
		public List<TerminalNode> TP() { return getTokens(T7Parser.TP); }
		public Triplet_termContext triplet_term(int i) {
			return getRuleContext(Triplet_termContext.class,i);
		}
		public TerminalNode TP(int i) {
			return getToken(T7Parser.TP, i);
		}
		public List<Triplet_termContext> triplet_term() {
			return getRuleContexts(Triplet_termContext.class);
		}
		public TripletContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triplet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterTriplet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitTriplet(this);
		}
	}

	public final TripletContext triplet() throws RecognitionException {
		TripletContext _localctx = new TripletContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_triplet);
		try {
			setState(434);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(424); triplet_term();
				setState(425); match(TP);
				setState(426); triplet_term();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(428); triplet_term();
				setState(429); match(TP);
				setState(430); triplet_term();
				setState(431); match(TP);
				setState(432); triplet_term();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Triplet_termContext extends ParserRuleContext {
		public Arith_expContext arith_exp() {
			return getRuleContext(Arith_expContext.class,0);
		}
		public Triplet_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triplet_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterTriplet_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitTriplet_term(this);
		}
	}

	public final Triplet_termContext triplet_term() throws RecognitionException {
		Triplet_termContext _localctx = new Triplet_termContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_triplet_term);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436); arith_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(T7Parser.COMMENT, 0); }
		public AnyContext any() {
			return getRuleContext(AnyContext.class,0);
		}
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitComment(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438); match(COMMENT);
			setState(439); any();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class New_lineContext extends ParserRuleContext {
		public TerminalNode LINE() { return getToken(T7Parser.LINE, 0); }
		public New_lineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_new_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterNew_line(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitNew_line(this);
		}
	}

	public final New_lineContext new_line() throws RecognitionException {
		New_lineContext _localctx = new New_lineContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_new_line);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441); match(LINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnyContext extends ParserRuleContext {
		public List<TerminalNode> LINE() { return getTokens(T7Parser.LINE); }
		public TerminalNode LINE(int i) {
			return getToken(T7Parser.LINE, i);
		}
		public AnyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).enterAny(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof T7Listener ) ((T7Listener)listener).exitAny(this);
		}
	}

	public final AnyContext any() throws RecognitionException {
		AnyContext _localctx = new AnyContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_any);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(444); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(443);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==LINE) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(446); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3<\u01c3\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2r\n\2\3\2\3\2\5\2v\n\2\3\2\3\2\3\3"+
		"\3\3\5\3|\n\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4\u0088\n\4\f"+
		"\4\16\4\u008b\13\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5\u0095\n\5\f\5\16"+
		"\5\u0098\13\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\6\f\u00b2\n\f\r\f\16\f\u00b3"+
		"\3\r\3\r\6\r\u00b8\n\r\r\r\16\r\u00b9\3\16\3\16\3\16\3\16\5\16\u00c0\n"+
		"\16\3\16\3\16\3\16\5\16\u00c5\n\16\7\16\u00c7\n\16\f\16\16\16\u00ca\13"+
		"\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00e0\n\21\3\21\3\21\3\22\3\22"+
		"\7\22\u00e6\n\22\f\22\16\22\u00e9\13\22\3\22\3\22\3\23\3\23\3\23\5\23"+
		"\u00f0\n\23\3\23\3\23\3\24\3\24\7\24\u00f6\n\24\f\24\16\24\u00f9\13\24"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\7\27\u0104\n\27\f\27\16"+
		"\27\u0107\13\27\3\30\3\30\3\30\7\30\u010c\n\30\f\30\16\30\u010f\13\30"+
		"\3\31\3\31\3\31\7\31\u0114\n\31\f\31\16\31\u0117\13\31\3\31\3\31\3\32"+
		"\7\32\u011c\n\32\f\32\16\32\u011f\13\32\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\5\33\u0129\n\33\3\33\5\33\u012c\n\33\3\33\3\33\3\33\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u013c\n\34\3\34"+
		"\3\34\3\34\3\35\6\35\u0142\n\35\r\35\16\35\u0143\3\36\3\36\3\37\3\37\3"+
		"\37\3\37\3\37\5\37\u014d\n\37\3 \3 \3 \3 \5 \u0153\n \3!\3!\3!\3!\3!\5"+
		"!\u015a\n!\3\"\3\"\3\"\3\"\3\"\5\"\u0161\n\"\3#\3#\3#\3#\3#\5#\u0168\n"+
		"#\3$\3$\3$\3$\3$\3$\3$\3$\5$\u0172\n$\3%\3%\3%\3%\5%\u0178\n%\3&\3&\3"+
		"&\3&\3&\3&\3&\3&\3&\5&\u0183\n&\3\'\3\'\3\'\3\'\3\'\5\'\u018a\n\'\3(\3"+
		"(\3(\3(\3(\5(\u0191\n(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)\u019d\n)\3*\3"+
		"*\3*\3*\3+\3+\3,\3,\3,\3,\5,\u01a9\n,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\5"+
		"-\u01b5\n-\3.\3.\3/\3/\3/\3\60\3\60\3\61\6\61\u01bf\n\61\r\61\16\61\u01c0"+
		"\3\61\2\2\62\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:<>@BDFHJLNPRTVXZ\\^`\2\n\3\2\31\32\3\2\"#\3\2\63\63\3\2-.\3\2\20"+
		"\21\3\2\22\24\3\2\7\f\3\2\3\3\u01d1\2q\3\2\2\2\4{\3\2\2\2\6\u0081\3\2"+
		"\2\2\b\u008e\3\2\2\2\n\u00a0\3\2\2\2\f\u00a2\3\2\2\2\16\u00a5\3\2\2\2"+
		"\20\u00a8\3\2\2\2\22\u00aa\3\2\2\2\24\u00ac\3\2\2\2\26\u00af\3\2\2\2\30"+
		"\u00b5\3\2\2\2\32\u00bb\3\2\2\2\34\u00cd\3\2\2\2\36\u00d3\3\2\2\2 \u00d8"+
		"\3\2\2\2\"\u00e3\3\2\2\2$\u00ec\3\2\2\2&\u00f3\3\2\2\2(\u00fa\3\2\2\2"+
		"*\u00fc\3\2\2\2,\u0100\3\2\2\2.\u0108\3\2\2\2\60\u0110\3\2\2\2\62\u011d"+
		"\3\2\2\2\64\u0120\3\2\2\2\66\u0130\3\2\2\28\u0141\3\2\2\2:\u0145\3\2\2"+
		"\2<\u014c\3\2\2\2>\u0152\3\2\2\2@\u0159\3\2\2\2B\u0160\3\2\2\2D\u0167"+
		"\3\2\2\2F\u0171\3\2\2\2H\u0177\3\2\2\2J\u0182\3\2\2\2L\u0189\3\2\2\2N"+
		"\u0190\3\2\2\2P\u019c\3\2\2\2R\u019e\3\2\2\2T\u01a2\3\2\2\2V\u01a8\3\2"+
		"\2\2X\u01b4\3\2\2\2Z\u01b6\3\2\2\2\\\u01b8\3\2\2\2^\u01bb\3\2\2\2`\u01be"+
		"\3\2\2\2br\5\b\5\2cr\5\4\3\2dr\5\6\4\2er\5\\/\2fr\5\n\6\2gr\5\34\17\2"+
		"hr\5\36\20\2ir\5 \21\2jr\5\f\7\2kr\5\16\b\2lr\5\20\t\2mr\5\22\n\2nr\5"+
		"\26\f\2or\5\24\13\2pr\5\30\r\2qb\3\2\2\2qc\3\2\2\2qd\3\2\2\2qe\3\2\2\2"+
		"qf\3\2\2\2qg\3\2\2\2qh\3\2\2\2qi\3\2\2\2qj\3\2\2\2qk\3\2\2\2ql\3\2\2\2"+
		"qm\3\2\2\2qn\3\2\2\2qo\3\2\2\2qp\3\2\2\2ru\3\2\2\2sv\5\\/\2tv\3\2\2\2"+
		"us\3\2\2\2ut\3\2\2\2vw\3\2\2\2wx\5^\60\2x\3\3\2\2\2y|\7\27\2\2z|\3\2\2"+
		"\2{y\3\2\2\2{z\3\2\2\2|}\3\2\2\2}~\5:\36\2~\177\7,\2\2\177\u0080\5V,\2"+
		"\u0080\5\3\2\2\2\u0081\u0082\7\30\2\2\u0082\u0083\5:\36\2\u0083\u0084"+
		"\7\t\2\2\u0084\u0089\5<\37\2\u0085\u0086\7/\2\2\u0086\u0088\5<\37\2\u0087"+
		"\u0085\3\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u008a\u008c\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008d\7\7\2\2\u008d"+
		"\7\3\2\2\2\u008e\u008f\t\2\2\2\u008f\u0090\5:\36\2\u0090\u0091\7\t\2\2"+
		"\u0091\u0096\5:\36\2\u0092\u0093\7/\2\2\u0093\u0095\5:\36\2\u0094\u0092"+
		"\3\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u0099\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009a\7\7\2\2\u009a\u009b\5\62"+
		"\32\2\u009b\u009c\7\62\2\2\u009c\u009d\5\62\32\2\u009d\u009e\5*\26\2\u009e"+
		"\u009f\7\63\2\2\u009f\t\3\2\2\2\u00a0\u00a1\7\26\2\2\u00a1\13\3\2\2\2"+
		"\u00a2\u00a3\7!\2\2\u00a3\u00a4\5`\61\2\u00a4\r\3\2\2\2\u00a5\u00a6\t"+
		"\3\2\2\u00a6\u00a7\5`\61\2\u00a7\17\3\2\2\2\u00a8\u00a9\7$\2\2\u00a9\21"+
		"\3\2\2\2\u00aa\u00ab\7%\2\2\u00ab\23\3\2\2\2\u00ac\u00ad\7(\2\2\u00ad"+
		"\u00ae\5`\61\2\u00ae\25\3\2\2\2\u00af\u00b1\7&\2\2\u00b0\u00b2\5\32\16"+
		"\2\u00b1\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4"+
		"\3\2\2\2\u00b4\27\3\2\2\2\u00b5\u00b7\7\'\2\2\u00b6\u00b8\5\32\16\2\u00b7"+
		"\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00ba\3\2"+
		"\2\2\u00ba\31\3\2\2\2\u00bb\u00bc\5:\36\2\u00bc\u00bf\7\60\2\2\u00bd\u00c0"+
		"\5:\36\2\u00be\u00c0\7:\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00be\3\2\2\2\u00c0"+
		"\u00c8\3\2\2\2\u00c1\u00c4\7/\2\2\u00c2\u00c5\5:\36\2\u00c3\u00c5\7:\2"+
		"\2\u00c4\u00c2\3\2\2\2\u00c4\u00c3\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c1"+
		"\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"+
		"\u00cb\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cc\7\61\2\2\u00cc\33\3\2\2"+
		"\2\u00cd\u00ce\7\33\2\2\u00ce\u00cf\5:\36\2\u00cf\u00d0\7 \2\2\u00d0\u00d1"+
		"\5<\37\2\u00d1\u00d2\5\"\22\2\u00d2\35\3\2\2\2\u00d3\u00d4\7\34\2\2\u00d4"+
		"\u00d5\5(\25\2\u00d5\u00d6\5^\60\2\u00d6\u00d7\5\"\22\2\u00d7\37\3\2\2"+
		"\2\u00d8\u00d9\7\35\2\2\u00d9\u00da\5(\25\2\u00da\u00db\5^\60\2\u00db"+
		"\u00dc\5\62\32\2\u00dc\u00df\5&\24\2\u00dd\u00e0\5$\23\2\u00de\u00e0\3"+
		"\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1"+
		"\u00e2\7\37\2\2\u00e2!\3\2\2\2\u00e3\u00e7\5\62\32\2\u00e4\u00e6\5\2\2"+
		"\2\u00e5\u00e4\3\2\2\2\u00e6\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8"+
		"\3\2\2\2\u00e8\u00ea\3\2\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00eb\7\37\2\2"+
		"\u00eb#\3\2\2\2\u00ec\u00ef\7\36\2\2\u00ed\u00f0\5^\60\2\u00ee\u00f0\3"+
		"\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1"+
		"\u00f2\5&\24\2\u00f2%\3\2\2\2\u00f3\u00f7\5\62\32\2\u00f4\u00f6\5\2\2"+
		"\2\u00f5\u00f4\3\2\2\2\u00f6\u00f9\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f8"+
		"\3\2\2\2\u00f8\'\3\2\2\2\u00f9\u00f7\3\2\2\2\u00fa\u00fb\5H%\2\u00fb)"+
		"\3\2\2\2\u00fc\u00fd\5,\27\2\u00fd\u00fe\5.\30\2\u00fe\u00ff\5\60\31\2"+
		"\u00ff+\3\2\2\2\u0100\u0101\7)\2\2\u0101\u0105\5\62\32\2\u0102\u0104\5"+
		"\64\33\2\u0103\u0102\3\2\2\2\u0104\u0107\3\2\2\2\u0105\u0103\3\2\2\2\u0105"+
		"\u0106\3\2\2\2\u0106-\3\2\2\2\u0107\u0105\3\2\2\2\u0108\u0109\7*\2\2\u0109"+
		"\u010d\5\62\32\2\u010a\u010c\5\66\34\2\u010b\u010a\3\2\2\2\u010c\u010f"+
		"\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e/\3\2\2\2\u010f"+
		"\u010d\3\2\2\2\u0110\u0111\7+\2\2\u0111\u0115\5\62\32\2\u0112\u0114\n"+
		"\4\2\2\u0113\u0112\3\2\2\2\u0114\u0117\3\2\2\2\u0115\u0113\3\2\2\2\u0115"+
		"\u0116\3\2\2\2\u0116\u0118\3\2\2\2\u0117\u0115\3\2\2\2\u0118\u0119\5\62"+
		"\32\2\u0119\61\3\2\2\2\u011a\u011c\5^\60\2\u011b\u011a\3\2\2\2\u011c\u011f"+
		"\3\2\2\2\u011d\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\63\3\2\2\2\u011f"+
		"\u011d\3\2\2\2\u0120\u0121\58\35\2\u0121\u0122\7-\2\2\u0122\u012b\58\35"+
		"\2\u0123\u0124\7-\2\2\u0124\u0128\58\35\2\u0125\u0126\7-\2\2\u0126\u0129"+
		"\58\35\2\u0127\u0129\3\2\2\2\u0128\u0125\3\2\2\2\u0128\u0127\3\2\2\2\u0129"+
		"\u012c\3\2\2\2\u012a\u012c\3\2\2\2\u012b\u0123\3\2\2\2\u012b\u012a\3\2"+
		"\2\2\u012c\u012d\3\2\2\2\u012d\u012e\7.\2\2\u012e\u012f\5\62\32\2\u012f"+
		"\65\3\2\2\2\u0130\u0131\58\35\2\u0131\u0132\7-\2\2\u0132\u013b\58\35\2"+
		"\u0133\u0134\7-\2\2\u0134\u0135\58\35\2\u0135\u0136\7-\2\2\u0136\u0137"+
		"\58\35\2\u0137\u013c\3\2\2\2\u0138\u0139\7-\2\2\u0139\u013c\58\35\2\u013a"+
		"\u013c\3\2\2\2\u013b\u0133\3\2\2\2\u013b\u0138\3\2\2\2\u013b\u013a\3\2"+
		"\2\2\u013c\u013d\3\2\2\2\u013d\u013e\7.\2\2\u013e\u013f\5\62\32\2\u013f"+
		"\67\3\2\2\2\u0140\u0142\n\5\2\2\u0141\u0140\3\2\2\2\u0142\u0143\3\2\2"+
		"\2\u0143\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u01449\3\2\2\2\u0145\u0146"+
		"\79\2\2\u0146;\3\2\2\2\u0147\u014d\5V,\2\u0148\u0149\5:\36\2\u0149\u014a"+
		"\7.\2\2\u014a\u014b\5H%\2\u014b\u014d\3\2\2\2\u014c\u0147\3\2\2\2\u014c"+
		"\u0148\3\2\2\2\u014d=\3\2\2\2\u014e\u0153\5F$\2\u014f\u0153\5@!\2\u0150"+
		"\u0153\5B\"\2\u0151\u0153\5D#\2\u0152\u014e\3\2\2\2\u0152\u014f\3\2\2"+
		"\2\u0152\u0150\3\2\2\2\u0152\u0151\3\2\2\2\u0153?\3\2\2\2\u0154\u015a"+
		"\5F$\2\u0155\u0156\5B\"\2\u0156\u0157\t\6\2\2\u0157\u0158\5> \2\u0158"+
		"\u015a\3\2\2\2\u0159\u0154\3\2\2\2\u0159\u0155\3\2\2\2\u015aA\3\2\2\2"+
		"\u015b\u0161\5F$\2\u015c\u015d\5D#\2\u015d\u015e\t\7\2\2\u015e\u015f\5"+
		"> \2\u015f\u0161\3\2\2\2\u0160\u015b\3\2\2\2\u0160\u015c\3\2\2\2\u0161"+
		"C\3\2\2\2\u0162\u0168\5F$\2\u0163\u0164\5F$\2\u0164\u0165\7\25\2\2\u0165"+
		"\u0166\5> \2\u0166\u0168\3\2\2\2\u0167\u0162\3\2\2\2\u0167\u0163\3\2\2"+
		"\2\u0168E\3\2\2\2\u0169\u0172\7:\2\2\u016a\u0172\5:\36\2\u016b\u016c\7"+
		"\60\2\2\u016c\u016d\5> \2\u016d\u016e\7\61\2\2\u016e\u0172\3\2\2\2\u016f"+
		"\u0170\7\21\2\2\u0170\u0172\5F$\2\u0171\u0169\3\2\2\2\u0171\u016a\3\2"+
		"\2\2\u0171\u016b\3\2\2\2\u0171\u016f\3\2\2\2\u0172G\3\2\2\2\u0173\u0178"+
		"\5P)\2\u0174\u0178\5J&\2\u0175\u0178\5L\'\2\u0176\u0178\5N(\2\u0177\u0173"+
		"\3\2\2\2\u0177\u0174\3\2\2\2\u0177\u0175\3\2\2\2\u0177\u0176\3\2\2\2\u0178"+
		"I\3\2\2\2\u0179\u0183\5P)\2\u017a\u017b\5L\'\2\u017b\u017c\7\13\2\2\u017c"+
		"\u017d\5H%\2\u017d\u0183\3\2\2\2\u017e\u017f\5L\'\2\u017f\u0180\7\f\2"+
		"\2\u0180\u0181\5H%\2\u0181\u0183\3\2\2\2\u0182\u0179\3\2\2\2\u0182\u017a"+
		"\3\2\2\2\u0182\u017e\3\2\2\2\u0183K\3\2\2\2\u0184\u018a\5P)\2\u0185\u0186"+
		"\5N(\2\u0186\u0187\7\r\2\2\u0187\u0188\5H%\2\u0188\u018a\3\2\2\2\u0189"+
		"\u0184\3\2\2\2\u0189\u0185\3\2\2\2\u018aM\3\2\2\2\u018b\u0191\5P)\2\u018c"+
		"\u018d\5P)\2\u018d\u018e\7\16\2\2\u018e\u018f\5H%\2\u018f\u0191\3\2\2"+
		"\2\u0190\u018b\3\2\2\2\u0190\u018c\3\2\2\2\u0191O\3\2\2\2\u0192\u019d"+
		"\7\5\2\2\u0193\u019d\7\6\2\2\u0194\u019d\5:\36\2\u0195\u019d\5R*\2\u0196"+
		"\u0197\7\60\2\2\u0197\u0198\5H%\2\u0198\u0199\7\61\2\2\u0199\u019d\3\2"+
		"\2\2\u019a\u019b\7\17\2\2\u019b\u019d\5P)\2\u019c\u0192\3\2\2\2\u019c"+
		"\u0193\3\2\2\2\u019c\u0194\3\2\2\2\u019c\u0195\3\2\2\2\u019c\u0196\3\2"+
		"\2\2\u019c\u019a\3\2\2\2\u019dQ\3\2\2\2\u019e\u019f\5> \2\u019f\u01a0"+
		"\5T+\2\u01a0\u01a1\5> \2\u01a1S\3\2\2\2\u01a2\u01a3\t\b\2\2\u01a3U\3\2"+
		"\2\2\u01a4\u01a9\5:\36\2\u01a5\u01a9\5> \2\u01a6\u01a9\5X-\2\u01a7\u01a9"+
		"\5H%\2\u01a8\u01a4\3\2\2\2\u01a8\u01a5\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8"+
		"\u01a7\3\2\2\2\u01a9W\3\2\2\2\u01aa\u01ab\5Z.\2\u01ab\u01ac\7-\2\2\u01ac"+
		"\u01ad\5Z.\2\u01ad\u01b5\3\2\2\2\u01ae\u01af\5Z.\2\u01af\u01b0\7-\2\2"+
		"\u01b0\u01b1\5Z.\2\u01b1\u01b2\7-\2\2\u01b2\u01b3\5Z.\2\u01b3\u01b5\3"+
		"\2\2\2\u01b4\u01aa\3\2\2\2\u01b4\u01ae\3\2\2\2\u01b5Y\3\2\2\2\u01b6\u01b7"+
		"\5> \2\u01b7[\3\2\2\2\u01b8\u01b9\7\4\2\2\u01b9\u01ba\5`\61\2\u01ba]\3"+
		"\2\2\2\u01bb\u01bc\7\3\2\2\u01bc_\3\2\2\2\u01bd\u01bf\n\t\2\2\u01be\u01bd"+
		"\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1"+
		"a\3\2\2\2&qu{\u0089\u0096\u00b3\u00b9\u00bf\u00c4\u00c8\u00df\u00e7\u00ef"+
		"\u00f7\u0105\u010d\u0115\u011d\u0128\u012b\u013b\u0143\u014c\u0152\u0159"+
		"\u0160\u0167\u0171\u0177\u0182\u0189\u0190\u019c\u01a8\u01b4\u01c0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}