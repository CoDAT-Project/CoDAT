package eshmun.gui.kripke.bars;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import t7.StatementInterpreter;
import t7.gui.TerminalGUI;
import eshmun.Eshmun;
import eshmun.choreography.ChoreographyRepresentation;
import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.dialogs.AboutDialog;
import eshmun.gui.kripke.dialogs.DecisionProcedureDialog;
import eshmun.gui.kripke.dialogs.HelpDialog;
import eshmun.gui.kripke.dialogs.StateDialog;
import eshmun.gui.kripke.menus.StatePopupMenu;
import eshmun.gui.kripke.utils.AbstractionCTLPanel;
import eshmun.gui.kripke.utils.DragAndDropHandler;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonFrame;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.skeletontextrepresentation.ProgramToKripkeConverter;
import eshmun.skeletontextrepresentation.infinitespace.InfiniteProgramToKripkeConverter;

public class EshmunMenuBar extends JMenuBar {
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -8902703044541332178L;

	private static int ctrlModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	private Eshmun eshmun;

	private JMenuItem undoItem;
	private JMenuItem redoItem;

	private JMenuItem editItem;
	private JMenuItem deleteItem;

	private JCheckBoxMenuItem syncOverProcesses;
	private JCheckBoxMenuItem syncOverActions;

	private JMenuItem addEdgeItem;

	private JCheckBoxMenuItem actionColor;
	private JCheckBoxMenuItem transitionColorProcess;
	private JCheckBoxMenuItem labelColorProcess;
	private JMenuItem customColors;
	private JMenu specificProcess;

	private JMenuItem abstractByLabel;
	private JMenuItem abstractByCTLFormulae;
	private JMenuItem abstractByFormula;
	private JMenuItem abstractToFull;
	private JMenuItem resetAbstract;

	public EshmunMenuBar(Eshmun eshmunInstance) {
		super();

		this.eshmun = eshmunInstance;

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu insertMenu = new JMenu("Insert");
		JMenu viewMenu = new JMenu("View");
		JMenu formulaMenu = new JMenu("Formula");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");

		// enable/disable items

		editMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent arg0) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();

				boolean canUndo = graphPanel.getUndoManager().canUndo();
				boolean canRedo = graphPanel.getUndoManager().canRedo();

				undoItem.setEnabled(canUndo);
				redoItem.setEnabled(canRedo);

				if (graphPanel.getSelectedState() != null || graphPanel.getSelectedTransition() != null) {
					editItem.setEnabled(true);
					deleteItem.setEnabled(true);
				} else {
					editItem.setEnabled(false);
					deleteItem.setEnabled(false);
				}

