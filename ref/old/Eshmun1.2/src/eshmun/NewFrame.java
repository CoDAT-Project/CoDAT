package eshmun;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;

import javax.imageio.ImageIO;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.modelchecker.ModelChecker;
import eshmun.modelrepairer.FormulaString;
import eshmun.modelrepairer.FormulaStringCollection;
import eshmun.modelrepairer.OptimizedFormulaeGenerator;
import eshmun.parser.ANTLRParser;
import eshmun.sat.SAT4jSolver;

public class NewFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtLabel;
	private JTextField txtState;
	private JTextField txtTransName;
	private JTable tblTransition;
	private JTable tblStates;
	JScrollPane statesScroll;
	
	
	////////// 
	private Kripke kripkeModel;
	private PredicateFormula ctlFormula;
	private PredicateFormula CNFFormula;
	private int transvariablecount = 0;
	boolean modelWasChecked;
	boolean modelWasRepaired;
	private FormulaStringCollection formulaStringList; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewFrame frame = new NewFrame();
					frame.setTitle("Program Repair");
					//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
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
	public NewFrame() {
		kripkeModel = new Kripke();
		ctlFormula = null;
		CNFFormula = null;
		formulaStringList = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
//////////States///////////////States////////////States////////////States//////////////States///////////
		
		Panel statesPanel = new Panel();
		statesPanel.setLayout(null);
		statesPanel.setBounds(0, 10, this.getWidth()/3, (this.getHeight()/2) - 50);
		contentPane.add(statesPanel);
		
		statesScroll = new JScrollPane();
		
		statesScroll.setBounds(10, 74, statesPanel.getWidth() - 10, statesPanel.getHeight() - 80);
		statesPanel.add(statesScroll);
		
		tblStates = new JTable();
		statesScroll.setViewportView(tblStates);
		tblStates.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "State", "Labels", "is Start" }) {
			Class[] columnTypes = new Class[] { Object.class, Object.class,
					Boolean.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		
		txtLabel = new JTextField();
		txtLabel.setColumns(10);
		txtLabel.setBounds(92, 33, 100, 20);
		statesPanel.add(txtLabel);
		
		JLabel label = new JLabel("Labels(comma seperated)");
		label.setBounds(92, 11, 159, 14);
		statesPanel.add(label);
		
		txtState = new JTextField();
		txtState.setBounds(10, 33, 62, 20);
		statesPanel.add(txtState);
		
		JLabel label_1 = new JLabel("State");
		label_1.setBounds(10, 11, 46, 14);
		statesPanel.add(label_1);
		
		final JCheckBox chkIsStartState = new JCheckBox("");
		chkIsStartState.setBounds(212, 32, 21, 23);
		statesPanel.add(chkIsStartState);
			
		JButton btnAddState = new JButton("Add");
		btnAddState.setBounds(257, 32, 70, 23);
		statesPanel.add(btnAddState);
		
		
////////////Transitions///////////////Transitions////////////Transitions////////////Transitions//////////////Transitions///////////
		
	Panel trnsPanel = new Panel();
	trnsPanel.setLayout(null);
	trnsPanel.setBounds(0, statesPanel.getY() + statesPanel.getHeight() + 10, this.getWidth()/3, (this.getHeight()/2)-50);
	contentPane.add(trnsPanel);
	
	final JComboBox cmbFromState = new JComboBox();
	cmbFromState.setModel(new DefaultComboBoxModel(
			new String[] { "-Slct-" }));
	cmbFromState.setBounds(88, 36, 70, 20);
	trnsPanel.add(cmbFromState);
	
	final JComboBox cmbToState = new JComboBox();
	cmbToState.setModel(new DefaultComboBoxModel(
			new String[] { "-Slct-" }));
	cmbToState.setBounds(174, 36, 70, 20);
	trnsPanel.add(cmbToState);
	
	JLabel label_3 = new JLabel("From");
	label_3.setBounds(88, 11, 46, 14);
	trnsPanel.add(label_3);
	
	JLabel label_4 = new JLabel("To");
	label_4.setBounds(174, 11, 46, 14);
	trnsPanel.add(label_4);
	
	JButton btnAdd = new JButton("Add");
	
	btnAdd.setBounds(268, 35, 60, 23);
	trnsPanel.add(btnAdd);
	
	
	JPanel trnsList = new JPanel();
	trnsList.setLayout(null);
	trnsList.setBounds(0, 58, (trnsPanel.getWidth()), (trnsPanel.getHeight()) -70);
	trnsPanel.add(trnsList);
	
	JScrollPane trnsListScroll = new JScrollPane();
	trnsListScroll.setBounds(10, 11, (trnsList.getWidth()) -10, (trnsList.getHeight()) -10);
	trnsList.add(trnsListScroll);
	
	tblTransition = new JTable();
	tblTransition.setModel(new DefaultTableModel(new Object[][] {},
			new String[] { "Name", "From", "To" }));
	trnsListScroll.setViewportView(tblTransition);
	
	JLabel label_5 = new JLabel("Name");
	label_5.setBounds(10, 11, 46, 14);
	trnsPanel.add(label_5);
	
	txtTransName = new JTextField();
	txtTransName.setText("t0");
	txtTransName.setColumns(10);
	txtTransName.setBounds(10, 36, 59, 20);
	trnsPanel.add(txtTransName);
	
	/////Buttons///Buttons///Buttons///Buttons///Buttons///Buttons///Buttons///Buttons///Buttons///Buttons///Buttons
		
		btnAddState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String state = txtState.getText();
				String label = txtLabel.getText();
				Pattern statePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
				Pattern labelPattern = Pattern
						.compile("[a-zA-Z][a-zA-Z0-9]*(,(\\s)*[a-zA-Z][a-zA-Z0-9]*)*");
				if (state != null && state.trim() != "" && state.length() > 0) {
					if (statePattern.matcher(state).matches()) {
						if (label.length() == 0
								|| labelPattern.matcher(label).matches()) {
							DefaultTableModel defaultModel = (DefaultTableModel) tblStates
									.getModel();
							defaultModel.insertRow(
									tblStates.getRowCount(),
									new Object[] { txtState.getText(),
										txtLabel.getText(),
										chkIsStartState.isSelected() });
							DefaultComboBoxModel modelcmbFromState = (DefaultComboBoxModel) cmbFromState
									.getModel();
							modelcmbFromState.addElement(state);
							DefaultComboBoxModel modelcmbToState = (DefaultComboBoxModel) cmbToState
									.getModel();
							modelcmbToState.addElement(state);
							AddStateAndLabels(state, label,
									chkIsStartState.isSelected());
							txtState.setText("");
							txtLabel.setText("");
							chkIsStartState.setSelected(false);

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
			}
		});
		
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trans = txtTransName.getText();
				if (trans.length() > 0 && cmbFromState.getSelectedIndex() > 0
						&& cmbToState.getSelectedIndex() > 0) {
					
					DefaultTableModel defaultModel = (DefaultTableModel) tblTransition
							.getModel();
					for (int i = 0; i < defaultModel.getRowCount() - 1; i++) {
						
						Object transName = defaultModel.getValueAt(i, 0);
						{
							if(transName.equals(trans))
							{
								JOptionPane.showMessageDialog(null,
										"Task Name already exists!");
								return;
							}
								
						}
					}
					defaultModel.insertRow(tblTransition.getRowCount(),
							new Object[] {
						trans,
						cmbFromState.getSelectedItem().toString()
						.trim(),
						cmbToState.getSelectedItem().toString()
						.trim() });
					AddTransition(trans, cmbFromState.getSelectedItem()
							.toString().trim(), cmbToState.getSelectedItem()
							.toString().trim());
					cmbFromState.setSelectedIndex(0);
					cmbToState.setSelectedIndex(0);
					transvariablecount += 1;
					txtTransName.setText("t" + transvariablecount);
				} else
					JOptionPane.showMessageDialog(null,
							"All fields are required!");
			}
		});
		
		
		
		
		
