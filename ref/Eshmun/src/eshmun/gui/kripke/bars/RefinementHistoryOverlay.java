package eshmun.gui.kripke.bars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import com.sun.java.swing.plaf.motif.MotifBorders.BevelBorder;

import eshmun.gui.kripke.utils.RefinementManager;
import eshmun.gui.kripke.utils.Saveable;

/**
 * This is an overlay in which the history of the refinement is displayed as a list.
 * Should not be resizable nor draggable, should be always on top and attached to the left of the screen.
 * 
 * @author kinan
 *
 */
public class RefinementHistoryOverlay extends JToolBar {
	/**
	 * Default version.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The width.
	 */
	private static int WIDTH = 250;
	
	/**
	 * If the history Overlay is visible.
	 */
	private static boolean VISIBLE = false;
	
	/**
	 * @return If the refinement history was visible.
	 */
	public static boolean wasVisible() {
		return VISIBLE;
	}
	
	/**
	 * The origin.
	 */
	private JFrame origin;
	
	/**
	 * The refinement bar.
	 */
	private EshmunRefinementBar refinement;
	
	/**
	 * The history list
	 */
	private JList<String> list;
	
	/**
	 * The Structures in the refinement.
	 */
	private Saveable[] structures;
	
	/**
	 * The names of the structures in the refinement.
	 */
	private String[] names;

	/**
	 * Creates a new overlay bar that is attached to the given frame.
	 * @param origin the frame to attach the overlay to.
	 * @param refinement the refinement.
	 */
	public RefinementHistoryOverlay(JFrame origin, EshmunRefinementBar refinement) {   
		super(VERTICAL);
		
        this.origin = origin;
        this.refinement = refinement;
        
        setFloatable(false);
        
        BevelBorder raised = new BevelBorder(true, Color.GRAY, Color.GRAY);
        setBorder(BorderFactory.createCompoundBorder(raised, BorderFactory.createEmptyBorder()));
        
        ImageIcon[] icons = icons();
        
        JPanel top = new JPanel();
        top.setPreferredSize(new Dimension(WIDTH, 60));
        top.add(new JLabel("<html><b>Refinement History &nbsp;&nbsp;&nbsp;&nbsp; </b></html>"), BorderLayout.WEST);

        JButton hide = new JButton();
        hide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RefinementHistoryOverlay.this.setVisible(false);				
			}
		});
        top.add(hide, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        
        list = new JList<String>();
        
        JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroller.setPreferredSize(new Dimension(WIDTH - 10, 500));
        add(listScroller, BorderLayout.CENTER);
        
        JPanel buttons = new JPanel();
        add(buttons, BorderLayout.SOUTH);
        
        JButton open = new JButton();
        JButton openWindow = new JButton();
        JButton delete = new JButton();
        JButton rename = new JButton();
        
        buttons.add(open);
        buttons.add(openWindow);
        buttons.add(delete);
        buttons.add(rename);
        
        hide.setIcon(icons[0]);
        open.setIcon(icons[1]);
        openWindow.setIcon(icons[2]);
        delete.setIcon(icons[3]);
        rename.setIcon(icons[4]);
        
        hide.setBorderPainted(false);
        open.setBorderPainted(false);
        openWindow.setBorderPainted(false);
        delete.setBorderPainted(false);
        rename.setBorderPainted(false);
        
        update();
        
        open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final int index = list.getSelectedIndex();
				
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						RefinementManager.current.goToIndex(index, RefinementHistoryOverlay.this.origin.getSize());
					}
				});
				
				RefinementHistoryOverlay.this.origin.dispose();
			}
		});
        
        openWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final int index = list.getSelectedIndex();
				
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						RefinementManager.current.goToIndex(index, RefinementHistoryOverlay.this.origin.getSize());
					}
				});
				
				update();
			}
		});
        
        delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				
				RefinementManager.current.removeAtIndex(index);
				update();
			}
		});
        
        rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(null, "Please Enter a name for this step in the refinement");
				if(name != null) {
					RefinementManager.current.setNameAtIndex(list.getSelectedIndex(), name);
					update();
				}
			}
		});
	}
	
	/**
	 * Updates the content of the history overlay.
	 */
	public void update() {
		structures = RefinementManager.current.getStructures();
		names = RefinementManager.current.getNames();
		
		DefaultListModel<String> model = new DefaultListModel<String>();		
		for(int i = 0; i < structures.length; i++) {
			if(structures[i] != null && names[i] != null) 
				model.addElement(names[i] + " : " + structures[i].getStructureType());
			else
				model.addElement("<new structure> : new ..");
		}		
		
		list.setModel(model);
		list.setSelectedIndex(refinement.getCurrent());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
	}
	
	/**
	 * Loads the icons from files.
	 * @return the button icons.
	 */
	private ImageIcon[] fileIcons() {
		ImageIcon[] icons = new ImageIcon[5];
				
		ImageIcon prev = new ImageIcon(getClass().getResource("/cancel.png"));
	    Image tmp = prev.getImage().getScaledInstance(19, 19,  java.awt.Image.SCALE_SMOOTH);
	    icons[0] = new ImageIcon(tmp);
		
		prev = new ImageIcon(getClass().getResource("/open.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[1] = new ImageIcon(tmp);
	    
		prev = new ImageIcon(getClass().getResource("/new_window.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[2] = new ImageIcon(tmp);
		
		prev = new ImageIcon(getClass().getResource("/trash.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[3] = new ImageIcon(tmp);
		
		prev = new ImageIcon(getClass().getResource("/rename.png"));
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);
	    icons[4] = new ImageIcon(tmp);
	    
	    return icons;
	}
	
	/**
	 * Loads the icons from JAR, if it failed, load from files.
	 * @return the button icons.
	 */
	private ImageIcon[] icons() {
		URL url = ClassLoader.getSystemClassLoader().getResource("icons/cancel.png");
		if(url == null) {
			return fileIcons();
		}
			
		ImageIcon[] icons = new ImageIcon[5];

		ImageIcon prev = new ImageIcon(url);
	    Image tmp = prev.getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);  
	    icons[0] = new ImageIcon(tmp);
		
		url = ClassLoader.getSystemClassLoader().getResource("icons/open.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[1] = new ImageIcon(tmp);
		
		url = ClassLoader.getSystemClassLoader().getResource("icons/new_window.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[2] = new ImageIcon(tmp);

		url = ClassLoader.getSystemClassLoader().getResource("icons/trash.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[3] = new ImageIcon(tmp);

		url = ClassLoader.getSystemClassLoader().getResource("icons/rename.png");
		prev = new ImageIcon(url);
	    tmp = prev.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
	    icons[4] = new ImageIcon(tmp);
	    
	    return icons;
	}
	
	/**
	 * Sets the visibility status to the static variable VISIBLE.
	 */
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		VISIBLE = aFlag;
	}
}
