package ast;

import parsing.Token;

//a node for an operator that stands in front of the operand: e.g "-", "!" --> -5, !true
public class PrefixOp extends Expr {
	
	//op-token, operand
	public PrefixOp(Token token, Expr right) {
		super(token);
		
		addChild(right);
	}

}
