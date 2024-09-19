package eshmun.gui.synchronizationskeletons.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

public class TransitionPopupMenu extends JPopupMenu {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -4870320673772268593L;
	
	SynchronizationSkeletonPanel skeletonPanel;
	SkeletonTransition oldEdge;
	
	public TransitionPopupMenu(SynchronizationSkeletonPanel skeletonPanelInstance) {
		super("Transition");
		
		skeletonPanel = skeletonPanelInstance;
				
		JCheckBoxMenuItem editItem = new JCheckBoxMenuItem("Edit", false);
		add(editItem);
		
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(oldEdge.edit()) {
					skeletonPanel.getUndoManager().register();
					skeletonPanel.repaint();
				}
			}
		});

		addSeparator();
		
		JMenuItem deleteItem = new JMenuItem("Delete");
		add(deleteItem);
		
						
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeletonPanel.delete(oldEdge);
				skeletonPanel.getUndoManager().register();
				skeletonPanel.repaint();
			}
		});
		
	}
	
	public void show(Component invoker, int x, int y, SkeletonTransition e) {		
		super.show(invoker, x, y);
		oldEdge = e;
	}
		
}
