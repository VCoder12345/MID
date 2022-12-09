package ast;

import parsing.Token;

//a node for a while-statement
public class WhileStmt extends AST {
	
	//"while" expr statements
	//the conditional is the boolean expression that has to be true, to execute statements in the interpreter
	public WhileStmt(Token token, Expr conditional, AST statements) {
		super(token);
		
		addChild(conditional);
		addChild(statements);
	}

}
