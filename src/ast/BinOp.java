package ast;

import parsing.Token;

//a node for a binary operator: e.g "+", "/", "<="
public class BinOp extends Expr {
	
	//expr op expr
	public BinOp(Expr left, Token token, Expr right) {
		super(token);
		
		addChild(left);
		addChild(right);
	}

}
