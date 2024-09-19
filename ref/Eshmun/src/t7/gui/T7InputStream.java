package t7.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextArea;

public class T7InputStream extends InputStream {
	private JTextArea inputArea;
	private String buffer;
	private int index;
	
	public T7InputStream(final JTextArea inputArea) {
		super();
		
		this.inputArea = inputArea;
		this.buffer = null;
		this.index = 0;
		this.inputArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER || System.lineSeparator().equals(e.getKeyChar()+"")) {
					registerLine(((JTextArea) e.getSource()).getText());
					((JTextArea) e.getSource()).setText("");
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) { }
		});
	}

	@Override
	public int read() throws IOException {
		if((buffer == null || index >= buffer.length()) && inputArea == null) return -1;
		block();
		if((buffer == null || index >= buffer.length()) && inputArea == null) return -1;

		int c;
		synchronized (buffer) {
			c = buffer.charAt(index);
			index++;
		}
		
		return c;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int count = 0;
		for(; count < len; count++) {
			int r = read();
			
			if(r == -1 && count == 0) return -1;
			else if(r == -1) return count;
			
			b[off + count] = (byte) r;
			char c = (char) r;
			if(System.lineSeparator().equals(c+"")) return count+1;
		}
		
		return count;
	}
	
	private synchronized void block() {
		while(buffer == null || index >= buffer.length()) {
			try {
				wait();
				if(inputArea == null) return;
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public synchronized void registerLine(String line) {
		if(buffer == null) {
			buffer = line;
			index = 0;
		} else {
			synchronized (buffer) {
				buffer += line;
			}
		}
		
		notify();
	}
	
	@Override
	public int available() throws IOException {
		return inputArea == null ? 0 : (buffer != null ? buffer.length() : 1);
	}
	
	public synchronized void end() {
		inputArea = null;
		notify();
	}
	
	@Override
	public void close() throws IOException {
		end();
		super.close();
	}
}
