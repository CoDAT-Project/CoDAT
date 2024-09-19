package eshmun.gui.synchronizationskeletons.menus;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import eshmun.Eshmun;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.utils.models.skeleton.SkeletonSaveObject;
import eshmun.gui.utils.models.skeleton.SkeletonState;

public class FreePopupMenu extends JPopupMenu {

	/**
	 * Auto Generated Serial UID
	 */
	private static final long serialVersionUID = 6878666297214222241L;
	
	SynchronizationSkeletonPanel skeletonPanel;
	
	JMenuItem undoItem;
	JMenuItem redoItem;
	public FreePopupMenu(SynchronizationSkeletonPanel skeletonPanelInstance) {
		super("Eshmun");
		
		skeletonPanel = skeletonPanelInstance;
		
		JMenuItem newStateItem = new JMenuItem("New State");
		add(newStateItem);
		
		addSeparator();
		
		JMenuItem zoomInItem = new JMenuItem("Zoom in");
		add(zoomInItem);
		
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		add(zoomOutItem);
		
		JMenuItem autoFormatItem = new JMenuItem("Auto Format");
		add(autoFormatItem);
		
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
		
		newStateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Point location = skeletonPanel.getMousePopupPoint();
				
				String newLabel = JOptionPane.showInputDialog(null, "Enter the new state label", "");
				if(newLabel != null) {
					SkeletonState s = new SkeletonState(newLabel, false, location);
					if(skeletonPanel.add(s)) {
						skeletonPanel.getUndoManager().register();
						skeletonPanel.repaint();
					}
				}
								
				skeletonPanel.requestFocusInWindow();
			}
		});
		
		zoomInItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point location = skeletonPanel.getMousePopupPoint();
				skeletonPanel.zoomIn(location);
			}
		});
		
		zoomOutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point location = skeletonPanel.getMousePopupPoint();
				skeletonPanel.zoomOut(location);
			}
		});
		
		autoFormatItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				skeletonPanel.autoFormat();
			}
		});
		
		undoItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				skeletonPanel.getUndoManager().undo();
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeletonPanel.getUndoManager().redo();
			}
		});
		
		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeletonPanel.clear();
			}
		});
		
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if(dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						String path = chooser.getSelectedFile().getAbsolutePath();
						skeletonPanel.save(path);
						
						JOptionPane.showMessageDialog(null, "Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);
				if(dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						String path = chooser.getSelectedFile().getAbsolutePath();
							
						final Saveable saveable = Saveable.load(path);
						if(saveable.getStructureType() == StructureType.SynchronizationSkeleton) {
							SkeletonSaveObject obj = (SkeletonSaveObject) saveable;
							skeletonPanel.load(obj);
							JOptionPane.showMessageDialog(null, "Opened Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
						} else {
							java.awt.EventQueue.invokeLater(new Runnable() {
							    public void run() {
							    	Eshmun eshmun = new Eshmun(saveable.getStructureType());
							    	eshmun.setVisible(true);
							    	eshmun.load(saveable);
							    }
							});
							
							skeletonPanel.getFrame().dispose();
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (ClassCastException e2) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e2.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	@Override
	public void show(Component invoker, int x, int y) {
		boolean canUndo = skeletonPanel.getUndoManager().canUndo();
		boolean canRedo = skeletonPanel.getUndoManager().canRedo();
		
		undoItem.setEnabled(canUndo);
		redoItem.setEnabled(canRedo);
		
		super.show(invoker, x, y);
	}
}
