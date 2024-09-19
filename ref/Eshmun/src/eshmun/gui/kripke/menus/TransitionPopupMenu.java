package eshmun.gui.kripke.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.utils.models.vanillakripke.Transition;

public class TransitionPopupMenu extends JPopupMenu {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -4870320673772268593L;
	
	GraphPanel graphPanel;
	Transition oldEdge;
	
	JCheckBoxMenuItem retainItem;
	public TransitionPopupMenu(GraphPanel graphPanelInstance) {
		super("Transition");
		
		graphPanel = graphPanelInstance;
		
		JMenuItem editItem = new JMenuItem("Edit Action");
		add(editItem);
				
		retainItem = new JCheckBoxMenuItem("Retain", false);
		add(retainItem);

		addSeparator();
		
		JMenuItem deleteItem = new JMenuItem("Delete");
		add(deleteItem);
		
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				oldEdge.edit();
			}
		});
		
		retainItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(graphPanel.isTableau()) return;
				oldEdge.setRetain(retainItem.isSelected());
				graphPanel.repaint();
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				graphPanel.deleteTransition(oldEdge);
			}
		});
		
	}
	
	public void show(Component invoker, int x, int y, Transition e) {
		retainItem.setSelected(e.getRetain());
		
		super.show(invoker, x, y);
		oldEdge = e;
	}
		
}
