package eshmun;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import eshmun.DecisionProcedure.KripkeMergeState;
import eshmun.DecisionProcedure.KripkeSubStructGenerator;
import eshmun.DecisionProcedure.KripkeSubStructParent;
import eshmun.lts.kripke.*;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SubStructureFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField txtLabels;
	String fullModel = "FullModel";
	/**
	 * Create the frame.
	 */
	public SubStructureFrame(final FullFrameEvents parentFrame) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sub Structure Name:");
		lblNewLabel.setBounds(10, 21, 121, 14);
		contentPane.add(lblNewLabel);
		
		txtName = new JTextField();
		txtName.setBounds(158, 18, 165, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 78, 325, 292);
		contentPane.add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 307, 270);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"State", "Selected", "Exit "
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Boolean.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(155);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(51);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					boolean isChanged = false;
					DefaultTableModel defaultModel = (DefaultTableModel) table
							.getModel();
					for(int i=0;i<table.getRowCount();i++)
					{
						boolean toretain = (boolean)defaultModel.getValueAt(i, 1);						
						if(toretain )
						{
							isChanged= true;
						}
					}
					if(isChanged)
						 CreateSubAndUpdateInitial(parentFrame);
					dispose();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCreate.setBounds(10, 391, 89, 23);
		contentPane.add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(228, 391, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblSubStructureLabels = new JLabel("Sub Structure Labels:");
		lblSubStructureLabels.setBounds(10, 53, 138, 14);
		contentPane.add(lblSubStructureLabels);
		
		txtLabels = new JTextField();
		txtLabels.setColumns(10);
		txtLabels.setBounds(158, 47, 165, 20);
		contentPane.add(txtLabels);
		
		DefaultTableModel defaultModel = (DefaultTableModel) table
				.getModel();
		Kripke fullKripke = parentFrame.subStructures.get(fullModel);
		List<KripkeState> states;
		if(fullKripke == null)
		{
			states = parentFrame.kripkeModel.getListOfStates();
		}
		else
		{
			states = fullKripke.getListOfStates();
		}
		for (KripkeState state : states) {
			if(!(state instanceof KripkeMergeState) && !state.isPartOfMerge())
			{
				boolean selected = false;
				defaultModel.insertRow(table.getRowCount(),
					new Object[] {
				state.getName(), selected, false});
			}
		}		
		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private boolean CreateSubAndUpdateInitial(FullFrameEvents parentFrame) throws CloneNotSupportedException
	{
		String subName = txtName.getText();
		String labels = txtLabels.getText();
		Pattern labelPattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*(,(\\s)*[a-zA-Z][a-zA-Z0-9]*)*");
		Pattern namePattern = Pattern.compile("[a-zA-Z0-9]+");
		
		if(!labelPattern.matcher(labels).matches())
		{
			JOptionPane.showMessageDialog(null, "Labels name must start with a letter and can contain only letters or digits.\nLabels must be comma seperated!");
			return false;
		}
		if(parentFrame.subStructures.containsKey(subName) || subName.toLowerCase().trim().equals("fullmodel") || parentFrame.kripkeModel.getState(subName) != null)
		{
			JOptionPane.showMessageDialog(null, "Name already exists!");
			return false;
		}
		if(!namePattern.matcher(subName).matches())
		{
			JOptionPane.showMessageDialog(null, "Only letters or numbers allowed.");
			return false;
		}
		DefaultComboBoxModel<String> allSubs =  (DefaultComboBoxModel<String>)parentFrame.cmbSub.getModel();
		allSubs.addElement(subName);	
		KripkeSubStructParent fullKripke = (KripkeSubStructParent) parentFrame.subStructures.get(fullModel);
		if(fullKripke == null)
		{
			fullKripke = new KripkeSubStructParent(parentFrame.kripkeModel, parentFrame.kripkeModel.clone());
			parentFrame.subStructures.put(fullModel, fullKripke);
			allSubs.insertElementAt(fullModel,1);
		}
		DefaultTableModel defaultModel = (DefaultTableModel) table
				.getModel();
		List<String> statesToBeMerged = new ArrayList<String>();
		List<String> statesToBeLinked = new ArrayList<String>();
		for(int i=0;i<table.getRowCount();i++)
		{
			boolean toretain = (boolean)defaultModel.getValueAt(i, 1);
			boolean isExit = (boolean)defaultModel.getValueAt(i, 2);
			if(toretain && !isExit)
			{
				String stateName = defaultModel.getValueAt(i, 0).toString();				
				statesToBeMerged.add(stateName);
			}
			else if(toretain && isExit)
			{
				String stateName = defaultModel.getValueAt(i, 0).toString();				
				statesToBeLinked.add(stateName);
			}
		}
		Kripke subStructure = fullKripke.GenerateSubStructure(statesToBeMerged, statesToBeLinked, txtName.getText(), txtLabels.getText().replaceAll(" ", "").split(","));
		parentFrame.subStructures.put(fullModel, fullKripke);
		parentFrame.subStructures.put(subName, subStructure);
		parentFrame.cmbSub.setSelectedItem(subName);
		return true;
	}
	
}
