package eshmun.gui.synchronizationskeletons.bars;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import eshmun.Eshmun;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.dialogs.AboutDialog;
import eshmun.gui.kripke.dialogs.HelpDialog;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.synchronizationskeletons.menus.StatePopupMenu;
import eshmun.gui.synchronizationskeletons.utils.DragAndDropHandler;
import eshmun.gui.utils.models.skeleton.SkeletonSaveObject;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

public class SynchronizationMenuBar extends JMenuBar {
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -8902703044541332178L;
	
	private static int ctrlModifier = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
		
	private SynchronizationSkeletonPanel skeleton;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	
	private JMenuItem editItem;
	private JMenuItem deleteItem;
	private JMenuItem addEdgeItem;
	
	public SynchronizationMenuBar(SynchronizationSkeletonPanel skeletonInstance) {
		super();
		
		this.skeleton = skeletonInstance;
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu insertMenu = new JMenu("Insert");
		JMenu viewMenu = new JMenu("View");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");
		
		//enable/disable items
		
		editMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent arg0) {				
				boolean canUndo = skeleton.getUndoManager().canUndo();
				boolean canRedo = skeleton.getUndoManager().canRedo();
				
				undoItem.setEnabled(canUndo);
				redoItem.setEnabled(canRedo);
				
				if(skeleton.getSelectedState() != null || skeleton.getSelectedTransition() != null) {
					editItem.setEnabled(true);
					deleteItem.setEnabled(true);
				} else {
					editItem.setEnabled(false);
					deleteItem.setEnabled(false);
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent arg0) {
				
			}
			
			@Override
			public void menuCanceled(MenuEvent arg0) {
				
			}
		});
		
		insertMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent arg0) {
				if(skeleton.getSelectedState() != null) {
					addEdgeItem.setEnabled(true);
				} else {
					addEdgeItem.setEnabled(false);
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent arg0) {
				addEdgeItem.setEnabled(true);
			}
			
			@Override
			public void menuCanceled(MenuEvent arg0) {
				addEdgeItem.setEnabled(true);
			}
		});
		
		//FILE MENU
		//FILE MENU
		JMenu newFileMenu = new JMenu("New");
		JMenuItem newKripkeItem = new JMenuItem("Kripke");
		JMenuItem newMultiKripkeItem = new JMenuItem("Concurrent Kripke");
		JMenuItem newSynchronizationSkeleton = new JMenuItem("Synchronization Skeleton");
		//JMenuItem newIOAItem = new JMenuItem("IO-Automata");
		//JMenuItem newBipItem = new JMenuItem("Bip Structure");
		
		newKripkeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				java.awt.EventQueue.invokeLater(new Runnable() {
				    public void run() {
				    	new Eshmun(StructureType.Kripke).setVisible(true);
				    }
				});
				
				skeleton.getFrame().dispose();
			}
		});
		
		newMultiKripkeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
				    public void run() {
				    	new Eshmun(StructureType.MultiKripke).setVisible(true);
				    }
				});
				
				skeleton.getFrame().dispose();
			}
		});
		
		newSynchronizationSkeleton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.clear();
		    	skeleton.resetUndoManager();
		    	repaint();
			}
		});
		
		newKripkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ctrlModifier));
		newMultiKripkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ctrlModifier));
		newSynchronizationSkeleton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ctrlModifier));
		//newIOAItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ctrlModifier));
		//newBipItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ctrlModifier));
		
		newFileMenu.add(newKripkeItem);
		newFileMenu.add(newMultiKripkeItem);
		newFileMenu.add(newSynchronizationSkeleton);
		//newFileMenu.add(newIOAItem);
		//newFileMenu.add(newBipItem);

		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem loadItem = new JMenuItem("Open");
		JMenuItem clearItem = new JMenuItem("Clear");
		
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if(dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						String path = chooser.getSelectedFile().getAbsolutePath();
						skeleton.save(path);
						
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
							skeleton.load(obj);
							JOptionPane.showMessageDialog(null, "Opened Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
						} else {
							java.awt.EventQueue.invokeLater(new Runnable() {
							    public void run() {
							    	Eshmun eshmun = new Eshmun(saveable.getStructureType());
							    	eshmun.load(saveable);
							    	eshmun.setVisible(true);
							    }
							});
							
							skeleton.getFrame().dispose();
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
		
		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.clear();
				repaint();
			}
		});
		
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrlModifier));
		loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctrlModifier));
		clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrlModifier));
		
		fileMenu.add(newFileMenu);
		fileMenu.add(saveItem);
		fileMenu.add(loadItem);
		fileMenu.add(clearItem);
		
		fileMenu.addSeparator();
		
		JMenu exportItem = new JMenu("Export");
		JMenuItem exportImageItem = new JMenuItem("Image");
		
		exportImageItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				double width = skeleton.getDrawableWidth() * Eshmun.EXPORT_IMAGE_SCALE_FACTOR;
				double height = skeleton.getDrawableHeight() * Eshmun.EXPORT_IMAGE_SCALE_FACTOR;
				
				if(width == 0 || height == 0) {
					return; //EMPTY DRAWING PANEL
				}
				
				BufferedImage bImg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
			    Graphics2D cg = bImg.createGraphics();
			    skeleton.paintComponents(cg, true);
			    
			    JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if(dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						if(ImageIO.write(bImg, "png", chooser.getSelectedFile().getAbsoluteFile()))
							JOptionPane.showMessageDialog(null, "Exported Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			    	
			    	} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			    	}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
				
		exportImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.ALT_DOWN_MASK));
		exportItem.add(exportImageItem);
		fileMenu.add(exportItem);
		
		//EDIT MENU
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		
		undoItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getUndoManager().undo();
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getUndoManager().redo();
			}
		});
		
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ctrlModifier));
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ctrlModifier));
		
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		
		editMenu.addSeparator();
		
		editItem = new JMenuItem("Edit");
		deleteItem = new JMenuItem("Delete");
		
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonState s = skeleton.getSelectedState();
				SkeletonTransition edg = skeleton.getSelectedTransition();
				
				if(s != null) {
					s.edit();
					String tmp = s.getLabel();
					if(s.edit()) {
						if(skeleton.checkLegalLabel(s)) {
							skeleton.getUndoManager().register();
							skeleton.repaint();
						} else {
							s.setLabel(tmp);
						}
					}
				} else if(edg != null) {
					if(edg.edit()) {
						skeleton.getUndoManager().register();
						skeleton.repaint();
					}
				}
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonState s = skeleton.getSelectedState();
				SkeletonTransition edg = skeleton.getSelectedTransition();
				
				if(s != null) {
					skeleton.delete(s);
					skeleton.getUndoManager().register();
					skeleton.repaint();
				} else if(edg != null) {
					skeleton.delete(edg);
					skeleton.getUndoManager().register();
					skeleton.repaint();
				}			
			}
		});
		
		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		editMenu.add(editItem);
		editMenu.add(deleteItem);
		
		//INSERT MENU
		JMenuItem addStateItem = new JMenuItem("State");
		addEdgeItem = new JMenuItem("Transition");
		
		addStateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int x = (int) skeleton.getScrollPane().getHorizontalScrollBar().getValue() + 5;
				int y = (int) skeleton.getScrollPane().getVerticalScrollBar().getValue() + 5;
				Point location = skeleton.translatePoint(new Point(x, y));		
						
				String newLabel = JOptionPane.showInputDialog(null, "Enter the new state label", "");
				if(newLabel != null) {
					SkeletonState s = new SkeletonState(newLabel, false, location);
					if(skeleton.add(s)) {
						skeleton.getUndoManager().register();
						skeleton.repaint();
					}
				}
				
				skeleton.requestFocusInWindow();
			}
		});
		
		addEdgeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonState s = skeleton.getSelectedState();
				if(s == null) {
					return;
				}
				
				DragAndDropHandler dadh = skeleton.getDragAndDropHandler();
				dadh.addEdge = true;
				
				StatePopupMenu spum = skeleton.getStatePopupMenu();
				spum.oldState = s;
			}
		});
		
		addStateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		addEdgeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.SHIFT_DOWN_MASK));
		
		insertMenu.add(addStateItem);
		insertMenu.add(addEdgeItem);
		
		//VIEW MENU		
		JMenuItem zoomInItem = new JMenuItem("Zoom In");
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");
		
		zoomInItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				skeleton.zoomIn();
			}
		});
		
		zoomOutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				skeleton.zoomOut();
			}
		});
		
		resetZoomItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				skeleton.resetZoom();
			}
		});
		
		zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ctrlModifier));
		zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ctrlModifier));
		resetZoomItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ctrlModifier));
		
		viewMenu.add(zoomInItem);
		viewMenu.add(zoomOutItem);
		viewMenu.add(resetZoomItem);
		
		viewMenu.addSeparator();
				
		JMenuItem autoFormat = new JMenuItem("Auto Format");
		
		autoFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				skeleton.autoFormat();				
			}
		});
		
		viewMenu.add(autoFormat);
				
		// TOOLS MENU
		
		JMenuItem kripkeItem = new JMenuItem("Generate Kripke Structure");
		
		kripkeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				String name = skeleton.getFrame().getBaseName();
				
				if(name == null || name.isEmpty()) {
					name = JOptionPane.showInputDialog(null, "Please enter the structure's base name", "");
					if(name == null || name.isEmpty()) return;
				}
				
				Object[] options = new Object[] {"Single Kripke Structure", "Concurrent-Pairs Kripke Structure"};
				Object choice = JOptionPane.showInputDialog(null, "Choose the resulting Structure", "To Kripke", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);				
				if(choice == null) return;

				if(choice.equals(options[0])) {
					skeleton.getFrame().generateSingleKripke(name);
				} else {
					skeleton.getFrame().generateConcurrentKripke(name);
				}
			}
		});
		
		toolsMenu.add(kripkeItem);
		
		
		//HELP MENU
		
		JMenuItem helpItem = new JMenuItem("Help");
		JMenuItem aboutItem = new JMenuItem("About Eshmun");
		
		helpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new HelpDialog().setVisible(true);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not locate help files", "Error", JOptionPane.ERROR_MESSAGE);
				}			
			}
		});

		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutDialog().setVisible(true);
			}
		});
		
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		
		helpMenu.add(helpItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutItem);
		
		
		add(fileMenu);
		add(editMenu);
		add(insertMenu);
		add(viewMenu);
		add(toolsMenu);
		add(helpMenu);
	}
}
