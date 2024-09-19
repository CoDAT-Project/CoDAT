package eshmun.gui.kripke.bars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import eshmun.Eshmun;
import eshmun.gui.kripke.utils.RefinementManager;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonFrame;
import eshmun.gui.utils.models.multikripke.MultiSaveObject;
import eshmun.gui.utils.models.vanillakripke.SaveObject;

public class EshmunRefinementBar extends JToolBar {

	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -6759687059386296271L;
	
	/**
	 * Origin.
	 */
	private JFrame origin;
	
	/*
	 * Self describing buttons.
	 */
	private JButton previous;
	private JButton next;
	private JButton create;
	private JButton export;
	private JButton history;
	private JButton save;
	
	/**
	 * The current index.
	 */
	private int current;
	
	/**
	 * The history overlay/toolbar instance.
	 */
	private RefinementHistoryOverlay historyBar;
		
	/**
	 * Creates a new Bar for the given origin.
	 * @param origin the origin frame.
	 */
	public EshmunRefinementBar(JFrame origin) {
		super("Refinement Tool Bar", JToolBar.HORIZONTAL);
		
		this.origin = origin;
		this.current = 0;
		
		//GENERAL FORMATTING
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setPreferredSize(new Dimension(200, 30));
		
		previous = new JButton();
		next = new JButton();
		create = new JButton();
		export = new JButton();
		history = new JButton();
		save = new JButton();
		
		add(new JLabel("Refinement: "));
		
		add(create);
		add(save);
		add(previous);
		add(next);
		add(export);
		add(history);
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Saveable sav = null;
				
				if(EshmunRefinementBar.this.origin instanceof SynchronizationSkeletonFrame) {
					sav = ((SynchronizationSkeletonFrame) EshmunRefinementBar.this.origin).getSaveable();
				} else {
					sav = ((Eshmun) EshmunRefinementBar.this.origin).getSaveableObject();
				}
				
				String name = RefinementManager.current.getNames()[EshmunRefinementBar.this.current];
				if(name == null)
					name = JOptionPane.showInputDialog(null, "Please Enter a name for this step in the refinement");
				
				if(name != null)
					RefinementManager.current.saveToIndex(sav, name, EshmunRefinementBar.this.current);
				
				historyBar.update();
			}
		});
		
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				//Get the Saveable of the current structure.
				Saveable sav = null;
				
				if(EshmunRefinementBar.this.origin instanceof SynchronizationSkeletonFrame) {
					sav = ((SynchronizationSkeletonFrame) EshmunRefinementBar.this.origin).getSaveable();
				} else {
					sav = ((Eshmun) EshmunRefinementBar.this.origin).getSaveableObject();
				}
				
				//Create a new step, carry the current structure to it and save it without a name.
				RefinementManager.current.createAtIndex(++current);
				RefinementManager.current.saveToIndex(sav, null, current);
				enableOrDisableButtons();
				
				historyBar.update();
			}
		});
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						RefinementManager.current.goToIndex(EshmunRefinementBar.this.current + 1, EshmunRefinementBar.this.origin.getSize());
					}
				});
				
				EshmunRefinementBar.this.origin.dispose();
			}
		});
		
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						RefinementManager.current.goToIndex(EshmunRefinementBar.this.current - 1, EshmunRefinementBar.this.origin.getSize());
					}
				});
				
				EshmunRefinementBar.this.origin.dispose();
			}
		});
		
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
				int dec = chooser.showSaveDialog(null);
				if(dec == JFileChooser.APPROVE_OPTION) {
					Eshmun.last_Directory = chooser.getSelectedFile().getParent();
					try {
						String path = chooser.getSelectedFile().getAbsolutePath();
						RefinementManager.current.save(path);
						
						JOptionPane.showMessageDialog(null, "Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (dec == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null, "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
				}				
			}
		});
		
		history.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {								
				if(historyBar != null) {
					historyBar.setVisible(!historyBar.isVisible());
					historyBar.update();
				} else {
					historyBar.setVisible(true);
					historyBar.update();
				}
			}
		});
		
		setIcons();
		enableOrDisableButtons();
		
		// History bar
		historyBar = new RefinementHistoryOverlay(origin, this);
		origin.add(historyBar, BorderLayout.EAST);
		historyBar.setVisible(RefinementHistoryOverlay.wasVisible());
	}
	
	/**
	 * Sets the current location.
	 * @param current the current structure.
	 */
	public void setCurrent(int current) {
		this.current = current;
		enableOrDisableButtons();
	}
	
	/**
	 * Gets the current location.
	 * @return the current location.
	 */
	public int getCurrent() {
		return current;
	}
	
	/**
	 * Enables or disables the next and previous depending on current and size.
	 */
	public void enableOrDisableButtons() {
		int size = RefinementManager.current.getSize();
		
		previous.setEnabled(false);
		next.setEnabled(false);
		
		if(current > 0) 
			previous.setEnabled(true);
		
		if(current < size - 1)
			next.setEnabled(true);
	}
	
	/**
	 * Sets the buttons icons loaded from files.
	 */
	private void setFileIcons() {
		ImageIcon prev = new ImageIcon(getClass().getResource("/left.png"));
	    Image tmp = prev.getImage().getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);  
		previous.setIcon(new ImageIcon(tmp));
		
		prev = new ImageIcon(getClass().getResource("/right.png"));
		
	    tmp = prev.getImage().getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);  
		next.setIcon(new ImageIcon(tmp));
		
	 
		prev= new ImageIcon(getClass().getResource("/save.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		save.setIcon(new ImageIcon(tmp));

		
		prev = new ImageIcon(getClass().getResource("/export.png"));
	 
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		export.setIcon(new ImageIcon(tmp));

		
		prev = new ImageIcon(getClass().getResource("/history.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		history.setIcon(new ImageIcon(tmp));

		
		prev = new ImageIcon(getClass().getResource("/new.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		create.setIcon(new ImageIcon(tmp));
	}
	
	/**
	 * Sets the buttons icons, attempt loading from JAR, if it failed load from files.
	 */
	private void setIcons() {
		URL url = ClassLoader.getSystemClassLoader().getResource("icons/left.png");
		if(url == null) {
			setFileIcons();
			return;
		}
			
		ImageIcon prev = new ImageIcon(url);
	    Image tmp = prev.getImage().getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);  
		previous.setIcon(new ImageIcon(tmp));
		
		url = ClassLoader.getSystemClassLoader().getResource("icons/right.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);  
		next.setIcon(new ImageIcon(tmp));
		
		url = ClassLoader.getSystemClassLoader().getResource("icons/save.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		save.setIcon(new ImageIcon(tmp));

		url = ClassLoader.getSystemClassLoader().getResource("icons/export.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		export.setIcon(new ImageIcon(tmp));

		url = ClassLoader.getSystemClassLoader().getResource("icons/history.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		history.setIcon(new ImageIcon(tmp));

		url = ClassLoader.getSystemClassLoader().getResource("icons/new.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		create.setIcon(new ImageIcon(tmp));
	}
	
	/**
	 * Gets the origin.
	 * @return the origin.
	 */
	public JFrame getOrigin() {
		return origin;

	}
	
	/**
	 * Prints the history to the screen, debugging.
	 */
	public void printHistory() {
		System.out.println("Current: "+current);
		for(Saveable s : RefinementManager.current.getStructures()) {
			if(s == null) continue;
			System.out.print(s.getStructureType()+":  ");
			switch(s.getStructureType()) {
				case Kripke: 
					System.out.println(((SaveObject) s).getStructureName());
					break;
				case MultiKripke:
					System.out.println(((MultiSaveObject) s).getSaveObjects()[0].getStructureName());
					break;
				case SynchronizationSkeleton: 
					System.out.println(((eshmun.gui.utils.models.skeleton.SkeletonSaveObject) s).getBaseName());
					break;
					
				default: System.out.println("nothing");
			}
		}
	}
}
