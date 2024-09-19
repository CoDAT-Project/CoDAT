package eshmun;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.TimeoutException;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.sun.org.apache.bcel.internal.generic.NEW;

import t7.Terminal;
import eshmun.tripleprover.Z3SolverHelper;
import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.parser.LogicParser;
import eshmun.expression.visitor.visitors.GeneralVariableListerVisitor;
import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.kripke.GraphPanelContainer;
import eshmun.gui.kripke.GraphPanelFactory;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.bars.EshmunMenuBar;
import eshmun.gui.kripke.bars.EshmunRefinementBar;
import eshmun.gui.kripke.bars.EshmunSearchToolBar;
import eshmun.gui.kripke.bars.EshmunZoomToolBar;
import eshmun.gui.kripke.dialogs.EnterFormulaDialog;
import eshmun.gui.kripke.dialogs.CustomizeColorDialog;
import eshmun.gui.kripke.dialogs.StatsDialog;
import eshmun.gui.kripke.utils.AbstractionCTLPanel;
import eshmun.gui.kripke.utils.LogicTextPane;
import eshmun.gui.kripke.utils.RefinementManager;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonFrame;
import eshmun.gui.utils.EshmunWindowListener;

import eshmun.gui.utils.models.multikripke.MultiSaveObject;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.modelrepair.ModelRepairer;
import eshmun.modelrepair.unsat.UnsatCoreSolver;
import eshmun.skeletontextrepresentation.KripkeToProgramConverter;
import eshmun.skeletontextrepresentation.infinitespace.InfiniteProgramToKripkeConverter;
import eshmun.skeletontextrepresentation.infinitespace.TransitionTripleChecker;
import eshmun.skeletontextrepresentation.infinitespace.TransitionTripleChecker.TransitionValidationResult;
import eshmun.structures.AbstractState;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.skeletons.ToSkeletonConverter;

/**
 * The Main Frame, contains the application's entry point.
 * 
 * @author Choukri Soueidi 
 * @since ....
 * Prior authors: Jad Saklawi, Mouhammad Sakr, Kinan Dak Al Bab, Emile Shartouny
 */
public class Eshmun extends JFrame {
	
	//Instance vars that store "constants"
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 5555485758086511529L;

	/**
	 * the minimum difference between two random colors in each of their respective
	 * RGB component.
	 */
	private static final int COLOR_DIFFERENTIATION_FACTOR = 40;

	/**
	 * the factor by which the canvas (and images) are enlarged when exported.
	 */
	public static final double EXPORT_IMAGE_SCALE_FACTOR = 2.0;
	
	//Instance vars that store local state

	/**
	 * The current running instance of the app.
	 */
	public static Eshmun eshmun;

	/**
	 * The last used directory.
	 */
	public static String last_Directory = "";

	
	//Instance vars that store flags
	
	/**
	 * Flag for printing helpful debugging messages.
	 */
	public static final boolean DEBUG_FLAG = false;

	/**
	 * Flag for printing statistics (number of states/transitions deleted etc)
	 */
	public static boolean ECHO_STATS = true;

	/**
	 * The directory for the MINIMAL-UNSAT-CORE Tool.
	 */
	private static String MUS_DIRECTORY = null;

