package vm;

import java.util.ArrayList;

//the class in which bytecode is saved by the compiler and read by the virtual machine
public class Code {
	public ArrayList<Byte> data = new ArrayList<>(); //the byte-data of the code (instructions, parameters, etc.)
	public ArrayList<Value> constantPool = new ArrayList<>(); //values are stored in the constant-pool and the index is stored as a parameter in the data
	public ArrayList<Integer> lines = new ArrayList<>(); //stores the line of every emitted byte with the same index --> for debug messages
	public int numGlobals = 0; //the number of globals --> is increased when a new global is defined --> for giving each global an id
	
	//adds a byte to the data
	public void write(byte x, int currentLine) {
		data.add(x);
		lines.add(currentLine);
	}
	
	//stores a value in the constant-pool and returns the index
	public byte addConstant(Value value) {
		constantPool.add(value);
		
		return (byte)(constantPool.size() - 1);
	}
	
	//prints out the instructions and their parameters for debugging
	public void printCode() {
		int ip = 0;
		while(ip < data.size()) {
			byte instr = data.get(ip);
			ip = printOp(instr, ip);
			
			++ip;
		}
	}
	
	//prints an operation (instr) --> for debugging
	public int printOp(byte instr, int ip) {
		System.out.print(String.format("%04d", ip) + "   ");
		switch(instr) {
		case Op.ADD:
			return printSimpleOp("add", ip);
		case Op.SUB:
			return printSimpleOp("sub", ip);
		case Op.MUL:
			return printSimpleOp("mul", ip);
		case Op.DIV:
			return printSimpleOp("div", ip);
		case Op.POWER:
			return printSimpleOp("power", ip);
		case Op.CONSTANT:
			return printValueOp("constant", ip);
		case Op.OUT:
			return printSimpleOp("out", ip);
		case Op.NEGATE:
			return printSimpleOp("negate", ip);
		case Op.TRUE:
			return printSimpleOp("true", ip);
		case Op.FALSE:
			return printSimpleOp("false", ip);
		case Op.NOT:
			return printSimpleOp("not", ip);
		case Op.SET_GLOBAL:
			return print1ParameterOp("set-global", ip);
		case Op.GET_GLOBAL:
			return print1ParameterOp("get-global", ip);
		case Op.SMALLER:
			return printSimpleOp("smaller", ip);
		case Op.SMALLER_EQUALS:
			return printSimpleOp("smaller-equals", ip);
		case Op.BIGGER:
			return printSimpleOp("bigger", ip);
		case Op.BIGGER_EQUALS:
			return printSimpleOp("bigger-equals", ip);
		case Op.FALSE_JUMP:
			return printJump("false-jump", ip);
		case Op.JUMP:
			return printJump("jump", ip);
		case Op.JUMP_BACK:
			return printJump("jump-back", ip);
		case Op.IN:
			return printSimpleOp("in", ip);
		case Op.BOOL_CAST:
			return printSimpleOp("bool-cast", ip);
		case Op.NUM_CAST:
			return printSimpleOp("num-cast", ip);
		case Op.STRING_CAST:
			return printSimpleOp("string-cast", ip);
		case Op.MODULO:
			return printSimpleOp("modulo", ip);
		case Op.EQUALS:
			return printSimpleOp("equals", ip);
		}
		
		throw new RuntimeException("could not recognize instruction " + instr);
	}
	
	//prints an instruction which has as a parameter an index to the constant-pool (Op.CONSTANT) --> for debugging
	private int printValueOp(String name, int ip) {
		System.out.print(name);
		System.out.print(" ");
		
		byte index = data.get(ip + 1);
		Value value = constantPool.get(index);
		System.out.println(value.dataToString());
		
		return ip + 1;
	}
	
	//prints a jump-instruction (Op.JUMP, Op.JUMP_BACK, etc.) --> for debugging
	private int printJump(String name, int ip) {
		short offset = (short) (data.get(ip + 2) | (data.get(ip + 1) << 8)); 
		System.out.println(name + " " + offset);
		return ip + 2;
	}
	
	//prints an instruction without any parameter (Op.OUT, Op.ADD, etc.) --> for debugging
	private int printSimpleOp(String name, int ip) {
		System.out.println(name);
		
		return ip;
	}
	
	//prints an instruction with one parameter (Op.SET_GLOBAL, Op.GET_GLOBAL) --> for debugging
	private int print1ParameterOp(String name, int ip) {
		System.out.print(name);
		System.out.print(" ");
		byte index = data.get(ip + 1);
		System.out.println(index);
		
		return ip + 1;
	}
}
