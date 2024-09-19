package eshmun.gui.synchronizationskeletons.bars;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eshmun.gui.kripke.utils.CallBack;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonPanel;

public class SynchronizationZoomToolBar extends JToolBar implements CallBack {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 5272348760566871364L;

	private final int start = -5;
	private final int end = 5;
	private final int step = 1;
	private final double scaleStepOut = 0.15;
	private final double scaleStepIn = 0.22;
	
	private int count;
	private int identity; //no scale
	private int identityPosition;
	
	private SynchronizationSkeletonPanel skeleton;
	private JTextField zoomText;
	public void init() {
		count = 0;
		for(int s = start; s <= end; s += step) {
			count++;
		}
		
		identity = (start + end) / 2;
		identityPosition = count / 2;
	}
	
	public SynchronizationZoomToolBar(SynchronizationSkeletonPanel skeletonInstance) {
		super("Zoom Tool Bar", JToolBar.HORIZONTAL);
		
		skeleton = skeletonInstance;
		skeleton.setZoomToolbar(this);
		
		init(); //init count, identity, etc..
		
		//GENERAL FORMATTING
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		//ZOOM SECTION		
		JLabel zoomLabel = new JLabel(" Zoom:");
		JSlider slider = new JSlider(JSlider.HORIZONTAL, start, end, step);
		zoomText = new JTextField("100%", 5);
		
		slider.setValue(0);
		slider.setPreferredSize(new Dimension(300, 15));
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int value = ((JSlider) arg0.getSource()).getValue();
				int valuePosition = Math.abs(value - start) / step;
				
				double scale = 1;
				if(value < identity) {
					scale = 1 - (identityPosition - valuePosition) * scaleStepOut;
				} else if(value > identity) {
					scale = 1 + (valuePosition - identityPosition) * scaleStepIn;
				}
				
				skeleton.applyScale(scale);
			}
		});
		
		zoomText.addFocusListener(new FocusListener() {
			double lastValue = 100;
			@Override
			public void focusLost(FocusEvent arg0) {
				String text = ((JTextField) arg0.getSource()).getText();
				text.trim();
				if(text.endsWith("%")) {
					text = text.substring(0, text.length() - 1);
				}
				
				try {
					lastValue = Double.parseDouble(text);
					skeleton.applyScale(lastValue / 100);
				} catch(Exception e) {
					Toolkit.getDefaultToolkit().beep();
				}
				
				((JTextField) arg0.getSource()).setText((int)(lastValue)+"%");
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				String text = ((JTextField) arg0.getSource()).getText();
				text = text.substring(0, text.length() - 1);
				
				try {
					lastValue = Double.parseDouble(text);
				} catch(Exception e) {
					lastValue = 100;
					((JTextField) arg0.getSource()).setText("100%");
				}
				
			}
		});
		
		zoomText.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.requestFocusInWindow();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
				
		add(zoomLabel);
		add(slider);
		add(zoomText);

	}
	
	public void callBack(Object[] params) {
		double scale = (Double) params[0];
		
		scale *= 100;
		zoomText.setText(((int) scale)+"%");
	}
}
