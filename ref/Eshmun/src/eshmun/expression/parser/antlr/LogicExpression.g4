/**
 * Grammar for LogicExpression
 * 
 * Priorities (lowest to highest)
 * - Equivalence
 * - Implication
 * - And
 * - Or
 * - Not
 * - CTL
 * - Parenthesis
 */
grammar LogicExpression;

/* Guard Expression */
guardExpression : andGuardExpression;
andGuardExpression : orGuardExpression (AND_GUARD orGuardExpression)*;
orGuardExpression : atomicGuardExpression (OR_GUARD atomicGuardExpression)*;
atomicGuardExpression : paraGuardExpression | singleGuard;
singleGuard : expression (COMM_GUARD (variable SEMI)+)?;
paraGuardExpression : LEFT_PARA guardExpression RIGHT_PARA;

/* A General Expression */
expression : boolOperator | notBoolExpression;
notBoolExpression : parenthesizedExpression | ctl | literal | variable;

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

/* CTL Modalities */
ctl : unaryCTL | binaryCTL;

unaryCTL : uCTLMod LEFT_PARA expression RIGHT_PARA; //AG(expression), AF(expression), ...
binaryCTL : bCTLMod LEFT_PARA expression bCTLPathMod expression RIGHT_PARA; //A(expression U expression), E(expression W expression) ...

uCTLMod : AG | AF | AX | EG | EF | EX;
bCTLMod : A | E;
bCTLPathMod : U | W | V | R;

/* variable */
variable : VARIABLE;

/* literal */
literal : TRUE | FALSE;

/* parenthesis */
LEFT_PARA : '('; 
RIGHT_PARA : ')';

LEFT_CURL : '{';
RIGHT_CURL : '}';

/* @ for action names */
AT : '@';

/* PROCESS INDICES FOR AX AND EX */
INDEX : UNDERSCORE LEFT_CURL INDEX_TERM (COMMA INDEX_TERM)* RIGHT_CURL;
fragment INDEX_TERM : AT? ((INT* VARIABLE?) | INT+);

/* UNARY CTL MODALITIES */
AG : 'AG';
AF : 'AF';
AX : 'AX' | 'AX' INDEX;
EG : 'EG';
EF : 'EF';
EX : 'EX' | 'EX' INDEX;

/* BINARY CTL MODALITIES */
A : 'A';
E : 'E';
U : 'U';
W : 'W';
V : 'V';
R : 'R';

/* literals */
TRUE : 'true';
FALSE : 'false';

/* Variable */
VARIABLE : (WORD (ALPHANUMERIC)* (VAR_INDEX | ) (VAR_ASS | )) 
		   | (AT WORD (ALPHANUMERIC)*) /* Action name in skeletons. no index or assignment */ ;
VAR_INDEX : LEFT_CURL (INT* VARIABLE | INT+) ((COMMA (INT* VARIABLE | INT+))*) RIGHT_CURL;
VAR_ASS : ASSIGNMENT ((INT* VARIABLE | INT+) | BOTTOM);

/* Characters */
WORD : ('a'..'z' | 'A'..'Z')+;
INT : [0-9]+;
UNDERSCORE : '_';
DOT : '.';
COMMA : ',';
SEMI : ';';

ALPHANUMERIC : (WORD|INT|UNDERSCORE|DOT)+;

/* Guard Operators */
AND_GUARD : '_&_';
OR_GUARD : '_|_';
COMM_GUARD : '->';
BOTTOM : '/|\\';

/* Boolean Operators */
AND : '&';
OR : '|';
NOT : '!';
IMPLICATION : '=>';  
EQUIVALENCE : '<=>';

/* Assignment */
ASSIGNMENT : '=';

WS : [ \t\r\n]+ -> skip ; //Skip White Spaces

