package eshmun;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import eshmun.DecisionProcedure.DecisionProcedure;
import eshmun.expression.PredicateFormula;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.parser.ANTLRParser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DecisionProcFrame extends JFrame {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtF1;
	private JTextField txtF2;
	private JTextField txtMain;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DecisionProcFrame frame = new DecisionProcFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DecisionProcFrame() {
		setTitle("Decision Procedure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 267);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sub Formula 1:");
		lblNewLabel.setBounds(10, 11, 85, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblFormula = new JLabel("Sub Formula 2:");
		lblFormula.setBounds(10, 64, 85, 14);
		contentPane.add(lblFormula);
		
		txtF1 = new JTextField();
		txtF1.setBounds(105, 8, 307, 20);
		contentPane.add(txtF1);
		txtF1.setColumns(10);
		
		txtF2 = new JTextField();
		txtF2.setColumns(10);
		txtF2.setBounds(105, 61, 307, 20);
		contentPane.add(txtF2);
		
		final JLabel lblResult = new JLabel("");
		lblResult.setBounds(10, 187, 388, 14);
		contentPane.add(lblResult);
		
		JButton btnCheckSat = new JButton("Check Statisfiability");
		btnCheckSat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String result = CheckSatisfiabilityThroughDecProc(txtF1.getText(), txtF2.getText(), txtMain.getText());
				lblResult.setText(result);
			}
		});
		btnCheckSat.setBounds(261, 148, 151, 23);
		contentPane.add(btnCheckSat);
		
		
		
		JLabel lblAnd = new JLabel("AND");
		lblAnd.setBounds(236, 39, 57, 14);
		contentPane.add(lblAnd);
		
		JLabel lblImplies = new JLabel("Implies");
		lblImplies.setBounds(236, 92, 57, 14);
		contentPane.add(lblImplies);
		
		txtMain = new JTextField();
		txtMain.setColumns(10);
		txtMain.setBounds(105, 117, 307, 20);
		contentPane.add(txtMain);
		
		JLabel lblMainFormula = new JLabel("Main Formula:");
		lblMainFormula.setBounds(10, 120, 85, 14);
		contentPane.add(lblMainFormula);
	}
	
	private String CheckSatisfiabilityThroughDecProc(String f1, String f2, String mainF)
	{
		ANTLRParser parser = new ANTLRParser();
		PredicateFormula pf1, pf2, mainPf;
		try 
		{
			pf1 = parser.parse(CommonFunc.FormatInput(f1));
			pf2 = parser.parse(CommonFunc.FormatInput(f2));
			mainPf = parser.parse(CommonFunc.FormatInput(mainF));
			AndOperator pf1AnDpf2 = new AndOperator(pf1, pf2);
			NotOperator noTpf1AnDpf2 = new NotOperator(pf1AnDpf2);
			OrOperator noTpf1AnDpf2ORmainPf = new OrOperator(noTpf1AnDpf2, mainPf);
			DecisionProcedure proc = new DecisionProcedure(new NotOperator(noTpf1AnDpf2ORmainPf));
			if(proc.CheckStatisfiability())
			{
				return "not Satisfiable!";//because not
			}
			else
			{
				return "Satisfiable!";//because not
			}
		}
		catch (Exception e)
		{
			return e.getMessage();
		
		}
		
	}
}
