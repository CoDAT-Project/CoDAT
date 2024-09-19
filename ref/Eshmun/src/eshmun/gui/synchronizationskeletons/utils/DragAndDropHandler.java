package eshmun.gui.synchronizationskeletons.utils;

import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class DragAndDropHandler extends MouseMotionAdapter {
	SkeletonState selected = null;
    Point delta = new Point();
    boolean selecting = false;
    boolean moveScreen = false;
    
    Point mousePopupPoint;
    public boolean addEdge;
    
    Point lastPoint;

    @Override
    public void mouseDragged(MouseEvent e) {
        if(selecting) { //selecting
        	SynchronizationSkeletonPanel d = (SynchronizationSkeletonPanel) e.getComponent();
            
        	Point mousePt = d.translatePoint(e.getPoint());
            Point p = new Point(delta.x + mousePt.x, delta.y + mousePt.y);
            
            selected.setLocation(p);
        } else if(moveScreen) {
        	SynchronizationSkeletonPanel d = (SynchronizationSkeletonPanel) e.getComponent();
        	
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
    		if(e.getClickCount() == 2 && !e.isPopupTrigger()) {
    			SynchronizationSkeletonPanel d = ((SynchronizationSkeletonPanel)e.getComponent());
                                
                SkeletonState s = d.getSelectedState();
                SkeletonTransition edg = d.getSelectedTransition();
                
                if(s != null) { //Selected is a State
                	String tmp = s.getLabel();
                	if(s.edit()) {
                		if(d.checkLegalLabel(s)) {
	                		d.getUndoManager().register();
	                		d.repaint();
                		} else {
                			s.setLabel(tmp);
                		}
                	}
                } else if(edg != null) { //Selected is an Edge
                	if(edg.edit()) {
                		d.getUndoManager().register();
                		d.repaint();
                	}
                }
    		}
    		
    	};
    	
    	@Override
        public void mouseReleased(MouseEvent e) {
    		if(selecting) {
    			((SynchronizationSkeletonPanel) e.getComponent()).getUndoManager().register();
    		}
    		
            selecting = false;
            moveScreen = false;
            
            delta = new Point();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            SynchronizationSkeletonPanel d = ((SynchronizationSkeletonPanel) e.getComponent());
            d.requestFocusInWindow();
            
            Point mousePtUn = e.getPoint(); //untranslated
            Point mousePt = d.translatePoint(mousePtUn);
            SkeletonState s = d.getStateByLocation(mousePt);
            
            d.setSelectedState(null);
            d.setSelectedTransition(null);

            if(s == null) {
            	SkeletonTransition edg = d.getTransitionByLocation(mousePt);
            	if(edg != null) {
            		d.setSelectedTransition(edg);
            	}
            	
            	if (edg == null && e.isPopupTrigger()) {
            		mousePopupPoint = mousePt;
            		d.getFreePopupMenu().show(d, mousePtUn.x, mousePtUn.y);
            	}
            	
            	else if(edg != null && e.isPopupTrigger()) {
	            	mousePopupPoint = mousePt;
	            	
	            	d.getTransitionPopupMenu().show(d, mousePtUn.x, mousePtUn.y, edg);
            	}
            	
            	else if(!e.isPopupTrigger()) {
            		moveScreen = true;
            		delta.setLocation(mousePt.x, mousePt.y);
            	}
            	
            	d.repaint();
            	return;
            } 
            
            d.setSelectedState(s);
            
            if (e.isPopupTrigger()) {
        		mousePopupPoint = mousePt;
        		
        		d.getStatePopupMenu().show(d, mousePtUn.x, mousePtUn.y, s);
        		d.repaint();
        		
        		return;
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
