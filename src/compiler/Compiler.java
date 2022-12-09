package compiler;

import static parsing.TokenType.*;

import java.util.HashMap;

import ast.*;
import vm.Code;
import vm.Op;
import vm.VMError;
import vm.Value;

//walks the AST and generates Bytecode
public class Compiler {
	public Code code = new Code(); //the compiler writes the bytecode into this code-instance
	//and it is later interpreted by the Virtual Machine
	public int currentLine; //the line of the current ast node (the line of the token of the ast)
	public HashMap<String, Byte> globals = new HashMap<>(); //a dictionary for the variables (the globals): 
	//the key is the name and the value the id of the global
	private static final int MAX_JUMP_OFFSET = Short.MAX_VALUE; //the max offset that is possible 
	//to represent with two bytes
	
	public Compiler() {
	}
	
	//looks at the type of the token of the node 
	//and calls an appropriate function or just emits the right bytes
	//most of the functions are also overloaded compile functions 
	//which take as a parameter the node cast to the specific type for the function
	public void compile(AST node) {
		//gets the line of the token of the ast node
		currentLine = node.getLine();
		//what bytecode should be generated based on the type of the token of the ast
		switch(node.getType()) {
		case TRUE:
			emit(Op.TRUE);
			break;
		case NIL:
			emit(Op.NIL);
			break;
		case NUMBER:
			compileNumber((Expr) node);
			break;
		case STRING:
			compileString((Expr) node);
			break;
		case FALSE:
			emit(Op.FALSE);
			break;
		case ADD: case SUB: case MUL: case DIV: case POWER: case MODULO: 
		case SMALLER, SMALLER_EQUALS, BIGGER, BIGGER_EQUALS, EQUALS, BANG_EQUALS:
			compile((BinOp) node);
			break;
		case NEGATE, BANG, NUMBER_TYPE, STRING_TYPE, BOOL_TYPE:
			compile((PrefixOp) node);
			break;
		case OUT:
			compile((OutStmt) node);
			break;
		case STATEMENTS:
			for(AST child : node.getChilds()) {
				compile(child);
			}
			
			break;
		case VAR:
			compile((VarDecl) node);
			break;
		case ASSIGN:
			compile((Assignment) node);
			break;
		case ID:
			compileID((Expr) node);
			break;
		case IF:
			compile((IfStmt)node);
			break;
		case WHILE:
			compile((WhileStmt)node);
			break;
		case IN:
			compile((InExpr)node);
			break;
		default:
			break;
		}
	}
	
	private void compile(IfStmt node) {
		//compile the conditional
		compile(node.getChild(0));
		//emit a false-jump for jumping if the conditional is false
		int thenJump = emitJump(Op.FALSE_JUMP);
		compile(node.getChild(1));
		

		//checks if there's a else-statement
		if(node.childCount() > 2) {
			//if this jump is reached, the else-statements should be skipped --> jump
			int elseJump = emitJump(Op.JUMP);
			//backpatches the thenJump, so it jumps to the else-statement
			backpatching(thenJump);
			compile(node.getChild(2));
			backpatching(elseJump);
		}else {
			//backpatches the thenJump, so it jumps to the end of the if-statement
			backpatching(thenJump);
		}

	}
	
	private void compile(WhileStmt node) {
		int whileCondPos = code.data.size();
		//compile the conditional
		compile(node.getChild(0));
		//emit a false-jump for jumping if the conditional is false
		int thenJump = emitJump(Op.FALSE_JUMP);
		compile(node.getChild(1));
		
		emitJumpBack(whileCondPos);

		//backpatches the thenJump, so it jumps to the end of the while-statement
		backpatching(thenJump);

	}
	
	//prints out error-messages for the compiler
	private CompilerError error(String msg) {
		System.err.println(msg + " in line " + currentLine);
		return new CompilerError();
	}
	
	public void compileID(Expr node) {
		//resolves the global, gets the id and emits bytecode for getting the global
		byte id = resolveGlobal(node.getValue());
		emit(Op.GET_GLOBAL, id);
	}
	
	public void compile(VarDecl node) {
		//creates a global, adds it to the globals dictionary and emits bytecode to define it
		VarNode varNode = (VarNode) node.getChild(0);
		byte id = (byte) code.numGlobals;
		globals.put(varNode.getValue(), id);
		
		compile(node.getChild(1));
		emit(Op.SET_GLOBAL, id);
		
		++code.numGlobals;
	}
	
