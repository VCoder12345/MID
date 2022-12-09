package ast;

import parsing.Token;

//a node for an out-statement
public class OutStmt extends AST {
	
	//"out" expr
	//expr = the expression that is being printed to the console
	public OutStmt(Token token, Expr expr) {
		super(token);
		
		addChild(expr);
	}

}
