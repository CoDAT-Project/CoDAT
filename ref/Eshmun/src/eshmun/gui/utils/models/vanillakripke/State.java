package eshmun.gui.utils.models.vanillakripke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.Collection;

import eshmun.Eshmun;
import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.kripke.dialogs.StateDialog;

public class State implements Serializable {
    /**
     * Auto generated Serial UID
     */

    private static final long serialVersionUID = 1228050819931966075L;

    /*
     * Attributes
     */
    private String name; // State Name
    private String labels; // Comma Separated

    private boolean start;
    private boolean retain;

    private Point location;
    private int width;
    private int height;
    private int radius;
    private Shape shape;

    private String preCondition ;

    private boolean dashed;
    private boolean dotted;
    private boolean striped;

    public static final String STATE_PREFIX = "X";

    public State(String name, String labels, Point location) {
        this(name, labels, location, false, false);
    }


    public State(String name, String labels, Point location, boolean start,    boolean retain) {
        this.start = start;
        this.retain = retain;

        this.name = name.trim();
        this.labels = labels.trim();

        this.location = location;
        this.dashed = false;

        this.initDimensions();
    }

    public State(String name, String labels, String preCondition, Point location, boolean start,    boolean retain) {
        this.start = start;
        this.retain = retain;

        this.name = name.trim();
        this.labels = labels.trim();

        this.location = location;
        this.dashed = false;

        this.preCondition = preCondition;

        this.initDimensions();
    }


public State(String name, String labels,String preCondition, Point location) {

        this(name, labels, location);
        this.preCondition = preCondition;



    }

    /*
     * Attribute Exposures
     */

    public String getName() {
        return name;
    }

    public String getLabels() {
        return labels;
    }

    public void setName(String name) {
        this.name = name.trim();

        initDimensions();

        Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
    }

    public void setLabels(String labels) {
        this.labels = labels.trim();

        initDimensions();

        Eshmun.eshmun.getCurrentGraphPanel().updateTransitionProcess(this);

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

    public boolean isStriped() {
        return striped;
    }

    public void setStriped(boolean striped) {
        this.striped = striped;
    }

    public boolean getRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;

        Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
    }

    public boolean getStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;

        Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;

        shape = new RoundRectangle2D.Double(location.x, location.y, width,
                height, radius, radius);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRadius() {
        return radius;
    }


