package t7;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import t7.grammar.T7Lexer;
import t7.grammar.T7Parser;

/**
 * Parses the provided statements and calls the appropriate methods to execute them.
 * @author kinan
 */
public class StatementInterpreter {
	/**
	 * The interpreter for the statements.
	 */
	private StatementHandler handler;
	
	/**
	 * The parser.
	 */
	private StatementParser parser;
	
	/**
	 * Creates a new StatementParser that utilizes the given handler.s
	 * @param resultStream the stream to print results through.
	 */
	public StatementInterpreter(PrintStream resultStream) {
		handler = new StatementHandler(this, resultStream);
		parser = new StatementParser();
	}
	
	/**
	 * Reads the next statement from the given scanner, the statement is not always one line, 
	 * also, the statement best not start with empty lines, hence this method is needed.
	 * @param in the scanner to read input from.
	 * @return the next statement.
	 * @throws NoSuchElementException if the given scanner does not contain more lines to read.
	 * @throws IllegalStateException if the given scanner is closed.
	 */
	public String read(Scanner in) throws NoSuchElementException, IllegalArgumentException {
		//Start reading instructions
		String line = in.nextLine() + System.lineSeparator();
						
		if(nonEmptyStartsWith(line, "define ") || nonEmptyStartsWith(line, "define\t") || nonEmptyStartsWith(line, "@define")) {
			while(!nonEmptyEndsWith(line.trim(), "}") && in.hasNext()) { //define ends with '}'.
				line += in.nextLine() + System.lineSeparator();
			}
		} else if(nonEmptyStartsWith(line, "if ") || nonEmptyStartsWith(line, "if\t") ||
				nonEmptyStartsWith(line, "for ") || nonEmptyStartsWith(line, "for\t") ||
				nonEmptyStartsWith(line, "while ") || nonEmptyStartsWith(line, "while\t")) {
			int scopeCounter = 1; 
			while(scopeCounter != 0 && in.hasNext()) { //branches end with 'end'.
				String tmp = in.nextLine() + System.lineSeparator();
				if(nonEmptyStartsWith(tmp, "if ") || nonEmptyStartsWith(tmp, "if\t") ||
						nonEmptyStartsWith(tmp, "for ") || nonEmptyStartsWith(tmp, "for\t") ||
						nonEmptyStartsWith(tmp, "while ") || nonEmptyStartsWith(tmp, "while\t")) {
					scopeCounter++;					
				} else if(nonEmptyEndsWith(tmp.trim(), "end")) {
					scopeCounter--;
				}
				
				line += tmp;
			}
		}
		
		//Get rid of leading white space
		while(line.startsWith(System.lineSeparator()) || line.startsWith(" ") || line.startsWith("\t") ||
				line.startsWith("\n") || line.startsWith("\r")) {
			line = line.substring(1);
		}
		
		return line;
	}
	
	/**
	 * Reads the next statement from the given input stream, the statement is not always one line, 
	 * also, the statement best not start with empty lines, hence this method is needed.
	 * @param in the input stream to read input from.
	 * @throws IOException if an exception happened while reading.
	 * @return the next statement.
	 */
	public String read(InputStream in) throws IOException {
		//Start reading instructions
		String line = readLine(in);
						
		if(nonEmptyStartsWith(line, "define ") || nonEmptyStartsWith(line, "define\t") || nonEmptyStartsWith(line, "@define")) {
			while(!nonEmptyEndsWith(line.trim(), "}") && in.available() > 0) { //define ends with '}'.
				line += readLine(in);
			}
		} else if(nonEmptyStartsWith(line, "if ") || nonEmptyStartsWith(line, "if\t") ||
				nonEmptyStartsWith(line, "for ") || nonEmptyStartsWith(line, "for\t") ||
				nonEmptyStartsWith(line, "while ") || nonEmptyStartsWith(line, "while\t")) {
			int scopeCounter = 1; 
			while(scopeCounter != 0 && in.available() > 0) { //branches end with 'end'.
				String tmp = readLine(in) + System.lineSeparator();
				if(nonEmptyStartsWith(tmp, "if ") || nonEmptyStartsWith(tmp, "if\t") ||
						nonEmptyStartsWith(tmp, "for ") || nonEmptyStartsWith(tmp, "for\t") ||
						nonEmptyStartsWith(tmp, "while ") || nonEmptyStartsWith(tmp, "while\t")) {
					scopeCounter++;					
				} else if(nonEmptyEndsWith(tmp.trim(), "end")) {
					scopeCounter--;
				}
				
				line += tmp;
			}
		}
		
		//Get rid of leading white space
		while(line.startsWith(System.lineSeparator()) || line.startsWith(" ") || line.startsWith("\t") ||
				line.startsWith("\n") || line.startsWith("\r")) {
			line = line.substring(1);
		}
		
		return line;
	}
	
