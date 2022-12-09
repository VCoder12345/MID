package parsing;

import static parsing.TokenType.*; 

//generates tokens for parsing from a string
public class Lexer {
	private String input; //the input-code as text
	private char current; //the current char
	private int position = -1; //the position of the current char --> is -1 so that the first char can be read in the constructor
	private int currentLine = 1; //the current line --> used for debugging
	
	private static final char EOF_CHAR = (char)-1; //the last char, when the lexer has looked at all the chars of the input
	
	public Lexer(String input) {
		this.input = input;
		//reads the first char of the input
		readNext();
	}
	
	//prints out error-messages
	private ParsingError error(String msg) {
		System.err.println(msg + " in line " + currentLine);
		return new ParsingError();
	}
	
	//generates the next token
	//is called by the parser
	public Token nextToken() {
		//runs the while-loop until either a token is returned or the input-string was read completely and current = EOF_CHAR
		while(current != EOF_CHAR) {
			//what should be done with the char? Should it be skipped? Should a token be generated?
			switch(current) {
			case ' ': case '\r': case '\t': 
				//skips whitespaces and other unimportant chars
				readNext();
				continue;
			case '\n':
				++currentLine; //increments current-line to keep track, on which line we are right now
				return makeToken(NEWLINE);
			case '+':
				return makeToken(ADD);
			case '-':
				return makeToken(SUB);
			case '*':
				return makeToken(MUL);
			case '/':
				return makeToken(DIV);
			case '^':
				return makeToken(POWER);
			case '%':
				return makeToken(MODULO);
			case '(':
				return makeToken(LBRACK);
			case ')':
				return makeToken(RBRACK);
			case '!':
				readNext();
				//is the token a BANG (=) or a BANG_EQUALS (!=)
				if(current == '=') {
					return makeToken(BANG_EQUALS, "!=");
				}else {
					return newToken(BANG);
				}
			case '=':
				readNext();
				if(current == '=') {
					return makeToken(EQUALS, "==");
				}
				
				return newToken(ASSIGN, "=");
			case '<':
				readNext();
				if(current == '=') {
					return makeToken(SMALLER_EQUALS, "<=");
				}else {
					return newToken(SMALLER, "<");
				}
			case '>':
				readNext();
				if(current == '=') {
					return makeToken(BIGGER_EQUALS, ">=");
				}else {
					return newToken(BIGGER, ">");
				}
			default:
				//if the char was none of the cases above:
				//checks if the char is the beginning of a number (is it a digit?)
				//or the beginning of a text (is it alphabetical?)
				//or the beginning of a string (is it '"'?)
				//or the beginning of a comment (is it ';'?)
				if(isDigit()) {
					return number();
				}else if(isAlphabetical()) {
					return text();
				}else if(current == '"') {
					//'"' should not be added to the content of the string
					readNext();
					return string();
				}else if(current == ';') {
					skipComment();
					continue;
				}
				//otherwise an error is thrown that the char could not be recognized
				throw error("could not recognize the char '" + current + "'");
			}
		}
		
		//the EOF_Token is returned when the lexer is finished with the input-string
		return newToken(EOF, "<eof>");
		
	}
	
	//generates a new token with a type, the current char and the current-line
	private Token newToken(TokenType type) {
		return newToken(type, String.valueOf(current));
	}
	
	//generates a new token with a type and a value and the current-line
	private Token newToken(TokenType type, String val) {
		return new Token(type, val, currentLine);
	}
	
	//is the current char a digit?
	private boolean isDigit() {
		return current >= '0' && current <= '9';
	}
	
	//is the current char an alphabetical string?
	private boolean isAlphabetical() {
		return (current >= 'a' && current <= 'z') || (current >= 'A' && current <= 'Z');
	}
	
	//skips comments
	private void skipComment() {
		do {
			readNext();
		}while(current != '\n');
	}
	
	//generates a identifier- or keyword-token
	private Token text() {
		//adds all following chars that can be part of a identifier
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(current);
			readNext();
		}while(isAlphabetical() || isDigit());
		
		String val = buf.toString();
		
		//is it a keyword?
		switch(val) {
		case "out":
			return newToken(OUT, val);
		case "true":
			return newToken(TRUE, val);
		case "false":
			return newToken(FALSE, val);
		case "nil":
			return newToken(NIL, val);
		case "var":
			return newToken(VAR, val);
		case "in":
			return newToken(IN, val);
		case "if":
			return newToken(IF, val);
		case "end":
			return newToken(END, val);
		case "else":
			return newToken(ELSE, val);
		case "while":
			return newToken(WHILE, val);
		case "num":
			return newToken(NUMBER_TYPE);
		case "str":
			return newToken(STRING_TYPE);
		case "bool":
			return newToken(BOOL_TYPE);
		}
		
		//if it was not a keyword, an ID-Token is generated
		return newToken(ID, val);
	}
	
	//generates a string-token
	//starts and ends with a '"'
	private Token string() {
		//adds all following chars until a '"' is reached
		StringBuilder buf = new StringBuilder();
		while(current != '"') {
			buf.append(current);
			readNext();
		}
		
		//uses makeToken, because it has to read the '"' too, otherwise it would recognize the '"' as another beginning of a string
		return makeToken(STRING, buf.toString());
	}
	
	//generates a number-token
	private Token number() {
		//adds all following digit-chars and optional decimal places
		StringBuilder buf = new StringBuilder();
		
		do {
			buf.append(current);
			readNext();
		}while(isDigit());
		
		//is it a decimal? --> read the digits after the colon too 
		if(current == '.') {
			do {
				buf.append(current);
				readNext();
			}while(isDigit());
		}
		
		return newToken(NUMBER, buf.toString());
	}

	
	//creates a new token with a type and the value of the current char and reads the next char
	private Token makeToken(TokenType type) {
		return makeToken(type, String.valueOf(current));
	}
	
	//creates a new token with a type and a value and reads the next char
	private Token makeToken(TokenType type, String val) {
		readNext();
		return newToken(type, val);
	}
	
	//reads the next char of the input-string
	private void readNext() {
		++position;
		if(position < input.length()) {
			current = input.charAt(position);
		}else {
			//when the lexer has looked at all the chars of the input
			current = EOF_CHAR;
		}
	}
	
	
	
}
