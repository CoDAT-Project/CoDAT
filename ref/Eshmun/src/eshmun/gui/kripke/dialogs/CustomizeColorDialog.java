package eshmun.gui.kripke.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eshmun.Eshmun;

/**
 * A Dialog that allows the user to pick the color for each process and actions.
 * @author kinan
 */
public class CustomizeColorDialog extends JDialog {

	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = 8064145060207248743L;
	
	/**
	 * The calling Eshmun instance.
	 */
	private Eshmun eshmun;
	
	/**
	 * A Mapping between processes and their color, if a process color is unchanged
	 * it wont exist in the map.
	 */
	private HashMap<String, Color> selectionToColor = new HashMap<>();
	
	/**
	 * The currently chosen process/action.
	 */
	private String selection = null;
	
	/**
	 * The color chooser.
	 */
	private JColorChooser colorChooser;
	
	/**
	 * The list of process names.
	 */
	private JList<String> selectionList;
	
	/**
	 * The position where the process name label is in the list.
	 */
	private int startProcessIndex = -1;
	
	/**
	 * The position where the action name label is in the list.
	 */
	private int startActionIndex = -1;
	
	/**
	 * Creates a new modal dialog for picking the processes colors.
	 * @param eshmun the calling Eshmun frame instance.
	 */
	public CustomizeColorDialog(Eshmun eshmun) {
		super(eshmun, "Processes Colors", true);
		this.eshmun = eshmun;
		
		// Constant dimensions.
		final int frameWidth = 850;
		final int frameHeight = 700;
		final int processListWidth = 130;
		final int controlPanelHeight = 60;
		
		// Dialog Format
		setSize(frameWidth, frameHeight + 20);
		setResizable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// Setup panels.
		JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		content.setPreferredSize(new Dimension(frameWidth - 10, frameHeight - controlPanelHeight - 10 ));
		control.setPreferredSize(new Dimension(frameWidth - 10, controlPanelHeight));
		
		getContentPane().add(content);
		getContentPane().add(control);
		
		// Process List
		ArrayList<String> processes = new ArrayList<String>(eshmun.getColoredProcessNames());
		Collections.sort(processes, new Comparator<String>() { // Compare by Shortlex ordering.
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.trim(); o2 = o2.trim();
				if(o1.length() == o2.length())
					return o1.compareTo(o2);
				
				return o1.length() - o2.length();
			}
		}); // sort alphabetically.
		
		// Action List
		ArrayList<String> actions = new ArrayList<String>(eshmun.getGraphPanelContainer().getAllActionNames());
		Collections.sort(actions, new Comparator<String>() { // Compare by Shortlex ordering.
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.trim(); o2 = o2.trim();
				if(o1.length() == o2.length())
					return o1.compareTo(o2);
				
				return o1.length() - o2.length();
			}
		}); // sort alphabetically.
		
		DefaultListModel<String> model = new DefaultListModel<>();
		if(processes.size() > 0) {
			startProcessIndex = 0;
			model.addElement("Process Name");
			for(String p : processes) if(!p.trim().isEmpty()) model.addElement(p);
		}
		
		if(actions.size() > 0) {
			startActionIndex = processes.size();
			model.addElement("Action Name");
			for(String a : actions) if(!a.trim().isEmpty()) model.addElement(a);
		}
		
		selectionList = new JList<>();
		selectionList.setModel(model);
		selectionList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getFirstIndex() == startProcessIndex || e.getFirstIndex() == startActionIndex)
					selectionChanged(null); //Label selected
				
				else if(startActionIndex > -1 && e.getFirstIndex() > startActionIndex) //Action selected
					selectionChanged("@@"+selectionList.getSelectedValue()+"@@");
				
				else //Process selected
					selectionChanged(selectionList.getSelectedValue());
			}
		});
		
		JScrollPane scroll = new JScrollPane(selectionList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(processListWidth, frameHeight - controlPanelHeight - 25));
		content.add(scroll);
		
		// Color Chooser
		colorChooser = new JColorChooser();
		colorChooser.setPreferredSize(new Dimension(frameWidth - processListWidth - 20, frameHeight - controlPanelHeight - 25));
		content.add(colorChooser);
		
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				colorChanged(colorChooser.getColor());				
			}
		});
		
		// Controls
		JButton cancel = new JButton("Cancel");
		JButton ok = new JButton(" OK ");
		
		control.add(cancel);
		control.add(ok);
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectionToColor.clear();
				dispose();
				setVisible(false);
			}
		});
	}
	
	/**
	 * Called when the user chooses a process/action in the list.
	 * @param selection the process/action name that was chosen.
	 */
	private void selectionChanged(String selection) {
		this.selection = selection;
		if(selection == null) return;
		
		Color color = selectionToColor.get(selection);
		if(color != null)
			colorChooser.setColor(color);
		else
			colorChooser.setColor(eshmun.getProcessColor(selection));
	}
	
	/**
	 * Called when the user chooses a color.
	 * @param color the chosen color.
	 */
	private void colorChanged(Color color) {
		if(this.selection == null) return;
		selectionToColor.put(selection, color);
	}
	
	/**
	 * @return A mapping between processes/actions and their chosen colors (unchanged processes will not be included).
	 */
	public HashMap<String, Color> getColors() {
		return selectionToColor;
	}
}
