package parsing;

public enum TokenType {
	OUT, IN,
	NEWLINE, 
	ADD, SUB, 
	MUL, DIV,
	POWER, MODULO,
	NUMBER,
	ID, NEGATE,
	STATEMENTS,
	LBRACK, RBRACK,
	STRING, 
	TRUE, FALSE,
	BANG, 
	SMALLER, 
	BIGGER, 
	EQUALS,
	SMALLER_EQUALS,
	BIGGER_EQUALS,
	BANG_EQUALS,
	NIL, ASSIGN,
	VAR,
	IF, END, 
	ELSE, WHILE,
	NUMBER_TYPE, STRING_TYPE,
	BOOL_TYPE,
	EOF
}
