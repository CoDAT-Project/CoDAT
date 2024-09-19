package eshmun.gui.synchronizationskeletons;

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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eshmun.Eshmun;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.synchronizationskeletons.bars.SynchronizationZoomToolBar;
import eshmun.gui.synchronizationskeletons.menus.FreePopupMenu;
import eshmun.gui.synchronizationskeletons.menus.StatePopupMenu;
import eshmun.gui.synchronizationskeletons.menus.TransitionPopupMenu;
import eshmun.gui.synchronizationskeletons.utils.DragAndDropHandler;
import eshmun.gui.synchronizationskeletons.utils.UndoManager;
import eshmun.gui.utils.models.skeleton.SkeletonSaveObject;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

/**
 * The drawing panel in which a Synchronization Skeleton is displayed.
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SynchronizationSkeletonPanel extends JPanel {

	/*
	 * CONSTANTS
	 */
	
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = -9168504900955801423L;
	
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
	 * The spacing between each two seperate skeletons
	 */
	public static final int SPACING = 120;
		
	/*
	 * STATES AND TRANSITIONS
	 */
	
	/**
	 * The states inside this Synchronization Skeleton
	 */
	private ArrayList<SkeletonState> states;
	
	/**
	 * The start states inside this Synchronization Skeleton
	 */
	private ArrayList<SkeletonState> startStates;
	
	/**
	 * The transitions inside this Synchronization Skeleton
	 */
	private ArrayList<SkeletonTransition> transitions;
	
	/**
	 * The currently selected state (null if nothing is selected).
	 */
	private SkeletonState selectedState;
	
	/**
	 * The currently selected transition (null if nothing is selected).
	 */
	private SkeletonTransition selectedTransition;
	
	/*
	 * VIEWPORTS
	 */
	
	/**
	 * The frame this panel belongs to.
	 */
	private SynchronizationSkeletonFrame frame;
	
	/**
	 * The AffineTransformation representing only the zoom, useful for calculating ratios. 
	 */
	private AffineTransform zoomTransform;
	
	/**
	 * The scroll pane in which this Panel reside.
	 */
	private JScrollPane scroll;	
	
	/*
	 * HANDLERS
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
	 * The zoom toolbar.
	 */
	private SynchronizationZoomToolBar zoomToolbar;
		
	/*
	 * FLAGS
	 */
		
	/**
	 * Flags whether the skeleton is to be formatted on next draw.
	 */
	private boolean toBeFormatted;
	
	/**
	 * Creates a new Synchronization Skeleton Panel.
	 * @param scroll the enclosing scroll pane.
	 * @param frame the origin skeleton frame.
	 */
	public SynchronizationSkeletonPanel(JScrollPane scroll, SynchronizationSkeletonFrame frame) {
		setPreferredSize(DEFAULT_SIZE);
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		
		states = new ArrayList<SkeletonState>();
		startStates = new ArrayList<SkeletonState>();
		transitions = new ArrayList<SkeletonTransition>();
		
		selectedState = null;
		selectedTransition = null;
		
		this.frame = frame;
		this.scroll = scroll;
		this.zoomTransform = new AffineTransform();
		
		this.scroll.setViewportView(this);
		
		dragAndDropHandler = new DragAndDropHandler();
		addMouseMotionListener(dragAndDropHandler);
		addMouseListener(dragAndDropHandler.mouseHandler);
		
		freePopupMenu = new FreePopupMenu(this);
		statePopupMenu = new StatePopupMenu(this);
		transitionPopupMenu = new TransitionPopupMenu(this);
		undoManager = null;	
		
		toBeFormatted = false;
	}

	/*
	 * Drawing & Painting.
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
		
		//Draw Gray Grid in the background.
		g.setColor(new Color(230, 230, 230));
		for(int i = 80; i < getSize().getWidth(); i += 80) {
			g.drawLine(i, 0, i, (int) getSize().getHeight());
		}
		
		for(int i = 80; i < getSize().getHeight(); i += 80) {
			g.drawLine(0, i, (int) getSize().getWidth(), i);
		}
		
		//Reset state counters
	    g.setColor(Color.BLACK);
		for (int i = 0; i < states.size(); i++) {
			states.get(i).resetInOffset();;
			states.get(i).resetOutOffset();;
		}
		
		if(toBeFormatted)
			autoFormat(g);
		
		else 
			for(SkeletonTransition t : transitions)
				t.update(null);
						
		//Paint the actual Components
		paintComponents(g, false);
		
		//Selected components get little blue dots around them
		
		if(selectedState != null) { //selected state
			g.setColor(Color.BLUE);
			
			Point[] points = selectedState.getBoundries();
			for(Point p : points) {
				g.fillOval(p.x - 3, p.y - 3, 6, 6);
			}
		}

		if(selectedTransition != null) { //selected transition
			g.setColor(Color.BLUE);

			Point[] points = selectedTransition.getSelectPoints();
			for(Point p : points) {
				g.fillOval(p.x - 3, p.y - 3, 6, 6);
			}
		}
		
		if(undoManager == null) 
			undoManager = new UndoManager(this);
	}
	
	/**
	 * Paints the components, however this to an image if exportToImage is set to true.
	 * @param g Graphics2D through which the graphics are drawn.
	 * @param exportToImage flags if the output is an image file.
	 */
	public void paintComponents(Graphics g, boolean exportToImage) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(exportToImage) {			
			double minX = Integer.MAX_VALUE;
		    double minY = Integer.MAX_VALUE;
		    
		    //Get minY and minX
			g.setColor(Color.BLACK);
			for (int i = 0; i < states.size(); i++) {
				SkeletonState s = states.get(i);
				
				minX = Math.min(minX, s.getLocation().getX());
				minY = Math.min(minY, s.getLocation().getY());
			}
			
			for (SkeletonTransition t : transitions) {
				minY = Math.min(t.getPeaks()[1], minY);
			}
			
			minX = Math.max(minX - 30 - 40, 0) - 17;
			minY = Math.max(minY - 30 - 40, 0) - 23;
			
			//Zoom and shift away from borders
			AffineTransform aff = new AffineTransform();
			aff.scale(Eshmun.EXPORT_IMAGE_SCALE_FACTOR, Eshmun.EXPORT_IMAGE_SCALE_FACTOR);
			aff.translate(-minX, -minY);
			
			((Graphics2D) g).transform(aff);

			super.paintComponent(g);		
		}
		
		g.setFont(new Font("Times-Roman", Font.BOLD, FONT_SIZE));
		g.setColor(Color.BLACK);
		
	    Stroke stroke = new BasicStroke(TRANSITION_THICKNESS);
	    ((Graphics2D) g).setStroke(stroke);
	    
	    //Draw all Transitions
		for (SkeletonTransition e : transitions) {
			e.draw(g);
		}

		stroke = new BasicStroke(STATE_BORDER_THICKNESS);
	    ((Graphics2D) g).setStroke(stroke);
	    
	    // Draw all States
		g.setColor(Color.BLACK);
		for (int i = 0; i < states.size(); i++) {
			states.get(i).draw(g);
		}
		
		//Paint Contours around skeletons
		paintContours(g);
	}
	
	/**
	 * Paints the contours (dashed rectangular shapes surrounding the skeletons).
	 * @param g graphics to paint in.
	 */
	public void paintContours(Graphics g) {
		for(SkeletonState st : startStates) {			
			int minX = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int[] peaks = new int[] {0, Integer.MAX_VALUE}; //0 max, 1 min.
			
			LinkedList<SkeletonState> queue = new LinkedList<SkeletonState>();
			queue.push(st);
			
			HashSet<SkeletonState> visited = new HashSet<SkeletonState>();
			visited.add(st);
			while(!queue.isEmpty()) {
				SkeletonState s = queue.poll();
				peaks[0] = Math.max(peaks[0], s.getLocation().y + s.getRadius());
				peaks[1] = Math.min(peaks[1], s.getLocation().y - s.getRadius());
				maxX = Math.max(maxX, s.getLocation().x + s.getRadius());
				minX = Math.min(minX, s.getLocation().x - s.getRadius());
				
				for(SkeletonTransition t : transitions) {
					if(t.getFrom().equals(s) || t.getTo().equals(s)) {
						int[] tmp = t.getPeaks();
						peaks[0] = Math.max(peaks[0], tmp[0]);
						peaks[1] = Math.min(peaks[1], tmp[1]);
						
						SkeletonState next = t.getFrom().equals(s) ? t.getTo() : t.getFrom();
						if(!visited.contains(next)) {
							queue.push(next);
							visited.add(next);
						}
					}
				}
			}
			
			g.setColor(Color.BLACK);
			RoundRectangle2D contour = new RoundRectangle2D.Double(minX - 40, peaks[1] - 40, maxX - minX + 80, peaks[0] - peaks[1] + 80, 10, 10);
			((Graphics2D) g).draw(contour);
			
			String p = st.getProcess();
			if(p == null) p = "Not Set";
			
			g.setFont(new Font("Times-Roman", Font.BOLD, FONT_SIZE + 3));
			int strWidth = g.getFontMetrics().stringWidth(p);
			int labelX = minX + 20;
			int labelY = peaks[1] -40;
			
			g.setColor(Color.WHITE);
			Rectangle2D whiteRect = new Rectangle2D.Double(labelX, labelY - 4, strWidth + 10, 8);
			((Graphics2D) g).fill(whiteRect);
			
			g.setColor(Color.BLACK);
			g.drawString(p, labelX + 5, labelY + 7);
		}
	}
	
	/**
	 * Auto formats the skeleton.
	 */
	public void autoFormat() {
		toBeFormatted = true;
		repaint();
		
		undoManager.register();
	}
	
	/**
	 * Auto formats the synchronization Skeleton.
	 * @param g the graphics used to draw the skeleton.
	 */
	private void autoFormat(Graphics g) {
		toBeFormatted = false;
		
		for(SkeletonState s : states) {
			s.setLocation(null);
		}
		
		int[] lastPeaks = new int[] {0, Integer.MAX_VALUE};
		
		for(SkeletonState st : startStates) {
			if(st.getLocation() != null) continue;
			
			int startY = lastPeaks[0] + SPACING;
			int diff = 0;
			
			int maxX;
			do {
				maxX = Integer.MIN_VALUE;
				
				int[] peaks = new int[] {0, Integer.MAX_VALUE}; //0 max, 1 min.
				st.setLocation(new Point(SPACING, startY));
				
				LinkedList<SkeletonState> queue = new LinkedList<SkeletonState>();
				queue.push(st);
				
				HashSet<SkeletonState> visited = new HashSet<SkeletonState>();
				visited.add(st);
				while(!queue.isEmpty()) {
					SkeletonState s = queue.poll();
					peaks[0] = Math.max(peaks[0], s.getLocation().y + s.getRadius());
					peaks[1] = Math.min(peaks[1], s.getLocation().y - s.getRadius());
					maxX = Math.max(maxX, s.getLocation().x + s.getRadius());
					
					for(SkeletonTransition t : s.getTransitions()) {
						t.update(g);
						
						int[] tmp = t.getPeaks();
						peaks[0] = Math.max(peaks[0], tmp[0]);
						peaks[1] = Math.min(peaks[1], tmp[1]);
						
						SkeletonState next = t.getTo();
						if(!visited.contains(next)) {
							queue.push(next);
							visited.add(next);
						}
					}
				}

				diff = peaks[1] - lastPeaks[0];
				if(diff != SPACING) {
					startY += SPACING - diff;
					
					for(SkeletonState s : visited) {
						s.setLocation(null);
						s.resetInOffset();
						s.resetOutOffset();
					}
				} else {
					lastPeaks = peaks;
				}
			} while(diff != SPACING);
		}
	}
	
	/*
	 * Selected Component Management.
	 */
	
	/**
	 * Gets the frame this panel belongs to.
	 * @return the frame.
	 */
	public SynchronizationSkeletonFrame getFrame() {
		return frame;
	}
	
	/**
	 * Gets the selected state (null if no selection).
	 * @return the selected state.
	 */
	public SkeletonState getSelectedState() {
		return selectedState;
	}
	
	/**
	 * Gets the selected transition (null if no selection).
	 * @return the selected transition.
	 */
	public SkeletonTransition getSelectedTransition() {
		return selectedTransition;
	}
	
	/**
	 * Sets the selected state (set to null to deselect).
	 * @param selectedState the selected state.
	 */
	public void setSelectedState(SkeletonState selectedState) {
		this.selectedState = selectedState;
	}
	
	/**
	 * Sets the selected transition (set to null to deselect).
	 * @param selectedTransition the selected transition.
	 */
	public void setSelectedTransition(SkeletonTransition selectedTransition) {
		this.selectedTransition = selectedTransition;
	}
	
	/**
	 * Sets the zoom toolbar to call back when zooming.
	 * @param zoomToolbar the zoom toolbar.
	 */
	public void setZoomToolbar(SynchronizationZoomToolBar zoomToolbar) {
		this.zoomToolbar = zoomToolbar;
	}
	
	/*
	 * Getters and Setters
	 */
	
	/**
	 * gets the JScrollPane this GraphPanel reside in.
	 * @return the scrollPane this GraphPanel is in.
	 */
	public JScrollPane getScrollPane() {
		return scroll;
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
	
	/**
	 * Resets the undo manager.
	 */
	public void resetUndoManager() {
		undoManager = new UndoManager(this);
	}
	
	/**
	 * set the states inside the GraphPanel.
	 * @param states the new states.
	 */
	public void setStates(ArrayList<SkeletonState> states) {
		this.startStates = new ArrayList<SkeletonState>();
		
		map = new HashMap<String, SkeletonState>();
		this.states = new ArrayList<SkeletonState>(states.size());
		
		for(SkeletonState s : states) {
			SkeletonState x = s.copy();
			this.states.add(x);
			map.put(x.getLabel(), x);
			
			if(x.isStart())
				startStates.add(x);
		}
	}

	/**
	 * set the transitions inside the GraphPanel.
	 * @param transitions the new transitions.
	 */
	public void setTransitions(ArrayList<SkeletonTransition> transitions) {
		this.transitions = transitions;
		this.transitions = copyTransitions();
		
		this.transitions = new ArrayList<SkeletonTransition>();
		for(SkeletonState s : states) {
			this.transitions.addAll(s.getTransitions());
		}
	}
	
	/*
	 * Copying and accessing components
	 */
	
	/**
	 * Checks if the state l has a unique label.
	 * @param l the state to check.
	 * @return true if label is unique, false otherwise.
	 */
	public boolean checkLegalLabel(SkeletonState l) {
		for(SkeletonState s : states) {
			if(s != l) 
				if(l.getLabel().equals(s.getLabel())) {
					JOptionPane.showMessageDialog(null, "State label must be unique", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
		}
		
		return true;
	}
	
	/**
	 * Adds s to the skeleton, if s's label is already existing returns false without adding it.
	 * @param s the state to add.
	 * @return true if label is unique and state is added, false otherwise.
	 */
	public boolean add(SkeletonState s) {
		selectedState = null;
		selectedTransition = null;
		
		if(!checkLegalLabel(s)) 
			return false;
		
		states.add(s);
		return true;
	}
	
	/**
	 * Adds a transition.
	 * @param t the transition to add.
	 */
	public void add(SkeletonTransition t) {
		transitions.add(t);
		
		selectedState = null;
		selectedTransition = null;
		
		propagateProcess(t.getFrom());
		propagateProcess(t.getTo());
	}
	
	/**
	 * Adds s to the start states, Note that s should exist inside the states prior to 
	 * calling this method to guarantee success. 
	 * @param s the state to add to start states.
	 */
	public void addStartState(SkeletonState s) {
		startStates.add(s);
	}
	
	/**
	 * Deletes a state with its transitions.
	 * @param s the state to delete.
	 */
	public void delete(SkeletonState s) {
		states.remove(s);
				
		for(int i = 0; i < transitions.size(); i++) {
			SkeletonTransition e = transitions.get(i);
			if(e.getTo().equals(s) || e.getFrom().equals(s)) {
				transitions.remove(i);
				e.getFrom().deleteTransition(e);
				i--;
			}
		}
		
		selectedState = null;
		selectedTransition = null;
	}
	
	/**
	 * Deletes a transition.
	 * @param t the transition to delete.
	 */
	public void delete(SkeletonTransition t) {
		transitions.remove(t);
		t.getFrom().deleteTransition(t);
		
		selectedState = null;
		selectedTransition = null;
	}
	
	/**
	 * Maps state name to the most recent copy of the state made by copyStates(),
	 * Useful for transition copying (ensure the copied states and copied transitions match).
	 */
	private HashMap<String, SkeletonState> map;
	
	/**
	 * returns a copy of the states (deep copy), saves the new copied stated in this.map .
	 * @return a copy of the states.
	 */
	public ArrayList<SkeletonState> copyStates() {
		map = new HashMap<String, SkeletonState>();
		ArrayList<SkeletonState> result = new ArrayList<SkeletonState>(states.size());
		
		for(SkeletonState s : states) {
			SkeletonState x = s.copy();
			result.add(x);
			map.put(x.getLabel(), x);
		}
				
		return result;
	}

	/**
	 * returns a copy of the transitions (deep copy).
	 * @return a copy of the transitions.
	 */
	public ArrayList<SkeletonTransition> copyTransitions() {
		ArrayList<SkeletonTransition> result = new ArrayList<SkeletonTransition>(transitions.size());
		
		for(SkeletonTransition e : transitions) {
			String textTo = e.getTo().getLabel();
			String textFrom = e.getFrom().getLabel();
			
			SkeletonTransition t = e.copy(map.get(textFrom), map.get(textTo));
			result.add(t);
			map.get(textFrom).addTransition(t);
		}
				
		return result;
	}
	
	/**
	 * Gets the state that occupies the given location, if more than one state occupy 
	 * this point, returns the most recent.
	 * 
	 * @param p the location.
	 * @return the state that occupies p. 
	 */
	public SkeletonState getStateByLocation(Point p) {
		for(int i = states.size(); i > 0; i--) {
			SkeletonState s = states.get(i - 1);
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
	public SkeletonTransition getTransitionByLocation(Point p) {
		for (SkeletonTransition e : transitions) {
			if (e.contains(p))
				return e;
		}

		return null;		
	}
	
	/**
	 * Clears the content of this GraphPanel.
	 * 
	 * <p>Undo manger can undo this action.</p>
	 */
	public void clear() {		
		states = new ArrayList<SkeletonState>();
		transitions = new ArrayList<SkeletonTransition>();
		startStates = new ArrayList<SkeletonState>();
		
		selectedState = null;
		selectedTransition = null;
		
		undoManager.register();
		
		repaint();
	}
	
	
	
	/*
	 * ZOOM
	 */
	
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
		
		if(zoomToolbar != null)
			zoomToolbar.callBack(new Object[] { zoomTransform.getScaleX() });
		
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
		
		if(zoomToolbar != null)
			zoomToolbar.callBack(new Object[] { zoomTransform.getScaleX() });
		
		repaint();
	}
	
	/**
	 * Reset zoom to no zoom (scales of 1).
	 */
	public void resetZoom() {
		double scaleX = zoomTransform.getScaleX();
		double scaleY = zoomTransform.getScaleY();
		
		zoomTransform.scale(1/scaleX, 1/scaleY);
		
		if(zoomToolbar != null)
			zoomToolbar.callBack(new Object[] { 1.0 });
		
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
		
		if(zoomToolbar != null)
			zoomToolbar.callBack(new Object[] { scale });
		
		repaint();
	}
	
	/**
	 * Gets the maximum width so far reached.
	 * @return the maximum width so far reached.
	 */
	public int getDrawableWidth() {
		double maxX = 0;
		double minX = Integer.MAX_VALUE;
		
		for(SkeletonState s : states) {
			if(s.getLocation().getX() + s.getRadius() + 30 > maxX) {
				maxX = s.getLocation().getX() + s.getRadius() + 30;
			}
			
			minX = Math.min(minX, s.getLocation().getX());
		}
		
		return (int) maxX - (int) minX + 30 + 100;
	}
	
	/**
	 * Gets the maximum height so far reached.
	 * @return the maximum height so far reached.
	 */
	public int getDrawableHeight() {
		double maxY = 0;
		double minY = Integer.MAX_VALUE;

		for(SkeletonState s : states) {
			if(s.getLocation().getY() + s.getRadius() + 30 > maxY) {
				maxY = s.getLocation().getY() + s.getRadius() + 30;
			}
			
			minY = Math.min(minY, s.getLocation().getY());
		}
		
		for(SkeletonTransition t : transitions) {
			int[] peaks = t.getPeaks();
			
			maxY = Math.max(peaks[0], maxY);
			minY = Math.min(peaks[1], minY);
		}
		
		return (int) maxY - (int) minY + 30 + 100;
	}
	
	/**
	 * Propagates the given states process to all states connected to it.
	 * @param s the state to propagate.
	 */
	public void propagateProcess(SkeletonState s) {
		String p = s.getProcess();
		if(p == null) return;
		
		LinkedList<SkeletonState> bfs = new LinkedList<SkeletonState>();
		bfs.push(s);
		
		while(!bfs.isEmpty()) {
			SkeletonState current = bfs.poll();
			for(SkeletonTransition t : transitions) {
				SkeletonState next = null;
				if(t.getFrom().equals(current)) {
					next = t.getTo();
				} else if(t.getTo().equals(current)) {
					next = t.getFrom();
				}
			
				if(next == null || (next.getProcess() != null && next.getProcess().equals(p)))
					continue;
				
				next.setProcess(p);
				bfs.add(next);
			}
		}
	}
	
	/**
	 * Constructs a save object containing this skeletons and save it to the path.
	 * @param path the path to save in.
	 * @throws IOException if the path is unaccessible
	 */
	public void save(String path) throws IOException {
		SkeletonSaveObject obj = new SkeletonSaveObject(states, transitions, frame.getBaseName());
		obj.save(path);
	}
	
	/**
	 * Gets the saveable structure.
	 * @return saveable structure.
	 */
	public Saveable getSaveable() {
		return new SkeletonSaveObject(states, transitions, frame.getBaseName());
	}
	
	/**
	 * Loads Synchronization Skeletons from inside the given save object.
	 * @param obj the save object to load skeletons from.
	 */
	public void load(SkeletonSaveObject obj) {
		clear();
		
		setStates(obj.getStates());
		setTransitions(obj.getTransitions());
		
		frame.setBaseName(obj.getBaseName());		
		resetUndoManager();
	}
}
