package vm;

//has static final byte variables for all possible operations 
public class Op {
	public static final byte CONSTANT = 1;
	public static final byte ADD = 2;
	public static final byte SUB = 3;
	public static final byte MUL = 4;
	public static final byte DIV = 5;
	public static final byte POWER = 6;
	public static final byte OUT = 8;
	public static final byte NEGATE = 9;
	public static final byte MODULO = 10;
	public static final byte TRUE = 11;
	public static final byte FALSE = 12;
	public static final byte NOT = 13;
	public static final byte SMALLER = 14;
	public static final byte BIGGER = 15;
	public static final byte SMALLER_EQUALS = 16;
	public static final byte BIGGER_EQUALS = 17;
	public static final byte EQUALS = 18;
	public static final byte BANG_EQUALS = 19;
	public static final byte NIL = 20;
	public static final byte SET_GLOBAL = 21;
	public static final byte GET_GLOBAL = 22;
	public static final byte FALSE_JUMP = 23;
	public static final byte JUMP = 24;
	public static final byte JUMP_BACK = 25;
	public static final byte IN = 26;
	public static final byte NUM_CAST = 27;
	public static final byte STRING_CAST = 28;
	public static final byte BOOL_CAST = 29;
}
