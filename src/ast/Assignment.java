package ast;

import parsing.Token;

//a node for a assignment
public class Assignment extends AST {
	
	//var '=' expr
	public Assignment(VarNode var, Token token, Expr expr) {
		super(token);
		
		addChild(var);
		addChild(expr);
	}
}
