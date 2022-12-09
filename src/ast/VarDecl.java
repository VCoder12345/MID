package ast;

import parsing.Token;

//a node for the declaration of a new variable
public class VarDecl extends AST {
	
	//var '=' expr
	public VarDecl(VarNode var, Token token, Expr expr) {
		super(token);
		
		addChild(var);
		addChild(expr);
	}

}
