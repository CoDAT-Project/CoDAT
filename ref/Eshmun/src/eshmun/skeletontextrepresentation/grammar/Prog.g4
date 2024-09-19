grammar Prog;
 
prog: 'program' LEFT_CURL intial sharedvariables? sections RIGHT_CURL;

intial: 'initial' COLON guardExpression;


sharedvariables:  'sharedvariables'  COLON vardomainassignment (COMMA? vardomainassignment)* ;
vardomainassignment: variablename COLON vardomain;
vardomain: LEFT_CURL variablevalue  (COMMA variablevalue)* RIGHT_CURL;

variablevalue : name;
variablename: name;  
 

sections: (process)+ faults? ctlspec?;
process: 'process' processName  LEFT_CURL (action)* RIGHT_CURL;
processName: INT; 
action: 'action'  name?   LEFT_CURL actionDefinition? actionDefinition? actionDefinition? actionDefinition?  actionDefinition? RIGHT_CURL;

ctlspec: 'specifications' COLON LEFT_CURL tempctl RIGHT_CURL;

tempctl:  .*? ; 
  
 
  

name: IDENTIFIER | INT;   
 
faults: 'faults' LEFT_CURL auxProps faultAction+ RIGHT_CURL;
auxProps: 'aux_props' COLON    (IDENTIFIER (',' IDENTIFIER )* )  ;
faultAction: 'fault_action'     LEFT_CURL actionDefinition? actionDefinition?   RIGHT_CURL;
 
//Action specifications
actionDefinition: localGuard | globalGuard | localEffect | globalEffect | effect | guard | predicate;

localGuard: 'l_grd' COLON guardExpression SEMI?;
globalGuard: 'g_grd' COLON guardExpression SEMI?;

localEffect: LEFF COLON statement+ SEMI? |  LEFF COLON SSKIP SEMI? ; 
globalEffect: GEFF COLON statement+ SEMI?| GEFF COLON SSKIP SEMI?; 

effect : 'eff' COLON statement+;		//for fault_actions
guard: 'grd' COLON guardExpression; //for fault_actions
predicate : 'predicate' COLON LEFT_PARA .*? RIGHT_PARA  ;

/* Guard Expression */
guardExpression : andGuardExpression;
andGuardExpression : orGuardExpression (AND_GUARD orGuardExpression)*;
orGuardExpression : atomicGuardExpression (OR_GUARD atomicGuardExpression)*;
atomicGuardExpression : paraGuardExpression | expression;
paraGuardExpression : LEFT_PARA guardExpression RIGHT_PARA;

/* A General Expression */
expression : boolOperator | notBoolExpression;
notBoolExpression : parenthesizedExpression  | label; 

/* Removing left recursion */ 
notEqExpression : notBoolExpression| implicationOp | andOp | orOp | notOp;
notImpExpression : notBoolExpression | andOp | orOp | notOp;
notAndOrExpression : notBoolExpression | notOp;
notAndExpression : notBoolExpression | orOp | notOp;
notOrExpression : notBoolExpression | andOp | notOp;

/* Parenthesized Expression : ( expression ) */ 
parenthesizedExpression : LEFT_PARA expression RIGHT_PARA; 

/* Boolean Logic Operators */
boolOperator : equivalenceOp | implicationOp | andOp | orOp | notOp ;

andOp : notAndExpression (AND notAndExpression)+; // &
orOp : notAndOrExpression (OR notOrExpression)+; // | 
notOp : NOT notBoolExpression; // !
equivalenceOp : notEqExpression (EQUIVALENCE notEqExpression)+; // =>
implicationOp : notImpExpression IMPLICATION expression; // =>

label: IDENTIFIER | variableExpression |  literal ;

variableExpression: IDENTIFIER '=' expr;

statement :   multipleDesignator assignmentSymbol expr(COMMA expr)*    ;

multipleDesignator:    designator (COMMA designator)*; 
designator: IDENTIFIER  ; 

expr : (addop)? term ( addop term )* ; 
term: factor ( mulop factor )* ; 
factor :   NULL |   INT |  LEFT_PARA expr RIGHT_PARA | literal;
 
assignmentSymbol : ':=';
  
addop : OP_ADD; 
mulop : OP_TIMES;
 
variable :   VARIABLE ;
literal : TT | FF;

NULL : 'null';
COMMA : ',';

LEFF: 'l_eff';
GEFF: 'g_eff';

TT : 'true' | 'tt' | 'True';
FF : 'false' | 'ff' | 'False'; 

LEFT_PARA : '(';
RIGHT_PARA : ')';

LEFT_CURL : '{';
RIGHT_CURL : '}';

SSKIP:  'skip';

IDENTIFIER  :    WORD (ALPHANUMERIC)* ;

VARIABLE : (IDENTIFIER  (VAR_ASS | ))   		     		    
		   | (AT WORD (ALPHANUMERIC)*) /* Action name in skeletons. no index or assignment */ ; 

VAR_ASS : ASSIGNMENT ((INT* VARIABLE | INT+) | BOTTOM);

/* Characters */
WORD : ('a'..'z' | 'A'..'Z')+;
INT : [0-9]+;
UNDERSCORE : '_';
DOT : '.';

SEMI : ';';
COLON : ':';

/* Guard Operators */
AND_GUARD : '_&_';
OR_GUARD : '_|_';
COMM_GUARD : '->';
BOTTOM : 'null';
 
/* Boolean Operators */
AND : '&';
OR : '|';
NOT : '!';
IMPLICATION : '=>';  
EQUIVALENCE : '<=>';


OP_ADD      :   '+'         //OP_ADD_PLUS
                |   '-'     ;  //OP_ADD_MINUS
OP_TIMES    :   '*'         //OP_MUL_TIMES
                |   '/'         //OP_MUL_DIV
                |   '%'     ;   //OP_MUL_MOD


ASSIGNMENT : ':=';

AT : '@';

TOK_COMMENT     :   TOK_COMMENT_START .*? TOK_COMMENT_END   -> skip  ;
TOK_COMMENT_START : '/*';
TOK_COMMENT_END : '*/';

INTLIT :  INT+ ;
 
LINE_COMMENT  : '//' ~[\r\n]* -> skip    ;

ALPHANUMERIC : (WORD|INT|UNDERSCORE|DOT)+;

//fragments
fragment LETTER :   [a-zA-Z];
fragment DIGIT     :   [0-9]    ;

WS : [ \t\r\n]+ -> skip ; //Skip White Spaces


