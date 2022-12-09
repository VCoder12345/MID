package ast;

import parsing.Token;

//a node for an in-statement
public class InStmt extends AST {

	public InStmt(Token token, VarNode varNode) {
		super(token);
		
		addChild(varNode);
	}

}
