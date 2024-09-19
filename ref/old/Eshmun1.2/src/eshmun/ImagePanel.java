package eshmun;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {  
	  
	  private Image img;  
	  
	  public ImagePanel(String img, Dimension size) {  
	    this(new ImageIcon(img).getImage(), size);  
	  }  
	  
	  public ImagePanel(Image img, Dimension size) {  
	    this.img = img;  
	    setPreferredSize(size);  
	    //setMinimumSize(size);  
	    //setMaximumSize(size);  
	    setSize(size);  
	    setLayout(null);  
	  }  
	  public ImagePanel(String img) { 
		  this.img = new ImageIcon(img).getImage();
		    Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null)); 
		    setPreferredSize(size);  
		    //setMinimumSize(size);  
		    //setMaximumSize(size);  
		    setSize(size);  
		    setLayout(null);  
		  }  
	  public void setImage(String img){
		  this.img = new ImageIcon(img).getImage();
		  Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null));
		  setPreferredSize(size);
		  setSize(size);
	  }
	  public void repaintPanel(){
		  this.repaint();
	  }
	  
	  
	  public void paintComponent(Graphics g) {  
	    g.drawImage(img, 0, 0, null);  
	  }  
	  
	}  