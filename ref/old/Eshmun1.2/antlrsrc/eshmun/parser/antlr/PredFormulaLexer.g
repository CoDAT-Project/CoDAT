lexer grammar PredFormulaLexer;

options {
  language = Java;
}

@header {
  package eshmun.parser.antlr;
}

PROPOSITIONAL_CONNECTIVE : '|' | '&' ;
NOT_CONNECTIVE : '!';

ARITHMETIC_OPERATOR : '<' | '<=' | '>' | '>=' | '==' | '!=';
NEGATIVE_SIGN : '-';
CONSTANT : (NEGATIVE_SIGN)? (DIGIT)+;

LEFT_PARANTHESIS : '(';
RIGHT_PARANTHESIS : ')';

ATOMIC_PROPOSITION :  (CAPS_LETTER)+ (DIGIT)+;
VAR : (SMALL_LETTER)+ (DIGIT)*;


fragment CAPS_LETTER 
  : 'A'..'Z';

fragment SMALL_LETTER 
  : 'a'..'z';

fragment LETTER 
  : SMALL_LETTER 
  | CAPS_LETTER;

fragment DIGIT
  : '0'..'9';
 
fragment NOTHING
  : '#'; 
  
WS  : (' '|'\r'|'\t'|'\u000C'|'\n') {_channel=99;}
    ;
  
  