	public void compile(Assignment node) {
		//resolves the variable for this assignment and then emits bytecode for the assignment with the right id
		VarNode varNode = (VarNode) node.getChild(0);
		String varName = varNode.getValue();
		
		byte id = resolveGlobal(varName);
		
		compile(node.getChild(1));
		
		emit(Op.SET_GLOBAL, id);
	}
	
	//resolves a global, by checking if the variable "varName" exists and returning it true the id of it
	public byte resolveGlobal(String varName) {
		if(!globals.containsKey(varName)) {
			throw error("the variable " + varName + " is not defined");
		}
		byte id = globals.get(varName);
		
		return id;
	}
	
	//compiles out-statement
	public void compile(OutStmt node) {
		compile(node.getChild(0));
		
		emit(Op.OUT);
	}
	
	public void compile(InExpr node) {
		compile(node.getChild(0));
		
		emit(Op.IN);
	}
	
	//handels prefix-operators, like negation, casting, etc.
	public void compile(PrefixOp node) {
		compile(node.getChild(0));
		
		//emit bytecode for the specific prefix-operator
		switch(node.getType()) {
		case NEGATE:
			emit(Op.NEGATE);
			break;
		case BANG:
			emit(Op.NOT);
			break;
		case NUMBER_TYPE:
			emit(Op.NUM_CAST);
			break;
		case STRING_TYPE:
			emit(Op.STRING_CAST);
			break;
		case BOOL_TYPE:
			emit(Op.BOOL_CAST);
			break;
		}
		
	}
	
	//emits bytecode for binary operations like addition, subtraction, multiplication, etc.
	public void compile(BinOp node) {
		compile(node.getChild(0));
		compile(node.getChild(1));
		
		//which binary operation is it?
		switch(node.getType()) {
		case ADD:
			emit(Op.ADD);
			break;
		case SUB:
			emit(Op.SUB);
			break;
		case MUL:
			emit(Op.MUL);
			break;
		case DIV:
			emit(Op.DIV);
			break;
		case POWER:
			emit(Op.POWER);
			break;
		case MODULO:
			emit(Op.MODULO);
			break;
		case SMALLER:
			emit(Op.SMALLER);
			break;
		case BIGGER:
			emit(Op.BIGGER);
			break;
		case SMALLER_EQUALS:
			emit(Op.SMALLER_EQUALS);
			break;
		case BIGGER_EQUALS:
			emit(Op.BIGGER_EQUALS);
			break;
		case EQUALS:
			emit(Op.EQUALS);
			break;
		case BANG_EQUALS:
			emit(Op.BANG_EQUALS);
			break;
		}
	}
	
	//emits bytecode for a number
	public void compileNumber(Expr node) {
		double numberVal = Double.parseDouble(node.getValue());
		emitConstant(new Value(numberVal));
	}
	
	//emits bytecode for a string
	public void compileString(Expr node) {
		emitConstant(new Value(node.getValue()));
	}
	
	//stores the value in the constant-pool and emits a constant-op and the index
	private void emitConstant(Value value) {
		byte index = code.addConstant(value);
		emit(Op.CONSTANT, index);
	}
	
	//emits a jump instruction and an empty offset which is later changed to the real jump offset by backpatching
	private int emitJump(byte jumpInstr) {
		emit(jumpInstr);
		emit((byte)0xff);
		emit((byte)0xff);
		return code.data.size() - 2;
	}
	
	//emits a jump instruction back to address
	private void emitJumpBack(int address) {
		int offset = code.data.size() - address + 3;
		if(offset > MAX_JUMP_OFFSET) {
			//the offset is to big for two bytes
			throw error("Too much code to jump over");
		}
		emit(Op.JUMP_BACK);
		emit((byte)((offset >> 8) & 0xff));
		emit((byte)(offset & 0xff));
	}
	
	//uses backpatching to change the empty jump-offset to the one lading on the next instruction
	//jumpPos is the position of the jump-instruction in the code
	private void backpatching(int jumpPos) {
		int offset = code.data.size() - jumpPos - 2;
		
		if(offset > MAX_JUMP_OFFSET) {
			//the offset is to big for two bytes
			throw error("Too much code to jump over");
		}
		
		code.data.set(jumpPos, (byte)((offset >> 8) & 0xff));
		code.data.set(jumpPos + 1, (byte)(offset & 0xff));
	}
	
	
	//writes bytes into the code
	private void emit(byte... bytes) {
		for(byte b : bytes) {
			code.write(b, currentLine);
		}
	}
}
