package eshmun;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.sun.xml.internal.ws.util.StringUtils;

import eshmun.expression.*;
import eshmun.expression.atomic.bool.*;
import eshmun.lts.kripke.*;
import eshmun.modelchecker.*;
import eshmun.modelrepairer.*;
import eshmun.parser.ANTLRParser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;


public class FullFrame extends JFrame {

	protected JPanel contentPane;
	protected JTextField txtLabel;
	protected JTextField txtState;
	protected JTable tblTransition;
	protected JTable tblStates;
	protected JButton btnAdd;
	protected Panel trnsPanel;
	protected JPanel panel;
	protected JScrollPane statesScroll;
	protected ImagePanel myPicture;
	protected JScrollPane myPictureScroll;
	protected JComboBox cmbFromState;
	protected JComboBox cmbToState;
	protected JButton btnAddState;
	protected JCheckBox chkIsStartState;
	protected Panel statesPanel;
	protected JButton btnSave;
	protected JButton btnLoad;
	protected JCheckBox chckAbstractionByGrouping;
	protected JCheckBox chckbxClear;
	protected JLabel lblModelCheckResult;
	protected JButton btnModelRepair;
	protected JButton btnCheckModel;
	protected JButton btnFormulae;
	protected JButton btnApplyToFull;
	protected JTextField txtFormulaes;
	protected JRadioButton rdbtnByFormula;
	protected JRadioButton rdbtnByLabel;	 
	protected JButton saveImageButton;
	protected JComboBox cmbCtlFormulae;
	protected JButton btnNewSub;
	protected JComboBox cmbSub;
	protected JButton btnX;
	protected JButton btnSatDecProc;
	protected JButton btnStateDelete;
	protected JButton btnStateDuplicate;
	protected JLabel label_2;
	protected JComboBox  cmbBox;
	
	protected JButton btnDelBox;
	////////// 
	protected List<Transition> transitionsToRetain;
	protected List<KripkeState> statesToRetain;
	protected Kripke kripkeModel;
	protected PredicateFormula ctlFormula;
	protected PredicateFormula CNFFormula;
	protected int transvariablecount = 0;
	protected boolean modelWasChecked;
	protected boolean modelWasRepaired;
	protected FormulaStringCollection formulaStringList;
	protected Kripke kripkeLabelModel;
	protected Kripke kripkeFormulaModel;
	protected Kripke kripkeByLabelGroupingModel;
	protected Kripke kripkeByFormulaGroupingModel;
	protected List<Transition> virtualTransitionsToBeDeleted;
	protected String currentFile;
	protected JCheckBox chckbxShortCut;
	protected Map<String, Kripke> subStructures;

