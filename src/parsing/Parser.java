package parsing;

import static parsing.TokenType.*;


import ast.AST;
import ast.Assignment;
import ast.BinOp;
import ast.Expr;
import ast.IfStmt;
import ast.InExpr;
import ast.OutStmt;
import ast.PrefixOp;
import ast.VarDecl;
import ast.VarNode;
import ast.WhileStmt;

//uses a lexer to generate tokens on the fly and parses them to generate an AST
//uses recursive-descent 
//the functions of the parser are mostly derived of the grammar of MID
public class Parser {
	private static final int MAX_LOOKAHEAD = 2; //how big should the lookahead-buffer be?
	private Lexer lexer; //the lexer which generates the tokens
	private Token[] lookaheadBuffer = new Token[MAX_LOOKAHEAD]; //a buffer of the next MAX_LOOKAHEAD tokens
	private Token lastToken; //the token before the current one
	private int bufferPos = 0; //the position of the current token in the buffer
	
	public Parser(Lexer lexer) {
		super();
		this.lexer = lexer;
		//initializes the lookahead buffer
		fillLookaheadBuffer();
	}
	
	//fills the empty lookahead buffer with tokens until it's full
	private void fillLookaheadBuffer() {
		for(int i = 0; i < MAX_LOOKAHEAD; ++i) {
			lookaheadBuffer[i] = lexer.nextToken();
		}
	}

	//la=look ahead -> looks x tokens ahead (x < MAX_LOOKAHEAD)
	private Token la(int x) {
		return lookaheadBuffer[(bufferPos + x) % MAX_LOOKAHEAD];
	}
	
	//lt = looks x tokens ahead (x < MAX_LOOKAHEAD) and returns it's type
	private TokenType lt(int x) {
		return la(x).type;
	}

	//prints out error-messages
	private ParsingError error(String msg) {
		System.err.println(msg + " in line " + la(0).line);
		return new ParsingError();
	}
	
	//if the type of the current token equals "type", the parser gets the next token otherwise it produces an error
	private void consume(TokenType type) {
		if(lt(0) == type) {
			advance();
		}else {
			throw error("expected a token of type " + type + " but found " + la(0));
		}
	}
	
	//generates the next token with the lexer and saves it in the lookahead buffer
	private void advance() {
		lastToken = la(0);
		
		lookaheadBuffer[bufferPos] = lexer.nextToken();
		bufferPos = (bufferPos + 1) % MAX_LOOKAHEAD;
	}
	
	//if the type of the current token equals any of the types of "types", the parser advances and returns true; otherwise the parser just returns false
	private boolean match(TokenType...types) {
		if(check(types)) {
			advance();
			return true;
		}
		
		return false;
	}
	
	//checks if the current-token-type equals any of the types of "types"
	private boolean check(TokenType...types) {
		return check(0, types);
	}
	
	//checks if the token x tokens ahead equals any of the types of "types"
	private boolean check(int x, TokenType...types) {
		for(TokenType type : types) {
			if(lt(x) == type) {
				return true;
			}
		}
			
		return false;
	}
	
	//the production "primary" of the grammar
	private Expr primary() {
		if(match(LBRACK)) {
			Expr node = expr();
			consume(RBRACK);
			
			return node;
		}else {
			return type();
		}
	}
	
	//the production "type" of the grammar
	private Expr type() {
		Token typeToken = la(0);
		//is it not any of the possible types? --> error
		if(!match(NUMBER) 
				&& !match(STRING) 
				&& !match(TRUE) 
				&& !match(FALSE)
				&& !match(NIL)
				&& !match(ID)) {
			throw error("expected number, string, boolean, nil or variable but found " + la(0));
		}
		
		return new Expr(typeToken);
	}
	
	//the production "unary" of the grammar
	private Expr unary() {
		if(match(BANG)) {
			Token bangToken = lastToken;
			Expr result = primary();
			result = new PrefixOp(bangToken, result);
			
			return result;
		}else if(check(LBRACK) && check(1, NUMBER_TYPE, STRING_TYPE, BOOL_TYPE)){
			//when after the LBRACK a NUMBER_TYPE, STRING_TYPE OR BOOL_TYPE follows --> typecasting
			consume(LBRACK);
			Token castTypeToken = la(0);
			typeName();
			consume(RBRACK);
			
			return new PrefixOp(castTypeToken, primary());
		}else{
			//checks how many negate-operators are in front of the value --> only if the number of negate-operators is uneven, a negate-token is needed
			int negateOccurences = 0;
			while(check(ADD, SUB)) {
				if(match(SUB)) {
					++negateOccurences;
				}else {
					consume(ADD);
				}
			}
			
			Expr result = primary();
			if(negateOccurences % 2 != 0) {
				result = new PrefixOp(new Token(NEGATE, result.getLine()), result);
			}
			
			return result;
		}
		
	}
	
