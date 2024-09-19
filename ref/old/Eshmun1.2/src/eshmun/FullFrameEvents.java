package eshmun;


import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import eshmun.DecisionProcedure.KripkeSubStructParent;
import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.ImpliesOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.*;
import eshmun.lts.ReducedKripke.*;
import eshmun.modelchecker.ModelChecker;

import eshmun.modelrepairer.ModelRepairer;
import eshmun.parser.ANTLRParser;

public class FullFrameEvents extends FullFrame {
	
	protected FullFrameEvents currentFrame;
	boolean isEventOn = true;

	public FullFrameEvents() {
		super();
		currentFrame = this;
		transitionsToRetain = new ArrayList<Transition>();
		statesToRetain = new ArrayList<KripkeState>();
		// Saved Kripke mode //Saved Kripke mode //Saved Kripke mode		
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm((DefaultComboBoxModel) cmbFromState.getModel(),
						(DefaultComboBoxModel) cmbToState.getModel());
				myPicture.repaintPanel();
				String startName = LoadFileText((DefaultTableModel) tblStates.getModel(),
						(DefaultTableModel) tblTransition.getModel(),
						(DefaultComboBoxModel) cmbFromState.getModel(),
						(DefaultComboBoxModel) cmbToState.getModel());
				DoDrawDiagram(kripkeModel, null);
				
				for (KripkeState kr : getCurrentKripke().getStartStatesList()) {
					kr.setStart(false);
				}
				
				if(startName.trim().equals("")) {
					return;
				} else {
					getCurrentKripke().getState(startName).setStart(true);
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateSavedFile(getCurrentKripke(),(DefaultTableModel) tblStates.getModel(),
						(DefaultTableModel) tblTransition.getModel());
			}
		});
		
		//*******************************************************************I******************************************
		// Tables Actions Tables Actions Tables Actions Tables Actions Tables Actions Tables Actions Tables Actions Tables Actions
		Action transTableAction = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        TableCellListener tcl = (TableCellListener)e.getSource();
		        DefaultTableModel defaultModel = (DefaultTableModel) tblTransition
						.getModel();
		        if(tcl.getColumn() == 2)
		        {
		        	KripkeState state = getCurrentKripke().getState(defaultModel.getValueAt(tcl.getRow(), 1).toString());
		    		Transition transition = KripkeState.getTransition(getCurrentKripke(), defaultModel.getValueAt(tcl.getRow(), 0).toString(),defaultModel.getValueAt(tcl.getRow(), 1).toString());
		        	boolean toretain = (boolean)defaultModel.getValueAt(tcl.getRow(), 2);
		        	if(toretain)
		        		transitionsToRetain.add(transition);
		        	else		        		
		        		transitionsToRetain.remove(transition);
		        }
		        if(tcl.getColumn() == 3)
		        {
		        	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this transition?", "Delete State", JOptionPane.YES_NO_OPTION);
	                if (reply == JOptionPane.YES_OPTION)
	                {
						if (defaultModel.getRowCount() > 0) {
							String stateFrom = defaultModel.getValueAt(tcl.getRow(), 0).toString();
							String stateTo = defaultModel.getValueAt(tcl.getRow(), 1).toString();
							DeleteTransition(stateFrom, stateTo, getCurrentKripke());
							defaultModel.removeRow(tcl.getRow());
							DoDrawDiagram(getCurrentKripke(), null);
						}					
	                }
		        }
		        btnApplyToFull.setEnabled(false);
		    }
		};

		TableCellListener transTcl = new TableCellListener(tblTransition, transTableAction);
		
		Action stateTableAction = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        TableCellListener tcl = (TableCellListener)e.getSource();	
		        DefaultTableModel defaultModel = (DefaultTableModel) tblStates.getModel();
		        String stateTobechanged = defaultModel.getValueAt(tcl.getRow(), 0).toString().trim();
		        if(tcl.getColumn() == 3)
		        {
		        	boolean toretain = (boolean)defaultModel.getValueAt(tcl.getRow(), 4);
		        	if(toretain)
		        		statesToRetain.add(getCurrentKripke().getState(stateTobechanged));
		        	else		        		
		        		statesToRetain.remove(getCurrentKripke().getState(stateTobechanged));
		        }
		        //if start flag changed
		        if(tcl.getColumn() == 2)
		        {
		        	KripkeState s = getCurrentKripke().getState(stateTobechanged);
		        	s.setStart((boolean)(tcl.getNewValue()));
		        }
		        //if labels changed
		        if(tcl.getColumn() == 1)
		        {
		        	Pattern labelPattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*(,(\\s)*[a-zA-Z][a-zA-Z0-9]*)*");
		        	String label = tcl.getNewValue().toString().trim();
		        	if (label.length() == 0	|| labelPattern.matcher(label).matches()) {
		        		UpdateLabels(stateTobechanged, label, getCurrentKripke());
		        	}
		        	else
		        	{
		        		JOptionPane.showMessageDialog(null, "Labels name must start with a letter and can contain only letters or digits.\nLabels must be comma seperated.");
		        	}
		        }
		        if(tcl.getColumn() == 0)
		        {
		        	Pattern statePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		        	String state = tcl.getNewValue().toString().trim();
		        	if (state.length() != 0	|| statePattern.matcher(state).matches()) {
		        		boolean success = UpdateState(tcl.getOldValue().toString().trim(), tcl.getNewValue().toString().trim(), getCurrentKripke());
		        		if(!success)
		        		{
		        			defaultModel.setValueAt(tcl.getOldValue(), tcl.getRow(), 0);
		        		}
		        	}
		        	else
		        	{
		        		JOptionPane.showMessageDialog(null, "State name must start with a letter and can contain only letters or digits!");
		        	}
		        }
		        btnApplyToFull.setEnabled(false);
		        DoDrawDiagram(getCurrentKripke(), null);
		        
		    }
		};

		TableCellListener statesTcl = new TableCellListener(tblStates, stateTableAction);
		
		btnStateDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel defaultModel = (DefaultTableModel) tblStates.getModel();
				int rowNb = defaultModel.getRowCount();
				boolean isChanged = false;
				for(int i=0;i<rowNb;i++)
				{
					boolean isToDelete = defaultModel.getValueAt(i, 4) == null ? false : (boolean)defaultModel.getValueAt(i, 4);
					if(isToDelete)
					{
						String stateTobeDeleted = defaultModel.getValueAt(i, 0).toString().trim();
						DeleteStateCorrespondingTrans_frame(stateTobeDeleted);
						defaultModel.removeRow(i);
			        	//delete from kripke
			    		DeleteState(stateTobeDeleted, getCurrentKripke());
			    		// delete from combo boxes	
			    		RemoveStateFromCombos(stateTobeDeleted);
			    		isChanged = true;
			    		rowNb--;
			    		i--;
					}
				}
				if(isChanged)
					DoDrawDiagram(getCurrentKripke(), null);
			}
		});
		
		btnStateDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel defaultModel = (DefaultTableModel) tblStates.getModel();
				int rowNb = defaultModel.getRowCount();
				boolean isChanged = false;
				for(int i=0;i<rowNb;i++)
				{
					boolean isToDuplicate = defaultModel.getValueAt(i, 4) == null ? false : (boolean)defaultModel.getValueAt(i, 4);
					if(isToDuplicate)
					{
						String stateTobeDuplicated = defaultModel.getValueAt(i, 0).toString().trim();
						String labels = defaultModel.getValueAt(i, 1).toString().trim();
						try{
						duplicateState(stateTobeDuplicated, labels, getCurrentKripke(), defaultModel);
						defaultModel.setValueAt(false,i, 4);
						}
						catch(Exception ex){}
			    		isChanged = true;
					}
				}
				if(isChanged)
					DoDrawDiagram(getCurrentKripke(), null);
				
			}
		});
		
		//////////state add button////////state add button////////state add button////////state add button////////state add button
			btnAddState.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addState();
				}
			});
			
	////////transition add button////////transition add button////////transition add button////////transition add button////////transition add button
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addTransition();
				}
			});
			
			 
			//*******************************************************************I******************************************		
			// Repairing Repairing Repairing Repairing Repairing Repairing Repairing Repairing
			btnModelRepair.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modelRepair();
				}
				
			});			
			
			btnCheckModel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					modelCheck();
				}
			});
			//*******************************************************************I******************************************
			
			//Abstraction Abstraction Abstraction Abstraction Abstraction Abstraction Abstraction 
			
			
			
			chckbxClear.addItemListener(new ItemListener() {

	            @Override
	            public void itemStateChanged(ItemEvent e) {
	               if(e.getStateChange() == ItemEvent.SELECTED)
	               {
	            	   isEventOn = false;
	            	   rdbtnByLabel.setSelected(false);
	            	   rdbtnByFormula.setSelected(false);
	            	   btnApplyToFull.setEnabled(false);
	            	   isEventOn = false;
	            	   	chckAbstractionByGrouping.setSelected(false);
	            	   	chckAbstractionByGrouping.setEnabled(false);
	            	   	enableFrmlAbstraction(false);
	            	   	btnModelRepair.setEnabled(true);
						btnNewSub.setEnabled(true);
						btnCheckModel.setEnabled(true);
						cmbSub.setSelectedIndex(0);
	            	   	lblModelCheckResult.setText("");
	            	   	TablesDataBind(kripkeModel.getStatesList());
	            	   	chckbxClear.setSelected(false);
	            	   	isEventOn = true;
		   				DoDrawDiagram(kripkeModel, null);
	               }
	            }
	        });
			
			
			
			chckAbstractionByGrouping.addItemListener(new ItemListener() {

	            @Override
	            public void itemStateChanged(ItemEvent e) 
	            {
	            	if(isEventOn)
	            	{
		            	lblModelCheckResult.setText("");
		               if(e.getStateChange() == ItemEvent.SELECTED)
		               {
		            	   if(rdbtnByLabel.isSelected())
		            	   {
		            		   AbstractionByLabelGrouping();
		            	   }
		            	   else if(rdbtnByFormula.isSelected())
		            	   {
		            		   AbstractionByFormulaGrouping();
		            	   }
		               }
		               else
		               {
		            	   if(rdbtnByLabel.isSelected())
		            	   {
		            		   AbstractionByLabel();
		            	   }
		            	   else if(rdbtnByFormula.isSelected())
		            	   {
		            		   AbstractionByFormula();
		            	   }
		               }
	            	}
	            }
	        });
			
			rdbtnByLabel.addItemListener(new ItemListener() {

	            @Override
	            public void itemStateChanged(ItemEvent e) 
	            {
	            	if(isEventOn)
	            	{
	            		lblModelCheckResult.setText("");
		               if(e.getStateChange() == ItemEvent.SELECTED)
		               {
		            	   btnApplyToFull.setEnabled(false);
		            	   AbstractionByLabel();
		               }
	            	}
	            }
	        });
			
			rdbtnByFormula.addItemListener(new ItemListener() {
	            @Override
	            public void itemStateChanged(ItemEvent e) 
	            {
	            	if(isEventOn)
	            	{
		            	lblModelCheckResult.setText("");
		               if(e.getStateChange() == ItemEvent.SELECTED)
		               {
		            	   btnApplyToFull.setEnabled(false);
		            	   AbstractionByFormula();
		               }
	            	}
	            }
	        });
			
			
			
			btnFormulae.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(txtFormulaes.getText().trim() != "")
					{
						btnApplyToFull.setEnabled(false);
						List<PredicateFormula> repairFrmlPropFrmls = new ArrayList<PredicateFormula>();
						try
						{
						String[] formulas = txtFormulaes.getText().split(",");
						for (String frml : formulas) {
							if(!checkIfPropositional(frml))
							{
								JOptionPane.showMessageDialog(null,
										"Enter only correct propositional formulae!");
								return;
							}
						}
						ANTLRParser parser = new ANTLRParser();
						for (String frml : formulas) {
							repairFrmlPropFrmls.add(parser.parse(frml));
						}
						}
						catch(Exception ex)
						{
							JOptionPane.showMessageDialog(null,
									"Enter only correct propositional formulae!");
							return;
						}			
						
		            	//modelWasChecked = false;
		            	modelWasRepaired = false;
		            	
	            		List<PredicateFormula> frmlForAbstraction = new ArrayList<PredicateFormula>();
						for (PredicateFormula pf : repairFrmlPropFrmls) {
							if(!(pf instanceof BooleanPredicate) && !( (pf instanceof NotOperator) && (((NotOperator)pf).getChild() instanceof BooleanPredicate) ))
							{
								if(pf instanceof ImpliesOperator)
								{
									ImpliesOperator impOp = (ImpliesOperator)pf;									
									frmlForAbstraction.add(new OrOperator(new NotOperator(impOp.getLeftChild()), impOp.getRightChild()));
								}
								else
								{
									frmlForAbstraction.add(pf);
								}
							}
						}
						if(frmlForAbstraction.size() > 0)
						{
							if(!chckAbstractionByGrouping.isSelected())
			            	{
								KripkeByFormula krpByFrml = new KripkeByFormula(kripkeModel, frmlForAbstraction);
								kripkeFormulaModel = krpByFrml.Reduce();
								TablesDataBind(kripkeFormulaModel.getStatesList());
								DoDrawDiagram(kripkeFormulaModel, null);
			            	}
							else
			            	{
			            		KripkyByFrmllWithGrouping krpByFrmlGrp = new KripkyByFrmllWithGrouping(kripkeModel, frmlForAbstraction);
			        			kripkeByFormulaGroupingModel = krpByFrmlGrp.Reduce();
			        			TablesDataBind(kripkeByFormulaGroupingModel.getStatesList());
				            	DoDrawDiagram(kripkeByFormulaGroupingModel, null);
			            	}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "there are No Propositional Formulae", "No Propositional Formula", NORMAL);
						}	            	
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								"Enter formulae comma seperated!");
					}
				}
			});
			//*******************************************************************I******************************************
			
			btnApplyToFull.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					applyToInitialModel();
					
					
				}
			});
			
			saveImageButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SaveCtlFormulae();
				}				
				});
			
			cmbCtlFormulae.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getCtlFormula();
				}
			});
			
			btnNewSub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					SubStructureFrame myFrame = new SubStructureFrame(currentFrame);
					myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					myFrame.setVisible(true); 
					chckbxClear.setSelected(false);
				}
			});
			
			cmbSub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(cmbSub.getSelectedIndex() > 0)
					{
						Kripke subStructure = subStructures.get(cmbSub.getSelectedItem());
						TablesDataBind(subStructure.getStatesList());
						DoDrawDiagram(subStructure, null);
						chckbxClear.setSelected(false);
					}
				}
			});
			
			btnX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(cmbSub.getSelectedIndex() > 1)
					{
						String selectedName = cmbSub.getSelectedItem().toString();
						int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete \"" + selectedName + "\"  substructure ?", "Delete Substructure", JOptionPane.YES_NO_OPTION);
		                if (reply == JOptionPane.YES_OPTION)
		                {	    
		                	KripkeSubStructParent parent = (KripkeSubStructParent) subStructures.get("FullModel");
		                	parent.DeleteSubstructure(parent.getSubStructures().get(selectedName));
		                	subStructures.remove(cmbSub.getSelectedItem());
		                	cmbSub.removeItemAt(cmbSub.getSelectedIndex());		                	
		                	cmbSub.setSelectedIndex(1);		                	
		                	DoDrawDiagram(parent, null);
		                }
					}
				}
			});
			
			btnSatDecProc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					DecisionProcFrame myFrame = new DecisionProcFrame();
					myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					myFrame.setVisible(true); 
				}
			});
	}
	
	private void addState()
	{
		 btnApplyToFull.setEnabled(false);
			String state = txtState.getText();
			String label = txtLabel.getText();
			Pattern statePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
			Pattern labelPattern = Pattern
					.compile("[a-zA-Z][a-zA-Z0-9]*(,(\\s)*[a-zA-Z][a-zA-Z0-9]*)*");
			if (state != null && state.trim() != "" && state.length() > 0) {					
				if (statePattern.matcher(state).matches()) {
					if (label.length() == 0
							|| labelPattern.matcher(label).matches()) {
						if(getCurrentKripke().getState(state) == null)
						{
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
									chkIsStartState.isSelected(), getCurrentKripke());
							txtState.setText("");
							txtLabel.setText("");
							chkIsStartState.setSelected(false);
							DoDrawDiagram(getCurrentKripke(), null);
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
	}
	
	private void addTransition()
	{
		 btnApplyToFull.setEnabled(false);
		 btnAdd.setEnabled(false);
		String trans = "t_" + Calendar.getInstance().getTimeInMillis();
		if (cmbFromState.getSelectedIndex() > 0
				&& cmbToState.getSelectedIndex() > 0) {
			
			DefaultTableModel defaultModel = (DefaultTableModel) tblTransition
					.getModel();
			for (int i = 0; i < defaultModel.getRowCount() - 1; i++) {				
				Object transName = defaultModel.getValueAt(i, 0);
				if(transName.equals(trans))
				{
					JOptionPane.showMessageDialog(null,
							"Task Name already exists!");
					return;
				}
				if(
						cmbFromState.getSelectedItem().toString().equals(defaultModel.getValueAt(i, 0)) &&  
						cmbToState.getSelectedItem().toString().equals(defaultModel.getValueAt(i, 1))
					)
				{
					JOptionPane.showMessageDialog(null,
							"Transition already exists!");
					return;
				}
			}
			defaultModel.insertRow(tblTransition.getRowCount(),
					new Object[] {
				cmbFromState.getSelectedItem().toString()
				.trim(),
				cmbToState.getSelectedItem().toString()
				.trim() });
			AddTransition(trans, cmbFromState.getSelectedItem()
					.toString().trim(), cmbToState.getSelectedItem()
					.toString().trim(), getCurrentKripke());
			cmbFromState.setSelectedIndex(0);
			cmbToState.setSelectedIndex(0);
			DoDrawDiagram(getCurrentKripke(), null);
		} else
			JOptionPane.showMessageDialog(null,
					"All fields are required!");
		btnAdd.setEnabled(true);
	}
	
	private void modelCheck()
	{
		if(cmbCtlFormulae.getSelectedItem().toString().trim() != "")
		{
			try
			{
				modelWasChecked = true;
				lblModelCheckResult.setText("");					
				boolean trueModel = FormulaModelCheck(getCtlFormula(), getCurrentKripke());
				if(trueModel)
					lblModelCheckResult.setText("The model is correct!");
				else
				{
					lblModelCheckResult.setText("The model needs to be repaired!");
					isEventOn = false;
					btnModelRepair.setEnabled(true);
					rdbtnByLabel.setEnabled(true);
					rdbtnByFormula.setEnabled(true);
					 chckbxClear.setEnabled(true);
					 isEventOn = true;
				}
			}
			catch(Exception ex)
			{
				lblModelCheckResult.setText("Make sure you enter a correct formula!");
				modelWasChecked = false;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					"Enter a CTL formula!");
		}
	}
	
	private void modelRepair()
	{
		//GetAllRepairs();
		try
		{
			Kripke kripke = getCurrentKripke();					
			if(modelWasChecked)
			{
				ModelRepairer repairer = new ModelRepairer(kripke, getCtlFormula(), transitionsToRetain, statesToRetain);
				
				long start = System.nanoTime();
				System.out.println("--------------------------------");
				boolean isRepaired = repairer.repair();
				
				long delta = System.nanoTime();
				
				System.out.println("Time taken : " + Math.abs(start - delta));
				System.out.println("--------------------------------");

				if(isRepaired)
				{
					List<Transition> deletedTrans = repairer.getTransitionsToDelete();
					virtualTransitionsToBeDeleted = deletedTrans;
					modelWasRepaired = true;
					DoDrawDiagram(kripke, deletedTrans);
					lblModelCheckResult.setText("The model is repaired!");
					if(rdbtnByFormula.isSelected() || rdbtnByLabel.isSelected() )
						btnApplyToFull.setEnabled(true);
				}
				else
					lblModelCheckResult.setText("The model cannot be repaired!");
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,
					"Error occurred: make sure you enter a correct formula!");
		}
	}
	
	private void GetAllRepairs()
	{
		try
		{
			Kripke kripke = getCurrentKripke();					
			if(modelWasChecked)
			{
				
				ModelRepairer repairer = new ModelRepairer(kripke, getCtlFormula(), transitionsToRetain, statesToRetain);
				//boolean isRepaired = repairer.repair();
				//if(isRepaired)
				{
					List<List<Transition>> deletedTrans = repairer.getAllPossibleRepairs();
					for (List<Transition> list : deletedTrans) {
						virtualTransitionsToBeDeleted = list;
						modelWasRepaired = true;
						DoDrawDiagram(kripke, list);
					}
					
					lblModelCheckResult.setText("The model is repaired!");
					if(rdbtnByFormula.isSelected() || rdbtnByLabel.isSelected() )
						btnApplyToFull.setEnabled(true);
				}
				//else
					//lblModelCheckResult.setText("The model cannot be repaired!");
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,
					"Error occurred: make sure you enter a correct formula!");
		}
	}
	
	private void applyToInitialModel()
	{
		isEventOn = false;
		btnApplyToFull.setEnabled(false);
		Kripke reducedKripke =getCurrentKripke();
		rdbtnByLabel.setSelected(false);
		chckAbstractionByGrouping.setSelected(false);
		rdbtnByFormula.setSelected(false);		
		rdbtnByLabel.setEnabled(false);
		chckAbstractionByGrouping.setEnabled(false);
		rdbtnByFormula.setEnabled(false);
		btnModelRepair.setEnabled(false);
		btnCheckModel.setEnabled(false);
		btnNewSub.setEnabled(false);
		isEventOn = true;
		List<Transition> originalModelTransitions = AbstractReducedKripke.GetInitialModelTransitions(reducedKripke, virtualTransitionsToBeDeleted);				
		Kripke toCheck = kripkeModel;
		try {
			kripkeModel = kripkeModel.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Transition transition : originalModelTransitions) {
			toCheck.removeTransition(transition.getStartState(), transition);
		}
		DoDrawDiagram(kripkeModel, originalModelTransitions);
		boolean isCorrect = FormulaModelCheck(getCtlFormula(), toCheck);
		if(isCorrect)
			JOptionPane.showMessageDialog(null, "The model is correct!");
		else
			JOptionPane.showMessageDialog(null, "The model is not correct!");
	}
	
	private PredicateFormula getCtlFormula()
	{		
		if(cmbCtlFormulae.getSelectedItem() != null && cmbCtlFormulae.getSelectedItem().toString().trim() != "" )
		{
			String ctlFormulaText = cmbCtlFormulae.getSelectedItem().toString().trim();
			if(chckbxShortCut.isSelected())
				ctlFormulaText = CommonFunc.FormatInput(ctlFormulaText);
			ANTLRParser parser = new ANTLRParser();
			PredicateFormula pf;
			try 
			{
				pf = parser.parse(ctlFormulaText);			
				ctlFormula = pf;
				return ctlFormula;
			} 
			catch (Exception e) 
			{				
				e.printStackTrace();
			}
		}
		return null;
	}
	
 	public void TablesDataBind(List<KripkeState> states)
	{
		emptyTable((DefaultTableModel) tblStates.getModel());
		emptyTable((DefaultTableModel) tblTransition.getModel());
		emptyCombo((DefaultComboBoxModel) cmbFromState.getModel());
		emptyCombo((DefaultComboBoxModel) cmbToState.getModel());
		DefaultTableModel stateModel = (DefaultTableModel) tblStates
				.getModel();
		DefaultComboBoxModel modelcmbFromState = (DefaultComboBoxModel) cmbFromState
				.getModel();
		DefaultComboBoxModel modelcmbToState = (DefaultComboBoxModel) cmbToState
				.getModel();
		DefaultTableModel transitionModel = (DefaultTableModel) tblTransition
				.getModel();
		for (KripkeState state : states) {
			stateModel.insertRow(
					tblStates.getRowCount(),
					new Object[] { state.getName(),
						toLabelsString(state.getLabels()),
						state.isStart() });
			modelcmbToState.addElement(state.getName());
			modelcmbFromState.addElement(state.getName());
			Collection<Transition> stateTransitions = state.getTransitions();
			for (Transition transition : stateTransitions) {
				transitionModel.insertRow(tblTransition.getRowCount(),
						new Object[] {
					state.getName(),
					transition.getEndState().getName() });
			}			
		}		
	}
	
	private String toLabelsString(List<BooleanPredicate> labels)
	{
		if(labels != null && labels.size() > 0)
		{
			StringBuilder builder = new StringBuilder();
			for (BooleanPredicate label : labels) {
				builder.append(label.toString()+ ",");
			}
			String toRet = builder.toString();
			return toRet.substring(0, toRet.lastIndexOf(",")).trim();
		}
		return "";
	}
	
	private void AbstractionByLabel()
	{
		chckAbstractionByGrouping.setEnabled(true);
 	   	chckAbstractionByGrouping.setSelected(false);
 	   	chckbxClear.setSelected(false);
 	   	enableFrmlAbstraction(false);
 	   	rdbtnByFormula.setSelected(false);
 	   	if(getCtlFormula() != null)
 	   	{
	 	   	List<BooleanPredicate> ctlAllPred = ctlFormula.getBooleanPredicates();
				List<BooleanPredicate> ctlPred = new ArrayList<BooleanPredicate>();
				for (BooleanPredicate booleanPredicate : ctlAllPred) {
					if(!booleanPredicate.toString().equals("false") && !booleanPredicate.toString().equals("true"))
					{
						ctlPred.add(booleanPredicate);
					}
				}
				KripkeByLabel krp = new KripkeByLabel(kripkeModel, ctlPred);
				kripkeLabelModel = krp.Reduce();//kripkeLabelModel = kripkeModel.GroupAdjacentStates(ctlPred);//kripkeModel.getAbstractedModelByLabels(ctlPred);
			//modelWasChecked = false;
			modelWasRepaired = false;
			TablesDataBind(kripkeLabelModel.getStatesList());
			DoDrawDiagram(kripkeLabelModel, null);
 	   	}
 	   	else
 	   	{
 	   		JOptionPane.showMessageDialog(null, "Please select a formula first!", "Incomplete Entry", NORMAL);
 	   	}
	}

	private void AbstractionByFormula()
	{
		if(getCtlFormula() != null)
 	   	{
			isEventOn = false;
		   chckAbstractionByGrouping.setEnabled(true);
		   enableFrmlAbstraction(true);
		   rdbtnByLabel.setSelected(false);
		   chckAbstractionByGrouping.setSelected(false);
		   chckbxClear.setSelected(false);  
		   isEventOn = true;
		   List<PredicateFormula> repairFrmlPropFrmls = PredicateFormula.GetAbstractionSubs(ctlFormula, false);
		   List<PredicateFormula> frmlForAbstraction = new ArrayList<PredicateFormula>();
		   for (PredicateFormula pf : repairFrmlPropFrmls) {
			   if(!( (pf instanceof NotOperator) && (((NotOperator)pf).getChild() instanceof BooleanPredicate))) {
				   frmlForAbstraction.add(pf);
			   }
		   }
		   if(frmlForAbstraction.size() > 0)
		   {
			   KripkeByFormula krpByFrml = new KripkeByFormula(kripkeModel, frmlForAbstraction);
			   kripkeFormulaModel = krpByFrml.Reduce();//kripkeFormulaModel = kripkeModel.getAbstractedModelByFormulae(repairFrmlPropFrmls);
			   TablesDataBind(kripkeFormulaModel.getStatesList());
			   DoDrawDiagram(kripkeFormulaModel, null);
		   }
		   else
		   {
			   JOptionPane.showMessageDialog(null, "there are No Propositional Formulae", "No Propositional Formula", NORMAL);
		   }
		   //modelWasChecked = false;
		   modelWasRepaired = false;
		   
 	   	}
	   	else
	   	{
	   		JOptionPane.showMessageDialog(null, "Please select a formula first!", "Incomplete Entry", NORMAL);
	   	}
	}
	
	private void AbstractionByLabelGrouping()
	{
		if(getCtlFormula() != null)
 	   	{
			List<BooleanPredicate> ctlAllPred = ctlFormula.getBooleanPredicates();
			List<BooleanPredicate> ctlPred = new ArrayList<BooleanPredicate>();
			for (BooleanPredicate booleanPredicate : ctlAllPred) 
			{
				if(!booleanPredicate.toString().equals("false") && !booleanPredicate.toString().equals("true"))
				{
					ctlPred.add(booleanPredicate);
				}
			}
			KripkeBylblWithGrouping krpGroup = new KripkeBylblWithGrouping(kripkeModel, ctlPred);
			kripkeByLabelGroupingModel = krpGroup.Reduce();//kripkeModel.getAbstractedModelByLabelsGrouping(ctlPred);
			TablesDataBind(kripkeByLabelGroupingModel.getStatesList());
			modelWasRepaired = false;
			DoDrawDiagram(kripkeByLabelGroupingModel, null);
 	   	}
	   	else
	   	{
	   		JOptionPane.showMessageDialog(null, "Please select a formula first!", "Incomplete Entry", NORMAL);
	   	}
	}
	
	private void AbstractionByFormulaGrouping()
	{
		if(getCtlFormula() != null)
 	   	{
			List<PredicateFormula> repairFrmlPropFrmls = PredicateFormula.GetAbstractionSubs(ctlFormula, false);
			List<PredicateFormula> frmlForAbstraction = new ArrayList<PredicateFormula>();
			for (PredicateFormula pf : repairFrmlPropFrmls) {
				   if(!(pf instanceof BooleanPredicate) && !( (pf instanceof NotOperator) && (((NotOperator)pf).getChild() instanceof BooleanPredicate) ))
				   {
					   frmlForAbstraction.add(pf);
				   }
			   }
			   if(frmlForAbstraction.size() > 0)
			   {
					String currentFrml =  getCtlFormula().toString().replace(" ","");
					int counter = 1;
					for (PredicateFormula fa : frmlForAbstraction) {
						currentFrml = currentFrml.replace(fa.toString().replace(" ",""), "ttt"+counter);
						counter++;
					}
					DefaultComboBoxModel ctlCombo = (DefaultComboBoxModel)cmbCtlFormulae.getModel();
					ctlCombo.removeElement(currentFrml);
					ctlCombo.addElement(currentFrml);
					ctlCombo.setSelectedItem(currentFrml);
					KripkyByFrmllWithGrouping krpByFrmlGrp = new KripkyByFrmllWithGrouping(kripkeModel, frmlForAbstraction);
					kripkeByFormulaGroupingModel = krpByFrmlGrp.Reduce();//kripkeModel.getAbstractedModelByFrmlsGrouping(repairFrmlPropFrmls);
					TablesDataBind(kripkeByFormulaGroupingModel.getStatesList());
			 	   //modelWasChecked = false;
			 	   modelWasRepaired = false;
			 	   DoDrawDiagram(kripkeByFormulaGroupingModel, null);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(null, "there are No Propositional Formulae", "No Propositional Formula", NORMAL);
			   }
 	   	}
	   	else
	   	{
	   		JOptionPane.showMessageDialog(null, "Please select a formula first!", "Incomplete Entry", NORMAL);
	   	}
	}

	public void DoDrawDiagram(Kripke kripke, Collection<Transition> transitions) 
	{
		DrawingTool tool = new DrawingTool();
		String img = tool.DoDrawDiagram(kripke, transitions); 
			if (img != "") {
	
				panel.removeAll();
				
				GridBagConstraints gbc_myPictureScroll = new GridBagConstraints();
				gbc_myPictureScroll.insets = new Insets(5, 5, 5, 5);
				gbc_myPictureScroll.weighty = 1.0;
				gbc_myPictureScroll.fill = GridBagConstraints.BOTH;
				gbc_myPictureScroll.gridx = 0;
				gbc_myPictureScroll.gridy = 0;
				
				myPicture.setImage(img);
				myPictureScroll = new JScrollPane(myPicture);
				panel.add(myPictureScroll, gbc_myPictureScroll);
				myPicture.repaintPanel();
				panel.revalidate();
			}
				
		

	}
	
	private void enableFrmlAbstraction(boolean enable)
	{
		txtFormulaes.setEnabled(enable);
		btnFormulae.setEnabled(enable);
		
	}

	private boolean FormulaModelCheck(PredicateFormula pf, Kripke kripke) {
		try {
				ModelChecker modelCheck = new ModelChecker();
				List<PredicateFormula> pfCollection = new ArrayList<PredicateFormula>();
				pfCollection.add(pf);
				Kripke clonedKripke = kripke.clone();
				Boolean isCorrect =  modelCheck.modelCheck(clonedKripke, pfCollection);
				return isCorrect;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	private boolean checkIfPropositional(String formula)
	{
		String toCompare = formula.replace(" ",	"");
		if(toCompare.contains("AF(")
				|| toCompare.contains("AG(")
				|| toCompare.contains("AX(")
				|| toCompare.contains("AU(")
				|| toCompare.contains("AV(")
				|| toCompare.contains("AW(")
				|| toCompare.contains("EG(")
				|| toCompare.contains("EX(")
				|| toCompare.contains("EU(")
				|| toCompare.contains("EV(")
				|| toCompare.contains("EF(")
				|| toCompare.contains("EW(")
				|| toCompare.contains("A[F(")
				|| toCompare.contains("A[G(")
				|| toCompare.contains("A[X(")
				|| toCompare.contains("A[U(")
				|| toCompare.contains("A[V(")
				|| toCompare.contains("A[W(")
				|| toCompare.contains("E[G(")
				|| toCompare.contains("E[X(")
				|| toCompare.contains("E[U(")
				|| toCompare.contains("E[V(")
				|| toCompare.contains("E[F(")
				|| toCompare.contains("E[W(")
				)
			return false;
		return true;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FullFrameEvents frame = new FullFrameEvents();
					frame.setTitle("Program Repair");
					//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

}
