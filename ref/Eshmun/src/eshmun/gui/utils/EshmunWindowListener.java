package eshmun.gui.utils;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Listens for frame windows state changes.
 * This is used by all Eshmun JFrame to keep track of how many windows are currently open.
 * When a window is opened a counter is increased, when a window is closed the counter is decreased.
 * Works similarly to reference-count garbage collection, when the counter hits zero, Eshmun is closed.
 * @author kinan
 */
public class EshmunWindowListener implements WindowListener {
	/**
	 * Counts number of active windows.
	 */
	private static int WINDOW_COUNT = 0;
	
	/**
	 * Each instance of the listener is dedicated to just one frame.
	 * Lock tracks the behavior of that frame, if the frame goes through
	 * windowClosing to windowClosed, then this is due to a user interaction (Quit or X button).
	 * Otherwise, it is internal (opening a saved file, etc..). Lock is false when internal.
	 */
	private boolean lock = false;

	@Override
	public void windowActivated(WindowEvent e) { }

	/**
	 * Called when a window is closed (Either by user's direct actions or internal traversing).
	 * Never fully exists Eshmun, however if it is internal, it must decrement the counter for consistency.
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		synchronized(getClass()) {
			if(!lock) WINDOW_COUNT--;
		}
	}

	/**
	 * Called when a window is closed/closing because the user clicked the X button (and also quit).
	 * Can exist Eshmun if counter dropped to zero.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		synchronized(getClass()) {
			if(--WINDOW_COUNT == 0) System.exit(0);
			lock = true;
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) { }

	@Override
	public void windowDeiconified(WindowEvent e) { }

	@Override
	public void windowIconified(WindowEvent e) { }

	/**
	 * A new window showed up, increment counter.
	 */
	@Override
	public void windowOpened(WindowEvent e) { 
		synchronized(getClass()) {
			WINDOW_COUNT++; 
		}
	}

}
