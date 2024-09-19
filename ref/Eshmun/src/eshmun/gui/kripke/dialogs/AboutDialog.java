package eshmun.gui.kripke.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class AboutDialog extends JDialog {
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -1423848009169306135L;

	private final int width = 420;
	private final int height = 250;
	public AboutDialog() {
		super(null, "About Eshmun", JDialog.DEFAULT_MODALITY_TYPE);
		
		getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		
		setSize(width, height);
		setResizable(false);
		
		JLabel label = new JLabel("<html>&nbsp;<b>Eshmun Tool</b>"
				+ "<p>&nbsp;Paul C. Attie, Kinan Dak Al Bab, and Mohammad Sakr.</p><br>"
				+ "<p><b>&nbsp;eshmuntool@gmail.com</b></p></html>");
		label.setPreferredSize(new Dimension(width, height));
		label.setMaximumSize(new Dimension(width, height));
		label.setMinimumSize(new Dimension(width, height));
		
		add(label);
	}
}
