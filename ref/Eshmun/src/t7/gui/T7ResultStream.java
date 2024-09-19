package t7.gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import t7.Terminal;

public class T7ResultStream extends OutputStream {
	
	/**
	 * The output area that will receive the results.
	 */
	private JTextArea outputArea;
	
	/**
	 * The scroll pane.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * Saves the last few chars to compare against the special
	 * clear screen token.
	 */
	private String clearScreenBuffer;
	
	/**
	 * Creates a new Result stream that will print the result into the given output area.
	 * @param outputArea the output area that will receive the results.
	 * @param scrollPane the scroll pane containing output area.
	 */
	public T7ResultStream(JTextArea outputArea, JScrollPane scrollPane) {
		this.outputArea = outputArea;
		this.scrollPane = scrollPane;
		this.clearScreenBuffer = "";
	}
	
	/**
	 * Writes a char into the output area.
	 * @param b the char.
	 * @throws IOException never.
	 */
	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
	
		clearScreenBuffer += c;
		outputArea.append(c+"");

		if(clearScreenBuffer.length() > Terminal.CLEAR_TOKEN.length())
			clearScreenBuffer = clearScreenBuffer.substring(clearScreenBuffer.length() - Terminal.CLEAR_TOKEN.length());
		
		if(clearScreenBuffer.equals(Terminal.CLEAR_TOKEN))
			clear();
		
		if((c+"").equals(System.lineSeparator()) || c == '>') {
			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		}
	}
	
	/**
	 * Clears the output area.
	 */
	private void clear() {
		clearScreenBuffer = "";
		outputArea.setText("");
	}
}