	/**
	 * Reads one line from the given input stream (or until EOF, if new line was not reached).
	 * @param in the input stream.
	 * @throws IOException if an exception happened while reading.
	 * @return one line (or until EOF).
	 */
	private String readLine(InputStream in) throws IOException {
		StringBuilder builder = new StringBuilder(128);
		
		while(in.available() > 0) {
			int r = in.read();
			if(r < 0) break;
			
			char c = (char) r;			
			builder.append(c);
			
			if(c == '\n' || c == '\r' || System.lineSeparator().equals(c+"")) break;
		}
		
		return builder.toString();
	}
	
	/**
	 * Executes the given statements, statement-by-statement.
	 * @param statements the statements (body of the program).
	 * @throws Exception variety of exception types depending on the associated syntax error.
	 */
	public void execute(String statements) throws Exception {
		Scanner scan = new Scanner(statements);
		while(scan.hasNext()) {
			String line = read(scan);
			if(line.trim().equals("cl")) continue;
			
			parse(line);
		}
		
		scan.close();
	}
	
	/**
	 * Executes the given program, statement-by-statement.
	 * @param file the file that contains the program to be executed.
	 * @throws Exception variety of exception types depending on the associated syntax error,
	 * 				Note that FileNotFoundException could be thrown in two cases,
	 * 				either the given file does not exist, or the given file contain a file-dependent 
	 * 				statement (like load) that refers to a file that does not exist.
	 */
	public void execute(File file) throws Exception {
		boolean started = false;
		
		Scanner scan = new Scanner(file);
		while(scan.hasNext()) {
			String line = read(scan);
			if(line.trim().equals("cl") || line.trim().isEmpty()) continue;

			started = line.trim().equals("start");
			parse(line);
		}
		
		if(!started) parse("start");
		
		scan.close();
	}
	
	/**
	 * Parses the given line.
	 * @param line the line to parse.
	 * @throws IOException if the statement is IO related and the file is inaccessible.
	 */
	public void parse(String line) throws IOException {
		parser.clear(); //Clears the parser's buffers
		
		//Create parser.
		T7Lexer lexer = new T7Lexer(new ANTLRInputStream(line));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		T7Parser p = new T7Parser(tokens);
				
		//Walk the parse tree.
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(parser, p.statement());
		
		//Execute the statement.
		handler.handleStatement(parser.getStatement());		
	}
	
	/**
	 * Checks if the first non empty characters match the given string.
	 * @param string the string to look in.
	 * @param match the prefix to match.
	 * @return true if the string starts with the match (disregarding leading empty space),
	 * 		false otherwise.
	 */
	public static boolean nonEmptyStartsWith(String string, String match) {
		if(match.contains("//")) {
			match = match.substring(0, match.indexOf("//"));
		}
		
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == ' ' || string.charAt(i) == '\t' || string.charAt(i) == '\n' || string.charAt(i) == '\r') {
				continue;
			}
			
			return string.substring(i).startsWith(match);
		}
		
		return false;
	}
	
	/**
	 * Checks if the last non empty characters match the given string.
	 * @param string the string to look in.
	 * @param match the suffix to match.
	 * @return true if the string ends with the match (disregarding leading empty space),
	 * 			false otherwise.
	 */
	public static boolean nonEmptyEndsWith(String string, String match) {
		if(match.contains("//")) {
			match = match.substring(0, match.indexOf("//"));
		}
		
		for(int i = string.length() - 1; i >= 0; i--) {
			if(string.charAt(i) == ' ' || string.charAt(i) == '\t' || string.charAt(i) == '\n' || string.charAt(i) == '\r') {
				continue;
			}
			
			return string.substring(0, i+1).endsWith(match);
		}
		
		return false;
	}
}