	/**
	 * Launch the application.
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FullFrame frame = new FullFrame();
					frame.setTitle("Program Repair");
					//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} */

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("deprecation")
	public FullFrame() {
	    subStructures = new HashMap<String, Kripke>();   //boxes 
	    kripkeModel = new Kripke();   //model M to be repaired
	    ctlFormula = null;        //eta    
	    CNFFormula = null;       //CNF form of the repair(M,eta)
		formulaStringList = null;
		currentFile = "";
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(10, 10, 1200, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
//////////States///////////////States////////////States////////////States//////////////States///////////
		
		statesPanel = new Panel();
		statesPanel.setLayout(null);
		statesPanel.setBounds(0, 10, this.getWidth()/3, (this.getHeight()/2) - 50);
		contentPane.add(statesPanel);
		
		statesScroll = new JScrollPane();
		
		statesScroll.setBounds(10, 74, statesPanel.getWidth() - 10, statesPanel.getHeight() - 110);
		statesPanel.add(statesScroll);
		
		tblStates = new JTable();
		statesScroll.setViewportView(tblStates);
		tblStates.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"State", "Labels", "is Start", "Retain", ""
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, Boolean.class, Boolean.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblStates.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblStates.getColumnModel().getColumn(3).setPreferredWidth(30);
		tblStates.getColumnModel().getColumn(4).setResizable(false);
		tblStates.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		txtLabel = new JTextField();
		txtLabel.setColumns(10);
		txtLabel.setBounds(92, 33, 100, 20);
		statesPanel.add(txtLabel);
		
		JLabel label = new JLabel("Labels(comma seperated)");
		label.setBounds(92, 11, 189, 14);
		statesPanel.add(label);
		
		txtState = new JTextField();
		txtState.setBounds(10, 33, 62, 20);
		statesPanel.add(txtState);
		
		JLabel label_1 = new JLabel("State");
		label_1.setBounds(10, 11, 46, 14);
		statesPanel.add(label_1);
		
		chkIsStartState = new JCheckBox("");
		chkIsStartState.setBounds(212, 32, 21, 23);
		statesPanel.add(chkIsStartState);
			
		btnAddState = new JButton("Add");
		btnAddState.setBounds(257, 32, 80, 23);
		statesPanel.add(btnAddState);
		
		InputStream saveImage = getClass().getResourceAsStream("Save.png");
		BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(saveImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveImageButton = new JButton(new ImageIcon(buttonIcon));
		
		saveImageButton.setBorder(BorderFactory.createEmptyBorder());
		saveImageButton.setContentAreaFilled(false);
		saveImageButton.setBounds(385, 0, 15, 15);
		statesPanel.add(saveImageButton);
		
		btnStateDuplicate = new JButton("Dupl.");
		btnStateDuplicate.setBounds(274, statesPanel.getHeight() - 25, 82, 23);
		statesPanel.add(btnStateDuplicate);
		
		btnStateDelete = new JButton("Del.");		
		btnStateDelete.setBounds(338, 284, 62, 23);
		statesPanel.add(btnStateDelete);
		
		label_2 = new JLabel("Box:");
		label_2.setBounds(10, 293, 46, 14);
		statesPanel.add(label_2);
		cmbBox = new JComboBox();
		cmbBox.setBounds(47, 285, 86, 20);
		statesPanel.add(cmbBox);
		
		btnDelBox = new JButton("X");
		btnDelBox.setBounds(144, 284, 39, 23);
		statesPanel.add(btnDelBox);
		
		
		
		trnsPanel = new Panel();
		trnsPanel.setLayout(null);
		trnsPanel.setBounds(0, statesPanel.getY() + statesPanel.getHeight() + 10, this.getWidth()/3, (this.getHeight()/2)-50);
		contentPane.add(trnsPanel);
		
		cmbFromState = new JComboBox();
		cmbFromState.setModel(new DefaultComboBoxModel(
				new String[] { "-Slct-" }));
		cmbFromState.setBounds(10, 36, 70, 20);
		trnsPanel.add(cmbFromState);
		
		cmbToState = new JComboBox();
		cmbToState.setModel(new DefaultComboBoxModel(
				new String[] { "-Slct-" }));
		cmbToState.setBounds(96, 36, 70, 20);
		trnsPanel.add(cmbToState);
		
		JLabel label_3 = new JLabel("From");
		label_3.setBounds(10, 11, 46, 14);
		trnsPanel.add(label_3);
		
		JLabel label_4 = new JLabel("To");
		label_4.setBounds(96, 11, 46, 14);
		trnsPanel.add(label_4);
		
		btnAdd = new JButton("Add");
		
		btnAdd.setBounds(190, 35, 70, 23);
		trnsPanel.add(btnAdd);
		
		
		JPanel trnsList = new JPanel();
		trnsList.setLayout(null);
		trnsList.setBounds(0, 58, (trnsPanel.getWidth()), (trnsPanel.getHeight()) -70);
		trnsPanel.add(trnsList);
		
		JScrollPane trnsListScroll = new JScrollPane();
		trnsListScroll.setBounds(10, 11, (trnsList.getWidth()) -10, (trnsList.getHeight()) -10);
		trnsList.add(trnsListScroll);
	
		tblTransition = new JTable();
		tblTransition.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"From", "To", "Retain", "Delete"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, Boolean.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblTransition.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblTransition.getColumnModel().getColumn(3).setPreferredWidth(30);
		trnsListScroll.setViewportView(tblTransition);
		
		/// Load and Save/ Load and Save/ Load and Save/ Load and Save/ Load and Save/ Load and Save
		btnSave = new JButton("Save");	
		btnSave.setBounds(10, this.getHeight() - 75, 89, 23);
		contentPane.add(btnSave);
		
		btnLoad = new JButton("Load");		
		btnLoad.setBounds(311, this.getHeight() - 75, 89, 23);
		contentPane.add(btnLoad);
		
		
		chckAbstractionByGrouping = new JCheckBox("grouping");
		chckAbstractionByGrouping.setEnabled(false);
		chckAbstractionByGrouping.setBounds(1097, 54, 91, 23);
		contentPane.add(chckAbstractionByGrouping);
		
		chckbxClear = new JCheckBox("Full Model");
		//chckbxClear.setEnabled(false);
		chckbxClear.setBounds(1105, 10, 100, 23);
		contentPane.add(chckbxClear);
		
		txtFormulaes = new JTextField();
		txtFormulaes.setEnabled(false);
		txtFormulaes.setBounds(481, 55, 337, 20);
		contentPane.add(txtFormulaes);
		txtFormulaes.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Formulae(,)");
		lblNewLabel.setBounds(400, 58, 100, 14);
		contentPane.add(lblNewLabel);
		
		btnFormulae = new JButton("OK");
		btnFormulae.setEnabled(false);
		btnFormulae.setBounds(828, 54, 60, 23);
		contentPane.add(btnFormulae);
		
		btnApplyToFull = new JButton("Apply To Full Model");
		
		
		btnApplyToFull.setEnabled(false);
		btnApplyToFull.setBounds(this.getWidth() - 770, this.getHeight() - 75, 180, 23);
		contentPane.add(btnApplyToFull);
		
		
		btnSatDecProc = new JButton("SAT via Dec. Proc.");
		
		btnSatDecProc.setBounds(this.getWidth() - 435, this.getHeight() - 75, 163, 23);
		contentPane.add(btnSatDecProc);
		
		btnNewSub = new JButton("New");
		
		btnNewSub.setBounds(this.getWidth() - 85, this.getHeight() - 75, 70, 23);
		contentPane.add(btnNewSub);
		
		
		
		btnX = new JButton("x");		
		btnX.setBounds(this.getWidth() - 265, this.getHeight() - 75, 45, 23);
		contentPane.add(btnX);
		
		
		cmbSub = new JComboBox();
		
		cmbSub.setModel(new DefaultComboBoxModel(new String[] {"--Select--"}));
		cmbSub.setBounds(this.getWidth() - 210, this.getHeight() - 75, 116, 20);
		contentPane.add(cmbSub);
		
		rdbtnByFormula = new JRadioButton("by Formula");
		rdbtnByFormula.setBounds(905, 55, 105, 21);
		contentPane.add(rdbtnByFormula);
		
		rdbtnByLabel = new JRadioButton("by Label");
		rdbtnByLabel.setBounds(1006, 54, 95, 23);
		contentPane.add(rdbtnByLabel);
		
		
		
		JLabel lblCtlFormula = new JLabel("Formula:");
		lblCtlFormula.setBounds(406, 10, 70, 14);
		contentPane.add(lblCtlFormula);
		
		lblModelCheckResult = new JLabel("");
		lblModelCheckResult.setBounds(491, 30, 244, 14);
		contentPane.add(lblModelCheckResult);
		
		btnModelRepair = new JButton("repair");
		btnModelRepair.setBounds(1018, 10, 85, 23);
		contentPane.add(btnModelRepair);
		btnModelRepair.disable();
		
		btnCheckModel = new JButton("check");
		btnCheckModel.setBounds(942, 10, 79, 23);
		contentPane.add(btnCheckModel);
				
	
		panel = new JPanel();
		panel.setBounds(410, 84, 770, this.getHeight() - 170);
		contentPane.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{770, 0};
		gbl_panel.rowHeights = new int[]{540, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
	
////////////DiGraph///////////////DiGraph////////////DiGraph////////////DiGraph//////////////DiGraph///////////
	
		myPicture = new ImagePanel("test.png");		
		//myPicture.setLocation((this.getWidth()/3) + 10, statesScroll.getY()+10);
		myPictureScroll= new JScrollPane(myPicture);
		GridBagConstraints gbc_myPictureScroll = new GridBagConstraints();
		gbc_myPictureScroll.insets = new Insets(5, 5, 5, 5);
		gbc_myPictureScroll.weighty = 1.0;
		gbc_myPictureScroll.fill = GridBagConstraints.BOTH;
		gbc_myPictureScroll.gridx = 0;
		gbc_myPictureScroll.gridy = 0;
		panel.add(myPictureScroll, gbc_myPictureScroll);
		
		cmbCtlFormulae = new JComboBox();		
		cmbCtlFormulae.setEditable(true);
		cmbCtlFormulae.setBounds(481, 10, 451, 20);
		contentPane.add(cmbCtlFormulae);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetForm((DefaultComboBoxModel) cmbFromState.getModel(),
						(DefaultComboBoxModel) cmbToState.getModel());
				myPicture.repaintPanel();
			}
		});
		btnClear.setBounds(164, this.getHeight() - 75, 89, 23);
		contentPane.add(btnClear);
		
		chckbxShortCut = new JCheckBox("s.c.");
		chckbxShortCut.setSelected(true);
		chckbxShortCut.setBounds(406, 31, 69, 23);
		contentPane.add(chckbxShortCut);		
		
		
		
		
	}
	
	
	
	
	protected void RemoveStateFromCombos(String stateTobeDeleted)
	{
		DefaultComboBoxModel modelcmbFromState = (DefaultComboBoxModel) cmbFromState.getModel();
		modelcmbFromState.removeElement(stateTobeDeleted);
		DefaultComboBoxModel modelcmbToState = (DefaultComboBoxModel) cmbToState
				.getModel();
		modelcmbToState.removeElement(stateTobeDeleted);
	}

