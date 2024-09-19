package eshmun.gui.kripke.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import eshmun.Eshmun;
import eshmun.gui.kripke.utils.LogicTextPane;

public class FormulaPopupMenu extends JPopupMenu {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 8166191073847382662L;
	
	private LogicTextPane logicPane;
	private Eshmun eshmun;

	public FormulaPopupMenu(LogicTextPane logicPaneInstance, Eshmun eshmunInstance, String l) {
		super(l);
		
		logicPane = logicPaneInstance;
		eshmun = eshmunInstance;
				
		JMenuItem storeItem = new JMenuItem("Store Formula");
		JMenuItem loadItem = new JMenuItem("Load Formula");
		JMenuItem clearItem = new JMenuItem("Clear Formula");
		
		add(storeItem);
		add(loadItem);
		add(clearItem);
		
		storeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String formula = logicPane.getText();
				storeFormula(formula);
			}
		});
		
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String formula = loadFormula();
				
				if(formula != null) {
					logicPane.setText(formula);
				}
			}
		});
		
		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logicPane.setText("");
				logicPane.requestFocusInWindow();
			}
		});
	}
	
	public String loadFormula() {
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showOpenDialog(null);
		if(dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				FileInputStream fin = new FileInputStream(chooser.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);
				
				String sobj = (String) ois.readObject();
				
				ois.close();
				fin.close();
				
				eshmun.writeMessage("Formula Loaded!", false);
				
				return sobj;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Loading Formula: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Saved Formula has Unrecognizable Format: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			} catch (ClassCastException e2) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Saved Formula has Unrecognizable Format: "+e2.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}	
		
		return null;
	}
	
	public void storeFormula(String formula) {
		JFileChooser chooser = new JFileChooser(Eshmun.last_Directory);
		int dec = chooser.showSaveDialog(null);
		if(dec == JFileChooser.APPROVE_OPTION) {
			Eshmun.last_Directory = chooser.getSelectedFile().getParent();
			try {
				File f = chooser.getSelectedFile();
				if(f.exists())
					f.delete();
				
				f.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				oos.writeObject(formula);
				
				oos.close();
				fos.close();
				
				eshmun.writeMessage("Fromula Stored!", false);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Error Storing Formula: "+e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				eshmun.writeMessage("Error!", true);
			}
		} else if (dec == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this.getRootPane(), "Error Choosing File", "Error", JOptionPane.ERROR_MESSAGE);
			eshmun.writeMessage("Error!", true);
		}
	}
}
