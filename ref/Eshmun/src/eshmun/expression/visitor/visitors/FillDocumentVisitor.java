package eshmun.expression.visitor.visitors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.AWModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.binary.EWModality;
import eshmun.expression.modalities.unary.AFModality;
import eshmun.expression.modalities.unary.AGModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EFModality;
import eshmun.expression.modalities.unary.EGModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.Visitor;

/**
 * Visitor for syntax colorizing and indentation of an 
 * AbstractExpression into a Document (java swing).
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.AbstractExpression
 */
public class FillDocumentVisitor implements Visitor {
	/**
	 * The document in which the result will appear.
	 */
	private StyledDocument doc;
	
	/**
	 * Stores whether CTL Modalities are allowed.
	 */
	private boolean allowCTL;
	
	/**
	 * Stack of Text with its attributes, useful to add elements to document with their parents.
	 */
	private Stack<TextAttribute> stack;
	
	/**
	 * Style Attributes for variables
	 */
	private SimpleAttributeSet variable;
	
	/**
	 * Style Attributes for boolean literals
	 */
	private SimpleAttributeSet literal;
	
	/**
	 * Style Attributes for CTL Modalities
	 */
	private SimpleAttributeSet modality;
	
	/**
	 * Style Attributes for operators
	 */
	private SimpleAttributeSet operator;
	
	/**
	 * Style Attributes parenthesis
	 */
	private SimpleAttributeSet parenthesis;
	
	/**
	 * initialize the styles for each syntax component.
	 */
	private void initStyles() {
		variable = new SimpleAttributeSet();
		StyleConstants.setForeground(variable, Color.BLACK);
		
		literal = new SimpleAttributeSet();
		StyleConstants.setForeground(literal, Color.BLUE);
		StyleConstants.setItalic(literal, true);
		
		modality = new SimpleAttributeSet();
		if(allowCTL)
			StyleConstants.setForeground(modality, Color.BLUE);
		else {
			StyleConstants.setForeground(modality, Color.RED);
			StyleConstants.setUnderline(modality, true);
		}
		StyleConstants.setBold(modality, true);
		
		operator = new SimpleAttributeSet();
		StyleConstants.setForeground(operator, Color.BLUE);
		
		parenthesis = new SimpleAttributeSet();
		StyleConstants.setBold(parenthesis, true);
	}
	
