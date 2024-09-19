package eshmun.gui.kripke; 

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.visitor.visitors.EvaluaterVisitor;
import eshmun.gui.kripke.dialogs.StructureDialog;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.utils.models.multikripke.MultiSaveObject;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.MultiKripkeStructure;

/**
 * GraphPanel Container for a single Kripke structure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class MultiKripkeGraphPanelContainer extends JTabbedPane implements GraphPanelContainer {
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = 1533296082796504629L;
		
	/**
	 * The instance of the application.
	 */
	private Eshmun eshmun;
	
	/**
	 * The containers for each Kripke structure inside this multi-Kripke structure.
	 */
	private ArrayList<GraphPanelContainer> containers;
	
	/**
	 * This JTabbedPane, for accessing it within other scopes.
	 */
	private JTabbedPane This;
	
	/**
	 * The specification for each structure.
	 */
	private ArrayList<String> specifications;
	
	/**
	 * The width of this container.
	 */
	private int width;
	
	/**
	 * The height of this container.
	 */
	private int height;
	
	/**
	 * Stores the synch formula temporary in case of abstraction for later checking.
	 */
	private AbstractExpression tmpSynch;
	
	/**
	 * Create a new Container for a multi kripke structure.
	 * @param eshmun the original enclosing eshmun frame
	 * @param width the width of the panel.
	 * @param height the height of the panel.
	 */
	public MultiKripkeGraphPanelContainer(Eshmun eshmun, int width, int height) {
		this(eshmun, width, height, new GraphPanelFactory(StructureType.Kripke, eshmun).makeGraphPanel(width, height - 50));
	}
	
	/**
	 * Create a new Container for a multi kripke structure with the given containers.
	 * @param eshmun the original enclosing eshmun frame
	 * @param width the width of the panel.
	 * @param height the height of the panel.
	 * @param containers the containers for the kripke structures inside this structure.
	 */
	public MultiKripkeGraphPanelContainer(Eshmun eshmun, int width, int height, GraphPanelContainer...containers) {
		this.eshmun = eshmun;
		
		this.width = width;
		this.height = height;
		
		This = this;
		
		this.specifications = new ArrayList<String>();
		this.containers = new ArrayList<GraphPanelContainer>();
		
		for(GraphPanelContainer container : containers) {
			addTab(container, "");
		}
	}
		
	/**
	 * Add a new tab (GraphPanelContainer) to this JTabbedPane.
	 * @param container the container to be added.
	 * @param specs the specifications for the structure in this tab.
	 */
	public void addTab(GraphPanelContainer container, String specs) {	
		this.specifications.add(specs);		
		this.containers.add(container);
		
		eshmun.setSpecificationFormula(specs);
		
		JComponent comp = container.getGraphPanelComponent();
		add(comp);
		
		int index = indexOfComponent(comp);
		
		JLabel label = new JLabel(container.getCurrentGraphPanel().getStructureName());
		setTabComponentAt(index, label);
		
		label.setComponentPopupMenu(new TabPopupMenu(container));
		label.addMouseListener(new MouseListener(container));
		setSelectedIndex(index);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getGraphPanelComponent() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphPanel getCurrentGraphPanel() {
		Component comp = getSelectedComponent();
		for(GraphPanelContainer container : containers) {
			if(comp == container.getGraphPanelComponent())
				return container.getCurrentGraphPanel();
		}
		
		return null;
	}
		
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the save-able was not of type MultiKripke.
	 */
	@Override
	public void load(Saveable saveable) throws IllegalArgumentException {
		if(saveable.getStructureType() != StructureType.MultiKripke) {
			throw new IllegalArgumentException();
		}
		
		removeAll();
		
		this.specifications = new ArrayList<String>();
		this.containers = new ArrayList<GraphPanelContainer>();
				
		MultiSaveObject mObj = (MultiSaveObject) saveable;
		GraphPanelFactory factory = new GraphPanelFactory(StructureType.Kripke, eshmun);
		for(SaveObject obj : mObj.getSaveObjects()) {
			GraphPanelContainer container = factory.makeGraphPanel(width, height - 50);
			container.load(obj);
			
			addTab(container, obj.getSpecifications());
		}
		
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SaveObject[] getSaveObjects() {
		SaveObject[] objs = new SaveObject[containers.size()];
		
		for(int i = 0; i < containers.size(); i++) {
			GraphPanelContainer container = containers.get(i);
			objs[i] = container.getCurrentGraphPanel().getSaveObject(specifications.get(i), eshmun.getStructureFormula());
		}
		
		return objs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String stringRepresentation() {
		String strs = "";
		
		for(int i = 0; i < containers.size(); i++) {
			strs += containers.get(i).stringRepresentation();
			
			if(i < containers.size() - 1)
				strs += ";";
		}
		
		return strs;
	}
	
	/**
	 * {@inheritDoc}
	 * @return StructureType.MultiKripke .
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.MultiKripke;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String constructDefinition() {
		String representation = "***MULTI***" + System.lineSeparator();
		for(int i = 0; i < containers.size(); i++) {
			GraphPanelContainer container = containers.get(i);
			if(container instanceof KripkeGraphPanelContainer) {
				representation += ((KripkeGraphPanelContainer) container).constructDefinition(specifications.get(i));			
			} else {
				System.out.println("Illegal nesting of structures");
				continue;
			}
			representation += "***END***" + System.lineSeparator();
		}
		
		return representation;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the type is not Multi Kripke.
	 * @return null, since the specifications are already added in their appropriate positions.
	 */
	@Override
	public String loadDefinition(String definition, StructureType structureType) throws IllegalArgumentException {
		if(structureType != StructureType.MultiKripke)
			throw new IllegalArgumentException();
		
		Scanner scan = new Scanner(definition);
		String tmp = scan.nextLine();
		if(!tmp.equals("***MULTI***")) {
			scan.close();
			throw new IllegalArgumentException();
		}
		
		removeAll();
		this.containers = new ArrayList<GraphPanelContainer>();
		this.specifications = new ArrayList<String>();
		
		GraphPanelFactory factory = new GraphPanelFactory(StructureType.Kripke, eshmun);
		while(scan.hasNextLine()) {
			String current = "";
			tmp = scan.nextLine();
			while(!tmp.equals("***END***") && !tmp.trim().equals("")) {
				current += tmp + System.lineSeparator();
				tmp = scan.nextLine();
			}
			
			GraphPanelContainer container = factory.makeGraphPanel(width, height - 50);
			String specs = container.loadDefinition(current, StructureType.Kripke);
			
			addTab(container, specs);
		}
		
		scan.close();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addStructure(String name) {
		GraphPanelFactory factory = new GraphPanelFactory(StructureType.Kripke, eshmun);
		GraphPanelContainer container = factory.makeGraphPanel(width, height - 50);
		container.getCurrentGraphPanel().setStructureName(name);
		
		addTab(container, "");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getStructureNames() {
		ArrayList<String> names = new ArrayList<>();
		for(GraphPanelContainer cont : containers)
			names.addAll(cont.getStructureNames());
		
		return names;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashSet<String> getAllActionNames() {
		HashSet<String> result = new HashSet<String>();
		for(GraphPanelContainer cont : containers)
			result.addAll(cont.getAllActionNames());
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Repairable constructStructure() {
		MultiKripkeStructure struct = new MultiKripkeStructure();
		for(GraphPanelContainer container : containers) {
			struct.add((KripkeStructure) container.constructStructure());
		}
		
		return struct;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Repairable constructCurrentStructure() {
		Component comp = getSelectedComponent();
		for(GraphPanelContainer container : containers) {
			if(comp == container.getGraphPanelComponent())
				return container.constructStructure();
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSpecifications() {
		specifications.set(getSelectedIndex(), eshmun.getSpecificationFormula());
		
		ArrayList<String> specs = new ArrayList<String>();
		for(int i = 0; i < containers.size(); i++) {
			GraphPanelContainer c = containers.get(i);
			specs.add(c.applyRenameToSpec(specifications.get(i)));
		}
		
		return specs;			
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpecifications(String specs) {
		specifications.set(getSelectedIndex(), specs);
	}

	/**
	 * {@inheritDoc}
	 * Does nothing, only meaningful in single KripkeGraphPanelContainer.
	 */
	@Override
	public String applyRenameToSpec(String specifications) {
		return specifications;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyDeletions(Collection<String> deletions) {
		restore();
		
		for(int i = 0; i < containers.size(); i++) {
			GraphPanelContainer container = containers.get(i);
			
			ArrayList<String> tmp = new ArrayList<String>();
			String name = container.getCurrentGraphPanel().getStructureName().trim();
			name = name.substring(0, name.indexOf("(")).trim();
			for(String d : deletions) {
				String dd = d.trim().substring(2);
				if(dd.startsWith(name))
					tmp.add(d);
			}
			
			deletions.removeAll(tmp);
			container.applyDeletions(tmp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyUnsatCore(Collection<String> unsatCore) {
		restore();
		
		for(int i = 0; i < containers.size(); i++) {
			GraphPanelContainer container = containers.get(i);
			
			ArrayList<String> tmp = new ArrayList<String>();
			String name = container.getCurrentGraphPanel().getStructureName().trim();
			name = name.substring(0, name.indexOf("(")).trim();
			for(String d : unsatCore) {
				String dd = d.trim().substring(2);
				if(dd.startsWith(name))
					tmp.add(d);
			}
			
			unsatCore.removeAll(tmp);
			container.applyUnsatCore(tmp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyStateByState(Collection<String> stateByState) {
		Component comp = getSelectedComponent();
		for(GraphPanelContainer container : containers) {
			if(comp == container.getGraphPanelComponent())
				container.applyStateByState(stateByState);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore() {
		for(GraphPanelContainer container : containers) {
			container.getCurrentGraphPanel().restore();
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteDashed() {
		for(GraphPanelContainer container : containers) {
			container.getCurrentGraphPanel().deleteDashed();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		
		if(index < specifications.size()) {
			eshmun.setSpecificationFormula(specifications.get(index));
		} else {
			eshmun.setSpecificationFormula("");
		}
	}
			
	/**
	 * Handles traversing between tabs.
	 */
	private class MouseListener extends MouseAdapter {
		/**
		 * The ID of the related tab
		 */
		private GraphPanelContainer element;
		
		/**
		 * Create a new MouseListener for the tab specified with its element.
		 * @param element The element this relates to.
		 */
		public MouseListener(GraphPanelContainer element) {
			this.element = element;
		}
		
		/**
		 * Handles mouse click.
		 * @param e the event related to the mouse click.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {			
			int clickIndex = -1;
			for(int i = 0; i < containers.size(); i++) { 
				if(element == containers.get(i)) {
					clickIndex = i; 
					break;
				}
			}
			
			if(clickIndex == -1) return;
			setSelectedIndex(clickIndex);
		}
	}
	
	/**
	 * Handles displaying a popup menu when the tab is clicked for renaming and removing.
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	private class TabPopupMenu extends JPopupMenu {
		/**
		 * Auto generated serial UID
		 */
		private static final long serialVersionUID = 7068476760102134104L;
		
		/**
		 * The ID of the related tab
		 */
		private GraphPanelContainer container;
		
		/**
		 * Create a new Popupmenu for the tab specified with its element.
		 * @param element The related element.
		 */
		public TabPopupMenu(GraphPanelContainer element) {
			super();
			
			this.container = element;
			
			JMenuItem add = new JMenuItem("Add");
			JMenuItem edit = new JMenuItem("Edit");
			JMenuItem remove = new JMenuItem("Remove");
			
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					StructureDialog dialog = new StructureDialog(eshmun, "", "");
					dialog.setVisible(true);
					
					if(dialog.isSuccessful()) {
						String name = dialog.getName().trim() + " ("+dialog.getProcesses()+")";
						addStructure(name);
					}
				}
			});
			
			edit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GraphPanel graphPanel = container.getCurrentGraphPanel();
					String name = graphPanel.getStructureName();
					String processes = name.contains("(") ? name.substring(name.indexOf("(") + 1, name.indexOf(")")).trim() : "";
					name = name.contains("(") ? name.substring(0, name.indexOf("(")).trim() : name;
					
					StructureDialog diag = new StructureDialog(eshmun, name, processes);
					diag.setVisible(true);
					if(diag.isSuccessful()) {
						name = diag.getName();
						processes = diag.getProcesses();
						
						name = name +" (" + processes + ")";
						graphPanel.setStructureName(name);
						
						int index = indexOfComponent(container.getGraphPanelComponent());
						((JLabel) getTabComponentAt(index)).setText(name);
					}
					
					repaint();
				}
			});
			
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String currentSpec = eshmun.getSpecificationFormula();
					specifications.set(getSelectedIndex(), currentSpec);
					
					int index = -1;
					for(int i = 0; i < containers.size(); i++) { 
						if(container == containers.get(i)) {
							index = i; 
							break;
						}
					}
					
					if(index == -1) return;
					
					containers.remove(index);
					specifications.remove(index);
					
					This.remove(container.getGraphPanelComponent());
					
					repaint();
					
					eshmun.setSpecificationFormula(specifications.get(getSelectedIndex()));
				}
			});
			
			add(add);
			add(edit);
			add(remove);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByLabel(String specifications, Collection<String> allVars) {
		tmpSynch = ((MultiKripkeStructure) constructStructure()).processSynchronizationFormula();

		List<String> specsList = getSpecifications();
		for(GraphPanelContainer c : containers) {
			int index = indexOfComponent(c.getGraphPanelComponent());
			c.abstractByLabel(specsList.get(index), allVars);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByFormula(String specifications) {
		tmpSynch = ((MultiKripkeStructure) constructStructure()).processSynchronizationFormula();

		List<String> specsList = getSpecifications();
		for(GraphPanelContainer c : containers) {
			int index = indexOfComponent(c.getGraphPanelComponent());
			c.abstractByFormula(specsList.get(index));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abstractByCTLFormulae(ArrayList<String> specifications) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> concretize(String specifications) {
		boolean tmp = false;
		for(GraphPanelContainer c : containers) {
			if(c instanceof KripkeGraphPanelContainer) {
				tmp = ((KripkeGraphPanelContainer) c).abstractedByFormula;
				((KripkeGraphPanelContainer) c).abstractedByFormula = false;
			}
		}
		
		HashSet<String> allDeletions = new HashSet<String>();
		
		List<String> specsList = getSpecifications();
		for(GraphPanelContainer c : containers) {
			
			int index = indexOfComponent(c.getGraphPanelComponent());
			Collection<String> deletions = c.concretize(specsList.get(index));
			if(deletions == null) {
				return null;
			}
			
			allDeletions.addAll(deletions);
		}
		
		EvaluaterVisitor ev = new EvaluaterVisitor(allDeletions, false);
		if(ev.run(tmpSynch)) {
			eshmun.setSpecificationFormula(this.specifications.get(getSelectedIndex()));
			return allDeletions;
		}
		
		for(GraphPanelContainer c : containers) {
			if(c instanceof KripkeGraphPanelContainer)
				((KripkeGraphPanelContainer) c).abstractedByFormula = tmp;
		}
		
		eshmun.setSpecificationFormula(this.specifications.get(getSelectedIndex()));
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		for(GraphPanelContainer c : containers) {
			c.rollback();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register() {
		for(GraphPanelContainer c : containers) {
			c.register();
		}
	}
	
	@Override
	public void resetAbstraction() {
		for(GraphPanelContainer c : containers) {
			c.resetAbstraction();
		}
	}
}