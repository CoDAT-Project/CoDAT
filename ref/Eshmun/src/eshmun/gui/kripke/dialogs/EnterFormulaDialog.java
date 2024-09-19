package eshmun.gui.kripke.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.parser.LogicParser;
import eshmun.gui.kripke.utils.LogicTextPane;

/**
 * Dialog that allows the user to enter a formula (in an IDE like enviroment).
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class EnterFormulaDialog extends JDialog {

	/**
	 * Default version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logic Panel.
	 */
	private LogicTextPane logic;
	
	/**
	 * Eshmun Instance.
	 */
	private Eshmun eshmunInstance;
	
	/**
	 * If the dialog was canceled.
	 */
	private boolean canceled = true;
	
	/**
	 * Saves the formula.
	 */
	private AbstractExpression formula = null;
	
	/**
	 * Create the frame.
	 * @param eshmun the main eshmun frame.
	 */
	public EnterFormulaDialog(Eshmun eshmun) {
		super(eshmun, "Enter Formula", true);
				
		setSize(800, 600);
		setResizable(false);
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		this.eshmunInstance = eshmun;
				
		//LOGIC PANEL		
		logic = new LogicTextPane("Formula", eshmunInstance, true);
		
		JScrollPane scroll = new JScrollPane(logic, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(790, 500));
		add(scroll);
		
		// OK/CANCEL PANEL
		JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cPanel.setPreferredSize(new Dimension(790, 40));
		
		add(cPanel);
		
		JButton cancel = new JButton("Cancel");
		JButton ok = new JButton(" OK ");
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				setVisible(false);
				dispose();
			}
		});
		
		ok.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = logic.getText();
				if(txt.equals("Formula")) return;	
				
				formula = new LogicParser().parse(txt);
				if(formula == null) {
					JOptionPane.showMessageDialog(null, "The formula has syntax errors", "Error", JOptionPane.ERROR_MESSAGE);
					logic.requestFocus();
				} else {
					canceled = false;
					setVisible(false);
					dispose();
				}
			}
		});
		
		cPanel.add(cancel);
		cPanel.add(ok);
	}
	
	/**
	 * @return If the dialog was canceled.
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/**
	 * @return The formula the user entered (or null if it was canceled/pending).
	 */
	public AbstractExpression getFormula() {
		return formula;
	}
	
	/**
	 * Shows the dialog, blocks until dialog is done. 
	 * @return true if the dialog returned a value (finished with a confirm), false if it was canceled.
	 */
	public boolean showDialog() {
		setVisible(true);
		return !isCanceled();
	}
}
