lexer grammar SpecLexer;

options {
  language = Java;
}

@header {
  package eshmun.parser.antlr.lexer;
}


//propositional operators
PROPOSITIONAL_CONNECTIVE : '|' | '&' | '=>' | '<=>';
NOT_CONNECTIVE : '!';

//arithmetic

COMPARISON_OPERATOR : '<' | '<=' | '>' | '>=' | '==' | '!=';
NEGATIVE_SIGN : '-';
CONSTANT : (NEGATIVE_SIGN)? (DIGIT)+ ;


LEFT_PARANTHESIS : '(';
RIGHT_PARANTHESIS : ')';
LEFT_BRACKET : '[';
RIGHT_BRACKET : ']';

//CTL
CTL_BRANCH_OP : 'A' | 'E';
CTL_PATH_CONNECTIVE : 'U' | 'V' |'W' ;
CTL_PATH_OP : ('X' | 'F' | 'G') ;
//MPCTL
MPCTL_CONNECTIVE: 'AND' (SMALL_LETTER)+ | 'OR' (SMALL_LETTER)+;

BOOLEAN : 'true' | 'false';
STRING : '\'' (LETTER|DIGIT)*  '\'';

//variables
//ATOMIC_PROPOSITION : (CAPS_LETTER)+ (DIGIT)+;
VAR : (LETTER|DIGIT|'_'|'.')+;
//AUTOMATON_VAR: (LETTER|DIGIT)*;

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
    