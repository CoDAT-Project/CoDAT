package eshmun.gui.utils.models.skeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Represents a state in a synchronization skeleton.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SkeletonState implements Serializable {
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = -6267911402611526493L;

	/**
	 * The vertical spacing between transitions.
	 */
	private static final int SPACING = 50;
	
	/**
	 * The label of the state.
	 */
	private String label;
	
	/**
	 * If this is a start state.
	 */
	private boolean start;
	
	/**
	 * The process this state belongs to.
	 */
	private String process;
	
	/**
	 * The location of the center.
	 */
	private Point location;
	
	/**
	 * The radius of the circle.
	 */
	private int radius;
	
	/**
	 * The shape this state has on the panel (circle).
	 */
	private Shape shape;
	
	/**
	 * The transitions coming out of this state.
	 */
	private ArrayList<SkeletonTransition> transitions;
	
	/**
	 * Creates a new State in a Synchronization Skeleton.
	 * @param label the label of the state.
	 */
	public SkeletonState(String label) {
		this(label, false);
	}
	
	/**
	 * Creates a new State in a Synchronization Skeleton.
	 * @param label the label of the state.
	 * @param start if this is a start state.
	 */
	public SkeletonState(String label, boolean start) {
		this(label, start, new Point(15, 15));
	}
	
	/**
	 * Creates a new State in a Synchronization Skeleton.
	 * @param label the label of the state.
	 * @param start if this is a start state.
	 * @param location the location of the center of the state.
	 */
	public SkeletonState(String label, boolean start, Point location) {
		this.label = label;
		this.start = start;
		this.location = location;
		
		this.transitions = new ArrayList<SkeletonTransition>();
		this.process = null;
		
		update();
	}
	
	/**
	 * Updates the radius of the state according to size of the label. 
	 */
	public void update() {
		radius = (int) (12 * Math.pow(label.length(), 0.7));
		
		if(location != null)
			shape = new Ellipse2D.Double(location.x - radius, location.y - radius, 2 * radius, 2 * radius);
	}
	
	/**
	 * Gets the label of the state.
	 * @return the label.
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets this states label.
	 * @param label the new label.
	 */
	public void setLabel(String label) {
		this.label = label;
		
		update();
	}
	
	/**
	 * Gets if this is a start state or not.
	 * @return true if this is a start state, false otherwise.
	 */
	public boolean isStart() {
		return start;
	}
	
	/**
	 * Sets if this state is a start state or not.
	 * @param start the new value.
	 */
	public void setStart(boolean start) {		
		this.start = start;
	}
		
	/**
	 * Gets the radius of this state's circle on the panel.
	 * @return the radius.
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Gets the location of the center of this state's circle.
	 * @return the location of the center.
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Sets the location of the center of the state.
	 * @param location the new location.
	 */
	public void setLocation(Point location) {
		this.location = location;
		
		update();
	}
	
	/**
	 * Gets the process name of this state.
	 * @return the process name.
	 */
	public String getProcess() {
		return process;
	}
	
	/**
	 * Sets the process name of this state.
	 * @param process the process name.
	 */
	public void setProcess(String process) {
		this.process = process;
	}
	
	/**
	 * Returns a copy of the outgoing transitions of this state.
	 * @return a copy of the outgoing transitions.
	 */
	public ArrayList<SkeletonTransition> getTransitions() {
		return new ArrayList<SkeletonTransition>(transitions);
	}
	
	public void deleteTransition(SkeletonTransition t) {
		transitions.remove(t);
	}
	
	/**
	 * Checks if a point is contained inside the circle of this state. 
	 * @param point the point to check.
	 * @return true if the point is in the circle, false otherwise.
	 */
	public boolean contains(Point point) {
		return shape.contains(point.x, point.y);
	}
	
	/**
	 * Draws this state into the given graphics.
	 * @param g the graphics in which the state will be drawn.
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setColor(start ? Color.GREEN : Color.WHITE);
		g2d.fill(shape);
		
		g2d.setColor(Color.BLACK);
		g2d.draw(shape);
		
		int offset = (int) ((10 * Math.pow(label.length(), 0.75)) * Math.pow(label.length(), 0.2) * Math.pow(label.length(), 0.05));
		int x = location.x - offset/2;
		int y = location.y + 5;
		
		g2d.drawString(label, x, y);
	}
	
	/**
	 * Adds an outgoing transition if it is unique, if it already exists does nothing.
	 * @param t the transition to be added.
	 */
	public void addTransition(SkeletonTransition t) {
		for(SkeletonTransition e : transitions) {
			if(e.getTo().equals(t.getTo())) {
				return;
			}
		}
		
		transitions.add(t);
	}
	
	/**
	 * Keeps track of the current vertical offset of outgoing transition.
	 */
	private int outOffset = 0;
	
	/**
	 * Resets the outgoing offset counter, should be called before drawing.
	 */
	public void resetOutOffset() {
		outOffset = 0;
	}
	
	/**
	 * Returns the next outgoing offset, always positive.
	 * @return the next outgoing offset.
	 */
	public int nextOutOffset() {
		int tmp = outOffset;
		outOffset += SPACING;
		return tmp;
		
	}
	
	/**
	 * Keeps track of the current vertical offset of incoming transition.
	 */
	private int inOffset = 0;
	
	/**
	 * Resets the incoming offset counter, should be called before drawing.
	 */
	public void resetInOffset() {
		inOffset = 0;
	}
	
	/**
	 * Returns the next incoming offset, always negative.
	 * @return the next incoming offset.
	 */
	public int nextInOffset() {
		inOffset -= SPACING;
		return inOffset;
	}
	
	/**
	 * Resets the transitions list.
	 */
	public void resetTransitions() {
		transitions = new ArrayList<SkeletonTransition>();
	}
	
	/**
	 * Checks for equality between two states.
	 * @return true if both states have the same label, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SkeletonState) {
			return label.equals(((SkeletonState) obj).label);
		}
		
		return false;
	}
	
	/**
	 * Gets 4 vertices of the smallest square surrounding the state's circle, in
	 * clock wise order, from the top left corner.
	 * @return the vertices of the boundary.
	 */
	public Point[] getBoundries() {
		Point[] vertices = new Point[4];
		
		int x = location.x; int y = location.y;
		vertices[0] = new Point(x - radius, y - radius);
		vertices[1] = new Point(x + radius, y - radius);
		vertices[2] = new Point(x + radius, y + radius);
		vertices[3] = new Point(x - radius, y + radius);
		
		return vertices;
	}
	
	/**
	 * Shows an edit dialog to edit the content of the state.
	 * @return true if the label of the state was changed, false otherwise.
	 */
	public boolean edit() {
		String newLabel = JOptionPane.showInputDialog(null, "Enter the new state label", label);
		if(newLabel != null) {
			this.label = newLabel;
			
			update();
		}
		
		return newLabel != null;
	}

	/**
	 * Shows an edit dialog to edit the process of the state.
	 * @return true if the process of state was changed, false otherwise.
	 */
	public boolean editProcess() {
		String process = JOptionPane.showInputDialog(null, "Please enter the process this skeleton belongs to", this.process);
		if(process == null)
			return false;
		
		this.process = process.trim();
		return true;
	}
	
	/**
	 * Copies this state into an identical state.
	 * @return a copy of this state.
	 */
	public SkeletonState copy() {
		SkeletonState s = new SkeletonState(label, start, location);
		s.update(); 
		
		s.transitions = new ArrayList<SkeletonTransition>();
		s.process = process;
		
		return s;
	}
}
