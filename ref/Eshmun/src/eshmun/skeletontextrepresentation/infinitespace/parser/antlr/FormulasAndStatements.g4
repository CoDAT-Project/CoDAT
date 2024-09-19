grammar FormulasAndStatements;



prog: 'program' LCURLY intial sections RCURLY;
intial: 'initial' COLON formula;

sections: (process)+    ;

process: 'process' processname  LCURLY (action)* RCURLY;

processname  : NUM_INT;

action: 'action'  name?  LCURLY actionDefinition? 
								actionDefinition? 
								actionDefinition? 
						 		actionDefinition? 
					      RCURLY;
					     
actionDefinition: localGuard | globalGuard | localEffect | globalEffect ;

localGuard: 'l_grd' COLON formula SEMI?;
globalGuard: 'g_grd' COLON formula SEMI?;

localEffect:  'l_eff' COLON statement SEMI? ; 
globalEffect: 'g_eff' COLON statement SEMI?; 
name: IDENT | NUM_INT; 
/* ************* FORMULAS */

formula   : quantifiedFormula | disjunction  ;					

quantifiedFormula: 	quantifier	boundVariable disjunction ;	

andOr: 	OR|AND;		 
quantifier: FORALL | EXISTS;
disjunction   : conjunction (OR conjunction)*   ;
conjunction   : negation (AND negation)*   ;
negation   : NOT? (predicate | LPAREN formula RPAREN)   ;
predicate   : binaryPredicate    | (atomicPredicate | boolLiteral  );
binaryPredicate  : LPAREN logicTerm relOp logicTerm RPAREN  ;
logicTerm   : binaryFunction   | unaryFunction   ;
binaryFunction    : LPAREN (unaryFunction) (arithOp) (unaryFunction) RPAREN   ;  
relOp: EQUAL     |NOT_EQUAL       |              LT|         LE            |GT        |              GE;
arithOp: MINUS|  PLUS| STAR|  SLASH;
unaryFunction: boundVariable | freeVariable ;
boolLiteral: TRUE |FALSE;
freeVariable: IDENT | NUM_INT | '0';
boundVariable : VARIABLE;
atomicPredicate: PREPOSITION;


/* ************* COMMANDS */

statement :   assignmentStatement | ifStatement | block | sskip ;

sskip : SKIP;

assignmentStatement:  (leftIdentifier| leftPredicate) ASSIGN (righExpr | rightBool )  SEMI;
leftIdentifier: identifier ;
leftPredicate: atomicPredicate;
righExpr: expr ;
rightBool:  bool;


ifStatement : IF LPAREN ifboolPredicate RPAREN ifBody elseBody?;
ifBody: statement;
elseBody: ELSE statement;

block : LCURLY ( statement )* RCURLY ;

ifboolPredicate: ifcondition;
ifcondition : ifcondTerm ( OR ifcondTerm )* ;
ifcondTerm : negated ( AND negated )* ;
negated : NOT? (ifpredicate | LPAREN  ifcondition RPAREN);
ifpredicate: expr relationaloperator expr;


expr
	:	signedNumber //numbers
	|	identifier  //variables
	|	'(' expr ')'  //grouping with parentheses
	|	expr multiplicativeoperator expr	//explicit division/multiplication
	|	expr additiveoperator expr	//addition/subtraction
	;

unaryPLus: additiveoperator ;
relationaloperator   : EQUAL   | NOT_EQUAL   | LT   | LE   | GE   | GT;
additiveoperator   : PLUS   | MINUS  ;
multiplicativeoperator   : STAR   | SLASH   ;
 

identifier : IDENT;

signedNumber   :  sign? NUM_INT | '0'; 
sign   : PLUS   | MINUS;
bool   : TRUE   | FALSE;
string   : STRING_LITERAL;


 /*  TOKENS */

NOT          :   '!'     ;
OR           :   '|'     ;
AND          :   '&'     ;
IMPLIES      :   '=>'    ;
EQUIVALENCE  :   '<=>'    ;


FORALL   : F O R A L L   ;
EXISTS   : E X I S T S   ;

TRUE   : T R U E  | T T ;
FALSE   : F A L S E | F F;

VARIABLE   : '?' (('a' .. 'z') | ('0' .. '9')) CHARACTER*   ;
PREPOSITION   : ('A' .. 'Z') CHARACTER*   ;

SKIP: S K I P;




fragment CHARACTER   : ('0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '_')   ;





fragment A   : ('a' | 'A');
fragment B   : ('b' | 'B');
fragment C   : ('c' | 'C');
fragment D   : ('d' | 'D');
fragment E   : ('e' | 'E');
fragment F   : ('f' | 'F');
fragment G   : ('g' | 'G');
fragment H   : ('h' | 'H');
fragment I   : ('i' | 'I');
fragment J   : ('j' | 'J');
fragment K   : ('k' | 'K');
fragment L   : ('l' | 'L');
fragment M   : ('m' | 'M');
fragment N   : ('n' | 'N');
fragment O   : ('o' | 'O');
fragment P   : ('p' | 'P');
fragment Q   : ('q' | 'Q');
fragment R   : ('r' | 'R');
fragment S   : ('s' | 'S');
fragment T   : ('t' | 'T');
fragment U   : ('u' | 'U');
fragment V   : ('v' | 'V');
fragment W   : ('w' | 'W');
fragment X   : ('x' | 'X');
fragment Y   : ('y' | 'Y');
fragment Z   : ('z' | 'Z');



THEN   : T H E N;
ELSE   : E L S E;
IF    : I F;
NULL   : N U L L; 
PLUS   : '+';
MINUS   : '-';
STAR   : '*';
SLASH   : '/';
 

ASSIGN   : ':=';
COMMA   : ',';
SEMI  	 : ';';
COLON   : ':';
EQUAL   : '=';
NOT_EQUAL   : '!=';
LT   : '<';
LE   : '<=';
GE   : '>=';
GT   : '>';
LPAREN   : '(';
RPAREN   : ')';


LCURLY   : '{';
RCURLY   : '}';


WS   : [ \t\r\n] -> skip;

TOK_COMMENT     :   TOK_COMMENT_START .*? TOK_COMMENT_END   -> skip  ;
TOK_COMMENT_START : '/*';
TOK_COMMENT_END : '*/';
LINE_COMMENT  : '//' ~[\r\n]* -> skip ;

IDENT   : ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_')*;
STRING_LITERAL   : '\'' ('\'\'' | ~ ('\''))* '\'';
NUM_INT   : ('1' .. '9') ('0' .. '9')*;


