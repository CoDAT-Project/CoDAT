package eshmun.gui.kripke.bars;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import eshmun.Eshmun;
import eshmun.gui.kripke.GraphPanel;
import eshmun.gui.utils.models.vanillakripke.State;
public class EshmunSearchToolBar extends JToolBar {

	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -6759687059386296271L;
	
	private Eshmun eshmun;
	
	private JButton previousRepair;
	private JButton nextRepair;
		
	public EshmunSearchToolBar(Eshmun eshmunInstance) {
		super("Search Tool Bar", JToolBar.HORIZONTAL);
		
		this.eshmun = eshmunInstance;
		
		//GENERAL FORMATTING
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//SEARCH SECTION		
		JLabel searchLabel = new JLabel(" Search:");
		JTextField searchField = new JTextField("State Name", 20);
		searchField.setForeground(new Color(200, 200, 200));
		
		searchField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				GraphPanel graphPanel = eshmun.getCurrentGraphPanel();
				
				JTextField tf = (JTextField) e.getSource();
				String name = tf.getText().trim();
				
				if(name != "") {
					State s = graphPanel.getStateByName(name);
					if(s == null) {
						Toolkit.getDefaultToolkit().beep();
					} else {
						Point p = graphPanel.translatePointReverse(s.getLocation());
						
						graphPanel.setSelectedState(s);
						graphPanel.getScrollPane().getHorizontalScrollBar().setValue(p.x);
						graphPanel.getScrollPane().getVerticalScrollBar().setValue(p.y);
						graphPanel.repaint();
					}
				}
				
				
				tf.setForeground(new Color(200, 200, 200));
				tf.setText("State Name");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				JTextField tf = (JTextField) e.getSource();				
				
				tf.setForeground(Color.BLACK);
				tf.setText("");
			}
		});
		
		searchField.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eshmun.getCurrentGraphPanel().requestFocusInWindow();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
		
		add(searchLabel);
		add(searchField);
		add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html>"));
		
		previousRepair = new JButton("<");
		nextRepair = new JButton(">");
		
		previousRepair.setEnabled(false);
		nextRepair.setEnabled(false);
		
		add(previousRepair);
		add(nextRepair);
		
		previousRepair.setBackground(Color.WHITE);
		nextRepair.setBackground(Color.WHITE);
		
		previousRepair.setFont(new Font("serif", Font.BOLD, 14));
		nextRepair.setFont(new Font("serif", Font.BOLD, 14));
		
		previousRepair.setEnabled(false);
		nextRepair.setEnabled(true);
		
		nextRepair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flag = eshmun.nextRepair();
				
				nextRepair.setEnabled(flag);
				if(flag) {
					previousRepair.setEnabled(true);
				}
			}
		});
		
		previousRepair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flag = eshmun.previousRepair();
				
				previousRepair.setEnabled(flag);
				nextRepair.setEnabled(true);
			}
		});
	}
	
	public void resetRepairs(boolean next) {
		previousRepair.setEnabled(false);
		nextRepair.setEnabled(next);
	}
}
