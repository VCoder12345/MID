package vm;

//a value
//has a type and data
public class Value {
	public ValueType type; //the type of the Value (number, string, etc.)
	public Object data; //the actual value/data the Value stores
	
	public Value(Object data, ValueType type) {
		this.data = data;
		this.type = type;
	}
	
	//inits the value as a number
	public Value(double x) {
		this(x, ValueType.NUMBER);
	}
	
	//inits the value as a string
	public Value(String x) {
		this(x, ValueType.STRING);
	}
	
	//inits the value as a boolean
	public Value(boolean x) {
		this(x, ValueType.BOOL);
	}
	
	//returns the data as a number, as a double
	public double asNumber() {
		return (double)data;
	}
	
	//is the type of the value a number?
	public boolean isNumber() {
		return type == ValueType.NUMBER;
	}
	
	//returns the data as a string
	public String asString() {
		return (String)data;
	}
	
	//is the type of the value a string?
	public boolean isString() {
		return type == ValueType.STRING;
	}
	
	//returns the data as a boolean
	public boolean asBool() {
		return (boolean)data;
	}
	
	//is the type of the value a boolean?
	public boolean isBool() {
		return type == ValueType.BOOL;
	}
	
	//is the data type of the value nil?
	public boolean isNil() {
		return type == ValueType.NIL;
	}
	
	//returns a value with the data null and the type nil
	public static Value nil() {
		return new Value(null, ValueType.NIL);
	}

	@Override
	public String toString() {
		return "Value [data=" + data + ", type=" + type + "]";
	}
	
	//returns the data as a string
	public String dataToString() {
		if(isNil()) {
			return "nil";
		}
		return data.toString();
	}
}