    public String getPreCondition() {
    	
        return  preCondition == null || preCondition.equals(new String()) ? "True" : preCondition;
    }


    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition.trim();
        Eshmun.eshmun.getCurrentGraphPanel().getUndoManager().register();
    }

    /**
     * Displays and handles an edit dialog, also handles repainting if needed.
     */
    public void edit() {
        StateDialog dialog = new StateDialog(Eshmun.eshmun, getName(), getLabels(),getPreCondition(), getStart(), getRetain());
        dialog.setVisible(true);
        if (dialog.isSuccessful()) {
            String oldName = getName();

            setName(dialog.getName());
            setLabels(dialog.getLabels());
            setStart(dialog.isStart());
            setRetain(dialog.isRetain());
            setPreCondition(dialog.getPreCondition());

            Eshmun.eshmun.getCurrentGraphPanel().updateName(oldName, getName());
            Eshmun.eshmun.getCurrentGraphPanel().repaint();
        }

        Eshmun.eshmun.getCurrentGraphPanel().requestFocusInWindow();
    }

    public String constructDefinition() {
        String definition = "";

        definition += name+":";
        definition += labels+":";
        definition += start+":";
        definition += retain+";";

        return definition;
    }

    /*
     * Drawing and Geometry
     */

    public void initDimensions() {
        int max = Math.max(name.length(), labels.length());
        if(max > 15)
            max = 11;

        initDimensions(max);
    }

    public void initDimensions(int max) {


        this.width = max * 17;
        this.width = Math.max(this.width, 50);
        this.height = 40;
        this.radius = (int) (Math.max(width, height) / 2.5);

        this.shape = new RoundRectangle2D.Double(location.x, location.y, width,
                height, radius, radius);
    }


    public void draw(Graphics g) {
        if(Eshmun.eshmun.getCurrentGraphPanel().isTableau()) {
            Graphics2D g2d = (Graphics2D) g.create();

            Stroke bold = new BasicStroke(GraphPanel.STATE_BORDER_THICKNESS);
            g2d.setStroke(bold);

            if(getName().startsWith("or")) {
                int[] xs = new int[]{location.x, location.x + 8, location.x + width - 8, location.x + width, location.x + width - 8,  location.x + 8};
                int[] ys = new int[]{location.y + (height/2), location.y + height, location.y + height, location.y + (height/2), location.y, location.y};

                if(start) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillPolygon(xs, ys, 6);
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(xs, ys, 6);
            } else {
                if(start) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRect(location.x, location.y, width, height);
                g2d.setColor(Color.BLACK);


                g2d.drawRect(location.x, location.y, width, height);
            }

            g2d.dispose();

        } else {
            if(start) {
                g.setColor(Color.GREEN);
                g.fillRoundRect(location.x, location.y, width, height, radius,
                        radius);
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
                g.fillRoundRect(location.x, location.y, width, height, radius,
                        radius);
                g.setColor(Color.BLACK);
            }

            if(dotted) {
                Graphics2D g2d = (Graphics2D) g.create();
                Stroke dotted = new BasicStroke(GraphPanel.STATE_BORDER_THICKNESS,
                        BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[]{3.5f}, 0);

                g2d.setStroke(dotted);
                g2d.drawRoundRect(location.x, location.y, width, height, radius, radius);

                g2d.dispose();
            } else if(retain) { //retain is bolder
                Graphics2D g2d = (Graphics2D) g.create();

                Stroke bold = new BasicStroke(GraphPanel.STATE_BORDER_THICKNESS + GraphPanel.RETAIN_EXTRA_THICKNESS);
                g2d.setStroke(bold);
                g2d.drawRoundRect(location.x, location.y, width, height, radius, radius);

                g2d.dispose();
            } else if(dashed) { //dashed
                Graphics2D g2d = (Graphics2D) g.create();

                Stroke dashed = new BasicStroke(GraphPanel.STATE_BORDER_THICKNESS, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g2d.setStroke(dashed);
                g2d.drawRoundRect(location.x, location.y, width, height, radius, radius);

                g2d.dispose();
            } else { //not retain not dashed
                g.drawRoundRect(location.x, location.y, width, height, radius, radius);
            }

            if(striped) {
                final int LINE_DISTANCE = 6; // distance between parallel lines in pixels.

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.GRAY);

                // Clips (restricts) the canvas of drawing down to the given shape (which is the shape of the state).
                // This will hide any overflows of stripe lines. Now I dont need to change the length of the lines as
                // As I am moving further into the edges.
                g2d.setClip(new RoundRectangle2D.Double(location.x + 1, location.y + 1, width - 1, height - 1, radius, radius));

                // Compute diagonal line (from bottom left corner to top right).
                int x = location.x;
                int y = location.y;
                int yPrime = y + height;
                int xPrime; //unknown now.

                double m = -1.25;
                double p = yPrime - m * x;
                xPrime = (int) ((y - p) / m);

                // Draw diagonal line, then shift right and left to draw the parallel lines.
                for(int spacing = 0; spacing < width; spacing += LINE_DISTANCE) {
                    g2d.drawLine(x + spacing, yPrime, xPrime + spacing, y);
                    g2d.drawLine(x - spacing, yPrime, xPrime - spacing, y);
                }

                //g2.setPaint(getBackground());
                //g2d.fill(getShape());
                //

                // draw diagonal lines
                //g2.setPaint(getLineColor());



                g2d.dispose();
            }
        }

        String tmpName = name;
        String tmpLabels = labels.replace(":=", "=");


        int offset = (width - ((tmpName.length()) * 10) - 5) / 2;
        if(tmpName.length() > 15) {
            tmpName = tmpName.substring(0, 15)+"...";
            offset = 15;
        }

        g.drawString(tmpName, location.x + 5 + offset, location.y + 17);

        offset = (width - ((tmpLabels.length()) * 10) - 12) / 2;

        if(tmpLabels.length() > 15) {
            tmpLabels = "(" + tmpLabels.substring(0, 15) + "...";
            offset = 15;
        } else {
            tmpLabels = "(" + tmpLabels + ")";
        }

        Collection<String> processNames = Eshmun.eshmun.getColoredProcessNames();
        String[] labelsSplit = tmpLabels.split(",");
        g.drawString("(", location.x + 5 + offset, location.y + 35);

        for(int i = 0; i < labelsSplit.length; i++) {
            Color c = Color.BLACK;

            String label = labelsSplit[i].trim(); //Bug fix: dont consider ")" as part of process suffix.
            if(label.endsWith(")")) label = label.substring(0, label.length()-1).trim();

            for(String process : processNames) {
                if(process.trim().isEmpty()) continue;
                if(label.endsWith(process)) {
                    c = Eshmun.eshmun.getStateLabelColor(process);
                    break;
                }
            }

            g.setColor(c);
            g.drawString(labelsSplit[i].trim(), location.x + 5 + offset, location.y + 35);
            offset += g.getFontMetrics().stringWidth(labelsSplit[i].trim()); //size of shit

            if(i != labelsSplit.length - 1) { //Draw comma
                g.setColor(Color.BLACK);
                g.drawString(",", location.x + 5 + offset, location.y + 35);
                offset += g.getFontMetrics().stringWidth(","); //size of shit
            }
        }


    }

    public Point[] getBoundries() {
        Point[] p = new Point[4];

        p[0] = new Point(location);
        p[1] = new Point(location.x + width, location.y);
        p[2] = new Point(location.x + width, location.y + height);
        p[3] = new Point(location.x, location.y + height);

        return p;
    }

    public boolean contains(Point p) {
        return shape.contains(p);
    }

    public State copy() {
        State s = new State(name, labels, preCondition, new Point(location), start, retain);

        s.width = width;
        s.height = height;
        s.radius = radius;
        s.dashed = dashed;

        return s;
    }

    /*
     * DETAILS EXPORTED AS HTML
     */
    public String toString(String structureName) {
        if(structureName.contains("("))
            structureName = structureName.substring(0, structureName.indexOf("(")).trim();

        String variableName = getVariableName(structureName);

        String result = "<html>";
        result += "<b>Name: </b>" + name + ".&nbsp;&nbsp;&nbsp;&nbsp;";
        result += "<b>Labels: </b>"
                + (labels.trim().equals("") ? "-" : labels + ".") + "<br/>";
        result += "<b>Start State: </b>" + (start ? "Yes" : "No")
                + ".&nbsp;&nbsp;&nbsp;&nbsp;";
        result += "<b>Retain State: </b>" + (retain ? "Yes" : "No") +".<br/>";

        if(variableName != null)
            result += "<b>Variable Name: </b>"+variableName+".";

        result += "</html>";

        return result;
    }

    public String getVariableName(String structureName) {
        if(structureName.contains("("))
            structureName = structureName.substring(0, structureName.indexOf("(")).trim();

        return STATE_PREFIX+"."+structureName+"."+getName();
    }
}