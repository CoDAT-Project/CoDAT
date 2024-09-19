package eshmun.gui.kripke.utils;

import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class DragAndDropHandler extends MouseMotionAdapter {
	State selected = null;
    Point delta = new Point();
    boolean selecting = false;
    boolean moveScreen = false;
    
    Point mousePopupPoint;
    public boolean addEdge;
    
    Point lastPoint;

    @Override
    public void mouseDragged(MouseEvent e) {
        if(selecting) { //selecting
        	GraphPanel d = (GraphPanel) e.getComponent();
            
        	Point mousePt = d.translatePoint(e.getPoint());
            Point p = new Point(delta.x + mousePt.x, delta.y + mousePt.y);
            
            selected.setLocation(p);
            
        } else if(moveScreen) {
        	GraphPanel d = (GraphPanel) e.getComponent();
        	
        	Point mousePt = e.getPoint();
        	Point difference = new Point(mousePt.x - delta.x, mousePt.y - delta.y);
        	        	
        	int h = d.getScrollPane().getHorizontalScrollBar().getValue();
        	int v = d.getScrollPane().getVerticalScrollBar().getValue();

        	h = h + difference.x / 2;
        	v = v + difference.y / 2;
        	
        	d.getScrollPane().getHorizontalScrollBar().setValue(h);
        	d.getScrollPane().getVerticalScrollBar().setValue(v);
        	
        	delta = mousePt;
        }
        
        addEdge = false;
        e.getComponent().repaint();
    }
    
    public MouseHandler mouseHandler = new MouseHandler();
    private class MouseHandler extends MouseAdapter {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		if(e.getClickCount() == 2 &&    e.getModifiers() != MouseEvent.BUTTON3_MASK) {
    			GraphPanel d = ((GraphPanel)e.getComponent());
                                
                State s = d.getSelectedState();
                Transition edg = d.getSelectedTransition();
                
                if(s != null) { //Selected is a State
                	s.edit();
                } else if(edg != null) { //Selected is an Edge
                	edg.edit();
                }
    		}
    		
    	};
    	
    	@Override
        public void mouseReleased(MouseEvent e) {
    		if(selecting) {
    			((GraphPanel) e.getComponent()).getUndoManager().register();
    		}
    		
            selecting = false;
            moveScreen = false;
            
            delta = new Point();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            GraphPanel d = ((GraphPanel)e.getComponent());
            d.requestFocusInWindow();
            
            Point mousePtUn = e.getPoint(); //untranslated
            Point mousePt = d.translatePoint(mousePtUn);
            State s = d.getStateByLocation(mousePt);
            
            d.setSelectedState(null);
            d.setSelectedTransition(null);

            if(s == null) {
            	Transition edg = d.getTransitionByLocation(mousePt);
            	if(edg != null) {
            		d.setSelectedTransition(edg);
            	}
            	
            	if (edg == null &&  e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            		mousePopupPoint = mousePt;
            		d.getFreePopupMenu().show(d, mousePtUn.x, mousePtUn.y);
            	}
            	
            	else if(edg != null &&  e.getModifiers() == MouseEvent.BUTTON3_MASK) {
	            	mousePopupPoint = mousePt;
	            	
	            	d.getTransitionPopupMenu().show(d, mousePtUn.x, mousePtUn.y, edg);
            	}
            	
            	else if( e.getModifiers() != MouseEvent.BUTTON3_MASK) {
            		moveScreen = true;
            		delta.setLocation(mousePt.x, mousePt.y);
            		
            		if(e.isShiftDown()) {
            			if(edg != null) {
	            			String text = edg.getVariableName(d.getStructureName());
	            			try {
	            				d.getEshmun().insertVariableIntoStructure(text);
	            			} catch(Exception ex) {
	            				
	            			}
            			}
            		}
            	}
            	
            	d.repaint();
            	return;
            } 
            
            d.setSelectedState(s);
            
            if ( e.getModifiers() == MouseEvent.BUTTON3_MASK) {
        		mousePopupPoint = mousePt;
        		
        		d.getStatePopupMenu().show(d, mousePtUn.x, mousePtUn.y, s);
        		d.repaint();
        		
        		return;
        	} else if(s != null) {
            	if(e.isShiftDown()) {
        			String text = s.getVariableName(d.getStructureName());
        			try {
        				d.getEshmun().insertVariableIntoStructure(text);
        			} catch(Exception ex) {
        				
        			}
        		}
            }
	            
            if(addEdge) {
            	d.getStatePopupMenu().setState(s);
            	addEdge = false;
            	
            	d.repaint();
            	return;
            }
            
            delta.setLocation(s.getLocation().x - mousePt.x, s.getLocation().y - mousePt.y);            
            
            selected = s;
            selecting = true;

            d.repaint();
        }
    }
    
    public Point getMousePopupPoint() {
    	return mousePopupPoint;
    }
}
