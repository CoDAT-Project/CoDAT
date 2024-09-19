package eshmun.gui.kripke.utils;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import eshmun.Eshmun;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class AbstractionCTLPanel extends JFrame {

	/**
	 * this class contain the implementation of the UI used by the abstract by set of CTL formulae feature
	 */
	private static final long serialVersionUID = 1L;
	private LogicTextPane formula1, formula2, formula3, formula4, formula5, formula6, formula7, formula8, formula9, formula10;
	private JScrollPane scroll1, scroll2, scroll3, scroll4, scroll5, scroll6, scroll7, scroll8, scroll9, scroll10;
	private JButton btnCancel, Abstract, addFormula, btnDelete2, btnDelete3, btnDelete4, btnDelete5, btnDelete6, btnDelete7, btnDelete8, btnDelete9, btnDelete10;
	private static int deleteID = 0;
	private static int addID = 2;
	public static ArrayList<String> CTLs = new ArrayList<String>();
	
	/**
	 * Construct a new abstraction UI instance.
	 */
	public AbstractionCTLPanel() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					setVisible(true);
//					JOptionPane.showMessageDialog(null, "This is a Beta Feature. We assumed that the state names range from S0 (start State) to S(N-1) where N is the total number of states",
//							"Beta Feature", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("Abstraction Formulae");
		this.setSize(800, 720);
		setDefaultCloseOperation(0);
		this.setLocation(280, 0);
		getContentPane().setLayout(null);


		JLabel lblEnterTheAbstraction = new JLabel("Enter the Abstraction CTL Formulae:");
		lblEnterTheAbstraction.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEnterTheAbstraction.setBounds(25, 10, 260, 25);
		getContentPane().add(lblEnterTheAbstraction);

		Abstract = new JButton("Abstract");
		Abstract.setFont(new Font("Tahoma", Font.BOLD, 12));
		Abstract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extractFormulae();
				Eshmun.eshmun.abstractByCTLFormulae();
				resetUI();
				dispose();
			}
		});
		Abstract.setBounds(290, 640, 140, 40);
		getContentPane().add(Abstract);

		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetUI();
				dispose();
			}
		});
		btnCancel.setBounds(650, 647, 120, 25);
		getContentPane().add(btnCancel);

		addFormula = new JButton("Add Formula");
		addFormula.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addFormula.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addLogicTextPane();
			}
		});
		addFormula.setBounds(650, 53, 120, 25);
		getContentPane().add(addFormula);

		formula1 = new LogicTextPane("Formula #1", Eshmun.eshmun, true);
		scroll1 = new JScrollPane(formula1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll1.setBounds(25, 40, 600, 50);
		getContentPane().add(scroll1);

		formula2 = new LogicTextPane("Formula #2", Eshmun.eshmun, true);
		scroll2 = new JScrollPane(formula2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setBounds(25, 100, 600, 50);
		getContentPane().add(scroll2);

		formula3 = new LogicTextPane("Formula #3", Eshmun.eshmun, true);
		scroll3 = new JScrollPane(formula3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll3.setBounds(25, 160, 600, 50);
		getContentPane().add(scroll3);

		formula4 = new LogicTextPane("Formula #4", Eshmun.eshmun, true);
		scroll4 = new JScrollPane(formula4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll4.setBounds(25, 220, 600, 50);
		getContentPane().add(scroll4);

		formula5 = new LogicTextPane("Formula #5", Eshmun.eshmun, true);
		scroll5 = new JScrollPane(formula5, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll5.setBounds(25, 280, 600, 50);
		getContentPane().add(scroll5);

		formula6 = new LogicTextPane("Formula #6", Eshmun.eshmun, true);
		scroll6 = new JScrollPane(formula6, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll6.setBounds(25, 340, 600, 50);
		getContentPane().add(scroll6);

		formula7 = new LogicTextPane("Formula #7", Eshmun.eshmun, true);
		scroll7 = new JScrollPane(formula7, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll7.setBounds(25, 400, 600, 50);
		getContentPane().add(scroll7);

		formula8 = new LogicTextPane("Formula #8", Eshmun.eshmun, true);
		scroll8 = new JScrollPane(formula8, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll8.setBounds(25, 460, 600, 50);
		getContentPane().add(scroll8);

		formula9 = new LogicTextPane("Formula #9", Eshmun.eshmun, true);
		scroll9 = new JScrollPane(formula9, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll9.setBounds(25, 520, 600, 50);
		getContentPane().add(scroll9);

		formula10 = new LogicTextPane("Formula #10", Eshmun.eshmun, true);
		scroll10 = new JScrollPane(formula10, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll10.setBounds(25, 580, 600, 50);
		getContentPane().add(scroll10);

		btnDelete2 = new JButton("Delete");
		btnDelete2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 2;
				deleteLogicTextPane();
			}
		});
		btnDelete2.setBounds(650, 110, 120, 25);
		getContentPane().add(btnDelete2);

		btnDelete3 = new JButton("Delete");
		btnDelete3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 3;
				deleteLogicTextPane();
			}
		});
		btnDelete3.setBounds(650, 170, 120, 25);
		getContentPane().add(btnDelete3);

		btnDelete4 = new JButton("Delete");
		btnDelete4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 4;
				deleteLogicTextPane();
			}
		});
		btnDelete4.setBounds(650, 230, 120, 25);
		getContentPane().add(btnDelete4);

		btnDelete5 = new JButton("Delete");
		btnDelete5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 5;
				deleteLogicTextPane();
			}
		});
		btnDelete5.setBounds(650, 290, 120, 25);
		getContentPane().add(btnDelete5);

		btnDelete6 = new JButton("Delete");
		btnDelete6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 6;
				deleteLogicTextPane();
			}
		});
		btnDelete6.setBounds(650, 350, 120, 25);
		getContentPane().add(btnDelete6);

		btnDelete7 = new JButton("Delete");
		btnDelete7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 7;
				deleteLogicTextPane();
			}
		});
		btnDelete7.setBounds(650, 410, 120, 25);
		getContentPane().add(btnDelete7);

		btnDelete8 = new JButton("Delete");
		btnDelete8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 8;
				deleteLogicTextPane();
			}
		});
		btnDelete8.setBounds(650, 470, 120, 25);
		getContentPane().add(btnDelete8);

		btnDelete9 = new JButton("Delete");
		btnDelete9.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 9;
				deleteLogicTextPane();
			}
		});
		btnDelete9.setBounds(650, 530, 120, 25);
		getContentPane().add(btnDelete9);

		btnDelete10 = new JButton("Delete");
		btnDelete10.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteID = 10;
				deleteLogicTextPane();
			}
		});
		btnDelete10.setBounds(650, 590, 120, 25);
		getContentPane().add(btnDelete10);

		btnDelete2.setVisible(false);
		btnDelete3.setVisible(false);
		btnDelete4.setVisible(false);
		btnDelete5.setVisible(false);
		btnDelete6.setVisible(false);
		btnDelete7.setVisible(false);
		btnDelete8.setVisible(false);
		btnDelete9.setVisible(false);
		btnDelete10.setVisible(false);

		scroll2.setVisible(false);
		scroll3.setVisible(false);
		scroll4.setVisible(false);
		scroll5.setVisible(false);
		scroll6.setVisible(false);
		scroll7.setVisible(false);
		scroll8.setVisible(false);
		scroll9.setVisible(false);
		scroll10.setVisible(false);
	}
	
	/**
	 * Reset all the UI elements back to default
	 */
	private void resetUI() {
		btnDelete2.setVisible(false);
		btnDelete3.setVisible(false);
		btnDelete4.setVisible(false);
		btnDelete5.setVisible(false);
		btnDelete6.setVisible(false);
		btnDelete7.setVisible(false);
		btnDelete8.setVisible(false);
		btnDelete9.setVisible(false);
		btnDelete10.setVisible(false);

		scroll2.setVisible(false);
		scroll3.setVisible(false);
		scroll4.setVisible(false);
		scroll5.setVisible(false);
		scroll6.setVisible(false);
		scroll7.setVisible(false);
		scroll8.setVisible(false);
		scroll9.setVisible(false);
		scroll10.setVisible(false);
		
		deleteID = 0;
		addID = 2;
		CTLs = new ArrayList<String>();
	}

	/**
	 * extract all CTL formulae from the logic text pane
	 */
	private void extractFormulae() {
		if(!formula1.getText().isEmpty() && !formula1.getText().contains("Formula")) {
			CTLs.add(formula1.getText());
		}
		if(!formula2.getText().isEmpty() && !formula2.getText().contains("Formula")) {
			CTLs.add(formula2.getText());
		}
		if(!formula3.getText().isEmpty() && !formula3.getText().contains("Formula")) {
			CTLs.add(formula3.getText());
		}
		if(!formula4.getText().isEmpty() && !formula4.getText().contains("Formula")) {
			CTLs.add(formula4.getText());
		}
		if(!formula5.getText().isEmpty() && !formula5.getText().contains("Formula")) {
			CTLs.add(formula5.getText());
		}
		if(!formula6.getText().isEmpty() && !formula6.getText().contains("Formula")) {
			CTLs.add(formula6.getText());
		}
		if(!formula7.getText().isEmpty() && !formula7.getText().contains("Formula")) {
			CTLs.add(formula7.getText());
		}
		if(!formula8.getText().isEmpty() && !formula8.getText().contains("Formula")) {
			CTLs.add(formula8.getText());
		}
		if(!formula9.getText().isEmpty() && !formula9.getText().contains("Formula")) {
			CTLs.add(formula9.getText());
		}
		if(!formula10.getText().isEmpty() && !formula10.getText().contains("Formula")) {
			CTLs.add(formula10.getText());
		}
		if(CTLs.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter at least 1 CTL formula",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * allow to add new LogicTextPane element to the UI
	 */
	private void addLogicTextPane() {
		switch(addID) {
		case 2:
			btnDelete2.setVisible(true);
			scroll2.setVisible(true);
			addID++;
			break;
		case 3:
			btnDelete2.setVisible(false);
			btnDelete3.setVisible(true);
			scroll3.setVisible(true);
			addID++;
			break;
		case 4:
			btnDelete3.setVisible(false);
			btnDelete4.setVisible(true);
			scroll4.setVisible(true);
			addID++;
			break;
		case 5:
			btnDelete4.setVisible(false);
			btnDelete5.setVisible(true);
			scroll5.setVisible(true);
			addID++;
			break;
		case 6:
			btnDelete5.setVisible(false);
			btnDelete6.setVisible(true);
			scroll6.setVisible(true);
			addID++;
			break;
		case 7:
			btnDelete6.setVisible(false);
			btnDelete7.setVisible(true);
			scroll7.setVisible(true);
			addID++;
			break;
		case 8:
			btnDelete7.setVisible(false);
			btnDelete8.setVisible(true);
			scroll8.setVisible(true);
			addID++;
			break;
		case 9:
			btnDelete8.setVisible(false);
			btnDelete9.setVisible(true);
			scroll9.setVisible(true);
			addID++;
			break;
		case 10:
			btnDelete9.setVisible(false);
			btnDelete10.setVisible(true);
			scroll10.setVisible(true);
			addID++;
			break;
		}	
	}

	/**
	 * delete the LogicTextPanes
	 */
	private void deleteLogicTextPane() {
		switch(deleteID) {
		case 2:
			btnDelete2.setVisible(false);
			scroll2.setVisible(false);
			formula2.setText(null);
			addID--;
			break;
		case 3:
			btnDelete2.setVisible(true);
			btnDelete3.setVisible(false);
			scroll3.setVisible(false);
			formula3.setText(null);
			addID--;
			break;
		case 4:
			btnDelete3.setVisible(true);
			btnDelete4.setVisible(false);
			scroll4.setVisible(false);
			formula4.setText(null);
			addID--;
			break;
		case 5:
			btnDelete4.setVisible(true);
			btnDelete5.setVisible(false);
			scroll5.setVisible(false);
			formula5.setText(null);
			addID--;
			break;
		case 6:
			btnDelete5.setVisible(true);
			btnDelete6.setVisible(false);
			scroll6.setVisible(false);
			formula6.setText(null);
			addID--;
			break;
		case 7:
			btnDelete6.setVisible(true);
			btnDelete7.setVisible(false);
			scroll7.setVisible(false);
			formula7.setText(null);
			addID--;
			break;
		case 8:
			btnDelete7.setVisible(true);
			btnDelete8.setVisible(false);
			scroll8.setVisible(false);
			formula8.setText(null);
			addID--;
			break;
		case 9:
			btnDelete8.setVisible(true);
			btnDelete9.setVisible(false);
			scroll9.setVisible(false);
			formula9.setText(null);
			addID--;
			break;
		case 10:
			btnDelete9.setVisible(true);
			btnDelete10.setVisible(false);
			scroll10.setVisible(false);
			formula10.setText(null);
			addID--;
			break;
		}
	}
}