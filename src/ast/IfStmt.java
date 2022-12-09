package ast;

import parsing.Token;

//a node for an if-statement
public class IfStmt extends AST {
	
	//"if" expr statements
	//the conditional is the boolean expression that has to be true, to execute statements in the interpreter
	//elseStatements is the node for the statements in the else-block. It's null when there's no else-block
	public IfStmt(Token token, Expr conditional, AST statements, AST elseStatements) {
		super(token);
		
		addChild(conditional);
		addChild(statements);
		if(elseStatements != null) {
			addChild(elseStatements);
		}
	}

}
