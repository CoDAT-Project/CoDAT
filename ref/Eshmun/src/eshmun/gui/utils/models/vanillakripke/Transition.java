package eshmun.gui.utils.models.vanillakripke;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import eshmun.Eshmun;
import eshmun.gui.kripke.GraphPanel;
import javafx.scene.shape.Line;

public class Transition implements Serializable {

	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -5756542238372741836L;

	/*
	 * Attributes
	 */
	private final State from;
	private final State to;
	private boolean doubled;
	private boolean hasDouble;
	private boolean dashed;
	private boolean dotted;

	private Boolean transitionValid;
	private boolean transitionTripletSyntaxError;
	private boolean transitionBeginningWarning;
	private boolean transitionEndError;

	private boolean retain;
	private String actionName;
	private String preCondition;
	private String postCondition;
	private String statementBlock;

	private Shape shape;
	private String processName;
	private String structureName;
	private int actionBubbleRadius;
	private Point actionBubbleCenter;

	public static final String TRANSITION_PREFIX = "E";

	public Transition(State from, State to, boolean doubled, String structureName) {
		this(from, to, doubled, structureName, "", false, false);
	}

	public Transition(State from, State to, boolean doubled, String structureName, String actionName) {
		this(from, to, doubled, structureName, actionName, false, false);
	}

	public Transition(State from, State to, boolean doubled, String structureName, String actionName, boolean retain,
			boolean hasDouble) {
		this.from = from;
		this.to = to;

		this.retain = retain;
		this.doubled = doubled;
		this.hasDouble = hasDouble;

		if (actionName == null)
			actionName = "";
		actionName = actionName.replace(" ", "");
		this.actionName = actionName;

		updateProcess(structureName);
	}

	public Transition(State from, State to, boolean doubled, String structureName, String actionName, boolean retain,
			boolean hasDouble, String statementBlock) {
		this.from = from;
		this.to = to;

		this.retain = retain;
		this.doubled = doubled;
		this.hasDouble = hasDouble;

		this.statementBlock = statementBlock.replace("-SEMI-", ";");
		;

		if (actionName == null)
			actionName = "";
		actionName = actionName.replace(" ", "");
		this.actionName = actionName;

		updateProcess(structureName);
	}

	/*
	 * Attributes Exposure
	 */

	public void setTransitionValidity(boolean valid) {

		transitionValid = new Boolean(valid);
	}

	public Boolean getTransitionValidity() {

		return transitionValid;
	}

	public void setTransitionBeginningWarning(boolean warning) {

		transitionBeginningWarning = warning;
	}

	public boolean getTransitionBeginningWarning() {

		return transitionBeginningWarning;
	}

	public void setTransitionEndError(boolean error) {

		transitionEndError = error;
	}

	public boolean getTransitionEndError() {

		return transitionEndError;
	}

	public void setTransitionTripletSyntaxError(boolean error) {

		transitionTripletSyntaxError = error;
	}

	public boolean getTransitionTripletSyntaxError() {

		return transitionTripletSyntaxError;
	}

	public void setHasDouble(boolean hasDouble) {
		this.hasDouble = hasDouble;
	}

	public boolean hasDouble() {
		return hasDouble;
	}

	public void setDoubled(boolean doubled) {
		this.doubled = doubled;
	}

	public boolean isDoubled() {
		return doubled;
	}

	public State getFrom() {
		return from;
	}

	public State getTo() {
		return to;
	}

	public boolean getRetain() {
		return retain;
	}

	public String getStatementBlock() {
		return statementBlock;
	}

	public String getPostCondition() {
		return postCondition;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;

		Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
	}

	public boolean isDashed() {
		return dashed;
	}

	public void setDashed(boolean dashed) {
		this.dashed = dashed;
	}

	public boolean isDotted() {
		return dotted;
	}

	public void setDotted(boolean dotted) {
		this.dotted = dotted;
	}

	public void setActionName(String actionName) {
		if (actionName == null)
			actionName = "";
		actionName = actionName.replace(" ", "");
		this.actionName = actionName;

		Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
	}

	public String getActionNames() {
		return actionName == null ? "" : actionName;
	}