	protected void DeleteStateCorrespondingTrans_frame(String stateTobeDeleted)
	{
		DefaultTableModel defaultModel = (DefaultTableModel) tblTransition.getModel();
		for (int i = 0; i < defaultModel.getRowCount(); i++) {
			String column1 = defaultModel.getValueAt(i, 0).toString().trim();
			String column2 = defaultModel.getValueAt(i, 1).toString().trim();
			if (column1.equals(stateTobeDeleted)) {
				DeleteTransition(column1, column2, getCurrentKripke());
				defaultModel.removeRow(i);
				i -= 1;
				//continue; check if state self transition
			}
			if (column2.equals(stateTobeDeleted)) {
				DeleteTransition(column2, column1, getCurrentKripke());
				defaultModel.removeRow(i);
				i -= 1;
			}
		}
	}
	
	

	
	protected List<Transition> GetTransitionsTobeDeleted(List<FormulaString> frmls)
	{
		List<Transition> deletedTrans = new ArrayList<Transition>();
		for (FormulaString frmlStr : frmls) {
			String EorX = frmlStr.getEorX().trim();
			KripkeState startState = frmlStr.getState();
			KripkeState endState = frmlStr.getEndState();
			if(EorX.equals("E") && startState!= null && endState != null && !frmlStr.getValuation())
			{				
				Collection<Transition> stateTransitions =  startState.getTransitions();
				for (Transition transition : stateTransitions) {
					if((transition.getEndState() == null && endState == null) || ( transition.getEndState() != null && endState != null && transition.getEndState().getName().trim().equals(endState.getName().trim())))
					{
						deletedTrans.add(transition);
						break;
					}
				}
			}
		}
		return deletedTrans;
		
	}

