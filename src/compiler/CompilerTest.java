package compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ast.AST;
import parsing.Lexer;
import parsing.Parser;

//tests the compiler by compiling a file and printing the generated bytecode
public class CompilerTest {

	public static void main(String[] args) throws IOException {
		Lexer lexer = new Lexer(Files.readString(Path.of("examples/factorial.txt")));
		Parser parser = new Parser(lexer);
		AST tree = parser.parse();
		Compiler compiler = new Compiler();
		compiler.compile(tree);
		compiler.code.printCode();
	}

}
