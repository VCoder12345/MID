package vm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ast.AST;
import compiler.Compiler;
import parsing.Lexer;
import parsing.Parser;

public class VMTest {
	//runs the virtual machine on the primes.txt and prints out the time it takes for compilation and for execution
	public static void main(String[] args) throws IOException {
		String inputText = Files.readString(Path.of("examples/primes.txt"));
		
		long compilationStart = System.currentTimeMillis();
		Lexer lexer = new Lexer(inputText);
		Parser parser = new Parser(lexer);
		AST tree = parser.parse();
		Compiler compiler = new Compiler();
		compiler.compile(tree);
		long compilationTime = System.currentTimeMillis() - compilationStart;
		double compilationSeconds = compilationTime / 1000.0;
		System.out.println("[*] compilation finished in " + compilationSeconds + " seconds");
		
		VM vm = new VM(compiler.code);
		//vm.traceExecution = true;
		
		System.out.println("[*] starting program...");
		System.out.println();
		long execStart = System.currentTimeMillis();
		vm.cpu();
		long execTime = System.currentTimeMillis() - execStart;
		double execTimeSeconds = execTime / 1000.0;
		
		System.out.println();
		System.out.println("[*] execution finished in " + execTimeSeconds + " seconds");
	}

}
