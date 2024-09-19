package eshmun.gui.kripke.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Timer;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.parser.LogicParser;
import eshmun.expression.visitor.visitors.FillDocumentVisitor;
import eshmun.gui.kripke.menus.FormulaPopupMenu;
/**
 * 
 * This class represents an IDE-like TextPane for logic expression.
 * 
 * <p>This IDE-like environment gives the following functionalities:
 * <ul>
 * 	<li>Syntax Coloring</li>
 * 	<li>Syntax Error Underlining</li>
 * 	<li>Auto-Indentation</li>
 * 	<li>Auto-complete and Variable name helpers</li>
 * </ul>
 * </p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */

public class LogicTextPane extends JTextPane {
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = -7375698973098319737L;
	
	/**
	 * Default font for LogicTextPane
	 */
	private static final Font defaultFont = new Font(Font.SERIF, Font.PLAIN, 14);

	/**
	 * Document Listener for this text pane, listens to user's input.
	 */
	private LogicDocumentListener documentListener;
	
	/**
	 * Swing Timer, schedules a thread to parse content each INTERVAL_DURATION millisecond.
	 */
	private Timer swingTimer;
	
	/**
	 * Stores whether this has been changed since the last pass of the ParseTask
	 */
	private boolean changed;
	
	/**
	 * Stores whether CTL modalities are allowed inside this textPane.
	 */
	private boolean allowCTL;
	
	/**
	 * Store the label (placeholder) of this textPane.
	 */
	private String label;
	
	
	/**
	 * The current instance of the application.
	 */
	private Eshmun eshmun;
	
	/**
	 * Create a new LogicTextPane (IDE-Like text pane for Logic Expressions).
	 * @param textLabel the placeholder label.
	 * @param eshmunInstance the main Eshmun frame.
	 * @param allowCTL this pane accepts CTL as well as propositional logic statements.
	 */
	public LogicTextPane(String textLabel, Eshmun eshmunInstance, boolean allowCTL) {
		super();
		
		//set text to the given label
		setText(textLabel);
		
		
		this.label = textLabel;
		this.eshmun = eshmunInstance;
		this.allowCTL = allowCTL;
		
		changed = false;
		setFont(defaultFont);
		
		//DocumentListener for formatting, parsing, and caret movement.
		documentListener = new LogicDocumentListener(this);
		getDocument().addDocumentListener(documentListener);
		addCaretListener(documentListener);
		
		//Focus Listener, displays label and deletes it when needed.
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if(getText().trim().equals("")) {
					SimpleAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, Color.BLACK);
					StyleConstants.setBold(attr, false);
					StyleConstants.setUnderline(attr, false);
					
					try {
						documentListener.changeWithoutFiring(label, attr);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				if(getText().equals(label)) {
					SimpleAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, Color.BLACK);
					StyleConstants.setBold(attr, false);
					StyleConstants.setUnderline(attr, false);
					
					try {
						documentListener.changeWithoutFiring("", attr);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		//Add Pop-up Menu
		setComponentPopupMenu(new FormulaPopupMenu(this, eshmun, this.label));
		
		swingTimer = new Timer(ParseTask.RUN_INTERVAL, new ParseTask(this));
		swingTimer.start(); //start ParseTask
	}

	/**
	 * Listener for document changes of a LogicTextPane (insertion, deletion, style-change), 
	 * it also listens for caret movement.
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	public static class LogicDocumentListener implements DocumentListener, CaretListener {
		/**
		 * The textPane this listener belongs to
		 */
		private LogicTextPane textPane;
		
		/**
		 * Last time the textPane got changed, in milliseconds.
		 */
		private long lastChanged;
		
		/**
		 * Flags whether the caret has been changed recently.
		 */
		private boolean caretChanged;
		
		/**
		 * Flags whether to ignore the next change or not.
		 */
		private boolean ignore;
		
		/**
		 * Create a new LogicDocumentListener for the given textPane.
		 * @param textPane the textPane this listener belong to.
		 */
		public LogicDocumentListener(LogicTextPane textPane) {
			this.textPane = textPane;
			
			ignore = false;
		}
		
		/**
		 * This method gets triggered when the user delete part of the text.
		 * @param arg0 DocumentEvent related to the event.
		 */
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			if(arg0.getLength() == 0)
				return;
			
			if(ignore)
				return;
			
			textPane.changed = true;
			lastChanged = System.currentTimeMillis();
		}
		
		/**
		 * This method gets triggered when the user inserts some text into the textPane.
		 * @param arg0 DocumentEvent related to the event.
		 */
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			if(arg0.getLength() == 0)
				return;
			
			if(ignore)
				return;
			
			textPane.changed = true;
			lastChanged = System.currentTimeMillis();
		}
		
		/**
		 * This method gets triggered when the user change the style or caret.
		 * @param arg0 DocumentEvent related to the event.
		 */
		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		/**
		 * Change the value of the textPane without executing the handler's code.
		 * @param newText the new value of the textPane.
		 * @param attr the display attribute (text formatting).
		 * @throws BadLocationException if the location to put the string in is out of range.
		 */
		public void changeWithoutFiring(String newText, SimpleAttributeSet attr) throws BadLocationException {
			textPane.changed = false;
			
			ignore = true;
			textPane.setText("");
			textPane.getDocument().insertString(0, newText, attr);
			
			ignore = false;
		}
		
