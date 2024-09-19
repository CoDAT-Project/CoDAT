package eshmun.gui.synchronizationskeletons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eshmun.Eshmun;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.bars.EshmunRefinementBar;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.bars.SynchronizationMenuBar;
import eshmun.gui.synchronizationskeletons.bars.SynchronizationZoomToolBar;
import eshmun.gui.utils.EshmunWindowListener;
import eshmun.gui.utils.models.skeleton.SkeletonSaveObject;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;
import eshmun.structures.skeletons.FromSkeletonConverter;

/**
 * This is the frame containing Eshmun's interface for Synchronization Skeletons.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SynchronizationSkeletonFrame extends JFrame {

	/**
	 * Auto generated serial UID. 
	 */
	private static final long serialVersionUID = 5615853251839223652L;

	/**
	 * The Panel responsible for drawing the synchronization skeleton.
	 */
	private SynchronizationSkeletonPanel skeletonPanel;
	
	/**
	 * The base name of the structure for generation.
	 */
	private String baseName;
	
	/**
	 * The refinement tab.
	 */
	private EshmunRefinementBar refinmentTab;
	
	/**
	 * Creates a new empty Synchronization Skeleton Interface.
	 */
	public SynchronizationSkeletonFrame() {		
		setTitle("Eshmun: Synchronization Skeletons");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new EshmunWindowListener());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(screenSize.width - 10, screenSize.height - 50));
		skeletonPanel = new SynchronizationSkeletonPanel(scroll, this);
		
		SynchronizationMenuBar bar = new SynchronizationMenuBar(skeletonPanel);
		setJMenuBar(bar);
		
		JPanel toolBarsPanel = new JPanel(new GridLayout(1, 2));
		
		SynchronizationZoomToolBar zoomToolbar = new SynchronizationZoomToolBar(skeletonPanel);
		toolBarsPanel.add(zoomToolbar, BorderLayout.WEST);
		
		// REFINMENT TAB
		refinmentTab = new EshmunRefinementBar(this);
		toolBarsPanel.add(refinmentTab, BorderLayout.NORTH);
		
		add(toolBarsPanel, BorderLayout.NORTH);
		
		skeletonPanel.resetUndoManager();
		add(scroll);
	}
	
	/**
	 * Creates a new Synchronization Skeleton Interface with the given states and transitions.
	 * 
	 * @param states a list of states.
	 * @param transitions a list of transitions.
	 * @param baseName the base name of the structure for generation.
	 */
	public SynchronizationSkeletonFrame(ArrayList<SkeletonState> states, ArrayList<SkeletonTransition> transitions, String baseName) {
		this();
		
		this.baseName = baseName;
		
		if(states != null) skeletonPanel.setStates(states);
		if(transitions != null) skeletonPanel.setTransitions(transitions);
		
		skeletonPanel.resetUndoManager();
		skeletonPanel.autoFormat();
		skeletonPanel.resetUndoManager();
	}
	
	/**
	 * Gets the base name.
	 * @return the base name.
	 */
	public String getBaseName() {
		return baseName;
	}
	
	/**
	 * Sets the base name.
	 * @param baseName the base name.
	 */
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	
	/**
	 * Creates a new Synchronization Skeleton then loads the given save object into it.
	 * 
	 * @param saveable the save object containing the synchronization skeletons.
	 */
	public SynchronizationSkeletonFrame(final Saveable saveable) {
		this();
		
		if(saveable.getStructureType() != StructureType.SynchronizationSkeleton) {
			java.awt.EventQueue.invokeLater(new Runnable() {
			    public void run() {
			    	Eshmun eshmun = new Eshmun(saveable.getStructureType());
			    	eshmun.load(saveable);
			    	eshmun.setVisible(true);
			    	eshmun.setRefinementCurrent(refinmentTab.getCurrent());
			    }
			});
			
			dispose();
		} else {
			SkeletonSaveObject obj = (SkeletonSaveObject) saveable;
			skeletonPanel.load(obj);
		}
	}
	
	/**
	 * Generates an equivalent Single Kripke Structure from the current Synchronization Skeleton.
	 * @param name the name of the generated structure.
	 */
	public void generateSingleKripke(String name) {
		String def = "";
		
		ArrayList<SkeletonState> states = skeletonPanel.copyStates();
		skeletonPanel.copyTransitions(); //This copies transitions into already copied states above.
		
		FromSkeletonConverter converter = new FromSkeletonConverter(baseName, states);
		if(converter.convertToSingleKripke())
			def = converter.getStructureDefinition();
		
		Eshmun eshmun = new Eshmun(StructureType.Kripke);
		eshmun.loadDefinition(def, StructureType.Kripke);
		eshmun.setVisible(true);
		eshmun.setRefinementCurrent(refinmentTab.getCurrent());
		
		dispose();
	}
			
	/**
	 * Generates an equivalent Concurrent-Pair Kripke Structure from the current Synchronization Skeleton.
	 * @param name the base name of the generated structure, each pair structure will have that pair suffixed
	 * with a counter and process-indices.
	 */
	public void generateConcurrentKripke(String name) {
		String def = "";
		
		ArrayList<SkeletonState> states = skeletonPanel.copyStates();
		skeletonPanel.copyTransitions(); //This copies transitions into already copied states above.
		
		FromSkeletonConverter converter = new FromSkeletonConverter(baseName, states);
		if(converter.convertToMultipKripke())
			def = converter.getStructureDefinition();
		
		System.out.println(def);
		Eshmun eshmun = new Eshmun(StructureType.MultiKripke);
		eshmun.loadDefinition(def, StructureType.MultiKripke);
		eshmun.setVisible(true);
		eshmun.setRefinementCurrent(refinmentTab.getCurrent());
		
		dispose();
	}
	
	/**
	 * Gets the saveable structure.
	 * @return saveable structure.
	 */
	public Saveable getSaveable() {
		return skeletonPanel.getSaveable();
	}
	
	/**
	 * Sets the current structure in the refinement
	 * @param current the current index of the structure in refinement. 
	 */
	public void setRefinementCurrent(int current) {
		refinmentTab.setCurrent(current);
	}
	
	/**
	 * Gets the skeletonPanel.
	 * @return the skeleton panel.
	 */
	public SynchronizationSkeletonPanel getSkeletonPanel() {
		return skeletonPanel;
	}
}


