	protected void AddStateAndLabels(String state, String labels,
			boolean isStartState, Kripke kripke) {
		KripkeState s = new KripkeState(kripke, state, isStartState);
		kripke.addState(s);
		String[] lbs = labels.split(",");
		for (String str : lbs) {
			if(!str.trim().equals(""))
			{
				BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
						str.trim()));
				PredicateFormulaValuation predValue = new PredicateFormulaValuation(
						kripke, s, pred, true);
				s.addPredicateFormulaValuation(predValue);
			}
		}
	}
	protected void UpdateStateAndLabels(String state, String labels,
			boolean isStartState, Kripke kripke) {
		kripke.removeState(state);
		AddStateAndLabels(state, labels, isStartState, kripke);
		
	}
	
	protected void UpdateLabels(String state, String labels, Kripke kripke)
	{
		KripkeState s = kripke.getState(state);
		s.resetLabels();
		s.resetValuations();
		String[] lbs = labels.split(",");
		for (String str : lbs) {
			if(!str.trim().equals(""))
			{
				BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
						str.trim()));
				PredicateFormulaValuation predValue = new PredicateFormulaValuation(
						kripke, s, pred, true);
				s.addPredicateFormulaValuation(predValue);
			}
		}
	}
	protected void duplicateState(String state, String labels, Kripke kripke, DefaultTableModel tableModel) throws CloneNotSupportedException
	{
		KripkeState s = kripke.getState(state);
		
		long nameSuffix = Calendar.getInstance().getTimeInMillis();
		String newStateName = s.getName() + "_" + nameSuffix;
		while(kripke.getState(newStateName) != null)
		{
			nameSuffix++;
			newStateName = s.getName() + "_" + nameSuffix;
		}
		AddStateAndLabels(newStateName, labels,
				s.isStart(), kripke);
	
		tableModel.addRow(new Object[] {newStateName, labels, s.isStart(), false,false});
		DefaultComboBoxModel cmbModel = (DefaultComboBoxModel) cmbFromState.getModel();
		cmbModel.addElement(newStateName);
		cmbModel = (DefaultComboBoxModel) cmbToState.getModel();
		cmbModel.addElement(newStateName);
	}
	protected boolean UpdateState(String oldState, String newState, Kripke kripke)
	{
		if(kripke.getState(newState) == null)
		{
			KripkeState s = kripke.getState(oldState);
			s.changeName(newState);
			DefaultComboBoxModel cmbModel = (DefaultComboBoxModel) cmbFromState.getModel();
			int size = cmbModel.getSize();
			for(int i=0;i< cmbModel.getSize();i++)
			{
				if(cmbModel.getElementAt(i).toString().trim().equals(oldState.trim()))
				{
					cmbModel.removeElementAt(i);
					break;
				}
			}
			cmbModel.addElement(newState);
			cmbModel = (DefaultComboBoxModel) cmbToState.getModel();
			size = cmbModel.getSize();
			for(int i=0;i< cmbModel.getSize();i++)
			{
				if(cmbModel.getElementAt(i).toString().trim().equals(oldState.trim()))
				{
					cmbModel.removeElementAt(i);
					break;
				}
			}
			cmbModel.addElement(newState);
			//update transitions table
			DefaultTableModel defaultModel = (DefaultTableModel) tblTransition.getModel();
			int rowNb = defaultModel.getRowCount();
			boolean isChanged = false;
			for(int i=0;i<rowNb;i++)
			{
				String startState = defaultModel.getValueAt(i, 0) == null ? "" : (String)defaultModel.getValueAt(i, 0);
				String endState = defaultModel.getValueAt(i, 1) == null ? "" : (String)defaultModel.getValueAt(i, 1);
				if(startState.trim().equals(oldState))
				{
					defaultModel.setValueAt(newState, i, 0);
				}
				if(endState.trim().equals(oldState))
				{
					defaultModel.setValueAt(newState, i, 1);
				}
			}
			return true;
		}
		else
			JOptionPane
			.showMessageDialog(
					null,
					"State already exists!");
		return false;
	}
	protected void DeleteState(String state, Kripke kripke) {
		kripke.msakrRemoveState(state);

	}
	protected void AddTransition(String transName, String startState,
			String endState, Kripke kripke) {
		KripkeState fromState = kripke.getState(startState.trim());
		KripkeState toState = kripke.getState(endState.trim());
		Transition t = new Transition(fromState, toState, transName, transName);
		fromState.addOutgoingTransition(t);
	}
	protected void DeleteTransition(String startState, String toState, Kripke kripke) {
		KripkeState state = kripke.getState(startState);
		Transition transition = state.getTransitionByStateName(toState);
		kripke.removeTransition(state, transition);
	}
	

	
	

	protected String SaveFileText(Kripke kripke, DefaultTableModel tbtSts,
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
			String column2 = tblTrans.getValueAt(i, 0).toString().trim();
			String column3 = tblTrans.getValueAt(i, 1).toString().trim();
			String column1 = KripkeState.getTransition(kripke, column2, column3).getName();
			builder.append(column1 + ":" + column2 + ":" + column3 + ";");
		}

		return builder.toString();
	}
	protected void CreateSavedFile(Kripke kripke, DefaultTableModel tbtSts,
			DefaultTableModel tblTrans) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("kripke.txt"));
			int status = fileChooser.showSaveDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				currentFile = fileChooser.getSelectedFile().getPath();
				File target = fileChooser.getSelectedFile();

				if (target.exists())
					target.delete();

				target.createNewFile();

				FileWriter fw = new FileWriter(target);
				PrintWriter pw = new PrintWriter(fw);
				String stmts = SaveFileText(kripke, tbtSts, tblTrans);
				pw.println(stmts);
				pw.flush();
				fw.flush();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	protected void LoadSatSolution(DefaultTableModel tblCnf, List<FormulaString> list, String[] solution)
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
	
	protected void SaveCtlFormulae()
	{
		try {
			String ctlToSave = cmbCtlFormulae.getSelectedItem().toString().trim();
			DefaultComboBoxModel model = (DefaultComboBoxModel) cmbCtlFormulae.getModel();
			if(model.getIndexOf(cmbCtlFormulae.getSelectedItem()) <= 0 )
			{
				final Charset ENCODING = StandardCharsets.UTF_8;
				List<String> allText = Files.readAllLines(Paths.get(currentFile), ENCODING);
				if (allText.size() > 0) 
				{				
					String text = allText.get(0);
					String[] AllData = text.split("\\*\\*\\*");
					if(AllData.length > 2)
					{
						text +="#" + ctlToSave;
					}
					else
					{
						text +="***" + ctlToSave;
					}
					FileWriter fw = new FileWriter(currentFile);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(text);
					pw.flush();
					fw.flush();
				}
				JOptionPane.showMessageDialog(null, "Formula was saved successfully!");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Formula already exists!");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	protected String LoadFileText(DefaultTableModel tbtSts,
			DefaultTableModel tblTrans,
			DefaultComboBoxModel modelcmbFromState,
			DefaultComboBoxModel modelcmbToState) {
		String startNameResult = "";
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
					
					currentFile = fileChooser.getSelectedFile().getPath();
					String text = allText.get(0);
					String[] AllData = text.split("\\*\\*\\*");
					String[] states = AllData[0].split(";");
					for (String state : states) {
						String[] cells = state.split(":");
						tbtSts.insertRow(tblStates.getRowCount(), new Object[] {
							cells[0], cells[1], Boolean.valueOf(cells[2]) });
						modelcmbFromState.addElement(cells[0]);
						modelcmbToState.addElement(cells[0]);
						AddStateAndLabels(cells[0], cells[1], Boolean.valueOf(cells[2]), getCurrentKripke());
						if(Boolean.valueOf(cells[2])) {
							startNameResult = cells[0];
						}
					}
					String[] trans = AllData[1].split(";");
					for (String tr : trans) {
						String[] cells = tr.split(":");
						tblTrans.insertRow(tblTrans.getRowCount(),
								new Object[] { cells[1], cells[2] });
						AddTransition(cells[0], cells[1], cells[2], getCurrentKripke());
					}
					if(AllData.length > 2)
					{
						String[] ctls = AllData[2].split("#");
						DefaultComboBoxModel ctlCombo = (DefaultComboBoxModel)cmbCtlFormulae.getModel();
						ctlCombo.removeAllElements();
						ctlCombo.addElement("");
						for (String ctl : ctls) {
							ctlCombo.addElement(ctl);
						}
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return startNameResult;
	}

	
	protected void emptyTable(DefaultTableModel myTableModel)
	{
		if (myTableModel.getRowCount() > 0) {
		    for (int i = myTableModel.getRowCount() - 1; i > -1; i--) {
		        myTableModel.removeRow(i);
		    }
		}
	}
	protected void emptyCombo(DefaultComboBoxModel myComboModel)
	{
		if (myComboModel.getSize() > 0) {
		    for (int i = myComboModel.getSize() - 1; i > -1; i--) {
		    	myComboModel.removeElementAt(i);
		    }
		    myComboModel.addElement("--Select--");
		}
	}
	protected Kripke getCurrentKripke()
	{
		if(chckAbstractionByGrouping.isSelected())
		{
			if(rdbtnByLabel.isSelected())
				return kripkeByLabelGroupingModel;
			else if(rdbtnByFormula.isSelected())
				return kripkeByFormulaGroupingModel;
			else
				return kripkeModel;
		}
		else
		{
			if(cmbSub.getSelectedIndex() > 0)
			{
				return subStructures.get(cmbSub.getSelectedItem());
			}
			else if(rdbtnByLabel.isSelected())
				return kripkeLabelModel;
			else if(rdbtnByFormula.isSelected())
				return kripkeFormulaModel;
			else
				return kripkeModel;
		}
	}
	protected void resetForm(DefaultComboBoxModel modelcmbFromState,
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
		transvariablecount = 0;
		modelWasChecked = false;
		modelWasRepaired = false;
		kripkeLabelModel = null;
		kripkeFormulaModel = null;
		kripkeByLabelGroupingModel = null;
		kripkeByFormulaGroupingModel = null;
		virtualTransitionsToBeDeleted = null;
		transitionsToRetain = new ArrayList<Transition>();
		statesToRetain = new ArrayList<KripkeState>();
		currentFile = "";
	}
}