	//the production "type-name" of the grammar
	private void typeName() {
		if(!match(NUMBER_TYPE, STRING_TYPE, BOOL_TYPE)) {
			throw error("expected a number-, string- or bool-type, but found " + la(0));
		}
	}
	
	//the production "power" of the grammar
	private Expr power() {
		Expr result = unary();
		while(match(POWER)) {
			result = new BinOp(result, lastToken,  unary());
		}
		
		return result;
	}
	
	//the production "factor" of the grammar
	private Expr factor() {
		Expr result = power();
		while(match(MUL, DIV, MODULO)) {
			result = new BinOp(result, lastToken, power());
		}
		
		return result;
	}
	
	//the production "term" of the grammar
	private Expr term() {
		Expr result = factor();
		while(match(ADD, SUB)) {
			result = new BinOp(result, lastToken, factor());
		}
		
		return result;
	}
	
	//the production "comparison" of the grammar
	private Expr comparison() {
		Expr result = term();
		while(match(SMALLER, SMALLER_EQUALS, BIGGER, BIGGER_EQUALS)) {
			result = new BinOp(result, lastToken, term());
		}
		
		return result;
	}
	
	//the production "equality" of the grammar
	private Expr equality() {
		Expr result = comparison();
		while(match(EQUALS, BANG_EQUALS)) {
			result = new BinOp(result, lastToken, comparison());
		}
		
		return result;
	}
	
	//the production "expr" of the grammar
	private Expr expr()  {
		return inExpr();
	}
	
	//the production "out-statement" of the grammar
	private OutStmt outStatement() {
		Token token = lastToken;
		Expr exprVal = expr();
		
		return new OutStmt(token, exprVal);
	}
	
	//the production "assignment" of the grammar
	private Assignment assignment() {
		Token varToken = lastToken;
		Token token = la(0);
		consume(ASSIGN);
		Expr exprNode = expr();
		
		return new Assignment(new VarNode(varToken), token, exprNode);
		
	}
	
	//the production "var-decl" of the grammar
	private VarDecl varDecl() {
		Token token = lastToken;
		consume(ID);
		Token varToken = lastToken;
		consume(ASSIGN);
		Expr exprNode = expr();
		
		return new VarDecl(new VarNode(varToken), token, exprNode);
	}
	
	//the production "statement" of the grammar
	private AST statement() {
		AST node = null;
		if(match(OUT)) {
			node = outStatement();
		}else if(match(VAR)) {
			node = varDecl();
		}else if(match(ID)) {
			node = assignment();
		}else if(match(IF)) {
			node = ifStatement();
		}else if(match(WHILE)) {
			node = whileStatement();
		}
		
		//at the end of a statement there can either be a NEWLINE or the EOF Token
		if(!match(NEWLINE, EOF)) {
			throw error("expected newline at the end of a statement, but found " + la(0));
		}
		
		return node;
	}
	
	//the production "in-expr" of the grammar
	private Expr inExpr() {
		if(match(IN)) {
			return new InExpr(lastToken, equality());
		}else {
			return 	equality();
		}
	}
	
	//the production "while-statement" of the grammar
	private WhileStmt whileStatement() {
		Token token = lastToken;
		Expr conditional = expr();
		AST stmts = statements();
		
		consume(END);
		
		return new WhileStmt(token, conditional, stmts);
	}
	
	//the production "if-statement" of the grammar
	private IfStmt ifStatement() {
		Token token = lastToken;
		Expr conditional = expr();
		AST stmts = statements();
		
		AST elseStmts = null;
		if(match(ELSE)) {
			elseStmts = statements();
		}
		consume(END);
		
		return new IfStmt(token, conditional, stmts, elseStmts);
	}
	
	//the production "statements" of the grammar
	private AST statements() {
		AST result = new AST(new Token(STATEMENTS, 0));
		while(check(OUT, VAR, IF, WHILE, ID, NEWLINE)) {
			AST node = statement();
			//if the ast-node is important, add it to the ast
			if(node != null) { 
				result.addChild(node);
			}
		}
		
		return result;
	}
	
	//the main function of the parser:
	//parses the tokens generated by the lexer and returns an AST (abstract syntax tree)
	public AST parse() {
		return statements();
	}
}
