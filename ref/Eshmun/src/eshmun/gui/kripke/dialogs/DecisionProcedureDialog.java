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
import eshmun.decisionprocedure.DecisionProcedure;
import eshmun.expression.AbstractExpression;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.parser.LogicParser;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.utils.LogicTextPane;

/**
 * Dialog for Decision Procedure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class DecisionProcedureDialog extends JDialog {

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
	 * Create the frame.
	 * @param eshmun the main eshmun frame.
	 */
	public DecisionProcedureDialog(Eshmun eshmun) {
		super(eshmun, "Decision Procedure", false);
				
		setSize(800, 600);
		setResizable(false);
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		this.eshmunInstance = eshmun;
		
		//TOP PANELS FOR CONTROLS
		JPanel lPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lPanel.setPreferredSize(new Dimension(730/3, 40));
		
		JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cPanel.setPreferredSize(new Dimension(730/3, 40));
		
		JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rPanel.setPreferredSize(new Dimension(730/3, 40));
		
		//SATISFIABILITY
		JButton test = new JButton("Test Satifiability");
		lPanel.add(test);
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = logic.getText();
				if(txt.equals("Formula"))
					return;
				
				AbstractExpression exp = new LogicParser().parse(txt);
				DecisionProcedure d = new DecisionProcedure(exp);
				if(d.checkStatisfiability()) {
					JOptionPane.showMessageDialog(null, "Satifiable!");
				} else {
					JOptionPane.showMessageDialog(null, "Unsatifiable!");
				}
			}
		});
		
		//DISPLAY TABLEAU
		JButton tab = new JButton("Display Tableau");
		cPanel.add(tab);
		tab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = logic.getText();
				if(txt.equals("Formula"))
					return;
				
				AbstractExpression exp = new LogicParser().parse(txt);
				DecisionProcedure d = new DecisionProcedure(exp);
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue? \n Your unsaved changes in Eshmun will be lost", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(confirm == JOptionPane.YES_OPTION) {
					eshmunInstance.loadDefinition(d.getTableau().getStringDefinition(), StructureType.Kripke);
					eshmunInstance.getCurrentGraphPanel().setTableau(true);
					dispose();
				}
			}
		});
		
		//VALIDITY
		JButton val = new JButton("Test Validity");
		rPanel.add(val);
		val.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = logic.getText();
				if(txt.equals("Formula"))
					return;
				
				AbstractExpression exp = new LogicParser().parse(txt);
				exp = new NotOperator(exp);
				
				DecisionProcedure d = new DecisionProcedure(exp);
				if(d.checkStatisfiability()) {
					JOptionPane.showMessageDialog(null, "Not Valid!");
				} else {
					JOptionPane.showMessageDialog(null, "Valid!");
				}
			}
		});
				
		add(lPanel);
		add(cPanel);
		add(rPanel);
		
		//LOGIC PANEL		
		logic = new LogicTextPane("Formula", eshmunInstance, true);
		
		JScrollPane scroll = new JScrollPane(logic, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(790, 500));
		add(scroll);
	}
}
