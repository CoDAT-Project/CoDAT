package eshmun.gui.kripke.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Dialog containing help. Taken from html files.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class HelpDialog extends JDialog {
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = -1423848009169306135L;

	private final int width = 1050;
	private final int height = 800;
	
	private JEditorPane helpPane;
	
	public HelpDialog() throws IOException {
		super(null, "Help", ModalityType.MODELESS);
		
		getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		
		setSize(width, height);
		setMaximumSize(new Dimension(width, height));
		setResizable(true);
		
		helpPane = new JEditorPane();
		helpPane.setContentType("text/html");
		helpPane.setEditable(false);
		helpPane.addHyperlinkListener(new LinkListener());
		
		String path = "/usermanual/index.html";
		helpPane.setPage(getClass().getResource(path));
		
		JScrollPane scroll = new JScrollPane(helpPane);
		scroll.setPreferredSize(new Dimension(width - 30, height - 30));
		
		add(scroll);
	}
	
	/**
	 * Handles clicking on a link inside help.
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	private class LinkListener implements HyperlinkListener {
		/**
		 * Gets called when a link is clicked.
		 * e the HyperlinkEvent fired by this action.
		 */
		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			HyperlinkEvent.EventType eventType = e.getEventType();
	        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
	            if (e instanceof HTMLFrameHyperlinkEvent) {
	                HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) e;
	                HTMLDocument document = (HTMLDocument) helpPane.getDocument();
	                document.processHTMLFrameHyperlinkEvent(linkEvent);
	            } else {
	            	try {
						helpPane.setPage(e.getURL());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Could not locate the required help file",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
	            }
	        }
		}
		 
	}
}
