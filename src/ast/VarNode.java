package ast;

import parsing.Token;

//a node for a simple variable
public class VarNode extends Expr {
	
	//the type of the token is ID, the value the name of the variable
	public VarNode(Token token) {
		super(token);
	}
	
}