		/**
		 * This method gets triggered when the user caret moves.
		 * @param e CaretEvent related to the event.
		 */
		@Override
		public void caretUpdate(CaretEvent e) {
			caretChanged = true;
		}
	}
	
	/**
	 * This class represents a ParseTask, that is a task that runs in the background 
	 * with the objective of parsing and formatting the text in the given textPane, the 
	 * task also takes care of underlining matching parenthesis when needed (depending on
	 * the caret's position).
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	private static class ParseTask implements ActionListener {
		/**
		 * The amount of time after the user's last change that must be guaranteed 
		 * to pass before the parsing takes place, in milliseconds.
		 */
		private static final int WAIT_DURATION = 1000;
		
		/**
		 * The interval between each execution of this task and the next one in 
		 * milliseconds, should be at least equal to WAIT_DURATION or greater.
		 */
		public static final int RUN_INTERVAL = 2000;
		
		/**
		 * The textPane this task belongs to.
		 */
		private LogicTextPane textPane;
		
		/**
		 * Create a new ParseTask for the given textPane.
		 * @param textPane the textPane to which this ParseTask belongs to.
		 */
		public ParseTask(LogicTextPane textPane) {
			this.textPane = textPane;
		}

		/**
		 * The method that gets executed in the background each TIME_INTERVAL.
		 * @param e ActionEvent related to the current execution event.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!textPane.changed) {//Nothing changed
				caret(); //underline the parenthesis near caret
				return;
			}
			
			long timeNow = System.currentTimeMillis();
			long lastChanged = textPane.documentListener.lastChanged;
						
			if(timeNow - lastChanged > WAIT_DURATION) { //Guarantee that WAIT_DURATION milliseconds have passed				
				if(textPane.getText().trim().equals("") || textPane.getText().equals(textPane.label)) { //Empty formula
					textPane.changed = false;
					return;
				}
				
				String text = textPane.getText().trim();
				LogicParser parser = new LogicParser();
				
				AbstractExpression exp = parser.parse(text); //Parse text
				if(exp != null) { //no syntax errors
					FillDocumentVisitor fid = new FillDocumentVisitor(((StyledDocument) textPane.getDocument()), textPane.allowCTL);
					exp.accept(fid); //Construct formatting rule for the text.
					
					textPane.documentListener.ignore = true; 
					fid.applyToDoc(); //Apply the formatting rule to the document.
					textPane.documentListener.ignore = false;
				} else { //syntax error, underline them.
					Integer[] errors = parser.getSyntaxErrorCharacters(); //Error Number
					StyledDocument doc = (StyledDocument) textPane.getDocument();
					
					textPane.documentListener.ignore = true;
					for(int i = 0; i < doc.getLength(); i++) { //Remove previous Errors
						AttributeSet attr = doc.getCharacterElement(i).getAttributes();
						
						SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
						if(StyleConstants.getForeground(newAttr).equals(Color.RED)) {
							StyleConstants.setForeground(newAttr, Color.BLACK);
							StyleConstants.setUnderline(newAttr, false);
							doc.setCharacterAttributes(i, 1, newAttr, true);
						}
					}
					
					for(Integer charNum : errors) { //Highlight new Errors with red foreground and underline
						AttributeSet attr = doc.getCharacterElement(charNum).getAttributes();
						
						SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
						StyleConstants.setForeground(newAttr, Color.RED);
						StyleConstants.setUnderline(newAttr, true);
						doc.setCharacterAttributes(charNum-1, Math.min(doc.getLength()-charNum+1, 3), newAttr, true);
					}
					textPane.documentListener.ignore = false;
				}
				
				textPane.changed = false;
				caret(); //underline the parenthesis near caret
			}
		}
		
		/**
		 * This method looks near the caret, if there exist a parenthesis it gets underlined with its 
		 * matching opposite parenthesis, the method clears old parenthesis underlines. 
		 */
		public void caret() {
			if(!textPane.documentListener.caretChanged)
				return;
			
			textPane.documentListener.caretChanged = false;
			
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			String text = textPane.getText();
			int caretLoc = textPane.getCaretPosition();
			
			for(int i = 0; i < text.length(); i++) { //Remove previous parenthesis underlines
				if(text.charAt(i) == '(' || text.charAt(i) == ')') {
					AttributeSet attr = doc.getCharacterElement(i).getAttributes();
					
					SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
					StyleConstants.setUnderline(newAttr, false);
					doc.setCharacterAttributes(i, 1, newAttr, true);
				}
			}
						
			//Look in the current location (right behind cursor, right after cursor) for parenthesis,
			//if found, find its match, underline both.
			int[] locations = new int[] { caretLoc-1, caretLoc };
			for(int location : locations) {
				if(location >= text.length() || location < 0)
					continue;
				
				int matchLoc = -1;
				char c = text.charAt(location);
				
				if(c == '(') { //look for match ahead
					int m = location;
					int count = 1;
					while(count > 0 && m < text.length() - 1) {
						m++;
						
						if(text.charAt(m) == '(')
							count++;
						else if(text.charAt(m) == ')')
							count--;
					}
					
					matchLoc = m;
				}
				
				else if(c == ')') { //look for match behind
					int m = location;
					int count = 1;
					while(count > 0 && m > 0) {
						m--;
						
						if(text.charAt(m) == '(')
							count--;
						else if(text.charAt(m) == ')')
							count++;
					}
					
					matchLoc = m;
				} else {
					continue;
				}
				
				//Apply underline to the location and its match
				AttributeSet attr = doc.getCharacterElement(location).getAttributes();
				
				SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
				StyleConstants.setUnderline(newAttr, true);
				doc.setCharacterAttributes(location, 1, newAttr, true);
				
				attr = doc.getCharacterElement(matchLoc).getAttributes();
				
				newAttr = new SimpleAttributeSet(attr);
				StyleConstants.setUnderline(newAttr, true);
				doc.setCharacterAttributes(matchLoc, 1, newAttr, true);
				
				break; //stop
			}
		}
	}
}