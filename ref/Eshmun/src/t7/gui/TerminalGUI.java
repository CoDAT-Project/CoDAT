package t7.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import t7.Terminal;

public class TerminalGUI extends JFrame {
	/**
	 * Auto generated UID.
	 */
	private static final long serialVersionUID = 2014566491510117002L;

	/**
	 * The output text area.
	 */
	private JTextArea outputArea;
	
	/**
	 * The input text area.
	 */
	private JTextArea inputArea;
	
	/**
	 * The output scroll pane.
	 */
	private JScrollPane outputScroll;
	
	/**
	 * The input scroll pane.
	 */
	private JScrollPane inputScroll;
	
	/**
	 * The output stream that prints into the output area.
	 */
	private T7ResultStream resultStream;
	
	/**
	 * The input stream that reads from the input stream.
	 */
	private T7InputStream inputStream;
	
	/**
	 * Creates a new GUI for the terminal.
	 */
	public TerminalGUI() {
		super();
		
		//Frame Properties
		setTitle("T7 Terminal");
		setSize(800, 1000);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		//Text Areas
		outputArea = new JTextArea();
		inputArea = new JTextArea();
		
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setEditable(false);
		
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setEditable(true);
		
		outputArea.setBackground(Color.BLACK);
		inputArea.setBackground(Color.BLACK);
		
		outputArea.setForeground(Color.WHITE);
		inputArea.setForeground(Color.WHITE);
		
		outputArea.setSelectedTextColor(Color.BLACK);
		outputArea.setSelectionColor(Color.WHITE);
				
		inputArea.setCaretColor(Color.WHITE);
				
		outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		inputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		//Scrolls
		outputScroll = new JScrollPane(outputArea);
		inputScroll = new JScrollPane(inputArea);
		
	    outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	   
	    inputScroll.setPreferredSize(new Dimension(800, 100));
	    	    
	    //Splitter
	    JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);		
		splitPane.setTopComponent(outputScroll);
		splitPane.setRightComponent(inputScroll);
		splitPane.setResizeWeight(1);
	    
		//Add to frame
	    getContentPane().add(splitPane, BorderLayout.CENTER);
	    
	    addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					inputStream.end();
					resultStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
		if(b) {
			resultStream = new T7ResultStream(outputArea, outputScroll);
			inputStream = new T7InputStream(inputArea);
			Terminal terminal = new Terminal(resultStream, inputStream);
			terminal.start();
			
			inputArea.requestFocusInWindow();
			inputArea.grabFocus();
		}
	}
}
