package eshmun.gui.kripke;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import eshmun.Eshmun;

/**
 * Factory Pattern, creates a new GraphPanel and puts it in a container.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class GraphPanelFactory {
	/**
	 * the type of the structure that the GraphPanel should support.
	 */
	private StructureType type;
	
	/**
	 * the current instance of the application.
	 */
	private Eshmun eshmun;
	
	/**
	 * Create a new GraphPanelFactory for the given type of structure.
	 * 
	 * @param type the type of structure.
	 * @param eshmun the current instance of the application's frame.
	 * @throws NullPointerException if the specified type is null.
	 */
	public GraphPanelFactory(StructureType type, Eshmun eshmun) throws NullPointerException {
		if(type == null || eshmun == null)
			throw new NullPointerException();
		
		this.eshmun = eshmun;
		this.type = type;
	}
	
	/**
	 * Create a new GraphPanel and place it in a container, the GraphPanel 
	 * and its container are appropriate to the specified type, the Container 
	 * will be of the given size.
	 * 
	 * @param width width of container in pixels.
	 * @param height height of container in pixels.
	 * @return an appropriate GraphPanelContainer with an appropriate 
	 * GraphPanel inside it.
	 */
	public GraphPanelContainer makeGraphPanel(int width, int height) {		
		if(type == StructureType.Kripke) {
			JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			scroll.setAutoscrolls(true);	
			scroll.getHorizontalScrollBar().setUnitIncrement(50);
			scroll.getVerticalScrollBar().setUnitIncrement(50);
			scroll.setBorder(new LineBorder(Color.BLACK, 1));
					
			GraphPanel panel = new GraphPanel(scroll, eshmun);
			panel.setStructureName("Kripke Structure (1, 2)");
			
			scroll.setViewportView(panel);
			scroll.setPreferredSize(new Dimension(width, height));
			
			return new KripkeGraphPanelContainer(scroll, panel, eshmun);
			
		} else if(type == StructureType.MultiKripke) {
			
			return new MultiKripkeGraphPanelContainer(eshmun, width, height);
			
		}else if(type == StructureType.InfiniteStateKripke) {
			JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			scroll.setAutoscrolls(true);	
			scroll.getHorizontalScrollBar().setUnitIncrement(50);
			scroll.getVerticalScrollBar().setUnitIncrement(50);
			scroll.setBorder(new LineBorder(Color.BLACK, 1));
					
			GraphPanel panel = new GraphPanel(scroll, eshmun);
			panel.setStructureName("Kripke Structure (1, 2)");
			
			scroll.setViewportView(panel);
			scroll.setPreferredSize(new Dimension(width, height));
		 
			return new InfiniteStateKripkeGraphPanelContainer(scroll, panel, eshmun);
		}
		
		System.exit(0);
		return null;
	}
}