				syncOverActions.setSelected(eshmun.syncOverActions());
				syncOverProcesses.setSelected(eshmun.syncOverProcessIndices());
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
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.getSelectedState() != null) {
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

		viewMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent arg0) {
				actionColor.setSelected(eshmun.isActionColored());
				transitionColorProcess.setSelected(eshmun.isTransitionColoredByProcess());
				labelColorProcess.setSelected(eshmun.isLabelColoredByProcess());
				specificProcess.removeAll();

				String name = eshmun.getCurrentGraphPanel().getStructureName();
				if (eshmun.hasColoring() && name.contains("(")) {
					customColors.setEnabled(true);
					specificProcess.setEnabled(true);

					int start = name.indexOf("(") + 1;
					int end = name.indexOf(")");

					String[] processes = name.substring(start, end).split(",");
					for (String p : processes) {
						p = p.trim();

						boolean selected = eshmun.getTransitionColor(p).getAlpha() == 255;

						JCheckBoxMenuItem process_p = new JCheckBoxMenuItem(p);
						process_p.setName(p);
						process_p.setSelected(selected);

						process_p.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								String process = ((JCheckBoxMenuItem) arg0.getSource()).getName();
								boolean value = ((JCheckBoxMenuItem) arg0.getSource()).isSelected();
								int alpha = value ? 255 : 0;

								eshmun.setAlphaForProcessColor(process, alpha);
								eshmun.getCurrentGraphPanel().repaint();
							}
						});

						specificProcess.add(process_p);
					}

				} else {
					customColors.setEnabled(false);
					specificProcess.setEnabled(false);
				}
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {

			}

			@Override
			public void menuCanceled(MenuEvent arg0) {

			}
		});

		// FILE MENU
		JMenu newFileMenu = new JMenu("New");
		JMenuItem newKripkeItem = new JMenuItem("Kripke");
		JMenuItem newMultiKripkeItem = new JMenuItem("Concurrent Kripke");
		JMenuItem newSynchronizationSkeleton = new JMenuItem("Synchronization Skeleton");
		JMenuItem newInfiniteStateKripke = new JMenuItem("Infinite State Kripke");
		// JMenuItem newIOAItem = new JMenuItem("IO-Automata");
		// JMenuItem newBipItem = new JMenuItem("Bip Structure");

		newKripkeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						new Eshmun(StructureType.Kripke).setVisible(true);
					}
				});
				eshmun.dispose();
			}
		});

		newInfiniteStateKripke.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						new Eshmun(StructureType.InfiniteStateKripke).setVisible(true);
					}
				});
				eshmun.dispose();
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
				eshmun.dispose();
			}
		});

		newSynchronizationSkeleton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						new SynchronizationSkeletonFrame().setVisible(true);
					}
				});

				eshmun.dispose();
			}
		});

		newKripkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ctrlModifier));
		newMultiKripkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ctrlModifier));
		newSynchronizationSkeleton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ctrlModifier));
		newInfiniteStateKripke.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ctrlModifier));
		// newIOAItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
		// ctrlModifier));
		// newBipItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
		// ctrlModifier));

		newFileMenu.add(newKripkeItem);
		newFileMenu.add(newMultiKripkeItem);
		newFileMenu.add(newSynchronizationSkeleton);
		newFileMenu.add(newInfiniteStateKripke);
		// newFileMenu.add(newIOAItem);
		// newFileMenu.add(newBipItem);

		fileMenu.add(newFileMenu);

		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem loadItem = new JMenuItem("Open");
		JMenuItem clearItem = new JMenuItem("Clear");

		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						eshmun.save(chooser.getSelectedFile().getAbsolutePath());

						eshmun.writeMessage("Saved Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(eshmun, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(eshmun, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						eshmun.load(Saveable.load(chooser.getSelectedFile().getAbsolutePath()));
						eshmun.writeMessage("Opened Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(eshmun, "Error Opening File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(eshmun, "Saved File has Unrecognizable Format: " + e1.toString(),
								"Error", JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					} catch (ClassCastException e2) {
						JOptionPane.showMessageDialog(eshmun, "Saved File has Unrecognizable Format: " + e2.toString(),
								"Error", JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(eshmun, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				graphPanel.clear();
			}
		});

		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrlModifier));
		loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctrlModifier));
		clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrlModifier));

		fileMenu.add(saveItem);
		fileMenu.add(loadItem);
		fileMenu.add(clearItem);

		fileMenu.addSeparator();

		JMenu exportItem = new JMenu("Export");
		JMenuItem exportImageItem = new JMenuItem("Image");
		JMenuItem exportTextItem = new JMenuItem("Text Representation");

		exportImageItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();

				double width = graphPanel.getDrawableWidth() * Eshmun.EXPORT_IMAGE_SCALE_FACTOR;
				double height = graphPanel.getDrawableHeight() * Eshmun.EXPORT_IMAGE_SCALE_FACTOR;

				if (width == 0 || height == 0) {
					return; // EMPTY DRAWING PANEL
				}

				BufferedImage bImg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
				Graphics2D cg = bImg.createGraphics();
				graphPanel.paintComponents(cg, true);

				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						if (ImageIO.write(bImg, "png", chooser.getSelectedFile().getAbsoluteFile()))
							eshmun.writeMessage("Exported Successfully!", false);

					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		exportTextItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String definition = eshmun.getGraphPanelContainer().constructDefinition();

				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						if (f.exists())
							f.delete();

						f.createNewFile();
						FileWriter fw = new FileWriter(f);
						fw.write(definition);
						fw.close();

						eshmun.writeMessage("Exported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		exportImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.ALT_DOWN_MASK));
		exportTextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.ALT_DOWN_MASK));

		exportItem.add(exportImageItem);
		exportItem.add(exportTextItem);

		JMenu importItem = new JMenu("Import");

		JMenuItem importStringItem = new JMenuItem("Text Representation");
		
		importStringItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						Scanner scan = new Scanner(f);

						StructureType type = StructureType.Kripke;
						String name = scan.nextLine();
						if (name.equals("***MULTI***")) {
							type = StructureType.MultiKripke;
						} else {
							type = StructureType.Kripke;
						}

						String result = name + System.lineSeparator();
						while (scan.hasNextLine()) {
							result += scan.nextLine() + System.lineSeparator();
						}

						scan.close();
						eshmun.loadDefinition(result.trim(), type);

						eshmun.writeMessage("Imported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		importStringItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));

		importItem.add(importStringItem);

		// For Choreography Representation - Al-Abbass
		JMenuItem importChoreoItem = new JMenuItem("Choreography Representation");
		
		importChoreoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						Scanner scan = new Scanner(f);

						StructureType type = StructureType.Kripke;
						//String name = scan.nextLine();
						/*if (name.equals("***MULTI***")) {
							type = StructureType.MultiKripke;
						} else {
							type = StructureType.Kripke;
						}*/

						String result = ""; // name + System.lineSeparator();
						while (scan.hasNextLine()) {
							String line = scan.nextLine();
							if(!line.startsWith("--"))
								result += line + System.lineSeparator();
						}

						scan.close();
						try {
							result = new ChoreographyRepresentation().Convert(result);	
						}
						catch(Exception e) {
							
						}
						
						eshmun.loadDefinition(result.trim(), type);

						eshmun.writeMessage("Imported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		importItem.add(importChoreoItem);
		
		JMenuItem importStringProgram = new JMenuItem("Program");

		importStringProgram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);

				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						StructureType type = StructureType.Kripke;

						String result = ProgramToKripkeConverter.convert(f.getPath(),  false);
						
						eshmun.loadDefinition(result, type);
						
						String spec = ProgramToKripkeConverter.getCtl();
						if(spec!= null) {
							eshmun.setSpecificationFormula(spec);
						}

						eshmun.writeMessage("Imported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error loading file", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		importStringProgram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));

		importItem.add(importStringProgram);

		JMenuItem importStringProgramAbstract = new JMenuItem("Program (Abstract View)");

		importStringProgramAbstract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);

				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						StructureType type = StructureType.Kripke;

						String result = ProgramToKripkeConverter.convert(f.getPath(),  true);

						eshmun.loadDefinition(result, type);
						
						String spec = ProgramToKripkeConverter.getCtl();
						if(spec!= null) {
							eshmun.setSpecificationFormula(spec);
						}

						eshmun.writeMessage("Imported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Error loading file", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		importStringProgramAbstract.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));

		importItem.add(importStringProgramAbstract);
		
		
		JMenuItem importStringProgramInfinite = new JMenuItem("Infinite Program");

		 
		
		
		importStringProgramInfinite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);

				if (dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						File f = chooser.getSelectedFile().getAbsoluteFile();
						StructureType type = StructureType.Kripke;

						long startTime = System.nanoTime();
						
						String result = InfiniteProgramToKripkeConverter.convert(f.getPath(), eshmunInstance);
						long endTime = System.nanoTime();
						
						System.out.println((endTime - startTime)/1000000000 + " seconds");
						eshmun.loadDefinition(result, type);
						
//						String spec = ProgramToKripkeConverter.getCtl();
//						if(spec!= null) {
//							eshmun.setSpecificationFormula(spec);
//						}

						eshmun.writeMessage("Imported Successfully!", false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
								JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Error loading file", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		//importStringProgramInfinite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));

		importItem.add(importStringProgramInfinite);

		fileMenu.add(exportItem);
		fileMenu.add(importItem);

		fileMenu.addSeparator();

		JMenuItem quitItem = new JMenuItem("Quit");

		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.exit();
			}
		});

		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctrlModifier));
		fileMenu.add(quitItem);

		// EDIT MENU
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");

		undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;
				graphPanel.getUndoManager().undo();
			}
		});

		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;
				graphPanel.getUndoManager().redo();
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
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;

				State s = graphPanel.getSelectedState();
				Transition edg = graphPanel.getSelectedTransition();

				if (s != null) {
					s.edit();
				} else if (edg != null) {
					edg.edit();
				}
			}
		});

		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;

				State s = graphPanel.getSelectedState();
				Transition edg = graphPanel.getSelectedTransition();

				if (s != null) {
					graphPanel.deleteState(s);
				} else if (edg != null) {
					graphPanel.deleteTransition(edg);
				}
			}
		});

		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

		editMenu.add(editItem);
		editMenu.add(deleteItem);

		editMenu.addSeparator();

		syncOverActions = new JCheckBoxMenuItem("Sync Actions");
		syncOverProcesses = new JCheckBoxMenuItem("Sync Process Indices");

		syncOverActions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setSyncOverActions(syncOverActions.isSelected());
			}
		});

		syncOverProcesses.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setSyncOverProcessIndices(syncOverProcesses.isSelected());
			}
		});

		editMenu.add(syncOverActions);
		editMenu.add(syncOverProcesses);

		if (eshmun.getStructureType() == StructureType.Kripke) {
			editMenu.addSeparator();

			JMenuItem editStructureNameItem = new JMenuItem("Edit Structure Name");
			editStructureNameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
			editStructureNameItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (eshmun.getCurrentGraphPanel().isTableau())
						return;

					String newName = (String) JOptionPane.showInputDialog(null, "Enter New Structure Name",
							"Structure Name", JOptionPane.INFORMATION_MESSAGE, null, null,
							eshmun.getCurrentGraphPanel().getStructureName());

					if (newName != null && !newName.trim().equals(""))
						eshmun.getCurrentGraphPanel().setStructureName(newName);
				}
			});

			editMenu.add(editStructureNameItem);
		}

		JMenuItem generatePairs = new JMenuItem("Generate Pairs");
		generatePairs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;

				try {
					String processes = JOptionPane.showInputDialog(null, "Enter the number of processes",
							"Generate Pairs", JOptionPane.PLAIN_MESSAGE);
					if (processes == null)
						return;

					int numberOfProcesses = Integer.parseInt(processes.trim());

					String[] possibilities = new String[] { "All Pairs", "Ring" };
					Object arrangment = JOptionPane.showInputDialog(null, "Specify the arrangment of pairs",
							"Generate Pairs", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
					if (arrangment == null)
						return;

					eshmun.generatePairs(numberOfProcesses, arrangment.equals(possibilities[1]));
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid Number", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		editMenu.add(generatePairs);

		JMenu scriptingMenu = new JMenu("Scripts");
		JMenuItem scriptFile = new JMenuItem("Script File");
		JMenuItem scriptTerminal = new JMenuItem("Script Terminal");

		scriptFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showOpenDialog(null);
				if (dec == JFileChooser.APPROVE_OPTION) {
					try {
						Eshmun.last_Directory = chooser.getSelectedFile().getParent();
						File script = chooser.getSelectedFile();
						if (!script.exists()) {
							JOptionPane.showMessageDialog(eshmun, "File Doesn't Exist", "Error",
									JOptionPane.ERROR_MESSAGE);
							eshmun.writeMessage("Error!", true);
							return;
						}

						// interpreter to interpret statements.
						StatementInterpreter interpreter = new StatementInterpreter(System.out);

						interpreter.execute(script);
						eshmun.writeMessage("Executed Successfully!", false);
					} catch (Exception e1) {
						String message = "Error in Script File " + System.lineSeparator() + e1.getMessage();
						JOptionPane.showMessageDialog(eshmun, message, "Error", JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
						e1.printStackTrace();
					}

				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(eshmun, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
					eshmun.writeMessage("Error!", true);
				}
			}
		});

		scriptTerminal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TerminalGUI().setVisible(true);
			}
		});

		scriptingMenu.add(scriptFile);
		scriptingMenu.add(scriptTerminal);

		editMenu.addSeparator();
		editMenu.add(scriptingMenu);

		// INSERT MENU
		JMenuItem addStateItem = new JMenuItem("State");
		addEdgeItem = new JMenuItem("Transition");

		addStateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;

				int x = (int) graphPanel.getScrollPane().getHorizontalScrollBar().getValue() + 5;
				int y = (int) graphPanel.getScrollPane().getVerticalScrollBar().getValue() + 5;
				Point location = graphPanel.translatePoint(new Point(x, y));

				StateDialog diag = new StateDialog(eshmun, "", "", "", false, false);
				diag.setVisible(true);

				if (diag.isSuccessful()) {
					State s = new State(diag.getName(), diag.getLabels(), location);
					s.setStart(diag.isStart());
					s.setRetain(diag.isRetain());

					graphPanel.addState(s);
				}

				graphPanel.requestFocusInWindow();
			}
		});

		addEdgeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				if (graphPanel.isTableau())
					return;

				State s = graphPanel.getSelectedState();
				if (s == null) {
					return;
				}

				DragAndDropHandler dadh = graphPanel.getDragAndDropHandler();
				dadh.addEdge = true;

				StatePopupMenu spum = graphPanel.getStatePopupMenu();
				spum.oldState = s;
			}
		});

		addStateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		addEdgeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.SHIFT_DOWN_MASK));

		insertMenu.add(addStateItem);
		insertMenu.add(addEdgeItem);

		// VIEW MENU
		JCheckBoxMenuItem viewStats = new JCheckBoxMenuItem("Repair Statistics", true);
		viewStats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Eshmun.ECHO_STATS = ((JCheckBoxMenuItem) e.getSource()).isSelected();
			}
		});

		viewMenu.add(viewStats);
		viewMenu.addSeparator();

		actionColor = new JCheckBoxMenuItem("Color Actions", true);
		transitionColorProcess = new JCheckBoxMenuItem("Color Transitions", true);
		labelColorProcess = new JCheckBoxMenuItem("Color Labels", true);
		customColors = new JMenuItem("Customize Colors");

		actionColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setColorActions(((JCheckBoxMenuItem) e.getSource()).isSelected());
			}
		});

		transitionColorProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setTransitionColoredByProcess(((JCheckBoxMenuItem) e.getSource()).isSelected());
			}
		});

		labelColorProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setLabelColoredByProcess(((JCheckBoxMenuItem) e.getSource()).isSelected());
			}
		});

		customColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eshmun.letUserChooseColors();
			}
		});

		specificProcess = new JMenu("Show/Hide Processes");

		viewMenu.add(actionColor);
		viewMenu.add(transitionColorProcess);
		viewMenu.add(labelColorProcess);
		viewMenu.add(customColors);
		viewMenu.add(specificProcess);
		viewMenu.addSeparator();

		JMenuItem zoomInItem = new JMenuItem("Zoom In");
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");

		zoomInItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				graphPanel.zoomIn();
			}
		});

		zoomOutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				graphPanel.zoomOut();
			}
		});

		resetZoomItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				graphPanel.resetZoom();
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
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				graphPanel.autoFormat();
			}
		});

		viewMenu.add(autoFormat);

		// FORMULA MENU

		JMenuItem storeFormulaItem = new JMenuItem("Store Formula");
		JMenuItem loadFormulaItem = new JMenuItem("Load Formula");
		JMenuItem clearFormulaItem = new JMenuItem("Clear Formula");

		formulaMenu.add(storeFormulaItem);
		formulaMenu.add(loadFormulaItem);
		formulaMenu.add(clearFormulaItem);

		storeFormulaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String formula = eshmun.getSpecificationFormula();
				storeFormula(formula);
			}
		});

		loadFormulaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String formula = loadFormula();

				if (formula != null) {
					eshmun.setSpecificationFormula(formula);
				}
			}
		});

		clearFormulaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.setSpecificationFormula("");
				eshmun.getCurrentGraphPanel().requestFocusInWindow();
			}
		});

		storeFormulaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK));
		loadFormulaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_DOWN_MASK));
		clearFormulaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.ALT_DOWN_MASK));

		// TOOLS MENU

		JMenuItem modelCheckItem = new JMenuItem("Model Check");
		JMenuItem modelRepairItem = new JMenuItem("Model Repair");
		JMenuItem tripletCheckItem = new JMenuItem("Hoare Triple Check");

		modelCheckItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.modelCheck();
			}
		});

		modelRepairItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.modelRepair();
			}
		});

		tripletCheckItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.newTripletCheck(); 
			}
		});

		modelCheckItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		modelRepairItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		tripletCheckItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));

		toolsMenu.add(modelCheckItem);
		toolsMenu.add(modelRepairItem);
		toolsMenu.add(tripletCheckItem);

		toolsMenu.addSeparator();

		JMenuItem restoreTransitions = new JMenuItem("Restore Transitions");
		JMenuItem deleteTransitions = new JMenuItem("Delete Dashed");

		restoreTransitions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.restore();
			}
		});

		deleteTransitions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.deleteDashed();
			}
		});

		toolsMenu.add(restoreTransitions);
		toolsMenu.add(deleteTransitions);

		toolsMenu.addSeparator();

		abstractByLabel = new JMenuItem("Abstract By Label");
		abstractByFormula = new JMenuItem("Abstract By Formula");
		abstractByCTLFormulae = new JMenuItem("Abstract By CTL Formulae");
		abstractToFull = new JMenuItem("Concretize Repair");
		resetAbstract = new JMenuItem("Cancel Abstraction");

		toolsMenu.add(abstractByLabel);
		toolsMenu.add(abstractByFormula);
		toolsMenu.add(abstractByCTLFormulae);
		toolsMenu.add(abstractToFull);
		toolsMenu.add(resetAbstract);

		abstractByLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.abstractByLabel();

			}
		});

		abstractByFormula.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.abstractByFormula();

			}
		});
 
		abstractByCTLFormulae.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(eshmun.getCurrentGraphPanel().isTableau()) return;
				AbstractionCTLPanel CTLFormulae = new AbstractionCTLPanel();
				CTLFormulae.setResizable(false);
			}
		});
		
 
		abstractToFull.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.concretize();
			}
		});

		resetAbstract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;
				eshmun.resetAbstraction();
			}
		});

		toolsMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent arg0) {
				boolean isAbstracted = eshmun.isAbstracted();

				abstractByLabel.setEnabled(!isAbstracted);
				abstractByFormula.setEnabled(!isAbstracted);
				abstractToFull.setEnabled(isAbstracted);
				resetAbstract.setEnabled(isAbstracted);
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {

			}

			@Override
			public void menuCanceled(MenuEvent arg0) {

			}
		});

		toolsMenu.addSeparator();

		JMenuItem generalizedCheck = new JMenuItem("Global Model Check");
		toolsMenu.add(generalizedCheck);

		generalizedCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eshmun.stateByStateModelCheck();
			}
		});

		JMenuItem unsatCore = new JMenuItem("Minimal Unsat Core");
		toolsMenu.add(unsatCore);

		unsatCore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eshmun.unsatCore();
			}
		});

		JMenuItem decisionProcedure = new JMenuItem("Decision Procedure");
		toolsMenu.add(decisionProcedure);

		decisionProcedure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DecisionProcedureDialog diag = new DecisionProcedureDialog(eshmun);
				diag.setVisible(true);
			}
		});

		JMenuItem synchSkel = new JMenuItem("To Synchronization Skeleton");
		toolsMenu.add(synchSkel);

		synchSkel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;

				try {
					eshmun.toSynchSkeletons();
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JMenuItem textProgSkel = new JMenuItem("To Program (Text)");
		toolsMenu.add(textProgSkel);

		textProgSkel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (eshmun.getCurrentGraphPanel().isTableau())
					return;

				try {
					String definition = eshmun.toProgramText();

					JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
					int dec = chooser.showSaveDialog(null);
					if (dec == JFileChooser.APPROVE_OPTION) {
						Eshmun.last_Directory = chooser.getSelectedFile().getParent();
						try {
							File f = chooser.getSelectedFile().getAbsoluteFile();
							if (f.exists())
								f.delete();

							f.createNewFile();
							FileWriter fw = new FileWriter(f);
							fw.write(definition);
							fw.close();

							eshmun.writeMessage("Exported Successfully!", false);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Error Saving File: " + e1.toString(), "Error",
									JOptionPane.ERROR_MESSAGE);
							eshmun.writeMessage("Error!", true);
						}
					} else if (dec == JFileChooser.ERROR_OPTION) {
						JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
						eshmun.writeMessage("Error!", true);
					}

				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// HELP MENU

		JMenuItem helpItem = new JMenuItem("Help");
		JMenuItem aboutItem = new JMenuItem("About Eshmun");

		helpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new HelpDialog().setVisible(true);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not locate help files", "Error",
							JOptionPane.ERROR_MESSAGE);
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
		add(formulaMenu);
		add(toolsMenu);
		add(helpMenu);
	}

	public String loadFormula() {
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showOpenDialog(null);
		if (dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				FileInputStream fin = new FileInputStream(chooser.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);

				String sobj = (String) ois.readObject();

				ois.close();
				fin.close();

				eshmun.writeMessage("Formula Loaded!", false);

				return sobj;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Loading Formula: " + e1.toString(), "Error",
						JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(),
						"Saved Formula has Unrecognizable Format: " + e1.toString(), "Error",
						JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassCastException e2) {
				JOptionPane.showMessageDialog(this.getRootPane(),
						"Saved Formula has Unrecognizable Format: " + e2.toString(), "Error",
						JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error",
					JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}

		return null;
	}

	public void storeFormula(String formula) {
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showSaveDialog(null);
		if (dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				File f = chooser.getSelectedFile();
				if (f.exists())
					f.delete();

				f.createNewFile();

				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				oos.writeObject(formula);

				oos.close();
				fos.close();

				eshmun.writeMessage("Fromula Stored!", false);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Storing Formula: " + e1.toString(), "Error",
						JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error",
					JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}
	}
}
