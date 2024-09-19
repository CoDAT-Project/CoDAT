package eshmun.gui.kripke;

import eshmun.Eshmun;
import eshmun.Eshmun.ColorMode;
import eshmun.gui.kripke.menus.FreePopupMenu;
import eshmun.gui.kripke.menus.StatePopupMenu;
import eshmun.gui.kripke.menus.TransitionPopupMenu;
import eshmun.gui.kripke.utils.DragAndDropHandler;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.kripke.utils.UndoManager;
import eshmun.gui.kripke.utils.graphphysics.GraphPhysics;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;
import eshmun.structures.kripke.KripkeState;
import eshmun.structures.kripke.KripkeStructure;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * This class represents a GraphPanel, responsible for drawing and
 * handling a single Kripke structure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class GraphPanel extends JPanel {
	//Symbolic constants
	/**
	 * auto generated serial version ID
	 */
	private static final long serialVersionUID = -5896734786703249168L;
	
	/**
	 * Default size of a GraphPanel.
	 */
	private static final Dimension DEFAULT_SIZE = new Dimension(8000, 6000);
	
	/**
	 * Default font size.
	 */
	public static final int FONT_SIZE = 15;
	
	/**
	 * Default transition arrow thickness.
	 */
	public static final int TRANSITION_THICKNESS = 2;
	
	/**
	 * Default thickness of a state's border.
	 */
	public static final int STATE_BORDER_THICKNESS = 1;
	
	
	/**
	 * Default extra thickness to add when a component is retained.
	 */
	public static final int RETAIN_EXTRA_THICKNESS = 3; 
	
	/*
	 * Attributes
	 */
	
	/**
	 * Name of current structure.
	 */
	private String structureName;
	
	/**
	 * The states of the current structure.
	 */
	private ArrayList<State> states;
	
	/**
	 * The transitions of the current strucutre.
	 */
	private ArrayList<Transition> transitions;
	
	/**
	 * A Mapping between a state's name and the state.
	 */
	private HashMap<String, State> stateNames;

	/*
	 * Handlers
	 */
	
	/**
	 * Drag and drop handler + mouse handler.
	 */
	private DragAndDropHandler dragAndDropHandler;
	
	/**
	 * Pop-up menu when clicked in empty space.
	 */
	private FreePopupMenu freePopupMenu;
	
	/**
	 * Pop-up menu when clicked in a state.
	 */
	private StatePopupMenu statePopupMenu;
	
	/**
	 * Pop-up menu when clicked in a transition.
	 */
	private TransitionPopupMenu transitionPopupMenu;
	
	/**
	 * Undo Manager.
	 */
	private UndoManager undoManager;
	
	/**
	 * Flags whether this graph panel is dedicated for a tableau.
	 */
	private boolean tableau;
	
	/*
	 * Getters for handlers
	 */
	
	public HashSet<String> getActionNames() {
		HashSet<String> result = new HashSet<String>();
		for(Transition t : transitions) {              //Iterate through all transitions
			result.addAll(t.getActionsAsCollection()); //Add in action name(s) of transition t, if any
		}
		
		return result;
	}
	
	/**
	 * @return the current drag and drop handler.
	 */
	public DragAndDropHandler getDragAndDropHandler() {
		return dragAndDropHandler;
	}
	
	/**
	 * @return the point in which the mouse recently clicked. 
	 */
	public Point getMousePopupPoint() {
		return dragAndDropHandler.getMousePopupPoint();
	}

	/**
	 * @return the current drag and drop handler.
	 */
	public FreePopupMenu getFreePopupMenu() {
		return freePopupMenu;
	}
	
	/**
	 * @return the current pop-up menu for points within states.
	 */
	public StatePopupMenu getStatePopupMenu() {
		return statePopupMenu;
	}
	
	/**
	 * @return the current pop-up menu for points within transitions.
	 */
	public TransitionPopupMenu getTransitionPopupMenu() {
		return transitionPopupMenu;
	}

	/**
	 * @return the current undo manager.
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}

	
	/*
	 * Selected Elements
	 */
	
	/**
	 * The currently selected State, null if no state is selected.
	 */
	private State selectedState;
	
	/**
	 * The currently selected Transition, null if no Transition is selected.
	 */
	private Transition selectedTransition;
	
	/*
	 * Display ports and utils
	 */
	
	/**
	 * The AffineTransformation representing only the zoom, useful for calculating ratios. 
	 */
	private AffineTransform zoomTransform;
	
	/**
	 * The scroll pane in which this Panel reside.
	 */
	private JScrollPane scroll;	
	
	/**
	 * The current instance of the application.
	 */
	Eshmun eshmun;
	
	/**
	 * Gets the current instance of the application.
	 * @return the current instance of the application.
	 */
	public Eshmun getEshmun() {
		return eshmun;
	}
	
	/**
	 * Sets if this is a tableau or not.
	 * @param tableau true if it is tableau, false otherwise.
	 */
	public void setTableau(boolean tableau) {
		this.tableau = tableau;
	}
	
	/**
	 * Gets if this a tableau.
	 * @return true if this is a tableau, false otherwise.
	 */
	public boolean isTableau() {
		return tableau;
	}
	
	/*
	 * Constructor
	 */
	/**
	 * Create a new GraphPanel that resides in scroll.
	 * 
	 * @param scroll the scroll pane in which this Panel reside.
	 * @param eshmunInstance the current instance of the application.
	 */
	public GraphPanel(JScrollPane scroll, Eshmun eshmunInstance) {
		super();
		
		this.eshmun = eshmunInstance;
		
		structureName = "";
		
		setPreferredSize(DEFAULT_SIZE);
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		
		this.scroll = scroll;
		this.zoomTransform = new AffineTransform();
		
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		stateNames = new HashMap<String, State>();
		
		dragAndDropHandler = new DragAndDropHandler();
		addMouseMotionListener(dragAndDropHandler);
		addMouseListener(dragAndDropHandler.mouseHandler);

		freePopupMenu = new FreePopupMenu(this);
		statePopupMenu = new StatePopupMenu(this);
		transitionPopupMenu = new TransitionPopupMenu(this);
		undoManager = new UndoManager(this);	
		
		tableau = false;
	}

	 public ArrayList<Transition> getTransitions(){
		 return transitions;
	 }
	 
	 public ArrayList<State> getStates(){
		 return states;
	 }
	
	/*
	 * Drawing Methods
	 */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		paintComponent(g);
	}

	/**
	 * Paints the components, i,e paints the states, transitions, movements and other graphics.
	 * @param g Graphics2D through which the graphics are drawn.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		((Graphics2D)g).transform(zoomTransform);
		super.paintComponent(g);
		
		g.setColor(new Color(230, 230, 230));
		for(int i = 80; i < getSize().getWidth(); i += 80) {
			g.drawLine(i, 0, i, (int) getSize().getHeight());
		}
		
		for(int i = 80; i < getSize().getHeight(); i += 80) {
			g.drawLine(0, i, (int) getSize().getWidth(), i);
		}
		
		paintComponents(g, false);
		
		// Draw little blue ellipse around selected State
		if(selectedState != null) {
			g.setColor(Color.BLUE);
			
			Point[] points = selectedState.getBoundries();
			for(Point p : points) {
				g.fillOval(p.x - 3, p.y - 3, 6, 6);
			}
			
			eshmun.writeInfo(selectedState.toString(getStructureName().trim()));
		}

		// Draw little blue ellipse around selected Transition
		if(selectedTransition != null) {
			if(eshmun.isTransitionColoredByProcess()) {
				String processName = selectedTransition.getProcessName();
				g.setColor(eshmun.getTransitionColor(processName));
			} else {
				g.setColor(Color.BLUE);
			}
			
			Point[] points = selectedTransition.getSelectPoints();
			for(Point p : points) {
				g.fillOval(p.x - 3, p.y - 3, 6, 6);
			}
			
			eshmun.writeInfo(selectedTransition.toString(getStructureName().trim()));
		}		
	}
	
	/**
	 * Paints the components, however this to an image if exportToImage is set to true.
	 * @param g Graphics2D through which the graphics are drawn.
	 * @param exportToImage flags if the output is an image file.
	 */
	public void paintComponents(Graphics g, boolean exportToImage) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(exportToImage) {
			eshmun.switchColorMode(ColorMode.ScreenMode);
			
			double minX = Integer.MAX_VALUE;
		    double minY = Integer.MAX_VALUE;
		    
		    //Get minY and minX
			g.setColor(Color.BLACK);
			for (int i = 0; i < states.size(); i++) {
				State s = states.get(i);
				
				minX = Math.min(minX, s.getLocation().getX());
				minY = Math.min(minY, s.getLocation().getY());
			}
			
			minX = Math.max(minX - 30, 0);
			minY = Math.max(minY - 30, 0);
			
			//Zoom and shift away from borders
			AffineTransform aff = new AffineTransform();
			aff.scale(Eshmun.EXPORT_IMAGE_SCALE_FACTOR, Eshmun.EXPORT_IMAGE_SCALE_FACTOR);
			aff.translate(-minX, -minY);
			
			((Graphics2D) g).transform(aff);

			super.paintComponent(g);
		}
		
		g.setFont(new Font("Times-Roman", Font.BOLD, FONT_SIZE));

		// Draw all Transitions
		g.setColor(Color.BLACK);
		
	    Stroke stroke = new BasicStroke(TRANSITION_THICKNESS);
	    ((Graphics2D) g).setStroke(stroke);
		for (Transition e : transitions) {
			e.draw(g);
		}

		stroke = new BasicStroke(STATE_BORDER_THICKNESS);
	    ((Graphics2D) g).setStroke(stroke);
	    
	    // Draw all States
		g.setColor(Color.BLACK);
		for (int i = 0; i < states.size(); i++) {
			states.get(i).draw(g);
		}
		
		eshmun.switchColorMode(ColorMode.ScreenMode);
	}
	
	/**
	 * Gets the maximum width so far reached.
	 * @return the maximum width so far reached.
	 */
	public int getDrawableWidth() {
		double maxX = 0;
		double minX = Integer.MAX_VALUE;
		
		for(State s : states) {
			if(s.getLocation().getX() + s.getWidth() + 30 > maxX) {
				maxX = s.getLocation().getX() + s.getWidth() + 30;
			}
			
			minX = Math.min(minX, s.getLocation().getX());
		}
		
		return (int) maxX - (int) minX + 30;
	}
	
	/**
	 * Gets the maximum height so far reached.
	 * @return the maximum height so far reached.
	 */
	public int getDrawableHeight() {
		double maxY = 0;
		double minY = Integer.MAX_VALUE;

		
		for(State s : states) {
			if(s.getLocation().getY() + s.getHeight() + 30 > maxY) {
				maxY = s.getLocation().getY() + s.getHeight() + 30;
			}
			
			minY = Math.min(minY, s.getLocation().getY());
		}
		
		return (int) maxY - (int) minY + 30;
	}

	/*
	 * Data Control Method
	 */
	
	/**
	 * Adds a State to the GraphPanel.
	 * 
	 * <p>
	 * Requires: s has a unique name.
	 * </p>
	 * 
	 * <p>Undo manger can undo this action.</p>
	 * 
	 * @param s state to be added.
	 */
	public void addState(State s) {
		states.add(s);
		stateNames.put(s.getName(), s);
		
		setSelectedState(s);
		
		repaint();

		undoManager.register();
	}
	
	/**
	 * Adds a State to the GraphPanel, if internal is true, it
	 * doesn't register it in the undo manager.
	 * 
	 * <p>
	 * Requires: s has a unique name.
	 * </p>
	 * 
	 * @param s state to be added.
	 * @param internal flags if the undo manager should be notified.
	 */
	
	public void addState(State s, boolean internal) {
		if(internal) {
			if(stateNames.containsKey(s.getName())) {
				return;
			}
			
			states.add(s);
			stateNames.put(s.getName(), s);
		} else {
			addState(s);
		}
	}

	/**
	 * Adds a transition to the graph. If the transition is duplicated nothing happen.
	 * 
	 * <p>Undo manger can undo this action.</p>
	 * 
	 * @param e transition to add.
	 */
	public void addTransition(Transition e) {
		Transition tmp = null;
		for(Transition e1 : transitions) {
			if(e.getTo().getName().equals(e1.getTo().getName())
					&& e.getFrom().getName().equals(e1.getFrom().getName())) {
				return; //Duplicate
			}
			
			if(e.getTo().getName().equals(e1.getFrom().getName())
					&& e.getFrom().getName().equals(e1.getTo().getName())) {
				tmp = e1;
			}
		}
		
		if(tmp != null)
			tmp.setHasDouble(true);
		
		transitions.add(e);
		
		setSelectedState(null);
		setSelectedTransition(e);
		
		repaint();

		undoManager.register();
	}
	
	/**
	 * Adds a transition to the graph. If the transition is duplicated nothing happen,
	 * if internal is true, it doesn't register it in the undo manager.
	 * @param e transition to add.
 	 * @param internal flags if the undo manager should be notified.
	 */
	public void addTransition(Transition e, boolean internal) {
		if(internal) {
			Transition tmp = null;
			for(Transition e1 : transitions) {
				if(e.getTo().getName().equals(e1.getTo().getName())
						&& e.getFrom().getName().equals(e1.getFrom().getName())) {
					return; //Duplicate
				}
				
				if(e.getTo().getName().equals(e1.getFrom().getName())
						&& e.getFrom().getName().equals(e1.getTo().getName())) {
					tmp = e1;
				}
			}
			
			if(tmp != null)
				tmp.setHasDouble(true);
			
			transitions.add(e);
		} else {
			addTransition(e);
		}
	}
	
	/**
	 * Delete a state from the GraphPanel.
	 * 
	 * <p>Undo manger can undo this action.</p>
	 * 
	 * @param s state to be deleted.
	 */
	public void deleteState(State s) {
		Collection<Transition> transitionsToDelete = new ArrayList<Transition>();
		
		if(states.contains(s)) {
			for(Transition e : transitions) {
				if(e.getTo() == s || e.getFrom() == s) {
					transitionsToDelete.add(e);
				}
			}
			
			deleteTransitions(transitionsToDelete);
			stateNames.remove(s.getName());
			states.remove(s);
		}
		
		selectedState = null;
		
		repaint();
		undoManager.register();
	}
	
	/**
	 * Delete a transition from the GraphPanel.
	 * 
	 * <p>Undo manger can undo this action.</p>
	 * 
	 * @param e transition to be deleted.
	 */
	public void deleteTransition(Transition e) {
		if(e.isDoubled() || e.hasDouble()) {
			for(Transition e1 : transitions) {
				if(e.getTo().getName().equals(e1.getFrom().getName())
						&& e.getFrom().getName().equals(e1.getTo().getName())) {
					e1.setHasDouble(false);
					e1.setDoubled(false);
				}
			}
		}
		
		transitions.remove(e);
		
		selectedTransition = null;
		
		repaint();
		undoManager.register();
	}
	
	/**
	 * Delete all the given transitions from the GraphPanel.
	 *  
	 * @param trans collection of transitions to be deleted.
	 */
	public void deleteTransitions(Collection<Transition> trans) {
		transitions.removeAll(trans);
		
		selectedTransition = null;
	}
	
	/**
	 * Clears the content of this GraphPanel.
	 * 
	 * <p>Undo manger can undo this action.</p>
	 */
	public void clear() {	
		tableau = false;
		
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		stateNames = new HashMap<String, State>();
		
		selectedState = null;
		selectedTransition = null;
		
		undoManager.register();
		
		repaint();
	}

	/**
	 * Gets the state that occupies the given location, if more than one state occupy 
	 * this point, returns the most recent.
	 * 
	 * @param p the location.
	 * @return the state that occupies p. 
	 */
	public State getStateByLocation(Point p) {
		for(int i = states.size(); i > 0; i--) {
			State s = states.get(i - 1);
			if(s.contains(p))
				return s;
		}

		return null;
	}
	
	/**
	 * Gets the transition that occupies the given location, if more than one transition occupy 
	 * this point, returns the oldest.
	 * 
	 * @param p the location.
	 * @return the transition that occupies p. 
	 */
	public Transition getTransitionByLocation(Point p) {
		for (Transition e : transitions) {
			if (e.contains(p))
				return e;
		}

		return null;		
	}
	
	/**
	 * Checks if the transition going from state from to state to is doubled (an opposite transition exist).
	 * 
	 * @param from start state.
	 * @param to end state.
	 * @return true if the specified transition is doubled, false otherwise. 
	 */
	public boolean isDoubled(State from, State to) {
		//Flip from and to, then look for an transition betweem them.
		State tmp = to; 
		to = from;
		from = tmp;
		
		for(Transition e : transitions) {
			if(e.getTo() == to && e.getFrom() == from) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Translate point from a coordinate system with the current zoom, 
	 * to a point in the original system.
	 * 
	 * @param p point to translate.
	 * @return the translated point.
	 */
	public Point translatePoint(Point p) {
		double scaleX = zoomTransform.getScaleX();
		double scaleY = zoomTransform.getScaleY();
		
		double x = p.x / scaleX; double y = p.y / scaleY;
		
		return new Point((int) x, (int) y);
	}
	
	/**
	 * Translate point from a coordinate system with no zoom.
	 * to a point in the system with the current zoom.
	 * 
	 * @param p point to translate.
	 * @return the translated point.
	 */
	public Point translatePointReverse(Point p) {
		double scaleX = zoomTransform.getScaleX();
		double scaleY = zoomTransform.getScaleY();
		
		double x = p.x * scaleX; double y = p.y * scaleY;
		
		return new Point((int) x, (int) y);
	}

	/**
	 * Get the zoom scales.
	 * @return an array with the zoom scales : [Xscale, Yscale].
	 */
	public double[] getScales() {
		return new double[] { zoomTransform.getScaleX(), zoomTransform.getScaleY()  }; 
	}
	
	/**
	 * Zoom in, no specific focus point.
	 */
	public void zoomIn() {
		zoomIn(null);
	}
	
	/**
	 * Zoom in, with the focus point being newLocation (the container would be moved
	 * to have this point almost centered), if null no focus point is considered.
	 * 
	 * @param newLocation the point of focus.
	 */
	public void zoomIn(Point newLocation) {
		if(newLocation != null) {
			double x = newLocation.x * zoomTransform.getScaleX();
			double y = newLocation.y * zoomTransform.getScaleY();
			
			getScrollPane().getHorizontalScrollBar().setValue((int)x);
        	getScrollPane().getVerticalScrollBar().setValue((int)y);
		}
		
		zoomTransform.scale(1.33, 1.33);
		
		eshmun.getZoomToolBar().callBack(new Object[] { zoomTransform.getScaleX() });
		
		repaint();
	}
	
	/**
	 * Zoom out, no specific focus point.
	 */
	public void zoomOut() {
		zoomOut(null);
	}
	
	/**
	 * Zoom out, with the focus point being newLocation (the container would be moved
	 * to have this point almost centered), if null no focus point is considered.
	 * 
	 * @param newLocation the point of focus.
	 */
	public void zoomOut(Point newLocation) {		
		if(newLocation != null) {
			double x = newLocation.x * 0.75 * 0.75;
			double y = newLocation.y * 0.75 * 0.75;
			
			getScrollPane().getHorizontalScrollBar().setValue((int)x);
        	getScrollPane().getVerticalScrollBar().setValue((int)y);
        }
		
		zoomTransform.scale(0.75, 0.75);
		
		eshmun.getZoomToolBar().callBack(new Object[] { zoomTransform.getScaleX() });
		
		repaint();
	}
	
	/**
	 * Reset zoom to no zoom (scales of 1).
	 */
	public void resetZoom() {
		double scaleX = zoomTransform.getScaleX();
		double scaleY = zoomTransform.getScaleY();
		
		zoomTransform.scale(1/scaleX, 1/scaleY);
		
		eshmun.getZoomToolBar().callBack(new Object[] { 1.0 });
		
		repaint();
	}
	
	/**
	 * Returns the scaling factor for zoom.
	 * @return the scale
	 */
	public double getScale() {
		return zoomTransform.getScaleX();
	}
	
	/**
	 * zoom in according to the given scale.
	 * 
	 * @param scale the scale to zoom by. the same scale is applied for X, Y axis.
	 */
	public void applyScale(double scale) {
		double scaleX = zoomTransform.getScaleX();
		double scaleY = zoomTransform.getScaleY();
		
		zoomTransform.scale(scale * (1/scaleX), scale * (1/scaleY));
		
		eshmun.getZoomToolBar().callBack(new Object[] { scale });
		
		repaint();
	}
	
	/**
	 * Saves the structure inside this graphPanel, Shows a save dialog then writes a save object into the specified file.
	 * @param specifications the specifications to be saved.
	 * @param structural the structural formula to be saved.
	 */
	public void save(String specifications, String structural) {
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showSaveDialog(null);
		if(dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				getSaveObject(specifications, structural).save(chooser.getSelectedFile().getAbsolutePath());
				
				eshmun.writeMessage("Saved Successfully!", false);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Saving File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}
	}
	
	/**
	 * Constructs a saveObject that could be used to save the structure in this GraphPanel.
	 * @param specifications the specifications to be saved.
	 * @param structural the structural formula to be saved.
	 * @return a saveObject to save the structure in this GraphPanel.
	 */
	public SaveObject getSaveObject(String specifications, String structural) {
		return new SaveObject(structureName, copyStates().toArray(new State[0]), 
				copyTransitions().toArray(new Transition[0]), specifications, structural);
	}
	
	/**
	 * Loads the structure, Shows a load dialog then reads a save object from the specified file.
	 */
	public void load() {
		tableau = false;
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showOpenDialog(null);
		if(dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				load(Saveable.load(chooser.getSelectedFile().getAbsolutePath()));
				eshmun.writeMessage("Opened Successfully!", false);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Opening File: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Saved File has Unrecognizable Format: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassCastException e2) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Saved File has Unrecognizable Format: "+e2.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}
	}
	
	/**
	 * Loads a structure from the given save object into the GraphPanel.
	 * @param saveable the saveObject containing the states and transitions.
	 */
	public void load(Saveable saveable) {
		tableau = false;
		if(saveable.getStructureType() != StructureType.Kripke) {
			Eshmun.eshmun.load(saveable);
			return;
		}
		
		SaveObject obj = (SaveObject) saveable;
		
		setStructureName(obj.getStructureName());

		states = obj.getStates();
		transitions = obj.getTransitions();
		
		stateNames = new HashMap<String, State>();
		
		for(State s : states) {
			stateNames.put(s.getName(), s);
		}
		
		selectedState = null;
		selectedTransition = null;
		
		undoManager = new UndoManager(this);	
		
		repaint();
		
		String specs = obj.getSpecifications();
		eshmun.setSpecificationFormula(specs);
		eshmun.setStructureFormula(obj.getStructural());
		
		for(Transition t : transitions) {
			t.updateProcess(getStructureName());
		}
	}
	
	/**
	 * Loads a structure from the given save object into the GraphPanel.
	 * @param saveable the saveObject containing the states and transitions.
	 * @param internal if true, then loading is internal, no repaint, no undo manager.
	 */
	public void load(Saveable saveable, boolean internal) {
		tableau = false;
		if(!internal) {
			load(saveable);
			return;
		}
		
		if(saveable.getStructureType() != StructureType.Kripke) {
			Eshmun.eshmun.load(saveable);
			return;
		}
				
		UndoManager tmp = undoManager;
		undoManager = new UndoManager(this);
		
		SaveObject obj = (SaveObject) saveable;
		
		setStructureName(obj.getStructureName());
		states = obj.getStates();
		transitions = obj.getTransitions();
		
		stateNames = new HashMap<String, State>();
		
		for(State s : states) {
			stateNames.put(s.getName(), s);
		}
		
		selectedState = null;
		selectedTransition = null;
		
		String specs = obj.getSpecifications();
		eshmun.setSpecificationFormula(specs);
		
		undoManager = tmp;
	}
	
	/**
	 * Construct a string representation / definition of the structure inside.
	 * @param specifications the specifications of the structure.
	 * @return a string representation of the structure.
	 */
	public String constructDefinition(String specifications) {
		String definition = structureName + System.lineSeparator();
		
		definition += "states:" + System.lineSeparator();
		for(State s : states) {
			definition += s.constructDefinition() + System.lineSeparator();
		}
		
		definition += "transitions:" + System.lineSeparator();
		for(Transition t : transitions) {
			definition += t.constructDefinition() + System.lineSeparator();
		}
		
		definition += "specifications:" + System.lineSeparator();
		definition += specifications + System.lineSeparator();
		
		return definition;
	}
	
	/**
	 * Loads a Structure from a string representation of the structure.
	 * 
	 * @param structureName the structure Name.
	 * @param statesDef the states definition.
	 * @param transitionsDef the transitions definition.
	 */
	public void loadDefinition(String structureName, String statesDef, String transitionsDef) {	
	 
		tableau = false;
		setStructureName(structureName);
		
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		stateNames = new HashMap<String, State>();
				
		selectedState = null;
		selectedTransition = null;
		
		Point location = new Point(10, 10);
		String[] sDefs =  statesDef.split(";");
		for(String d : sDefs) {
			d = d.trim();
			//d = d.replace(":=", "##&=&!@#");
			
			String[] ds = d.split(":(?!=)");
			
			String name = ds[0]; 
			String labels = ds[1]; //ChangedBy Chukri 
			//String labels = ds[1];
			
//			d = d.trim();
//			d = d.replace(":=", "##&=&!@#");
//			String[] ds = d.split(":");
//			String name = ds[0].replace("##&=&!@#", ":="); 
//			String labels = ds[1].replace("##&=&!@#", ":=");
			
			boolean start = false;
			if(ds.length > 2) {
				start = Boolean.parseBoolean(ds[2]);
			}
					
			boolean retain = false;
			if(ds.length > 3) {
				retain = Boolean.parseBoolean(ds[3]);
			}	
			
			String predicate = "";
			if(ds.length > 4) {
				predicate =  ds[4] ;
			}	

			State s = new State(name, labels,predicate, location, start, retain);
			location = new Point(location);
			
			if(location.x > location.y) {
				location = new Point(location.x, location.y + 150);
			} else if (location.x < location.y) {
				location = new Point(location.x + 75, location.y);
			} else {
				location = new Point(location.x + 75, location.y);
			}
			
			addState(s, true);
		}
		
		sDefs = transitionsDef.split(";");
		for(String d : sDefs) {
			String[] ds = d.split(":(?!=)");

			String from = ds[0];
			String to = ds[1];
			
			boolean retain = false;
			if(ds.length > 2) {
				retain = Boolean.parseBoolean(ds[2]);
			}
			
			String actionName = "";
			if(ds.length > 3) {
				actionName = ds[3];
			}
			
			String command = "";
			if(ds.length > 4) {
				command = ds[4];
			}
			State f = getStateByName(from);
			State t = getStateByName(to);
			boolean doubled = isDoubled(f, t);
			
			Transition trans = new Transition(f, t, doubled, getStructureName(), actionName, retain, false,command);
			addTransition(trans, true);
		}
		
		GraphPhysics graphPhysics = new GraphPhysics();
		graphPhysics.autoSpace(stateNames, transitions);
		
		undoManager = new UndoManager(this);		
		repaint();
	}
	
	/**
	 * Sets the selected state to state.
	 * @param state the new selected state.
	 */
	public void setSelectedState(State state) {
		eshmun.writeInfo("");
		if(state != null) {
			eshmun.writeInfo(state.toString(getStructureName().trim()));
		}
		
		selectedState = state;
	}
	
	/**
	 * Gets the selected state.
	 * @return the selected state.
	 */
	public State getSelectedState() {
		return selectedState;
	}
	
	/**
	 * Sets the selected transition to transition.
	 * @param transition the new selected transition.
	 */
	public void setSelectedTransition(Transition transition) {
		eshmun.writeInfo("");
		if(transition != null) {
			eshmun.writeInfo(transition.toString(getStructureName().trim()));
		}
		
		selectedTransition = transition;
	}
	
	/**
	 * Gets the selected transition.
	 * @return the selected transition.
	 */
	public Transition getSelectedTransition() {
		return selectedTransition;
	}

	/**
	 * Checks if the given state's name exists or not.
	 * @param name the name to check.
	 * @return true if the name is new, false if it exists.
	 */
	public boolean verifyNewName(String name) {
		return ! stateNames.containsKey(name);
	}
	
	/**
	 * Propagate the change of state name to the state-name mapping.
	 * @param oldName the old name.
	 * @param newName the new name.
	 */
	public void updateName(String oldName, String newName) {
		State t = stateNames.remove(oldName);
		stateNames.put(newName, t);
	}
	
	/**
	 * Checks if the given state's labels exists or not.
	 * @param labels the labels to check.
	 * @return true if the labels is new, false if it exists.
	 */
	public boolean verifyNewLabel(String labels) {
		List<String> n = Arrays.asList(labels.split(","));
		
		for(State s : states) {
			List<String> t = Arrays.asList(s.getLabels().split(","));
			if(t.containsAll(n) && n.containsAll(t)) return false;
		}
		
		return true;
	}
	
	/**
	 * Auto space-out the graph and draws it using GraphPhysics.
	 */
	public void autoFormat() {
		GraphPhysics auto = new GraphPhysics();
		auto.autoSpace(stateNames, transitions);
		
		undoManager.register();
		repaint();
	}
	
	/*
	 * Attributes Exposures
	 */
	
	/**
	 * Gets the structure name.
	 * @return the structure name.
	 */
	public String getStructureName() {
		return structureName;
	}
	
	/**
	 * Set the structure name.
	 * @param structureName the structure name.
	 */
	public void setStructureName(String structureName) {
		this.structureName = structureName;
		this.scroll.setName(structureName);
		if(eshmun != null && eshmun.getStructureType() == StructureType.MultiKripke) {
			JTabbedPane tabbed = ((JTabbedPane) this.scroll.getParent());
			if(tabbed == null)
				return;
			
			int index = tabbed.getSelectedIndex();
			
			if(index != -1)
				tabbed.setTitleAt(index, structureName);
		}
		
		for(Transition t : transitions) {
			t.updateProcess(this.structureName);
		}
	}

	/**
	 * Maps state name to the most recent copy of the state made by copyStates(),
	 * Useful for transition copying (ensure the copied states and copied transitions match).
	 */
	HashMap<String, State> map;
	
	/**
	 * returns a copy of the states (deep copy), saves the new copied stated in this.map .
	 * @return a copy of the states.
	 */
	public ArrayList<State> copyStates() {
		map = new HashMap<String, State>();
		ArrayList<State> result = new ArrayList<State>(states.size());
		
		for(State s : states) {
			State x = s.copy();
			result.add(x);
			map.put(x.getName(), x);
		}
				
		return result;
	}

	/**
	 * returns a copy of the transitions (deep copy).
	 * @return a copy of the transitions.
	 */
	public ArrayList<Transition> copyTransitions() {
		ArrayList<Transition> result = new ArrayList<Transition>(transitions.size());
		
		for(Transition e : transitions) {
			String textTo = e.getTo().getName();
			String textFrom = e.getFrom().getName();
			
			result.add(e.copy(map.get(textFrom), map.get(textTo)));
		}
				
		return result;
	}
	
	/**
	 * returns a copy of the mapping between state names and states.
	 * @param states the copied states in order to match pointers in the new states.
	 * @return the copied names to states mapping.
	 */
	public HashMap<String, State> copyStateNames(ArrayList<State> states) {
		if(states == null)
			return new HashMap<String, State>(stateNames);
		
		HashMap<String, State> result = new HashMap<String, State>();
		for(State s : states) {
			result.put(s.getName(), s);
		}
		
		return result;
	}

	/**
	 * set the states inside the GraphPanel.
	 * @param states the new states.
	 */
	public void setStates(ArrayList<State> states) {
		tableau = false;
		
		this.states = states;
		this.states = copyStates();
	}

	/**
	 * set the transitions inside the GraphPanel.
	 * @param transitions the new transitions.
	 */
	public void setTransitions(ArrayList<Transition> transitions) {
		tableau = false;
		
		this.transitions = transitions;
		this.transitions = copyTransitions();
	}
	
	/**
	 * set the mapping between states and their names inside the GraphPanel.
	 * @param stateNames the new mapping between states and their names.
	 */
	public void setStateNames(HashMap<String, State> stateNames) {
		tableau = false;
		
		this.stateNames = stateNames;
	}

	/**
	 * Gets the state by index.
	 * @param i index of state
	 * @return the state at index i.
	 */
	public State getState(int i) {
		return states.get(i);
	}
	
	/**
	 * Gets the state by name.
	 * @param name the name of the state.
	 * @return the state with the given name.
	 */
	public State getStateByName(String name) {
		for(State s : states) {
			if(s.getName().equals(name)) {
				return s;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the index of the given state.
	 * @param s the state to get its index.
	 * @return the index of s.
	 */
	public int getStateIndex(State s) {
		for(int i = 0; i < states.size(); i++) {
			if(s == states.get(i))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * gets the JScrollPane this GraphPanel reside in.
	 * @return the scrollPane this GraphPanel is in.
	 */
	public JScrollPane getScrollPane() {
		return scroll;
	}
			
	/*
	 * POST KRIPKE REPAIR
	 */
	
	/**
	 * Dash the states and transitions given (by var name).
	 * @param deletions a collection of the names of states and transitions to be dashed. 
	 * @return an exact list of the names of the states and transitions that were dashed.
	 */
	public Collection<String> dash(Collection<String> deletions) {	
		return dash(deletions, false);
	}
	
	/**
	 * Dash the states and transitions given (by var name).
	 * @param deletions a collection of the names of states and transitions to be dashed.
	 * @param internal if true then dashing is internal, no undo, no repaint. 
	 * @return an exact list of the names of the states and transitions that were dashed.
	 */
	public Collection<String> dash(Collection<String> deletions, boolean internal) {
		//List of start states.
		ArrayList<State> startStates = new ArrayList<State>();
		
		//By var name, state -> out transitions.
		HashMap<String, Collection<Transition>> reachability = new HashMap<String, Collection<Transition>>();
		HashMap<String, Collection<Transition>> revReachability = new HashMap<String, Collection<Transition>>();
		
		for(State s : states) {
			s.setDashed(true);
			
			String varName = s.getVariableName(structureName);
			reachability.put(varName, new HashSet<Transition>());
			revReachability.put(varName, new HashSet<Transition>());
			
			if(s.getStart())
				startStates.add(s);
		}
		
		for(Transition t : transitions) {
			t.setDashed(true);
			String fromVar = t.getFrom().getVariableName(structureName);
			reachability.get(fromVar).add(t);
			
			String toVar = t.getTo().getVariableName(structureName);
			revReachability.get(toVar).add(t);
		}
		
		//Ensure reachability, delete enreachable states and transitions.
		LinkedList<State> bfsQueue = new LinkedList<State>();
		HashSet<String> visited = new HashSet<String>(); //By var name
		HashSet<String> kept = new HashSet<String>();
		
		for(State start : startStates) {
			if(!visited.contains(start.getVariableName(structureName))) {
				bfsQueue.push(start);
			}
			
			while(!bfsQueue.isEmpty()) {
				State s = bfsQueue.pop();
				String varName = s.getVariableName(structureName);
				
				visited.add(varName);
				if(!deletions.contains(varName)) {
					kept.add(varName);
					s.setDashed(false);
					for(Transition t : reachability.get(varName)) {
						String tVarName = t.getVariableName(structureName);
						if(!deletions.contains(tVarName)) {			
							kept.add(tVarName);
							t.setDashed(false);
							State next = t.getTo();
							if(!visited.contains(next.getVariableName(structureName))) {
								bfsQueue.push(next);
							}
						}
					}
				}
			}
		}
		
		//Ensure That every state has at least one outgoing transition, delete those who don't.
		Stack<State> toCheck = new Stack<State>();
		for(State s : states) {
			toCheck.push(s);
		}
		
		while(!toCheck.isEmpty()) {
			State s = toCheck.pop();
			if(s.isDashed()) continue;
			
			String sVarName = s.getVariableName(structureName);
			
			boolean hasTransition = false;
			for(Transition t : reachability.get(sVarName)) {
				if(!t.isDashed()) {
					hasTransition = true;
					break;
				}
			}
			
			if(!hasTransition) {
				kept.remove(sVarName);
				s.setDashed(true);
				for(Transition t : revReachability.get(sVarName)) {
					if(!t.isDashed()) {
						t.setDashed(true);
						kept.remove(t.getVariableName(structureName));
						toCheck.push(t.getFrom());
					}
				}
			}
		}
		
		if(!internal) {
			undoManager.register();
			repaint();
		}
		
		//The actual deleted transitions
		deletions = new HashSet<String>();
		for(State s : states) {
			String varName = s.getVariableName(structureName);
			if(s.isDashed())
				deletions.add(varName);
			
			
			for(Transition t : reachability.get(varName)) {
				String tVarName = t.getVariableName(structureName);
				if(t.isDashed())
					deletions.add(tVarName);
			}
		}
		
		return deletions;
	}
	
	/**
	 * Dotes the states and transitions given (by var name).
	 * @param dotted a collection of the names of states and transitions to be dashed.
	 */
	public void dot(Collection<String> dotted) {
		for(State s : states) {
			String varName = s.getVariableName(structureName);
			if(dotted.contains(varName)) s.setDotted(true);
		}
		
		for(Transition t : transitions) {
			String varName = t.getVariableName(structureName);
			if(dotted.contains(varName)) t.setDotted(true);
		}
	}
	
	/**
	 * Stripe the background of states given (by var name), transitions are not supported.
	 * @param striped a collection of the names of states to be stripped.
	 */
	public void stripe(Collection<String> striped) {
		for(State s : states) { //Restores internally.
			String varName = s.getVariableName(structureName);
			s.setStriped(striped.contains(varName));
		}
		
		repaint();
	}
	
	/**
	 * Restores the transitions that were already dashed.
	 */
	public void restore() {
		for(Transition t : transitions) {
			t.setDashed(false);
			t.setDotted(false);
		}
		
		for(State s : states) {
			s.setDashed(false);
			s.setDotted(false);
			s.setStriped(false);
		}
		
		undoManager.register();
		repaint();
	}
	
	/**
	 * Deletes the dashed transitions and states.
	 */
	public void deleteDashed() {
		for(int i = 0; i < transitions.size(); i++) {
			Transition t = transitions.get(i);
			if(t.isDashed()) {
				if(t.isDoubled() || t.hasDouble()) {
					for(Transition e1 : transitions) {
						if(t.getTo().getName().equals(e1.getFrom().getName())
								&& t.getFrom().getName().equals(e1.getTo().getName())) {
							e1.setHasDouble(false);
							e1.setDoubled(false);
						}
					}
				}
				
				transitions.remove(t);
				i--;
			}
		}
		
		for(int i = 0; i < states.size(); i++) {
			State s = states.get(i);
			
			if(s.isDashed()) {				
				for(int j = 0; j < transitions.size(); j++) {
					Transition e = transitions.get(j);
					if(e.getTo() == s || e.getFrom() == s) {
						if(e.isDoubled() || e.hasDouble()) {
							for(Transition e1 : transitions) {
								if(e.getTo().getName().equals(e1.getFrom().getName())
										&& e.getFrom().getName().equals(e1.getTo().getName())) {
									e1.setHasDouble(false);
									e1.setDoubled(false);
								}
							}
						}
						
						j--;
						transitions.remove(e);
					}
				}
				
				stateNames.remove(s.getName());
				states.remove(s);
				i--;
			}
		}
		
		undoManager.register();
		repaint();
	}
	
	/**
	 * assigns a new kripke structure to display.
	 * @param kripkeStructure the new kripke structure to display
	 */
	public void assignKripke(KripkeStructure kripkeStructure) {
		tableau = false;
		
		selectedState = null;
		selectedTransition = null;
		
		resetZoom();
		
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		stateNames = new HashMap<String, State>();
		
		scroll.getVerticalScrollBar().setValue(0);
		scroll.getHorizontalScrollBar().setValue(0);
		
		Point location = new Point(10, 10);
		
		int diagonal = 1;
		int current = 0;
		for(AbstractState state : kripkeStructure.getStates()) {
			KripkeState kState = (KripkeState) state;
			
			String labels = "";
			Collection<String> l = kState.getStateLabelAsCollection();
			
			for(String s : l)
				labels += s + ",";
			
			if(labels.endsWith(","))
				labels = labels.substring(0, labels.length() - 1);
			
			State s = new State(kState.getName(), labels, location, kState.isStartState(), kState.isRetain());
			
			states.add(s);
			stateNames.put(s.getName(), s);
			
			int x = 10;
			int y = 10;
			
			current++;
			if(current == diagonal) {
				diagonal++;
				current = 0;
			}
			
			x += 120 * (diagonal - current - 1);
			y += 120 * (current);			
			
			location = new Point(x, y);
		}
		
		
		for(AbstractState state : kripkeStructure.getStates()) {
			State s = stateNames.get(state.getName());
			for(AbstractTransition t : state.getOutTransition()) {
				State end = stateNames.get(t.getTo().getName());
				
				Transition tran = new Transition(s, end, isDoubled(s, end), getStructureName());
				transitions.add(tran);
			}
			
		}
				
		dragAndDropHandler = new DragAndDropHandler();
				
		autoFormat();
		
		undoManager.register();
		
		repaint();
	}
	
	//COLOR TRANSITIONS BY PROCESS
	
	/**
	 * Updates the transition process for all transitions of the given state.
	 * @param s the state to update its processes.
	 */
	public void updateTransitionProcess(State s) {
		for(Transition e : transitions) {
			if(e.getTo() == s || e.getFrom() == s) {
				e.updateProcess();
			}
		}
	}
}