package eshmun.gui.synchronizationskeletons.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import eshmun.expression.guard.AtomicGuardExpression;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.synchronizationskeletons.utils.DragAndDropHandler;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

public class StatePopupMenu extends JPopupMenu {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -4870320673772268593L;
	
	SynchronizationSkeletonPanel skeletonPanel;
	public SkeletonState oldState;
	
	JCheckBoxMenuItem startItem;
	public StatePopupMenu(SynchronizationSkeletonPanel skeletonPanelInstance) {
		super("State");
		
		skeletonPanel = skeletonPanelInstance;
		
		JMenuItem addEdgeItem = new JMenuItem("Add Transition");
		add(addEdgeItem);
		
		addSeparator();
		
		JMenuItem editItem = new JMenuItem("Edit Label");
		add(editItem);
				
		JMenuItem processItem = new JMenuItem("Edit Process");
		add(processItem);
		
		startItem = new JCheckBoxMenuItem("Start", false);
		add(startItem);
		
		addSeparator();
		
		JMenuItem deleteItem = new JMenuItem("Delete");
		add(deleteItem);
		
		
		addEdgeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				oldState = skeletonPanel.getSelectedState();
				if(oldState == null) {
					return;
				}
				
				DragAndDropHandler dadh = skeletonPanel.getDragAndDropHandler();
				dadh.addEdge = true;
			}
		});
		
		
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tmp = oldState.getLabel();
				if(oldState.edit()) {
					if(skeletonPanel.checkLegalLabel(oldState)) {
						skeletonPanel.getUndoManager().register();
						skeletonPanel.repaint();
					} else {
						oldState.setLabel(tmp);
					}
				}
			}
		});
		
		processItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(oldState.editProcess()) {
					skeletonPanel.propagateProcess(oldState);
				}
				
				skeletonPanel.getUndoManager().register();
				skeletonPanel.repaint();
			}
		});
		
		startItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				oldState.setStart(startItem.isSelected());
				skeletonPanel.addStartState(oldState);
				skeletonPanel.getUndoManager().register();
				skeletonPanel.repaint();
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeletonPanel.delete(oldState);
				skeletonPanel.getUndoManager().register();
				skeletonPanel.repaint();
			}
		});
		
	}
	
	public void show(Component invoker, int x, int y, SkeletonState s) {
		startItem.setSelected(s.isStart());
		
		super.show(invoker, x, y);
		oldState = s;
	}
	
	public void setState(SkeletonState s) {
		if(oldState != null && s != null && !oldState.equals(s)) {
			for(SkeletonTransition t : oldState.getTransitions()) {
				if(t.getTo().getLabel().equals(s.getLabel())) {
					oldState = null;
					return;
				}
			}
			
			SkeletonTransition e = new SkeletonTransition(oldState, s, new AtomicGuardExpression());
			
			if(e.edit()) {
				oldState.addTransition(e);
				skeletonPanel.add(e);
				skeletonPanel.getUndoManager().register();
				skeletonPanel.repaint();
			}
			
			oldState = null;
		}
	}
	
}
