package ast;

import parsing.Token;

//a node for an input-expression
public class InExpr extends Expr {
	
	public InExpr(Token token, Expr right) {
		super(token);
		
		addChild(right);
	}

}