	/**
	 * Entry Point of the application, By default Creates an application that deals
	 * with Standard Kripke Structure.
	 * 
	 * @param args
	 *            command line arguments (ignored).
	 */
	public static void main(String[] args) {

		// InfiniteProgramToKripkeConverter.parseProgram();

		try { // SET LOOK AND FEEL THEME
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			} else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
		} catch (Exception ex) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				System.out.println("Couldn't find look and feel");
			}
		}

		UIManager.put("Slider.paintValue", false); //?? BRDS: I can't find anything on this. ??*

		// Open either Eshmun or data definition language shell
		if (args.length > 0 && args[0].trim().equalsIgnoreCase("T7")) {  //if T7 given then start shell
			Terminal t = new Terminal(); // /src/eshmun/t7/Terminal.java
			t.start();
		} else {
			java.awt.EventQueue.invokeLater(new Runnable() {   //otherwise start single instance of the Eshmun object
				public void run() {
					new Eshmun(StructureType.Kripke).setVisible(true);
					eshmun.setExtendedState(eshmun.getExtendedState() | JFrame.MAXIMIZED_BOTH); // BRDS: Maximizes window.
				}
			});
		}
	}

	//Instance vars that store panels
	
	/**
	 * GraphPanelContainer, The main component.
	 */
	private GraphPanelContainer graphPanelContainer;
	// src/gui/kripke/GraphPanelContainer.java

	/**
	 * Zoom toolbar, in the toolbar section of the screen.
	 */
	private EshmunZoomToolBar zoomToolBar; // src/eshmun/gui/kripke/bars/EshmunZoomToolBar.java

	/**
	 * Search tool bar, also traverses repairs.
	 */
	private EshmunSearchToolBar searchToolBar; // src/eshmun/gui/kripke/bars/EshmunSearchToolBar.java

	/**
	 * Refinment Tab.
	 */
	private EshmunRefinementBar refinmentTab; // src/eshmun/gui/kripke/bars/EshmunRefinementBar.java

	/**
	 * Information label, displays information about selected states and
	 * transitions.
	 */
	private JLabel infoLabel;

	/**
	 * Message Label, displays the status and error messages.
	 */
	private JLabel messageLabel;

	/**
	 * Logic Pane for the specifications
	 */
	private LogicTextPane specificationsPane; // src/eshmun/gui/kripke/utils/LogicTextPane.java

	/**
	 * Logic Pane for the extra structure formula pane.
	 */
	private LogicTextPane structurePane; // src/eshmun/gui/kripke/utils/LogicTextPane.java

	/**
	 * ScrollPane in which the specification formula pane reside.
	 */
	private JScrollPane specificationsScroll;

	/**
	 * ScrollPane in which the structure extra formula pane reside.
	 */
	private JScrollPane structureScroll;

	/**
	 * Panel containing the info and message labels.
	 */
	private JPanel infoAndMessagePanel;

	/**
	 * Panel containing the message label.
	 */
	private JPanel messagePanel;

	/**
	 * Panel Containing everything bellow the GraphPanel.
	 */
	private JPanel bottomPanel;

	
	
	//Instance vars that store major state information
	
	/**
	 * The current structureType being served.
	 */
	private StructureType structureType;

	/**
	 * The model repairer.
	 */
	private ModelRepairer repairer;

	/**
	 * Stores all previous repairs.
	 */
	private ArrayList<Collection<String>> oldRepairs;

	/**
	 * Stores the index of current repair.
	 */
	private int currentRepair;

	/**
	 * Whether the current model is abstracted or not.
	 */
	private boolean isAbstracted;

	/**
	 * Whether the model is colored by process (for transitions).
	 */
	private boolean transitionColoredByProcess;

	/**
	 * Whether the model is colored by process (for state's labels).
	 */
	private boolean labelColoredByProcess;

	/**
	 * Whether the model's actions are colored.
	 */
	private boolean colorActions;

	/**
	 * The color mode currently being used (screen or print).
	 */
	private ColorMode colorMode;

	/**
	 * Flags whether or not to sync based on action names.
	 * 
	 * @see eshmun.structures.kripke.MultiKripkeStructure#actionSynchronizationFormula()
	 */
	private boolean syncOverActions;

	/**
	 * Flags whether or not to sync on process labels combined with from and to
	 * states labels.
	 * 
	 * @see eshmun.structures.kripke.MultiKripkeStructure#processSynchronizationFormula()
	 */
	private boolean syncOverProcessIndices;

	/*
	 * GUI CONSTRUCTOR
	 */

	/**
	 * Constructs a new instance of the application for a given structure type.
	 * 
	 * <p>
	 * This entails constructing the Menu on top, the GraphPanelContainer (and its
	 * contents), The LogicTextPanes (both Specifications and Structure), and the
	 * Message, information labels.
	 * </p>
	 * 
	 * @param structureType
	 *            the Type of structure the application is going to serve.
	 */
	public Eshmun(StructureType structureType) {
		super(); //BRDS: calls superclass constructor (parent class) without an argument

		eshmun = this;

		resetProcessColors();
		resetActionColors();

		//[PCA 25/9/2019] set initial parameter values 
		if (structureType != StructureType.Kripke && structureType != StructureType.MultiKripke
				&& structureType != StructureType.InfiniteStateKripke)
			this.structureType = StructureType.Kripke;
		else
			this.structureType = structureType;

		this.isAbstracted = false;
		this.transitionColoredByProcess = true;
		this.labelColoredByProcess = true;
		this.colorActions = true;
		this.colorMode = ColorMode.ScreenMode;
		this.syncOverActions = true;
		this.syncOverProcessIndices = true;

		// Initialize info and status label
		infoLabel = new JLabel("");
		messageLabel = new JLabel("<html><b><small>Ready</small></b>");

		/*
		 * GENERAL FORMAT
		 */
		setTitle("Eshmun, Automated Repairer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new EshmunWindowListener());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		/*
		 * GRAPH PANEL
		 */
		// CREATE NEW DRAWING PANEL IN SCROLL PANE
		GraphPanelFactory factory = new GraphPanelFactory(this.structureType, this);
		// /src/gui/kripke/GraphPanelFactory.java
		graphPanelContainer = factory.makeGraphPanel(screenSize.width - 20, screenSize.height - 335);

		JComponent component = graphPanelContainer.getGraphPanelComponent();

		/*
		 * MENU AND TOOL BARS
		 */
		// MENU BAR
		EshmunMenuBar menuBar = new EshmunMenuBar(this); // /src/eshmun/gui/kripke/bars/EshmunMenuBar.java
		setJMenuBar(menuBar);

		// TOOLBARS
		JPanel toolBarsPanel = new JPanel(new GridLayout(1, 3));

		// ZOOM TOOL BAR
		zoomToolBar = new EshmunZoomToolBar(this); // /src/eshmun/gui/kripke/bars/EshmunZoomToolBar.java
		toolBarsPanel.add(zoomToolBar);

		// REFINMENT TAB
		refinmentTab = new EshmunRefinementBar(this); // /src/eshmun/gui/kripke/bars/EshmunRefinementBar.java
		toolBarsPanel.add(refinmentTab);

		// STATE SEARCH TOOL BAR
		searchToolBar = new EshmunSearchToolBar(this); // /src/eshmun/gui/kripke/bars/EshmunSearchToolBar.java
		searchToolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBarsPanel.add(searchToolBar);

		add(toolBarsPanel, BorderLayout.NORTH);

		/*
		 * BOTTOM PART OF SCREEN
		 */
		// SOUTH PANEL CONTAINING FORMULA, INFO BAR, STATUS BAR
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// FORMULA PANEL
		specificationsPane = new LogicTextPane("CTL Specifications", this, true);
		// /src/eshmun/gui/kripke/utils/LogicTextPane.java
		specificationsScroll = new JScrollPane(specificationsPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		specificationsPane.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				String specs = specificationsPane.getText();
				graphPanelContainer.setSpecifications(specs);
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		bottomPanel.add(specificationsScroll, BorderLayout.NORTH);

		structurePane = new LogicTextPane("Structural Formula", this, false);
		// /src/eshmun/gui/kripke/utils/LogicTextPane.java
		structurePane.setPreferredSize(new Dimension(50, 50));

		structureScroll = new JScrollPane(structurePane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		bottomPanel.add(structureScroll, BorderLayout.NORTH);

		// INFO BAR
		infoAndMessagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoAndMessagePanel.add(infoLabel);

		// STATUS BAR
		messagePanel = new JPanel(new BorderLayout());

		JPanel statusInnerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		statusInnerPanel.setPreferredSize(new Dimension(200 - 5, 20));
		statusInnerPanel.add(messageLabel);

		messagePanel.add(statusInnerPanel, BorderLayout.SOUTH);

		infoAndMessagePanel.add(messagePanel, BorderLayout.SOUTH);
		bottomPanel.add(infoAndMessagePanel, BorderLayout.SOUTH);

		resizeBottomPanel(screenSize, false, 0);

		// HANDLERS FOR BOTTOM PANEL
		getRootPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeBottomPanel(e.getComponent().getSize(), false, 0);
			}
		});

		addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent arg0) {
				if ((arg0.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
					resizeBottomPanel(Toolkit.getDefaultToolkit().getScreenSize(), false, 0);
				} else {
					resizeBottomPanel(arg0.getComponent().getSize(), false, 0);
				}
			}
		});

		specificationsScroll.setPreferredSize(new Dimension(screenSize.width - 20, 60));
		structureScroll.setPreferredSize(new Dimension(screenSize.width - 20, 60));

		// SPLIT PANE BETWEEN BOTTOM_PANEL AND GRAPH_PANEL_CONTAINER
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(component);
		bottomPanel.setPreferredSize(new Dimension(screenSize.width - 10, 210));
		splitPane.setRightComponent(bottomPanel);
		splitPane.setResizeWeight(1);

		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				int val = ((JComponent) evt.getSource()).getHeight() + 10 - (Integer) evt.getNewValue();
				if (val < 40) {
					val = ((JComponent) evt.getSource()).getHeight() + 10 - 40;
					((JSplitPane) evt.getSource()).setDividerLocation(val);
				} else {
					resizeBottomPanel(((JComponent) evt.getSource()).getSize(), true, val);
				}
			}
		});

		add(splitPane);
	} //END OF THE CONSTRUCTOR

	/**
	 * Resize the bottom panel with the frame resize.
	 * 
	 * @param size
	 *            the new size of the frame.
	 * @param resizeHeight
	 *            flags whether the resize is Height and width wise, or just width
	 *            wise.
	 * @param extra
	 *            extra height for the panel in pixels (when the splitter is moved).
	 */
	private void resizeBottomPanel(Dimension size, boolean resizeHeight, int extra) {
		int formulaHeight = specificationsScroll.getHeight();
		int extraHeight = structureScroll.getHeight();

		if (resizeHeight) {
			extra -= 100;
			formulaHeight = (extra * 2) / 3;
			extraHeight = extra / 3;
		}

		bottomPanel.setPreferredSize(new Dimension(size.width - 8, bottomPanel.getHeight())); // 180

		specificationsPane.setPreferredSize(new Dimension(size.width - 8, 1000));
		specificationsScroll.setPreferredSize(new Dimension(size.width - 8, formulaHeight));

		structurePane.setPreferredSize(new Dimension(size.width - 8, 700));
		structureScroll.setPreferredSize(new Dimension(size.width - 8, extraHeight));

		infoAndMessagePanel.setPreferredSize(new Dimension(size.width - 8, 80));

		infoLabel.setPreferredSize(new Dimension(size.width - 250 - 8 - 10, 60));
		infoLabel.setMaximumSize(new Dimension(size.width - 250 - 8 - 10, 60));
		messagePanel.setPreferredSize(new Dimension(250, 60));

	}

	
	//BUNCH OF SIMPLE GETTER/SETTER METHODS
	
	/**
	 * @param syncOverActions
	 *            Set whether or not to sync based on action names.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#actionSynchronizationFormula()
	 */
	public void setSyncOverActions(boolean syncOverActions) {
		repairer = null; // Cannot use the next repair feature, must start over.
		this.syncOverActions = syncOverActions;
	}

	/**
	 * @param syncOverProcessIndices
	 *            Set whether or not to sync on process labels combined with labels.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#processSynchronizationFormula()
	 */
	public void setSyncOverProcessIndices(boolean syncOverProcessIndices) {
		repairer = null; // Cannot use the next repair feature, must start over.
		this.syncOverProcessIndices = syncOverProcessIndices;
	}

	/**
	 * @return Whether or not are currently syncing based on action names.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#actionSynchronizationFormula()
	 */
	public boolean syncOverActions() {
		return this.syncOverActions;
	}

	/**
	 * @return Whether or not are currently syncing on process labels combined with from and to
	 *         states labels.
	 * @see eshmun.structures.kripke.MultiKripkeStructure#processSynchronizationFormula()
	 */
	public boolean syncOverProcessIndices() {
		return this.syncOverProcessIndices;
	}

	/**
	 * Gets the zoom toolbar.
	 * 
	 * @return the zoom toolbar.
	 */
	public EshmunZoomToolBar getZoomToolBar() {
		return zoomToolBar;
	}

	/**
	 * Sets the specifications formula to the given formula.
	 * 
	 * @param formula
	 *            new specification formula.
	 */
	public void setSpecificationFormula(String formula) {
		if (specificationsPane == null)
			return;

		specificationsPane.requestFocusInWindow(); //set input focus to spec pane
		specificationsPane.setText(formula);       //set spec pane content to "formula"
	}

	/**
	 * Gets the specification formula as a String.
	 * 
	 * @return the specification formula as a String.
	 */
	public String getSpecificationFormula() {
		if (specificationsPane == null)   //if no spec pane,  return empty string
			return "";

		return specificationsPane.getText();  //otherwise, get contents of spec pane and return
	}

	/**
	 * Gets the specifications formula as an AbstractExpression.
	 * 
	 * @return the specification formula as an expression.
	 */
	public AbstractExpression getSpecificationExpression() { // /src/eshmun/expression/AbstractExpression.java
		String formula = specificationsPane.getText(); //get the spec formula

		LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java
		return parser.parse(formula);            //parse it into an abstract syntax tree
	}

	/**
	 * Sets the Structure formula to the given formula.
	 * 
	 * @param formula
	 *            new Structure formula.
	 */
	public void setStructureFormula(String formula) {
		structurePane.requestFocusInWindow();   //set input focus to structure pane
		structurePane.setText(formula);         //set structure formula to "formula"
	}

	/**
	 * Gets the Structure formula as a String.
	 * 
	 * @return the Structure formula as a String.
	 */
	public String getStructureFormula() {
		return structurePane.getText();
	}

	/**
	 * Gets the Structure formula as an AbstractExpression.
	 * 
	 * @return the Structure formula as an expression.
	 */
	public AbstractExpression getStructureExpression() { // /src/eshmun/expression/AbstractExpression.java
		String formula = structurePane.getText(); //get contents of structure pane

		if (formula.trim().equals("Structural Formula") || formula.trim().equals(""))
			return null; //return null if there is no formula in the structure pane

		LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java
		return parser.parse(formula); //otherwise parse and return the formula
	}

	/**
	 * writes the given message to the message label.
	 * 
	 * @param message
	 *            the message to write.
	 * @param error
	 *            if true the message is an error, otherwise it is not.
	 */
	public void writeMessage(String message, boolean error, String... dialogMessage) {

		String messageToShow = "";
		if (error) {  //color error message in red
			messageToShow = "<html><b><span style='color: red; '>" + message + "</span></b></html>";

		} else {
			messageToShow = "<html><b>" + message + "</b></html>";
		}
		if (dialogMessage.length > 0) //bring up dialog box if needed
			JOptionPane.showMessageDialog(eshmun, dialogMessage);

		messageLabel.setText(messageToShow); //actuall;y display the message

	}

	/**
	 * writes the given information to the information label.
	 * 
	 * @param info
	 *            the info to write.
	 */
	public void writeInfo(String info) {
		infoLabel.setText(info);
	}

	/**
	 * Insert the given variableName into the structurePane in the caret position.
	 * 
	 * @param variableName
	 *            The variable to be inserted.
	 * @throws BadLocationException
	 *             if the loaction in the document of structure pane is out of
	 *             range.
	 */
	public void insertVariableIntoStructure(String variableName) throws BadLocationException {
		int position = structurePane.getCaretPosition();
		// String text = structurePane.getText();
		// text = text.substring(0, position) + variableName + text.substring(position);
		structurePane.getDocument().insertString(position, variableName, new SimpleAttributeSet());
		;
	}

	/**
	 * The graphPanelContainer used by this window (frame).
	 * 
	 * @return the graphPanelContainer.
	 */
	public GraphPanelContainer getGraphPanelContainer() {
		return graphPanelContainer;
	}

	/**
	 * The currently visible and in-use GraphPanel.
	 * 
	 * @return the current GraphPanel.
	 */
	public GraphPanel getCurrentGraphPanel() {
		return graphPanelContainer.getCurrentGraphPanel();
	}

	/**
	 * Gets the type of the structure currently being served.
	 * 
	 * @return the structure type.
	 */
	public StructureType getStructureType() {
		return structureType;
	}

	/**
	 * Routine to execute before exiting.
	 */
	public void exit() {
		for (WindowListener lst : getWindowListeners())
			if (lst instanceof EshmunWindowListener)
				lst.windowClosing(null);

		dispose();
	}

	/*
	 * SAVE AND LOAD
	 */

	/**
	 * Save the current Structure to the given file.
	 * 
	 * @param path
	 *            the path of the file.
	 * @throws IOException
	 *             if an error occurred while writing to the file.
	 */
	public void save(String path) throws IOException {
		SaveObject[] objs = graphPanelContainer.getSaveObjects(); //store current Eshmun state into objs

		if (graphPanelContainer.getStructureType() == StructureType.Kripke) {
			SaveObject ss = objs[0];  //set ss to the Kripke structure
			if (ss.getStructureName().equals("Infinite State Kripke Structure"))
				ss.structureName = "Kripke Structure (1, 2)";
			objs[0].save(path); //save Kripke structure in file given by "path"
		} else if (graphPanelContainer.getStructureType() == StructureType.MultiKripke) {
			MultiSaveObject mObj = new MultiSaveObject(objs); //MultiKripke 
			// src/eshmun/gui/utils/models/multikripke/MultiSaveObject.java
			mObj.save(path);
		}
	}

	/**
	 * Creates a saveable object for the entire structure currently open.
	 * 
	 * @return the saveable object (null if structure is unrecognized).
	 */
	public Saveable getSaveableObject() {
		SaveObject[] objs = graphPanelContainer.getSaveObjects();
		if (graphPanelContainer.getStructureType() == StructureType.Kripke) {
			return objs[0];
		} else if (graphPanelContainer.getStructureType() == StructureType.MultiKripke) {
			MultiSaveObject mObj = new MultiSaveObject(objs);
			// /src/eshmun/gui/utils/models/multikripke/MultiSaveObject.java
			return mObj;
		}

		return null;
	}

	/**
	 * Loads a structure from the given save-able object.
	 * 
	 * @param saveable
	 *            saveable object to load from.
	 */
	public void load(final Saveable saveable) {
		if (saveable.getStructureType() == StructureType.Refinement) { //Refinement object, buggy
			java.awt.EventQueue.invokeLater(new Runnable() { // BRDS: places runnable at the end of the event queue
				public void run() { // BRDS: runs following code block in current thread
					RefinementManager.current = (RefinementManager) saveable;
					RefinementManager.current.goToIndex(0, getSize());
				}
			});
			dispose(); // BRDS: closes AWT frame
			return;
		}

		if (saveable.getStructureType() == StructureType.SynchronizationSkeleton) { //Sync skel
			java.awt.EventQueue.invokeLater(new Runnable() { // BRDS: places runnable at the end of the event queue
				public void run() {
					SynchronizationSkeletonFrame frame = new SynchronizationSkeletonFrame(saveable);
					// /src/eshmun/gui/synchronizationskeletons/SynchronizationSkeletonFrame.java
					frame.setVisible(true);

					frame.setRefinementCurrent(refinmentTab.getCurrent());
				}
			});

			dispose(); // BRDS: closes AWT frame
			return;
		}

		isAbstracted = false;	//BRDS:	If the above two conditions are false, isAbstracted is false. If they're true,
								//		then in either case it'll return and isAbstracted will not be affected AFAIK.
		if (saveable.getStructureType() == structureType) {
			graphPanelContainer.load(saveable);
		} else {
			java.awt.EventQueue.invokeLater(new Runnable() { // BRDS: places runnable at the end of the event queue
				public void run() {
					Eshmun eshmun = new Eshmun(saveable.getStructureType()); // /src/eshmun/Eshmun.java
					eshmun.setVisible(true); 
					eshmun.setExtendedState(eshmun.getExtendedState() | JFrame.MAXIMIZED_BOTH); // BRDS: Maximizes the window. Why?
					eshmun.load(saveable);
					eshmun.setRefinementCurrent(refinmentTab.getCurrent());
				}
			});

			dispose(); // BRDS: closes AWT frame
		}
	}

	/**
	 * Loads a structure from the given string definition.
	 * 
	 * @param definition
	 *            the string definition.
	 * @param structureType
	 *            the type of structure in the definition.
	 */
	public void loadDefinition(final String definition, final StructureType structureType) {
		try {

			isAbstracted = false;
			if (this.structureType == structureType) { //[PCA: why match types?
			//BRDS: If loading a structure, and the type of what's currently open is not the same, you switch.
				String specs = graphPanelContainer.loadDefinition(definition, structureType);
				if (specs != null)
					setSpecificationFormula(specs);
			} else {
				java.awt.EventQueue.invokeLater(new Runnable() { //[PCA: create new Eshmun? BRDS: Yes!
					public void run() {
						Eshmun eshmun = new Eshmun(structureType); // /src/eshmun/Eshmun.java
						eshmun.setVisible(true);
						eshmun.setExtendedState(eshmun.getExtendedState() | JFrame.MAXIMIZED_BOTH);
						eshmun.loadDefinition(definition, structureType);
						eshmun.setRefinementCurrent(refinmentTab.getCurrent());
					}
				});

				dispose(); // BRDS: closes AWT frame
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog(eshmun, "The imported file cannot be loaded into a Kripke Structure ");
		}
	}

	/**
	 * Checks if either the name or all the processes given exist already in a
	 * structure
	 * 
	 * @param name
	 *            the name to look for.
	 * @param processes
	 *            the processes to look for.
	 * @return true if either exists, false otherwise.
	 */
	public boolean contains(String name, String[] processes) {
		name = name.trim();

		String[] strs;
		
		//BRDS: Grabs string representation of graphPanelContainer and breaks it up into an array strs.
		String str = graphPanelContainer.stringRepresentation();
		if (!str.contains(";")) {
			strs = new String[] { str }; // only one
		} else {
			strs = str.split(";");
		}
		
		//BRDS: Iterates for each string s over array strs. Following comments on this loop are from me.
		for (String s : strs) {
			// first, check if there are processes to go through here, looking for parentheses.
			if (s.contains("(")) { // Does s contain an open parenthesis? Then grab the following substrings...
				String n = s.substring(0, s.indexOf("(")); // n - substring before the open parenthesis.
				String pro = s.substring(s.indexOf("(") + 1, s.indexOf(")")); //pro - substring of the parenthetical
				String[] pros = pro.split(","); // array pros - splits pro at commas 
				for (int i = 0; i < pros.length; i++)
					pros[i] = pros[i].trim();

				//Compare processes to procs and n to name.
				Arrays.sort(pros);
				if (Arrays.equals(processes, pros) || n.trim().equals(name))
					return true;
			} else { // Simply compare s to name, since there aren't any processes to go through.
				if (s.trim().equals(name))
					return true;
			}
		}

		return false;
	}

	/*
	 * Important Methods, model checking + model repair.
	 */

	/**
	 * Restores the dashed states and transitions.
	 */
	public void restore() {
		graphPanelContainer.restore();
	}

	/**
	 * Deletes the dashed transitions and states.
	 */
	public void deleteDashed() {
		graphPanelContainer.deleteDashed();
	}

	/*
	 * Model check
	 */

	/**
	 * Model Checks this model.
	 */
	public void modelCheck() {
		long start = System.nanoTime();

		Repairable repairable = graphPanelContainer.constructStructure(); // /src/eshmun/structures/Repairable.java
		LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java

		List<String> strs = graphPanelContainer.getSpecifications();
		ArrayList<AbstractExpression> specs = new ArrayList<AbstractExpression>();
		// /src/eshmun/expression/AbstractExpression.java

		for (String str : strs) {
			specs.add(parser.parse(str));
		}

		boolean result = repairable.modelCheck(specs);

		long end = System.nanoTime();
		long time = end - start;

		if (result) {
			writeMessage("Model is Correct; Taken: " + time, false, "Model is Correct");
		} else {
			writeMessage("Model needs repair; Taken: " + time, false, "Model needs repair");
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * Model Checks this model state-by-state. States that satisfy the given formula
	 * are displayed differently.
	 */
	public void stateByStateModelCheck() {
		// Allow user to enter formula in dialog.
		EnterFormulaDialog dialog = new EnterFormulaDialog(this);
		// /src/eshmun/gui/kripke/dialogs/EnterFormulaDialog.java
		if (!dialog.showDialog())
			return;

		// Return previously striped/shaded states to their initial style.
		graphPanelContainer.applyStateByState(new HashSet<String>());

		// Begin Timing.
		long start = System.nanoTime();

		// Construct the structure and model check it (state-by-state) against user
		// specific specs.
		KripkeStructure structure = (KripkeStructure) graphPanelContainer.constructCurrentStructure();
		Collection<String> satisfyingStates = structure.stateByStateModelCheck(dialog.getFormula());
		
		// End Timing.
		long end = System.nanoTime();
		long time = end - start;

		// Apply Shading/Striping visual effect to the satisfying states.
		graphPanelContainer.applyStateByState(satisfyingStates);
		writeMessage("Done; Taken: " + time, false);
		System.out.println("Time Taken (Global Check): " + time + " Nano");
	}
	
	/**
	 * Model check the structure given a set of ctl formulae and will return for every formula
	 * the states that satisfy the checked formula. 
	 * @param specifications the set of CTL formulae that we want to use in the abstraction process.
	 * @return return the truth table of the states.
	 */
	public ArrayList<ArrayList<Integer>> stateByStateModelCheck(ArrayList<String> specifications) {
		//start timer
		long start = System.nanoTime();
		//get the kripke structure
		KripkeStructure structure = (KripkeStructure) graphPanelContainer.constructCurrentStructure();
		//initialize the truth table
		ArrayList<ArrayList<Integer>> truthTable = new ArrayList <ArrayList<Integer>>();
		//get the number of states from the displayed structure
		int states = structure.getNumberOfStates();
		
//		HashSet<AbstractState> structureStates =  (HashSet<AbstractState>) structure.getStates();
//		List<AbstractState> structureStatesAsList = new ArrayList<AbstractState>(structureStates);
		
		List<State> structureStatesAsList =  getCurrentGraphPanel().getStates( );
	 
	
		 
		
		//model checking the passed specifications as an argument 1 by 1 
		for(int i = 0; i < AbstractionCTLPanel.CTLs.size(); i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			String formula = AbstractionCTLPanel.CTLs.get(i);
			LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java
			AbstractExpression expression =  parser.parse(formula);
			// /src/eshmun/expression/AbstractExpression.java
			Collection <String> satisfyingStates = structure.stateByStateModelCheck(expression);
			ArrayList<String> trueStates = (ArrayList<String>) satisfyingStates;
		
			for(int j = 0; j < states; j++) {
				temp.add(0);
				State sss = structureStatesAsList.get(j);				
				
				for(int k = 0; k<trueStates.size(); k++) {						
					
					if(trueStates.get(k).contains(sss.getName()) || trueStates.get(k).contains(sss.getName())) {
						temp.set(j, 1);
						break;
					}
				}
			}
			
		
			
			truthTable.add(temp);
		}
		// end timer and calculate time needed
		long end = System.nanoTime();
		long time = end - start;
		// success message
		writeMessage("Done; Taken: " + time, false);
		System.out.println("Time Taken (Global Check): " + time + " Nano");
		System.out.println(truthTable.toString());
		// return the truth table to the calling function
		return truthTable;
	}

	/*
	 * Repair
	 */

	/**
	 * Attempts repairing this model.
	 * 
	 * @return true if model was repaired, false otherwise.
	 */
	public boolean modelRepair() {
		return modelRepair(true);
	}

	/**
	 * Attempts repairing this model.
	 * 
	 * @param callback
	 *            callback the switch buttons.
	 * @return true if model was repaired, false otherwise.
	 */
	private boolean modelRepair(boolean callback) {
		graphPanelContainer.restore();

		BooleanVariable.reset(); // /src/eshmun/expression/atomic/BooleanVariable.java

		Repairable repairable = graphPanelContainer.constructStructure(); // /src/eshmun/structures/Repairable.java

		oldRepairs = new ArrayList<Collection<String>>();
		currentRepair = 0;

		LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java

		List<String> strs = graphPanelContainer.getSpecifications();
		ArrayList<AbstractExpression> specs = new ArrayList<AbstractExpression>();
		// /src/eshmun/expression/AbstractExpression/java

		for (String str : strs) {
			if (str.trim().equals("") || str.trim().equals("CTL Specifications"))
				return false;

			specs.add(parser.parse(str));
		}

		try {
			long start = System.nanoTime();
			repairer = new ModelRepairer(repairable, specs, getStructureExpression(), syncOverActions,
					syncOverProcessIndices); // /src/eshmun/modelrepair/ModelRepairer.java
			boolean repaired;
			try {
				repaired = repairer.modelRepair();

				if (repaired) {
					Collection<String> deletions = repairer.getNextDeletions();

					// Display repair statistics
					if (Eshmun.ECHO_STATS) {
						int[] n = repairer.getNumbers();
						StatsDialog.resetHistory();
						StatsDialog.updateDialog(n[0], n[1], n[2], n[3], n[4], n[5]);
					}

					long end = System.nanoTime();
					System.out.println("Time Taken: " + (end - start) + " Nano");

					Collection<String> tmp = new HashSet<String>();
					for (String d : deletions) {
						if ((d.startsWith(State.STATE_PREFIX) || d.startsWith(Transition.TRANSITION_PREFIX))
								&& (d.length() - d.replace(".", "").length()) == 2) {
							tmp.add(d);
						}
					}

					deletions = tmp;
					oldRepairs.add(deletions);

					Collection<String> deletionsCopy = new ArrayList<String>(deletions);
					graphPanelContainer.applyDeletions(deletionsCopy);

					writeMessage("Model repaired; Taken: " + (end - start), false, "Model repaired");
				} else {
					long end = System.nanoTime();
					System.out.println("Time Taken: " + (end - start) + " Nano");

					writeMessage("Model cannot be repaired", false, "Model cannot be repaired");
				}

				if (callback)
					searchToolBar.resetRepairs(repaired);

				return repaired;
			} catch (TimeoutException e) {
				writeMessage("TimeOut during repair", true, "TimeOut during repair");
			}
		} catch (NotCNFException e) {
			writeMessage("Error in repair formula", true, "Error in repair formula");
		} catch (ParseFormatException e) {
			writeMessage("Error in repair formula", true, "Error in repair formula");
		} catch (IOException e) {
			writeMessage("Error in reading repair formula", true, "Error in reading repair formula");
		} catch (IllegalArgumentException e) {
			writeMessage("Error in specifications formula", true);
			JOptionPane.showMessageDialog(null,
					"Label: '" + e.getMessage() + "' is present in the specifications but not in the structure",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		return false;
	}

	/**
	 * Traverses to the next repair.
	 * 
	 * @return true if there exist a new repair, false otherwise.
	 */
	public boolean nextRepair() {
		// First run.
		if (repairer == null)
			return modelRepair();

		if (currentRepair == oldRepairs.size() - 1) { // Repair is unknown and must be computed.
			try {
				while (true) {
					Collection<String> deletions = repairer.getNextDeletions();
					if (deletions == null) { // No repair available (Either UNSAT or exhausted search space).
						// Display repair statistics
						if (Eshmun.ECHO_STATS) {
							int[] n = repairer.getNumbers();
							StatsDialog.updateDialog(n[0], n[1], n[2], n[3], n[4], n[5]);
						}

						writeMessage("No More Repairs", true);
						Toolkit.getDefaultToolkit().beep();
						return false;
					} else { // Repair available, but maybe similar to a previous repair (different in hidden
								// vars).
						Collection<String> tmp = new HashSet<String>();
						for (String d : deletions) {
							if ((d.startsWith(State.STATE_PREFIX) || d.startsWith(Transition.TRANSITION_PREFIX))
									&& (d.length() - d.replace(".", "").length()) == 2) {
								tmp.add(d);
							}
						}

						// Check if the repair is new.
						deletions = tmp;
						boolean newRepair = true;
						for (Collection<String> old : oldRepairs) {
							if (old.equals(deletions)) {
								newRepair = false;
								break;
							}
						}

						// If it is a new repair display it, else continue looking up for new repairs.
						if (newRepair) {
							// Display repair statistics
							if (Eshmun.ECHO_STATS) {
								int[] n = repairer.getNumbers();
								StatsDialog.updateDialog(n[0], n[1], n[2], n[3], n[4], n[5]);
							}

							currentRepair++;
							oldRepairs.add(deletions);

							Collection<String> deletionsCopy = new ArrayList<String>(deletions);
							graphPanelContainer.applyDeletions(deletionsCopy);

							return true;
						}
					}
				}
			} catch (IllegalStateException e) {
				return modelRepair();

			} catch (TimeoutException e) {
				writeMessage("TimeOut during repair", true, "TimeOut during repair");
				Toolkit.getDefaultToolkit().beep();
				return false;
			}
		} else if (currentRepair < oldRepairs.size() - 1) { // Repair is stored previously.
			currentRepair++;

			// Display repair statistics
			if (Eshmun.ECHO_STATS)
				StatsDialog.goToNext();

			Collection<String> deletionsCopy = new ArrayList<String>(oldRepairs.get(currentRepair));
			graphPanelContainer.applyDeletions(deletionsCopy);

			return true;
		}

		Toolkit.getDefaultToolkit().beep();
		return false;
	}

	/**
	 * Traverses to the previous repair.
	 * 
	 * @return true if there exist a previous repair, false otherwise.
	 */
	public boolean previousRepair() {
		restore();

		if (currentRepair == 0) {
			Toolkit.getDefaultToolkit().beep();
			return false;
		}

		currentRepair--;

		// Display repair statistics
		if (Eshmun.ECHO_STATS)
			StatsDialog.goToPrevious();

		Collection<String> deletionsCopy = new ArrayList<String>(oldRepairs.get(currentRepair));
		graphPanelContainer.applyDeletions(deletionsCopy);

		return currentRepair > 0;
	}

	public void unsatCore() {
		graphPanelContainer.restore();

		BooleanVariable.reset(); // /src/eshmun/expression/atomic/BooleanVariable.java

		Repairable repairable = graphPanelContainer.constructStructure(); // /src/eshmun/structures/Repairable.java
		LogicParser parser = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java

		List<String> strs = graphPanelContainer.getSpecifications();
		ArrayList<AbstractExpression> specs = new ArrayList<AbstractExpression>();
		// /src/eshmun/expression/AbstractExpression.java

		for (String str : strs) {
			if (str.trim().equals("") || str.trim().equals("CTL Specifications"))
				return;

			specs.add(parser.parse(str));
		}

		try {
			long start = System.nanoTime();

			AndOperator repairFormula = repairable.generateRepairFormula(specs, syncOverActions,
					syncOverProcessIndices); // IN CNF BY CONSTRUCTION
			// /src/eshmun/expression/operators/AndOperator.java
			
			AbstractExpression structural = getStructureExpression();
			// /src/eshmun/expression/AbstractExpression.java
			if (structural != null)
				repairFormula.and(structural);

			if (MUS_DIRECTORY == null) { // Choose directory for MUS Tool.
				// try default directory first
				if (new File("MUS" + File.separator + "run-muser2").exists())
					MUS_DIRECTORY = "MUS" + File.separator;
				else if (new File("mus" + File.separator + "run-muser2").exists())
					MUS_DIRECTORY = "mus" + File.separator;
				else {
					JOptionPane.showMessageDialog(null, "Please choose the directory of MUSer2", "MUSer2",
							JOptionPane.INFORMATION_MESSAGE);
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int dec = fileChooser.showOpenDialog(null);
					if (dec == JFileChooser.APPROVE_OPTION) {
						String directory = fileChooser.getSelectedFile().getAbsolutePath().trim();
						if (!directory.endsWith(File.separator))
							directory = directory + File.separator;

						MUS_DIRECTORY = directory;
					} else if (dec == JFileChooser.ERROR_OPTION) {
						JOptionPane.showConfirmDialog(null, "Error in choosing the directory", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else
						return;
				}
			}

			// Has directory
			UnsatCoreSolver unsatSolver = new UnsatCoreSolver(repairFormula, MUS_DIRECTORY);
			// /src/eshmun/modelrepair/unsat/UnsatCoreSolver.java
			Collection<String> unsatCore = unsatSolver.getNextRepair();

			long end = System.nanoTime();
			System.out.println("Time Taken: " + (end - start) + " Nano");

			graphPanelContainer.applyUnsatCore(unsatCore);

			writeMessage("Unsat Core; Taken: " + (end - start), false);

		} catch (NotCNFException e) {
			writeMessage("Error in repair formula", true);
		} catch (IOException e) {
			writeMessage("Error in UNSAT CORE Solver", true);
			JOptionPane.showMessageDialog(null,
					"Error in Communicating with UNSAT CORE Solver. Please check the directory in the settings and make sure it is read-write.",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			MUS_DIRECTORY = null;
		} catch (IllegalArgumentException e) {
			writeMessage("Error in specifications formula", true);
			JOptionPane.showMessageDialog(null,
					"Label: '" + e.getMessage() + "' is present in the specifications but not in the structure",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Triplet Checker
	 */

	public void tripletCheck() {

		ArrayList<Transition> transitions = this.getCurrentGraphPanel().getTransitions();

		boolean tripleCheckFinalResult = true;
		boolean hidemessage = false;

		for (Transition t : transitions) {

			// for each transition

			State previousState = (State) t.getFrom();
			String Pred_S = previousState.getPreCondition();

			State nextState = (State) t.getTo();
			String Pred_t = nextState.getPreCondition();

			String Pre_t = t.getPreCondition();
			String S = t.getStatementBlock();
			String Q = t.getPostCondition();

			Boolean transitionValidityResult = null;
			String error = "";

			// if a state or transition is empty, we need to set it to true
			if (Pred_S == null || Pred_S.isEmpty()) {
				Pred_S = "True";
			}
			if (Pre_t == null || Pre_t.isEmpty()) {
				Pre_t = "True";
			}
			if (Q == null || Q.isEmpty()) {
				Q = "True";
			}

			// Checking warning if Pred_S => Pre_t is unsatisfiable
			try {

				if (Z3SolverHelper.SolvePredAndPre(Pred_S, Pre_t, " & ", true)) {

					t.setTransitionBeginningWarning(true);

				} else {
					error = "";
					t.setTransitionBeginningWarning(false);
				}
			} catch (Exception e) {

				t.setTransitionBeginningWarning(true);
				error = e.getMessage();

			}

			// Checking if the triple {Pred_S & Pre_t} S Q is valid

			boolean solverResult = false;
			try {

				// Checking if the triple {Pred_S & Pre_t} S Q is valid
				if (S != null && !S.isEmpty()) {

					S = S.replaceAll("-", " - "); // to be fixed later
					solverResult = Z3SolverHelper
							.SolvePSQ("{" + Pred_S + " & " + Pre_t + "}" + "{" + S + "}" + "{" + Q + "}");
					t.setTransitionTripletSyntaxError(false);
					error = "";

					transitionValidityResult = solverResult;

				} else { // else it is a skip operation

					// If it is a skip, then we need to validate {Pred_S & Pre_t} => Q

					if (!Z3SolverHelper.SolvePredAndPre(Pred_S + " & " + Pre_t, Q, " => ", false)) {
						t.setTransitionEndError(true);
						tripleCheckFinalResult = tripleCheckFinalResult && false;
						transitionValidityResult = false;
					} else {
						tripleCheckFinalResult = tripleCheckFinalResult && true;
						transitionValidityResult = true;
					}

				}

				// Checking Validity of Q => Pred_t
				if (!Z3SolverHelper.SolvePredAndPre(Q, Pred_t, " => ", false)) {
					t.setTransitionEndError(true);
					tripleCheckFinalResult = tripleCheckFinalResult && false;
					transitionValidityResult = false;
				} else {
					t.setTransitionEndError(false);
				}

			} catch (Exception e) {

				solverResult = false;
				t.setTransitionTripletSyntaxError(true);
				error = e.getMessage();

			}

			// flag transition as invalid
			if (transitionValidityResult != null) {
				t.setTransitionValidity(transitionValidityResult);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();
				tripleCheckFinalResult = tripleCheckFinalResult && transitionValidityResult;
			}

			// if there is a syntax error in triples, display a dialog
			if (t.getTransitionTripletSyntaxError()) {

				JOptionPane.showMessageDialog(this, "<html>Triplet syntax error<br>" + error + "</html>");
				hidemessage = true;
				t.setTransitionTripletSyntaxError(false);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();
			}
		}

		// if no syntax error, inform the user about the final result
		if (!hidemessage) {
			if (tripleCheckFinalResult)
				writeMessage("All triples are correct", false, "All triples are correct");
			else
				writeMessage("Some triples are not correct", false, "Some triples are not correct");
		}

	}

	public void newTripletCheck() {

		ArrayList<Transition> transitions = this.getCurrentGraphPanel().getTransitions();

		boolean tripleCheckFinalResult = true;
		boolean hidemessage = false;

		for (Transition t : transitions) {

			if(t.getRetain()) {
				System.out.println("in");
			}
			// for each transition

			State previousState = (State) t.getFrom();
			String Pred_S = previousState.getPreCondition();

			State nextState = (State) t.getTo();
			String Pred_t = nextState.getPreCondition();

			String Pre_t = t.getPreCondition();
			String S = t.getStatementBlock();
			String Q = t.getPostCondition();

			TransitionTripleChecker checker = new TransitionTripleChecker();
			// /src/eshmun/skeletontextrepresentation/infinitespace/TransitionTripleChecker.java
			TransitionValidationResult tt = checker.checkTriple(Pred_S, Pred_t, Pre_t, S, Q);

			if (tt.errorMessage != null) {

				hidemessage = true;
				JOptionPane.showMessageDialog(this, "<html>Triplet syntax error<br>" + tt.errorMessage + "</html>");
				hidemessage = true;
				t.setTransitionTripletSyntaxError(false);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();

			}

			if (tt.beginningError) {
				t.setTransitionBeginningWarning(tt.beginningError);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();
			}

			if (tt.endError) {
				t.setTransitionEndError(tt.endError);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();
			}

			if (!tt.isValid) {
				t.setTransitionValidity(false);
				Eshmun.eshmun.getCurrentGraphPanel().repaint();
			}

			tripleCheckFinalResult = tripleCheckFinalResult && tt.isValid;

		}

		// if no syntax error, inform the user about the final result
		if (!hidemessage) {
			if (tripleCheckFinalResult)
				writeMessage("All triples are correct", false, "All triples are correct");
			else
				writeMessage("Some triples are not correct", false, "Some triples are not correct");
		}

	}

	/*
	 * Abstraction
	 */

	/**
	 * Checks if the model is already abstracted.
	 * 
	 * @return true if the model is abstracted, false otherwise.
	 */
	public boolean isAbstracted() {
		return isAbstracted;
	}

	/**
	 * Abstracts the model by label. If model is already abstracted does nothing.
	 */
	public void abstractByLabel() {
		if (isAbstracted)
			return;

		HashSet<String> allVars = new HashSet<String>();
		for (String s : graphPanelContainer.getSpecifications()) {
			LogicParser p = new LogicParser(); // /src/eshmun/expression/parser/LogicParser.java
			AbstractExpression e = p.parse(s); // /src/eshmun/expression/AbstractExpression.java

			GeneralVariableListerVisitor v = new GeneralVariableListerVisitor();
			// /src/eshmun/expression/visitor/visitors/GeneralVariableListerVisitor.java
			allVars.addAll(v.getVariables(e));
		}

		graphPanelContainer.abstractByLabel(getSpecificationFormula(), allVars);
		isAbstracted = true;
	}

	/**
	 * Abstracts the model by formula. If model is already abstracted does nothing.
	 */
	public void abstractByFormula() {
		if (isAbstracted)
			return;

		graphPanelContainer.abstractByFormula(getSpecificationFormula());
		isAbstracted = true;
	}
	
	/**
	 * Abstracts the model by a set of CTL formulae. If model is already abstracted does nothing.
	 */
	public void abstractByCTLFormulae() {
		if(isAbstracted)
			return;
		
		graphPanelContainer.abstractByCTLFormulae(AbstractionCTLPanel.CTLs);
		isAbstracted = true;
	}

	/**
	 * Concretizes the abstracted model.
	 */
	public void concretize() {
		if (!isAbstracted)
			return;

		long start = System.nanoTime();

		if (graphPanelContainer.concretize(getSpecificationFormula()) != null) {
			long end = System.nanoTime();

			graphPanelContainer.register();
			writeMessage("Concretized; Taken: " + (end - start), false, "Concretized");

			isAbstracted = false;
		} else {
			long end = System.nanoTime();

			graphPanelContainer.rollback();
			writeMessage("Repair is Invalid; Taken: " + (end - start), true, "Repair is Invalid");
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * Resets the abstraction, reverts back to old model.
	 */
	public void resetAbstraction() {
		graphPanelContainer.resetAbstraction();

		isAbstracted = false;
	}

	/*
	 * Pair Generation
	 */

	/**
	 * Takes the current single Kripke Structure, and generate isomorphic pairs for
	 * the specified number of processes, with their specs.
	 * 
	 * @param processNum
	 *            the number of processes.
	 * @param ring
	 *            true if the pairs must be generated on a ring, false if all pairs.
	 */
	public void generatePairs(int processNum, boolean ring) {
		if (getStructureType() != StructureType.Kripke && getStructureType() != StructureType.MultiKripke) // Only valid
																											// for
																											// single
																											// kripkes
			return;

		String structural = getStructureFormula();
		SaveObject obj = getCurrentGraphPanel().getSaveObject(getSpecificationFormula(), structural); // current kripke

		String initialName = obj.getStructureName();

		String name = obj.getStructureName();
		if (name.contains("(")) { // trim name
			name = name.substring(0, name.indexOf("("));
		}

		name = name.trim();

		String firstIndex = "1";
		String secondIndex = "2";
		String objName = obj.getStructureName();
		if (objName.contains("(")) {
			int start = objName.indexOf("(");
			int end = objName.indexOf(")");

			String[] names = objName.substring(start + 1, end).split(",");
			firstIndex = names[0].trim();
			secondIndex = names[1].trim();
		}

		String specs = obj.getSpecifications(); // fix specifications template
		specs = specs.replaceAll(firstIndex, "{" + firstIndex + "}").replaceAll(secondIndex, "{" + secondIndex + "}");

		// Fix the current kripke to be a template
		HashMap<String, State> states = new HashMap<String, State>();
		// /src/eshmun/gui/utils/models/vanillakripke/State.java
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		// /src/eshmun/gui/utils/models/vanillakripke/Transition.java
		for (State s : obj.getStates()) {
			String s_name = s.getName();
			String s_label = s.getLabels().replaceAll(firstIndex, "{" + firstIndex + "}").replaceAll(secondIndex,
					"{" + secondIndex + "}");

			State new_s = new State(s_name, s_label, new Point(s.getLocation()), s.getStart(), s.getRetain());
			// /src/eshmun/gui/utils/models/vanillakripke/State.java
			states.put(s_name, new_s); 
		}

		for (Transition t : obj.getTransitions()) {
			String from = t.getFrom().getName();
			String to = t.getTo().getName();

			Transition new_t = new Transition(states.get(from), states.get(to), t.isDoubled(), obj.getStructureName(),
					t.getActionNames(), t.getRetain(), t.hasDouble());
			// /src/eshmun/gui/utils/models/vanillakripke/Transition.java
			transitions.add(new_t);
		}

		// Store the template to use later
		State[] statesArray = states.values().toArray(new State[1]);
		Transition[] transArray = transitions.toArray(new Transition[1]);
		obj = new SaveObject(name, statesArray, transArray, "", structural);

		// Use template to generate multiple pair structures
		ArrayList<SaveObject> saveObjs = new ArrayList<SaveObject>();
		// /src/eshmun/gui/utils/models/vanillakripke/SaveObject.java
		int counter = 1;
		for (int i = 1; i <= processNum; i++) {
			if (ring) {
				int j = (i % processNum) + 1;
				states = new HashMap<String, State>(); // /src/eshmun/gui/utils/models/vanillakripke/State.java
				transitions = new ArrayList<Transition>();
				// /src/eshmun/gui/utils/models/vanillakripke/Transition.java

				String new_name = name + counter + "N (" + i + "," + j + ")";

				for (State s : obj.getStates()) {
					String s_name = s.getName();
					String s_label = s.getLabels().replaceAll("\\{" + firstIndex + "}", i + "")
							.replaceAll("\\{" + secondIndex + "}", j + "");

					State new_s = new State(s_name, s_label, new Point(s.getLocation()), s.getStart(), s.getRetain());
					// /src/eshmun/gui/utils/models/vanillakripke/State.java
					states.put(s_name, new_s); 
				}

				for (Transition t : obj.getTransitions()) {
					String from = t.getFrom().getName();
					String to = t.getTo().getName();

					Transition new_t = new Transition(states.get(from), states.get(to), t.isDoubled(), new_name,
							t.getActionNames(), t.getRetain(), t.hasDouble());
					// /src/eshmun/gui/utils/models/vanillakripke/Transition.java
					transitions.add(new_t); 
				}

				String new_specs = specs.replaceAll("\\{" + firstIndex + "}", i + "")
						.replaceAll("\\{" + secondIndex + "}", j + "");

				statesArray = states.values().toArray(new State[1]);
				transArray = transitions.toArray(new Transition[1]);

				SaveObject newObj = new SaveObject(new_name, statesArray, transArray, new_specs, structural);
				saveObjs.add(newObj); // /src/eshmun/gui/utils/models/vanillakripke/SaveObject.java

				counter++;
			} else {
				for (int j = i + 1; j <= processNum; j++) {
					states = new HashMap<String, State>(); // /src/eshmun/gui/utils/models/vanillakripke/State.java
					transitions = new ArrayList<Transition>(); 
					// /src/eshmun/gui/utils/models/vanillakripke/Transition.java

					String new_name = name + counter + "N (" + i + "," + j + ")";

					for (State s : obj.getStates()) {
						String s_name = s.getName();
						String s_label = s.getLabels().replaceAll("\\{" + firstIndex + "}", i + "")
								.replaceAll("\\{" + secondIndex + "}", j + "");

						State new_s = new State(s_name, s_label, new Point(s.getLocation()), s.getStart(),
								s.getRetain()); // /src/eshmun/gui/utils/models/vanillakripke/State.java
						states.put(s_name, new_s);
					}

					for (Transition t : obj.getTransitions()) {
						String from = t.getFrom().getName();
						String to = t.getTo().getName();

						Transition new_t = new Transition(states.get(from), states.get(to), t.isDoubled(), new_name,
								t.getActionNames(), t.getRetain(), t.hasDouble());
						// /src/eshmun/gui/utils/models/vanillakripke/Transition.java
						transitions.add(new_t);
					}

					String new_specs = specs.replaceAll("\\{" + firstIndex + "}", i + "")
							.replaceAll("\\{" + secondIndex + "}", j + "");

					statesArray = states.values().toArray(new State[1]);
					transArray = transitions.toArray(new Transition[1]);

					SaveObject newObj = new SaveObject(new_name, statesArray, transArray, new_specs, structural);
					// /src/eshmun/gui/utils/models/vanillakripke/SaveObject.java
					saveObjs.add(newObj);

					counter++;
				}
			}
		}

		// Merge the pair structures into one Concurrent Kripke Structure and load it
		if (getStructureType() == StructureType.MultiKripke) {
			MultiSaveObject old = (MultiSaveObject) getSaveableObject();
			SaveObject[] oldObjs = old.getSaveObjects();

			int size = saveObjs.size() + oldObjs.length;
			ArrayList<SaveObject> allObjs = new ArrayList<SaveObject>(size);
			// /src/eshmun/gui/utils/models/vanillakripke/SaveObject.java
			for (SaveObject o : oldObjs) {
				System.out.println(o.getStructureName());
				if (o.getStructureName().equals(initialName))
					allObjs.addAll(saveObjs);

				else
					allObjs.add(o);
			}

			saveObjs = allObjs;
		}

		MultiSaveObject multiSave = new MultiSaveObject(saveObjs.toArray(new SaveObject[1]));
		// /src/eshmun/gui/utils/models/multikripke/MultiSaveObject.java
		load(multiSave);
	}

	/**
	 * Extracts templates from all the structures currently in Eshmun.
	 * 
	 * @return a list of definitions (scripting language style).
	 * @throws IllegalStateException
	 *             if eshmun does not contain valid Kripke or MultiKripke
	 *             Structures.
	 */
	public LinkedList<String> extractPairSpecs() throws IllegalStateException {
		return extractPairSpecs(null);
	}

	/**
	 * Extracts templates from all the structures currently in Eshmun whose name
	 * appear in the given set.
	 * 
	 * @param structureNames
	 *            a set of structure names to consider, null means consider all.
	 * @return a list of definitions (scripting language style).
	 * @throws IllegalStateException
	 *             if eshmun does not contain valid Kripke or MultiKripke
	 *             Structures.
	 */
	public LinkedList<String> extractPairSpecs(HashSet<String> structureNames) throws IllegalStateException {
		if (getStructureType() != StructureType.Kripke && getStructureType() != StructureType.MultiKripke) // Only valid
																											// for
																											// single
																											// kripkes
			throw new IllegalStateException("Eshmun does not contain valid extractalbe structures.");

		LinkedList<String> definitions = new LinkedList<>();

		// Get all structures.
		SaveObject[] objs = graphPanelContainer.getSaveObjects();

		// Filter structures by name.
		for (SaveObject obj : objs) {
			if (structureNames != null && !structureNames.contains(obj.getStructureName()))
				continue;

			String name = obj.getStructureName();
			if (name.contains("("))
				name = name.substring(0, name.indexOf("("));
			name = name.trim();

			// Not a pair structure.
			if (!obj.getStructureName().contains("("))
				continue;

			// Figure out process indices.
			String[] indices;
			String[] symbols = new String[] { "i", "j", "k", "l", "m", "n" };
			String tmp = obj.getStructureName();
			int start = tmp.indexOf("(");
			int end = tmp.indexOf(")");

			indices = tmp.substring(start + 1, end).split(",");
			for (int i = 0; i < indices.length; i++) {
				indices[i] = indices[i].trim();
			}

			// fix specifications template
			String specs = obj.getSpecifications();
			for (int i = 0; i < indices.length; i++)
				specs = specs.replaceAll(indices[i] + " ",
						"[" + (i < symbols.length ? symbols[i] : "i" + (i - symbols.length + 1)) + "] ");

			// Fix the current Kripke to be a template
			String states = "";
			for (State s : obj.getStates()) {
				String[] labels = s.getLabels().split(",");
				String modified = "";
				for (String l : labels) {
					l = l.trim();
					for (int i = 0; i < indices.length; i++)
						if (l.endsWith(indices[i]))
							l = l.substring(0, l.length() - indices[i].length()) + "["
									+ (i < symbols.length ? symbols[i] : "i" + (i - symbols.length + 1)) + "]";

					modified += l + ",";
				}

				if (modified.endsWith(","))
					modified = modified.substring(0, modified.length() - 1);

				String current = s.constructDefinition();
				states += current + System.lineSeparator();
			}

			// Get the transitions.
			String transitions = "";
			for (Transition t : obj.getTransitions()) {
				String current = t.constructDefinition();
				transitions += current + System.lineSeparator();
			}

			// Define the symbolic indices.
			String symbs = "";
			for (int i = 0; i < indices.length; i++) {
				symbs += (i < symbols.length ? symbols[i] : "i" + (i - symbols.length + 1));
				if (i < indices.length - 1)
					symbs += ", ";
			}

			// Construct definition.
			String definition = "define " + name + "<" + symbs + "> {" + System.lineSeparator();
			definition += "states:" + System.lineSeparator();
			definition += states + System.lineSeparator();
			definition += "transitions:" + System.lineSeparator();
			definition += transitions + System.lineSeparator();
			definition += "specifications:" + System.lineSeparator();
			definition += specs + System.lineSeparator();
			definition += "}";

			definitions.addLast(definition);
		}

		return definitions;
	}

	/*
	 * SYNC SKELETONS
	 */

	/**
	 * Transforms the structure in Eshmun to a collection of Synchronization
	 * skeletons, then loads it to the screen.
	 * 
	 * @throws IllegalArgumentException
	 *             if the current structure being converted has an invalid type or
	 *             components.
	 */
	public void toSynchSkeletons() {
		ToSkeletonConverter conv = new ToSkeletonConverter(graphPanelContainer.constructStructure());
		// /src/eshmun/structures/skeletons/ToSkeletonConverter.java
		if (!conv.convertToSkeleton())
			return;

		SynchronizationSkeletonFrame frame = new SynchronizationSkeletonFrame(conv.getStates(), conv.getTransitions(),
				conv.getCommonName()); // src/eshmun/gui/synchronizationskeletons/SynchronizationSkeletonFrame.java

		frame.setVisible(true);
		frame.setRefinementCurrent(refinmentTab.getCurrent());
		this.dispose();
	}

	/**
	 * Transforms a Kripke structure to a Program represented in text format.
	 * 
	 * @throws IllegalArgumentException
	 *             if the current structure being converted has an invalid type or
	 *             components.
	 */

	public String toProgramText() {

		KripkeToProgramConverter converter = new KripkeToProgramConverter(graphPanelContainer.constructStructure(),
				this); // /src/eshmun.gui.synchronizationskeletons.SynchronizationSkeletonFrame
		return converter.convertToProgram();

	}

	/*
	 * TRANSITION COLORING
	 */

	/**
	 * Sets the coloring of transitions by process flag.
	 * 
	 * @param transitionColoredByProcess
	 *            the new value of the flag.
	 */
	public void setTransitionColoredByProcess(boolean transitionColoredByProcess) {
		if (!labelColoredByProcess)
			resetProcessColors();

		this.transitionColoredByProcess = transitionColoredByProcess;
		getCurrentGraphPanel().repaint();
	}

	/**
	 * Getter for coloring transitions by process flag.
	 * 
	 * @return true if they are colored, false otherwise.
	 */
	public boolean isTransitionColoredByProcess() {
		return transitionColoredByProcess;
	}

	/**
	 * Sets the coloring of state's labels by process flag.
	 * 
	 * @param labelColoredByProcess
	 *            the new value of the flag.
	 */
	public void setLabelColoredByProcess(boolean labelColoredByProcess) {
		if (!transitionColoredByProcess)
			resetProcessColors();

		this.labelColoredByProcess = labelColoredByProcess;
		getCurrentGraphPanel().repaint();
	}

	/**
	 * Getter for coloring state's label by process flag.
	 * 
	 * @return true if they are colored, false otherwise.
	 */
	public boolean isLabelColoredByProcess() {
		return labelColoredByProcess;
	}

	/**
	 * Sets the action coloring flag.
	 * 
	 * @param colorActions
	 *            the new value of the flag.
	 */
	public void setColorActions(boolean colorActions) {
		resetActionColors();

		this.colorActions = colorActions;
		getCurrentGraphPanel().repaint();
	}

	/**
	 * Getter for coloring the actions.
	 * 
	 * @return true if they are colored, false otherwise.
	 */
	public boolean isActionColored() {
		return colorActions;
	}

	/**
	 * @return true if any coloring is enabled (actions/transitions/labels), false
	 *         otherwise.
	 */
	public boolean hasColoring() {
		return isActionColored() || isTransitionColoredByProcess() || isLabelColoredByProcess();
	}

	/**
	 * Maps Process/Action to Color.
	 */
	private HashMap<String, Color> elementToColor;

	/**
	 * The current Hue, updated and used to generate random colors.
	 */
	double hue;

	/**
	 * Resets the process colors.
	 */
	public void resetProcessColors() {
		HashMap<String, Color> elementToColor = new HashMap<String, Color>();
		elementToColor.put("", Color.BLACK);

		if (this.elementToColor != null) {
			for (String key : this.elementToColor.keySet())
				if (key.startsWith("@@") && key.endsWith("@@"))
					elementToColor.put(key, this.elementToColor.get(key));
		}

		this.elementToColor = elementToColor;

		hue = Math.random();
	}

	/**
	 * Resets the action colors.
	 */
	public void resetActionColors() {
		HashMap<String, Color> elementToColor = new HashMap<String, Color>();
		elementToColor.put("", Color.BLACK);

		if (this.elementToColor != null) {
			for (String key : this.elementToColor.keySet())
				if (!(key.startsWith("@@") && key.endsWith("@@")))
					elementToColor.put(key, this.elementToColor.get(key));
		}

		this.elementToColor = elementToColor;

		hue = Math.random();
	}

	/**
	 * Gets the color of the transition according to its process.
	 * 
	 * @param processName
	 *            the name of the process making the transition.
	 * @return the transition color.
	 */
	public Color getTransitionColor(String processName) {
		// Generate a color for this process.
		Color c = getProcessColor(processName); // Could be used by state even if the transitionColored flag is false.

		if (!eshmun.isTransitionColoredByProcess())
			return Color.BLACK;

		return c;
	}

	/**
	 * Gets the color of the state label according to its process.
	 * 
	 * @param processName
	 *            the name of the process making the transition.
	 * @return the state label color.
	 */
	public Color getStateLabelColor(String processName) {
		if (!eshmun.isLabelColoredByProcess())
			return Color.BLACK;

		return getProcessColor(processName);
	}

	/**
	 * Gets the color attached to the given process (whether transition or state).
	 * 
	 * @param processName
	 *            the process name.
	 * @return the process color.
	 */
	public Color getProcessColor(String processName) {
		if (processName == null || processName.isEmpty()) // Default
			return Color.BLACK;

		if (elementToColor.get(processName) != null) {
			return elementToColor.get(processName);
		}

		Color c = getNewRandomColor();
		elementToColor.put(processName, c);
		return c;
	}

	/**
	 * Gets the color attached to the given action.
	 * 
	 * @param actionName
	 *            the name of the action for which to get the color.
	 * @return the action color.
	 */
	public Color getActionColor(String actionName) {
		if (actionName == null || actionName.isEmpty() || !colorActions) // Default
			return Color.BLACK;

		actionName = "@@" + actionName.trim() + "@@"; // Action prefix and suffix.
		if (elementToColor.get(actionName) != null) {
			return elementToColor.get(actionName);
		}

		Color c = getNewRandomColor();
		elementToColor.put(actionName, c);
		return c;
	}

	/**
	 * @return all the process names of processes with colors.
	 */
	public Collection<String> getColoredProcessNames() {
		// Get Colors for processes that were not queried before.
		List<String> names = graphPanelContainer.getStructureNames();
		for (String name : names) {
			int start = name.indexOf("(");
			int end = name.lastIndexOf(")");
			if (start == -1 || end == -1)
				continue;

			for (String p : name.substring(start + 1, end).split(","))
				if (!p.trim().isEmpty())
					getProcessColor(p.trim());
		}

		HashSet<String> elements = new HashSet<String>(elementToColor.keySet());
		Iterator<String> it = elements.iterator();
		while (it.hasNext()) {
			String s = it.next();
			if (s.startsWith("@@") && s.endsWith("@@"))
				it.remove();
		}

		return elements;
	}

	/**
	 * Sets the alpha component of the color of the given process. the alpha
	 * component controls transparency, if alpha is 0 the color is fully
	 * transparent, if alpha is 255 the color is not.
	 * 
	 * @param process
	 *            the name of the process to set the alpha for.
	 * @param alpha
	 *            the new alpha value ranging between 0 (transparent) and 255(not
	 *            transparent).
	 */
	public void setAlphaForProcessColor(String process, int alpha) {
		if (elementToColor.get(process) != null) {
			Color c = elementToColor.get(process);

			Color nColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
			elementToColor.put(process, nColor);
		} else {
			Color c = getNewRandomColor();
			Color nColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
			elementToColor.put(process, nColor);
		}
	}

	/**
	 * Changes the color mode currently used, this changes the colors already picked
	 * to the new mode, and guarantees that new colors are generated in that mode.
	 * 
	 * @param mode
	 *            the mode of coloring.
	 */
	public void switchColorMode(ColorMode mode) {
		this.colorMode = mode;

		if (mode == ColorMode.ScreenMode) {
			HashSet<String> processes = new HashSet<String>(elementToColor.keySet());
			for (String p : processes) {
				Color c = elementToColor.get(p);
				float hue = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
				int newC = Color.HSBtoRGB(hue, 0.95f, 0.95f);

				int alpha = c.getAlpha();
				alpha = alpha << 24;
				newC = newC & 16777215;

				elementToColor.put(p, new Color(newC | alpha, true));
			}
		}
		if (mode == ColorMode.PrintMode) {
			HashSet<String> processes = new HashSet<String>(elementToColor.keySet());
			for (String p : processes) {
				Color c = elementToColor.get(p);
				float hue = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
				int newC = Color.HSBtoRGB(hue, 0.90f, 0.45f);

				int alpha = c.getAlpha();
				alpha = alpha << 24;
				newC = newC & 16777215;

				elementToColor.put(p, new Color(newC | alpha, true));
			}
		}
	}

	/**
	 * Gets a new random color, guaranteed to be unique.
	 * 
	 * @return a random color.
	 */
	public Color getNewRandomColor() {
		
		try {
			
		
		double golden_ratio_conjugate = 0.618033988749895;

		double s = 0;
		double v = 0;

		if (colorMode == ColorMode.ScreenMode) {
			s = 0.95;
			v = 0.95;
		} else if (colorMode == ColorMode.PrintMode) {
			s = 0.90;
			v = 0.45;
		}

		hue += golden_ratio_conjugate;
		hue %= 1;

		int hi = (int) Math.floor(hue * 10);
		double f = hue * 10 - hi;

		double p = v * (1 - s);
		double q = v * (1 - f * s);
		double t = v * (1 - (1 - f) * s);

		double r, g, b;

		switch (hi) {
		case 0:
			r = v;
			g = t;
			b = p;
			break;

		case 1:
			r = q;
			g = v;
			b = p;
			break;

		case 2:
			r = p;
			g = v;
			b = t;
			break;

		case 3:
			r = p;
			g = q;
			b = v;
			break;

		case 4:
			r = t;
			g = p;
			b = v;
			break;

		case 5:
			r = t;
			g = v;
			b = p;
			break;

		case 6:
			r = t;
			g = t;
			b = v;
			break;

		case 7:
			r = p;
			g = p;
			b = v;
			break;

		case 8:
			r = t;
			g = v;
			b = q;
			break;

		default:
			r = v;
			g = p;
			b = q;
			break;

		}

		 // assert r <= 1 && r >=0 && g <=1 && g >=0  && b >=0 && b <=1    : "Color is wrong";
		
		Color c = new Color((int) (r * 255), (int) (g * 255), (int) (b * 255));
		
		for (Color ct : elementToColor.values()) {
			if(ct !=null)
			if (Math.abs(ct.getBlue() - c.getBlue()) < COLOR_DIFFERENTIATION_FACTOR
					&& Math.abs(ct.getRed() - c.getRed()) < COLOR_DIFFERENTIATION_FACTOR
					&& Math.abs(ct.getGreen() - c.getGreen()) < COLOR_DIFFERENTIATION_FACTOR) {
				return getNewRandomColor();
			}
		}

		return c;
		
		} catch (Exception e) {
			
			return new Color(0,0,0);
		}
	}

	/**
	 * Shows a dialog through which the user can change/choose the colors of the
	 * processes.
	 */
	public void letUserChooseColors() {
		if (!hasColoring())
			return;

		CustomizeColorDialog dialog = new CustomizeColorDialog(this); 
		// /src/eshmun/gui/kripke/dialogs/CustomizeColorDialog.java
		dialog.setVisible(true); // Modal Operation, will stop execution until dialog is dismissed.

		HashMap<String, Color> chosenColors = dialog.getColors();
		for (String process : chosenColors.keySet()) {
			Color n = chosenColors.get(process);

			// keep current alpha if already has any.
			Color o = elementToColor.get(process);
			if (o != null)
				n = new Color(n.getRed(), n.getGreen(), n.getBlue(), o.getAlpha());

			elementToColor.put(process, n);
		}

		repaint();
	}

	/**
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 * 
	 *        The coloring mode used in Eshmun, currently has two options:
	 *        ScreenMode, and PrintMode.
	 * 
	 *        <p>
	 *        ScreenMode has high Brightness and Saturation (0.95, 0.95), and that
	 *        is optimal for on screen display and ease of differentiation.
	 *        </p>
	 * 
	 *        <p>
	 *        PrintMode has high Saturation (0.90) and low Brightness (0.45), this
	 *        makes the colors a bit harder to differentiate on first glance, but
	 *        ensure that they are visible when printed (whether on a colored or a
	 *        black-white printer).
	 *        </p>
	 */
	public static enum ColorMode {
		ScreenMode, PrintMode
	}

	/**
	 * Sets the current structure in the refinement
	 * 
	 * @param current
	 *            the current index of the structure in refinement.
	 */
	public void setRefinementCurrent(int current) {
		refinmentTab.setCurrent(current);
	}

}