////////////DiGraph///////////////DiGraph////////////DiGraph////////////DiGraph//////////////DiGraph///////////
		
		//Panel graphPanel = new Panel();
		//graphPanel.setLayout(null);
		//graphPanel.setBounds((this.getWidth()/2) + 10, statesScroll.getY() +10, (this.getWidth()/2) - 30, (this.getHeight()) - 160);
		//contentPane.add(graphPanel);
		
		//JScrollPane graphScroll = new JScrollPane();
		
		//graphScroll.setBounds((this.getWidth()/2) + 10, statesScroll.getY()+10, (this.getWidth()/2) - 30, (this.getHeight()) - 160);
		//graphPanel.add(graphScroll);
		
		
		//Dimension size = new Dimension((this.getWidth()/2) - 30, (this.getHeight()) - 160);
		ImagePanel myPicture = new ImagePanel("output.png");		
		myPicture.setLocation((this.getWidth()/3) + 10, statesScroll.getY()+10);
		JScrollPane sss= new JScrollPane(myPicture);
		sss.setBounds((this.getWidth()/3) + 10, statesScroll.getY()+10, (this.getWidth() * 2/3) - 30, (this.getHeight()) - 160);
		contentPane.add(sss);
		//contentPane.add(myPicture);
	}

	
	
	class ImagePanel extends JPanel {  
		  
		  private Image img;  
		  
		  public ImagePanel(String img, Dimension size) {  
		    this(new ImageIcon(img).getImage(), size);  
		  }  
		  
		  public ImagePanel(Image img, Dimension size) {  
		    this.img = img;  
		    setPreferredSize(size);  
		    //setMinimumSize(size);  
		    //setMaximumSize(size);  
		    setSize(size);  
		    setLayout(null);  
		  }  
		  public ImagePanel(String img) { 
			  this.img = new ImageIcon(img).getImage();
			    Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null)); 
			    setPreferredSize(size);  
			    //setMinimumSize(size);  
			    //setMaximumSize(size);  
			    setSize(size);  
			    setLayout(null);  
			  }  
		  
		  
		  public void paintComponent(Graphics g) {  
		    g.drawImage(img, 0, 0, null);  
		  }  
		  
		}  

	private void AddStateAndLabels(String state, String labels,
			boolean isStartState) {
		KripkeState s = new KripkeState(kripkeModel, state, isStartState);
		kripkeModel.addState(s);
		String[] lbs = labels.split(",");
		for (String str : lbs) {
			if(!str.trim().equals(""))
			{
				BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
						str.trim()));
				PredicateFormulaValuation predValue = new PredicateFormulaValuation(
						kripkeModel, s, pred, true);
				s.addPredicateFormulaValuation(predValue);
			}
		}
	}
	private void UpdateStateAndLabels(String state, String labels,
			boolean isStartState) {
		kripkeModel.removeState(state);
		KripkeState s = new KripkeState(kripkeModel, state, isStartState);
		kripkeModel.addState(s);
		String[] lbs = labels.split(",");
		for (String str : lbs) {
			if(!str.trim().equals(""))
			{
				BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
						str.trim()));
				PredicateFormulaValuation predValue = new PredicateFormulaValuation(
						kripkeModel, s, pred, true);
				s.addPredicateFormulaValuation(predValue);
			}
		}
	}


	private void DeleteState(String state) {
		kripkeModel.removeState(state);

	}

	private void AddTransition(String transName, String startState,
			String endState) {
		KripkeState fromState = kripkeModel.getState(startState.trim());
		KripkeState toState = kripkeModel.getState(endState.trim());
		Transition t = new Transition(fromState, toState, transName, transName);
		fromState.addOutgoingTransition(t);
	}


	private void DeleteTransition(String startState, String transName) {
		KripkeState state = kripkeModel.getState(startState);
		Transition transition = state.getTransition(transName);
		kripkeModel.removeTransition(state, transition);
	}

	private boolean FormulaModelCheck(String ctlFormulaText) {
		try {
			ANTLRParser parser = new ANTLRParser();
			PredicateFormula pf = parser.parse(ctlFormulaText);
			ctlFormula = pf;
			ModelChecker modelCheck = new ModelChecker();
			List<PredicateFormula> pfCollection = new ArrayList<PredicateFormula>();
			pfCollection.add(pf);

			return modelCheck.modelCheck(kripkeModel, pfCollection);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	
	public void setValueAt(Object value, int row, int col) 
	{
		JOptionPane.showMessageDialog(null, "");
	}

	private String SaveFileText(DefaultTableModel tbtSts,
			DefaultTableModel tblTrans) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < tbtSts.getRowCount(); i++) {
			String column1 = tbtSts.getValueAt(i, 0).toString().trim();
			String column2 = tbtSts.getValueAt(i, 1).toString().trim();
			boolean column3 = (Boolean) tbtSts.getValueAt(i, 2);
			builder.append(column1 + ":" + column2 + ":" + column3 + ";");
		}
		builder.append("***");
		for (int i = 0; i < tblTrans.getRowCount(); i++) {
			String column1 = tblTrans.getValueAt(i, 0).toString().trim();
			String column2 = tblTrans.getValueAt(i, 1).toString().trim();
			String column3 = tblTrans.getValueAt(i, 2).toString().trim();
			builder.append(column1 + ":" + column2 + ":" + column3 + ";");
		}

		return builder.toString();
	}

	private void CreateSavedFile(DefaultTableModel tbtSts,
			DefaultTableModel tblTrans) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("kripke.txt"));
			int status = fileChooser.showSaveDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File target = fileChooser.getSelectedFile();

				if (target.exists())
					target.delete();

				target.createNewFile();

				FileWriter fw = new FileWriter(target);
				PrintWriter pw = new PrintWriter(fw);
				String stmts = SaveFileText(tbtSts, tblTrans);
				pw.println(stmts);
				pw.flush();
				fw.flush();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void LoadSatSolution(DefaultTableModel tblCnf, List<FormulaString> list, String[] solution)
	{
		emptyTable(tblCnf); 
		for (FormulaString fs : list) {
			String state = (fs.getState() == null) ? "" : fs.getState().getName();
			String trans = (fs.getEndState() == null) ? "" : fs.getEndState().getName();
			String formula = (fs.getFormula() == null) ? "" : fs.getFormula().toString();
			tblCnf.insertRow(tblCnf.getRowCount(), new Object[] {
				fs.getIndex(), fs.getEorX(), state, trans, formula, fs.getSuperScriptIndex(), Integer.parseInt(solution[fs.getIndex() - 1])>=0  });
						
		}
		
	}
	private void LoadFileText(DefaultTableModel tbtSts,
			DefaultTableModel tblTrans,
			DefaultComboBoxModel modelcmbFromState,
			DefaultComboBoxModel modelcmbToState) {
		try {
			
			JFileChooser fileChooser = new JFileChooser();

			int status = fileChooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				final Charset ENCODING = StandardCharsets.UTF_8;
				List<String> allText = Files.readAllLines(Paths.get(fileChooser
						.getSelectedFile().getAbsolutePath()), ENCODING);
				if (allText.size() > 0) {
					
					resetForm(modelcmbFromState,
							modelcmbToState);
					
					
					String text = allText.get(0);
					String[] AllData = text.split("\\*\\*\\*");
					String[] states = AllData[0].split(";");
					for (String state : states) {
						String[] cells = state.split(":");
						tbtSts.insertRow(tblStates.getRowCount(), new Object[] {
							cells[0], cells[1], Boolean.valueOf(cells[2]) });
						modelcmbFromState.addElement(cells[0]);
						modelcmbToState.addElement(cells[0]);
						AddStateAndLabels(cells[0], cells[1], Boolean.valueOf(cells[2]));
					}
					String[] trans = AllData[1].split(";");
					for (String tr : trans) {
						String[] cells = tr.split(":");
						tblTrans.insertRow(tblTrans.getRowCount(),
								new Object[] { cells[0], cells[1], cells[2] });
						AddTransition(cells[0], cells[1], cells[2]);
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void DoDrawDiagram() {
		try {
			File dotFile = new File("kripke.dot");
			if (dotFile.exists())
				dotFile.delete();
			dotFile.createNewFile();
			FileWriter fw = new FileWriter(dotFile);
			PrintWriter pw = new PrintWriter(fw);
			String[] stmts = kripkeModel.GenerateDotText();
			for (String str : stmts) {
				pw.println(str);
			}
			pw.flush();
			fw.flush();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String command = "cmd /c dot -Tpng kripke.dot -o output.png";
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			// Thread.sleep(10000);
			if (p.exitValue() == 0) {
				BufferedImage myPicture = ImageIO.read(new File("output.png"));
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				JScrollPane scroll = new JScrollPane();
				//scroll.setBounds(10, 10, myPicture.getWidth() + 40,
						//myPicture.getHeight() + 40);
				JScrollBar bar = new JScrollBar();
				JFrame frame = new JFrame();
				frame.getContentPane().add(scroll);
				// frame.setBounds(0,0, myPicture.getHeight(),
				// myPicture.getWidth());
				frame.setBounds(0, 0, myPicture.getWidth() + 50,
						myPicture.getHeight() + 50);
				//scroll.add(picLabel);
				frame.getContentPane().add(picLabel);
				frame.show();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void emptyTable(DefaultTableModel myTableModel)
	{
		if (myTableModel.getRowCount() > 0) {
		    for (int i = myTableModel.getRowCount() - 1; i > -1; i--) {
		        myTableModel.removeRow(i);
		    }
		}
	}
	private void emptyCombo(DefaultComboBoxModel myComboModel)
	{
		if (myComboModel.getSize() > 0) {
		    for (int i = myComboModel.getSize() - 1; i > -1; i--) {
		    	myComboModel.removeElementAt(i);
		    }
		    myComboModel.addElement("--Select--");
		}
	}
	private void resetForm(DefaultComboBoxModel modelcmbFromState,
			DefaultComboBoxModel modelcmbToState)
	{
		
		emptyTable((DefaultTableModel) tblStates.getModel());
		emptyTable((DefaultTableModel) tblTransition.getModel());
		emptyCombo(modelcmbFromState);
		emptyCombo(modelcmbToState);
		kripkeModel = new Kripke();
		ctlFormula = null;
		CNFFormula = null;
		formulaStringList = null;
	}

}
