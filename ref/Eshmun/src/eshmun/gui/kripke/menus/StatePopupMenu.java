package eshmun.gui.kripke.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.kripke.utils.DragAndDropHandler;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

public class StatePopupMenu extends JPopupMenu {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -4870320673772268593L;
	
	GraphPanel graphPanel;
	public State oldState;
	
	JCheckBoxMenuItem retainItem;
	JCheckBoxMenuItem startItem;
	public StatePopupMenu(GraphPanel graphPanelInstance) {
		super("State");
		
		graphPanel = graphPanelInstance;
		
		JMenuItem addEdgeItem = new JMenuItem("Add Transition");
		add(addEdgeItem);
		
	 
		addSeparator();
		
		JMenuItem editItem = new JMenuItem("Edit");
		add(editItem);
		
		retainItem = new JCheckBoxMenuItem("Retain", false);
		add(retainItem);
		
		startItem = new JCheckBoxMenuItem("Start", false);
		add(startItem);
		
		addSeparator();
		
		JMenuItem deleteItem = new JMenuItem("Delete");
		add(deleteItem);
		
		
		addEdgeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				oldState = graphPanel.getSelectedState();
				if(oldState == null) {
					return;
				}
				
				DragAndDropHandler dadh = graphPanel.getDragAndDropHandler();
				dadh.addEdge = true;
			}
		});
		
		
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				oldState.edit();
			}
		});
				
		retainItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(graphPanel.isTableau()) return;
				oldState.setRetain(retainItem.isSelected());
				graphPanel.repaint();
			}
		});
		
		startItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(graphPanel.isTableau()) return;
				oldState.setStart(startItem.isSelected());
				
				graphPanel.repaint();
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				graphPanel.deleteState(oldState);
			}
		});
		
	}
	
	public void show(Component invoker, int x, int y, State s) {
		retainItem.setSelected(s.getRetain());
		startItem.setSelected(s.getStart());
		
		super.show(invoker, x, y);
		oldState = s;
	}
	
	public void setState(State s) {
		if(oldState != null && s != null) {
			boolean isDoubled = graphPanel.isDoubled(oldState, s);
			
			Transition e = new Transition(oldState, s, isDoubled, graphPanel.getStructureName());
			graphPanel.addTransition(e);
		}
	}
	
}
