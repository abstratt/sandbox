grammar URIQL;

query:
    expression EOF;

expression:
    nestedExpression | compositeExpression | comparisonExpression ;

compositeExpression:
    resolvedExpression (booleanOperator resolvedExpression)+;

resolvedExpression:
    comparisonExpression | nestedExpression;

nestedExpression: '(' expression ')';

comparisonExpression:
    property comparisonOperator literal;

comparisonOperator:
    EQ | NE | LT | LE | GT | GE;

booleanOperator:
    AND | OR;

property: IDENT;

WHITESPACE: [ \t\r\n]-> skip;


AND: 'AND';

OR: 'OR';

EQ: 'eq';

NE: 'ne';

LT: 'lt';

LE: 'le';

GT: 'gt';

GE: 'ge';

literal:
   CHAR_LITERAL | NUM_LITERAL;

IDENT : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

CHAR_LITERAL:
    '\'' ~ ['\r\n]* '\'';

NUM_LITERAL:
   ('0' .. '9') + ('.' ('0' .. '9') +)?;

