package parsing;

//a token
public class Token {
	public TokenType type; //the type of the token
	public String value; //the text of the token
	public int line; //the line in which the token was found
	
	public Token(TokenType type, String value, int line) {
		super();
		this.type = type;
		this.value = value;
		this.line = line;
	}

	
	public Token(TokenType type, int line) {
		this(type, "", line);
	}
	
	@Override
	public String toString() {
		return "Token [type=" + type + ", value= \"" + value + "\"]";
	}

	
	
	
	
}
