package ast;

import java.util.ArrayList;

import parsing.Token;
import parsing.TokenType;

//a node of the abstract syntax tree which has AST-child-nodes which have nodes in turn and so on...
public class AST {
	private ArrayList<AST> childs = new ArrayList<>(); //the children nodes of this ast node
	private Token token; //the token of the ast which acts as an identifier and a value at the same time
	
	public AST(Token token) {
		super();
		this.token = token;
	}
	
	//adds a child to childs
	public void addChild(AST child) {
		childs.add(child);
	}
	
	

	
	//returns the type of the token
	public TokenType getType() {
		return token.type;
	}
	
	//returns the value of the token
	public String getValue() {
		return token.value;
	}
	
	//returns the line of the token
		public int getLine() {
			return token.line;
		}
	
	//returns the token
	public Token getToken() {
		return token;
	}
	
	//returns the childs
	public ArrayList<AST> getChilds() {
		return childs;
	}
	
	//returns the number of childs of the ast
	public int childCount() {
		return childs.size();
	}
	
	//returns the child with index i
	public AST getChild(int i) {
		return childs.get(i);
	}
	
}
