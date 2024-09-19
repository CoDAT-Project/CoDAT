package eshmun.gui.kripke.menus;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

 
import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.dialogs.StateDialog;

import eshmun.gui.utils.models.vanillakripke.State;

public class FreePopupMenu extends JPopupMenu {

	/**
	 * Auto Generated Serial UID
	 */
	private static final long serialVersionUID = 6878666297214222241L;
	
	GraphPanel graphPanel;
	
	JMenuItem undoItem;
	JMenuItem redoItem;
	public FreePopupMenu(GraphPanel graphPanelInstance) {
		super("Eshmun");
		
		graphPanel = graphPanelInstance;
		
		JMenuItem newStateItem = new JMenuItem("New State");
		add(newStateItem);
		
		addSeparator();
		
		JMenuItem zoomInItem = new JMenuItem("Zoom in");
		add(zoomInItem);
		
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		add(zoomOutItem);
		
		addSeparator();
		
		undoItem = new JMenuItem("Undo");
		add(undoItem);
		redoItem = new JMenuItem("Redo");
		add(redoItem);
		
		addSeparator();		
		JMenuItem loadItem = new JMenuItem("Open");
		add(loadItem);
		JMenuItem saveItem = new JMenuItem("Save");
		add(saveItem);
		JMenuItem clearItem = new JMenuItem("Clear");
		add(clearItem);
		JMenuItem quiteItem = new JMenuItem("Quit");
		add(quiteItem);
		
		newStateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			if(graphPanel.isTableau()) return;
				Point location = graphPanel.getMousePopupPoint();
				
			if(graphPanel.getEshmun().getStructureType() == StructureType.InfiniteStateKripke){
				
				StateDialog dialog = new StateDialog(graphPanel.getEshmun(), "", "", "True", false, false);
				dialog.setVisible(true);
				
				if(dialog.isSuccessful()) {
					State s = new State(dialog.getName(), dialog.getLabels(), 
							dialog.getPreCondition(),  location);
				
					s.setStart(dialog.isStart());
					s.setRetain(dialog.isRetain());
					
					graphPanel.addState(s);
				}
			 
			}else{
				StateDialog dialog = new StateDialog(graphPanel.getEshmun(),"", "", "True", false, false);
				dialog.setVisible(true);
				
				if(dialog.isSuccessful()) {
					State s = new State(dialog.getName(), dialog.getLabels(),dialog.getPreCondition(),  location);
					s.setStart(dialog.isStart());
					s.setRetain(dialog.isRetain());
					
					graphPanel.addState(s);
				}
			}
			
				
				
				
				graphPanel.requestFocusInWindow();
			}
		});
		
		zoomInItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point location = graphPanel.getMousePopupPoint();
				graphPanel.zoomIn(location);
			}
		});
		
		zoomOutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point location = graphPanel.getMousePopupPoint();
				graphPanel.zoomOut(location);
			}
		});
		
		undoItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				graphPanel.getUndoManager().undo();
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphPanel.isTableau()) return;
				graphPanel.getUndoManager().redo();
			}
		});
		
		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphPanel.clear();
			}
		});
		
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphPanel.save(graphPanel.getEshmun().getSpecificationFormula(), graphPanel.getEshmun().getStructureFormula());
			}
		});
		
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphPanel.load();
			}
		});
		
		quiteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphPanel.getEshmun().exit();
			}
		});
		
	}
	
	@Override
	public void show(Component invoker, int x, int y) {
		boolean canUndo = graphPanel.getUndoManager().canUndo();
		boolean canRedo = graphPanel.getUndoManager().canRedo();
		
		undoItem.setEnabled(canUndo);
		redoItem.setEnabled(canRedo);
		
		super.show(invoker, x, y);
	}
}
