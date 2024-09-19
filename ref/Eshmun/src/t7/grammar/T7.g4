/**
 * Grammar for LogicExpression
 * 
 * Arithmetic precedences (Lowest to highest):
 * - Addition, Subtraction.
 * - Multiplication, Division, Mod.
 * - Power.
 * - Parenthesis.
 * 
 * Boolean precedences (Lowest to highest):\
 * - Equivalence.
 * - And.
 * - Or.
 * - Not.
 * - Equals, Greater/equals, Less/equals.
 * - Parenthesis.
 * 
 * Expressions are Left associative.
 */
grammar T7;

/* A general statement */
statement : (structure_definition | var_definition | instantiation | comment | clear | for_loop | while_loop | if_cond | load | export | start | list | del | search | extract) (comment | ) new_line;


/* Statements */
var_definition : (LET | ) identifier EQ value;

instantiation : INSTANCE identifier LT range (COMMA range)* GT; //SPECS

structure_definition : (DEFINE | DEFINEO) identifier LT identifier (COMMA identifier)* GT lines LC lines definition_body RC;

/* Export / run / start */
clear : CLEAR;
load : LOAD any;
export : (EXPORT |EXPORTO) any;
start : START;
list : LIST;
search : SEARCH any;
del : DEL header+;
extract : EXTRACT header+;

header : identifier LP (identifier | INT) (COMMA (identifier | INT))* RP;

/* Control */
for_loop : FOR identifier IN range code_body;
while_loop : WHILE condition new_line code_body;
if_cond : IF condition new_line lines if_body (else_cond | ) END;

code_body : lines statement* END;
else_cond : ELSE (new_line | ) if_body;
if_body : lines statement*;
condition : bool_exp;

/* Structure Definition */
definition_body : states_def transitions_def specifications_def;

states_def : STATES lines state_def*;
transitions_def : TRANSITIONS lines transition_def*;
specifications_def : (SPECIFICATIONS lines ~(RC)*) lines;
lines : new_line*;

state_def : def_term TP def_term ((TP def_term (TP def_term | )) | ) SEMI lines;
transition_def : def_term TP def_term ((TP def_term TP def_term) | TP def_term | ) SEMI lines;

def_term : (~(TP | SEMI))+;

/* Identifiers */
identifier : ID;

/* Instantiations */
range : value | identifier SEMI bool_exp;

/* Arithmetic Expressions */
arith_exp : atomic_arith_exp | arith_exp1 | arith_exp2 | arith_exp3;

arith_exp1 : atomic_arith_exp | arith_exp2 (PLUS | MINUS) arith_exp;
arith_exp2 : atomic_arith_exp | arith_exp3 (MULT | DIV | MOD) arith_exp;
arith_exp3 : atomic_arith_exp | atomic_arith_exp POW arith_exp;

atomic_arith_exp : INT | identifier | LP arith_exp RP  | MINUS atomic_arith_exp;

/* Boolean Expressions */
bool_exp : atomic_bool_exp | bool_exp1 | bool_exp2 | bool_exp3;

bool_exp1 : atomic_bool_exp | bool_exp2 BQ bool_exp | bool_exp2 NQ bool_exp;
bool_exp2 : atomic_bool_exp | bool_exp3 AND bool_exp;
bool_exp3 : atomic_bool_exp | atomic_bool_exp OR bool_exp;

atomic_bool_exp : TRUE | FALSE | identifier | bool_arith_exp | LP bool_exp RP | NOT atomic_bool_exp;

bool_arith_exp : arith_exp bool_op arith_exp;
bool_op : GT | GE | LT | LE | BQ | NQ;

/* Values */
value : identifier | arith_exp | triplet | bool_exp;
triplet : (triplet_term TP triplet_term) | (triplet_term TP triplet_term TP triplet_term);
triplet_term : arith_exp;

/* Comment */
comment : COMMENT any;

/* New Lines and Characters */
new_line : LINE;
any : ~( LINE )+;

/* Boolean operators */
LINE : ('\r' | '\n')+;
COMMENT : '//';

TRUE : 'true';
FALSE : 'false';

GT : '>';
GE : '>=';
LT : '<';
LE : '<=';
BQ : '==';
NQ : '!=';

AND : '&&';
OR : '||';
NOT : '!';

/* Arithmetic */
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';
POW : '^';

/* Language Constructs */
CLEAR : 'clear';
LET : 'let';
INSTANCE : 'instance';
DEFINEO : '@define';
DEFINE : 'define';

FOR : 'for';
WHILE : 'while';
IF : 'if';
ELSE : 'else';
END : 'end';
IN : 'in';

LOAD : 'load';
EXPORTO : '@export';
EXPORT : 'export';
START : 'start';
LIST : 'list';
DEL : 'del';
EXTRACT : 'extract';
SEARCH : 'search';

STATES : 'states:';
TRANSITIONS : 'transitions:';
SPECIFICATIONS : 'specifications:';

EQ : '=';
TP : ':';
SEMI : ';';
COMMA : ',';
LP : '(';
RP : ')';
LC : '{';
RC : '}';
LS : '[';
RS : ']';
DOT : '.';

SINGLEAND : '&';
SINGLECOL : '|';

/* Identifiers */
ID : LETTER (LETTER | DIGIT | UNDERSCORE)*;
INT : DIGIT+;

fragment UNDERSCORE : '_';
fragment DIGIT : [0-9];
fragment LETTER : ('a'..'z' | 'A'..'Z'); 

/* White Space */
WS : [ \t]+ -> skip ;
ANYTHING_ELSE : .; /* any symbol could be in a comment (i,e in ANY) so we need a lexer for any symbol */