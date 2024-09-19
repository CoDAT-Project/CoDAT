package eshmun.gui.kripke.dialogs;

import eshmun.Eshmun;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class StateDialog extends JDialog {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 5072383701249974194L;
	
	private boolean success;
	private Eshmun eshmun;
	private String name;
	private String originalName;
	private String originalLabels;
	private String labels;
	
	private String preCondition;  
	
	private JTextField nameField;
	private JTextField labelsField;
	private JCheckBox isStartCheck;
	private JCheckBox isRetainCheck;
	private JTextArea preConditionField;
	
	private final int width = 680;
	private final int height = 330;
	public StateDialog(Eshmun eshmunInstance, String previousName, String previousLabels, String previousPreCondition,  boolean isStart, boolean isRetain) {
		super(eshmunInstance, true);
		final StateDialog This = this;
		this.eshmun = eshmunInstance;
		
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		
		this.success = false;
		this.name = previousName;
		this.originalName = previousName;
		this.originalLabels = previousLabels;
		this.labels = previousLabels;
		
		this.preCondition = previousPreCondition.replace("   ", "");
		
		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		
		
		setResizable(false);
		setTitle("State");
		
		FlowLayout f1 = new FlowLayout(FlowLayout.LEFT, 10, 1);
		setLayout(f1);
		
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(width - 10, 45));
		
		nameField = new JTextField(name, 45);
		JLabel nameLabel = new JLabel("State Name: ");
		
		panel1.setLayout(f1);
		panel1.add(nameLabel);
		panel1.add(nameField);
		
		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(width - 10, 45));
		
		labelsField = new JTextField(labels, 45);
		JLabel labelsLabel = new JLabel("<html>Labels:  <br>(, seperated)</html>");
		
		panel2.setLayout(f1);
		panel2.add(labelsLabel);
		panel2.add(labelsField);
		
		
		JPanel panel5 = new JPanel();
		panel5.setPreferredSize(new Dimension(width - 10, 85));
		
	 
		JLabel preConditionLabel = new JLabel("Predicate: ");
		panel5.add(preConditionLabel);
		preConditionField = new JTextArea(preCondition);
 		preConditionField.setColumns(45);
 		preConditionField.setRows(5);
		preConditionField.setLineWrap(true);
		
		panel5.add(new JScrollPane(preConditionField ));
		
		//preConditionField.setWrapStyleWord (true);
		
		 
		
		//panel5.add(preConditionField);
		
		panel5.setLayout(f1);
		
		
		
		
		JPanel panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(width - 10, 45));
		
		isStartCheck = new JCheckBox();
		isStartCheck.setSelected(isStart);
		
		JLabel startLabel = new JLabel("Start State");
		
		isRetainCheck = new JCheckBox();
		isRetainCheck.setSelected(isRetain);
		isRetainCheck.setMargin(new Insets(0, 10, 0, 0));
		
		JLabel retainLabel = new JLabel("Retain State");

		panel3.setLayout(f1);
		panel3.add(isStartCheck);
		panel3.add(startLabel);
		panel3.add(isRetainCheck);
		panel3.add(retainLabel);
		
		JPanel panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(width - 10, 45));
		
		JButton doneButton = new JButton("Done");
		JButton cancelButton = new JButton("Cancel");
		
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = nameField.getText().replaceAll(" ", "");
				labels = labelsField.getText().replaceAll(" ", "");
				preCondition = preConditionField.getText().replaceAll(" ", "");
				if(name.trim().length() == 0) {
					JOptionPane.showMessageDialog(This, "State Name Cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(name.contains(".")) {
					JOptionPane.showMessageDialog(This, "State Name Cannot contain dots", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(!originalName.equals(name) && !eshmun.getCurrentGraphPanel().verifyNewName(name.trim())) {
					JOptionPane.showMessageDialog(This, "Given State Name already exists", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(!originalLabels.equals(labels)) {
					List<String> a1 = Arrays.asList(originalLabels.split(","));
					List<String> a2 = Arrays.asList(labels.split(","));
					if(!(a2.containsAll(a1) && a1.containsAll(a2))) {
						if(!eshmun.getCurrentGraphPanel().verifyNewLabel(labels.trim())) {
							JOptionPane.showMessageDialog(This, "Given labels are not unique", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
				
				success = true;
				dispose();
			}
		});
		
		
		AbstractAction cancelActionListener = new AbstractAction() {
			/**
			 * Auto generated Serial UID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		};
		
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
	    rootPane.getActionMap().put("ESCAPE", cancelActionListener);
		
		cancelButton.addActionListener(cancelActionListener);
		
		panel4.setLayout(f1);
		panel4.add(doneButton);
		panel4.add(cancelButton);
		
		add(panel1);
		add(panel2);
		add(panel5);
		add(panel3);
		add(panel4);
		
		getRootPane().setDefaultButton(doneButton);
	}
	
	public String getName() {
		return name.trim();
	}
	
	public String getLabels() {
		return labels.trim();
	}
	
	public boolean isStart() {
		return isStartCheck.isSelected();
	}
	
	public boolean isRetain() {
		return isRetainCheck.isSelected();
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	public String getPreCondition() {
		return preCondition.trim();
	}
}