	/**
	 * Creates a new FillDocumentVisitor.
	 * @param doc the document to insert into.
	 * @param allowCTL whether CTL is allowed or not.
	 */
	public FillDocumentVisitor(StyledDocument doc, boolean allowCTL) {
		this.doc = doc;
		this.allowCTL = allowCTL;
		
		stack = new Stack<TextAttribute>();
		initStyles();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanVariable v) {
		String text = v.getName();
		if(text.contains(":=")) text = text.replace(":=", "=");
		
		TextAttribute pair = new TextAttribute(text, variable);
		stack.push(pair);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanLiteral v) {
		String text = v.getValue() + "";
		text = text.toLowerCase();
				
		TextAttribute pair = new TextAttribute(text, literal);
		stack.push(pair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(NotOperator v) {
		stack.push(new TextAttribute("!", operator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AndOperator v) {
		stack.push(new TextAttribute("&", operator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(OrOperator v) {
		stack.push(new TextAttribute("|", operator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(ImplicationOperator v) {
		stack.push(new TextAttribute("=>", operator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EquivalenceOperator v) {
		stack.push(new TextAttribute("<=>", operator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AFModality v) {
		stack.push(new TextAttribute("AF", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AGModality v) {
		stack.push(new TextAttribute("AG", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AXModality v) {
		String processIndex = v.isIndexed() ? "_{"+v.getIndexAsString()+"}" : "";
		stack.push(new TextAttribute("AX"+processIndex, modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EFModality v) {
		stack.push(new TextAttribute("EF", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EGModality v) {
		stack.push(new TextAttribute("EG", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EXModality v) {
		String processIndex = v.isIndexed() ? "_{"+v.getIndexAsString()+"}" : "";
		stack.push(new TextAttribute("EX"+processIndex, modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AUModality v) {
		stack.push(new TextAttribute("AU", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AWModality v) {
		stack.push(new TextAttribute("AW", modality));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AVModality v) {
		String modalityName = "A" + v.getSymbol(); // Either AV or AR
		stack.push(new TextAttribute(modalityName, modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EUModality v) {
		stack.push(new TextAttribute("EU", modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EWModality v) {
		stack.push(new TextAttribute("EW", modality));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EVModality v) {
		String modalityName = "E" + v.getSymbol(); // Either EV or ER
		stack.push(new TextAttribute(modalityName, modality));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void childrenSeparator() {
		stack.push(new TextAttribute());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void separator() {
		if(stack.peek().isAtomic())
			return;
		
		Stack<TextAttribute> children = new Stack<TextAttribute>();
		
		TextAttribute current = stack.pop();
		while(!(current.isOperator() || current.isModality())) {
			if(!current.isSeparator()) {
				children.push(current);
			}
			
			current = stack.pop();
		}
		
		TextAttribute result = null;
		if(current.isOperator()) {
			result = composeOperator(current, children);
		}
		
		if(current.isModality()) {
			result = composeModality(current, children);
		}
		
		boolean notNull = (result != null);
		assert notNull; //sanity check
				
		stack.push(result);		
	}
	
	/**
	 * Compose a TextAttribute that matches the given operator with its children.
	 * @param operator the operator connecting children.
	 * @param children the terms of the operator.
	 * 
	 * @return a TextAttribute object that matches the given input, with the correct formatting.
	 */
	private TextAttribute composeOperator(TextAttribute operator, Stack<TextAttribute> children) {
		TextAttribute result = new TextAttribute();
		if(operator.text.get(0).equals("!")) { //! operator
			boolean oneChild = (children.size() == 1);
			assert oneChild; //sanity check
			
			TextAttribute child = children.pop();
			if(child.isAtomic() || child.isModality()) {
				result.add(operator);
				result.add(child);
			} else {
				result.add(operator);
				result.add(new TextAttribute("( ", this.parenthesis));
				result.add(child);
				result.add(new TextAttribute(" )", this.parenthesis));
			}
		} else {
			operator.text.set(0, " "+operator.text.get(0).trim()+" ");
			while(!children.isEmpty()) {
				TextAttribute currentChild = children.pop();
				if(currentChild.isAtomic() || currentChild.isModality()) {
					result.add(currentChild);
				} else {
					result.add(new TextAttribute("( ", this.parenthesis));
					result.add(currentChild);
					result.add(new TextAttribute(" )", this.parenthesis));
				}
				
				if(children.size() > 0) {
					result.add(operator); //add operator
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Compose a TextAttribute that matches the given CTL Modality with its children.
	 * @param modality the CTL modality.
	 * @param children children of modality (binary or unary).
	 * 
	 * @return a TextAttribute object that matches the given input, with the correct formatting.
	 */
	private TextAttribute composeModality(TextAttribute modality, Stack<TextAttribute> children) {
		TextAttribute result = new TextAttribute();
		
		if(children.size() == 1) { //Unary
			TextAttribute child = children.pop();
			result.add(modality);
			result.add(new TextAttribute("( ", this.parenthesis));
			result.add(child);
			result.add(new TextAttribute(" )", this.parenthesis));
		} else if(children.size() == 2){ //Binary
			String firstLetter = modality.text.get(0).substring(0, 1);
			String secondLetter = modality.text.get(0).substring(1);
						
			result.add(new TextAttribute(firstLetter, this.modality));
			result.add(new TextAttribute("( ", this.parenthesis));
			
			if(children.get(1).isAtomic() || children.get(1).isModality()) { //Children reversed due to stack.
				result.add(children.get(1));
			} else {
				result.add(new TextAttribute("( ", this.parenthesis));
				result.add(children.get(1));
				result.add(new TextAttribute(" )", this.parenthesis));
			}
			
			result.add(new TextAttribute(" "+secondLetter+" ", this.modality));
			
			if(children.get(0).isAtomic() || children.get(0).isModality()) {
				result.add(children.get(0));
			} else {
				result.add(new TextAttribute("( ", this.parenthesis));
				result.add(children.get(0));
				result.add(new TextAttribute(" )", this.parenthesis));
			}
			
			result.add(new TextAttribute(" )", this.parenthesis));
			
		} else {
			assert false; //sanity check
		}
		
		return result;
	}
	
	/**
	 * Writes the result to the given document in a formatted way (indentation, syntax coloring, parenthesis ...)
	 */
	public void applyToDoc() {
		try {
			doc.remove(0, doc.getLength());
			
			TextAttribute textAtt = stack.pop();
			for(int i = 0; i < textAtt.text.size(); i++) {
				String text = textAtt.text.get(i);
				SimpleAttributeSet attr = textAtt.attributes.get(i);
				
				doc.insertString(doc.getLength(), text, attr);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Gets the text as a String
	 * @return the text as a string.
	 */
	public String getString() {
		String str = "";
		TextAttribute textAtt = stack.pop();
		for(int i = 0; i < textAtt.text.size(); i++) {
			str += textAtt.text.get(i);
		}
		
		return str;
	}
	
	/**
	 * Pairs a text with its style attributes.
	 * 
	 * <p>The text and its respective style are matched by index.</p>
	 * 
	 * <p>One instance of this class could contain multiple pairs.</p>
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	private class TextAttribute {
		/**
		 * the text in this pair.
		 */
		private ArrayList<String> text;
		
		/**
		 * the style attributes in this pair.
		 */
		private ArrayList<SimpleAttributeSet> attributes;

		/**
		 * indicates whether this is just a dummy separator
		 */
		private boolean separator;
		
		/**
		 * Create a new pair of text and style attributes.
		 * 
		 * @param text the text.
		 * @param attributes the style attributes.
		 */
		public TextAttribute(String text, SimpleAttributeSet attributes) {
			this.text = new ArrayList<String>();
			this.attributes = new ArrayList<SimpleAttributeSet>();
			
			this.text.add(text);
			this.attributes.add(attributes);
			
			this.separator = false;
		}
		
		/**
		 * Create a dummy separator.
		 */
		public TextAttribute() {
			this.text = new ArrayList<String>();
			this.attributes = new ArrayList<SimpleAttributeSet>();
			
			this.separator = true;
		}
		
		/**
		 * checks whether this is a dummy separator.
		 * @return true if this is a dummy separator, false otherwise.
		 */
		public boolean isSeparator() {
			return separator;
		}
		
		/**
		 * String representation.
		 * @return the to String representation (the pair's text).
		 */
		@Override
		public String toString() {
			if(separator)
				return "child sep";
						
			String result = "";
			
			for(String s : text) {
				result += s;
			}
			
			return result;
		}
		
		/**
		 * checks if this represents an atomic expression.
		 * 
		 * <p>An atomic expression is either a variable, literal, or a negation of a variable or a literal.
		 * @return true if this represents an atomic expression, false otherwise.
		 * 
		 * @see eshmun.expression.atomic.BooleanVariable
		 * @see eshmun.expression.atomic.BooleanLiteral
		 * @see eshmun.expression.operators.NotOperator
		 */
		public boolean isAtomic() {
			if(text.size() == 2) 
				if(text.get(0).equals("!")) 
					return attributes.get(1) == literal ||
							attributes.get(1) == variable; 
				
			
			
			if(text.size() != 1) return false;
			
			return attributes.get(0) == literal ||
					attributes.get(0) == variable;  
		}
		
		/**
		 * checks if this represents a boolean operator expression
		 * @return true if this represents a boolean operator expression, false otherwise.
		 */
		public boolean isOperator() {
			if(text.size() != 1) return false;
			
			return attributes.get(0) == operator;
		}
		
		/**
		 * checks if this represents a CTL modality expression
		 * @return true if this represents a CTL modality expression, false otherwise.
		 */
		public boolean isModality() {
			if(text.size() != 1) return false;
			
			return attributes.get(0) == modality;
		}
		
		/**
		 * concatenate a TextAttribute to the end of this text attribute.
		 * @param attr the TextAttribute to concatenate to this.
		 */
		public void add(TextAttribute attr) {
			separator = false;
			
			text.addAll(attr.text);
			attributes.addAll(attr.attributes);
		}
	}
}
