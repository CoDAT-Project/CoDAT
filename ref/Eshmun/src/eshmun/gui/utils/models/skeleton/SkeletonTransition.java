package eshmun.gui.utils.models.skeleton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import eshmun.expression.guard.AndGuardOperator;
import eshmun.expression.guard.AtomicGuardExpression;
import eshmun.expression.guard.GuardExpression;
import eshmun.expression.guard.OrGuardOperator;

/**
 * Represent a guarded transition between two states in a Synchronization Skeleton.
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SkeletonTransition implements Serializable {
	/**
	 * Default guard/command font.
	 */
	private static final Font FONT = new Font("", Font.BOLD, 17);
	
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = -4100909948434566153L;
		
	/**
	 * Maximum width for a segment.
	 */
	private static final int MAX_WIDTH = 375;
		
	/**
	 * The start state.
	 */
	private SkeletonState from;
	
	/**
	 * The end state.
	 */
	private SkeletonState to;
	
	/**
	 * The guard saved as string (for serialization).
	 */
	private String guard;
	
	/**
	 * The guard expression including the guard boolean formula and the assignments (command).
	 */
	private transient GuardExpression guardExpression;
	
	/**
	 * The main line segment.
	 */
	private Shape mainSegment;
	
	/**
	 * The connecting vertical shapes.
	 */
	private Shape[] shapes;
	
	/**
	 * Self Loop Shapes.
	 */
	private Shape[] selfLoopShapes;
	
	/**
	 * The arrow head.
	 */
	private Shape arrowHead;
	
	/**
	 * Self loop.
	 */
	private boolean selfLoop;
	
	/**
	 * Ensures the transient guardExpression is loaded from the guard String.
	 * @return this with the guardExpression field parsed.
	 */
	private Object readResolve() {
		try {
			this.guardExpression = GuardExpression.parse(this.guard);
		} catch(IllegalArgumentException ex) {
			this.guardExpression = new AtomicGuardExpression();
			System.out.println(this.guard);
			System.out.println(ex.getMessage());
		}
		return this;
	}
			
	/**
	 * Creates a new Transition.
	 * @param guard the guard.
	 * @param from the start state.
	 * @param to the end state.
	 * @throws IllegalArgumentException if the command syntax is wrong.
	 */
	public SkeletonTransition(SkeletonState from, SkeletonState to, GuardExpression guard) {		
		this.from = from;
		this.to = to;
		this.guardExpression = guard.clone();
		this.guard = guardExpression.toString();
		
		update(null);
	}
	
	/**
	 * Gets the guard, with OR and AND written as |, &amp;.
	 * @return the guard.
	 */
	public GuardExpression getGuard() {
		return guardExpression.clone();
	}
	
	/**
	 * Sets the guard.
	 * @param guard the guard with OR and AND written as |, &amp;.
	 */
	public void setGuard(GuardExpression guard) {
		if(guard == null) this.guardExpression = new AtomicGuardExpression();
		else this.guardExpression = guard.clone();
		
		this.guard = this.guardExpression.toString();
	}
	
	/**
	 * Gets the start state.
	 * @return the start state.
	 */
	public SkeletonState getFrom() {
		return from;
	}
	
	/**
	 * Gets the end state.
	 * @return the end state.
	 */
	public SkeletonState getTo() {
		return to;
	}
	
	/**
	 * Updates the radius of the state according to size of the label. 
	 * @param g the graphics used to draw.
	 */
	public void update(Graphics g) {
		selfLoop = (to == from);
		
		this.shapes = new Shape[2];
		String guard = this.guardExpression.toString();
		
		boolean backwards = isBackwards();		
		int offset = (backwards ? to.nextInOffset() : from.nextOutOffset());
		int y = offset;
		if(y > 0) {
			y += from.getRadius();
		} else if(y < 0) {
			y -= to.getRadius();
		}
		
		y += (backwards ? to.getLocation().y : from.getLocation().y);
		
		if(to.getLocation() == null) {
			g.setFont(FONT);
			int strWidth = g.getFontMetrics().stringWidth(guard) + 50;
			strWidth = Math.min(strWidth, MAX_WIDTH);
			to.setLocation(new Point(from.getLocation().x + from.getRadius() + to.getRadius() + strWidth, y));
		}
		
		Point mainSeg1 = new Point(to.getLocation().x, y);
		Point mainSeg2 = new Point(from.getLocation().x, y);
		mainSegment = new Line2D.Double(mainSeg1, mainSeg2);
		
		Point loc = from.getLocation(); //The from segment
		shapes[0] = new Line2D.Double(loc, mainSeg2);
		
		loc = to.getLocation(); //The to segment
		shapes[1] = new Line2D.Double(loc, mainSeg1);

		Point intersection;
		Point p1;
		Point p2;
		
		int diff = mainSeg1.y - to.getLocation().y;
		if(diff > to.getRadius()) { //main segment is bellow to.
			intersection = new Point(to.getLocation().x, to.getLocation().y + to.getRadius());
			p1 = new Point(intersection.x - 5, intersection.y + 7);
			p2 = new Point(intersection.x + 5, intersection.y + 7);
		} else if(diff < -1 * to.getRadius()) { //main segment is above to.
			intersection = new Point(to.getLocation().x, to.getLocation().y - to.getRadius());
			p1 = new Point(intersection.x - 5, intersection.y - 7);
			p2 = new Point(intersection.x + 5, intersection.y - 7);
		} else {
			Line2D l = (Line2D) mainSegment;
			int r = to.getRadius();
			Point center = to.getLocation();
			
			int dy = (int) l.ptLineDist(center);
			int dx = (int) Math.sqrt(r*r - dy*dy);
			
			if(l.getY1() < center.y) {
				dy *= -1;
			}
			
			intersection = new Point(center.x - dx, center.y + dy);
			p1 = new Point(intersection.x - 7, intersection.y - 5);
			p2 = new Point(intersection.x - 7, intersection.y + 5);
		}

		//trim sides of mainSegment if they intersect the state.
		diff = mainSeg1.y - to.getLocation().y;
		if(diff <= 2 && diff >= -2) {
			mainSeg1 = new Point(to.getLocation().x - to.getRadius(), (int) ((Line2D) mainSegment).getY1());
			mainSeg2 = new Point((int) ((Line2D) mainSegment).getX2(), (int) ((Line2D) mainSegment).getY2());
			mainSegment = new Line2D.Double(mainSeg1, mainSeg2);
		}
		
		diff = mainSeg2.y - from.getLocation().y;
		if(diff <= 2 && diff >= -2) {
			mainSeg1 = new Point((int) ((Line2D) mainSegment).getX1(), (int) ((Line2D) mainSegment).getY1());
			mainSeg2 = new Point(from.getLocation().x + from.getRadius(), (int) ((Line2D) mainSegment).getY2());
			mainSegment = new Line2D.Double(mainSeg1, mainSeg2);
		}
		
		arrowHead = new Polygon(new int[] { intersection.x, p1.x, p2.x } , new int[] { intersection.y, p1.y, p2.y }, 3);
	}
	
	/**
	 * Checks if this transition is going backwards or forward.
	 * @return true if it is backwards, false otherwise.
	 */
	public boolean isBackwards() {
		if(to.getLocation() == null) return false;
		return to.getLocation().x - from.getLocation().x < 0;
	}
	
	/**
	 * Draws this transition into the given graphics.
	 * @param g the graphics in which the transition will be drawn.
	 */
	public void draw(Graphics g) {
		String guard = this.guardExpression.toSymbolsString();
		Graphics2D g2d = (Graphics2D) g.create();
		
		if(selfLoop) {
			g2d.setFont(FONT);

			int guardLength = g2d.getFontMetrics().stringWidth(guard);
			guardLength = Math.min(guardLength, 200);
			guardLength = Math.max(guardLength, from.getRadius() + 30);
			
			int minLength = getFrom().getRadius();
			int minHeight = minLength + 20;
			
			int length = Math.max(guardLength, minLength) + 10;
			int height = minHeight;
			
			int leftX = getFrom().getLocation().x - length / 2;
			int rightX = leftX + length;
			int topY = getFrom().getLocation().y - height - getFrom().getRadius() / 2;
			int botY = topY + height;
			
			selfLoopShapes = new Shape[4];			
			selfLoopShapes[0] = new Line2D.Double(leftX, botY, rightX, botY);
			selfLoopShapes[1] = new Line2D.Double(leftX, topY, rightX, topY);
			selfLoopShapes[2] = new Line2D.Double(leftX, botY, leftX, topY);
			selfLoopShapes[3] = new Line2D.Double(rightX, topY, rightX, botY);
			
			for(Shape shape : selfLoopShapes) {
				g2d.draw(shape);
			}
			
			int xArrowHead = getFrom().getLocation().x - getFrom().getRadius() + 3;
			int yArrowHead = botY;
						
			g2d.fillPolygon(
					new int[] { xArrowHead, xArrowHead - 9, xArrowHead - 9}, 
					new int[] {	yArrowHead, yArrowHead - 7, yArrowHead + 7}, 
					3);
			

			g.setColor(Color.BLACK); //return color to default
			
			//Break the guard into different lines if needed
			List<String> guardBreaks = breakGuard(length, guard, g2d);
			
			//draw the guard in the center
			for(int i = 0; i < guardBreaks.size(); i++) {
				int partSize = g2d.getFontMetrics().stringWidth(guardBreaks.get(i));
				int horOffset = (length - partSize) / 2;
				int verOffset = 15 * (guardBreaks.size() - 1 - i) + 5;
				g2d.drawString(guardBreaks.get(i), leftX + horOffset, topY - verOffset);
			}
			return;
		}
				
		//Draw different segments
		g2d.setColor(Color.BLACK);
		g2d.draw(mainSegment);
		g2d.draw(shapes[0]);
		g2d.draw(shapes[1]);
		
		g2d.setFont(FONT);
		
		if(arrowHead != null) { 
			g2d.fill(arrowHead);
			g2d.draw(arrowHead);
		}
		
		//Get the center of the main segment and its width
		Line2D line = (Line2D) mainSegment;
		int midX = (int) ((line.getX1() + line.getX2()) / 2);
		int midY = (int) ((line.getY1() + line.getY2()) / 2);
		int width = (int) Math.abs(line.getX1() - line.getX2());
				
		//Break the guard into different lines if needed
		List<String> guardBreaks = breakGuard(width - 10, guard, g2d);
		int maxSize = g.getFontMetrics().stringWidth(guardBreaks.get(guardBreaks.size() - 1));
		for(int i = 0; i < guardBreaks.size() - 1; i++) {
 			int size = g.getFontMetrics().stringWidth(guardBreaks.get(i));
			if(size > maxSize) {
				maxSize = size;
			}
		}

		//draw the guard in the center
		int horOffset = maxSize / 2;
		int length = guardBreaks.size() - 1;
		for(int i = 0; i < guardBreaks.size(); i++) {
			int verOffset = 15 * (length - i) + 5;
			g2d.drawString(guardBreaks.get(i), midX - horOffset, midY - verOffset);
		}
	}
	
	/**
	 * Breaks the guard into separate lines when needed, the breaking
	 * is done as close as possible to the center of the line.
	 * @param width the width possible to use.
	 * @param guardFragement the piece of the guard to line-break.
	 * @param g the graphics used to draw.
	 * @return the breaks as a list.
	 */
	private List<String> breakGuard(int width, String guardFragement, Graphics g) {
		LinkedList<String> result = new LinkedList<String>();
		
		int strWidth = g.getFontMetrics().stringWidth(guardFragement);
		if(strWidth > width && guardFragement.length() > 5) {
			int mid = guardFragement.length() / 2;
			for(int i = 0; i < mid; i++) {
				char c = guardFragement.charAt(mid - i);
				if(canBreakCharacter(c)) {
					String sub1 = guardFragement.substring(0, mid - i).trim();
					String sub2 = guardFragement.substring(mid - i).trim();
					
					result.addAll(breakGuard(width, sub1, g));
					result.addAll(breakGuard(width, sub2, g));
					return result;
				} 
				
				c = guardFragement.charAt(mid + i);
				if(canBreakCharacter(c)) {
					String sub1 = guardFragement.substring(0, mid + i).trim();
					String sub2 = guardFragement.substring(mid + i).trim();
					
					result.addAll(breakGuard(width, sub1, g));
					result.addAll(breakGuard(width, sub2, g));
					return result;
				}
			}
			
			String sub1 = guardFragement.substring(0, mid);
			String sub2 = "-"+guardFragement.substring(mid);
			
			result.addAll(breakGuard(width, sub1, g));
			result.addAll(breakGuard(width, sub2, g));
			return result;
		}
		
		result.push(guardFragement.trim());
		return result;
	}
	
	/**
	 * Returns two points on the Perpendicular Bisector of the main segment, useful in 
	 * drawing blue circles around the main segment when selected.
	 * 
	 * <p>These two points are equal in distance to the main segment (5 pixels), The 
	 * pixels are ordered from the least y to the biggest y.</p>
	 * 
	 * @return the two points.
	 */
	public Point[] getSelectPoints() {
		if(selfLoop) {
			Line2D line = (Line2D) selfLoopShapes[1];
			int midX = (int) (line.getX1() + line.getX2()) / 2;
			int midY = (int) line.getY1();
			
			Point p1 = new Point(midX, midY - 5);
			Point p2 = new Point(midX, midY + 5);
			
			return new Point[] {p1, p2};
		}
		
		Line2D line = (Line2D) mainSegment;
		int midX = (int) ((line.getX1() + line.getX2()) / 2);
		int midY = (int) ((line.getY1() + line.getY2()) / 2);
		
		Point p1 = new Point(midX, midY - 5);
		Point p2 = new Point(midX, midY + 5);
		
		return new Point[] {p1, p2};
	}
	
	/**
	 * Gets the maximum and the minimum Y on this segment, in order.
	 * @return the maximum and the minimum Y.
	 */
	public int[] getPeaks() {
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		
		Line2D tmp = (Line2D) shapes[0];
		int min = (int) Math.min(tmp.getY1(), tmp.getY2());
		int max = (int) Math.max(tmp.getY1(), tmp.getY2());
		maxY = Math.max(max, maxY);
		minY = Math.min(min, minY);
		
		tmp = (Line2D) shapes[1];
		min = (int) Math.min(tmp.getY1(), tmp.getY2());
		max = (int) Math.max(tmp.getY1(), tmp.getY2());
		maxY = Math.max(max, maxY);
		minY = Math.min(min, minY);
		
		tmp = (Line2D) mainSegment;
		min = (int) Math.min(tmp.getY1(), tmp.getY2());
		max = (int) Math.max(tmp.getY1(), tmp.getY2());
		maxY = Math.max(max, maxY);
		minY = Math.min(min, minY);
		
		return new int[] { maxY, minY };
	}
	
	/**
	 * Shows an edit dialog to edit the content of the transition.
	 * @return true of the transition was changed, false otherwise.
	 */
	public boolean edit() {		
		JPanel panel = new JPanel();
		JTextArea textArea = new JTextArea(7, 50);
		
		panel.add(textArea);
		panel.setBorder(new TitledBorder("Guarded Command"));
		textArea.setText(guardExpression.toString());
		
		while(true) {
			int op = JOptionPane.showConfirmDialog(null, panel, "Edit The Transition's Guarded Command", JOptionPane.OK_CANCEL_OPTION);
			if(op == JOptionPane.CANCEL_OPTION)
				return false;
			
			// attempt to parse the guard.
			String guardToParse = textArea.getText();
			try {
				this.guardExpression = GuardExpression.parse(guardToParse);
				this.guard = this.guardExpression.toString();
				return true;
			} catch(IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, "The provided guard contains syntax errors", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	}
	
	/**
	 * Copies this transition into an identical state.
	 * @param from the from state. 
	 * @param to the to state.
	 * @return a copy of this state.
	 */
	public SkeletonTransition copy(SkeletonState from, SkeletonState to) {
		SkeletonTransition t = new SkeletonTransition(from, to, guardExpression.clone());
		
		t.selfLoop = selfLoop;
		if(!t.selfLoop) {
			t.shapes = Arrays.copyOf(shapes, shapes.length);
			t.mainSegment = mainSegment;
			t.arrowHead = arrowHead;
		}
		
		return t;
	}
	
	/**
	 * Checks if a point is contained on this transition (to some epsilon). 
	 * @param p the point to check.
	 * @return true if the point is on the transition, false otherwise.
	 */
	public boolean contains(Point p) {
		if(selfLoop) {
			for(int i = 0; i < selfLoopShapes.length; i++) {
				Line2D segment = (Line2D) selfLoopShapes[i];
				int minX = (int) Math.min(segment.getX1(), segment.getX2());
				int maxX = (int) Math.max(segment.getX1(), segment.getX2());
				int minY = (int) Math.min(segment.getY1(), segment.getY2());
				int maxY = (int) Math.max(segment.getY1(), segment.getY2());
				
				int diffX = Math.abs(minX - maxX);
				int diffY = Math.abs(minY - maxY);
				
				if(segment.ptLineDist(p) >= 4) continue;
				
				boolean ok = false;
				if(diffX > 2) 
					ok = minX <= p.x && maxX >= p.x;
				
				else if(diffY > 2)
					ok = minY <= p.y && maxY >= p.y;
				
				if(!ok) continue;
				
				if(i > 0) return true;
				int x1 = (int) getFrom().getLocation().getX() - getFrom().getRadius();
				int x2 = x1 + 2 * getFrom().getRadius();
				if(p.x < x1 || p.x > x2) return true;
			}

			return false;
		}
		
		Line2D tmp = (Line2D) mainSegment;		
		int min = (int) Math.min(tmp.getX1(), tmp.getX2());
		int max = (int) Math.max(tmp.getX1(), tmp.getX2());
		if(tmp.ptLineDist(p) < 4 && min <= p.x && max >= p.x) 
			return true;
		
		tmp = (Line2D) shapes[0];
		min = (int) Math.min(tmp.getY1(), tmp.getY2());
		max = (int) Math.max(tmp.getY1(), tmp.getY2());
		if(tmp.ptLineDist(p) < 4 && min <= p.y && max >= p.y)
			return true;
		
		tmp = (Line2D) shapes[1];
		min = (int) Math.min(tmp.getY1(), tmp.getY2());
		max = (int) Math.max(tmp.getY1(), tmp.getY2());
		if(tmp.ptLineDist(p) < 4 && min <= p.y && max >= p.y)
			return true;
		
		return false;
	}
	
	/**
	 * Checks if two transitions are equal (equal from and to).
	 * @param obj the object to check equality with.
	 * @return true if both objects have equal from and to states, command and 
	 * guard are disregarded.
	 */
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof SkeletonTransition))
			return false;
		
		SkeletonTransition t = (SkeletonTransition) obj;
		return t.from.equals(from) && t.to.equals(to);
	}

	/**
	 * @param c the character to check.
	 * @return true if the character is an operator or white space (it is safe to break here)
	 *		   false otherwise.
	 */
	private boolean canBreakCharacter(char c) {
		return c == ' ' || c == '\t' || c == '\n' 
				|| c == AndGuardOperator.AND_GUARD_SYMBOL 
				|| c == OrGuardOperator.OR_GUARD_SYMBOL; 
	}
}
