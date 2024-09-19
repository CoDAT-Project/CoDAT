package eshmun;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.lts.kripke.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BoxBuilder extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtInState;
	private JTextField txtInLabels;
	private JTable tblStates;
	private JLabel lblOutState;
	private JTextField txtOutState;
	private JLabel label_1;
	private JTextField txtOutLabels;
	protected List<KripkeState> outStates;
	protected Kripke currentKripke;
	
	

	/**
	 * Create the frame.
	 */
	public BoxBuilder(final Kripke kripke) {
		
		currentKripke = kripke;
		outStates = new ArrayList<KripkeState>();
		setTitle("Boxes Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 536, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBoxName = new JLabel("Box Name:");
		lblBoxName.setBounds(10, 20, 75, 28);
		contentPane.add(lblBoxName);
		
		txtName = new JTextField();
		txtName.setBounds(95, 24, 100, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblInState = new JLabel("In State:");
		lblInState.setBounds(10, 64, 57, 28);
		contentPane.add(lblInState);
		
		txtInState = new JTextField();
		txtInState.setColumns(10);
		txtInState.setBounds(95, 68, 100, 20);
		contentPane.add(txtInState);
		
		JLabel lblStateLabels = new JLabel("State labels (,):");
		lblStateLabels.setBounds(215, 64, 90, 28);
		contentPane.add(lblStateLabels);
		
		txtInLabels = new JTextField();
		txtInLabels.setColumns(10);
		txtInLabels.setBounds(315, 68, 120, 20);
		contentPane.add(txtInLabels);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 175, 425, 199);
		contentPane.add(scrollPane);
		

		tblStates = new JTable();
		scrollPane.setViewportView(tblStates);
		tblStates.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"State", "Labels", "X"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		lblOutState = new JLabel("Out State:");
		lblOutState.setBounds(10, 115, 57, 28);
		contentPane.add(lblOutState);
		
		txtOutState = new JTextField();
		txtOutState.setColumns(10);
		txtOutState.setBounds(95, 119, 100, 20);
		contentPane.add(txtOutState);
		
		label_1 = new JLabel("State labels (,):");
		label_1.setBounds(215, 115, 90, 28);
		contentPane.add(label_1);
		
		txtOutLabels = new JTextField();
		txtOutLabels.setColumns(10);
		txtOutLabels.setBounds(315, 119, 120, 20);
		contentPane.add(txtOutLabels);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				addOutState(currentKripke);
			}
		});
		btnNewButton.setBounds(453, 118, 57, 23);
		contentPane.add(btnNewButton);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createBox(currentKripke);
			}
		});
		btnCreate.setBounds(421, 385, 89, 23);
		contentPane.add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
			}
		});
		btnCancel.setBounds(10, 385, 89, 23);
		contentPane.add(btnCancel);
		tblStates.getColumnModel().getColumn(2).setPreferredWidth(20);
		tblStates.getColumnModel().getColumn(2).setMaxWidth(20);
	}
	
	private void addOutState(Kripke kripke)
	{
		KripkeState outState = createState(kripke, txtOutState.getText(), txtOutLabels.getText());
		if(outState != null)
		{
			outStates.add(outState);
		}
	}
	
	private void createBox(Kripke kripke)
	{
		KripkeState inState = createState(kripke, txtInState.getText(), txtInLabels.getText());
		if(outStates.size() > 0)
		{
			if(inState != null)
			{
				Box newBox = new Box(txtName.getText(), inState, outStates);
				kripke.addBox(newBox);
			}
			else
				JOptionPane
				.showMessageDialog(
						null,
						"In State is required!");
		}
		else
			JOptionPane
			.showMessageDialog(
					null,
					"At least one out State is required!");
	}
	
	private KripkeState createState(Kripke kripke, String stateName, String labels)
	{
		Pattern statePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Pattern labelPattern = Pattern
				.compile("[a-zA-Z][a-zA-Z0-9]*(,(\\s)*[a-zA-Z][a-zA-Z0-9]*)*");
		KripkeState state = null;
		if (stateName != null && stateName.trim() != "" && stateName.length() > 0) {					
			if (statePattern.matcher(stateName).matches()) {
				if (labels.length() == 0
						|| labelPattern.matcher(labels).matches()) {
					if(kripke.getState(stateName) == null)
					{
						state = new KripkeState(kripke, stateName, false);
						kripke.addState(state);
						String[] lbs = labels.split(",");
						for (String str : lbs) {
							if(!str.trim().equals(""))
							{
								BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
										str.trim()));
								PredicateFormulaValuation predValue = new PredicateFormulaValuation(
										kripke, state, pred, true);
								state.addPredicateFormulaValuation(predValue);
							}
						}
					}
					else
						JOptionPane
						.showMessageDialog(
								null,
								"State already exists!");
				} else
					JOptionPane
					.showMessageDialog(
							null,
							"Labels name must start with a letter and can contain only letters or digits.\nLabels must be comma seperated.");
			} else
				JOptionPane
				.showMessageDialog(null,
						"State name must start with a letter and can contain only letters or digits!");

		} else
			JOptionPane.showMessageDialog(null, "State is required!");
		
		return state;
		
	}
	
}
