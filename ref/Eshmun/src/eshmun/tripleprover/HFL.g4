/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

grammar HFL;


triple
   : (p s q)+
   ;

p: TOK_CURLY_LEFT formula TOK_CURLY_RIGHT ;

s:  block ;

q: TOK_CURLY_LEFT formula TOK_CURLY_RIGHT;

formula
   : ((FORALL | EXISTS) ( VARIABLE))? disjunction (TOK_OP_OR|TOK_OP_AND ((FORALL | EXISTS) ( VARIABLE))? disjunction )*
   ;

disjunction
   : conjunction (TOK_OP_OR conjunction)*
   ;

conjunction
   : negation (TOK_OP_AND negation)*
   ;

negation
   : TOK_OP_NOT? (predicate | LPAREN formula RPAREN)
   ;

predicate
   : binaryPredicate 
   | PREPOSITION
   ;

binaryPredicate
  : LPAREN term (TOK_OP_REL) term RPAREN
  ;

term
   : function
   | VARIABLE
   ;

function
   : binaryFunction
   | CONSTANT
   ;

binaryFunction
   : LPAREN (CONSTANT | VARIABLE) (TOK_OP_ADD | TOK_OP_TIMES) (CONSTANT | VARIABLE) RPAREN
   ;



statement :   CONSTANT TOK_OP_ASSIGN expr  TOK_SEMI
            | TOK_IF LPAREN ifcondition RPAREN statement (TOK_ELSE statement)?
            | block
            | TOK_SEMI ;


block : TOK_CURLY_LEFT ( statement )* TOK_CURLY_RIGHT ;


ifcondition : ifcondTerm ( TOK_OP_OR ifcondTerm )* ;

ifcondTerm : ifcondFact ( TOK_OP_AND ifcondFact )* ;

ifcondFact : expr relop expr ;

expr : ( addop )? exprterm ( addop exprterm )* ;

exprterm: factor ( mulop factor )* ;

factor :
        | CONSTANT
        | INT
        | LPAREN expr RPAREN ;


relop : TOK_OP_REL ;


addop : TOK_OP_ADD ;


mulop : TOK_OP_TIMES ;



LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;

TOK_CURLY_LEFT: '{';
TOK_CURLY_RIGHT: '}';

TOK_IF       :   'if'     ;
TOK_ELSE       :   'else'     ;
TOK_SEMI      :   ';'     ;

//Sentenial Connectives



TOK_OP_NOT          :   '!'     ;
TOK_OP_OR           :   '|'     ;
TOK_OP_AND          :   '&'     ;
TOK_OP_IMPLIES      :   '=>'    ;
TOK_OP_EQUIVALENCE  :   '<=>'    ;





FORALL
   : 'forall'
   ;


EXISTS
   : 'exists'
   ;


VARIABLE
   : '?' (('a' .. 'z') | ('0' .. '9')) CHARACTER*
   ;

CONSTANT
   : (('a' .. 'z') | ('-')?('0' .. '9')) CHARACTER*
   ;


PREPOSITION
   : ('A' .. 'Z') CHARACTER*
   ;



INT : DIGIT+;



//Operators

TOK_OP_REL         :   '='       //OP_EQ
                   |   '!='       //OP_NE
                   |   '<'        //OP_LT
                   |   '<='       //OP_LE
                   |   '>'        //OP_GT
                   |   '>='    ;  //OP_GE


TOK_OP_ASSIGN   :   ':='    ;    //OP_ASSIGN

//Arithmetic
TOK_OP_ADD           :   '+'         //OP_ADD_PLUS
                     |   '-'     ;  //OP_ADD_MINUS
TOK_OP_TIMES         :   '*'         //OP_MUL_TIMES
                     |   '/'    ;     //OP_MUL_DIV





fragment UNDERSCORE : '_';
fragment DIGIT : [0-9];
fragment LETTER : ('a'..'z' | 'A'..'Z');


fragment CHARACTER
   : ('0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '_')
   ;


WS
   : (' ' | '\t' | '\r' | '\n') + -> skip
   ;

