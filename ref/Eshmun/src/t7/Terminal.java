package t7;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This class provides a shell for the language.
 * @author kinan
 *
 */
public class Terminal extends Thread {
	
	/**
	 * The stream to read commands from.
	 */
	private InputStream inputStream;
	
	/**
	 * Print stream to print result.
	 */
	private PrintStream resultStream;
	
	/**
	 * The token string used to clear the screen.
	 */
	public static final String CLEAR_TOKEN = "\033[2J\033[1;1H";

	/**
	 * The welcome message.
	 */
	public static final String WELCOME_MESSAGE = "T7 Interpreter 0.0.1 (default, October 23 2015)" + System.lineSeparator() + "Type \"version\" or \"help\" for more information.";
	
	/**
	 * Flags if the running os is windows.
	 */
	@SuppressWarnings("unused")
	private boolean windows;
	
	/**
	 * Flags if the running os is mac
	 */
	@SuppressWarnings("unused")
	private boolean mac;
	
	/**
	 * Flags if the running os is linux.
	 */
	@SuppressWarnings("unused")
	private boolean linux;
	
	/**
	 * Creates a new Terminal that prints its result through standard 
	 * System.out print stream and reads commands from the default System.in
	 */
	public Terminal() {
		this(System.out, System.in);
	}
	
	/**
	 * Creates a new Terminal that prints the result to the given stream.
	 * @param resultStream the stream to print results through.
	 * @param inputStream the stream to read commands from.
	 */
	public Terminal(OutputStream resultStream, InputStream inputStream) {
		this(new PrintStream(resultStream), inputStream);
	}

	/**
	 * Creates a new Terminal that prints the result to the given print stream.
	 * @param printStream the stream to print results through.
	 * @param inputStream the stream to read commands from.
	 */
	public Terminal(PrintStream printStream, InputStream inputStream) {
		this.resultStream = printStream;
		this.inputStream = inputStream;
		windows = false; mac = false; linux = false;
		
		//Figure out which OS is running
		String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("windows")) {
            windows = true;
        }
        else if(os.contains("linux")) {
            linux = true;
        } else {
        	mac = true;
        }
	}
		
	/**
	 * Gives a terminal for the data definition language language.
	 */
	@Override
	public void run() {
		Scanner scan = null;
		if(inputStream == System.in) scan = new Scanner(inputStream);
		
		//Input stream to read instructions.		
		//interpreter to interpret statements.
		StatementInterpreter interpreter = new StatementInterpreter(resultStream);
		clearScreen();
			
		try {
			resultStream.print(">>> ");
			while((scan != null && scan.hasNext()) || inputStream.available() > 0) {
				String line;
				if(scan != null) line = interpreter.read(scan);
				else line = interpreter.read(inputStream);
				
				if(line.trim().equals("cl")) {
					clearScreen();
					resultStream.print(">>> ");
					continue;
				}
							
				try {
					if(inputStream != System.in) resultStream.println(line.trim());
					interpreter.parse(line);
				} catch(Exception ex) {
					resultStream.println("ERROR: " + ex.getMessage());
					ex.printStackTrace(resultStream);
				}
				
				//Next Statement
				resultStream.print(">>> ");
			}
		
			if(scan != null) scan.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Clears the screen
	 */
	public void clearScreen() {
		resultStream.print(CLEAR_TOKEN);
		resultStream.println(WELCOME_MESSAGE);
	}
}
