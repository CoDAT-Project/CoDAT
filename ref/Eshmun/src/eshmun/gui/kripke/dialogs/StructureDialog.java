package eshmun.gui.kripke.dialogs;

import eshmun.Eshmun;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class StructureDialog extends JDialog {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 5072383701249974194L;
	
	private boolean success;
	private Eshmun eshmun;
	
	private String name;
	private String processes;
	private String oldName;
	private String oldProcesses;
	
	private JTextField nameField;
	private JTextField processesField;
	
	private final int width = 480;
	private final int height = 230;
	public StructureDialog(Eshmun eshmunInstance, String previousName, String previousProcesses) {
		super(eshmunInstance, true);
		final StructureDialog This = this;
		this.eshmun = eshmunInstance;
		
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		
		this.success = false;
		this.name = previousName;
		this.processes = previousProcesses;
		this.oldName = previousName;
		this.oldProcesses = previousProcesses.trim().equals("") ? "-,-" : previousProcesses;
		
		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		
		
		setResizable(false);
		setTitle("Kripke Structure");
		
		FlowLayout f1 = new FlowLayout(FlowLayout.CENTER, 10, 1);
		setLayout(f1);
		
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(width - 10, 45));
		
		nameField = new JTextField(name, 25);
		JLabel nameLabel = new JLabel("Structure Name: ");
		
		panel1.setLayout(f1);
		panel1.add(nameLabel);
		panel1.add(nameField);
		
		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(width - 10, 45));
		
		processesField = new JTextField(processes, 25);
		JLabel labelsLabel = new JLabel("<html>Processes:  <br>(, seperated)</html>");
		
		panel2.setLayout(f1);
		panel2.add(labelsLabel);
		panel2.add(processesField);
		
		JPanel panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(width - 10, 45));
		
		JButton doneButton = new JButton("Done");
		JButton cancelButton = new JButton("Cancel");
		
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = nameField.getText().trim();
				processes = processesField.getText().trim();
				
				String nameLookUp = name;
				if(name.equals(oldName.trim())) {
					nameLookUp = "";
				}
				
				if(!processes.matches("^[^,\\s]+(\\s)*(,(\\s)*[^,\\s]+)*$")) {
					JOptionPane.showMessageDialog(This, "Given Processes are invalid", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] newProc = processes.split(",");
				String[] oldProc = oldProcesses.split(",");
				
				for(int i = 0; i < Math.max(newProc.length, oldProc.length); i++) {
					if(i < newProc.length) newProc[i] = newProc[i].trim();
					if(i < oldProc.length) oldProc[i] = oldProc[i].trim();
				}
				
				Arrays.sort(newProc); Arrays.sort(oldProc);
				if(Arrays.equals(oldProc, newProc)) newProc = null;

				if(name.trim().length() == 0) {
					JOptionPane.showMessageDialog(This, "Structure Name Cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(newProc != null && eshmun.contains(nameLookUp, newProc)) {
					JOptionPane.showMessageDialog(This, "a Structure with given name or processes already exists", "Error", JOptionPane.ERROR_MESSAGE);
					return;
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
		add(panel4);
		
		getRootPane().setDefaultButton(doneButton);
	}
	
	public String getName() {
		return name.trim();
	}
	
	public String getProcesses() {
		return processes.trim();
	}
	
	public boolean isSuccessful() {
		return success;
	}
}