	public Collection<String> getActionsAsCollection() {
		String actions = getActionNames().trim();

		HashSet<String> result = new HashSet<String>();
		for (String action : actions.split(",")) {
			action = action.trim();
			if (!action.isEmpty())
				result.add(action);
		}

		return result;
	}

	/**
	 * Displays and handles an edit dialog, also handles redrawing.
	 */
	public void edit() { // USEFUL FOR SUB CLASSES

		// Added by Chukri Soueidi
		JTextField actionName = new JTextField(25);
		actionName.setText(this.getActionNames());

		JTextArea statementBlockArea = new JTextArea(10, 25);
		statementBlockArea.setText(this.statementBlock);

		JTextField postConditionField = new JTextField(25);
		postConditionField.setText(this.postCondition);

		JTextField preConditionField = new JTextField(25);
		preConditionField.setText(this.preCondition);

		JPanel myPanel = new JPanel(new BorderLayout());

		myPanel.add(new JLabel("Action Name (Comma-seperated):"));
		myPanel.add(actionName);

		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		myPanel.add(Box.createVerticalStrut(10));

		myPanel.add(new JLabel("Pre-Condition"));
		myPanel.add(preConditionField);
		myPanel.add(Box.createVerticalStrut(5));
		myPanel.add(new JLabel("Statements Block:"));

		JScrollPane scroll = new JScrollPane(statementBlockArea);
		scroll.setAlignmentX(0.0f);
		myPanel.add(scroll);
		myPanel.add(Box.createVerticalStrut(5));
		myPanel.add(new JLabel("Post-Condition"));
		myPanel.add(postConditionField);

		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

		int option = JOptionPane.showConfirmDialog(null, myPanel, "Edit Transition", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {

			statementBlock = statementBlockArea.getText();
			postCondition = postConditionField.getText();
			preCondition = preConditionField.getText();

			if (actionName.getText() == null)
				return; // User clicked cancel.

			setActionName(actionName.getText());

			Eshmun.eshmun.getCurrentGraphPanel().repaint();
			Eshmun.eshmun.getCurrentGraphPanel().requestFocusInWindow();

		}

		// Old Code commented By Chukri Soueidi
		// Object actionName = JOptionPane.showInputDialog(null, "Edit Action Name
		// (Comma-seperated)", "Edit",
		// JOptionPane.PLAIN_MESSAGE, null, null, getActionNames());
		// if(actionName == null) return; // User clicked cancel.
		//
		// setActionName(actionName.toString());
		//
		// Eshmun.eshmun.getCurrentGraphPanel().repaint();
		// Eshmun.eshmun.getCurrentGraphPanel().requestFocusInWindow();
	}

	public String constructDefinition() {
		String definition = "";

		definition += from.getName() + ":";
		definition += to.getName() + ":";
		definition += retain + ":";
		definition += getActionNames() + ";";

		return definition;
	}

	/*
	 * Draw and Geometry
	 */
	int i = 0;

	public void draw(Graphics g) {
		if (processName == null)
			updateProcess();

		g.setColor(Eshmun.eshmun.getTransitionColor(processName));

		if (getFrom().getName().equals(getTo().getName())) { // SELF LOOP
			int x = getFrom().getLocation().x + getFrom().getWidth();
			int y = getFrom().getLocation().y;

			int width = getFrom().getWidth();
			int height = getFrom().getHeight();

			int radius = Math.min(width, height);

			if (dotted) {
				Graphics2D g2d = (Graphics2D) g.create();
				Stroke dotted = new BasicStroke(GraphPanel.TRANSITION_THICKNESS, BasicStroke.CAP_SQUARE,
						BasicStroke.JOIN_MITER, 1, new float[] { 3.5f }, 0);

				g2d.setStroke(dotted);
				g2d.drawOval(x - radius / 2, y - radius / 2, radius, radius);

				drawActionBubbleLoop(g2d, x - radius / 2, y - radius / 2, radius);
				g2d.dispose();
			} else if (retain) { // retain is bolder.
				Graphics2D g2d = (Graphics2D) g.create();

				Stroke bold = new BasicStroke(GraphPanel.TRANSITION_THICKNESS + GraphPanel.RETAIN_EXTRA_THICKNESS);
				g2d.setStroke(bold);
				g2d.drawOval(x - radius / 2, y - radius / 2, radius, radius);

				drawActionBubbleLoop(g2d, x - radius / 2, y - radius / 2, radius);
				g2d.dispose();
			} else if (dashed) { // dashed
				Graphics2D g2d = (Graphics2D) g.create();

				Stroke dashed = new BasicStroke(GraphPanel.TRANSITION_THICKNESS, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
				g2d.setStroke(dashed);
				g2d.drawOval(x - radius / 2, y - radius / 2, radius, radius);

				drawActionBubbleLoop(g2d, x - radius / 2, y - radius / 2, radius);
				g2d.dispose();
			} else { // not retain, not dashed
				g.drawOval(x - radius / 2, y - radius / 2, radius, radius);
				drawActionBubbleLoop((Graphics2D) g.create(), x, y, radius);
			}

			Point p = new Point(x, y + radius / 2);

			Graphics2D g2d = (Graphics2D) g.create();
			g2d.fillPolygon(new int[] { p.x, p.x + 6, p.x + 13 }, new int[] { p.y + 2, p.y - 11, p.y + 7 }, 3);

			g.setColor(Color.BLACK); // return color to default

			return;
		}

		int x1 = getFrom().getLocation().x;
		int y1 = getFrom().getLocation().y;

		x1 += getFrom().getWidth() / 2;
		y1 += getFrom().getHeight() / 2;

		int x2 = getTo().getLocation().x;
		int y2 = getTo().getLocation().y;

		x2 += getTo().getWidth() / 2;
		y2 += getTo().getHeight() / 2;

		if (doubled) {
			int ys = Math.abs(y2 - y1);
			int xs = Math.abs(x2 - x1);

			int min = Math.min(ys, xs);
			if (min == ys) {
				y1 += 10;
				y2 += 10;

			} else {
				x1 += 10;
				x2 += 10;
			}
		}

		// The point where the line intersects the end state bubble
		Point endIntersect = getArrowHeadPoint(getTo(), x1, y1, x2, y2);

		if (dotted) {
			Graphics2D g2d = (Graphics2D) g.create();
			Stroke dotted = new BasicStroke(GraphPanel.TRANSITION_THICKNESS, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_MITER, 1, new float[] { 3.5f }, 0);

			g2d.setStroke(dotted);
			g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);

			drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
			g2d.dispose();
		} else if (retain) {
			Graphics2D g2d = (Graphics2D) g.create();

			Stroke bold = new BasicStroke(GraphPanel.TRANSITION_THICKNESS + GraphPanel.RETAIN_EXTRA_THICKNESS);
			g2d.setStroke(bold);
			g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);

			drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
			g2d.dispose();
		} else if (dashed) {
			Graphics2D g2d = (Graphics2D) g.create();

			Stroke dashed = new BasicStroke(GraphPanel.TRANSITION_THICKNESS, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
			g2d.setStroke(dashed);
			g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);
			drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
			g2d.dispose();
		} else if (transitionValid != null) {
			if (!transitionValid.booleanValue()) {
				Graphics2D g2d = (Graphics2D) g.create();
				float[] dashingPattern1 = { 2f, 2f };

				// creates a dotted effect
				Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
						dashingPattern1, 2.0f);
				g2d.setStroke(stroke1);
				if (transitionTripletSyntaxError)
					g2d.setColor(Color.RED);
				g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);
				drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
				g2d.dispose();
			} else {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);
				drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
				g2d.dispose();
			}

			if (transitionBeginningWarning) {
				showTransitionBeginningWarningError(g, x1, y1, endIntersect);
			}

			if (transitionEndError) {
				showTransitionEndError(g, x1, y1, endIntersect);
			}

		} else {
			 

				Graphics2D g2d = (Graphics2D) g.create();

				g2d.drawLine(x1, y1, endIntersect.x, endIntersect.y);
				drawActionBubble(g2d, x1, y1, endIntersect.x, endIntersect.y);
				g2d.dispose();
			 
		}

		this.shape = getShape(x1, y1, endIntersect.x, endIntersect.y);

		Graphics2D g2d = (Graphics2D) g.create();
		double dx = endIntersect.x - x1, dy = endIntersect.y - y1;
		double angle = Math.atan2(dy, dx);

		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));

		g2d.transform(at);
		g2d.fillPolygon(new int[] { len, len - 9, len - 9, len }, new int[] { -1, -9, 9, -1 }, 4);

		g.setColor(Color.BLACK); // return color to default

	}

	private void showTransitionEndError(Graphics g, int x1, int y1, Point endIntersect) {

		ImageIcon icon = new ImageIcon(getClass().getResource("/error.png"));
		int xImage, yImage;

		int dx = endIntersect.x - x1;
		int dy = endIntersect.y - y1;

		double slope = dy / (dx == 0 ? dx + 0.01 : dx);
		double intercept = y1 - slope * x1;

		if (java.lang.Math.abs(slope) > 10) {

			if (y1 <= endIntersect.y) {
				yImage = endIntersect.y - ((endIntersect.y - y1) / 8);
			} else {

				yImage = endIntersect.y + ((y1 - endIntersect.y) / 8);
			}
			xImage = (int) ((yImage - intercept) / slope);

		} else

		if (x1 <= endIntersect.x) {
			xImage = endIntersect.x - ((endIntersect.x - x1) / 8);
			yImage = (int) (slope * xImage + intercept);

		} else {
			xImage = endIntersect.x + ((x1 - endIntersect.x) / 8);
			yImage = (int) (slope * xImage + intercept);
		}

		icon.paintIcon(Eshmun.eshmun.getCurrentGraphPanel(), g, xImage - 12, yImage - 12);

	}

	private void showTransitionBeginningWarningError(Graphics g, int x1, int y1, Point endIntersect) {

		ImageIcon icon = new ImageIcon(getClass().getResource("/warning.png"));
		int xImage, yImage;

		int dx = endIntersect.x - x1;
		int dy = endIntersect.y - y1;

		double slope = dy / (dx == 0 ? dx + 0.01 : dx);
		double intercept = y1 - slope * x1;

		if (java.lang.Math.abs(slope) > 10) {

			if (y1 <= endIntersect.y) {
				yImage = y1 + ((endIntersect.y - y1) / 4);
			} else {

				yImage = y1 - ((y1 - endIntersect.y) / 4);
			}
			xImage = (int) ((yImage - intercept) / slope);

		} else

		if (x1 <= endIntersect.x) {
			xImage = x1 + ((endIntersect.x - x1) / 4);
			yImage = (int) (slope * xImage + intercept);

		} else {
			xImage = x1 - ((x1 - endIntersect.x) / 4);
			yImage = (int) (slope * xImage + intercept);
		}

		icon.paintIcon(Eshmun.eshmun.getCurrentGraphPanel(), g, xImage - 12, yImage - 12);

	}

	public Point getArrowHeadPoint(State to, int x1, int y1, int x2, int y2) {
		Point[] points = to.getBoundries();

		Point p = null;
		double min = Double.MAX_VALUE;

		Point p2 = null;
		double secondMin = Double.MAX_VALUE;

		for (int i = 0; i < 4; i++) {
			int x3 = points[i].x;
			int y3 = points[i].y;

			int x4 = points[(i + 1) % 4].x;
			int y4 = points[(i + 1) % 4].y;

			Point tmp = getIntersectionPoint(x1, y1, x2, y2, x3, y3, x4, y4);
			if (tmp != null) {
				double distance = Math.sqrt((x2 - tmp.x) * (x2 - tmp.x) + (y2 - tmp.y) * (y2 - tmp.y));

				if (distance <= min) {
					p2 = p;
					secondMin = min;

					min = distance;
					p = tmp;
				}

				else if (distance <= secondMin) {
					secondMin = distance;
					p2 = tmp;
				}

			}
		}
		try {
			double distance1 = Math.sqrt((x1 - p.x) * (x1 - p.x) + (y1 - p.y) * (y1 - p.y));
			double distance2 = Math.sqrt((x1 - p2.x) * (x1 - p2.x) + (y1 - p2.y) * (y1 - p2.y));
			if (distance1 <= distance2)
				return p;
			else
				return p2;
		} catch (Exception e) {
			return p2;
		}

	}

	public Point getIntersectionPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
			double y4) {
		Point p = null;

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d != 0) {
			double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
			double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

			p = new Point((int) xi, (int) yi);
		} else {

		}
		return p;
	}

	private Shape getShape(int x1, int y1, int x2, int y2) {
		int vx = y1 - y2;
		int vy = x2 - x1;

		double d = Math.sqrt(vx * vx + vy * vy);
		vx = (int) ((vx / d) * 5);
		vy = (int) ((vy / d) * 5);

		return new Polygon(new int[] { x1 + vx, x1 - vx, x2 - vx, x2 + vx },
				new int[] { y1 + vy, y1 - vy, y2 - vy, y2 + vy }, 4);
	}

	public Point[] getSelectPoints() {
		if (getFrom().getName().equals(getTo().getName())) { // SELF LOOP
			int x = getFrom().getLocation().x + getFrom().getWidth();
			int y = getFrom().getLocation().y;

			int width = getFrom().getWidth();
			int height = getFrom().getHeight();

			int radius = Math.min(width, height);

			x += radius / 2;
			y -= radius / 2;

			x -= 6;
			y += 6;

			int offset = 4 + (actionBubbleRadius > 0 ? actionBubbleRadius - 3 : 0);

			Point p1 = new Point(x - offset, y + offset);
			Point p2 = new Point(x + offset, y - offset);
			return new Point[] { p1, p2 };
		}

		int x1 = getFrom().getLocation().x;
		int y1 = getFrom().getLocation().y;

		x1 += getFrom().getWidth() / 2;
		y1 += getFrom().getHeight() / 2;

		int x2 = getTo().getLocation().x;
		int y2 = getTo().getLocation().y;

		x2 += getTo().getWidth() / 2;
		y2 += getTo().getHeight() / 2;

		if (doubled) {
			int ys = Math.abs(y2 - y1);
			int xs = Math.abs(x2 - x1);

			int min = Math.min(ys, xs);
			if (min == ys) {
				y1 += 10;
				y2 += 10;

			} else {
				x1 += 10;
				x2 += 10;
			}
		}

		int vx = y1 - y2;
		int vy = x2 - x1;

		double d = Math.sqrt(vx * vx + vy * vy);
		vx = (int) ((vx / d) * (7 + actionBubbleRadius));
		vy = (int) ((vy / d) * (7 + actionBubbleRadius));

		int middleX = (x1 + x2) / 2;
		int middleY = (y1 + y2) / 2;

		return new Point[] { new Point(middleX + vx, middleY + vy), new Point(middleX - vx, middleY - vy), };
	}

	public boolean contains(Point p) {
		// Action Bubble if exists.
		if (actionBubbleCenter != null && actionBubbleRadius != 0)
			if (p.distance(actionBubbleCenter) <= actionBubbleRadius)
				return true;

		if (getTo().getName().equals(getFrom().getName())) { // Self Loop
			int x = getFrom().getLocation().x + getFrom().getWidth();
			int y = getFrom().getLocation().y;

			int width = getFrom().getWidth();
			int height = getFrom().getHeight();

			if (Math.abs(x - p.x) > width * 2 || Math.abs(y - p.y) > height * 2) {
				return false;
			}

			int radius = Math.min(width, height);

			int distance = (int) Math.pow(p.x - x, 2) + (int) Math.pow(p.y - y, 2);
			if (Math.abs(Math.sqrt(distance) - radius / 2) <= 10) {
				return true;
			}

			return false;
		}

		if (shape == null)
			return false;

		return shape.contains(p);
	}

	/**
	 * Draws and fills the action bubble on the center of the transition (not for
	 * self loops).
	 * 
	 * @param g
	 *            the graphics to draw with.
	 * @param x
	 *            x-coordinate for the center of self-loop.
	 * @param y
	 *            y-coordinate for the center of self-loop.
	 * @param r
	 *            the radius of the self-loop.
	 */
	public void drawActionBubbleLoop(Graphics2D g, int x, int y, int r) {
		g = (Graphics2D) g.create();
		g.setColor(Color.BLACK);

		if (actionName == null || actionName.trim().isEmpty()) {
			actionBubbleRadius = 0;
			actionBubbleCenter = null;
			return;
		}

		// Determine location
		int actionsX = x + r / 3;
		int actionsY = y - r / 3;

		// Determine action name length
		int lineLength = r;
		String displayAction = actionName;
		if (displayAction.length() > 4)
			displayAction = displayAction.substring(0, 2) + "...";

		// Shrink action name if too big for transition.
		int actionLength = g.getFontMetrics().stringWidth(displayAction) + 4;
		int actionHeight = g.getFontMetrics().getHeight();
		int baselinePadding = g.getFontMetrics().getDescent();
		while (displayAction.length() > 3 && actionLength > 2 * lineLength / 3.0) {
			displayAction = displayAction.substring(0, displayAction.length() - 1);
			actionLength = g.getFontMetrics().stringWidth(displayAction) + 4;
		}

		// Determine thickness
		int borderThickness = GraphPanel.TRANSITION_THICKNESS;

		// Determine Action Coloring
		ArrayList<Color> colors = new ArrayList<Color>();
		if (!Eshmun.eshmun.isActionColored())
			colors.add(Color.BLACK);
		else
			for (String action : getActionsAsCollection())
				colors.add(Eshmun.eshmun.getActionColor(action));

		Graphics2D g2d = null;
		int colorSize = colors.size();
		while (!colors.isEmpty()) {
			g2d = (Graphics2D) g.create();

			int d = (int) (actionLength + 2 * colors.size() * (1.5 * borderThickness));
			g2d.setColor(colors.get(0));
			g2d.fillOval(actionsX - d / 2, actionsY - d / 2, d, d);
			g2d.dispose();

			colors.remove(0);
		}

		this.actionBubbleRadius = (int) ((actionLength / 2) + (1.5 * borderThickness) * colorSize);
		this.actionBubbleCenter = new Point(actionsX, actionsY);

		// Draw internal spacing
		g2d = (Graphics2D) g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillOval(actionsX - actionLength / 2, actionsY - actionLength / 2, actionLength, actionLength);
		g2d.dispose();

		// Draw string
		AffineTransform transform = AffineTransform.getTranslateInstance(actionsX, actionsY);

		g2d = (Graphics2D) g.create();
		g2d.transform(transform);
		g2d.drawString(displayAction, -actionLength / 2 + 2, actionHeight / 2 - baselinePadding);
		g2d.dispose();

		g.dispose();
	}

	/**
	 * Draws and fills the action bubble on the center of the transition (not for
	 * self loops).
	 * 
	 * @param g
	 *            the graphics to draw with.
	 * @param x1
	 *            x-coordinate for the center of the start state bubble.
	 * @param y1
	 *            y-coordinate for the center of the start state bubble.
	 * @param x2
	 *            x-coordinate for the intersection of the transition with the end
	 *            state bubble.
	 * @param y2
	 *            y-coordinate for the intersection of the transition with the end
	 *            state bubble.
	 */
	public void drawActionBubble(Graphics2D g, int x1, int y1, int x2, int y2) {
		g = (Graphics2D) g.create();
		g.setColor(Color.BLACK);

		if (actionName == null || actionName.trim().isEmpty()) {
			actionBubbleRadius = 0;
			actionBubbleCenter = null;
			return;
		}

		// The point where the transition intersects the start state bubble.
		Point startIntersect = getArrowHeadPoint(getFrom(), x2, y2, x1, y1);
		x1 = startIntersect.x;
		y1 = startIntersect.y;

		// Determine center point
		int midX = (x1 + x2) / 2;
		int midY = (y1 + y2) / 2;

		// Determine action name length
		int lineLength = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		String displayAction = actionName;
		if (displayAction.length() > 7)
			displayAction = displayAction.substring(0, 5) + "...";

		// Shrink action name if too big for transition.
		int actionLength = g.getFontMetrics().stringWidth(displayAction) + 4;
		int actionHeight = g.getFontMetrics().getHeight();
		int baselinePadding = g.getFontMetrics().getDescent();
		while (displayAction.length() > 3 && actionLength > 2 * lineLength / 3.0) {
			displayAction = displayAction.substring(0, displayAction.length() - 1);
			actionLength = g.getFontMetrics().stringWidth(displayAction) + 4;
		}

		// Determine thickness
		int borderThickness = GraphPanel.TRANSITION_THICKNESS;

		// Determine Action Coloring
		ArrayList<Color> colors = new ArrayList<Color>();
		if (!Eshmun.eshmun.isActionColored())
			colors.add(Color.BLACK);
		else
			for (String action : getActionsAsCollection())
				colors.add(Eshmun.eshmun.getActionColor(action));

		Graphics2D g2d = null;
		int colorSize = colors.size();
		while (!colors.isEmpty()) {
			g2d = (Graphics2D) g.create();

			int d = (int) (actionLength + 2 * colors.size() * (1.5 * borderThickness));
			g2d.setColor(colors.get(0));
			g2d.fillOval(midX - d / 2, midY - d / 2, d, d);
			g2d.dispose();

			colors.remove(0);
		}

		this.actionBubbleRadius = (int) ((actionLength / 2) + (1.5 * borderThickness) * colorSize);
		this.actionBubbleCenter = new Point(midX, midY);

		// Draw internal spacing
		g2d = (Graphics2D) g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillOval(midX - actionLength / 2, midY - actionLength / 2, actionLength, actionLength);
		g2d.dispose();

		// Draw string rotated with transition direction
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);

		if (angle > Math.PI / 2 || angle < -Math.PI / 2)
			angle = angle + Math.PI;

		AffineTransform transform = AffineTransform.getTranslateInstance(midX, midY);
		transform.concatenate(AffineTransform.getRotateInstance(angle));

		g2d = (Graphics2D) g.create();
		g2d.transform(transform);
		g2d.drawString(displayAction, -actionLength / 2 + 2, actionHeight / 2 - baselinePadding);
		g2d.dispose();

		g.dispose();
	}

	/*
	 * Copy
	 */

	public Transition copy(State from, State to) {
		Transition t = new Transition(from, to, this.doubled, this.structureName, this.actionName);

		t.retain = retain;
		t.dashed = dashed;
		t.hasDouble = hasDouble;
		t.updateProcess(this.structureName);
		t.statementBlock = statementBlock;
		t.postCondition = postCondition;
		t.preCondition = preCondition;
		return t;
	}

	/*
	 * DETAILS EXPORT AS HTML
	 */
	public String toString(String structureName) {
		if (structureName.contains("("))
			structureName = structureName.substring(0, structureName.indexOf("(")).trim();

		String variableName = "E." + structureName + "." + getFrom().getName() + "_" + getTo().getName();

		String result = "<html>";
		result += "<b>From: </b>" + from.getName() + ".&nbsp;&nbsp;&nbsp;&nbsp;";
		result += "<b>To: </b>" + to.getName() + ".<br/>";
		result += "<b>Retain State: </b>" + (retain ? "Yes" : "No") + ".<br/>";
		if (variableName != null)
			result += "<b>Variable Name: </b>" + variableName + ".&nbsp;&nbsp;&nbsp;&nbsp;";
		result += "<b>Process: </b>" + (processName == null || processName.isEmpty() ? "-" : processName)
				+ ".&nbsp;&nbsp;&nbsp;&nbsp;";
		if (actionName != null && !actionName.isEmpty())
			result += "<b>Action: </b>" + (actionName == null ? "" : actionName);
		result += "</html>";

		return result;
	}

	public String getVariableName(String structureName) {
		if (structureName.contains("("))
			structureName = structureName.substring(0, structureName.indexOf("(")).trim();

		return TRANSITION_PREFIX + "." + structureName + "." + getFrom().getName() + "_" + getTo().getName();
	}

	/**
	 * Detects and updates the process of this transition, changes the structure
	 * name.
	 * 
	 * @param structureName
	 *            the name of the structure containing this transition.
	 */
	public void updateProcess(String structureName) {
		this.structureName = structureName;
		updateProcess();
	}

	/**
	 * Detects and updates the process of this transition.
	 */
	public void updateProcess() {
		if (structureName == null) {
			this.processName = "";
			return;
		}

		if (!structureName.contains("(")) {
			this.processName = "";
			return;
		}

		// Get processes inside this structure
		String name = structureName;
		name = name.substring(name.indexOf("(") + 1, name.indexOf(")"));

		String[] processes = name.split(",");
		for (int i = 0; i < processes.length; i++) {
			processes[i] = processes[i].trim();
		}

		// Get labels of the start and end state of transition
		String fromLabel = from.getLabels();
		String toLabel = to.getLabels();

		List<String> fromLs = new ArrayList<String>();
		List<String> toLs = new ArrayList<String>();

		// Split labels into lists and trim
		// From labels splitting
		String currentLabel = "";
		boolean insideBrac = false;
		for (int i = 0; i < fromLabel.length(); i++) {
			char c = fromLabel.charAt(i);

			if (c == '{')
				insideBrac = true;
			if (c == '}')
				insideBrac = false;

			if (c == ',' && !insideBrac) {
				fromLs.add(currentLabel);
				currentLabel = "";
			} else {
				currentLabel += c;
			}
		}

		if (!currentLabel.isEmpty())
			fromLs.add(currentLabel);

		// To labels splitting
		currentLabel = "";
		insideBrac = false;
		for (int i = 0; i < toLabel.length(); i++) {
			char c = toLabel.charAt(i);

			if (c == '{')
				insideBrac = true;
			if (c == '}')
				insideBrac = false;

			if (c == ',' && !insideBrac) {
				toLs.add(currentLabel);
				currentLabel = "";
			} else {
				currentLabel += c;
			}
		}

		if (!currentLabel.isEmpty())
			toLs.add(currentLabel);

		// Triming labels
		for (int i = 0; i < fromLs.size(); i++) {
			fromLs.set(i, fromLs.get(i).trim());
		}

		for (int i = 0; i < toLs.size(); i++) {
			toLs.set(i, toLs.get(i).trim());
		}

		// Disregard unchanged labels
		List<String> tmp = new ArrayList<String>(fromLs);
		fromLs.removeAll(toLs); // Keep difference
		toLs.removeAll(tmp); // Keep difference

		// Remove Shared Variables
		tmp = new ArrayList<String>();
		for (String l : fromLs) {
			String cmp = l;
			if (l.contains(":=")) {
				cmp = l.substring(0, l.indexOf(":="));
				continue;
			}
			

			for (String p : processes) {
				if (cmp.endsWith(p)) { // Not shared
					tmp.add(l);
					break;
				}
			}
		}
		fromLs = tmp;
		
		
		

		tmp = new ArrayList<String>();
		for (String l : toLs) {
			String cmp = l;
			if (l.contains(":=")) {
				cmp = l.substring(0, l.indexOf(":="));
			continue;
		}
			for (String p : processes) {
				if (cmp.endsWith(p)) { // Not shared
					tmp.add(l);
					break;
				}
			}
		}
		toLs = tmp;

		// Locate the process making the transition
		processName = "";
		for (String p : processes) {
			p = p.trim();
			boolean changed = !(fromLs.isEmpty() && toLs.isEmpty());

			for (String l : fromLs) {
				if (!l.endsWith(p)) {
					changed = false;
					break;
				}
			}

			for (String l : toLs) {
				if (!l.endsWith(p)) {
					changed = false;
					break;
				}
			}

			if (changed) {
				processName = p;
				break;
			}
		}
	}

	/**
	 * Gets the name of the process making this transition.
	 * 
	 * @return the name of the process (or the empty string if it cannot be
	 *         detected).
	 */
	public String getProcessName() { // TODO: Process name must become an array. Distinct transition with no process
										// from unknown (maybe?)
		if (processName == null)
			updateProcess();

		return processName;
	}
}