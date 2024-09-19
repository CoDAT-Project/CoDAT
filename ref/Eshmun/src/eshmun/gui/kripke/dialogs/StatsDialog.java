package eshmun.gui.kripke.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JLabel;

import eshmun.Eshmun;

/**
 * Used to display statistics about the repair after it is done.
 */
public class StatsDialog extends JDialog {

	/**
	 * Only keep one instance of the dialog and update it.
	 */
	private static StatsDialog dialogInstance = null;
	
	/**
	 * Stores numbers for previous repairs (to navigate between them).
	 */
	private static ArrayList<int[]> history = new ArrayList<>();
	
	/**
	 * The current repair position.
	 */
	private static int current = -1;
	
	/**
	 * Updates the dialog with new numbers.
	 * @param totalStates the total number of states.
	 * @param totalTransitions obvious.
	 * @param deletedStates obvious.
	 * @param deletedTransitions obvious.
	 * @param numberOfClauses obvious.
	 * @param numberOfLiterals obvious.
	 */
	public static void updateDialog(int totalStates, int totalTransitions, int deletedStates, 
			int deletedTransitions, int numberOfClauses, int numberOfLiterals) {
		if(dialogInstance == null) dialogInstance = new StatsDialog();
		
		dialogInstance.applyNumbers(totalStates, totalTransitions, deletedStates, 
									deletedTransitions, numberOfClauses, numberOfLiterals);
		
		int[] numbers = new int[] { totalStates, totalTransitions, deletedStates, deletedTransitions, numberOfClauses, numberOfLiterals };
		history.add(numbers);
		current++;
	}
	
	/**
	 * Display the next repair numbers.
	 */
	public static void goToNext() {
		if(current >= history.size()) return;

		int[] n = history.get(++current);
		dialogInstance.applyNumbers(n[0], n[1], n[2], n[3], n[4], n[5]);
		
	}
	
	/**
	 * Display the previous repair numbers.
	 */
	public static void goToPrevious() {
		if(current <= 0) return;

		int[] n = history.get(--current);
		dialogInstance.applyNumbers(n[0], n[1], n[2], n[3], n[4], n[5]);
		
	}
	
	/**
	 * Reset the history.
	 */
	public static void resetHistory() {
		history.clear();
		current = -1;
	}
	
	/**
	 * Brings the instance to front.
	 */
	public static void bringToFront() {
		if(dialogInstance != null) dialogInstance.toFront();
	}
	
	
	/**
	 * Auto-generated Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Panel containing the labels.
	 */
	private JPanel contentPanel;
	
	/**
	 * Create the dialog.
	 */
	private StatsDialog() {
		
		setTitle("Repair Statistics");
		setBounds(100, 100, 350, 250);
		setLocation(Eshmun.eshmun.getWidth() - 350 - 100, 150);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new JPanel();
				
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StatsDialog.this.dispose();
			}
		});			
		
		getRootPane().setDefaultButton(okButton);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}
	
	private void applyNumbers(int totalStates, int totalTransitions, int deletedStates, 
			int deletedTransitions, int numberOfClauses, int numberOfLiterals) {
		
		contentPanel.removeAll();
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel totalStatesLabel = new JLabel("Total States: " + totalStates);
		totalStatesLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(totalStatesLabel);
		
		JLabel totalTransitionsLabel = new JLabel("Total Transitions: " + totalTransitions);
		totalTransitionsLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(totalTransitionsLabel);
		
		JLabel deletedStatesLabel = new JLabel("Deleted States: " + deletedStates);
		deletedStatesLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(deletedStatesLabel);
		
		JLabel deletedTransitionsLabel = new JLabel("Deleted Transitions: " + deletedTransitions);
		deletedTransitionsLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(deletedTransitionsLabel);
		
		JLabel clausesLabel = new JLabel("Number of Clauses: " + numberOfClauses);
		clausesLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(clausesLabel);
		
		JLabel literalsLabel = new JLabel("Number of Literals: " + numberOfLiterals);
		literalsLabel.setPreferredSize(new Dimension(250, 22));
		contentPanel.add(literalsLabel);
		
		setVisible(true);
		toFront();
	}
	
	/**
	 * Forces dialog back to front.
	 */
	@Override
	public void toFront() {
		super.toFront();
		super.setAlwaysOnTop(true);
		super.toFront();
		super.requestFocus();
		super.setAlwaysOnTop(false);
	}
}