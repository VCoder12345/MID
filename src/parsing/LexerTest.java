package parsing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//a program that tests the lexer by printing the tokens of a file
public class LexerTest {

	public static void main(String[] args) throws IOException {
		Lexer lexer = new Lexer(Files.readString(Path.of("files/test.txt")));
		Token currentToken;
		while((currentToken = lexer.nextToken()).type != TokenType.EOF) {
			System.out.println(currentToken);
		}
	}

}
